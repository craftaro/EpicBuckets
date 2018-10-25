package com.songoda.epicbuckets.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.inventories.InventoryManager;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * GenbucketCommand created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 23:20
 */
public class GenbucketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.colorPrefix(EpicBuckets.getInstance().messageFile.config.getString("PLAYER-COMMAND")));
            return true;
        }

        if (!sender.hasPermission("genbucket.command")) {
            sender.sendMessage(ChatUtil.colorPrefix(EpicBuckets.getInstance().messageFile.config.getString("NO-PERMISSION")));
            return true;
        }

        Player player = (Player) sender;

        InventoryManager inventoryManager = new InventoryManager();

        inventoryManager.openMainInventory(player);

        return true;

    }
}
