package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.References;
import com.songoda.epicbuckets.command.AbstractCommand;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.SubShop;
import com.songoda.epicbuckets.util.ChatUtil;
import com.songoda.epicbuckets.util.Validator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGive extends AbstractCommand {

    public CommandGive(AbstractCommand parent) {
        super(parent, false, "give");
    }

    @Override
    protected ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        if (args.length < 5 || args.length > 7) {
            return ReturnType.SYNTAX_ERROR;
        }
        if (Bukkit.getPlayerExact(args[1]) == null && !args[1].toLowerCase().equals("all")) {
            sender.sendMessage(ChatUtil.colorString(References.getPrefix() + "&cThat username does not exist, or the user is not online!"));
            return ReturnType.FAILURE;
        }

        Player player = Bukkit.getPlayerExact(args[1]);

        String trait = args[2];
        String genItem = args[3];
        String amount = args[4];
        String cost = args.length == 6 ? args[5] : null;

        if (!Validator.isInt(amount)) {
            return ReturnType.SYNTAX_ERROR;
        }

        SubShop subShop = null;

        for (Shop s : instance.getShopManager().getShops()) {
            if (s.getTrait().name().equals(trait)) {
                for (SubShop ss : s.getSubShops()) {
                    if (ss.getGenItem().getType().name().equals(genItem)) subShop = ss;
                }
            }
        }

        if (subShop != null) {
            if (Validator.isDouble(cost)) {
                instance.getShopManager().buyFromShop(player, subShop, Integer.parseInt(amount));
                return ReturnType.SUCCESS;
            }
            instance.getShopManager().giveGenbucketToPlayer(player, subShop, Integer.parseInt(amount));
            return ReturnType.SUCCESS;
        }

        player.sendMessage(instance.getLocale().getMessage("command.give.genbucketnotfound"));
        return ReturnType.FAILURE;
    }

    @Override
    public String getPermissionNode() {
        return "epicbuckets.give";
    }

    @Override
    public String getSyntax() {
        return "/eb give <player> <trait> <genItem> <amount> [cost]";
    }

    @Override
    public String getDescription() {
        return "Gives a genbucket to a player";
    }
}
