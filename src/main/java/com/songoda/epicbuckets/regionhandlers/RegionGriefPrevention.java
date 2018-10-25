package com.songoda.epicbuckets.regionhandlers;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * GriefPrevention created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 18:00
 */
public class RegionGriefPrevention {

    public static me.ryanhamshire.GriefPrevention.GriefPrevention getGriefPrevention() {
        org.bukkit.plugin.Plugin pl = EpicBuckets.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention");

        if (pl == null) {
            EpicBuckets.getInstance().getLogger().warning("GriefPrevention support is enabled but cannot find the plugin");
            return null;
        }

        return (me.ryanhamshire.GriefPrevention.GriefPrevention) pl;

    }

    public static boolean canBuild(Player player, Location location) {

        boolean isGriefPreventionEnabled = EpicBuckets.getInstance().getConfig().getBoolean("griefprevention-support");

        // If we don't check for griefprevention, just let them place
        if (!isGriefPreventionEnabled)
            return true;

        return getGriefPrevention().allowBreak(player, location.getBlock(), location) != null;

    }

}
