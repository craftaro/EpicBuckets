package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ShopManager {

    private HashMap<String, Shop> shopDatabase;
    private FileConfiguration shops;
    private EpicBuckets epicBuckets;

    private String path = "MENU-ITEMS";

    public ShopManager() {
        epicBuckets = EpicBuckets.getInstance();
        shopDatabase = new HashMap<>();
        shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");

        loadShops();
    }

    private void loadShops() {
        for (String key : epicBuckets.getConfig().getConfigurationSection(path).getKeys(false)) {
            if (!epicBuckets.getConfig().isConfigurationSection(path + "." + key)) {
                continue;
            }

            shopDatabase.put(epicBuckets.getConfig().getString(path + "." + key), new Shop(epicBuckets.getConfig().getString(path + "." + key), epicBuckets.getConfig().getString(path + "." + key + ".shop"), path + "." + key));
        }
    }

}
