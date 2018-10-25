package com.songoda.epicbuckets.filehandler;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * FileManager created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 22:56
 */
public class FileManager {

    public FileConfiguration config;
    private File file;

    public FileManager(String filename) {

        EpicBuckets main = EpicBuckets.getInstance();

        this.file = new File(main.getDataFolder(), filename);
        if (!this.file.exists()) {
            try {
                boolean b = this.file.createNewFile();

                if (b)
                    main.getLogger().info(filename + " was successfully created");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

}
