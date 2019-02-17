package com.songoda.epicbuckets.genbucket.types;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Infused extends Genbucket {

    private int blocksPlaced = 0;
    private boolean side1 = true;
    private boolean side2 = true;

    public Infused(Player owner, Block clickedBlock, BlockFace blockFace, SubShop s) {
        super(GenbucketType.INFUSED, clickedBlock, blockFace, s, owner);
    }

    @Override
    public void generate() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!epicBuckets.getGenbucketManager().isGenbucketActive(getGenUUID())) {
                    cancel();
                    return;
                }
//                if (isGravityGen()) {
//                        if (!side1 && !side2) {
//                            epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
//                            cancel();
//                            return;
//                        }
//                        if (side1 && !gravityGenInfused(blocksPlaced, getBlockFace())) {
//                            side1 = false;
//                        }
//                        if (side2 && !gravityGenInfused(blocksPlaced, getBlockFace().getOppositeFace())) {
//                            side2 = false;
//                        }
//                } else {
                if ((!side1 && !side2) || blocksPlaced >= epicBuckets.getConfigManager().getMaxVerticalHeight()) {
                    epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
                    cancel();
                    return;
                }
                if (side1 && !placeGen(getNextBlock(blocksPlaced, getBlockFace()))) {
                    side1 = false;
                }
                if (side2 && !placeGen(getNextBlock(blocksPlaced, getBlockFace().getOppositeFace()))) {
                    side2 = false;
                }
                blocksPlaced++;
//                }
            }
        };
        setGeneration(runnable.runTaskTimer(EpicBuckets.getInstance(), 0, epicBuckets.getConfigManager().getDelay()));
    }
}
