package com.songoda.epicbuckets.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.gui.GUIMain;
import com.songoda.epicbuckets.gui.GUIPanel;
import com.songoda.epicbuckets.util.ChatUtil;
import org.bukkit.entity.Player;

@CommandAlias("genbucket")
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
    public void panel(Player player) {
        if (!permCheck(player, "genbucket.admin") || !permCheck(player, "genbucket.admin.panel")) return;
        GUIPanel.PANEL.open(player);
    }

}
