package com.songoda.epicbuckets.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private FileConfiguration handler;
    private File file;

    public Config(File file) {
        initializeHandler(file);
    }

    private void initializeHandler(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.file = file;
        handler = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return handler;
    }

}
