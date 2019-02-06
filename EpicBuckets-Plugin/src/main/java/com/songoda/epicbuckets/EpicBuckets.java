package com.songoda.epicbuckets;

import com.google.common.base.Preconditions;
import com.songoda.epicbuckets.command.CommandManager;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.genbucket.GenbucketManager;
import com.songoda.epicbuckets.hooks.*;
import com.songoda.epicbuckets.listeners.GenbucketPlaceListener;
import com.songoda.epicbuckets.listeners.PlayerJoinListeners;
import com.songoda.epicbuckets.listeners.SourceBlockBreakListener;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.utils.ChatUtil;
import com.songoda.epicbuckets.utils.ConfigWrapper;
import com.songoda.epicbuckets.utils.Debugger;
import com.songoda.epicbuckets.utils.ServerVersion;
import com.songoda.epicbuckets.utils.hooks.ClaimableProtectionPluginHook;
import com.songoda.epicbuckets.utils.hooks.ProtectionPluginHook;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EpicBuckets extends JavaPlugin {

    private static EpicBuckets INSTANCE;
    private static CommandSender console = Bukkit.getConsoleSender();
    private References references;

    private ConfigManager configManager;
    private ShopManager shopManager;
    private Debugger debugger;
    private GenbucketManager genbucketManager;
    private Economy econ;
    private CommandManager commandManager;

    private Locale locale;

    private ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());

    private ConfigWrapper hooksFile = new ConfigWrapper(this, "", "hooks.yml");

    private List<ProtectionPluginHook> protectionHooks = new ArrayList<>();
    private ClaimableProtectionPluginHook factionsHook, townyHook, aSkyblockHook, uSkyblockHook, skyBlockEarhHook;

    public static EpicBuckets getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        console.sendMessage(ChatUtil.colorString("&a============================="));
        console.sendMessage(ChatUtil.colorString("&7EpicBuckets " + this.getDescription().getVersion() + " by &5Songoda <3!"));
        console.sendMessage(ChatUtil.colorString("&7Action: &aEnabling&7..."));

        saveDefaultConfig();

        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));

        this.references = new References();

        debugger = new Debugger();
        configManager = new ConfigManager();
        shopManager = new ShopManager();
        configManager.setup();
        genbucketManager = new GenbucketManager();
        this.commandManager = new CommandManager(this);

        PluginManager pluginManager = Bukkit.getPluginManager();

        // Event registration
        pluginManager.registerEvents(new GenbucketPlaceListener(this), this);
        pluginManager.registerEvents(new SourceBlockBreakListener(), this);
        pluginManager.registerEvents(new PlayerJoinListeners(this), this);

        // Register default hooks
        if (pluginManager.isPluginEnabled("ASkyBlock"))
            aSkyblockHook = (ClaimableProtectionPluginHook) this.register(HookASkyBlock::new);
        if (pluginManager.isPluginEnabled("FactionsFramework"))
            factionsHook = (ClaimableProtectionPluginHook) this.register(HookFactions::new);
        if (pluginManager.isPluginEnabled("GriefPrevention")) this.register(HookGriefPrevention::new);
        if (pluginManager.isPluginEnabled("Kingdoms")) this.register(HookKingdoms::new);
        if (pluginManager.isPluginEnabled("RedProtect")) this.register(HookRedProtect::new);
        if (pluginManager.isPluginEnabled("Towny"))
            townyHook = (ClaimableProtectionPluginHook) this.register(HookTowny::new);
        if (pluginManager.isPluginEnabled("USkyBlock"))
            uSkyblockHook = (ClaimableProtectionPluginHook) this.register(HookUSkyBlock::new);
        if (pluginManager.isPluginEnabled("SkyBlock"))
            skyBlockEarhHook = (ClaimableProtectionPluginHook) this.register(HookSkyBlockEarth::new);

        if (isServerVersionAtLeast(ServerVersion.V1_13)) {
            if (pluginManager.isPluginEnabled("WorldGuard")) this.register(HookWorldGuard::new);
            if (pluginManager.isPluginEnabled("PlotSquared")) this.register(HookPlotSquared::new);
        } else {
            if (pluginManager.isPluginEnabled("WorldGuard"))
                this.register(com.songoda.epicbuckets.hooks.legacy.HookWorldGuard::new);
            if (pluginManager.isPluginEnabled("PlotSquared"))
                this.register(com.songoda.epicbuckets.hooks.legacy.HookPlotSquared::new);
        }

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
        this.references = new References();
        this.getConfigManager().reload();
        this.hooksFile.createNewFile("Loading hookHandler File", "EpicBuckets Hooks File");
        this.getShopManager().reload();
    }

    private ProtectionPluginHook register(Supplier<ProtectionPluginHook> hookSupplier) {
        return this.registerProtectionHook(hookSupplier.get());
    }

    public ProtectionPluginHook registerProtectionHook(ProtectionPluginHook hook) {
        Preconditions.checkNotNull(hook, "Cannot register null hook");
        Preconditions.checkNotNull(hook.getPlugin(), "Protection plugin hook returns null plugin instance (#getPlugin())");

        JavaPlugin hookPlugin = hook.getPlugin();
        for (ProtectionPluginHook existingHook : protectionHooks) {
            if (existingHook.getPlugin().equals(hookPlugin)) {
                throw new IllegalArgumentException("Hook already registered");
            }
        }

        this.hooksFile.getConfig().addDefault("hooks." + hookPlugin.getName(), true);
        if (!hooksFile.getConfig().getBoolean("hooks." + hookPlugin.getName(), true)) return null;
        this.hooksFile.getConfig().options().copyDefaults(true);
        this.hooksFile.saveConfig();

        this.protectionHooks.add(hook);
        this.getLogger().info("Registered protection hook for plugin: " + hook.getPlugin().getName());
        return hook;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }

    public boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
    }

    public double getBalance(Player player) {
        return econ.getBalance(player);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public boolean canBuild(Player player, Location location) {
        if (player.hasPermission(getDescription().getName() + ".bypass")) {
            return true;
        }

        for (ProtectionPluginHook hook : protectionHooks)
            if (!hook.canBuild(player, location)) return false;
        return true;
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
        if (econ == null) setupEconomy();
        return econ;
    }

    public Locale getLocale() {
        return locale;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public References getReferences() {
        return references;
    }
}
