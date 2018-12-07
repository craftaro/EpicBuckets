package com.songoda.epicbuckets.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.gui.GUIMain;
import com.songoda.epicbuckets.gui.GUIPanel;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("epicbuckets|eb")
public class CommandGenbucket extends BaseCommand {

    private EpicBuckets epicBuckets;

    public CommandGenbucket() {
        epicBuckets = EpicBuckets.getInstance();
    }

    public boolean permCheck(Player player, String perm) {
        if (!player.hasPermission(perm)) {
            player.sendMessage(epicBuckets.getLocale().getMessage("event.general.nopermission"));
            return false;
        }
        return true;
    }

    @Subcommand("help")
    @CatchUnknown @Default
    public void doHelp(CommandSender sender) {
        sender.sendMessage(ChatUtil.colorString("&3&lEpicBuckets"));
        sender.sendMessage(ChatUtil.colorString("&f/epicbuckets|eb help: &7shows this help"));
        sender.sendMessage(ChatUtil.colorString("&f/epicbuckets|eb reload: &7reloads the config"));
        sender.sendMessage(ChatUtil.colorString("&f/epicbuckets|eb shop: &7opens up the genbucket shop"));
        sender.sendMessage(ChatUtil.colorString("&f/epicbuckets|eb admin toggle: &7toggle your status to receive genbucket placement notifications"));
        sender.sendMessage(ChatUtil.colorString("&f/epicbuckets|eb admin panel: &7opens up the panel with all active genbuckets"));
    }

    @Subcommand("shop")
    @Description("Opens up the Genbucket shop")
    public void shop(Player player) {
        if (!permCheck(player, "genbucket.shop")) return;
        new GUIMain(player).open();
    }

    @Subcommand("reload")
    @Description("Reloads the messages & config files")
    public void reload(Player player) {
        if (!permCheck(player, "genbucket.reload")) return;
        epicBuckets.reload();
        player.sendMessage(ChatUtil.colorPrefix(epicBuckets.getLocale().getMessage("command.reload.success")));
    }

    @Subcommand("admin toggle")
    @Description("Toggles your admin status to receive genbucket placement notifications")
    public void admin(Player player) {
        if (!permCheck(player, "genbucket.admin") || !permCheck(player, "genbucket.admin.toggle")) return;
        epicBuckets.getGenbucketManager().toggleAdmin(player);
    }

    @Subcommand("admin panel")
    @Description("Opens up the panel with all the active genbuckets")
    public void panel(Player player) {
        if (!permCheck(player, "genbucket.admin") || !permCheck(player, "genbucket.admin.panel")) return;
        GUIPanel.PANEL.open(player);
    }

}
