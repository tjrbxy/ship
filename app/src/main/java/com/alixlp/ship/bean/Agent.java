package com.alixlp.ship.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    private int id;
    private String name;
    private String tel;
    private String weixin;
    private String address;
}
