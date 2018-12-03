package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShopManager {

    private HashMap<String, Shop> shopDatabase;
    private FileConfiguration shops;
    private EpicBuckets epicBuckets;

    private String configPath = "MENU-ITEMS";
    private String shopPath = "shops";

    private ItemStack increaseItem;
    private ItemStack decreaseItem;
    private ItemStack purchaseItem;

    private List<Integer> increaseSlots;
    private List<Integer> decreaseSlots;
    private int purchaseSlot;

    private boolean useBackButtons;

    public ShopManager() {
        epicBuckets = EpicBuckets.getInstance();
        shopDatabase = new HashMap<>();
        increaseSlots = new ArrayList<>();
        decreaseSlots = new ArrayList<>();
        shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");

        loadData();
        loadShops();
        setupBulkShop();
    }

    private void setupBulkShop() {
        boolean i = Validator.getInstance().isMaterial(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".material");
        boolean d = Validator.getInstance().isMaterial(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".material");
        boolean p = Validator.getInstance().isMaterial(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".material");
        purchaseSlot = Validator.getInstance().slot(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".slot"));

        if (purchaseSlot == -1) {
            purchaseSlot = 40;
        }

        increaseItem = ((!i) ? XMaterial.GREEN_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".material")).parseItem());
        decreaseItem = ((!i) ? XMaterial.RED_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".material")).parseItem());
        purchaseItem = ((!i) ? XMaterial.YELLOW_STAINED_GLASS.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".material")).parseItem());

        for (String s : epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".slots").split(",")) {
            increaseSlots.add(Integer.parseInt(s));
        }
        for (String s : epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".slots").split(",")) {
            increaseSlots.add(Integer.parseInt(s));
        }
    }

    private void loadData() {
        useBackButtons = shops.getBoolean("use-back-buttons");
    }

    private void loadShops() {
        for (String key : epicBuckets.getConfig().getConfigurationSection(configPath).getKeys(false)) {
            if (!epicBuckets.getConfig().isConfigurationSection(configPath + "." + key)) {
                continue;
            }

            shopDatabase.put(epicBuckets.getConfig().getString(configPath + "." + key), new Shop(epicBuckets.getConfig().getString(configPath + "." + key), epicBuckets.getConfig().getString(configPath + "." + key + ".shop"), configPath + "." + key));
        }
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getShopPath() {
        return shopPath;
    }

    public ItemStack getIncreaseItem() {
        return increaseItem;
    }

    public ItemStack getDecreaseItem() {
        return decreaseItem;
    }

    public ItemStack getPurchaseItem() {
        return purchaseItem;
    }

    public List<Integer> getIncreaseSlots() {
        return increaseSlots;
    }

    public List<Integer> getDecreaseSlots() {
        return decreaseSlots;
    }

    public int getPurchaseSlot() {
        return purchaseSlot;
    }

    public boolean isUseBackButtons() {
        return useBackButtons;
    }

}
