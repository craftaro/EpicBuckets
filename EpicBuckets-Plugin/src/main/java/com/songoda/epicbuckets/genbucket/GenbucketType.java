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

    public String formatName() {
        return name.toUpperCase().charAt(0) + name.toLowerCase().substring(1);
    }

    @Override
    public String toString() {
        return name;
    }

}

