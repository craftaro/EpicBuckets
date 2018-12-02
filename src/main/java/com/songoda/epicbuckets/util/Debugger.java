package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.ChatColor;

public class Debugger {

    private EpicBuckets epicBuckets;

    public Debugger() {
        epicBuckets = EpicBuckets.getInstance();
    }

    public void sendConsole(String message) {
        epicBuckets.getLogger().info(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void invalidSlot(String item) {
        sendConsole(item + " has an invalid slot set, disabling shop..");
    }

    public void invalidPrice(String item) {
        sendConsole(item + " has an invalid price set, disabling shop..");
    }

}
