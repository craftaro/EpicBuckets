package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.utils.ChatUtil;
import com.songoda.epicbuckets.utils.XMaterial;
import com.songoda.epicbuckets.utils.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class GUIPanel extends AbstractGUI {

    private EpicBuckets epicBuckets;

    private int page = 0;

    public GUIPanel(Player player) {
        super(player);
        this.epicBuckets = EpicBuckets.getInstance();

        init(ChatUtil.colorString(EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.title")), 36);
    }

    @Override
    protected void constructGUI() {

        inventory.clear();
        resetClickables();

        HashMap<UUID, List<Genbucket>> userGens = epicBuckets.getGenbucketManager().getActiveGens();
        List<Genbucket> genbuckets = new ArrayList<>();

        userGens.forEach((uuid, gens) -> genbuckets.addAll(gens));

        int pageStart = 27 * page;

        if (genbuckets.size() < pageStart) {
            if (page > 0) page--;
            constructGUI();
            return;
        }

        // Registers extra buttons like next, deactivate-all, ect.
        registerExtraButtons();

        int slot = 0;
        for (int i = pageStart; i < pageStart + 27; i++, slot++) {
            if (i > genbuckets.size() - 1) break;

            Genbucket bucket = genbuckets.get(i);

            inventory.setItem(slot, createGenbucketItem(bucket));
            registerClickable(slot, (player, inventory, cursor, clickedSlot, type) -> {
                if (type == ClickType.LEFT) {
                    player.teleport(bucket.getPlayerLocation());
                    player.sendMessage(ChatUtil.colorString(epicBuckets.getLocale().getMessage("interface.admin.panel.teleported")
                            .replace("%location%", ChatUtil.getCoordinatesFromLocation(bucket.getClickedLocation()))));
                } else if (type == ClickType.RIGHT) {
                    epicBuckets.getGenbucketManager().unregisterGenbucketForPlayer(bucket.getOwner(), bucket.getGenUUID());
                    player.sendMessage(ChatUtil.colorString(epicBuckets.getLocale().getMessage("interface.admin.panel.deleted")
                            .replace("%location%", ChatUtil.getCoordinatesFromLocation(bucket.getClickedLocation()))));
                }
            });

        }

    }

    @Override
    protected void registerClickables() {
    }

    @Override
    protected void registerOnCloses() {

    }

    private void registerExtraButtons() {

        if (page > 0) {
            inventory.setItem(27, createItem(XMaterial.ARROW.parseItem(), epicBuckets.getLocale().getMessage("interface.admin.panel.previous")));
            registerClickable(27, (player, inventory, cursor, slot, type) -> {
                page--;
                constructGUI();
                player.updateInventory();
            });
        }

        ItemStack deactiveButton = createItem(XMaterial.RED_WOOL.parseItem(), epicBuckets.getLocale().getMessage("interface.admin.panel.deactivateall"));
        ItemStack pageButton = createItem(XMaterial.SIGN.parseItem(), epicBuckets.getLocale().getMessage("interface.admin.panel.page").replace("%page%", String.valueOf(page + 1)));
        ItemStack nextButton = createItem(XMaterial.ARROW.parseItem(), epicBuckets.getLocale().getMessage("interface.admin.panel.next"));

        inventory.setItem(30, deactiveButton);
        inventory.setItem(32, pageButton);
        inventory.setItem(35, nextButton);

        // Deactivate-All Button Clickable
        registerClickable(30, (player, inventory, cursor, slot, type) -> {
            epicBuckets.getGenbucketManager().deactiveAll();
            constructGUI();
            player.updateInventory();
            player.sendMessage(ChatUtil.colorString(epicBuckets.getLocale().getMessage("interface.admin.panel.deactivedall")));
        });

        // Next Button Clickable
        registerClickable(35, (player, inventory, cursor, slot, type) -> {
            page++;
            constructGUI();
            player.updateInventory();
        });

    }

    private ItemStack createItem(ItemStack itemStack, String title) {

        ItemStack item = new ItemStack(itemStack.getType(), 1, itemStack.getDurability());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatUtil.colorString(title));
        item.setItemMeta(meta);

        return item;

    }

    private ItemStack createGenbucketItem(Genbucket genbucket) {

        ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
        SkullMeta meta = (SkullMeta) (skull.getItemMeta() != null ? skull.getItemMeta() : Bukkit.getItemFactory().getItemMeta(skull.getType()));
        meta.setOwner(genbucket.getOwner().getName());
        meta.setDisplayName(ChatUtil.colorString(epicBuckets.getLocale().getMessage("interface.admin.panel.player").replace("%player%", genbucket.getOwner().getName())));
        meta.setLore(ChatUtil.colorList(new ArrayList<>(Arrays.asList(epicBuckets.getLocale().getMessage("interface.admin.panel.type").replace("%genbucket%", genbucket.getGenbucketType().name.toLowerCase()),
                EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.location").replace("%location%", ChatUtil.getCoordinatesFromLocation(genbucket.getClickedLocation())),
                "",
                EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.teleport"),
                EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.delete")))));
        skull.setItemMeta(meta);

        return skull;

    }

}
