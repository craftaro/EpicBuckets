package com.songoda.epicbuckets.genbuckets.types;

import com.songoda.epicbuckets.events.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbuckets.Genbucket;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.genbuckets.GenbucketType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Vertical created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:39
 */
public class Vertical extends Genbucket {

    private Location genbucketLocation;
    private int movedBlocks = 1;
    private Player player;
    private BlockFace blockFace;
    private GenbucketItem genbucketItem;
    private UUID genbucketUUID;
    private long delay = plugin.getConfig().getInt("DELAY");

    /**
     * Creating the class constructor for a Vertical genbucket
     *
     * @param player            the player who placed the genbucket
     * @param genbucketLocation the location of where the genbucket was placed
     * @param blockFace         the blockface received from the {GenbucketPlaceListener#onRightClickBlock}
     * @param genbucketItem     the genbucket that was placed
     */
    public Vertical(Player player, Location genbucketLocation, BlockFace blockFace, GenbucketItem genbucketItem) {
        super(GenbucketType.VERTICAL);

        this.genbucketLocation = genbucketLocation;
        this.player = player;
        this.blockFace = blockFace;
        this.genbucketItem = genbucketItem;
        this.genbucketUUID = UUID.randomUUID();

        // Call this to run the genbucket
        run();
    }

    @Override
    public void run() {

        if (!canPlace(player, genbucketLocation))
            return;

        if (!canPlayerPlaceAGenbucket(player))
            return;

        if (!withdrawMoney(player, genbucketItem))
            return;

        // Now we can start spawning the blocks

        notifyAdmins(player);

        GenbucketPlaceEvent genbucketPlaceEvent = new GenbucketPlaceEvent(player, genbucketLocation, blockFace, genbucketItem, genbucketUUID);

        Bukkit.getPluginManager().callEvent(genbucketPlaceEvent);

        if (genbucketPlaceEvent.isCancelled())
            return;

        /**
         * Compared to the other genbuckets the veritcal genbucket
         * can be placed in 3 different locations. Therefore, we cannot
         * use the same method as we've done with the other genbuckettypes.
         */

        switch (blockFace) {

            case DOWN:
                directionDown();
                break;

            case UP:
                directionUP();
                break;

            default:
                directionSide();
                break;

        }

    }

