package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.utils.ChatUtil;
import com.songoda.epicbuckets.utils.XMaterial;
import com.songoda.epicbuckets.utils.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class GUIPanel extends AbstractGUI {

    private EpicBuckets epicBuckets;

    private int size = 54;

    public GUIPanel(Player player) {
        super(player);
        this.epicBuckets = EpicBuckets.getInstance();

        init(ChatUtil.colorString(EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.title")), size);
    }

    @Override
    protected void constructGUI() {
        HashMap<UUID, List<Genbucket>> gens = epicBuckets.getGenbucketManager().getActiveGens();

        int place = 0;
        for (UUID uuid : gens.keySet()) {
            if (gens.get(uuid).size() < 1 && place != size) continue;
            for (Genbucket genbucket : gens.get(uuid)) {
                ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
                SkullMeta meta = (SkullMeta) (skull.getItemMeta() != null ? skull.getItemMeta() : Bukkit.getItemFactory().getItemMeta(skull.getType()));
                meta.setOwner(genbucket.getOwner().getName());
                meta.setDisplayName(ChatUtil.colorString(epicBuckets.getLocale().getMessage("interface.admin.panel.player").replace("%player%", genbucket.getOwner().getName())));
                meta.setLore(ChatUtil.colorList(new ArrayList<>(Arrays.asList(epicBuckets.getLocale().getMessage("interface.admin.panel.type").replace("%genbucket%", genbucket.getGenbucketType().name.toLowerCase()), EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.location").replace("%location%", ChatUtil.getCoordinatesFromLocation(genbucket.getClickedLocation())), "", EpicBuckets.getInstance().getLocale().getMessage("interface.admin.panel.teleport")))));
                skull.setItemMeta(meta);
                inventory.setItem(place, skull);
                registerClickable(place, ((player1, inventory1, cursor, slot, type) ->
                        player.teleport(genbucket.getPlayerLocation()
                        )));
                place++;
            }
        }
    }

    @Override
    protected void registerClickables() {
    }

    @Override
    protected void registerOnCloses() {

    }
}
