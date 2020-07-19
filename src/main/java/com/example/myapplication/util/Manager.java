package com.example.myapplication.util;

import android.os.Handler;
import android.os.Message;

/**
 * 自己实现Application，实现数据共享
 * @author jason
 */
public class Manager {
    public static final String TAG = "Manager";

    private static Manager mInstance;

    public Handler mHandler;

    public synchronized static Manager getInstance() {
        if (mInstance == null) {
            mInstance = new Manager();
        }
        return mInstance;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
    public void sendMessage(String read){
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.obj = read;
        mHandler.sendMessage(msg);
    }

}
