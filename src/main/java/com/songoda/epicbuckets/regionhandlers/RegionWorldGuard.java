package com.songoda.epicbuckets.regionhandlers;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * WorldGuard created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 18:00
 */
public class RegionWorldGuard {

    public static WorldGuardPlugin getWorldGuard() {
        // WorldGuard may not be loaded
        Plugin plugin = EpicBuckets.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }
        return (WorldGuardPlugin) plugin;
    }

    public static boolean canBuild(Player player, Location loc) {

        boolean isWorldGuardEnabled = EpicBuckets.getInstance().getConfig().getBoolean("WORLDGUARD-SUPPORT");

        if (!isWorldGuardEnabled)
            return true;

        RegionManager regionManager = getWorldGuard().getRegionManager(player.getLocation().getWorld());

        ApplicableRegionSet set = regionManager.getApplicableRegions(loc);

        for (ProtectedRegion region : set) {
            return false;
        }

        return true;
    }

}
