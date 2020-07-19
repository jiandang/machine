package com.com.tcn.sdk.springdemo.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:30
 * 邮箱：m68013@qq.com
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String action_boot="android.intent.action.BOOT_COMPLETED1";
    private Context m_context = null;
    private Intent m_intent_Service;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == context) {
            return;
        }
        m_context = context;
        if(action_boot.equals(intent.getAction())){

            //启动服务与主板进行通讯
            m_intent_Service = new Intent(m_context, VendService.class);
            m_context.startService(m_intent_Service);

        }
    }
}
