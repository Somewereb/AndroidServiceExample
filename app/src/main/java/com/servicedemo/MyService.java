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
 * description:启动式服务
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    private String data = "mydata";
    private long startTime, endTime;
    private SimpleDateFormat simpleDateFormat;
    private final String CHANNELID = "Notification_ID";//渠道id
    private final int NotificationID = 111;//Notification id


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, CHANNELID).build();
            startForeground(NotificationID, notification);
        }
        Log.i(TAG, TAG + "    onCreate");
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        Log.i(TAG, TAG + "    startForegroundService");
        return super.startForegroundService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = System.currentTimeMillis();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Log.i(TAG, TAG + "    onStartCommand" + "    " + simpleDateFormat.format(startTime));
        data = intent.getStringExtra("data");
        Log.i(TAG, "Activity传过来的数据:" + data);
        Thread thread = Thread.currentThread();
        //todo 会发生ANR 因为服务运行在UI线程中
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.e(TAG, "当前运行的线程名：" + thread.getName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, TAG + "    onDestroy:" + "    " + simpleDateFormat.format(System.currentTimeMillis()));
        super.onDestroy();
    }

}
