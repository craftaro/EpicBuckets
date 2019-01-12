package com.songoda.epicbuckets.regionhandler;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsSavage {

    public static boolean check(Player player, Location location) {
        Faction f;
        f = Board.getInstance().getFactionAt(new FLocation(location));

        if (f == null) return EpicBuckets.getInstance().getConfigManager().isGensInWilderness();

        if (FPlayers.getInstance().getByPlayer(player).getFaction() == null) return false;
        return FPlayers.getInstance().getByPlayer(player).getFaction().getTag().equals(f.getTag());
    }

}
