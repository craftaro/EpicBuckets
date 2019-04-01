package com.songoda.epicbuckets.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.songoda.epicbuckets.utils.hooks.ProtectionPluginHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HookWorldGuard implements ProtectionPluginHook {

    private final WorldGuardPlugin worldGuard;

    public HookWorldGuard() {
        this.worldGuard = WorldGuardPlugin.inst();
    }

    @Override
    public JavaPlugin getPlugin() {
        return worldGuard;
    }

    @Override
    public boolean canBuild(Player player, Location location) {
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (regionManager == null) return true;
        ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(location));

        return set.size() < 1;
    }

}