    private void directionSide() {

        final Block firstBlock = getLastBlockPlaced(genbucketLocation.getBlock(), blockFace);

        // This is needed to stop sand/gravel genbuckets
        // to destroy bedrock
        if (firstBlock.getLocation().getBlockY() <= 1)
            return;

        new BukkitRunnable() {

            public void run() {

                final Block block = firstBlock.getLocation().clone().subtract(0, movedBlocks - 1, 0).getBlock();

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

                if (movedBlocks > genbucketItem.getVerticalHeight()) {

                    if (block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.SAND) || block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.GRAVEL))
                        block.setType(Material.AIR);

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;

                }

                if (!canPlace(player, block.getLocation())) {

                    //ChatUtil.debugMSG(player, "done from canPlace()");

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;
                }

                boolean run = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(block.getType().name());

                if (!run && genbucketItem.getType().equals(Material.SAND) || !run && genbucketItem.getType().equals(Material.GRAVEL) || run) {

                    //ChatUtil.debugMSG(player, genbucketItem.getType(), genbucketItem.getTypeDamage(), movedBlocks);

                    if (genbucketItem.getType().equals(Material.SAND) || genbucketItem.getType().equals(Material.GRAVEL)) {

                        block.setType(genbucketItem.getType());

                        Block block_ = block.getLocation().clone().subtract(0, 1, 0).getBlock();
                        block_.setType(Material.COBBLESTONE);

                        Block blockUnderCobbleStone = block.getLocation().clone().subtract(0, 2, 0).getBlock();
                        boolean ignoreBlock = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(blockUnderCobbleStone.getType().name());

                        if (!ignoreBlock) {
                            block_.setType(genbucketItem.getType());
                            genbucketFinished(player, genbucketUUID);
                            this.cancel();
                            return;
                        } else {
                            block.setType(genbucketItem.getType());
                            block.setData(genbucketItem.getTypeDamage());
                        }

                    } else {
                        block.setType(genbucketItem.getType());
                        block.setData(genbucketItem.getTypeDamage());
                    }

                    movedBlocks++;

                } else {

                    //ChatUtil.debugMSG(player, "Last block: " + block.getType(), "Material:" + genbucketItem.getType());
                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                }

            }

        }.runTaskTimer(plugin, 0L, delay);

    }

    private void directionUP() {

        new BukkitRunnable() {

            public void run() {

                final Block block = genbucketLocation.clone().add(0, movedBlocks, 0).getBlock();

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

                if (movedBlocks > genbucketItem.getVerticalHeight() || block.getY() >= 257) {

                    if (block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.SAND) || block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.GRAVEL))
                        block.setType(Material.AIR);

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;

                }

                if (!canPlace(player, block.getLocation())) {

                    //ChatUtil.debugMSG(player, "done from canPlace()");

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;
                }

                boolean run = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(block.getType().name());

                if (!run && genbucketItem.getType().equals(Material.SAND) || !run && genbucketItem.getType().equals(Material.GRAVEL) || run) {

                    //ChatUtil.debugMSG(player, genbucketItem.getType(), genbucketItem.getTypeDamage(), movedBlocks);

                    if (genbucketItem.getType().equals(Material.SAND) || genbucketItem.getType().equals(Material.GRAVEL)) {

                        block.setType(genbucketItem.getType());

                        Block block_ = block.getLocation().clone().add(0, 1, 0).getBlock();
                        block_.setType(Material.COBBLESTONE);

                        Block blockUnderCobbleStone = block.getLocation().clone().add(0, 2, 0).getBlock();
                        boolean ignoreBlock = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(blockUnderCobbleStone.getType().name());

                        if (!ignoreBlock) {
                            block_.setType(genbucketItem.getType());
                            genbucketFinished(player, genbucketUUID);
                            this.cancel();
                            return;
                        } else {
                            block.setType(genbucketItem.getType());
                            block.setData(genbucketItem.getTypeDamage());
                        }

                    } else {
                        block.setType(genbucketItem.getType());
                        block.setData(genbucketItem.getTypeDamage());
                    }

                    movedBlocks++;

                } else {

                    //ChatUtil.debugMSG(player, "Last block: " + block.getType(), "Material:" + genbucketItem.getType());
                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                }

            }

        }.runTaskTimer(plugin, 0L, delay);
    }

    private void directionDown() {

        // This is needed to stop sand/gravel genbuckets
        // to destroy bedrock
        if (genbucketLocation.getBlockY() <= 3)
            return;

        new BukkitRunnable() {

            public void run() {

                final Block block = genbucketLocation.clone().subtract(0, movedBlocks, 0).getBlock();

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

                if (movedBlocks > genbucketItem.getVerticalHeight()) {

                    if (block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.SAND) || block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.GRAVEL))
                        block.setType(Material.AIR);

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;

                }

                if (!canPlace(player, block.getLocation())) {

                    //ChatUtil.debugMSG(player, "done from canPlace()");

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;
                }

                boolean run = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(block.getType().name());

                if (!run && genbucketItem.getType().equals(Material.SAND) || !run && genbucketItem.getType().equals(Material.GRAVEL) || run) {

                    //ChatUtil.debugMSG(player, genbucketItem.getType(), genbucketItem.getTypeDamage(), movedBlocks);

                    if (genbucketItem.getType().equals(Material.SAND) || genbucketItem.getType().equals(Material.GRAVEL)) {

                        block.setType(genbucketItem.getType());

                        Block block_ = block.getLocation().clone().subtract(0, 1, 0).getBlock();
                        block_.setType(Material.COBBLESTONE);

                        Block blockUnderCobbleStone = block.getLocation().clone().subtract(0, 2, 0).getBlock();
                        boolean ignoreBlock = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(blockUnderCobbleStone.getType().name());

                        if (!ignoreBlock) {
                            block_.setType(genbucketItem.getType());
                            genbucketFinished(player, genbucketUUID);
                            this.cancel();
                            return;
                        } else {
                            block.setType(genbucketItem.getType());
                            block.setData(genbucketItem.getTypeDamage());
                        }

                    } else {
                        block.setType(genbucketItem.getType());
                        block.setData(genbucketItem.getTypeDamage());
                    }

                    movedBlocks++;

                } else {

                    //ChatUtil.debugMSG(player, "Last block: " + block.getType(), "Material:" + genbucketItem.getType());
                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                }

            }

        }.runTaskTimer(plugin, 0L, delay);


    }


}
