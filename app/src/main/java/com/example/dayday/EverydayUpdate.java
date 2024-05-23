package com.example.dayday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class EverydayUpdate extends Service {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public EverydayUpdate() {
//        pref= PreferenceManager.getDefaultSharedPreferences();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //应该可以有很多的服务，也可以只是一个服务，但是很复杂吧
                //todo:更新各个打卡的颜色
//                Intent intent1=new Intent("com.example.dayday.MY_BROADCAST");
//
//                intent1.setComponent(new ComponentName("com.example.dayday",
//                        "com.example.dayday.DakaBroadcast"));//传输数据，也可以通过内存来传输吧
//                intent1.putExtra("state",intent.getStringExtra("state"));
//                sendBroadcast(intent1);

                //todo:当然也可以更新每天自己习惯打卡的东西

//                todo：闹钟也是可以控制的
                String uid=intent.getStringExtra("uid");
                String cid=intent.getStringExtra("cid");
                String adminId=intent.getStringExtra("adminId");
                Log.d("inService1",""+pref.getBoolean(uid+adminId+cid,false));
//                Log.d("timeisxxx",""+System.currentTimeMillis());//判断多次启动是否为同一个服务？每次启动都会启动一个新的服务
                pref=getSharedPreferences("first",MODE_PRIVATE);//文件名和写入模式，但是只有一种可以选择
                editor=pref.edit();
                editor.putBoolean(uid+adminId+cid,false);
                editor.apply();
                Log.d("inButton2",""+pref.getBoolean(uid+adminId+cid,false));


            }
        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int interval=60*24*60*1000;
        long triggerTime= SystemClock.elapsedRealtime()+interval;
        Intent intent1=new Intent(this,EverydayUpdate.class);
        intent1.putExtra("first","no");
        PendingIntent pi=PendingIntent.getService(this,0,intent,0);//这里直接传intent
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
