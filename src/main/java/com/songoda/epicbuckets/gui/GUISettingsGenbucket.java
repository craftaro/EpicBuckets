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

public class GUISettingsGenbucket implements InventoryProvider {

    private EpicBuckets epicBuckets;

    public GUISettingsGenbucket() {
        epicBuckets = EpicBuckets.getInstance();
    }

    public static final SmartInventory SETTINGS = SmartInventory.builder()
            .id("GUISettingsGenbucket")
            .provider(new GUISettings())
            .type(InventoryType.CHEST)
            .size(6, 9)
            .title(ChatUtil.colorString("&lGenbucket Settings"))
            .manager(EpicBuckets.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        /*
        TODO: Ask for input and execute change
         */
        epicBuckets.getConfigManager().getSettingsGenbucketIntegers()
                .forEach((title, exec) ->
                        contents.add(ClickableItem.of(ItemStackBuilder.of(XMaterial.SUNFLOWER.parseItem())
                                        .name("&6" + title)
                                        .build(),
                                e -> exec.accept(1))
                        )
                );
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
