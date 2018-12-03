package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Shop {

    private EpicBuckets epicBuckets;
    private FileConfiguration shops;

    private HashMap<String, SubShop> subShops;

    private ItemStack shopItem;
    private String menuItem;
    private String shopName;
    private int slot;

    private String path;
    private String shopPath;

    private int backButton;
    private GenbucketType trait;
    private int inventorySize;
    private boolean fillInventory;
    private String inventoryName;

    private boolean enabled = true;

    public Shop(String menuItem, String name, String path) {
        this.epicBuckets = EpicBuckets.getInstance();
        this.shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");;

        this.subShops = new HashMap<>();

        this.menuItem = menuItem;
        this.shopName = name;

        this.path = path;
        this.shopPath = epicBuckets.getShopManager().getShopPath() + "." + shopName;

        loadData();
        setupShopItem();
        loadSubShops();
    }

    private void loadData() {
        trait = Validator.getInstance().genbucketType(shops.getString(shopPath + "trait"));
        backButton = Validator.getInstance().slot(shops.getString(shopPath + "goBackButton"));
        inventorySize = Validator.getInstance().inventorySize(shops.getString(shopPath + "size"));
        fillInventory = shops.getBoolean(shopPath + "fill");
        inventoryName = shops.getString(shopPath + "inventory-name");

        if (trait == null) {
            epicBuckets.getDebugger().invalidGenbucketType(shopPath + "trait");
            setEnabled(false);
        }
        if (backButton == -1) {
            epicBuckets.getDebugger().invalidSlot(shopPath + "goBackButton");
            setEnabled(false);
        }
        if (inventorySize == -1) {
            epicBuckets.getDebugger().invalidInventorySize(shopPath + "size");
            setEnabled(false);
        }
    }

    private void loadSubShops() {
        for (String shop : shops.getConfigurationSection(shopPath).getKeys(false)) {
            if (!shops.isConfigurationSection(shopPath + "." + shop)) {
                continue;
            }

            subShops.put(shop, new SubShop(this, shop));
        }
    }

    private void setupShopItem() {
        String itemPath = path + ".item";

        slot = Validator.getInstance().slot(epicBuckets.getConfig().getString(itemPath + ".slot"));
        boolean m = Validator.getInstance().isMaterial(epicBuckets.getConfig().getString(itemPath + ".material"));

        if (slot == -1) {
            epicBuckets.getDebugger().invalidSlot(itemPath);
            setEnabled(false);
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(itemPath + ".material")).parseItem());

        shopItem.getItemMeta().setDisplayName(epicBuckets.getConfig().getString(itemPath + ".name"));
        shopItem.getItemMeta().setLore(epicBuckets.getConfig().getStringList(itemPath + ".lore"));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getMenuItem() {
        return menuItem;
    }

}
