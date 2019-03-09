package com.servicedemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

/**
 * time:2019/3/2 22:19
 * author:somewereb
 * description:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private static final String TAG = MyJobService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    //此方法不会执行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 运行于UI线程
     *
     * @return True if your service needs to process the work (on a separate thread). False if
     * there's no more work to be done for this job.
     * true    情况1：任务会保持执行状态，即使任务已经完成，需要手动调用jobFinished通知系统任务完成，才会执行 onStopJob》onDestroy
     *         情况2：就是任务的条件已经不满足了，例如本来要求联网的突然断开就会达到类似情况1手动调用jobFinished效果结束任务
     * false  任务处理完毕,直接调用onDestroy，不执行onStopJob
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob:"+"    currentThread："+Thread.currentThread().getName());
        Toast.makeText(this, "onStartJob", Toast.LENGTH_SHORT).show();
        jobFinished(params,true);//返回true去再次启动任务，前提条件是onStartJob返回true，否则设置无效。我将它理解为手动调用停止任务
        return true;
    }

    /**
     * 执行条件： 1.需要onStart返回true   2.未调用jobFinished方法     缺一不可
     * 一旦执行系统将释放这个job持有的资源
     * @return True to indicate to the JobManager whether you'd like to reschedule this job based
     * on the retry criteria provided at job creation-time. False to drop the job. Regardless of
     * the value returned, your job must stop executing
     * true 是否从新执行任务   false:停止任务
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(this, "onStopJob", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onStopJob");
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
