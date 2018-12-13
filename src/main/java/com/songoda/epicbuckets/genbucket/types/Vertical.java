package com.songoda.epicbuckets.genbucket.types;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Vertical extends Genbucket {

    private int blocksPlaced = 0;

    public Vertical(Player owner, Block clickedBlock, BlockFace blockFace, SubShop s) {
        super(GenbucketType.VERTICAL, clickedBlock, blockFace, s, owner);
    }

    @Override
    public void generate() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (isGravityGen()) {
                    if (!gravityGen(blocksPlaced)) {
                        epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
                        cancel();
                        return;
                    }
                } else if (isBelowVoid(blocksPlaced + 1) || blocksPlaced >= epicBuckets.getConfigManager().getMaxVerticalHeight() || !placeGen(getNextBlock())) {
                    epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
                    cancel();
                    return;
                }
                blocksPlaced++;
            }
        };
        setGeneration(runnable.runTaskTimer(EpicBuckets.getInstance(), 0, epicBuckets.getConfigManager().getDelay()));
    }
}
