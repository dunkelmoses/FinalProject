package com.example.finalproject.NasaImage;

public class ContactNasaImages {
    protected long id;
    private String date;
    private String regUrl;
    private String hdUrl;


    public ContactNasaImages() {
    }

    public ContactNasaImages(long id, String date, String regUrl, String hdurl) {
        this.id = id;
        this.date = date;
        this.regUrl = regUrl;
        this.hdUrl = hdurl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRegUrl(String regUrl) {
        this.regUrl = regUrl;
    }

    public void setHdurl(String hdurl) {
        hdUrl = hdurl;
    }

    public String getDate() {
        return date;
    }

    public String getRegUrl() {
        return regUrl;
    }

    public String getHdurl() {
        return hdUrl;
    }

    public long getId() {
        return id;
    }
}
