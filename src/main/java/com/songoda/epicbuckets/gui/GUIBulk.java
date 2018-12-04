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
import org.bukkit.inventory.ItemStack;

public class GUIBulk extends Gui {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;
    private SubShop subShop;

    public GUIBulk(SubShop subShop, Player player) {
        super(player, EpicBuckets.getInstance().getShopManager().getBulkInventorySize(), EpicBuckets.getInstance().getShopManager().getBulkInventoryName().replace("{player}", player.getName()));
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
                setItem(i, ItemStackBuilder.of(shopManager.getDecreaseItem()).name("&c-" + shopManager.getBulkAmounts().get(shopManager.getDecreaseSlots().indexOf(i))).buildItem().build());
                getSlot(i).bind(ClickType.LEFT, () -> handleDecrease(shopManager.getBulkAmounts().get(shopManager.getDecreaseSlots().indexOf(i))));
            });
            shopManager.getIncreaseSlots().forEach(i -> {
                setItem(i, ItemStackBuilder.of(shopManager.getIncreaseItem()).name("&a+" + shopManager.getBulkAmounts().get(shopManager.getIncreaseSlots().indexOf(i))).buildItem().build());
                getSlot(i).bind(ClickType.LEFT, () -> handleIncrease(shopManager.getBulkAmounts().get(shopManager.getIncreaseSlots().indexOf(i))));
            });
            setItem(shopManager.getBulkMainItemSlot(), ItemStackBuilder.of(subShop.getShopItem()).buildItem().build());

            setItem(shopManager.getPurchaseSlot(), ItemStackBuilder.of(shopManager.getPurchaseItem()).buildItem().build());
            getSlot(shopManager.getPurchaseSlot()).bind(ClickType.LEFT, this::handleBuy);
        }
    }

    private void handleBack() {
        new GUIShop(subShop.getParent(), getPlayer()).open();
    }

    public void handleDecrease(int amount) {
        ItemStack genbucket = getSlot(shopManager.getBulkMainItemSlot()).getItem();
        if (genbucket.getAmount() - amount >= 1) genbucket.setAmount(genbucket.getAmount() - amount);
    }

    public void handleIncrease(int amount) {
        ItemStack genbucket = getSlot(shopManager.getBulkMainItemSlot()).getItem();
        if (genbucket.getAmount() + amount <= 64) genbucket.setAmount(genbucket.getAmount() + amount);
    }

    public void handleBuy() {
        if (shopManager.hasEnoughFunds(getPlayer(), subShop, getSlot(shopManager.getBulkMainItemSlot()).getItem().getAmount())) shopManager.buyFromShop(getPlayer(), subShop, getSlot(shopManager.getBulkMainItemSlot()).getItem().getAmount());
        if (shopManager.isCloseAfterPurchase()) new GUIMain(getPlayer()).open();
    }


}
