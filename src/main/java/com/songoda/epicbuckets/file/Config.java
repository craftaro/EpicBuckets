package com.songoda.epicbuckets.file;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private FileConfiguration handler;
    private File file;
    private boolean isResource;

    public Config(File file, boolean resource) {
        this.file = file;
        this.isResource = resource;
        initializeHandler();
    }

    private void initializeHandler() {
        if (!file.exists()) {
            try {
                if (isResource) {
                    EpicBuckets.getInstance().saveResource(file.getName(), false);
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handler = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadConfig() {
        try {
            handler.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return handler;
    }

}
