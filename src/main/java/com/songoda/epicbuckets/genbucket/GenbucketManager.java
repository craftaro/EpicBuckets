package com.songoda.epicbuckets.genbucket;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.regionhandler.RegionFactions;
import com.songoda.epicbuckets.regionhandler.RegionGriefPrevention;
import com.songoda.epicbuckets.regionhandler.RegionWorldBorder;
import com.songoda.epicbuckets.regionhandler.RegionWorldGuard;
import com.songoda.epicbuckets.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GenbucketManager {

    private EpicBuckets epicBuckets;
    private HashMap<UUID, List<Genbucket>> activeGens;
    private List<Player> admins;

    public GenbucketManager() {
        this.epicBuckets = EpicBuckets.getInstance();
        activeGens = new HashMap<>();
        admins = new ArrayList<>();
    }

    public void notifyAdmins(Player user, Genbucket genbucket) {
        admins.forEach(player -> player.sendMessage(ChatUtil.colorPrefix(epicBuckets.getLocale().getMessage("event.genbucket.admin").replace("%player%", user.getName()).replace("%genbucket%", StringUtils.capitalize(genbucket.getGenbucketType().name.toLowerCase()) + " genbucket"))));
    }

    public void toggleAdmin(Player player) {
        if (admins.contains(player)) {
            removeAdmin(player);
            return;
        }
        addAdmin(player);
        player.sendMessage(ChatUtil.colorPrefix(epicBuckets.getLocale().getMessage("command.admin.toggle").replace("%mode%", ((admins.contains(player)) ? "&aenabled" : "&cdisabled"))));
    }

    private void removeAdmin(Player player) {
        admins.remove(player);
    }

    private void addAdmin(Player player) {
        admins.add(player);
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
            player.sendMessage(ChatUtil.colorPrefix(epicBuckets.getLocale().getMessage("event.place.nothere")));
            return false;
        }

        return true;
    }

}