package com.example.dayday;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TextView;

public class Plan {
    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    private String name;//没用
    private int imageId;
    private TextView editText;
    private TextView editText1;
    private TextView textView;//没用
    private CheckBox checkBox;
    private Button button;
    private long myId;
    private boolean isSetAlarm;
    private int hour;
    private int min;
    private String userId;

    public boolean isSetAlarm() {
        return isSetAlarm;
    }

    public void setSetAlarm(boolean setAlarm) {
        isSetAlarm = setAlarm;
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

//    public Plan(String name, int imageId, TextView editText, TextView editText1, TextView textView, CheckBox checkBox, Button button, long myId, boolean isSetAlarm, int hour, int min) {
//        this.name = name;
//        this.imageId = imageId;
//        this.editText = editText;
//        this.editText1 = editText1;
//        this.textView = textView;
//        this.checkBox = checkBox;
//        this.button = button;
//        this.myId = myId;
//        this.isSetAlarm = isSetAlarm;
//        this.hour = hour;
//        this.min = min;
//    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Plan(int imageId, TextView editText, TextView editText1, CheckBox checkBox, Button button, long myId, String userId) {
        this.imageId = imageId;
        this.editText = editText;
        this.checkBox = checkBox;
        this.button = button;
        this.editText1=editText1;
        this.myId = myId;
        this.isSetAlarm=false;
        this.min=0;
        this.hour=0;
        this.userId = userId;
    }

    public Plan( TextView editText, TextView editText1, CheckBox checkBox, Button button, long myId, String userId) {
        this.editText = editText;
        this.checkBox = checkBox;
        this.button = button;
        this.editText1=editText1;
        this.myId = myId;
        this.isSetAlarm=false;
        this.min=0;
        this.hour=0;
        this.userId = userId;
    }

    public boolean getCheckBox() {
        return checkBox.isChecked();
    }

    public void setCheckBox(boolean state) {
        this.checkBox.setChecked(state);
    }

    public Plan(String name, int imageId, TextView editText, TextView textView) {
        this.name = name;
        this.imageId = imageId;
        this.editText=editText;
        this.textView=textView;
    }



    public void setTextView(String s) {
        this.editText.setText(s);
    }
    public void setTextView1(String s) {
        this.editText1.setText(s);
    }


    public String getTextView() {
        return editText.getText().toString();
    }

    public String getTextView1() {
        return editText1.getText().toString();
    }

    public int getImageId() {
        return imageId;
    }
}
