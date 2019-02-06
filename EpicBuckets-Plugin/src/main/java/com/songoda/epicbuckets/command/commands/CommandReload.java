package com.songoda.epicbuckets.command.commands;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.command.AbstractCommand;
import com.songoda.epicbuckets.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class CommandReload extends AbstractCommand {

    public CommandReload(AbstractCommand parent) {
        super(parent, false, "reload");
    }

    @Override
    protected ReturnType runCommand(EpicBuckets instance, CommandSender sender, String... args) {
        instance.reload();
        sender.sendMessage(ChatUtil.colorPrefix(instance.getLocale().getMessage("command.reload.success")));
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "epicbuckets.admin";
    }

    @Override
    public String getSyntax() {
        return "/eb reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the messages & config files";
    }
}
