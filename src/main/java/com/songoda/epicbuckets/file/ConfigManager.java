package com.songoda.epicbuckets.file;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    private EpicBuckets epicBuckets;
    private HashMap<String, Config> configDatabase;

    private String backButtonPath = "BACK-BUTTON";
    private String fillItemPath = "FILL-ITEM";
    private String bulkShopIncreasePath = "BULK-SHOP-INVENTORY.increase-item";
    private String bulkShopDecreasePath = "BULK-SHOP-INVENTORY.decrease-item";
    private String bulkShopPurchasePath = "BULK-SHOP-INVENTORY.purchase-item";

    private ItemStack backButton;

    public ConfigManager() {
        this.epicBuckets = EpicBuckets.getInstance();
        setup();
    }

    private void setup() {
        epicBuckets.saveDefaultConfig();
        createConfig("shops", true);

        setupBackButton();
        setupFillItem();
    }

    private void setupFillItem() {
        boolean m = Validator.getInstance().isMaterial(epicBuckets.getConfig().getString(getFillItemPath() + ".material"));

        backButton = ((!m) ? XMaterial.BLACK_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(getFillItemPath() + ".material")).parseItem());
        backButton.getItemMeta().setDisplayName(epicBuckets.getConfig().getString(getFillItemPath() + ".name"));
    }

    private void setupBackButton() {
        boolean m = Validator.getInstance().isMaterial(epicBuckets.getConfig().getString(getBackButtonPath() + ".material"));

        backButton = ((!m) ? XMaterial.BARRIER.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(getBackButtonPath() + ".material")).parseItem());
        backButton.getItemMeta().setDisplayName(epicBuckets.getConfig().getString(getBackButtonPath() + ".name"));
    }

    public void createConfig(String name, boolean resource) {
        File f = new File(epicBuckets.getDataFolder(), name + ".yml");
        configDatabase.put(name, new Config(f, resource));
    }

    public void reloadConfig(String name) {
        if (!configDatabase.containsKey(name)) return;
        configDatabase.get(name).reloadConfig();
    }

    public FileConfiguration getConfig(String name) {
        if (!configDatabase.containsKey(name)) return null;
        return configDatabase.get(name).getConfig();
    }

    public String getBackButtonPath() {
        return backButtonPath;
    }

    public String getFillItemPath() {
        return fillItemPath;
    }

    public String getBulkShopIncreasePath() {
        return bulkShopIncreasePath;
    }

    public String getBulkShopDecreasePath() {
        return bulkShopDecreasePath;
    }

    public String getBulkShopPurchasePath() {
        return bulkShopPurchasePath;
    }
}
