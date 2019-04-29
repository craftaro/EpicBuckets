package com.songoda.epicbuckets.gui;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.file.ConfigManager;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.ShopManager;
import com.songoda.epicbuckets.utils.gui.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIMain extends AbstractGUI {

    private EpicBuckets epicBuckets;
    private ConfigManager configManager;
    private ShopManager shopManager;

    private int size;

    public GUIMain(Player player) {
        super(player);
        this.epicBuckets = EpicBuckets.getInstance();
        configManager = epicBuckets.getConfigManager();
        shopManager = epicBuckets.getShopManager();
        this.size = configManager.getInventorySize() * 9;

        init(EpicBuckets.getInstance().getConfigManager().getInventoryName(), size);
    }

    @Override
    public void constructGUI() {
        if (configManager.isFillInventory()) {
            int num = 0;
            while (num != size) {
                ItemStack glass = configManager.getFillItem();
                inventory.setItem(num, glass);
                num++;
            }
        }

        epicBuckets.getShopManager().getShops().stream().filter(Shop::isEnabled).forEach(shop ->
                inventory.setItem(shop.getSlot(), shop.getShopItem()));

    }

    @Override
    protected void registerClickables() {
        epicBuckets.getShopManager().getShops().stream().filter(Shop::isEnabled).forEach(shop ->
                registerClickable(shop.getSlot(), ((player1, inventory1, cursor, slot, type) ->
                        new GUIShop(player, shop))));

    }

    @Override
    protected void registerOnCloses() {

    }
}
