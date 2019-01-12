package com.songoda.epicbuckets.regionhandler;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsMassive {

    public static boolean check(Player player, Location location) {
        /*
        If wilderness
         */
        if (BoardColl.get().getFactionAt(PS.valueOf(location)) == FactionColl.get().getNone()) return EpicBuckets.getInstance().getConfigManager().isGensInWilderness();

        return MPlayer.get(player).isInOwnTerritory();
    }

}
