package com.example.imagedownload;

public class RecordUesr {
    private String name;
    private int urunishlarSoni;
    private int orni;


    public RecordUesr(String name, int urunishlarSoni, int orni) {
        this.name = name;
        this.urunishlarSoni = urunishlarSoni;
        this.orni = orni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUrunishlarSoni() {
        return urunishlarSoni;
    }

    public void setUrunishlarSoni(int urunishlarSoni) {
        this.urunishlarSoni = urunishlarSoni;
    }

    public int getOrni() {
        return orni;
    }

    public void setOrni(int orni) {
        this.orni = orni;
    }
}
