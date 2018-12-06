package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.Genbucket;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class GUIPanel implements InventoryProvider {

    private EpicBuckets epicBuckets = EpicBuckets.getInstance();

    public static final SmartInventory PANEL = SmartInventory.builder()
            .id("GUIPanel")
            .provider(new GUIPanel())
            .size(6, 9)
            .title("Active Genbuckets")
            .manager(EpicBuckets.getInstance().getInventoryManager())
            .build();


    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        HashMap<UUID, List<Genbucket>> gens = epicBuckets.getGenbucketManager().getActiveGens();
        List<ClickableItem> itemList = new ArrayList<>();

        Pagination pagination = inventoryContents.pagination();

        for (UUID uuid : gens.keySet()) {
            if (gens.get(uuid).size() < 1) continue;
            for (Genbucket genbucket : gens.get(uuid)) {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner(genbucket.getOwner().getName());
                meta.setDisplayName(ChatUtil.colorString("&a" + genbucket.getOwner().getName()));
                meta.setLore(ChatUtil.colorList(new ArrayList<>(Arrays.asList("&7Type: &f" + genbucket.getGenbucketType().name.toLowerCase(), "&7Location: &f" + ChatUtil.getCoordinatesFromLocation(genbucket.getClickedLocation()), "", "&f&oClick to teleport"))));
                skull.setItemMeta(meta);
                itemList.add(ClickableItem.of(skull, e -> player.teleport(genbucket.getPlayerLocation())));
            }
        }

        pagination.setItems(itemList.toArray(new ClickableItem[1]));
        pagination.setItemsPerPage(7);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        inventoryContents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> PANEL.open(player, pagination.previous().getPage())));
        inventoryContents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> PANEL.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }
}
