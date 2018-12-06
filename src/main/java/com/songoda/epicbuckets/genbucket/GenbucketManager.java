package com.songoda.epicbuckets.genbucket;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.regionhandler.RegionFactions;
import com.songoda.epicbuckets.regionhandler.RegionGriefPrevention;
import com.songoda.epicbuckets.regionhandler.RegionWorldBorder;
import com.songoda.epicbuckets.regionhandler.RegionWorldGuard;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GenbucketManager {

    private EpicBuckets epicBuckets;
    private HashMap<UUID, List<Genbucket>> activeGens;

    public GenbucketManager() {
        this.epicBuckets = EpicBuckets.getInstance();
        activeGens = new HashMap<>();
    }

    public void unregisterGenbucketForPlayer(Player owner, UUID genUUID) {
        List<Genbucket> genbuckets = activeGens.get(owner.getUniqueId());
        List<Genbucket> newGenbuckets = new ArrayList<>();
        genbuckets.stream().filter(genbucket -> genbucket.getGenUUID() != genUUID).forEach(newGenbuckets::add);
        activeGens.put(owner.getUniqueId(), newGenbuckets);
    }

    public void registerGenbucketForPlayer(Player owner, Genbucket genbucket) {
        if (!activeGens.containsKey(owner.getUniqueId())) activeGens.put(owner.getUniqueId(), new ArrayList<>(Arrays.asList(genbucket)));
        List<Genbucket> genbucketItems = activeGens.get(owner.getUniqueId());
        genbucketItems.add(genbucket);
        activeGens.put(owner.getUniqueId(), genbucketItems);
    }

    public boolean canRegisterNewGenbucket(Player owner) {
        if (!activeGens.containsKey(owner.getUniqueId())) return true;
        if (activeGens.get(owner.getUniqueId()).size() < getMaxGenbucketsForPlayer(owner)) return true;
        return false;
    }

    public int getMaxGenbucketsForPlayer(Player owner) {
        LinkedHashMap<String, Integer> groups = epicBuckets.getConfigManager().getGenbucketGroups();
        for (String perm : groups.keySet()) {
            if (owner.hasPermission(perm)) return groups.get(perm);
        }
        return epicBuckets.getConfigManager().getMaxGenbucketsPerPlayer();
    }

    public boolean canPlaceGenbucket(Player player, Location location) {
        boolean factionsCheck = RegionFactions.canBuild(player, location);
        boolean factionsUUIDCheck = RegionFactions.canBuild(player, location);
        boolean griefPreventionCheck = RegionGriefPrevention.canBuild(player, location);
        boolean worldGuardCheck = RegionWorldGuard.canBuild(player, location);
        boolean worldBorderCheck = RegionWorldBorder.isOutsideOfBorder(location);

        if (!factionsCheck || !factionsUUIDCheck || !griefPreventionCheck || !worldGuardCheck || worldBorderCheck) {
            player.sendMessage(epicBuckets.getLocale().getMessage("event.place.nothere"));
            return false;
        }

        return true;
    }

}
