package com.songoda.epicbuckets.genbucket;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.utils.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.UUID;

public abstract class Genbucket {

    protected EpicBuckets epicBuckets;
    private Player owner;
    private GenbucketType genbucketType;
    private BlockFace blockFace;
    private Block clickedBlock;
    private Block sourceBlock;
    private Block currentBlock;
    private SubShop subShop;
    private UUID genUUID;
    private Location playerLocation;
    private BukkitTask generation;
    private boolean isGravityGen;

    public Genbucket(GenbucketType genbucketType, Block clickedBlock, BlockFace blockFace, SubShop s, Player owner) {
        epicBuckets = EpicBuckets.getInstance();
        this.genUUID = UUID.randomUUID();
        this.owner = owner;
        this.genbucketType = genbucketType;
        this.blockFace = blockFace;
        this.clickedBlock = clickedBlock;
        this.currentBlock = clickedBlock;
        this.sourceBlock = clickedBlock.getRelative(blockFace);
        this.subShop = s;
        this.playerLocation = owner.getLocation();
        isGravityGen = s.getGenItem().getType().hasGravity();
    }

    public abstract void generate();

    public Block getSourceBlock() {
        return sourceBlock;
    }

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
            clickedBlock = sourceBlock.getRelative(getBlockFace().getOppositeFace());
            currentBlock = clickedBlock;
        }
        return true;
    }

    public BukkitTask getGeneration() {
        return generation;
    }

    public void setGeneration(BukkitTask task) {
        generation = task;
    }

    protected boolean isBelowVoid(int moved) {
        if (blockFace != BlockFace.DOWN) return false;
        return sourceBlock.getRelative(0, -moved, 0).getLocation().getBlockY() < 0;
    }

    protected boolean gravityGen(int moved) {
        Block b = getNextBlock();
        if (isBelowVoid(moved + 1)) return false;
        if (b.getRelative(getBlockFace()).getType() != Material.AIR) {
            if (b.getRelative(getBlockFace()).getType() != XMaterial.COBBLESTONE.parseMaterial()) {
                b.setType(getGenItem().getType());
                return false;
            }
        }
        b.getRelative(getBlockFace()).setType(XMaterial.COBBLESTONE.parseMaterial());
        b.setType(getGenItem().getType());
        return true;
    }

    protected boolean gravityGenInfused(int moved, BlockFace blockFace) {
        Block b = getNextBlock(moved, blockFace);
        if (isBelowVoid(moved + 1)) return false;
        if (b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            if (b.getRelative(BlockFace.DOWN).getType() != XMaterial.COBBLESTONE.parseMaterial()) {
                b.setType(getGenItem().getType());
                return false;
            }
        }
        b.getRelative(BlockFace.DOWN).setType(XMaterial.COBBLESTONE.parseMaterial());
        b.setType(getGenItem().getType());
        return true;
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

        List<XMaterial> materials = epicBuckets.getConfigManager().getIgnoredMaterials();

        if (!materials.contains(XMaterial.requestXMaterial(block.getType().name(), block.getData())))
            return false;

        if ((materials.contains(XMaterial.WATER) || materials.contains(XMaterial.LAVA)) && block.isLiquid())
            return false;

        if (!epicBuckets.canBuild(owner, block.getLocation())) return false;

        if (spongeInRange(block)) return false;

        block.setType(getGenItem().getType());

        return true;
    }

    public ItemStack getGenItem() {
        return subShop.getGenItem();
    }

    protected BlockFace getBlockFace() {
        return blockFace;
    }

    protected boolean isGravityGen() {
        return isGravityGen && getBlockFace() == BlockFace.DOWN;
    }

}
