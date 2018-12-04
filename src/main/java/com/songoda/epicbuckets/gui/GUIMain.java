package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.util.InventoryHelper;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GUIMain extends Gui {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;
    private Player holder;

    public GUIMain(Player player) {
        super(player, EpicBuckets.getInstance().getConfigManager().getInventorySize(), EpicBuckets.getInstance().getConfigManager().getInventoryName());
        epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        holder = player;
    }

    @Override
    public void redraw() {
        if (isFirstDraw()) {
            if (configManager.isFillInventory()) setItems(ItemStackBuilder.of(configManager.getFillItem()).buildItem().build(), InventoryHelper.emptySlots(configManager.getInventorySize() * 9));

            epicBuckets.getShopManager().getShops().stream().filter(Shop::isEnabled).forEach(shop -> {
                setItem(shop.getSlot(), ItemStackBuilder.of(shop.getShopItem()).buildItem().build());
                getSlot(shop.getSlot()).bind(ClickType.LEFT, () -> handleClick(shop));
            });
        }
    }

    private void handleClick(Shop shop) {
        new GUIShop(shop, getPlayer()).open();
    }
}
