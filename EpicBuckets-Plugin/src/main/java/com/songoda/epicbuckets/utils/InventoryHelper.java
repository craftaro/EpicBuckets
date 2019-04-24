package com.songoda.epicbuckets.utils;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InventoryHelper {

    public static List<Materials> convertMaterialList(List<String> toConvert, String item) {
        List<Materials> converted = new ArrayList<>();
        for (String s : toConvert) {
            if (!Validator.isMaterial(s.toUpperCase())) {
                EpicBuckets.getInstance().getDebugger().sendConsole("Invalid material " + s.toUpperCase() + " in " + item + ", skipping..");
                continue;
            }
            converted.add(Materials.valueOf(s.toUpperCase()));
        }
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

    public static ItemStack setSubShopLore(ItemStack item, List<String> lore, SubShop subShop) {
        ItemMeta im = item.getItemMeta();
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {
            String line = s;
            if (line.contains("%price%")) line = line.replace("%price%", subShop.getPrice() + "");
            if (line.contains("%material%"))
                line = line.replace("%material%", subShop.getType().parseMaterial().name());
            newLore.add(ChatColor.translateAlternateColorCodes('&', line));
        });
        im.setLore(newLore);
        item.setItemMeta(im);
        return item;
    }

}
