package com.songoda.epicbuckets.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.songoda.epicbuckets.gui.GUIMain;
import org.bukkit.entity.Player;

@CommandAlias("genbucket")
public class CommandGenbucket extends BaseCommand {

    @Subcommand("shop")
    @Description("Opens up the Genbucket shop")
    @CommandPermission("genbucket.shop")
    public void shop(Player player) {
        new GUIMain(player).open();
    }

}
