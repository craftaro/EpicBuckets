package com.songoda.epicbuckets.hooks.legacy;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
        return worldGuard.canBuild(player, location);
    }

}