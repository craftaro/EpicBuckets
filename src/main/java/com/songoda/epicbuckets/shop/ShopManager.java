package com.songoda.epicbuckets.shop;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.util.InventoryHelper;
import com.songoda.epicbuckets.util.NBTHelper;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.*;

public class ShopManager {

    private HashMap<String, Shop> shopDatabase;
    private FileConfiguration shops;
    private EpicBuckets epicBuckets;

    private String shopPath = "shops";
    private String bulkShopPath = "BULK-SHOP-INVENTORY";

    private ItemStack increaseItem;
    private ItemStack decreaseItem;
    private ItemStack purchaseItem;

    private List<Integer> increaseSlots;
    private List<Integer> decreaseSlots;
    private List<Integer> bulkAmounts = new ArrayList<>(Arrays.asList(1, 5, 10));
    private int purchaseSlot;

    private String bulkInventoryName;
    private int bulkInventorySize;
    private boolean bulkFillInventory;
    private int bulkBackButtonSlot;
    private int bulkMainItemSlot;

    private boolean useBackButtons;
    private boolean closeAfterPurchase;

    public ShopManager() {
        epicBuckets = EpicBuckets.getInstance();
        shopDatabase = new HashMap<>();
        increaseSlots = new ArrayList<>();
        decreaseSlots = new ArrayList<>();
        shops = EpicBuckets.getInstance().getConfigManager().getConfig("shops");
    }

    public void init() {
        loadData();
        loadShops();
        setupBulkShop();
    }

    private void setupBulkShop() {
        boolean i = Validator.isMaterial(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".material");
        boolean d = Validator.isMaterial(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".material");
        boolean p = Validator.isMaterial(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".material");
        purchaseSlot = Validator.slot(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".slot"));

        if (purchaseSlot == -1) {
            purchaseSlot = 40;
        }

        increaseItem = ((!i) ? XMaterial.GREEN_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".material")).parseItem());
        decreaseItem = ((!d) ? XMaterial.RED_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".material")).parseItem());
        purchaseItem = ((!p) ? XMaterial.YELLOW_STAINED_GLASS.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".material")).parseItem());
        purchaseItem = InventoryHelper.setDisplayName(purchaseItem, epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopPurchasePath() + ".name"));

        for (String s : epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopIncreasePath() + ".slots").split(",")) {
            increaseSlots.add(Integer.parseInt(s));
        }
        for (String s : epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getBulkShopDecreasePath() + ".slots").split(",")) {
            decreaseSlots.add(Integer.parseInt(s));
        }


    }

    private void loadData() {
        useBackButtons = shops.getBoolean("use-back-buttons");
        closeAfterPurchase = epicBuckets.getConfig().getBoolean("CLOSE-GUI-AFTER-PURCHASE");
        bulkInventoryName = epicBuckets.getConfig().getString(bulkShopPath + ".inventory-name");
        bulkInventorySize = epicBuckets.getConfig().getInt(bulkShopPath + ".size");
        bulkBackButtonSlot = epicBuckets.getConfig().getInt(bulkShopPath + ".return-back-slot");
        bulkFillInventory = epicBuckets.getConfig().getBoolean(bulkShopPath + ".fill");
        bulkMainItemSlot = epicBuckets.getConfig().getInt(bulkShopPath + ".main-item-slot");
    }

    private void loadShops() {
        for (String key : epicBuckets.getConfig().getConfigurationSection(epicBuckets.getConfigManager().getMenuItemsPath()).getKeys(false)) {
            if (!epicBuckets.getConfig().isConfigurationSection(epicBuckets.getConfigManager().getMenuItemsPath() + "." + key)) {
                continue;
            }

            shopDatabase.put(key, new Shop(key, epicBuckets.getConfig().getString(epicBuckets.getConfigManager().getMenuItemsPath() + "." + key + ".shop"), epicBuckets.getConfigManager().getMenuItemsPath() + "." + key));
        }
    }

    public boolean hasEnoughFunds(Player buyer, SubShop s, int amount) {
        if (epicBuckets.getEcon().getBalance(Bukkit.getOfflinePlayer(buyer.getUniqueId())) >= (s.getPrice() * amount)) return true;
        return false;
    }

    public void buyFromShop(Player buyer, SubShop s, int amount) {
        epicBuckets.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(buyer.getUniqueId()), s.getPrice());

        ItemStack genBucket = s.getGenShopItem();
        genBucket.setAmount(amount);
        buyer.getInventory().addItem(NBTHelper.addGenbucketData(genBucket, s.getParent().getTrait(), s));
    }

    public Collection<Shop> getShops() {
        return shopDatabase.values();
    }

    public Shop getShop(String shop) {
        return shopDatabase.get(shop);
    }

    public String getShopPath() {
        return shopPath;
    }

    public ItemStack getIncreaseItem() {
        return increaseItem;
    }

    public ItemStack getDecreaseItem() {
        return decreaseItem;
    }

    public ItemStack getPurchaseItem() {
        return purchaseItem;
    }

    public List<Integer> getIncreaseSlots() {
        return increaseSlots;
    }

    public List<Integer> getDecreaseSlots() {
        return decreaseSlots;
    }

    public int getPurchaseSlot() {
        return purchaseSlot;
    }

    public boolean isUseBackButtons() {
        return useBackButtons;
    }

    public boolean isCloseAfterPurchase() {
        return closeAfterPurchase;
    }

    public String getBulkInventoryName() {
        return bulkInventoryName;
    }

    public int getBulkInventorySize() {
        return bulkInventorySize;
    }

    public boolean isBulkFillInventory() {
        return bulkFillInventory;
    }

    public int getBulkBackButtonSlot() {
        return bulkBackButtonSlot;
    }

    public int getBulkMainItemSlot() {
        return bulkMainItemSlot;
    }

    public List<Integer> getBulkAmounts() {
        return bulkAmounts;
    }
}
