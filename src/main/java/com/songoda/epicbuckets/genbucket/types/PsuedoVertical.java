package com.songoda.epicbuckets.genbucket.types;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PsuedoVertical extends Genbucket {

    public PsuedoVertical(Player owner, Block clickedBlock, BlockFace blockFace, SubShop s) {
        super(GenbucketType.PSUEDO, clickedBlock, blockFace, s, owner);
    }

    @Override
    public void generate() {
        Bukkit.getServer().getScheduler().runTaskTimer(EpicBuckets.getInstance(), new Runnable() {
            @Override
            public void run() {

            }
        }, 0, epicBuckets.getConfigManager().getDelay());
    }
}
