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
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
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

        if (!e.getPlayer().hasPermission("genbucket.place")) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.place.nothere"));
            return;
        }
        if (epicBuckets.getConfigManager().isGenbucketsDisabled()) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.genbucket.disabled"));
            return;
        }
        if (!epicBuckets.getGenbucketManager().canRegisterNewGenbucket(e.getPlayer())) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.place.wait"));
            return;
        }
        if (!epicBuckets.getGenbucketManager().canPlaceGenbucket(e.getPlayer(), e.getClickedBlock().getLocation())) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.place.nothere"));
            return;
        }

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

        if (!genbucket.isValidBlockFace()) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.genbucket.placedwrong").replace("%genbucket%", StringUtils.capitalize(genbucket.getGenbucketType().name.toLowerCase()) + " genbucket"));
            return;
        }
        if (genbucket.getGenbucketType() == GenbucketType.PSUEDO && !epicBuckets.getConfigManager().getPsuedoMaterials().contains(XMaterial.requestXMaterial(e.getClickedBlock().getType().name(), e.getClickedBlock().getData()))) {
            e.getPlayer().sendMessage(epicBuckets.getLocale().getMessage("event.genbucket.wrongmaterialpsuedo"));
            return;
        }

        if (e.getPlayer().getGameMode() != GameMode.CREATIVE || !epicBuckets.getConfigManager().isUnlimitedGenbuckets()) {
            if (e.getItem().getAmount() > 1) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            } else {
                e.getItem().setAmount(0);
            }
        }

        epicBuckets.getGenbucketManager().registerGenbucketForPlayer(e.getPlayer(), genbucket);
        epicBuckets.getGenbucketManager().notifyAdmins(e.getPlayer(), genbucket);
        genbucket.generate();
    }

}