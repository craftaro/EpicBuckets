package com.songoda.epicbuckets.genbuckets;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.regionhandlers.*;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Genbucket created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:51
 */
public abstract class Genbucket {

    protected EpicBuckets main = EpicBuckets.getInstance();

    private GenbucketType genbucketType;

    public Genbucket(GenbucketType genbucketType) {

        this.genbucketType = genbucketType;

    }

    public abstract void run();

    public EpicBuckets getInstance() {
        return main;
    }

    protected void genbucketFinished(Player player, UUID uuid) {

        int a = GenbucketManager.getActiveGenBuckets(player);

        if (a - 1 < 0)
            GenbucketManager.setActiveGenBuckets(player, 0);
        else
            GenbucketManager.setActiveGenBuckets(player, a - 1);

        if (GenbucketManager.activeGenbucketItems.containsKey(uuid))
            GenbucketManager.activeGenbucketItems.remove(uuid);

        if (GenbucketManager.activeGenbucketLocation.containsKey(uuid))
            GenbucketManager.activeGenbucketLocation.remove(uuid);

    }

    protected boolean canPlace(Player player, Location location) {

        boolean factionsCheck = RegionFactions.canBuild(player, location);
        boolean factionsUUIDCheck = RegionFactions.canBuild(player, location);
        boolean griefPreventionCheck = RegionGriefPrevention.canBuild(player, location);
        boolean worldGuardCheck = RegionWorldGuard.canBuild(player, location);
        boolean worldBorderCheck = RegionWBorder.isOutsideOfBorder(location);

        //ChatUtil.debugMSG(player, factionsCheck, factionsUUIDCheck, griefPreventionCheck, worldGuardCheck, worldBorderCheck);

        if (!factionsCheck || !factionsUUIDCheck || !griefPreventionCheck || !worldGuardCheck || worldBorderCheck) {
            player.sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("YOU-CANNOT-PLACE-HERE")));
            return false;
        }

        return true;
    }

    protected boolean canPlace(Player player, Location location, boolean sendMessage) {

        boolean factionsCheck = RegionFactions.canBuild(player, location);
        boolean factionsUUIDCheck = RegionFactions.canBuild(player, location);
        boolean griefPreventionCheck = RegionGriefPrevention.canBuild(player, location);
        boolean worldGuardCheck = RegionWorldGuard.canBuild(player, location);
        boolean worldBorderCheck = RegionWBorder.isOutsideOfBorder(location);


        if (!factionsCheck || !factionsUUIDCheck || !griefPreventionCheck || !worldGuardCheck || worldBorderCheck) {

            if (sendMessage)
                player.sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("YOU-CANNOT-PLACE-HERE")));

            return false;

        }

        return true;
    }

    private void removeBucket(Player player, ItemStack item) {

        int genBuckets = item.getAmount();

        if (genBuckets > 1)
            item.setAmount(genBuckets - 1);
        else
            player.getInventory().clear(player.getInventory().getHeldItemSlot());


        player.updateInventory();
    }

    protected boolean canPlayerPlaceAGenbucket(Player player) {

        if (GenbucketManager.getActiveGenBuckets(player) >= maxActiveGenForPlayer(player) + 1) {

            player.sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("YOU-MUST-WAIT")));
            return false;

        }

        return true;

    }

    protected void notifyAdmins(Player target) {

        for (UUID uuid : GenbucketManager.adminList) {

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (!offlinePlayer.isOnline())
                continue;

            offlinePlayer.getPlayer().sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("ADMIN-MESSAGE").replace("{player}", target.getName()).replace("{type}", genbucketType.toString().toLowerCase())));

        }

    }

    protected boolean withdrawMoney(Player player, GenbucketItem item) {

        boolean useInfinityGens = main.getConfig().getBoolean("INFINITE-USE");

        if (!useInfinityGens) {
            removeBucket(player, player.getInventory().getItem(player.getInventory().getHeldItemSlot()));
            return true;
        }

        double playerBalance = main.getBalance(player);

        if (playerBalance >= item.getPrice()) {

            main.withdrawBalance(player, item.getPrice(), true);

            return true;

        } else {

            player.sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("NOT-ENOUGH-MONEY").replace("{money}", String.valueOf((playerBalance - item.getPrice()) * -1))));

            return false;
        }

    }

    private int maxActiveGenForPlayer(Player player) {

        int maxActiveGenForPlayer = 0;

        boolean foundValue = false;

        for (String maxAmountString : main.getConfig().getConfigurationSection("CUSTOM-ACTIVE-GEN-PER-PLAY").getKeys(false)) {

            String value[] = main.getConfig().getString("CUSTOM-ACTIVE-GEN-PER-PLAY." + maxAmountString).split(":");

            if (!player.hasPermission(value[1]))
                continue;

            maxActiveGenForPlayer = Integer.valueOf(value[0]);
            foundValue = true;
            break;

        }

        if (!foundValue)
            maxActiveGenForPlayer = main.getConfig().getInt("MAX-ACTIVE-GEN-PER-PLAYER");

        //ChatUtil.debugMSG(player, maxActiveGenForPlayer, foundValue);

        return maxActiveGenForPlayer;

    }

    protected boolean foundSponge(Location loc) {

        boolean useSponge = main.getConfig().getBoolean("USE-SPONGE-SUPPORT");

        if (!useSponge)
            return false;

        int radius = main.getConfig().getInt("SPONGE-RADIUS");

        if (radius < 0)
            return false;

        for (double x = loc.getX() - radius; x < loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y < loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z < loc.getZ() + radius; z++) {

                    Material materialAt = new Location(loc.getWorld(), x, y, z).getBlock().getType();

                    if (materialAt.equals(Material.SPONGE))
                        return true;
                }

            }

        }

        return false;
    }

    protected Block moveBlock(Block b, BlockFace blockFace, int length) {

        switch (blockFace) {
            case NORTH:
                return b.getRelative(0, 0, -length);
            case SOUTH:
                return b.getRelative(0, 0, length);
            case EAST:
                return b.getRelative(length, 0, 0);
            case WEST:
                return b.getRelative(-length, 0, 0);
        }
        return null;
    }

    protected Block getLastBlockPlaced(Block b, BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return b.getRelative(0, 0, -1);
            case SOUTH:
                return b.getRelative(0, 0, 1);
            case EAST:
                return b.getRelative(1, 0, 0);
            case WEST:
                return b.getRelative(-1, 0, 0);
        }
        return null;
    }
}
