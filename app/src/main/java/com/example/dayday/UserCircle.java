package com.example.dayday;

import org.litepal.crud.LitePalSupport;

public class UserCircle extends LitePalSupport implements Comparable<UserCircle>{
    String cid;
    String uid;//
    String adminId;//而且还要保证cid不重复max+1
    int state=1;
    int days;

    public UserCircle(String cid, String uid, String adminId, int state, int days) {
        this.cid = cid;
        this.uid = uid;
        this.adminId = adminId;
        this.state = state;
        this.days = days;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }



    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public UserCircle(String cid, String uid, int state, int days) {
        this.cid = cid;
        this.uid = uid;
        this.state = state;
        this.days = days;
    }

    @Override
    public int compareTo(UserCircle o) {
        return o.getDays()-days;//相减比较简单，不要想着比较
    }
}
