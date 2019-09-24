package com.example.myapplication;

public class PlaceItem {
    private int resIdThumbnail;
    private String countryName;

    public PlaceItem(int resIdThumbnail, String countryName) {
        this.resIdThumbnail = resIdThumbnail;
        this.countryName = countryName;
    }

    public int getResIdThumbnail() {
        return this.resIdThumbnail;
    }

    public String getCountryName() {
        return this.countryName;
    }
}
