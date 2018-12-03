package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;
import com.songoda.epicbuckets.genbucket.GenbucketType;

public class Validator {

    static Validator instance = new Validator();

    public static Validator getInstance() {
        return instance;
    }

    private EpicBuckets epicBuckets = EpicBuckets.getInstance();

    private Validator() {
    }

    public int inventorySize(String s) {
        int i;
        if (isInt(s)) {
            i = Integer.parseInt(s);
            if (i%9 == 0 && i<=54) return i;
        }
        return -1;
    }

    public GenbucketType genbucketType(String s) {
        try {
            return GenbucketType.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMaterial(String mat) {
        try {
            XMaterial.valueOf(mat);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public int slot(String s) {
        if (isInt(s)) return Integer.parseInt(s);
        return -1;
    }

}
