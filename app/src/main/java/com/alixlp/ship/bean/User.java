package com.alixlp.ship.bean;

public class User {
    private int id;
    private int kuaidiid; // 快遞單號
    private String token;
    private String password;
    private String username;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getKuaidiid() {
        return kuaidiid;
    }

    public void setKuaidiid(int kuaidiid) {
        this.kuaidiid = kuaidiid;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
