package com.firoz.mahmud.cse10thbatch;

public class UploadItem {
    String title,details,time,key;
    public UploadItem(){

    }

    public UploadItem(String title, String details, String time, String key) {
        this.title = title;
        this.details = details;
        this.time = time;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
