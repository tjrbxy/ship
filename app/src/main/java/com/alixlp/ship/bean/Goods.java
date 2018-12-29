package com.alixlp.ship.bean;

public class Goods {
    private int id;
    private int num;
    private int gid;


    private int scan;
    private String title;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

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

    public int getScan() {
        return scan;
    }

    public void setScan(int scan) {
        this.scan = scan;
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
