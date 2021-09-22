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
public class Goods implements Serializable {
    private int id;
    private int num;
    private int gid;
    private int scan;
    private String title;
    private String remark;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
