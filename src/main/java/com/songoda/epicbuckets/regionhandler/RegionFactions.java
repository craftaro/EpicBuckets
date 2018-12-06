package com.songoda.epicbuckets.regionhandler;

import com.songoda.epicbuckets.EpicBuckets;
import me.markeh.factionsframework.entities.Faction;
import me.markeh.factionsframework.entities.Factions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionFactions {

        public static boolean canBuild(Player player, Location location) {

        if (!EpicBuckets.getInstance().getConfigManager().isSupportFactions())
            return true;

        Faction factionAt = Factions.getFactionAt(location);

        boolean enableGensInWilderness = EpicBuckets.getInstance().getConfigManager().isGensInWilderness();

        if (factionAt.isNone()) {
            return enableGensInWilderness;
        }

        return false;
    }


}
