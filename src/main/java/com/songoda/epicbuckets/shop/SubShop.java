package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
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
        subShopPath = epicBuckets.getShopManager().getShopPath() + "." + parent.getMenuItem() + "." + item;

        loadData(shops);
        setupShopItem(shops);
    }

    private void loadData(FileConfiguration shops) {
        boolean d = Validator.getInstance().isDouble(shops.getString(subShopPath + ".price"));

        if (!d) {
            epicBuckets.getDebugger().invalidPrice(subShopPath);
            enabled = false;
        }

        this.shopName = shops.getString(subShopPath + ".name");
        this.description = shops.getStringList(subShopPath + ".description");
        this.genItemLore = shops.getStringList(subShopPath + ".item-lore");
    }

    private void setupShopItem(FileConfiguration shops) {
        slot = Validator.getInstance().slot(shops.getString(subShopPath + ".slot"));
        boolean m = Validator.getInstance().isMaterial(shops.getString(subShopPath + ".icon"));
        boolean t = Validator.getInstance().isMaterial(shops.getString(subShopPath + ".type"));

        if (slot == -1) {
            epicBuckets.getDebugger().invalidSlot(subShopPath);
            enabled = false;
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(shops.getString(subShopPath + ".icon")).parseItem());
        genItem = ((!t) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(shops.getString(subShopPath + ".type")).parseItem());
    }

    public boolean isEnabled() {
        return enabled;
    }

}
