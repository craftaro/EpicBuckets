package com.songoda.epicbuckets.genbuckets;

/**
 * GenbucketType created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 21:40
 */
public enum GenbucketType {

    HORIZONTAL("HORIZONTAL"),
    INFUSED("INFUSED"),
    PSUEDO("PSUEDO"),
    VERTICAL("VERTICAL");

    public final String name;

    GenbucketType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
