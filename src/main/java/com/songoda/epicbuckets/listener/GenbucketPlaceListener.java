package com.songoda.epicbuckets.listener;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketItem;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.genbucket.types.Horizontal;
import com.songoda.epicbuckets.genbucket.types.Infused;
import com.songoda.epicbuckets.genbucket.types.PsuedoVertical;
import com.songoda.epicbuckets.genbucket.types.Vertical;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GenbucketPlaceListener implements Listener {

    private EpicBuckets epicBuckets;

    public GenbucketPlaceListener() {
        epicBuckets = EpicBuckets.getInstance();
    }

    @EventHandler
    public void emptyBucket(PlayerBucketEmptyEvent e) {
        NBTItem nbtItem = new NBTItem(e.getItemStack());
        if (nbtItem.hasKey("Genbucket")) {
            e.setCancelled(true);
            e.getBlockClicked().getRelative(e.getBlockFace()).getState().update();
            e.getBlockClicked().getState().update();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getItem() == null) return;

        NBTItem nbtItem = new NBTItem(e.getItem());
        if (!nbtItem.hasKey("Genbucket")) return;

        e.setCancelled(true);

        if (!e.getPlayer().hasPermission("genbucket.place")) return;
        if (epicBuckets.getConfigManager().isGenbucketsDisabled()) return;
        if (!epicBuckets.getGenbucketManager().canRegisterNewGenbucket(e.getPlayer())) return;

        Genbucket genbucket = null;

        switch(GenbucketType.valueOf(nbtItem.getString("Type"))) {
            case PSUEDO:
                genbucket = new PsuedoVertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), epicBuckets.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case INFUSED:
                genbucket = new Infused(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), epicBuckets.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case VERTICAL:
                genbucket = new Vertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), epicBuckets.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case HORIZONTAL:
                genbucket = new Horizontal(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), epicBuckets.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
        }

        if (!genbucket.isValidBlockFace()) return;

        //GenbucketItem genbucketItem = new GenbucketItem(e.getPlayer(), genbucket, e.getClickedBlock().getLocation(), e.getBlockFace());
        epicBuckets.getGenbucketManager().registerGenbucketForPlayer(e.getPlayer(), genbucket);
        genbucket.generate();
    }

}
