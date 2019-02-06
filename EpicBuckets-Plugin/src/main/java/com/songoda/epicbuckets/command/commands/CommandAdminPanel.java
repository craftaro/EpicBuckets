package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.command.AbstractCommand;
import com.songoda.epicbuckets.gui.GUIPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdminPanel extends AbstractCommand {

    public CommandAdminPanel(AbstractCommand parent) {
        super(parent, true, "admin panel");
    }

    @Override
    protected ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        new GUIPanel((Player)sender);
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "epicbuckets.admin";
    }

    @Override
    public String getSyntax() {
        return "/eb admin panel";
    }

    @Override
    public String getDescription() {
        return "Opens up the panel with all the active genbuckets";
    }
}
