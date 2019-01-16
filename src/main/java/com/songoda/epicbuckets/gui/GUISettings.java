package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class GUISettings implements InventoryProvider {

    public static final SmartInventory SETTINGS = SmartInventory.builder()
            .id("GUISettings")
            .provider(new GUISettings())
            .type(InventoryType.HOPPER)
            .title(ChatUtil.colorString("&lSettings"))
            .manager(EpicBuckets.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.add(ClickableItem.of(ItemStackBuilder.of(XMaterial.ANVIL.parseMaterial())
                        .name("&3Genbucket Settings")
                        .build(),
                e -> GUISettingsGenbucket.SETTINGS.open(player)));
        contents.add(ClickableItem.of(ItemStackBuilder.of(XMaterial.CRAFTING_TABLE.parseMaterial())
                        .name("&6Hooks")
                        .build(),
                e -> GUISettingsGenbucket.SETTINGS.open(player)));
        contents.add(ClickableItem.of(ItemStackBuilder.of(XMaterial.CHEST.parseMaterial())
                        .name("&6Shop settings")
                        .build(),
                e -> GUISettingsGenbucket.SETTINGS.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
