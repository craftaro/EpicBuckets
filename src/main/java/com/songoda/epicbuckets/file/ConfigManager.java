package com.songoda.epicbuckets.file;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    private EpicBuckets epicBuckets;
    private HashMap<String, Config> configDatabase;

    public ConfigManager() {
        this.epicBuckets = EpicBuckets.getInstance();
        setup();
    }

    private void setup() {
        epicBuckets.saveDefaultConfig();
        createConfig("shops", true);
    }

    public void createConfig(String name, boolean resource) {
        File f = new File(epicBuckets.getDataFolder(), name + ".yml");
        configDatabase.put(name, new Config(f, resource));
    }

    public void reloadConfig(String name) {
        if (!configDatabase.containsKey(name)) return;
        configDatabase.get(name).reloadConfig();
    }

    public FileConfiguration getConfig(String name) {
        if (!configDatabase.containsKey(name)) return null;
        return configDatabase.get(name).getConfig();
    }

}
