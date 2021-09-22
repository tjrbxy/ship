package com.alixlp.ship.bean;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent implements Serializable {
    private int id;
    private String name;
    private String tel;
    private String weixin;
    private String address;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
