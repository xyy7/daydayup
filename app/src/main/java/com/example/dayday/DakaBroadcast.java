package com.example.dayday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DakaBroadcast extends BroadcastReceiver {

    static String state="1";//默认是1，等着来修改
    String test;

    public String getState() {
        Log.d("stateinis",state);
        return state;
}

    public void setState(String state) {
        this.state = state;
    }

    public String getTest() {
        return test;
    }

    //other 可以实现静态注册，但是仍然需要注册一下那么
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //静态注册的话：不能用来传递数据，因为每次调用这个的时候会直接挂掉，所以变量不能存储；但是动态注册的可以；或者使用静态变量
        state=intent.getStringExtra("state");
        test=intent.getStringExtra("state");
        Log.d("receiveit","state is"+state+intent.getStringExtra("state"));
//        Toast.makeText(context,"state is "+intent.getStringExtra("state"),Toast.LENGTH_SHORT).show();
    }
}
