package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.util.InventoryHelper;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
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

    private int backButtonSlot;
    private GenbucketType trait;
    private int inventorySize;
    private boolean fillInventory;
    private String inventoryName;

    private boolean enabled;

    public Shop(String menuItem, String name, String path) {
        this.epicBuckets = EpicBuckets.getInstance();
        this.shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");

        this.subShops = new HashMap<>();

        this.menuItem = menuItem;
        this.shopName = name;

        this.path = path;
        this.shopPath = epicBuckets.getShopManager().getShopPath() + "." + shopName;
        this.enabled = shops.getBoolean(shopPath + ".enabled");

        loadData();
        setupShopItem();
        loadSubShops();
    }

    private void loadData() {
        trait = Validator.genbucketType(shops.getString(shopPath + ".trait"));
        backButtonSlot = Validator.slot(shops.getString(shopPath + ".goBackButton"));
        inventorySize = Validator.inventorySize(shops.getString(shopPath + ".size"));
        fillInventory = shops.getBoolean(shopPath + ".fill");
        inventoryName = shops.getString(shopPath + ".inventory-name");

        if (trait == null) {
            epicBuckets.getDebugger().invalidGenbucketType(shopPath + ".trait");
            setEnabled(false);
        }
        if (backButtonSlot == -1) {
            epicBuckets.getDebugger().invalidSlot(shopPath + ".goBackButton");
            setEnabled(false);
        }
        if (inventorySize == -1) {
            epicBuckets.getDebugger().invalidInventorySize(shopPath + ".size");
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

        slot = Validator.slot(epicBuckets.getConfig().getString(path + ".slot"));
        boolean m = Validator.isMaterial(epicBuckets.getConfig().getString(itemPath + ".material"));

        if (slot == -1) {
            epicBuckets.getDebugger().invalidSlot(itemPath + ".slot");
            setEnabled(false);
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(itemPath + ".material")).parseItem());
        shopItem = InventoryHelper.setLore(InventoryHelper.setDisplayName(shopItem, epicBuckets.getConfig().getString(itemPath + ".name")), epicBuckets.getConfig().getStringList(itemPath + ".lore"));
    }

    public Collection<SubShop> getSubShops() {
        return subShops.values();
    }

    public SubShop getSubShop(String shop) {
        return subShops.get(shop);
    }

    public SubShop getSubShop(XMaterial mat) {
        for (SubShop subShop : subShops.values()) {
            if (subShop.getGenItem().getType() == mat.parseMaterial() &&
            subShop.getGenItem().getTypeId() == mat.parseItem().getTypeId()) {
                return subShop;
            }
        }
        return null;
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

    public ItemStack getShopItem() {
        return shopItem;
    }

    public int getBackButtonSlot() {
        return backButtonSlot;
    }

    public GenbucketType getTrait() {
        return trait;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public boolean isFillInventory() {
        return fillInventory;
    }

    public String getInventoryName() {
        return inventoryName;
    }
}
