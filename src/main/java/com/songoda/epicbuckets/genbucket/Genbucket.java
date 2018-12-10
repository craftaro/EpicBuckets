package com.songoda.epicbuckets.genbucket;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private Location playerLocation;

    public Genbucket(GenbucketType genbucketType, Block clickedBlock, BlockFace blockFace, SubShop s, Player owner) {
        epicBuckets = EpicBuckets.getInstance();
        this.genUUID = UUID.randomUUID();
        this.owner = owner;
        this.genbucketType = genbucketType;
        this.blockFace = blockFace;
        this.clickedBlock = clickedBlock;
        this.currentBlock = clickedBlock;
        this.subShop = s;
        this.playerLocation = owner.getLocation();
    }

    public abstract void generate();

    public Player getOwner() {
        return owner;
    }

    public Location getPlayerLocation() {
        return playerLocation;
    }

    public Location getClickedLocation() {
        return clickedBlock.getLocation();
    }

    public UUID getGenUUID() {
        return genUUID;
    }

    public GenbucketType getGenbucketType() {
        return genbucketType;
    }

    public boolean isValidBlockFace() {
        return epicBuckets.getConfigManager().getValidFacesForGenbucket(getGenbucketType()).contains(getBlockFace());
    }

    public boolean calculateBlockFace() {
        if (!isValidBlockFace()) return false;
        if (!epicBuckets.getConfigManager().getLogicalFacesForGenbucket(getGenbucketType()).contains(getBlockFace())) {
            blockFace = epicBuckets.getConfigManager().getDefaultFaceForGenbucket(genbucketType);
        }
        return true;
    }

    protected boolean isBelowVoid(int moved) {
        if (blockFace != BlockFace.DOWN) return false;
        return clickedBlock.getRelative(0, -moved, 0).getLocation().getBlockY() == 0;
    }

    protected Block getNextBlock(int moved, BlockFace blockFace) {
        return clickedBlock.getRelative(blockFace).getRelative(0, moved, 0);
    }

    protected Block getNextBlock() {
        currentBlock = currentBlock.getRelative(getBlockFace());
        return currentBlock;
    }

    protected void fixHole(Block block) {
        if (block.getType() == XMaterial.AIR.parseMaterial()) block.setType(getGenItem().getType());
    }

    protected boolean spongeInRange(Block block) {
        if (!epicBuckets.getConfigManager().isSpongeCheck()) return false;
        int radius = (epicBuckets.getConfigManager().getSpongeRadius() - 1) / 2;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (block.getRelative(x, 0, z).getType() == XMaterial.SPONGE.parseMaterial()) return true;
            }
        }
        return false;
    }

    protected boolean placeGen(Block block) {
        if (!epicBuckets.getConfigManager().getIgnoredMaterials().contains(XMaterial.requestXMaterial(block.getType().name(), block.getData()))) return false;
        if (spongeInRange(block)) return false;
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
