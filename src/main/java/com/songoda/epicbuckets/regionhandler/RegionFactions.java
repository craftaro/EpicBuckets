package com.songoda.epicbuckets.regionhandler;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionFactions {

    public static boolean canBuild(Player player, Location location) {
        if (!EpicBuckets.getInstance().getConfigManager().isSupportFactions()) return true;
        if (!Bukkit.getPluginManager().isPluginEnabled("Factions")) return true;

        if (Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("drtshock")) {
            /*
            FactionsUUID and his clones/forks/whatever
             */
            return FactionsSavage.check(player, location);
        } else {
            /*
            Massive
             */
            return FactionsMassive.check(player, location);
        }

    }

}
