package com.songoda.epicbuckets.regionhandler;

import com.massivecraft.factions.*;
import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionMassiveCraftFactions {

    public static boolean canBuild(Player player, Location location) {
        if (!EpicBuckets.getInstance().getConfigManager().isSupportFactions()) return true;
        if (!EpicBuckets.getInstance().getServer().getPluginManager().isPluginEnabled("Factions")) return true;

        Faction f = Board.getInstance().getFactionAt(new FLocation(location));

        if (f == null) return false;

        if (FPlayers.getInstance().getByPlayer(player).getFaction() != f) return false;

        return true;
    }

}
