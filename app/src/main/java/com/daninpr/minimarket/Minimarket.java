package com.daninpr.minimarket;

public class Minimarket {
    private int id;
    private String nama;
    private String alamat;
    private byte[] img;

    public Minimarket(int id, String nama, String alamat, byte[] img) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
