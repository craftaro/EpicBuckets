package com.songoda.epicbuckets;

import co.aikar.commands.PaperCommandManager;
import com.songoda.epicbuckets.command.CommandGenbucket;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.genbucket.GenbucketManager;
import com.songoda.epicbuckets.listener.GenbucketPlaceListener;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.Debugger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {

    private static EpicBuckets instance;
    private static CommandSender console = Bukkit.getConsoleSender();

    private ConfigManager configManager;
    private ShopManager shopManager;
    private Debugger debugger;
    private GenbucketManager genbucketManager;
    private Economy econ;
    private PaperCommandManager commandManager;

    private Locale locale;

    public static EpicBuckets getInstance() { return instance; }

    @Override
    public void onEnable() {
        console.sendMessage(ChatUtil.colorString("&a============================="));
        console.sendMessage(ChatUtil.colorString("&7EpicBuckets " + this.getDescription().getVersion() + " by &5Songoda <3!"));
        console.sendMessage(ChatUtil.colorString("&7Action: &aEnabling&7..."));
        instance = this;

        if (!isEnabled()) {
            return;
        }

        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));

        debugger = new Debugger();
        configManager = new ConfigManager();
        shopManager = new ShopManager();
        shopManager.init();
        genbucketManager = new GenbucketManager();
        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new CommandGenbucket());

        getServer().getPluginManager().registerEvents(new GenbucketPlaceListener(), this);

        setupEconomy();

        console.sendMessage(ChatUtil.colorString("&a============================="));
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatUtil.colorString("&a============================="));
        console.sendMessage(ChatUtil.colorString("&7EpicBuckets " + this.getDescription().getVersion() + " by &5Songoda <3!"));
        console.sendMessage(ChatUtil.colorString("&7Action: &cDisabling&7..."));
        console.sendMessage(ChatUtil.colorString("&a============================="));
    }

    public void reload() {
        this.locale.reloadMessages();
        this.getConfigManager().reload();
        this.getShopManager().reload();
    }

    public double getBalance(Player player) {
        return econ.getBalance(player);
    }

    public void withdrawBalance(Player player, int amount, boolean sendMessage) {

        if (sendMessage)
            player.sendMessage(locale.getMessage("interface.withdrawl.success").replace("{amount}", String.valueOf(amount)));

        econ.withdrawPlayer(player, amount);
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

    public Locale getLocale() {
        return locale;
    }

}
