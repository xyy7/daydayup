package com.example.dayday;

import org.litepal.crud.LitePalSupport;
public class UserData extends LitePalSupport {
    String name;
    String userId;
    String password;


    public UserData(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.name="";
        //可能还会添加其他属性
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserData(String name, String userId, String password) {
        this.name = name;
        this.userId = userId;
        this.password = password;
    }
}
