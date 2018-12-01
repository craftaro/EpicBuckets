package com.songoda.epicbuckets;

import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.util.Debugger;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {

    private static EpicBuckets instance;

    private ConfigManager configManager;
    private ShopManager shopManager;
    private Debugger debugger;

    public static EpicBuckets getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager();
        shopManager = new ShopManager();
        debugger = new Debugger();
    }

    @Override
    public void onDisable() {

    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public Debugger getDebugger() {
        return debugger;
    }

}
