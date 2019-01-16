package com.songoda.epicbuckets.regionhandler;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsMassive {

    public static boolean check(Player player, Location location) {
        Faction f = BoardColl.get().getFactionAt(PS.valueOf(location));
        /*
        If wilderness
         */
        if (f == FactionColl.get().getNone()) return EpicBuckets.getInstance().getConfigManager().isGensInWilderness();

        /*
        If player has no faction
         */
        if (!MPlayer.get(player).hasFaction()) return false;
        return MPlayer.get(player).getFactionName().equals(f.getName());
    }

}
