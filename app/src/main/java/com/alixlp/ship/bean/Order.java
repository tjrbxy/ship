package com.alixlp.ship.bean;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private int id;
    private String orderid;
    private int fahuo;
    private String name;
    private String tel;
    private String address;
    private String add_time;
    private List<Goods> goods;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
