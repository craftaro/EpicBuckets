package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.configuration.file.FileConfiguration;

public class SubShop {

    private Shop parent;
    private String item;

    public SubShop(Shop parent, String item) {
        this.parent = parent;
        this.item = item;

        init();
    }

    private void init() {
        FileConfiguration shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");

        //TODO: load in all data for this sub shop
    }

}
