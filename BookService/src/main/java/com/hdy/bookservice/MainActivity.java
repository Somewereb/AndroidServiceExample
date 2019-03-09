package com.hdy.bookservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn, mRunBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.btn_start);
        startBtn.setOnClickListener(this);
        mRunBtn = findViewById(R.id.btn_run);
        mRunBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(MainActivity.this, AIDLService.class);
                startService(intent);
                break;
            case R.id.btn_run://获取正在运行的服务
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
                for (ActivityManager.RunningServiceInfo info : runningServices) {
                    ComponentName componentName = info.service;
                    String className = componentName.getClassName();
                    Log.e("hdy", className);
                    Toast.makeText(this, className, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
