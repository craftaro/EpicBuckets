package com.songoda.epicbuckets;

public class References {

    public static String getPrefix() {
        return EpicBuckets.getInstance().getLocale().getMessage("general.nametag.prefix") + " ";
    }
}