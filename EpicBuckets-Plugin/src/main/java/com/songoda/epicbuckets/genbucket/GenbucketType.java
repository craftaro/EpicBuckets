package com.songoda.epicbuckets.genbucket;

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

