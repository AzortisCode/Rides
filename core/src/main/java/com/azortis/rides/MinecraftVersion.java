package com.azortis.rides;

public enum MinecraftVersion {
    v1_15_R1(1151, "v1_15_R1");

    private int versionInteger;
    private String versionString;

    MinecraftVersion(int versionInteger, String versionString){
        this.versionInteger = versionInteger;
        this.versionString = versionString;
    }

    public boolean isNewerThen(MinecraftVersion minecraftVersion){
        return minecraftVersion.versionInteger < this.versionInteger;
    }

    public String getVersionString(){
        return versionString;
    }



}
