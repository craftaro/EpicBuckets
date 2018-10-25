package com.songoda.epicbuckets.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbuckets.GenbucketItem;
import com.songoda.epicbuckets.genbuckets.GenbucketManager;
import com.songoda.epicbuckets.inventories.InventoryManager;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * GenbucketAdminCommand created by: SoFocused
 * Date Created: oktober 06 2018
 * Time created: 11:56
 */
public class GenbucketAdminCommand implements CommandExecutor {

    private EpicBuckets plugin = EpicBuckets.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {

            if (!sender.hasPermission("genbucketadmin.command.help")) {
                sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                return true;
            }

            sender.sendMessage("");
            sender.sendMessage(ChatUtil.colorString(ChatUtil.getPrefix() + "&7Version " + plugin.getDescription().getVersion() + " Created with <3 by &5&l&oBrianna"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin info&7 - Plugin information"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin admin&7 - Toggle admin mode"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin reload&7 - Reload the configs"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin toggleStatus&7 - Enable/Disable genbuckets"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin toggleSponge&7 - Enable/Disable sponges"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin toggleEnchant&7 - Toggle glowing genbuckets"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin toggleInfinity&7 - Toggle infinite genbuckets"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin activepanel&7 - Opens a GUI"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin setHeight <int>&7 - Modify vertical height"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin setLength <int>&7 - Modify horizontal length"));
            sender.sendMessage(ChatUtil.colorString("&8 - &agenbucketadmin give <player> <shop> <key> <amount>"));
            sender.sendMessage("");
            return true;
        }

        if (args.length == 1) {

            String permission = "genbucketadmin.command." + args[0];

            if (args[0].equalsIgnoreCase("admin")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.playercommand"));
                    return true;
                }

                Player player = (Player) sender;

                boolean isPlayerInAdminMode = GenbucketManager.adminList.contains(player.getUniqueId());

                if (isPlayerInAdminMode) {

                    // Player is in admin mode

                    GenbucketManager.adminList.remove(player.getUniqueId());

                    player.sendMessage(plugin.getLocale().getMessage("event.admin.off"));

                } else {

                    // Player is not in admin mode

                    GenbucketManager.adminList.add(player.getUniqueId());

                    player.sendMessage(plugin.getLocale().getMessage("event.admin.on"));

                }

                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                try {

                    EpicBuckets.getInstance().reloadFiles();

                } catch (Exception e) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.error"));
                    e.printStackTrace();
                }

                sender.sendMessage(plugin.getLocale().getMessage("command.reload.success"));

                return true;
            }


            if (args[0].equalsIgnoreCase("activepanel")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.playercommand"));
                    return true;
                }

                Player player = (Player) sender;

                InventoryManager inventoryManager = new InventoryManager();

                inventoryManager.openActiveGenbuckets(player);

                return true;
            }

            if (args[0].equalsIgnoreCase("toggleStatus")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                boolean oldValue = Util.disableGenbuckets();

                plugin.getConfig().set("DISABLE-GENBUCKETS", !oldValue);

                toggleSetting(sender, "disable-genbuckets", oldValue);

                return true;

            }

            if (args[0].equalsIgnoreCase("toggleInfinity")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                boolean oldValue = Util.infiniteGenbuckets();

                plugin.getConfig().set("INFINITE-USE", !oldValue);

                plugin.saveConfig();

                toggleSetting(sender, "INFINITE-USE", oldValue);

                return true;

            }

            if (args[0].equalsIgnoreCase("toggleSponge")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                boolean oldValue = Util.useSpongeSupport();

                plugin.getConfig().set("USE-SPONGE-SUPPORT", !oldValue);

                plugin.saveConfig();

                toggleSetting(sender, "use-sponge-support", oldValue);

                return true;

            }

            if (args[0].equalsIgnoreCase("info")) {

                String spigotUserId = "%%__USER__%%";
                String resourceId = "%%__RESOURCE__%%";
                String uniqueDownload = "%%__NONCE__%%";
                sender.sendMessage("§aName: §c" + EpicBuckets.getInstance().getDescription().getName());
                sender.sendMessage("§aVersion: §c" + EpicBuckets.getInstance().getDescription().getVersion());
                sender.sendMessage("§aDeveloper: §c" + EpicBuckets.getInstance().getDescription().getAuthors());
                sender.sendMessage("§aSpigot ID: §c" + spigotUserId);
                sender.sendMessage("§aResource ID: §c" + resourceId);
                sender.sendMessage("§aUnique download ID: §c" + uniqueDownload);
                sender.sendMessage("§aRegistered to: §fhttps://www.spigotmc.org/members/" + spigotUserId);

                return true;
            }

            if (args[0].equalsIgnoreCase("toggleEnchant")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                boolean oldValue = Util.enchantGenbuckets();

                plugin.getConfig().set("ENCHANT", !oldValue);

                plugin.saveConfig();

                toggleSetting(sender, "enchant", oldValue);

                return true;

            }


        } // END of args.length == 1

        if (args.length == 2) {

            String permission = "genbucketadmin.command." + args[0];

            if (args[0].equalsIgnoreCase("setheight")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                int height;

                try {
                    height = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {

                    return true;
                }


                plugin.getConfig().set("MAX-VERTICAL-HEIGHT", height);

                toggleSetting(sender, "MAX-VERTICAL-HEIGHT", String.valueOf(height));

                plugin.saveConfig();

                return true;

            }

            if (args[0].equalsIgnoreCase("setlength")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                int height;

                try {
                    height = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {

                    return true;
                }


                plugin.getConfig().set("MAX-HORIZONTAL-LENGTH", height);

                plugin.saveConfig();

                toggleSetting(sender, "MAX-HORIZONTAL-LENGTH", String.valueOf(height));

                return true;

            }

        }

        if (args.length == 5) {

            String permission = "genbucketadmin.command." + args[0];


            if (args[0].equalsIgnoreCase("give")) {

                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(plugin.getLocale().getMessage("event.general.nopermission"));
                    return true;
                }

                // /genbucketadmin give <player> <shop> <key> <amount>

                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                if (!player.hasPlayedBefore()) {

                    sender.sendMessage("§cPlayer '" + args[1] + "' has never joined");

                    return true;
                } else if (!player.isOnline()) {

                    sender.sendMessage("§cPlayer '" + player.getName() + "' is not online!");

                    return true;
                }

                int amount;

                try {
                    amount = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cPlease specify a number");
                    return true;
                }

                String shopName = args[2];
                String key = args[3];

                try {
                    GenbucketItem genbucketItem = new GenbucketItem(shopName, key);

                    genbucketItem.setAmount(amount);

                    player.getPlayer().getInventory().addItem(genbucketItem.getGenbucketItem());

                    sender.sendMessage("§7You sent §e" + genbucketItem.getAmount() + "x " + genbucketItem.getItemName() + "§7 genbucket(s) to §e" + player.getName());
                } catch (NullPointerException e) {
                    sender.sendMessage("§cError while making the genbucket, are you sure you specified a valid shop name and a valid key (config section)?");
                }
                return true;

            }

            return false;
        }

        return false;
    }


    private void toggleSetting(CommandSender sender, String setting, boolean oldValue) {
        sender.sendMessage(plugin.getLocale().getMessage("event.settings.modify", setting.toUpperCase(), String.valueOf(!oldValue)));
    }

    private void toggleSetting(CommandSender sender, String setting, String oldValue) {
        sender.sendMessage(plugin.getLocale().getMessage("event.settings.modify", setting.toUpperCase(), String.valueOf(oldValue)));
    }
}
