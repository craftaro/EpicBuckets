package com.songoda.epicbuckets.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.shop.Shop;
import com.songoda.epicbuckets.shop.SubShop;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTabbing {

    public static void registerCommandCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = EpicBuckets.getInstance().getCommandManager().getCommandCompletions();

        commandCompletions.registerCompletion("traits", c -> {
           if (c.getSender() instanceof Player) {
               List<String> traits = new ArrayList<>();
               for (Shop shop : EpicBuckets.getInstance().getShopManager().getShops()) {
                   if (!traits.contains(shop.getTrait().name())) {
                       traits.add(shop.getTrait().name());
                   }
               }
               return traits;
           }
           return null;
        });

        commandCompletions.registerCompletion("genitems", c -> {
            if (c.getSender() instanceof Player) {
                List<String> genitems = new ArrayList<>();
                for (Shop shop : EpicBuckets.getInstance().getShopManager().getShops()) {
                    for (SubShop subShop : shop.getSubShops()) {
                        if (!genitems.contains(subShop.getGenItem().getType().name())) {
                            genitems.add(subShop.getGenItem().getType().name());
                        }
                    }
                }
                return genitems;
            }
            return null;
        });
    }

}
