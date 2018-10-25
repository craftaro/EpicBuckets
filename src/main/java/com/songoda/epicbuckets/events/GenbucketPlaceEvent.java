package com.songoda.epicbuckets.events;

import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * GenbucketPlaceEvent created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 22:15
 */
public class GenbucketPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;

    private Player player;
    private Location location;
    private BlockFace blockFace;
    private GenbucketItem genbucketItem;
    private UUID genbucketUUID;

    public GenbucketPlaceEvent(Player player, Location location, BlockFace blockFace, GenbucketItem genbucketItem, UUID genbucketUUID) {
        this.player = player;
        this.location = location;
        this.blockFace = blockFace;
        this.genbucketItem = genbucketItem;
        this.genbucketUUID = genbucketUUID;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public GenbucketItem getGenbucketItem() {
        return genbucketItem;
    }

    public UUID getGenbucketUUID() {
        return genbucketUUID;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}

