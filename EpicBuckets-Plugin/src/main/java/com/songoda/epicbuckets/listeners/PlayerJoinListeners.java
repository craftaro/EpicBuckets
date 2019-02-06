package com.songoda.epicbuckets.listeners;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.References;
import com.songoda.epicbuckets.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by songoda on 3/13/2017.
 */
public class PlayerJoinListeners implements Listener {

    private EpicBuckets instance;

    public PlayerJoinListeners(EpicBuckets instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            if (instance.getServer().getPluginManager().getPlugin("Factions") != null && instance.getServer().getPluginManager().getPlugin("FactionsFramework") == null) {
                player.sendMessage("");
                player.sendMessage(ChatUtil.colorString(References.getPrefix() + "&7Here's the deal,"));
                player.sendMessage(ChatUtil.colorString("&7I cannot give you full support for Factions out of the box."));
                player.sendMessage(ChatUtil.colorString("&7Things will work without it but if you wan't a flawless"));
                player.sendMessage(ChatUtil.colorString("&7experience you need to download"));
                player.sendMessage(ChatUtil.colorString("&7&6https://www.spigotmc.org/resources/54337/&7."));
                player.sendMessage(ChatUtil.colorString("&7If you don't care and don't want to see this message again"));
                player.sendMessage(ChatUtil.colorString("&7turn &6Helpful-Tips &7off in the config."));
                player.sendMessage("");
            }
        }
    }
}