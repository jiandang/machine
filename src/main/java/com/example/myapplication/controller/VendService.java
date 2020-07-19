package com.example.myapplication.controller;


import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;


import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 16:25
 * 邮箱：m68013@qq.com
 */
public class VendService extends Service {
    private static final String TAG = "VendService";


    @Override
    public void onCreate() {
        super.onCreate();
        ////捕捉异常，并将具体异常信息写入日志中
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                //任意一个线程异常后统一的处理
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);
                ex.printStackTrace(writer); // 打印到输出流
                String exception =stringWriter.toString();
            }
        });
        TcnVendIF.getInstance().startWorkThread();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TcnVendIF.getInstance().stopWorkThread();
    }
}
