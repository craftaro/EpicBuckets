package com.songoda.epicbuckets.utils.updateModules;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.update.Module;
import com.songoda.update.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LocaleModule implements Module {

    @Override
    public void run(Plugin plugin) {
        JSONObject json = plugin.getJson();
        try {
            JSONArray files = (JSONArray) json.get("neededFiles");
            for (Object o : files) {
                JSONObject file = (JSONObject) o;

                if (file.get("type").equals("locale")) {
                    InputStream in = new URL((String) file.get("link")).openStream();
                    EpicBuckets.getInstance().getLocale().saveDefaultLocale(in, (String) file.get("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
