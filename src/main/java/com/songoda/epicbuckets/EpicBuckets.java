package com.songoda.epicbuckets;

import com.songoda.epicbuckets.commands.GenbucketAdminCommand;
import com.songoda.epicbuckets.commands.GenbucketCommand;
import com.songoda.epicbuckets.filehandler.files.ShopFile;
import com.songoda.epicbuckets.listeners.GenbucketPlaceListener;
import com.songoda.epicbuckets.listeners.InventoryClickListener;
import com.songoda.epicbuckets.util.ChatUtil;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {
    private static CommandSender console = Bukkit.getConsoleSender();

    private static EpicBuckets INSTANCE;
    private static Permission permission = null;
    private static Economy economy = null;
    private static Chat chat = null;
    public ShopFile shopFile;

    private Locale locale;

    public static EpicBuckets getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        console.sendMessage(ChatUtil.colorString("&a============================="));
        console.sendMessage(ChatUtil.colorString("&7EpicBuckets " + this.getDescription().getVersion() + " by &5Songoda <3!"));
        console.sendMessage(ChatUtil.colorString("&7Action: &aEnabling&7..."));
        INSTANCE = this;

        if (!isEnabled())
            return;

        // Locales
        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));


        this.setUpFiles();

        this.setupPermissions();
        this.setupEconomy();
        this.setupChat();
        this.setUpCommands();
        this.registerListeners();

        console.sendMessage(ChatUtil.colorString("&a============================="));
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatUtil.colorString("&a============================="));
        console.sendMessage(ChatUtil.colorString("&7EpicBuckets " + this.getDescription().getVersion() + " by &5Songoda <3!"));
        console.sendMessage(ChatUtil.colorString("&7Action: &cDisabling&7..."));
        console.sendMessage(ChatUtil.colorString("&a============================="));
    }

    private void setUpFiles() {

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            getLogger().warning("Folder not found, generating files!");
            saveResource("shops.yml", false);
            saveDefaultConfig();

        }
        shopFile = new ShopFile();


    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    public void withdrawBalance(Player player, int amount, boolean sendMessage) {

        if (sendMessage)
            player.sendMessage(locale.getMessage("interface.withdrawl.success").replace("{amount}", String.valueOf(amount)));

        economy.withdrawPlayer(player, amount);
    }


    private void setUpCommands() {

        getCommand("genbucket").setExecutor(new GenbucketCommand());
        getCommand("genbucketadmin").setExecutor(new GenbucketAdminCommand());

    }

    private void registerListeners() {

        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new GenbucketPlaceListener(), this);

    }


    public void reloadFiles() {
        this.locale.reloadMessages();
        shopFile.load();
        reloadConfig();

    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat() {

        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public Locale getLocale() {
        return locale;
    }

}
