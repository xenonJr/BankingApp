package com.example.bankingapp.Helpers;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExJobService extends JobService {

    private static final String TAG = "Ex";
    private boolean jobCancelled = false;



    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,"Job Started");
        doBankGroundWork(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG,"Job Canceled before complete");
        jobCancelled = true;
        return false;
    }

    void doBankGroundWork(JobParameters parameters){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.d(TAG,"run "+ i);
                    if(jobCancelled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Log.d(TAG,"Job Fininshed");
                jobFinished(parameters,false);
            }
        }).start();
    }

}
