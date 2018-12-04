package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.util.InventoryHelper;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GUIShop extends Gui {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;
    private Shop s;

    public GUIShop(Shop s, Player player) {
        super(player, s.getInventorySize(), s.getInventoryName());
        epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        this.s = s;
    }

    @Override
    public void redraw() {
        if (isFirstDraw()) {
            if (s.isFillInventory()) setItems(ItemStackBuilder.of(configManager.getFillItem()).buildItem().build(), InventoryHelper.emptySlots(s.getInventorySize() * 9));
            if (shopManager.isUseBackButtons()) {
                setItem(s.getBackButtonSlot(), ItemStackBuilder.of(configManager.getBackButton()).buildItem().build());
                getSlot(s.getBackButtonSlot()).bind(ClickType.LEFT, this::handleBack);
            }

            s.getSubShops().stream().filter(SubShop::isEnabled).forEach(subShop -> {
                setItem(subShop.getSlot(), ItemStackBuilder.of(subShop.getShopItem()).buildItem().build());
                getSlot(subShop.getSlot()).bind(ClickType.LEFT, () -> handleSubShop(subShop));
                getSlot(subShop.getSlot()).bind(ClickType.RIGHT, () -> handleBulk(subShop));
            });
        }
    }

    private void handleBulk(SubShop s) {
        new GUIBulk(s, getPlayer()).open();
    }

    private void handleBack() {
        new GUIMain(getPlayer()).open();
    }

    private void handleSubShop(SubShop s) {
        if (shopManager.hasEnoughFunds(getPlayer(), s)) shopManager.buyFromShop(getPlayer(), s);
    }

}
