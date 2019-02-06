package com.songoda.epicbuckets.event;

import com.songoda.epicbuckets.genbucket.Genbucket;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GenbucketPlaceEvent extends Event implements Cancellable {

    private final Player player;
    private final Genbucket genbucket;
    private boolean isCancelled;

    public GenbucketPlaceEvent(Player player, Genbucket genbucket) {
        this.player = player;
        this.genbucket = genbucket;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Genbucket getGenbucket() {
        return this.genbucket;
    }

    /*
     Needed for the event
    */
    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
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
