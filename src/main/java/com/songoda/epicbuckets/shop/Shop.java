package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Shop {

    private EpicBuckets epicBuckets;

    private HashMap<String, SubShop> subShops;

    private String menuItem;
    private String shopName;
    private String path;
    private ItemStack shopItem;
    private int slot;

    private String shopPath = "shops";

    private boolean enabled = true;

    public Shop(String menuItem, String name, String path) {
        this.epicBuckets = EpicBuckets.getInstance();

        this.subShops = new HashMap<>();

        this.menuItem = menuItem;
        this.shopName = name;
        this.path = path;

        setupShopItem();
        loadSubShops();
    }

    private void loadSubShops() {
        FileConfiguration shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");

        for (String shop : shops.getConfigurationSection(shopPath + "." + shopName).getKeys(false)) {
            if (!shops.isConfigurationSection(shopPath + "." + shopName + "." + shop)) {
                continue;
            }

            subShops.put(shop, new SubShop(this, shop));
        }
    }

    private void setupShopItem() {
        String itemPath = path + ".item";

        boolean i = Validator.getInstance().isInt(epicBuckets.getConfig().getString(itemPath + ".slot"));
        boolean m = Validator.getInstance().isMaterial(epicBuckets.getConfig().getString(itemPath + ".material"));

        if (!i) {
            epicBuckets.getDebugger().sendConsole("&cMENU-ITEM." + menuItem + " has an invalid slot set, disabling shop..");
            enabled = false;
        } else {
            slot = epicBuckets.getConfig().getInt(path + ".slot");
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(itemPath + ".material")).parseItem());

        shopItem.getItemMeta().setDisplayName(epicBuckets.getConfig().getString(itemPath + ".name"));
        shopItem.getItemMeta().setLore(epicBuckets.getConfig().getStringList(itemPath + ".lore"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getSlot() {
        return slot;
    }

    public String getShopName() {
        return shopName;
    }

}
