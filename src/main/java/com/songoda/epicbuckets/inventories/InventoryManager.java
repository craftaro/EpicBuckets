package com.songoda.epicbuckets.inventories;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.InventoryUtil;
import com.songoda.epicbuckets.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * InventoryManager created by: SoFocused
 * Date Created: oktober 03 2018
 * Time created: 16:24
 */
public class InventoryManager {

    public void openMainInventory(Player player) {

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        boolean fillInventory = config.getBoolean("MENU-ITEMS.fill");

        int size = config.getInt("MENU-ITEMS.size");

        String inventoryName = ChatUtil.colorString(config.getString("MENU-ITEMS.inventory-name"));

        Inventory inventory = Bukkit.createInventory(null, size, inventoryName);

        InventoryUtil.fillInventory(inventory, fillInventory);

        for (String key : config.getConfigurationSection("MENU-ITEMS").getKeys(false)) {

            // Check if the key has the item category if not just skip

            if (!config.contains("MENU-ITEMS." + key + ".item"))
                continue;

            // Now we know that we might have a possible item!

            setBucketInMainGUI(inventory, "MENU-ITEMS.", key);

        }

        player.getOpenInventory().close();
        player.openInventory(inventory);

    }

    public void loadSubInventory(Player player, String shopName) {

        FileConfiguration config = EpicBuckets.getInstance().shopFile;

        String inventoryName = ChatUtil.colorString(config.getString("shops." + shopName + ".inventory-name"));

        int inventorySize = config.getInt("shops." + shopName + ".size");

        boolean fillInventory = config.getBoolean("shops." + shopName + ".fill");

        boolean useBackButton = config.getBoolean("use-back-buttons");

        int backButtonSlot = config.getInt("shops." + shopName + ".goBackButton");

        Inventory inventory = Bukkit.createInventory(null, inventorySize, inventoryName);

        InventoryUtil.fillInventory(inventory, fillInventory);

        InventoryUtil.setBackButton(inventory, backButtonSlot, useBackButton);

        // Load in the buckets

        for (String key : config.getConfigurationSection("shops." + shopName).getKeys(false)) {

            // Make sure our key / path has a type in it

            if (!config.contains("shops." + shopName + "." + key + ".type"))
                continue;

            // Now we can "safely" load in the genbucket

            setBucketInSubGUI(inventory, shopName, key);

        }

        player.getOpenInventory().close();
        player.openInventory(inventory);

    }

    private void setBucketInSubGUI(Inventory inventory, String shopName, String key) {

        FileConfiguration config = EpicBuckets.getInstance().shopFile;

        String name = ChatUtil.colorString(config.getString("shops." + shopName + "." + key + ".name"));

        int price = config.getInt("shops." + shopName + "." + key + ".price");

        Material icon = Material.valueOf(config.getString("shops." + shopName + "." + key + ".icon").toUpperCase());
        Material type = Material.valueOf(config.getString("shops." + shopName + "." + key + ".type").toUpperCase());

        List<String> lore = ChatUtil.colorList(config.getStringList("shops." + shopName + "." + key + ".description"), type, price);

        int damage = config.getInt("shops." + shopName + "." + key + ".damage");

        int slot = config.getInt("shops." + shopName + "." + key + ".slot");

        inventory.setItem(slot, ItemStackUtil.createItemStack(name, lore, icon, 1, damage, true));

    }

    private void setBucketInMainGUI(Inventory inventory, String path, String key) {

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        String itemName = config.getString(path + key + ".item.name");

        List<String> itemLore = config.getStringList(path + key + ".item.lore");

        Material m = Material.valueOf(config.getString(path + key + ".item.material").toUpperCase());

        int damage = config.getInt(path + key + ".item.damage");

        int slot = config.getInt(path + key + ".slot");

        ItemStack itemStack = ItemStackUtil.createItemStack(itemName, itemLore, m, 1, damage, true);

        inventory.setItem(slot, itemStack);

    }

    public void openActiveGenbuckets(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 54, "Active genbuckets");

        int i = 0;

        for (UUID uuid : GenbucketManager.activeGenbucketItems.keySet()) {

            inventory.setItem(i, GenbucketManager.activeGenbucketItems.get(uuid));
            i++;

            if (i == 54)
                break;

        }

        inventory.setItem(53, ItemStackUtil.createItemStack("§a§lNext page", Arrays.asList("§7Current page 1"), Material.ARROW, 1, 0, true));

        player.getOpenInventory().close();

        player.openInventory(inventory);

    }

    public void loadNextInventory(Inventory inventory) {

        int page = Integer.parseInt(ChatUtil.stripColor(inventory.getItem(53).getItemMeta().getLore().get(0).split(" ")[2])) + 1;

        inventory.clear();

        int i = 0;
        int slot = 0;

        for (UUID uuid : GenbucketManager.activeGenbucketItems.keySet()) {

            i++;

            if (i < 53 * page)
                continue;

            if (slot > 52)
                break;

            inventory.setItem(slot, GenbucketManager.activeGenbucketItems.get(uuid));

            slot++;

        }

        inventory.setItem(53, ItemStackUtil.createItemStack("§a§lNext page", Arrays.asList("§7Current page " + page), Material.ARROW, 1, 0, true));

    }

}
