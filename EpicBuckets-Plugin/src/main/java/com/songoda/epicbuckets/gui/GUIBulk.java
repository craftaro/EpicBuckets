package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.util.gui.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIBulk extends AbstractGUI {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;

    private int size;

    private Shop shop;
    private SubShop subShop;

    public GUIBulk(Player player, Shop shop, SubShop subShop) {
        super(player);
        this.epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        this.size = shopManager.getBulkInventorySize() * 9;
        this.shop = shop;
        this.subShop = subShop;

        init(EpicBuckets.getInstance().getShopManager().getBulkInventoryName().replace("{player}", player.getName()), size);
    }

    @Override
    protected void constructGUI() {
        if (shopManager.isBulkFillInventory()) {
            int num = 0;
            while (num != size) {
                ItemStack glass = configManager.getFillItem();
                inventory.setItem(num, glass);
                num++;
            }
        }

        if (shopManager.isUseBackButtons()) {
            inventory.setItem(shopManager.getBulkBackButtonSlot(), configManager.getBackButton());
        }

        shopManager.getDecreaseSlots().forEach(i ->
                createButton(i, shopManager.getDecreaseItem(), "&c-" + shopManager.getBulkAmounts().get(shopManager.getDecreaseSlots().indexOf(i))));

        shopManager.getIncreaseSlots().forEach(i ->
                createButton(i, shopManager.getIncreaseItem(), "&a+" + shopManager.getBulkAmounts().get(shopManager.getIncreaseSlots().indexOf(i))));

        inventory.setItem(shopManager.getBulkMainItemSlot(), subShop.getShopItem());

        inventory.setItem(shopManager.getPurchaseSlot(), shopManager.getPurchaseItem());
    }

    @Override
    protected void registerClickables() {
        epicBuckets.getShopManager().getShops().stream().filter(Shop::isEnabled).forEach(shop ->
                registerClickable(shop.getSlot(), ((player1, inventory1, cursor, slot, type) -> new GUIShop(player, shop))));

        if (shopManager.isUseBackButtons()) {
            registerClickable(shopManager.getBulkBackButtonSlot(), ((player, inventory, cursor, slot, type) ->
                    new GUIShop(player, shop)));
        }

        shopManager.getDecreaseSlots().forEach(i ->
                registerClickable(i, ((player1, inventory1, cursor, slot, type) -> {
            ItemStack genbucket = inventory.getItem(shopManager.getBulkMainItemSlot());
            int amount = shopManager.getBulkAmounts().get(shopManager.getDecreaseSlots().indexOf(i));
            genbucket.setAmount(genbucket.getAmount() - amount < 1 ? 1 : genbucket.getAmount() - amount);
                })));

        shopManager.getIncreaseSlots().forEach(i ->
                registerClickable(i, ((player1, inventory1, cursor, slot, type) -> {
            ItemStack genbucket = inventory.getItem(shopManager.getBulkMainItemSlot());
            int amount = shopManager.getBulkAmounts().get(shopManager.getIncreaseSlots().indexOf(i));
            genbucket.setAmount(genbucket.getAmount() + amount > 64 ? 64 : genbucket.getAmount() + amount);
                })));

        registerClickable(shopManager.getPurchaseSlot(), ((player1, inventory1, cursor, slot, type) -> {
            if (shopManager.hasEnoughFunds(player1, subShop, inventory.getItem(shopManager.getBulkMainItemSlot()).getAmount())
                    && !shopManager.inventoryFull(player))
                shopManager.buyFromShop(player, subShop, inventory.getItem(shopManager.getBulkMainItemSlot()).getAmount());

            if (shopManager.isCloseAfterPurchase()) new GUIMain(player);
        }));
    }

    @Override
    protected void registerOnCloses() {

    }
}
