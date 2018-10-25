package com.songoda.epicbuckets.regionhandlers;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

/**
 * WBorder created by: SoFocused
 * Date Created: oktober 05 2018
 * Time created: 22:06
 */
public class RegionWBorder {

    public static boolean isOutsideOfBorder(Location loc) {

        if (EpicBuckets.getInstance().getServer().getVersion().contains("1.7"))
            return true;

        WorldBorder border = loc.getWorld().getWorldBorder();
        double size = border.getSize() / 2;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x > size || (-x) > size) || (z > size || (-z) > size));

    }

}
