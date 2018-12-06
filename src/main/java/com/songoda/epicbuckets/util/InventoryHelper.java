package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InventoryHelper {

    public static List<XMaterial> convertMaterialList(List<String> toConvert) {
        List<XMaterial> converted = new ArrayList<>();
        toConvert.forEach(s -> converted.add(XMaterial.valueOf(s.toUpperCase())));
        return converted;
    }

    public static int[] emptySlots(int size) {
        List<Integer> slots = new ArrayList<>();
        IntStream.range(0, size).forEach(slots::add);
        return slots.stream().mapToInt(i -> i).toArray();
    }

    public static ItemStack setDisplayName(ItemStack item, String s) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> newLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        im.setLore(newLore);
        item.setItemMeta(im);
        return item;
    }

}
