package com.songoda.epicbuckets.genbuckets.types;

import com.songoda.epicbuckets.events.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbuckets.Genbucket;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.genbuckets.GenbucketType;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

/**
 * PsuedoVertical created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:40
 */
public class PsuedoVertical extends Genbucket {

    private Location genbucketLocation;
    private int movedBlocks = 1;
    private Player player;
    private BlockFace blockFace;
    private GenbucketItem genbucketItem;
    private UUID genbucketUUID;

    /**
     * Creating the class constructor for a PsuedoVertical genbucket
     *
     * @param player            the player who placed the genbucket
     * @param genbucketLocation the location of where the genbucket was placed
     * @param blockFace         the blockface received from the {GenbucketPlaceListener#onRightClickBlock}
     * @param genbucketItem     the genbucket that was placed
     */
    public PsuedoVertical(Player player, Location genbucketLocation, BlockFace blockFace, GenbucketItem genbucketItem) {
        super(GenbucketType.PSUEDO);

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

        // Get the material the player clicked the genbucket on
        final Material clickedMaterial = genbucketLocation.getBlock().getType();

        boolean validMaterial = false;

        // This is a list of allowed material from the config
        List<String> materialList = main.getConfig().getStringList("PSUEDO-MATERIALS");

        // In the for loop we simply check if the material is
        // valid if it is then validMaterial is set to true
        for (String string : materialList) {

            if (string.toUpperCase().equalsIgnoreCase(genbucketItem.getType().name().toUpperCase()))
                validMaterial = true;
        }

        // If the material isn't valid we tell the player and stop the code
        if (!validMaterial || !clickedMaterial.equals(genbucketItem.getType())) {
            player.sendMessage(ChatUtil.colorPrefix(main.messageFile.config.getString("WRONG-MATERIAL-PSUEDO")));
            return;
        }

        // Region support
        if (!canPlace(player, genbucketLocation))
            return;

        // This checks if the player can place more
        // genbuckets or if he has placed max
        if (!canPlayerPlaceAGenbucket(player))
            return;

        // Tries to withdrawMoney, if successful then it
        // returns true
        if (!withdrawMoney(player, genbucketItem))
            return;

        // Delay from config
        long delay = main.getConfig().getInt("DELAY");

        // Now we can start spawning the blocks

        // If any admins (or players with permission) has enabled the
        // admin mode we will notify them
        notifyAdmins(player);

        // Call in the {@link GenbucketPlaceEvent}
        GenbucketPlaceEvent genbucketPlaceEvent = new GenbucketPlaceEvent(player, genbucketLocation, blockFace, genbucketItem, genbucketUUID);

        Bukkit.getPluginManager().callEvent(genbucketPlaceEvent);

        if (genbucketPlaceEvent.isCancelled())
            return;

        new BukkitRunnable() {

            public void run() {

                final Block block = genbucketLocation.clone().subtract(0, movedBlocks, 0).getBlock();

                if (foundSponge(block.getLocation())) {

                    genbucketFinished(player, genbucketUUID);
                    this.cancel();
                    return;
                }

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

                    //if (block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.SAND) || block.getType().equals(Material.COBBLESTONE) && genbucketItem.getType().equals(Material.GRAVEL))
                    //block.setType(Material.AIR);

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


                if (block.getType().equals(Material.AIR) || block.getType().equals(Material.WATER) || block.getType().equals(Material.STATIONARY_WATER)
                        || block.getType().equals(Material.LAVA) || block.getType().equals(Material.STATIONARY_LAVA)) {

                    block.setType(genbucketItem.getType());
                    block.setData(genbucketItem.getTypeDamage());

                } else {

                    List<String> materialList = main.getConfig().getStringList("PSUEDO-MATERIALS");

                    boolean validMaterial = false;

                    for (String string : materialList) {

                        if (string.toUpperCase().equalsIgnoreCase(genbucketItem.getType().name().toUpperCase()))
                            validMaterial = true;
                    }

                    if (!validMaterial || !block.getType().equals(clickedMaterial)) {
                        //ChatUtil.debugMSG(player, "Last block: " + block.getType(), "Material:" + genbucketItem.getType());
                        genbucketFinished(player, genbucketUUID);
                        this.cancel();
                    }

                    //ChatUtil.debugMSG(player, block.getType().name(), validMaterial, movedBlocks);

                }

                movedBlocks++;

            }

        }.runTaskTimer(main, 0L, delay);


    }
}
