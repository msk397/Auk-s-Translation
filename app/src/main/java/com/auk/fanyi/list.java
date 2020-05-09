package com.auk.fanyi;

public class list {

    private String yuan,mudi;
    private int imageId;

    public list(String name, String mudi,int imageId) {
        this.yuan = name;
        this.mudi = mudi;
        this.imageId = imageId;
    }

    public String getYuan() {
        return yuan;
    }

    public String getMudi() {
        return mudi;
    }

    public int getImageId() {
        return imageId;
    }
}
