package com.example.myapplication.controller;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 作者：Jiancheng,Song on 2016/5/28 15:18
 * 邮箱：m68013@qq.com
 */
public class TcnShareUseData {
    private static TcnShareUseData m_Instance = null;
    public Context m_context = null;


    public static synchronized TcnShareUseData getInstance() {
        if (null == m_Instance) {
            m_Instance = new TcnShareUseData();
        }
        return m_Instance;
    }

    public void init(Context context) {
        m_context = context;
    }


    /**
     *@desc 获取掉货检测开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isDropSensorCheck() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        boolean bCheck = sp.getBoolean("DropSensor", true);
        return bCheck;
    }


    /**
     *@desc 获取掉货检测开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setDropSensorCheck(boolean bDropSensor) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("DropSensor", bDropSensor);
            editor.commit();
        }
    }

}
