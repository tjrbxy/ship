package com.alixlp.ship.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private String orderid;
    private int fahuo;
    private String name;
    private String tel;
    private String address;
    private String add_time;
    private List<Goods> goods;
}
