package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdminToggle extends AbstractCommand {

    public CommandAdminToggle(AbstractCommand parent) {
        super(parent, true, "admin toggle");
    }

    @Override
    protected ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        instance.getGenbucketManager().toggleAdmin((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "epicbuckets.admin";
    }

    @Override
    public String getSyntax() {
        return "/eb admin toggle";
    }

    @Override
    public String getDescription() {
        return "Toggles your admin status to receive genbucket placement notifications";
    }
}
