package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;

public class Validator {

    static Validator instance = new Validator();

    public static Validator getInstance() {
        return instance;
    }

    private EpicBuckets epicBuckets = EpicBuckets.getInstance();

    private Validator() {
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

}
