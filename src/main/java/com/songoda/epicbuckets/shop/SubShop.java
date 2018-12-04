package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.InventoryHelper;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SubShop {

    private EpicBuckets epicBuckets;

    private String subShopPath;
    private boolean enabled;

    private ItemStack shopItem;
    private ItemStack genItem;
    private ItemStack genShopItem;

    private Shop parent;
    private String item;
    private String shopName;
    private int slot;
    private double price;
    private List<String> description;
    private List<String> genItemLore;


    public SubShop(Shop parent, String item) {
        this.epicBuckets = EpicBuckets.getInstance();

        this.parent = parent;
        this.item = item;
        this.description = new ArrayList<>();
        this.genItemLore = new ArrayList<>();

        init();
    }

    private void init() {
        FileConfiguration shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");
        subShopPath = epicBuckets.getShopManager().getShopPath() + "." + parent.getShopName() + "." + item;

        setEnabled(true);

        loadData(shops);
        setupShopItem(shops);
    }

    private void loadData(FileConfiguration shops) {
        price = Validator.price(shops.getString(subShopPath + ".price"));

        if (price == -1.0) {
            epicBuckets.getDebugger().invalidPrice(subShopPath);
            setEnabled(false);
        }

        this.shopName = shops.getString(subShopPath + ".name");
        this.description = shops.getStringList(subShopPath + ".description");
        this.genItemLore = shops.getStringList(subShopPath + ".item-lore");
    }

    private void setupShopItem(FileConfiguration shops) {
        slot = Validator.slot(shops.getString(subShopPath + ".slot"));
        boolean m = Validator.isMaterial(shops.getString(subShopPath + ".icon"));
        boolean t = Validator.isMaterial(shops.getString(subShopPath + ".type"));

        if (slot == -1) {
            epicBuckets.getDebugger().invalidSlot(subShopPath);
            setEnabled(false);
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(shops.getString(subShopPath + ".icon")).parseItem());
        shopItem = InventoryHelper.setDisplayName(InventoryHelper.setLore(shopItem, getDescription()), getShopName());

        genItem = ((!t) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(shops.getString(subShopPath + ".type")).parseItem());

        genShopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(shops.getString(subShopPath + ".icon")).parseItem());
        genShopItem = InventoryHelper.setDisplayName(InventoryHelper.setLore(genShopItem, getGenItemLore()), getShopName());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ItemStack getShopItem() {
        return shopItem;
    }

    public ItemStack getGenItem() {
        return genItem;
    }

    public Shop getParent() {
        return parent;
    }

    public String getShopName() {
        return shopName;
    }

    public int getSlot() {
        return slot;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<String> getGenItemLore() {
        return genItemLore;
    }

    public ItemStack getGenShopItem() {
        return genShopItem;
    }
}
