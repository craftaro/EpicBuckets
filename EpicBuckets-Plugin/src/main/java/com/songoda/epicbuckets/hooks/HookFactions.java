package com.songoda.epicbuckets.hooks;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.utils.hooks.ClaimableProtectionPluginHook;
import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.FPlayers;
import me.markeh.factionsframework.entities.Faction;
import me.markeh.factionsframework.entities.Factions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HookFactions implements ClaimableProtectionPluginHook {

    private final FactionsFramework factions;

    public HookFactions() {
        this.factions = FactionsFramework.get();
    }

    @Override
    public JavaPlugin getPlugin() {
        return factions;
    }

    @Override
    public boolean canBuild(Player player, Location location) {
        FPlayer fPlayer = FPlayers.getBySender(player);
        Faction faction = Factions.getFactionAt(location);

        if (faction.isNone()) return false;

        return fPlayer.getFaction().equals(faction);
    }

    @Override
    public boolean isInClaim(Location location, String id) {
        return Factions.getFactionAt(location).getId().equals(id);
    }

    @Override
    public String getClaimID(String name) {
        Faction faction = Factions.getByName(name, "");
        return (faction != null) ? faction.getId() : null;
    }

}