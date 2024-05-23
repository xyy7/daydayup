package com.example.dayday;

import org.litepal.crud.LitePalSupport;


//要改的东西很多很多，比如checkbox状态，edittext，id等等
//增删改查操作都是需要修改的
public class Habit_data extends LitePalSupport {
    boolean ck_state;
    String ed_state;
    String tx_state;
    long myId;
    String userId;

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public Habit_data(boolean ck_state, String ed_state, String tx_state, long myId) {
        this.ck_state = ck_state;
        this.ed_state = ed_state;
        this.tx_state = tx_state;
        this.myId = myId;
    }

    public Habit_data(){}//需要有一个空方法去应对数据库

    public Habit_data(Habit habit) {
        this.ck_state = habit.getCheckBox().isChecked();
        this.ed_state = habit.getEditText().getText().toString();//如果出错了，那么就会使地址的形式
        this.tx_state = habit.getTextView().getText().toString();
        this.myId = habit.getMyId();
        this.userId=habit.getUserId();
    }

    public boolean isCk_state() {
        return ck_state;
    }

    public void setCk_state(boolean ck_state) {
        this.ck_state = ck_state;
    }

    public String getEd_state() {
        return ed_state;
    }

    public void setEd_state(String ed_state) {
        this.ed_state = ed_state;
    }

    public String getTx_state() {
        return tx_state;
    }

    public void setTx_state(String tx_state) {
        this.tx_state = tx_state;
    }

    public Habit_data(boolean ck_state, String ed_state, String tx_state) {
        this.ck_state = ck_state;
        this.ed_state = ed_state;
        this.tx_state = tx_state;
    }
}
