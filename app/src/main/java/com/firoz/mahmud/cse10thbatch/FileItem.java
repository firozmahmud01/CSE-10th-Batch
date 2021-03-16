package com.firoz.mahmud.cse10thbatch;

public class FileItem {
    String url,filename,databasekey;

    public FileItem(){

    }
    public FileItem(String url, String filename,String databasekey) {
        this.url = url;
        this.databasekey=databasekey;
        this.filename = filename;
    }

    public String getDatabasekey() {
        return databasekey;
    }

    public void setDatabasekey(String databasekey) {
        this.databasekey = databasekey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
