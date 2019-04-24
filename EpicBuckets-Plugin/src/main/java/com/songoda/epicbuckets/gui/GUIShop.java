package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.utils.gui.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GUIShop extends AbstractGUI {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;

    private Shop shop;

    private int size;

    public GUIShop(Player player, Shop shop) {
        super(player);
        this.epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        this.size = shop.getInventorySize() * 9;
        this.shop = shop;

        init(shop.getInventoryName(), size);
    }

    @Override
    public void constructGUI() {
        if (shop.isFillInventory()) {
            int num = 0;
            while (num != size) {
                ItemStack glass = configManager.getFillItem();
                inventory.setItem(num, glass);
                num++;
            }
        }
        if (shopManager.isUseBackButtons()) {
            inventory.setItem(shop.getBackButtonSlot(), configManager.getBackButton());
        }

        shop.getSubShops().stream().filter(SubShop::isEnabled).forEach(subShop ->
                inventory.setItem(subShop.getSlot(), subShop.getShopItem()));

    }

    @Override
    protected void registerClickables() {
        if (shopManager.isUseBackButtons()) {
            registerClickable(shop.getBackButtonSlot(), ((player, inventory, cursor, slot, type) -> new GUIMain(player)));
        }

        shop.getSubShops().stream().filter(SubShop::isEnabled).forEach(subShop -> {
            registerClickable(subShop.getSlot(), ClickType.LEFT, ((player1, inventory1, cursor, slot, type) -> {
                if (shopManager.hasEnoughFunds(player1, subShop, 1) && !shopManager.inventoryFull(player))
                    shopManager.buyFromShop(player, subShop, 1);
                if (shopManager.isCloseAfterPurchase()) new GUIMain(player);
            }));
            registerClickable(subShop.getSlot(), ClickType.RIGHT, ((player1, inventory1, cursor, slot, type) -> new GUIBulk(player, shop, subShop)));
        });

    }

    @Override
    protected void registerOnCloses() {

    }
}
