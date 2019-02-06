package com.songoda.epicbuckets.event;

import com.songoda.epicbuckets.genbucket.Genbucket;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GenbucketPlaceEvent extends Event implements Cancellable {

    /*
     Needed for the event
    */
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Genbucket genbucket;
    private boolean isCancelled;

    public GenbucketPlaceEvent(Player player, Genbucket genbucket) {
        this.player = player;
        this.genbucket = genbucket;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Genbucket getGenbucket() {
        return this.genbucket;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
