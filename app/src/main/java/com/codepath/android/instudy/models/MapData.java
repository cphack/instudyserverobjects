package com.codepath.android.instudy.models;

/**
 * Created by Alessandro Barreto on 24/06/2016.
 */
public class MapData {
    private String latitude;
    private String longitude;

    public MapData() {
    }

    public MapData(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
