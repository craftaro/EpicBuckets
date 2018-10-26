package com.songoda.epicbuckets.listeners;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.inventories.InventoryManager;
import com.songoda.epicbuckets.util.ChatUtil;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * InventoryClickListener created by: SoFocused
 * Date Created: oktober 03 2018
 * Time created: 21:15
 */
public class InventoryClickListener implements Listener {

    private EpicBuckets plugin = EpicBuckets.getInstance();

    private Map<UUID, String> shopMap = new WeakHashMap<>();

    // Main inventory

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        String inventoryTitle = ChatUtil.stripColor(plugin.getConfig().getString("MENU-ITEMS.inventory-name"));
        String clickedInventoryTitle = ChatColor.stripColor(event.getInventory().getTitle());

        if (!clickedInventoryTitle.equalsIgnoreCase(inventoryTitle))
            return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
            return;

        if (!event.getCurrentItem().hasItemMeta())
            return;

        event.setCancelled(true);

        // I'm guessing the fastest way to find it is to check if the
        // slot is in the config.yml itself?

        int slot = event.getSlot();

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        Player player = (Player) event.getWhoClicked();

        for (String key : config.getConfigurationSection("MENU-ITEMS").getKeys(false)) {

            // Check if the key has the item category if not just skip

            if (!config.contains("MENU-ITEMS." + key + ".slot"))
                continue;

            int configSlot = config.getInt("MENU-ITEMS." + key + ".slot");

            if (slot == configSlot) {

                // Now we know that the slot they clicked
                // was found in the config!

                String shopName = config.getString("MENU-ITEMS." + key + ".shop");

                InventoryManager inventoryManager = new InventoryManager();

                inventoryManager.loadSubInventory(player, shopName);

            }


        }

    }

    // Sub inventories

    @EventHandler
    public void onClickSubInventory(InventoryClickEvent event) {

        FileConfiguration config = plugin.shopFile.config;

        String clickedInventory = ChatUtil.stripColor(event.getInventory().getTitle());

        boolean foundInventory = false;

        String shopName = "";

        ConfigurationSection cs = config.getConfigurationSection("shops");

        if (cs != null) {
            for (String key : config.getConfigurationSection("shops").getKeys(false)) {

                String inventoryName = ChatUtil.stripColor(config.getString("shops." + key + ".inventory-name"));

                if (!clickedInventory.equalsIgnoreCase(inventoryName))
                    continue;

                foundInventory = true;

                shopName = key;

                break;
            }
        }

        if (!foundInventory || shopName.equalsIgnoreCase(""))
            return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        int goBackButton = config.getInt("shops." + shopName + ".goBackButton");

        boolean useBackButton = config.getBoolean("use-back-buttons");

        // Player clicked on the back button
        if (useBackButton && slot == goBackButton) {

            player.getOpenInventory().close();

            player.performCommand("genbucket");

            return;
        }

        // They did not click on the back button so we
        // have to check if they're trying to purchase
        // a genbucket

        // Loop thru all slots in the shops config to see
        // if it equals with a genbucket

        // Left click to buy or right click to open stacked shop

        for (String key : config.getConfigurationSection("shops." + shopName).getKeys(false)) {

            if (!config.contains("shops." + shopName + "." + key + ".slot"))
                continue;

            int configSlot = config.getInt("shops." + shopName + "." + key + ".slot");

            if (configSlot != slot)
                continue;


            // I can init & declare because I know I have the
            // necessary data

            GenbucketItem genbucketItem = new GenbucketItem(shopName, key);

            // Buy a singular genbucket
            if (event.getClick().equals(ClickType.LEFT)) {

                purchaseGenbucket(player, 1, genbucketItem);

            } else {

                // Open the multiple gui shop

                final boolean infinite_gens = plugin.getConfig().getBoolean("INFINITE-USE");

                if (infinite_gens) {
                    return;
                }

                shopMap.put(player.getUniqueId(), shopName + ":" + key);

                GenbucketManager genbucketManager = new GenbucketManager();

                genbucketManager.openBulkShop(player, genbucketItem);

            }

            break;

        }

    }

    @EventHandler
    public void onClickBulkShop(InventoryClickEvent event) {

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        String inventoryName = ChatUtil.stripColor(config.getString("BULK-SHOP-INVENTORY.inventory-name").replace("{player}", event.getWhoClicked().getName()));
        String clickedInventoryName = ChatUtil.stripColor(event.getInventory().getTitle());

        if (!inventoryName.equalsIgnoreCase(clickedInventoryName))
            return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
            return;

        if (!event.getCurrentItem().hasItemMeta())
            return;

        event.setCancelled(true);

        // Lets first check if they're trying to increase/decrease the amount

        Player player = (Player) event.getWhoClicked();

        // We have to case this to avoid NPE

        if (!shopMap.containsKey(player.getUniqueId())) {
            player.getOpenInventory().close();
            return;
        }

        int clickedSlot = event.getSlot();

        int returnBackItemSlot = config.getInt("BULK-SHOP-INVENTORY.return-back-slot");

        int purchaseItemSlot = config.getInt("BULK-SHOP-INVENTORY.purchase-item.slot");

        if (ArrayUtils.contains(modifySlotList(), clickedSlot)) {

            modifyValue(event.getInventory(), player, event.getCurrentItem());

        } else if (clickedSlot == returnBackItemSlot) {

            player.getOpenInventory().close();

            String[] genbucketDetails = shopMap.get(player.getUniqueId()).split(":");

            InventoryManager inventoryManager = new InventoryManager();

            inventoryManager.loadSubInventory(player, genbucketDetails[0]);

        } else if (clickedSlot == purchaseItemSlot) {

            String[] genbucketDetails = shopMap.get(player.getUniqueId()).split(":");

            GenbucketItem genbucketItem = new GenbucketItem(genbucketDetails[0], genbucketDetails[1]);

            purchaseGenbucket(player, GenbucketManager.getPlayerInteger(player), genbucketItem);
        }

    }

    @EventHandler
    public void onClickActGbInventory(InventoryClickEvent event) {


        if (!event.getInventory().getTitle().equalsIgnoreCase("Active genbuckets"))
            return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
            return;

        if (!event.getCurrentItem().hasItemMeta())
            return;

        event.setCancelled(true);

        if (event.getSlot() == 53) {

            InventoryManager inventoryManager = new InventoryManager();

            inventoryManager.loadNextInventory(event.getInventory());
            return;
        }

        UUID uuid = UUID.fromString(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

        Player player = (Player) event.getWhoClicked();

        ClickType clickType = event.getClick();

        if (clickType.equals(ClickType.LEFT)) {

            // Teleport

            player.teleport(GenbucketManager.activeGenbucketLocation.get(uuid).clone().add(0, 50, 0));
            player.sendMessage("§a§lTeleporting...");

        } else {

            GenbucketManager.activeGenbucketItems.remove(uuid);
            event.getInventory().clear(event.getSlot());
            player.sendMessage("§c§lStopping...");
        }

    }

    private void modifyValue(Inventory inventory, Player player, ItemStack itemStack) {

        boolean addition = ChatUtil.stripColor(itemStack.getItemMeta().getDisplayName().split(" ")[0]).equalsIgnoreCase("+");

        int value = Integer.parseInt(itemStack.getItemMeta().getDisplayName().split(" ")[1]);

        int oldAmount = GenbucketManager.getPlayerInteger(player);

        int newAmount;

        if (addition) {

            newAmount = oldAmount + value;

            if (newAmount > 64)
                newAmount = newAmount - (newAmount - 64);

            GenbucketManager.setPlayerInteger(player, newAmount);

        } else {

            newAmount = oldAmount - value;

            if (newAmount == 0)
                newAmount = 1;
            else if (newAmount < 0)
                newAmount = oldAmount - (oldAmount - 1);

            GenbucketManager.setPlayerInteger(player, newAmount);

        }

        FileConfiguration config = plugin.getConfig();

        int mainItemSlot = config.getInt("BULK-SHOP-INVENTORY.plugin-item-slot");

        inventory.getItem(mainItemSlot).setAmount(GenbucketManager.getPlayerInteger(player));

        player.updateInventory();

    }

    private int[] modifySlotList() {

        FileConfiguration config = plugin.getConfig();

        int[] modifyValueSlots = new int[6];

        for (int i = 0; i < config.getString("BULK-SHOP-INVENTORY.increase-item.slots").split(",").length; i++)
            modifyValueSlots[i] = Integer.parseInt(config.getString("BULK-SHOP-INVENTORY.increase-item.slots").split(",")[i]);

        int x = 3;

        for (int i = 0; i < config.getString("BULK-SHOP-INVENTORY.decrease-item.slots").split(",").length; i++) {

            if (modifyValueSlots[x] == 0)
                modifyValueSlots[x] = Integer.parseInt(config.getString("BULK-SHOP-INVENTORY.decrease-item.slots").split(",")[i]);

            x++;

        }

        return modifyValueSlots;

    }

    private void purchaseGenbucket(Player player, int amount, GenbucketItem genbucketItem) {

        if (player.getInventory().firstEmpty() == -1) {

            player.sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.purchase.inventoryfull"));

            return;
        }

        int price = genbucketItem.getPrice() * amount;

        double playerBalance = plugin.getBalance(player);

        final boolean infinite_gens = plugin.getConfig().getBoolean("INFINITE-USE");

        if (infinite_gens) {
            player.getInventory().addItem(genbucketItem.getGenbucketItem());
            return;
        }

        if (playerBalance >= price) {

            // they have enough money

            plugin.withdrawBalance(player, price, true);

            genbucketItem.setAmount(amount);

            player.getInventory().addItem(genbucketItem.getGenbucketItem());

        } else {

            // they have less money

            player.sendMessage(plugin.getLocale().getMessage("interface.withdrawl.success", String.valueOf((playerBalance - price) * -1)));

        }

        boolean closeGUI = plugin.getConfig().getBoolean("CLOSE-GUI-AFTER-PURCHASE");

        if (closeGUI)
            player.getOpenInventory().close();

    }


}
