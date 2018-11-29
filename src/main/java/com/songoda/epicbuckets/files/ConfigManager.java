package com.songoda.epicbuckets.files;

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
        createConfig("shops");
    }

    public void createConfig(String name) {
        File f = new File(epicBuckets.getDataFolder(), name + ".yml");
        configDatabase.put(name, new Config(f));
    }

    public FileConfiguration getConfig() {
        return epicBuckets.getConfig();
    }

    public FileConfiguration getConfig(String name) {
        if (!configDatabase.containsKey(name)) return null;
        return configDatabase.get(name).getConfig();
    }

}
