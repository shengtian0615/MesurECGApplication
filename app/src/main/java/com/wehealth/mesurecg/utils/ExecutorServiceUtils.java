package com.wehealth.mesurecg.utils;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtils {

    public static ExecutorServiceUtils instance;
    private ExecutorService executorService;

    public ExecutorServiceUtils(){
        executorService = Executors.newFixedThreadPool(5);
    }

    public static  ExecutorServiceUtils getInstance(){
        if (instance==null){
            instance = new ExecutorServiceUtils();
        }
        return instance;
    }

    public void starTask(int count){
        executorService.submit(new Task(count));
    }

    public class Task implements Runnable {

        private int count;
        public Task(int c) {
            count = c;
        }

        @Override
        public void run() {

            Log.e(ExecutorServiceUtils.this.getClass().getName(), "+==========++++++++++++++============="+count);

                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.e(ExecutorServiceUtils.this.getClass().getName(), Thread.currentThread().getName());


        }
    }
}
