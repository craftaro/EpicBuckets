package com.songoda.epicbuckets.genbucket.types;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Horizontal extends Genbucket {

    private int blocksPlaced = 0;

    public Horizontal(Player owner, Block clickedBlock, BlockFace blockFace, SubShop s) {
        super(GenbucketType.HORIZONTAL, clickedBlock, blockFace, s, owner);
    }

    @Override
    public void generate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (blocksPlaced >= epicBuckets.getConfigManager().getMaxHorizontalLength() || !placeGen(getNextBlock())) {
                    epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(getOwner(), getGenUUID());
                    cancel();
                }
                blocksPlaced++;
            }
        }.runTaskTimer(EpicBuckets.getInstance(), 0, epicBuckets.getConfigManager().getDelay());
    }
}
