package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * InventoryUtil created by: SoFocused
 * Date Created: oktober 03 2018
 * Time created: 16:25
 */
public class InventoryUtil {

    public static EpicBuckets plugin = EpicBuckets.getInstance();


    public static ItemStack getFillItemStack() {

        FileConfiguration config = plugin.getConfig();

        Material m = Material.valueOf(config.getString("FILL-ITEM.material").toUpperCase());

        int damage = config.getInt("FILL-ITEM.damage");

        return ItemStackUtil.createItemStack(config.getString("FILL-ITEM.name"), Arrays.asList("&f"), m, 1, damage, false);

    }

    private static ItemStack getBackItemStack() {

        FileConfiguration config = plugin.getConfig();

        Material m = Material.valueOf(config.getString("BACK-BUTTON.material").toUpperCase());

        int damage = config.getInt("BACK-BUTTON.damage");

        return ItemStackUtil.createItemStack(config.getString("BACK-BUTTON.name"), Arrays.asList("&f"), m, 1, damage, false);

    }

    public static void fillInventory(Inventory inventory, boolean fillInventory) {

        if (!fillInventory)
            return;

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, getFillItemStack());

    }

    public static void setBackButton(Inventory inventory, int slot, boolean setItem) {

        if (setItem)
            inventory.setItem(slot, getBackItemStack());

    }


}
