package com.songoda.epicbuckets.file;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.GenbucketType;
import com.songoda.epicbuckets.util.InventoryHelper;
import com.songoda.epicbuckets.util.Validator;
import com.songoda.epicbuckets.util.XMaterial;
import me.lucko.helper.cooldown.Cooldown;
import me.lucko.helper.cooldown.CooldownMap;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigManager {

    private EpicBuckets epicBuckets;
    private HashMap<String, Config> configDatabase;

    private HashMap<String, Consumer<Boolean>> settingsGenbucketBooleans;
    private HashMap<String, Consumer<Integer>> settingsGenbucketIntegers;

    private HashMap<String, Consumer<Boolean>> settingsHooks;

    private HashMap<String, Consumer<Boolean>> settingsShop;

    private String backButtonPath = "BACK-BUTTON";
    private String fillItemPath = "FILL-ITEM";
    private String bulkShopIncreasePath = "BULK-SHOP-INVENTORY.increase-item";
    private String bulkShopDecreasePath = "BULK-SHOP-INVENTORY.decrease-item";
    private String bulkShopPurchasePath = "BULK-SHOP-INVENTORY.purchase-item";
    private String menuItemsPath = "MENU-ITEMS";

    private List<XMaterial> ignoredMaterials;
    private List<XMaterial> psuedoMaterials;

    private LinkedHashMap<String, Integer> genbucketGroups;

    private HashMap<GenbucketType, List<BlockFace>> genbucketValidFaces;
    private HashMap<GenbucketType, BlockFace> genbucketDefaultFace;
    private HashMap<GenbucketType, List<BlockFace>> genbucketLogicalFaces;

    private boolean supportFactions;
    private boolean supportWorldGuard;
    private boolean supportGriefPrevention;

    private boolean gensInWilderness;
    private boolean enchantGenbuckets;
    private boolean spongeCheck;
    private int spongeRadius;
    private int maxGenbucketsPerPlayer;
    private boolean unlimitedGenbuckets;

    private boolean infiniteUse;
    private boolean chargeInfiniteUse;
    private HashMap<GenbucketType, HashMap<ItemStack, Double>> infiniteUseCost;

    private int genbucketDelay;
    private CooldownMap<Player> cooldownMap;

    private int maxVerticalHeight;
    private int maxHorizontalLength;
    private int delay;
    private boolean genbucketsDisabled;
    private int inventorySize;
    private String inventoryName;
    private boolean fillInventory;

    private ItemStack backButton;
    private ItemStack fillItem;

    public ConfigManager() {
        this.epicBuckets = EpicBuckets.getInstance();
    }

    public void setup() {
        configDatabase = new HashMap<>();
        createConfig("shops", true);
        loadData();
        setupBackButton();
        setupFillItem();

        loadBooleanSettings();
        loadIntegerSettings();

        epicBuckets.getShopManager().init();
    }

    private void loadIntegerSettings() {
        /*
        Genbucket
         */
        settingsGenbucketIntegers.put("Cooldown between placements", this::setCooldown);
        settingsGenbucketIntegers.put("Sponge radius", this::setSpongeRadius);
        settingsGenbucketIntegers.put("Max active gens per player", this::setMaxGenbucketsPerPlayer);
        settingsGenbucketIntegers.put("Max vertical height", this::setMaxVerticalHeight);
        settingsGenbucketIntegers.put("Max horizontal length", this::setMaxHorizontalLength);
        settingsGenbucketIntegers.put("Genbucket Speed", this::setDelay);
    }

    private void loadBooleanSettings() {
        /*
        Genbucket
         */
        settingsGenbucketBooleans.put("Gens in Wilderness", this::setGensInWilderness);
        settingsGenbucketBooleans.put("Infinite genbucket use", this::setInfiniteUse);
        settingsGenbucketBooleans.put("Charge infinite use placement", this::setChargeInfiniteUse);
        settingsGenbucketBooleans.put("Sponge check", this::setSpongeCheck);
        settingsGenbucketBooleans.put("Unlimited active gens", this::setUnlimitedGenbuckets);
        settingsGenbucketBooleans.put("Disable genbuckets", this::setGenbucketsDisabled);

        /*
        Hooks
         */
        settingsHooks.put("Factions Support", this::setSupportFactions);
        settingsHooks.put("Worldguard Support", this::setSupportWorldGuard);
        settingsHooks.put("GriefPrevention Support", this::setSupportGriefPrevention);

        /*
        Shop
         */
        settingsShop.put("Close GUI after purchase", epicBuckets.getShopManager()::setCloseAfterPurchase);
        settingsShop.put("Fill bulk shop", epicBuckets.getShopManager()::setBulkFillInventory);
        settingsShop.put("Fill shops", this::setFillInventory);
        settingsShop.put("Use back buttons in shops", epicBuckets.getShopManager()::setUseBackButtons);
    }

    public void reload() {
        reloadConfig("shops");
        epicBuckets.reloadConfig();
        loadData();
        setupBackButton();
        setupFillItem();
    }

    private void loadData() {
        ignoredMaterials = new ArrayList<>();
        psuedoMaterials = new ArrayList<>();
        genbucketGroups = new LinkedHashMap<>();
        infiniteUseCost = new HashMap<>();

        loadValidFaces();

        ignoredMaterials = InventoryHelper.convertMaterialList(epicBuckets.getConfig().getStringList("IGNORE-MATERIALS"), "IGNORE-MATERIALS");
        psuedoMaterials = InventoryHelper.convertMaterialList(epicBuckets.getConfig().getStringList("PSUEDO-MATERIALS"), "PSUEDO-MATERIALS");
        supportFactions = epicBuckets.getConfig().getBoolean("FACTIONS-SUPPORT");
        supportWorldGuard = epicBuckets.getConfig().getBoolean("WORLDGUARD-SUPPORT");
        supportGriefPrevention = epicBuckets.getConfig().getBoolean("GRIEFPREVENTION-SUPPORT");
        gensInWilderness = epicBuckets.getConfig().getBoolean("ENABLE-GENS-IN-WILDERNESS");
        enchantGenbuckets = epicBuckets.getConfig().getBoolean("ENCHANT");
        spongeCheck = epicBuckets.getConfig().getBoolean("USE-SPONGE-SUPPORT");
        spongeRadius = epicBuckets.getConfig().getInt("SPONGE-RADIUS");
        maxGenbucketsPerPlayer = epicBuckets.getConfig().getInt("MAX-ACTIVE-GEN-PER-PLAYER");
        unlimitedGenbuckets = epicBuckets.getConfig().getBoolean("PLACE-UNLIMTED-GENS");
        infiniteUse = epicBuckets.getConfig().getBoolean("INFINITE-USE");
        maxVerticalHeight = epicBuckets.getConfig().getInt("MAX-VERTICAL-HEIGHT");
        maxHorizontalLength = epicBuckets.getConfig().getInt("MAX-HORIZONTAL-LENGTH");
        delay = epicBuckets.getConfig().getInt("DELAY");
        setGenbucketsDisabled(epicBuckets.getConfig().getBoolean("DISABLE-GENBUCKETS"));
        inventorySize = epicBuckets.getConfig().getInt(menuItemsPath + ".size");
        inventoryName = epicBuckets.getConfig().getString(menuItemsPath + ".inventory-name");
        fillInventory = epicBuckets.getConfig().getBoolean(menuItemsPath + ".fill");
        chargeInfiniteUse = epicBuckets.getConfig().getBoolean("CHARGE-FOR-INFINITE-USE");

        epicBuckets.getConfig().getConfigurationSection("CUSTOM-ACTIVE-GEN-PER-PLAY").getKeys(false)
                .forEach(s -> genbucketGroups.put(epicBuckets.getConfig().getString("CUSTOM-ACTIVE-GEN-PER-PLAY." + s).split(":")[1],
                        Integer.parseInt(epicBuckets.getConfig().getString("CUSTOM-ACTIVE-GEN-PER-PLAY." + s).split(":")[0])));

        epicBuckets.getConfig().getConfigurationSection("COST-FOR-INFINITE-USE").getKeys(false)
                .forEach(bucket -> {
                    HashMap<ItemStack, Double> chargingCostsPerItem = new HashMap<>();
                    epicBuckets.getConfig().getConfigurationSection("COST-FOR-INFINITE-USE." + bucket).getKeys(false)
                            .forEach(item -> chargingCostsPerItem.put(XMaterial.valueOf(item).parseItem(), epicBuckets.getConfig().getDouble("COST-FOR-INFINITE-USE." + bucket + "." + item)));
                    infiniteUseCost.put(GenbucketType.valueOf(bucket), chargingCostsPerItem);
                });

        setCooldown(epicBuckets.getConfig().getInt("GENBUCKET-DELAY"));
    }

    private void loadValidFaces() {
        genbucketValidFaces = new HashMap<>();
        genbucketDefaultFace = new HashMap<>();
        genbucketLogicalFaces = new HashMap<>();

        epicBuckets.getConfig().getConfigurationSection("VALID-FACES").getKeys(false).forEach(s -> {
            genbucketDefaultFace.put(GenbucketType.valueOf(s), BlockFace.valueOf(epicBuckets.getConfig().getString("VALID-FACES." + s + ".DEFAULT")));
            genbucketValidFaces.put(GenbucketType.valueOf(s), epicBuckets.getConfig().getStringList("VALID-FACES." + s + ".WHITELIST").stream().map(s1 -> BlockFace.valueOf(s1)).collect(Collectors.toList()));
        });

        genbucketLogicalFaces.put(GenbucketType.HORIZONTAL, new ArrayList<>(Arrays.asList(BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH)));
        genbucketLogicalFaces.put(GenbucketType.VERTICAL, new ArrayList<>(Arrays.asList(BlockFace.UP, BlockFace.DOWN)));
        genbucketLogicalFaces.put(GenbucketType.INFUSED, new ArrayList<>(Arrays.asList(BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH)));
        genbucketLogicalFaces.put(GenbucketType.PSUEDO, new ArrayList<>(Arrays.asList(BlockFace.UP, BlockFace.DOWN)));
    }

    private void setCooldown(int genbucketDelay) {
        setGenbucketDelay(genbucketDelay);
        if (getGenbucketDelay() > 0) {
            cooldownMap = CooldownMap.create(Cooldown.of(getGenbucketDelay() / 20, TimeUnit.SECONDS));
        } else {
            cooldownMap = null;
        }
    }

    private void setupFillItem() {
        boolean m = Validator.isMaterial(epicBuckets.getConfig().getString(getFillItemPath() + ".material"));

        fillItem = ((!m) ? XMaterial.BLACK_STAINED_GLASS_PANE.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(getFillItemPath() + ".material")).parseItem());
        fillItem = InventoryHelper.setDisplayName(fillItem, epicBuckets.getConfig().getString(getFillItemPath() + ".name"));
    }

    private void setupBackButton() {
        boolean m = Validator.isMaterial(epicBuckets.getConfig().getString(getBackButtonPath() + ".material"));

        backButton = ((!m) ? XMaterial.BARRIER.parseItem() : XMaterial.valueOf(epicBuckets.getConfig().getString(getBackButtonPath() + ".material")).parseItem());
        backButton = InventoryHelper.setDisplayName(backButton, epicBuckets.getConfig().getString(getBackButtonPath() + ".name"));
    }

    public boolean isOnCooldown(Player player) {
        if (cooldownMap == null) return false;
        return !cooldownMap.test(player);
    }

    public void createConfig(String name, boolean resource) {
        File f = new File(epicBuckets.getDataFolder(), name + ".yml");
        configDatabase.put(name, new Config(f, resource));
    }

    public void reloadConfig(String name) {
        if (!configDatabase.containsKey(name)) return;
        configDatabase.get(name).reloadConfig();
    }

    public FileConfiguration getConfig(String name) {
        if (!configDatabase.containsKey(name)) return null;
        return configDatabase.get(name).getConfig();
    }

    public String getBackButtonPath() {
        return backButtonPath;
    }

    public String getFillItemPath() {
        return fillItemPath;
    }

    public String getBulkShopIncreasePath() {
        return bulkShopIncreasePath;
    }

    public String getBulkShopDecreasePath() {
        return bulkShopDecreasePath;
    }

    public String getBulkShopPurchasePath() {
        return bulkShopPurchasePath;
    }

    public List<XMaterial> getIgnoredMaterials() {
        return ignoredMaterials;
    }

    public List<XMaterial> getPsuedoMaterials() {
        return psuedoMaterials;
    }

    public boolean isSupportFactions() {
        return supportFactions;
    }

    public boolean isSupportWorldGuard() {
        return supportWorldGuard;
    }

    public boolean isSupportGriefPrevention() {
        return supportGriefPrevention;
    }

    public boolean isGensInWilderness() {
        return gensInWilderness;
    }

    public boolean isEnchantGenbuckets() {
        return enchantGenbuckets;
    }

    public boolean isSpongeCheck() {
        return spongeCheck;
    }

    public int getSpongeRadius() {
        return spongeRadius;
    }

    public int getMaxGenbucketsPerPlayer() {
        return maxGenbucketsPerPlayer;
    }

    public boolean isUnlimitedGenbuckets() {
        return unlimitedGenbuckets;
    }

    public int getMaxVerticalHeight() {
        return maxVerticalHeight;
    }

    public int getMaxHorizontalLength() {
        return maxHorizontalLength;
    }

    public int getDelay() {
        return delay;
    }

    public void setGenbucketsDisabled(boolean enabled) {
        this.genbucketsDisabled = enabled;
    }

    public boolean isGenbucketsDisabled() {
        return genbucketsDisabled;
    }

    public ItemStack getBackButton() {
        return backButton;
    }

    public String getMenuItemsPath() {
        return menuItemsPath;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public boolean isFillInventory() {
        return fillInventory;
    }

    public ItemStack getFillItem() {
        return fillItem;
    }

    public LinkedHashMap<String, Integer> getGenbucketGroups() {
        return genbucketGroups;
    }

    public List<BlockFace> getValidFacesForGenbucket(GenbucketType genbucketType) {
        return genbucketValidFaces.get(genbucketType);
    }

    public BlockFace getDefaultFaceForGenbucket(GenbucketType genbucketType) {
        return genbucketDefaultFace.get(genbucketType);
    }

    public List<BlockFace> getLogicalFacesForGenbucket(GenbucketType genbucketType) {
        return genbucketLogicalFaces.get(genbucketType);
    }

    public boolean isInfiniteUse() {
        return infiniteUse;
    }

    public boolean isChargeInfiniteUse() {
        return chargeInfiniteUse;
    }

    public double getInfiniteUseCostForGenbucketType(GenbucketType genbucketType, ItemStack item) {
        return infiniteUseCost.get(genbucketType).get(item);
    }

    public int getGenbucketDelay() {
        return genbucketDelay;
    }

    public void setBackButtonPath(String backButtonPath) {
        this.backButtonPath = backButtonPath;
    }

    public void setSupportFactions(boolean supportFactions) {
        this.supportFactions = supportFactions;
    }

    public void setSupportWorldGuard(boolean supportWorldGuard) {
        this.supportWorldGuard = supportWorldGuard;
    }

    public void setSupportGriefPrevention(boolean supportGriefPrevention) {
        this.supportGriefPrevention = supportGriefPrevention;
    }

    public void setGensInWilderness(boolean gensInWilderness) {
        this.gensInWilderness = gensInWilderness;
    }

    public void setSpongeCheck(boolean spongeCheck) {
        this.spongeCheck = spongeCheck;
    }

    public void setSpongeRadius(int spongeRadius) {
        this.spongeRadius = spongeRadius;
    }

    public void setMaxGenbucketsPerPlayer(int maxGenbucketsPerPlayer) {
        this.maxGenbucketsPerPlayer = maxGenbucketsPerPlayer;
    }

    public void setUnlimitedGenbuckets(boolean unlimitedGenbuckets) {
        this.unlimitedGenbuckets = unlimitedGenbuckets;
    }

    public void setInfiniteUse(boolean infiniteUse) {
        this.infiniteUse = infiniteUse;
    }

    public void setChargeInfiniteUse(boolean chargeInfiniteUse) {
        this.chargeInfiniteUse = chargeInfiniteUse;
    }

    public void setGenbucketDelay(int genbucketDelay) {
        this.genbucketDelay = genbucketDelay;
    }

    public void setMaxVerticalHeight(int maxVerticalHeight) {
        this.maxVerticalHeight = maxVerticalHeight;
    }

    public void setMaxHorizontalLength(int maxHorizontalLength) {
        this.maxHorizontalLength = maxHorizontalLength;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setFillInventory(boolean fillInventory) {
        this.fillInventory = fillInventory;
    }

    public HashMap<String, Consumer<Boolean>> getSettingsGenbucketBooleans() {
        return settingsGenbucketBooleans;
    }

    public HashMap<String, Consumer<Integer>> getSettingsGenbucketIntegers() {
        return settingsGenbucketIntegers;
    }

    public HashMap<String, Consumer<Boolean>> getSettingsHooks() {
        return settingsHooks;
    }

    public HashMap<String, Consumer<Boolean>> getSettingsShop() {
        return settingsShop;
    }
}
