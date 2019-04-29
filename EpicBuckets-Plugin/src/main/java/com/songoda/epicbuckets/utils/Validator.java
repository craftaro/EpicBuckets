package com.songoda.epicbuckets.utils;

import com.songoda.epicbuckets.genbucket.GenbucketType;

public class Validator {

    public static int inventorySize(String s) {
        int i;
        if (isInt(s)) {
            i = Integer.parseInt(s);
            if (i <= 6) return i;
        }
        return -1;
    }

    public static GenbucketType genbucketType(String s) {
        try {
            return GenbucketType.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static double price(String s) {
        if (isDouble(s)) return Double.parseDouble(s);
        return -1;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMaterial(String mat) {
        try {
            Materials.valueOf(mat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static int slot(String s) {
        if (isInt(s)) return Integer.parseInt(s);
        return -1;
    }

}
