package com.songoda.epicbuckets.regionhandler;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class RegionWorldBorder {

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
