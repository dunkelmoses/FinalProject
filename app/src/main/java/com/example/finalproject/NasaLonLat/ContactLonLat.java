package com.example.finalproject.NasaLonLat;

import java.io.Serializable;


class ContactLonLat implements Serializable {

    private long id;
    private String path;
    private double latitude, longitude;

    /**
     * Default no args constructor
     */
    ContactLonLat(){}


    ContactLonLat(long id, double latitude, double longitude, String path) {
        setLatitude(latitude);
        setLongitude(longitude);
        setPath(path);
        setId(id);
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
