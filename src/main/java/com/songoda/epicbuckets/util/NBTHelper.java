package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBTHelper {

    public static ItemStack addGenbucketData(ItemStack item, GenbucketType genbucketType, SubShop s) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("Genbucket", true);
        nbtItem.setString("Type", genbucketType.name);
        nbtItem.setString("SubShop", s.getItem());
        nbtItem.setString("Shop", s.getParent().getMenuItem());
        return nbtItem.getItem();
    }

    public static boolean isGenbucket(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("Genbucket");
    }

}
