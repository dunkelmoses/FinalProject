package com.example.finalproject.NasaLonLat;
public class ContactLonLat {
    protected long id;
    private String date;
    private String url;
    private String lon;
    private String lat;


    public ContactLonLat() {
    }

    public ContactLonLat(long id, String date, String url, String lon, String lat) {
        this.id = id;
        this.date = date;
        this.url = url;
        this.lon = lon;
        this.lat = lat;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }


    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public long getId() {
        return id;
    }
}