package com.alixlp.ship.bean;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int id;
    private int kuaidiid; // 快遞單號
    private String token;
    private String password;
    private String username;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
