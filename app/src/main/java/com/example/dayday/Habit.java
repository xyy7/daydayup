package com.example.dayday;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.LitePalSupport;

//实现parcelable可能导致数据库报错
//public class Habit extends LitePalSupport implements Parcelable {
public class Habit extends LitePalSupport {
    private CheckBox checkBox;
    private EditText editText;
    private TextView textView;
    private long myId;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public Habit(CheckBox checkBox, EditText editText, TextView textView, long myId,String userId) {
        this.checkBox = checkBox;
        this.editText = editText;
        this.textView = textView;
        this.myId = myId;
        this.userId=userId;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setTextView(String s) {
        this.textView.setText(s);
    }

    public void setEditText(String s) {
        this.editText.setText(s);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setCheckBox(boolean b) {
        this.checkBox.setChecked(b);
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public Habit(CheckBox checkBox, EditText editText, TextView textView) {
        this.checkBox = checkBox;
        this.editText = editText;
        this.textView = textView;
    }

}
