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
import me.lucko.helper.item.ItemStackBuilder;
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

    private ClickableItem[] createItems(HashMap<UUID, List<Genbucket>> gens, Player player) {
        List<ClickableItem> itemList = new ArrayList<>();

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

        return itemList.toArray(new ClickableItem[1]);
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Pagination pagination = inventoryContents.pagination();

        pagination.setItems(createItems(epicBuckets.getGenbucketManager().getActiveGens(), player));
        pagination.setItemsPerPage(36);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        inventoryContents.set(5, 0, ClickableItem.of(ItemStackBuilder.of(XMaterial.ARROW.parseMaterial()).name("&fPrevious").build(),
                e -> PANEL.open(player, pagination.previous().getPage())));
        inventoryContents.set(5, 4, ClickableItem.of(ItemStackBuilder.of(XMaterial.REDSTONE_BLOCK.parseMaterial()).name("&cExit").build(),
                e -> PANEL.close(player)));
        inventoryContents.set(5, 8, ClickableItem.of(ItemStackBuilder.of(XMaterial.ARROW.parseMaterial()).name("&fNext").build(),
                e -> PANEL.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        int state = inventoryContents.property("state", 0);
        inventoryContents.setProperty("state", state + 1);

        if(state % 60 != 0)
            return;

        Pagination pagination = inventoryContents.pagination();

        pagination.setItems(createItems(epicBuckets.getGenbucketManager().getActiveGens(), player));
        pagination.setItemsPerPage(36);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }
}
