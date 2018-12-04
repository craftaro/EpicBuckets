package com.songoda.epicbuckets.genbucket;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GenbucketItem {

    private UUID genUUID;
    private Player owner;
    private Genbucket genbucket;
    private Location locationPlaced;
    private BlockFace blockFace;

    public GenbucketItem(Player owner, Genbucket genbucket, Location locationPlaced, BlockFace blockFace) {
        this.genUUID = UUID.randomUUID();
        this.owner = owner;
        this.genbucket = genbucket;
        this.locationPlaced = locationPlaced;
        this.blockFace = blockFace;
    }

    public UUID getGenUUID() {
        return genUUID;
    }

}
