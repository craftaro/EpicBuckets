package com.songoda.epicbuckets.genbuckets.types;


import com.songoda.epicbuckets.events.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbuckets.Genbucket;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.genbuckets.GenbucketType;
import com.songoda.epicbuckets.regionhandlers.RegionWBorder;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Horizontal created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:40
 */
public class Horizontal extends Genbucket {

    private Location genbucketLocation;
    private int movedBlocks = 1;
    private Player player;
    private BlockFace blockFace;
    private GenbucketItem genbucketItem;
    private UUID genbucketUUID;

    public Horizontal(Player player, Location genbucketLocation, BlockFace blockFace, GenbucketItem genbucketItem) {
        super(GenbucketType.HORIZONTAL);

        this.genbucketLocation = genbucketLocation;
        this.player = player;
        this.blockFace = blockFace;
        this.genbucketItem = genbucketItem;
        this.genbucketUUID = UUID.randomUUID();

        run();
    }

    @Override
    public void run() {

        if ("DOWN".equalsIgnoreCase(blockFace.name()) || "UP".equalsIgnoreCase(blockFace.name())) {
            player.sendMessage(ChatUtil.colorPrefix(plugin.getLocale().getMessage("event.genbucket.placedwrong", plugin.getLocale().getMessage("event.translate.directionside", ChatUtil.stripColor(genbucketItem.getItemName())))));
            return;
        }

        if (!canPlace(player, genbucketLocation))
            return;

        if (!canPlayerPlaceAGenbucket(player))
            return;

        if (!withdrawMoney(player, genbucketItem))
            return;

        long delay = plugin.getConfig().getInt("DELAY");

        // Now we can start spawning the blocks

        notifyAdmins(player);

        GenbucketPlaceEvent genbucketPlaceEvent = new GenbucketPlaceEvent(player, genbucketLocation, blockFace, genbucketItem, genbucketUUID);

        Bukkit.getPluginManager().callEvent(genbucketPlaceEvent);

        if (genbucketPlaceEvent.isCancelled())
            return;

        new BukkitRunnable() {

            public void run() {

                final Block block = moveBlock(genbucketLocation.getBlock(), blockFace, movedBlocks);

                if (foundSponge(block.getLocation())) {

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;
                }

                if (!GenbucketManager.activeGenbucketItems.containsKey(genbucketUUID)) {
                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;
                }

                if (movedBlocks > genbucketItem.getHorizontalLength()) {

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;

                }

                if (!canPlace(player, block.getLocation())) {

                    //ChatUtil.debugMSG(player, "done from canPlace()");

                    if (RegionWBorder.isOutsideOfBorder(block.getLocation())) {

                        if (blockFace.name().equalsIgnoreCase("SOUTH") ||
                                blockFace.name().equalsIgnoreCase("EAST"))
                            moveBlock(genbucketLocation.getBlock(), blockFace, movedBlocks - 1).setType(Material.AIR);
                    }

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;
                }

                boolean run = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(block.getType().name());

                if (run) {

                    //ChatUtil.debugMSG(player, genbucketItem.getType(), genbucketItem.getTypeDamage(), movedBlocks);

                    block.setType(genbucketItem.getType());
                    block.setData(genbucketItem.getTypeDamage());
                    movedBlocks++;

                } else {
                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                }

            }

        }.runTaskTimer(plugin, 0L, delay);

    }

}
