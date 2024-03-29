package com.songoda.epicbuckets.genbucket.types;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PsuedoVertical extends Genbucket {

    private int blocksUp = 0;

    public PsuedoVertical(Player owner, Block clickedBlock, BlockFace blockFace, SubShop s) {
        super(GenbucketType.PSUEDO, clickedBlock, blockFace, s, owner);
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
                if (isBelowVoid(blocksUp) || blocksUp >= epicBuckets.getConfigManager().getMaxVerticalHeight()) {
                    epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
                    cancel();
                    return;
                }
                fixHole(getNextBlock());
                blocksUp++;
            }
        };
        setGeneration(runnable.runTaskTimer(EpicBuckets.getInstance(), 0, epicBuckets.getConfigManager().getDelay()));
    }
}
