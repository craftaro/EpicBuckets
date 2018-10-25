package com.songoda.epicbuckets.genbuckets.types;

import com.songoda.epicbuckets.events.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbuckets.Genbucket;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.genbuckets.GenbucketType;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Infused created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:40
 */
public class Infused extends Genbucket {

    private Location genbucketLocation;
    private int movedBlocks = 1;
    private Player player;
    private BlockFace blockFace;
    private GenbucketItem genbucketItem;
    private boolean runPillarOne = true;
    private boolean runPillarTwo = true;
    private UUID genbucketUUID;

    /**
     * Creating the class constructor for a Infused genbucket
     *
     * @param player            the player who placed the genbucket
     * @param genbucketLocation the location of where the genbucket was placed
     * @param blockFace         the blockface received from the {GenbucketPlaceListener#onRightClickBlock}
     * @param genbucketItem     the genbucket that was placed
     */
    public Infused(Player player, Location genbucketLocation, BlockFace blockFace, GenbucketItem genbucketItem) {
        super(GenbucketType.INFUSED);

        this.blockFace = blockFace;
        this.genbucketLocation = genbucketLocation;
        this.genbucketItem = genbucketItem;
        this.player = player;
        this.genbucketUUID = UUID.randomUUID();

        // Call this to run the genbucket
        run();
    }

    @Override
    public void run() {

        if (!"UP".equalsIgnoreCase(blockFace.name())) {
            player.sendMessage(ChatUtil.colorPrefix(plugin.getLocale().getMessage("event.genbucket.placedwrong", plugin.getLocale().getMessage("event.translate.directionup", "{genbucket}", ChatUtil.stripColor(genbucketItem.getItemName())))));
            return;
        }

        final Block[] blocks = getBlocks(genbucketLocation.getBlock(), blockFace, player);

        final Block blockL = blocks[0];
        final Block blockR = blocks[1];

        if (!canPlace(player, blockL.getLocation()) || !canPlace(player, blockR.getLocation()))
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

                Block blockOne = blocks[0].getLocation().subtract(0, movedBlocks - 1, 0).getBlock();
                Block blockTwo = blocks[1].getLocation().subtract(0, movedBlocks - 1, 0).getBlock();

                if (foundSponge(blockOne.getLocation()))
                    runPillarOne = false;

                if (foundSponge(blockTwo.getLocation()))
                    runPillarTwo = false;

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

                boolean runFirst = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(blockOne.getType().name());

                if (runPillarOne && runFirst && canPlace(player, blockOne.getLocation(), false)) {

                    blockOne.setType(genbucketItem.getType());
                    if (!plugin.isServerVersionAtLeast(ServerVersion.V1_13))
                        blockOne.setData(genbucketItem.getTypeDamage());

                } else
                    runPillarOne = false;

                boolean runSecond = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(blockTwo.getType().name());

                if (runPillarTwo && runSecond && canPlace(player, blockTwo.getLocation(), false)) {

                    blockTwo.setType(genbucketItem.getType());
                    if (!plugin.isServerVersionAtLeast(ServerVersion.V1_13))
                        blockTwo.setData(genbucketItem.getTypeDamage());

                } else
                    runPillarTwo = false;

                movedBlocks++;

                if (!canPlace(player, blockOne.getLocation(), true) && !canPlace(player, blockTwo.getLocation(), false)) {

                    genbucketFinished(player, genbucketUUID);

                    this.cancel();
                    return;
                }

                Block nextBlockOne = blockOne.getLocation().clone().subtract(0, 1, 0).getBlock();
                Block nextBlockTwo = blockTwo.getLocation().clone().subtract(0, 1, 0).getBlock();

                boolean checkNextBlock = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(nextBlockOne.getType().name());
                boolean checkNextBlock_ = plugin.getConfig().getStringList("IGNORE-MATERIALS").contains(nextBlockTwo.getType().name());

                if (!checkNextBlock && !checkNextBlock_ || !runPillarOne && !runPillarTwo) {

                    //ChatUtil.debugMSG(player, nextBlockOne.getType().name(), nextBlockTwo.getType().name());

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();

                }

            }

        }.runTaskTimer(plugin, 0L, delay);

    }

    private Block[] getBlocks(Block block, BlockFace blockFace, Player player) {

        if ("NORTH".equalsIgnoreCase(blockFace.name()) || "SOUTH".equalsIgnoreCase(blockFace.name())) {
            Block b1 = block.getRelative(blockFace, 1); // Left
            Block b2 = block.getRelative(blockFace, -1); // Right

            return new Block[]{b1, b2};
        } else if ("EAST".equalsIgnoreCase(blockFace.name()) || "WEST".equalsIgnoreCase(blockFace.name())) {
            Location loc1 = block.getLocation();
            Location loc2 = block.getLocation();
            loc1.setX(loc1.getX() + 1);
            loc2.setX(loc1.getX() - 2);

            Block b1 = loc1.getBlock();
            Block b2 = loc2.getBlock();

            return new Block[]{b1, b2};
        }

        if ("e".equalsIgnoreCase(getDirection(player)) || "w".equalsIgnoreCase(getDirection(player))) {
            Location getLeftSide = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
            Location getRightSide = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() - 1);

            return new Block[]{getLeftSide.getBlock(), getRightSide.getBlock()};
        } else if (!"e".equalsIgnoreCase(getDirection(player)) || !"w".equalsIgnoreCase(getDirection(player)) && getDirection(player) != null) {
            Location getLeftSide = new Location(player.getWorld(), block.getX() + 1, block.getY(), block.getZ());
            Location getRightSide = new Location(player.getWorld(), block.getX() - 1, block.getY(), block.getZ());

            return new Block[]{getLeftSide.getBlock(), getRightSide.getBlock()};
        }

        return null;

    }

    private String getDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 180) % 360;
        if (rot < 0) {
            rot += 360.0;
        }

        if (0 <= rot && rot < 22.5) {
            return "N";
        } else if (67.5 <= rot && rot < 112.5) {
            return "E";
        } else if (157.5 <= rot && rot < 202.5) {
            return "S";
        } else if (247.5 <= rot && rot < 292.5) {
            return "W";
        } else if (337.5 <= rot && rot < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
}
