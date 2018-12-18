package com.alixlp.ship.bean;

public class Order {
    private int id;
    private String orderid;
    private String name;
    private String tel;
    private String address;
    private String goods;
    // private Goods goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderid='" + orderid + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", goods='" + goods + '\'' +
                '}';
    }
}
