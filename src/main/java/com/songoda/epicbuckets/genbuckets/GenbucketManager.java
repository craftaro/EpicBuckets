package com.songoda.epicbuckets.genbuckets;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.InventoryUtil;
import com.songoda.epicbuckets.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * GenbucketManager created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 12:41
 */
public class GenbucketManager {

    public static HashMap<UUID, Integer> playerInteger = new HashMap<>();
    public static HashMap<UUID, Integer> activeGenBuckets = new HashMap<>();
    public static HashMap<UUID, ItemStack> activeGenbucketItems = new HashMap<>();
    public static HashMap<UUID, Location> activeGenbucketLocation = new HashMap<>();

    public static List<UUID> adminList = new ArrayList<>();

    public static int getPlayerInteger(Player player) {
        return playerInteger.getOrDefault(player.getUniqueId(), 1);
    }

    public static void setPlayerInteger(Player player, int amount) {
        playerInteger.put(player.getUniqueId(), amount);
    }

    public static int getActiveGenBuckets(Player player) {
        return activeGenBuckets.getOrDefault(player.getUniqueId(), 1);
    }

    public static void setActiveGenBuckets(Player player, int amount) {
        activeGenBuckets.put(player.getUniqueId(), amount);
    }

    public void openBulkShop(Player player, GenbucketItem genbucketItem) {

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        String inventoryName = ChatUtil.colorString(config.getString("BULK-SHOP-INVENTORY.inventory-name").replace("{player}", player.getName()));

        int size = config.getInt("BULK-SHOP-INVENTORY.size");

        Inventory inventory = Bukkit.createInventory(player, size, inventoryName);

        int mainItemSlot = config.getInt("BULK-SHOP-INVENTORY.main-item-slot");

        int itemAmount = getPlayerInteger(player);

        genbucketItem.setAmount(itemAmount);

        genbucketItem.setItemLore(genbucketItem.getItemSecondLore());

        int returnBackItemSlot = config.getInt("BULK-SHOP-INVENTORY.return-back-slot");

        String purchaseItemName = ChatUtil.colorString(config.getString("BULK-SHOP-INVENTORY.purchase-item.name"));
        Material purchaseItemMaterial = Material.valueOf(config.getString("BULK-SHOP-INVENTORY.purchase-item.material").toUpperCase());
        int purchaseItemDamage = config.getInt("BULK-SHOP-INVENTORY.purchase-item.damage");
        int purchaseItemSlot = config.getInt("BULK-SHOP-INVENTORY.purchase-item.slot");

        InventoryUtil.fillInventory(inventory, config.getBoolean("BULK-SHOP-INVENTORY.fill"));

        InventoryUtil.setBackButton(inventory, returnBackItemSlot, true);

        inventory.setItem(purchaseItemSlot, ItemStackUtil.createItemStack(purchaseItemName, Arrays.asList("&f"), purchaseItemMaterial, 1, purchaseItemDamage, false));

        inventory.setItem(mainItemSlot, genbucketItem.getGenbucketItem());

        loadAmountModifiers(inventory, "increase");
        loadAmountModifiers(inventory, "decrease");

        player.getOpenInventory().close();
        player.openInventory(inventory);

    }

    private void loadAmountModifiers(Inventory inventory, String key) {

        FileConfiguration config = EpicBuckets.getInstance().getConfig();

        int x = 1;

        String operator = key.equalsIgnoreCase("increase") ? "§a§l+ " : "§c§l- ";

        Material material = Material.valueOf(config.getString("BULK-SHOP-INVENTORY.increase-item.material").toUpperCase());

        int damage = config.getInt("BULK-SHOP-INVENTORY." + key + "-item.damage");

        for (String str : config.getString("BULK-SHOP-INVENTORY." + key + "-item.slots").split(",")) {

            int slot = Integer.parseInt(str);

            inventory.setItem(slot, ItemStackUtil.createItemStack(operator + x, Arrays.asList("&f"), material, 1, damage, false));

            if (x == 1)
                x = 10;
            else
                x = 64;

        }

    }


}
