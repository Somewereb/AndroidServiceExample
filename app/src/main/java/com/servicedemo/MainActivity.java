package com.servicedemo;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
 * time:2019/3/1 23:54
 * author:somewereb
 * description:
 */
public class MainActivity extends AppCompatActivity implements Serializable {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_bind)
    Button btnBind;
    @BindView(R.id.btn_unbind)
    Button btnUnbind;
    @BindView(R.id.btn_intent)
    Button btnIntent;
    private Intent startIntent;
    private Intent bindIntent;
    private MainActivity.myServiceCon myServiceCon;
    public static MainActivity mContext;
    private JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.btn_start, R.id.btn_bind, R.id.btn_unbind,
            R.id.btn_intent, R.id.btn_stop, R.id.btn_go
            , R.id.btn_bind2, R.id.btn_job,R.id.btn_stopjob})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startIntent = new Intent(MainActivity.this, MyService.class);
                startIntent.putExtra("data", "启动服务传过来的数据");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(startIntent);
                } else {
                    startService(startIntent);
                }
//todo     java.lang.IllegalArgumentException: Service Intent must be explicit: Intent { act=MyService.ACTION }
//                Intent otherIntent = new Intent();
//                otherIntent.setAction("MyService.ACTION");
//                startService(otherIntent);
                break;
            case R.id.btn_stop:
                stopService(startIntent);
                break;
            case R.id.btn_bind:
                bindIntent = new Intent(MainActivity.this, MyService2.class);
                bindIntent.putExtra("data", "绑定服务传过来的数据");
                myServiceCon = new myServiceCon();
                bindService(bindIntent, myServiceCon, BIND_AUTO_CREATE);
                break;
            case R.id.btn_bind2:
                Intent bindIntent2 = new Intent(MainActivity.this, MyService2.class);
                startService(bindIntent2);
                break;
            case R.id.btn_unbind:
                unbindService(myServiceCon);
                break;
            case R.id.btn_go:
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("activity", this);
                startActivity(intent);
//                finish();
                break;
            case R.id.btn_intent:
                Intent intentA = new Intent(MainActivity.this, MyIntentService.class);
                intentA.putExtra("data", "INTENT服务传过来的数据");
                startService(intentA);
                break;
            case R.id.btn_job://开启jobScheduler
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    JobInfo jobInfo = new JobInfo.Builder(123456, new ComponentName(getPackageName(), MyJobService.class.getName()))
                            .setMinimumLatency(1000)//任务最少延迟
                            .setOverrideDeadline(5000)//任务deadline，当到期没达到指定条件也会开始执行
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)// 网络条件，默认值NETWORK_TYPE_NONE
//                            .setPeriodic(100)
                            .setRequiresCharging(true)//是否充电
                            .setRequiresDeviceIdle(false)//是否闲置
                            //initialBackoffMillis自定义的时间如果小于10秒，将默认设置成为10秒  如果任务设置了重启需要注意下这个方法setBackoffCriteria
                            .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
                            .build();
//                    jobInfo.getNetworkType();
                    JobInfo jobInfo1 = new JobInfo.Builder(1 << 10,
                            new ComponentName(getPackageName(), MyJobService.class.getName()))
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                            .setOverrideDeadline(5000)
                            //initialBackoffMillis自定义的时间如果小于10秒，将默认设置成为10秒
                            .setBackoffCriteria(100,JobInfo.BACKOFF_POLICY_LINEAR)//设置成线性的
                            .build();
                    jobScheduler.schedule(jobInfo1);
                }
                break;
            case R.id.btn_stopjob://停止jobScheduler
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) jobScheduler.cancelAll();
                break;
        }
    }


    public class myServiceCon implements ServiceConnection, Serializable {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService2.MyBinder binder = (MyService2.MyBinder) service;
            MyService2 myService = binder.getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        if (myServiceCon != null)
            unbindService(myServiceCon);
        super.onDestroy();
    }
}
