package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.command.AbstractCommand;
import com.songoda.epicbuckets.gui.GUIMain;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandEpicBuckets extends AbstractCommand {

    public CommandEpicBuckets() {
        super(null, true, "epicbuckets");
        addSubCommand("shop");
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        new GUIMain((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicBuckets instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicbuckets.shop";
    }

    @Override
    public String getSyntax() {
        return "/eb <shop>";
    }

    @Override
    public String getDescription() {
        return "opens up the genbucket shop";
    }
}
