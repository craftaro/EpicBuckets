package com.songoda.epicbuckets;

import org.bukkit.plugin.java.JavaPlugin;

public class EpicBuckets extends JavaPlugin {

    private static EpicBuckets instance;

    public static EpicBuckets getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

}
