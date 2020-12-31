package com.alixlp.ship.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    private int id;
    private int num;
    private int gid;
    private int scan;
    private String title;
    private String remark;
}
