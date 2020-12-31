package com.alixlp.ship.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private int kuaidiid; // 快遞單號
    private String token;
    private String password;
    private String username;
}
