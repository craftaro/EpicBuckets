package com.songoda.epicbuckets.regionhandler;

import com.songoda.epicbuckets.EpicBuckets;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RegionGriefPrevention {

    public static GriefPrevention getGriefPrevention() {
        Plugin pl = EpicBuckets.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention");

        if (pl == null) {
            EpicBuckets.getInstance().getLogger().warning("GriefPrevention support is enabled but cannot find the plugin");
            return null;
        }

        return (GriefPrevention) pl;
    }

    public static boolean canBuild(Player player, Location location) {
        boolean isGriefPreventionEnabled = EpicBuckets.getInstance().getConfigManager().isSupportGriefPrevention();

        // If we don't check for griefprevention, just let them place
        if (!isGriefPreventionEnabled)
            return true;

        if (getGriefPrevention() == null) return true;

        return getGriefPrevention().allowBreak(player, location.getBlock(), location) != null;
    }


}
