package com.songoda.epicbuckets.listeners;


import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.event.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.genbucket.types.Horizontal;
import com.songoda.epicbuckets.genbucket.types.Infused;
import com.songoda.epicbuckets.genbucket.types.PsuedoVertical;
import com.songoda.epicbuckets.genbucket.types.Vertical;
import com.songoda.epicbuckets.utils.XMaterial;
import com.songoda.epicbuckets.utils.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GenbucketPlaceListener implements Listener {

    private final EpicBuckets instance;

    public GenbucketPlaceListener(EpicBuckets instance) {
        this.instance = instance;
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

        boolean isInfiniteUse = instance.getConfigManager().isInfiniteUse();
        boolean isInfiniteUseCharge = instance.getConfigManager().isChargeInfiniteUse();

        if (!e.getPlayer().hasPermission("epicbuckets.place")) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.place.nothere"));
            return;
        }
        if (instance.getConfigManager().isOnCooldown(e.getPlayer())) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.place.delay"));
            return;
        }
        if (instance.getConfigManager().isGenbucketsDisabled()) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.genbucket.disabled"));
            return;
        }
        if (!instance.getGenbucketManager().canRegisterNewGenbucket(e.getPlayer())) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.place.wait"));
            return;
        }
        if (!instance.canBuild(e.getPlayer(), e.getClickedBlock().getLocation())) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.place.nothere"));
            return;
        }

        Genbucket genbucket = null;

        switch (GenbucketType.valueOf(nbtItem.getString("Type"))) {
            case PSUEDO:
                genbucket = new PsuedoVertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), instance.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case INFUSED:
                genbucket = new Infused(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), instance.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case VERTICAL:
                genbucket = new Vertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), instance.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case HORIZONTAL:
                genbucket = new Horizontal(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), instance.getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
        }
        instance.getConfigManager().updateCooldown(e.getPlayer());

        if (!genbucket.calculateBlockFace()) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.genbucket.placedwrong").replace("%genbucket%", genbucket.getGenbucketType().formatName() + " Genbucket"));
            return;
        }
        if (genbucket.getGenbucketType() == GenbucketType.PSUEDO && !instance.getConfigManager().getPsuedoMaterials().contains(XMaterial.requestXMaterial(e.getClickedBlock().getType().name(), e.getClickedBlock().getData()))) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.genbucket.wrongmaterialpsuedo"));
            return;
        }

        double infiniteUseCost = instance.getConfigManager().getInfiniteUseCostForGenbucketType(genbucket.getGenbucketType(), genbucket.getGenItem());

        if (isInfiniteUse && isInfiniteUseCharge && instance.getEcon().getBalance(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId())) < infiniteUseCost) {
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.genbucket.infiniteuse.notenough"));
            return;
        }

        /*
        Call event and check if cancelled before proceeding to start the gen and charging the player
         */
        GenbucketPlaceEvent placeEvent = new GenbucketPlaceEvent(e.getPlayer(), genbucket);
        Bukkit.getPluginManager().callEvent(placeEvent);
        if (placeEvent.isCancelled()) return;

        /*
        Subtract bucket from players inventory
         */
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && !isInfiniteUse) {
            if (e.getItem().getAmount() > 1) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            } else {
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), null);
            }
        }

        /*
        Charge for infinite use placement
         */
        if (isInfiniteUse && isInfiniteUseCharge && infiniteUseCost > 0) {
            instance.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()), infiniteUseCost);
            e.getPlayer().sendMessage(instance.getLocale().getMessage("event.genbucket.infiniteuse.charge").replace("%charge%", infiniteUseCost + ""));
        }

        instance.getGenbucketManager().registerGenbucketForPlayer(e.getPlayer(), genbucket);
        instance.getGenbucketManager().notifyAdmins(e.getPlayer(), genbucket);
        genbucket.generate();
    }

}
