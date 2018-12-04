package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.util.InventoryHelper;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GUIBulk extends Gui {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;
    private SubShop subShop;

    public GUIBulk(SubShop subShop, Player player) {
        super(player, EpicBuckets.getInstance().getShopManager().getBulkInventorySize(), EpicBuckets.getInstance().getShopManager().getBulkInventoryName());
        epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        this.subShop = subShop;
    }

    @Override
    public void redraw() {
        if (isFirstDraw()) {
            if (shopManager.isBulkFillInventory()) setItems(ItemStackBuilder.of(configManager.getFillItem()).buildItem().build(), InventoryHelper.emptySlots(configManager.getInventorySize() * 9));
            if (shopManager.isUseBackButtons()) {
                setItem(shopManager.getBulkBackButtonSlot(), ItemStackBuilder.of(configManager.getBackButton()).buildItem().build());
                getSlot(shopManager.getBulkBackButtonSlot()).bind(ClickType.LEFT, this::handleBack);
            }

            shopManager.getDecreaseSlots().forEach(i -> {
                setItem(i, ItemStackBuilder.of(shopManager.getDecreaseItem()).buildItem().build());
                getSlot(i).bind(ClickType.LEFT, this::handleDecrease);
            });
            shopManager.getIncreaseSlots().forEach(i -> {
                setItem(i, ItemStackBuilder.of(shopManager.getIncreaseItem()).buildItem().build());
                getSlot(i).bind(ClickType.LEFT, this::handleIncrease);
            });
            setItem(1, ItemStackBuilder.of(subShop.getGenItem()).buildItem().build());
            getSlot(1).bind(ClickType.LEFT, () -> handleBuy());

            setItem(shopManager.getPurchaseSlot(), ItemStackBuilder.of(shopManager.getPurchaseItem()).buildItem().build());
            getSlot(shopManager.getPurchaseSlot()).bind(ClickType.LEFT, this::handleBuy);
        }
    }

    private void handleBack() {
        new GUIShop(subShop.getParent(), getPlayer()).open();
    }

    public void handleDecrease() {

    }

    public void handleIncrease() {

    }

    public void handleBuy() {

    }


}
