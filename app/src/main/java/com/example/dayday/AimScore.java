package com.example.dayday;

import org.litepal.crud.LitePalSupport;

//存储的时候，需要继承一个类，然后再litepal。xml里面声明，然后再添加上去
//如果有多个的话，需要有id
//添加的话，就是没有则添加
//更新的话，则是要找到才能更新
public class AimScore extends LitePalSupport {
    String total="∞";
    String today="1000";
    String userId;
    String first="+5";
    String second="+10";
    String hadTotal="0";
    String hadToday="0";

    public AimScore(String total, String today, String userId, String first, String second, String hadTotal, String hadToday) {
        this.total = total;
        this.today = today;
        this.userId = userId;
        this.first = first;
        this.second = second;
        this.hadTotal = hadTotal;
        this.hadToday = hadToday;
    }

    public String getHadTotal() {
        return hadTotal;
    }

    public void setHadTotal(String hadTotal) {
        this.hadTotal = hadTotal;
    }

    public String getHadToday() {
        return hadToday;
    }

    public void setHadToday(String hadToday) {
        this.hadToday = hadToday;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public AimScore(String total, String today, String userId, String first, String second) {
        this.total = total;
        this.today = today;
        this.userId = userId;
        this.first = first;
        this.second = second;
    }

    public AimScore(String userId) {
        this.userId = userId;
    }


}

