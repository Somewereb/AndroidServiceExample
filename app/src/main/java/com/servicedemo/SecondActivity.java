package com.servicedemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * time:2019/3/2 11:40
 * author:somewereb
 * description:
 */
public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn_bindservice)
    Button btnBindservice;
    private MyServiceConnection myServiceConnection;
    private MainActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
//        activity = (MainActivity) getIntent().getSerializableExtra("activity");
    }

    @OnClick({R.id.btn, R.id.btn_bindservice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn://获取正在运行的服务
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
                for (ActivityManager.RunningServiceInfo info : runningServices) {
                    ComponentName componentName = info.service;
                    String className = componentName.getClassName();
                    Log.e("hdy", className);
                    Toast.makeText(this, className, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bindservice:
                Intent bindService = new Intent(getApplicationContext(), MyService2.class);
                myServiceConnection = new MyServiceConnection();
                bindService(bindService, myServiceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.btnfinish:
                MainActivity.mContext.finish();
                break;
        }

    }


    class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(SecondActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        if (null != myServiceConnection) unbindService(myServiceConnection);
        super.onDestroy();
    }
}
