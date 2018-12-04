package com.songoda.epicbuckets;

import co.aikar.commands.PaperCommandManager;
import com.songoda.epicbuckets.command.CommandGenbucket;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.genbucket.GenbucketManager;
import com.songoda.epicbuckets.listener.GenbucketPlaceListener;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.util.Debugger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {

    private static EpicBuckets instance;

    private ConfigManager configManager;
    private ShopManager shopManager;
    private Debugger debugger;
    private GenbucketManager genbucketManager;
    private Economy econ;
    private PaperCommandManager commandManager;

    public static EpicBuckets getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;

        debugger = new Debugger();
        configManager = new ConfigManager();
        shopManager = new ShopManager();
        shopManager.init();
        genbucketManager = new GenbucketManager();
        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new CommandGenbucket());

        getServer().getPluginManager().registerEvents(new GenbucketPlaceListener(), this);

        setupEconomy();
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
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

    public GenbucketManager getGenbucketManager() {
        return genbucketManager;
    }

    public Economy getEcon() {
        return econ;
    }

}
