package com.example.dayday;

import org.litepal.crud.LitePalSupport;

public class Plan_data extends LitePalSupport {

    String planName;
    String stEnd;
    int hour;
    int min;
    boolean alarmIsSet;
    boolean planIsFinished;
    long myId;
    String userId;
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStEnd() {
        return stEnd;
    }

    public void setStEnd(String stEnd) {
        this.stEnd = stEnd;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isAlarmIsSet() {
        return alarmIsSet;
    }

    public void setAlarmIsSet(boolean alarmIsSet) {
        this.alarmIsSet = alarmIsSet;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public boolean isPlanIsFinished() {
        return planIsFinished;
    }

    public void setPlanIsFinished(boolean planIsFinished) {
        this.planIsFinished = planIsFinished;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Plan_data(Plan plan){
        this.planName = plan.getTextView();
        this.stEnd = plan.getTextView1();
        this.hour = plan.getHour();
        this.min = plan.getMin();
        this.myId =plan.getMyId();
        this.alarmIsSet = plan.isSetAlarm();
        this.planIsFinished = plan.getCheckBox();
        this.userId=plan.getUserId();
    }

//    public Plan_data(String planName, String stEnd, int hour, int min, boolean alarmIsSet, boolean planIsFinished,long myId) {
//        this.planName = planName;
//        this.stEnd = stEnd;
//        this.hour = hour;
//        this.min = min;
//        this.myId = myId;
//        this.alarmIsSet = alarmIsSet;
//        this.planIsFinished = planIsFinished;
//    }
//
//    public Plan_data(String planName, String stEnd,  boolean planIsFinished,long myId) {
//        this.planName = planName;
//        this.stEnd = stEnd;
//        this.hour = 0;
//        this.min = 0;
//        this.myId = myId;
//        this.alarmIsSet = false;
//        this.planIsFinished = planIsFinished;
//    }



    public  Plan_data(){}//需要创建一个空的构造方法，否则在findAll的时候，就会出现不能实例化的错误

}
