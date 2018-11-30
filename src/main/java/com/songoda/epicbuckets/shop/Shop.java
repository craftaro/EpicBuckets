package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.inventory.ItemStack;

public class Shop {

    private EpicBuckets epicBuckets;

    private String name;
    private String path;

    private ItemStack shopItem;

    public Shop(String name, String path) {
        this.epicBuckets = EpicBuckets.getInstance();

        this.name = name;
        this.path = path;

        setupShopItem();
    }

    private void setupShopItem() {
        String itemPath = path + ".item";

        //TODO: create the shopitem
    }

}
