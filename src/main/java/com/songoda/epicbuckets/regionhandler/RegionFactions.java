package com.songoda.epicbuckets.regionhandler;

import com.songoda.epicbuckets.EpicBuckets;
import me.markeh.factionsframework.entities.FPlayers;
import me.markeh.factionsframework.entities.Faction;
import me.markeh.factionsframework.entities.Factions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionFactions {

    public static boolean canBuild(Player player, Location location) {
        if (!EpicBuckets.getInstance().getConfigManager().isSupportFactions()) return true;

        Faction f = Factions.getFactionAt(location);

        if (f.isNone()) return EpicBuckets.getInstance().getConfigManager().isGensInWilderness();

        if (FPlayers.getBySender(player).getFaction().isNone()) return false;
        if (FPlayers.getBySender(player).getFaction().getId().equals(f.getId())) return true;
        return false;
    }

}
