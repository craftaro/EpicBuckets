package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.References;
import com.songoda.epicbuckets.command.AbstractCommand;
import com.songoda.epicbuckets.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class CommandHelp extends AbstractCommand {

    public CommandHelp(AbstractCommand parent) {
        super(parent, false, "help", "?");
    }

    @Override
    protected ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        sender.sendMessage("");
        sender.sendMessage(ChatUtil.colorString(References.getPrefix() + "&7Version " + instance.getDescription().getVersion() + " Created with <3 by &5&l&oSongoda"));

        for (AbstractCommand command : instance.getCommandManager().getCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(ChatUtil.colorString("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/eb help/?";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}
