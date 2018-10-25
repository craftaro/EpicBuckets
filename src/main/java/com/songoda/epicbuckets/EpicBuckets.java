package com.songoda.epicbuckets;

import com.songoda.epicbuckets.commands.GenbucketAdminCommand;
import com.songoda.epicbuckets.commands.GenbucketCommand;
import com.songoda.epicbuckets.filehandler.files.MessageFile;
import com.songoda.epicbuckets.filehandler.files.ShopFile;
import com.songoda.epicbuckets.listeners.GenbucketPlaceListener;
import com.songoda.epicbuckets.listeners.InventoryClickListener;
import com.songoda.epicbuckets.util.ChatUtil;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {

    public static EpicBuckets main;
    private static Permission permission = null;
    private static Economy economy = null;
    private static Chat chat = null;
    public MessageFile messageFile;
    public ShopFile shopFile;

    public static EpicBuckets getInstance() {
        return main;
    }

    @Override
    public void onEnable() {

        main = this;

        //new AntiPiracy().enable();

        if (!isEnabled())
            return;

        setUpFiles();

        setupPermissions();
        setupEconomy();
        setupChat();
        setUpCommands();
        registerListeners();


    }

    @Override
    public void onDisable() {

        main = null;

    }

    private void setUpFiles() {

        if (!getDataFolder().exists()) {

            getDataFolder().mkdirs();
            getLogger().warning("Folder not found, generating files!");
            saveResource("messages.yml", false);
            saveResource("shops.yml", false);
            saveDefaultConfig();

        }

        messageFile = new MessageFile();
        shopFile = new ShopFile();


    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    public void withdrawBalance(Player player, int amount, boolean sendMessage) {

        if (sendMessage)
            player.sendMessage(ChatUtil.colorPrefix(messageFile.config.getString("WITHDRAW-MESSAGE")).replace("{amount}", String.valueOf(amount)));

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

        messageFile.load();
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


}
