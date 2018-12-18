package com.alixlp.ship.bean;

public class Goods {
    private int id;
    private int num;
    private int gid;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", num=" + num +
                ", gid=" + gid +
                ", title='" + title + '\'' +
                '}';
    }
}
