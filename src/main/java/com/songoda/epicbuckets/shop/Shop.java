package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.inventory.ItemStack;

public class Shop {

    private EpicBuckets epicBuckets;

    private String shopName;
    private String path;
    private ItemStack shopItem;
    private int slot;

    private boolean enabled = true;

    public Shop(String name, String path) {
        this.epicBuckets = EpicBuckets.getInstance();

        this.shopName = name;
        this.path = path;

        setupShopItem();
    }

    private void setupShopItem() {
        String itemPath = path + ".item";

        boolean i = Validator.getInstance().isInt(epicBuckets.getConfig().getString(itemPath + ".slot"));
        boolean m = Validator.getInstance().isMaterial(epicBuckets.getConfig().getString(itemPath + ".material"));

        if (!i) {
            epicBuckets.getDebugger().sendConsole("&cShop " + shopName + " has an invalid slot set, disabling shop..");
            enabled = false;
        } else {
            slot = epicBuckets.getConfig().getInt(path + ".slot");
        }

        shopItem = ((!m) ? XMaterial.WATER_BUCKET.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(itemPath + ".material")).parseItem());

        shopItem.getItemMeta().setDisplayName(epicBuckets.getConfig().getString(itemPath + ".name"));
        shopItem.getItemMeta().setLore(epicBuckets.getConfig().getStringList(itemPath + ".lore"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getSlot() {
        return slot;
    }

}
