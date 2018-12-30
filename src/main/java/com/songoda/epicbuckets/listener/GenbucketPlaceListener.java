package com.songoda.epicbuckets.listener;


import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.genbucket.types.Horizontal;
import com.songoda.epicbuckets.genbucket.types.Infused;
import com.songoda.epicbuckets.genbucket.types.PsuedoVertical;
import com.songoda.epicbuckets.genbucket.types.Vertical;
import com.songoda.epicbuckets.util.XMaterial;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GenbucketPlaceListener implements Listener {

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

        if (!e.getPlayer().hasPermission("epicbuckets.place")) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.place.nothere"));
            return;
        }
        if (EpicBuckets.getInstance().getConfigManager().isOnCooldown(e.getPlayer())) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.place.delay"));
            return;
        }
        if (EpicBuckets.getInstance().getConfigManager().isGenbucketsDisabled()) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.genbucket.disabled"));
            return;
        }
        if (!EpicBuckets.getInstance().getGenbucketManager().canRegisterNewGenbucket(e.getPlayer())) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.place.wait"));
            return;
        }
        if (!EpicBuckets.getInstance().getGenbucketManager().canPlaceGenbucket(e.getPlayer(), e.getClickedBlock().getLocation())) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.place.nothere"));
            return;
        }

        Genbucket genbucket = null;

        switch(GenbucketType.valueOf(nbtItem.getString("Type"))) {
            case PSUEDO:
                genbucket = new PsuedoVertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), EpicBuckets.getInstance().getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case INFUSED:
                genbucket = new Infused(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), EpicBuckets.getInstance().getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case VERTICAL:
                genbucket = new Vertical(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), EpicBuckets.getInstance().getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
                break;
            case HORIZONTAL:
                genbucket = new Horizontal(e.getPlayer(), e.getClickedBlock(), e.getBlockFace(), EpicBuckets.getInstance().getShopManager().getShop(nbtItem.getString("Shop")).getSubShop(nbtItem.getString("SubShop")));
        }

        if (!genbucket.calculateBlockFace()) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.genbucket.placedwrong").replace("%genbucket%", genbucket.getGenbucketType().name.toUpperCase() + " genbucket"));
            return;
        }
        if (genbucket.getGenbucketType() == GenbucketType.PSUEDO && !EpicBuckets.getInstance().getConfigManager().getPsuedoMaterials().contains(XMaterial.requestXMaterial(e.getClickedBlock().getType().name(), e.getClickedBlock().getData()))) {
            e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.genbucket.wrongmaterialpsuedo"));
            return;
        }

        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && !EpicBuckets.getInstance().getConfigManager().isInfiniteUse()) {
            if (e.getItem().getAmount() > 1) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            } else {
                e.getPlayer().getInventory().remove(e.getItem());
            }
        }

        if (EpicBuckets.getInstance().getConfigManager().isInfiniteUse() && EpicBuckets.getInstance().getConfigManager().isChargeInfiniteUse()) {
            if (EpicBuckets.getInstance().getEcon().getBalance(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId())) < EpicBuckets.getInstance().getConfigManager().getInfiniteUseCostForGenbucketType(genbucket.getGenbucketType(), genbucket.getGenItem())) {
                e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.genbucket.infiniteuse.notenough"));
                return;
            }
            if (EpicBuckets.getInstance().getConfigManager().getInfiniteUseCostForGenbucketType(genbucket.getGenbucketType(), genbucket.getGenItem()) > 0) {
                EpicBuckets.getInstance().getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()), EpicBuckets.getInstance().getConfigManager().getInfiniteUseCostForGenbucketType(genbucket.getGenbucketType(), genbucket.getGenItem()));
                e.getPlayer().sendMessage(EpicBuckets.getInstance().getLocale().getMessage("event.genbucket.infiniteuse.charge").replace("%charge%", EpicBuckets.getInstance().getConfigManager().getInfiniteUseCostForGenbucketType(genbucket.getGenbucketType(), genbucket.getGenItem()) + ""));
            }
        }

        EpicBuckets.getInstance().getGenbucketManager().registerGenbucketForPlayer(e.getPlayer(), genbucket);
        EpicBuckets.getInstance().getGenbucketManager().notifyAdmins(e.getPlayer(), genbucket);
        genbucket.generate();
    }

}
