package com.songoda.epicbuckets.genbuckets;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * GenbucketItem created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 12:00
 */
public class GenbucketItem {

    private EpicBuckets main = EpicBuckets.getInstance();

    private String itemName;
    private List<String> itemLore;
    private List<String> itemSecondLore;

    private Material icon;
    private int typeDamage;
    private Material type;

    private GenbucketType genbucketType;

    private int amount;
    private int damage;
    private int price;


    public GenbucketItem(String shopName, String key) {

        FileConfiguration config = EpicBuckets.getInstance().shopFile;

        this.itemName = ChatUtil.colorString(config.getString("shops." + shopName + "." + key + ".name"));

        this.price = config.getInt("shops." + shopName + "." + key + ".price");

        this.icon = Material.valueOf(config.getString("shops." + shopName + "." + key + ".icon").toUpperCase());

        this.typeDamage = config.getInt("shops." + shopName + "." + key + ".type-damage");
        this.type = Material.valueOf(config.getString("shops." + shopName + "." + key + ".type").toUpperCase());

        this.itemLore = ChatUtil.colorList(config.getStringList("shops." + shopName + "." + key + ".item-lore"));

        this.itemSecondLore = ChatUtil.colorList(config.getStringList("shops." + shopName + "." + key + ".description"), type, price);

        this.damage = config.getInt("shops." + shopName + "." + key + ".damage");

        this.genbucketType = GenbucketType.valueOf(config.getString("shops." + shopName + ".trait").toUpperCase());

        amount = 1;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItemLore() {
        return itemLore;
    }

    public void setItemLore(List<String> itemLore) {
        this.itemLore = itemLore;
    }

    public GenbucketType getGenbucketType() {
        return genbucketType;
    }

    public List<String> getItemSecondLore() {
        return itemSecondLore;
    }

    public Material getType() {
        return type;
    }

    public Material getIcon() {
        return icon;
    }

    public int getDamage() {
        return damage;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getVerticalHeight() {
        return main.getConfig().getInt("MAX-VERTICAL-HEIGHT");
    }

    public byte getTypeDamage() {
        return (byte) typeDamage;
    }

    public int getHorizontalLength() {
        return main.getConfig().getInt("MAX-HORIZONTAL-LENGTH");
    }

    public ItemStack getGenbucketItem() {
        return ItemStackUtil.createItemStack(getItemName(), getItemLore(), getIcon(), getAmount(), getDamage(), true);
    }
}
