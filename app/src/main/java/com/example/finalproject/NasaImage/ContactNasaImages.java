package com.example.finalproject.NasaImage;

public class ContactNasaImages {
    protected long id;
    private String date;
    private String regUrl;
    private String hdUrl;
    private String message;

    public ContactNasaImages() {
    }

    public ContactNasaImages(long id, String date, String regUrl, String hdurl, String message) {
        this.id = id;
        this.date = date;
        this.regUrl = regUrl;
        this.hdUrl = hdurl;
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
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
    public String getMessage() {
        return message;
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
