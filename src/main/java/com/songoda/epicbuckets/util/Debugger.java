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

    public void invalidInventorySize(String item) {
        sendConsole(item + " has an invalid inventory size, disabling shop..");
    }

    public void invalidGenbucketType(String item) {
        sendConsole(item + " has an invalid Genbucket type, disabling shop..");
    }

    public void invalidSlot(String item) {
        sendConsole(item + " has an invalid slot, disabling shop..");
    }

    public void invalidPrice(String item) {
        sendConsole(item + " has an invalid price set, disabling shop..");
    }

}
