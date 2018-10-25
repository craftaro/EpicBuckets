package com.songoda.epicbuckets.listeners;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.events.GenbucketPlaceEvent;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.genbuckets.types.Horizontal;
import com.songoda.epicbuckets.genbuckets.types.Infused;
import com.songoda.epicbuckets.genbuckets.types.PsuedoVertical;
import com.songoda.epicbuckets.genbuckets.types.Vertical;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.ItemStackUtil;
import com.songoda.epicbuckets.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * GenbucketPlaceListener created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 16:06
 */
public class GenbucketPlaceListener implements Listener {

    private EpicBuckets plugin = EpicBuckets.getInstance();

    // We have to do a double for loop to find the genbucket
    // don't think this is going to be a big issue but eehhh

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {

        FileConfiguration config = plugin.shopFile.config;

        final Player player = event.getPlayer();

        GenbucketItem genbucketItem;

        ItemStack itemStack = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

        if (!itemStack.hasItemMeta())
            return;

        for (String path : config.getConfigurationSection("shops").getKeys(false)) {

            for (String key : config.getConfigurationSection("shops." + path).getKeys(false)) {

                if (!config.contains("shops." + path + "." + key + ".icon"))
                    continue;

                genbucketItem = new GenbucketItem(path, key);

                Material itemMaterial = itemStack.getType();
                String itemName = ChatUtil.stripColor(itemStack.getItemMeta().getDisplayName());
                List<String> itemLore = itemStack.getItemMeta().getLore();

                if (!genbucketItem.getIcon().equals(itemMaterial))
                    continue;

                if (!ChatUtil.stripColor(genbucketItem.getItemName()).equalsIgnoreCase(itemName))
                    continue;

                if (!itemLore.equals(genbucketItem.getItemLore()))
                    continue;

                event.setCancelled(true);

                player.updateInventory();
                event.getBlockClicked().getRelative(event.getBlockFace()).getState().update();
                event.getBlockClicked().getState().update();

                break;
            }
        }

    }

    @EventHandler
    public void onRightClickBlock(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getItem() == null)
            return;

        if (!event.getItem().hasItemMeta())
            return;

        final Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();

        FileConfiguration config = plugin.shopFile.config;

        boolean foundGenbucket = false;

        GenbucketItem genbucketItem = null;

        for (String path : config.getConfigurationSection("shops").getKeys(false)) {

            if (foundGenbucket)
                break;

            for (String key : config.getConfigurationSection("shops." + path).getKeys(false)) {

                if (!config.contains("shops." + path + "." + key + ".icon"))
                    continue;

                genbucketItem = new GenbucketItem(path, key);

                Material itemMaterial;
                String itemName;
                List<String> itemLore;

                try {
                    itemMaterial = event.getItem().getType();
                    itemName = ChatUtil.stripColor(event.getItem().getItemMeta().getDisplayName());
                    itemLore = event.getItem().getItemMeta().getLore();
                } catch (NullPointerException e) {
                    continue;
                }

                if (!genbucketItem.getIcon().equals(itemMaterial))
                    continue;

                if (!ChatUtil.stripColor(genbucketItem.getItemName()).equalsIgnoreCase(itemName))
                    continue;

                if (!itemLore.equals(genbucketItem.getItemLore()))
                    continue;

                foundGenbucket = true;

                event.setCancelled(true);

                break;
            }
        }

        if (!foundGenbucket)
            return;

        if (!player.hasPermission("genbucket.place")) {
            player.sendMessage(ChatUtil.colorPrefix(EpicBuckets.getInstance().messageFile.config.getString("NO-PERMISSION")));
            return;
        }

        if (Util.disableGenbuckets()) {
            player.sendMessage(ChatUtil.colorPrefix(EpicBuckets.getInstance().messageFile.config.getString("GENBUCKET-DISABLED")));
            player.updateInventory();
            return;
        }

        switch (genbucketItem.getGenbucketType()) {

            case PSUEDO:
                new PsuedoVertical(player, clickedBlock.getLocation(), event.getBlockFace(), genbucketItem);
                break;

            case INFUSED:
                new Infused(player, clickedBlock.getLocation(), event.getBlockFace(), genbucketItem);
                break;

            case VERTICAL:
                new Vertical(player, clickedBlock.getLocation(), event.getBlockFace(), genbucketItem);
                break;

            case HORIZONTAL:
                new Horizontal(player, clickedBlock.getLocation(), event.getBlockFace(), genbucketItem);
                break;


        }

        player.updateInventory();

    }

    @EventHandler
    public void onGenbucketPlace(GenbucketPlaceEvent event) {

        ItemStack item = ItemStackUtil.createItemStack("§7" + event.getGenbucketUUID().toString(), Arrays.asList("", "§7Information:", " §eLeft click: §6Teleport to location", " §eRight click: §6Stop genbucket", " §ePlaced by §6SoFocused", " §eCordinates: §6" + ChatUtil.getCoordinatesFromLocation(event.getLocation()), " §eMaterial: §6" + event.getGenbucketItem().getType(), " §eDamage: §6" + event.getGenbucketItem().getTypeDamage()), Material.STAINED_GLASS_PANE, 1, 13, true);

        GenbucketManager.activeGenbucketItems.put(event.getGenbucketUUID(), item);
        GenbucketManager.activeGenbucketLocation.put(event.getGenbucketUUID(), event.getLocation());

        if (!plugin.getConfig().getBoolean("PLACE-UNLIMTED-GENS"))
            GenbucketManager.setActiveGenBuckets(event.getPlayer(), GenbucketManager.getActiveGenBuckets(event.getPlayer()) + 1);

    }

}
