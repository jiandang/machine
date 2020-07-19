package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:30
 * 邮箱：m68013@qq.com
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String action_boot="android.intent.action.BOOT_COMPLETED";
    private Context m_context = null;
    private Intent m_intent_Service;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == context) {
            return;
        }
        m_context = context;
        if(action_boot.equals(intent.getAction())){
            //开机自启
//            Intent i = new Intent(context, InitActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        }
    }
}
