package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;

/**
 * Util created by: SoFocused
 * Date Created: oktober 06 2018
 * Time created: 12:24
 */
public class Util {

    public static boolean disableGenbuckets() {
        return EpicBuckets.getInstance().getConfig().getBoolean("DISABLE-GENBUCKETS");
    }

    public static boolean useSpongeSupport() {
        return EpicBuckets.getInstance().getConfig().getBoolean("USE-SPONGE-SUPPORT");
    }

    public static boolean enchantGenbuckets() {
        return EpicBuckets.getInstance().getConfig().getBoolean("ENCHANT");
    }

    public static boolean infiniteGenbuckets() {
        return EpicBuckets.getInstance().getConfig().getBoolean("INFINITE-USE");
    }

}
