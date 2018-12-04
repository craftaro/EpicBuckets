package com.songoda.epicbuckets.genbucket;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class Genbucket {

    protected EpicBuckets epicBuckets;
    private Player owner;
    private GenbucketType genbucketType;
    private BlockFace blockFace;
    private Block clickedBlock;
    private Block currentBlock;
    private SubShop subShop;
    private UUID genUUID;

    public Genbucket(GenbucketType genbucketType, Block clickedBlock, BlockFace blockFace, SubShop s, Player owner) {
        epicBuckets = EpicBuckets.getInstance();
        this.genUUID = UUID.randomUUID();
        this.owner = owner;
        this.genbucketType = genbucketType;
        this.blockFace = blockFace;
        this.clickedBlock = clickedBlock;
        this.currentBlock = clickedBlock;
        this.subShop = s;
    }

    public abstract void generate();

    public Player getOwner() {
        return owner;
    }

    public UUID getGenUUID() {
        return genUUID;
    }

    public boolean isValidBlockFace() {
        switch(genbucketType) {
            case VERTICAL:
            case PSUEDO:
                if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) return true;
                return false;
            case HORIZONTAL:
            case INFUSED:
                if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) return false;
                return true;
            default:
                return false;
        }
    }

    protected Block getNextBlock(int moved, BlockFace blockFace) {
        return clickedBlock.getRelative(blockFace).getRelative(0, moved, 0);
    }

    protected Block getNextBlock() {
        currentBlock = currentBlock.getRelative(getBlockFace());
        return currentBlock;
    }

    protected boolean placeGen(Block block) {
        if (!epicBuckets.getConfigManager().getIgnoredMaterials().contains(block.getType().name())) return false;
        block.setType(getGenItem().getType());
        return true;
    }

    protected ItemStack getGenItem() {
        return subShop.getGenItem();
    }

    protected BlockFace getBlockFace() {
        return blockFace;
    }

}
