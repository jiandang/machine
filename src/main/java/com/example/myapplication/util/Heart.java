package com.example.myapplication.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by Think on 2019/1/28.
 */

public class Heart {
    public static final String TAG = "OkHttpUtils";
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    private static final int NETWORKTYPE_WIFI = 0;
    private static final int NETWORKTYPE_4G = 1;
    private static final int NETWORKTYPE_2G = 2;
    private static final int NETWORKTYPE_NONE = 3;
    public TextView mTextView;
    public static TelephonyManager mTelephonyManager;
    public static PhoneStatListener mListener;
    public static int mGsmSignalStrength;
    private static NetWorkBroadCastReciver mNetWorkBroadCastReciver;
    private static Context context;
    //初始化
    public static void get(Context con){
        context = con;
        //获取telephonyManager
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //开始监听
        mListener = new PhoneStatListener();
        /**由于信号值变化不大时，监听反应不灵敏，所以通过广播的方式同时监听wifi和信号改变更灵敏*/
        mNetWorkBroadCastReciver = new NetWorkBroadCastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(mNetWorkBroadCastReciver, intentFilter);
    }
    public static void resume(){
        Log.d(TAG, "resume: ++++");
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
    }
    public static void pause(){
        Log.d(TAG, "pause: +++++");
        //用户不在当前页面时，停止监听
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_NONE);
    }
    public static void destory(){
        Log.d(TAG, "destory: +++++");
        context.unregisterReceiver(mNetWorkBroadCastReciver);
    }
    private static class PhoneStatListener extends PhoneStateListener {
        //获取信号强度
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            //获取网络信号强度
            //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
//            int level = signalStrength.getLevel();
            mGsmSignalStrength = signalStrength.getGsmSignalStrength();
            //网络信号改变时，获取网络信息
            String netWorkInfo = getNetWorkInfo();
            if(netWorkInfo!=null && !netWorkInfo.equals("0")){
                Log.d(TAG, "onSign: ++++"+netWorkInfo);
                Heart.pause();
            }
        }
    }
    /**
     * 判断网络速度
     */
    private static boolean isFastMobileNetwork() {
        if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
            return true;
        }
        return false;
    }
    //接收网络状态改变的广播
    static class NetWorkBroadCastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNetWorkInfo();
        }
    }
    /**
     * 获取网络的信息
     */
    public static String getNetWorkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        String s = null;
        if (info != null && info.isAvailable()) {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    //wifi
                    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = manager.getConnectionInfo();
                    int rssi = connectionInfo.getRssi();
                    Log.d(TAG, "当前为wifi网络，信号强度=" + rssi);
                    s= String.valueOf(rssi);
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    //移动网络,可以通过TelephonyManager来获取具体细化的网络类型
                    String netWorkStatus = isFastMobileNetwork() ? "4G网络" : "2G网络";
                    Log.d(TAG, "当前为" + netWorkStatus + "，信号强度=" + mGsmSignalStrength);
                    s= String.valueOf(mGsmSignalStrength);
                    break;
            }
        } else {
            Log.d(TAG, "没有可用网络");
            s = "没有可用网络";
        }
        return s;
    }
}
