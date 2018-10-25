package com.songoda.epicbuckets.regionhandlers;

import com.songoda.epicbuckets.EpicBuckets;
import me.markeh.factionsframework.entities.FPlayers;
import me.markeh.factionsframework.entities.Faction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * FactionsUUID created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 18:00
 */
public class RegionFactions {

    public static boolean canBuild(Player player, Location location) {

        boolean isFactionsUUIDEnabled = EpicBuckets.getInstance().getConfig().getBoolean("FACTIONS-SUPPORT");

        if (!isFactionsUUIDEnabled)
            return true;


        Faction factionAt = me.markeh.factionsframework.entities.Factions.getFactionAt(location);

        boolean enableGensInWilderness = EpicBuckets.getInstance().getConfig().getBoolean("ENABLE-GENS-IN-WILDERNESS");

        if (factionAt.isNone()) {
            return enableGensInWilderness;
        }

        return false;
    }


}
