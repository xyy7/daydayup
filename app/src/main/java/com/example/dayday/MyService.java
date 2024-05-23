package com.example.dayday;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {
    public MyService() {
    }
    public static String ACTION_ALARM = "action_alarm";
    private Handler mHanler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Vibrator mVibrator;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d("alarmclock","come here123!");
        mHanler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyService.this, "闹钟来啦", Toast.LENGTH_SHORT).show();
                Log.d("alarmclock","come here!");
                mVibrator = (Vibrator) MyService.this.getSystemService(Service.VIBRATOR_SERVICE);
                mVibrator.vibrate(new long[]{100,2000,1000,1000}, -1);
                Toast.makeText(MyService.this, intent.getStringExtra("plan")+"闹钟时间到了!", Toast.LENGTH_LONG).show();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
