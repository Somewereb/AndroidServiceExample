package com.servicedemo;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * time:2019/3/2 0:13
 * author:somewereb
 * description:绑定式服务
 */
public class MyService2 extends Service {
    private static final String TAG = "MyService2";
    private String data = "mydata";
    private long startTime, endTime;
    private SimpleDateFormat simpleDateFormat;
    private final String CHANNELID = "Notification_ID";//渠道id
    private final int NotificationID = 111;//Notification id
    private boolean flag = true;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        data = intent.getStringExtra("data");
        Log.i(TAG, TAG + "    onBind" + "    " + data);
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (flag) {
                    try {
                        Thread.sleep(1000);
                        Log.e(TAG, "时间" + new SimpleDateFormat("yyyy:MM:dd HH:ss:mm", Locale.CHINA).format(new Date(System.currentTimeMillis())));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, TAG + "    onUnbind" );
        return true;
//        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, TAG + "    onRebind" );
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Notification notification = new Notification.Builder(this, CHANNELID).build();
//            startForeground(NotificationID, notification);
//        }
        Log.i(TAG, TAG + "    onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, TAG + "    onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, TAG + "    onDestroy:");
        flag = false;
        super.onDestroy();
    }

    class MyBinder extends Binder{
        public MyService2 getMyService() {
            return MyService2.this;
        }
    }
}
