package com.com.tcn.sdk.springdemo.controller;


import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.utils.Logger;
import com.tcn.springboard.control.TcnShareUseData;
import com.tcn.springboard.control.TcnVendApplication;

import org.xutils.x;

import android_serialport_api.sample.SerialPortFinder;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:53
 * 邮箱：m68013@qq.com
 */
public class VendApplication extends TcnVendApplication {
    private static VendApplication instance;
    private SharedPreferences spf;
    public static VendApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        instance = this;
        Logger.init(this);
        String first = TcnShareUseData.getInstance().getBoardSerPortFirst();
        Log.d("TAG", "onViewClicked: "+first);
//        TcnShareUseData.getInstance().setBoardSerPortFirst(first);    //此处主板串口接安卓哪个串口，就填哪个串口
//        TcnShareUseData.getInstance().setBoardSerPortFirst("/dev/ttymxc1");
        //VendIF 这个文件里面 TcnComDef.COMMAND_SLOTNO_INFO这个消息，是上报货道信息的消息,每次重启程序都会查询一次所有的货道信息

        /****************  如果接有副柜  则需要如下设置  **********************/
//        TcnShareUseData.getInstance().setSerPortGroupMapFirst("0");    //设置主柜组号，也可不设置，默认就是0
//        TcnShareUseData.getInstance().setSerPortGroupMapSecond("0");   //设置副柜组号为0,副柜需要接安卓另外一个串口
        //TcnShareUseData.getInstance().setBoardTypeSecond("thj");   //设置副柜类型为弹簧机
//        TcnShareUseData.getInstance().setBoardSerPortSecond("/dev/ttyS1");    //设置副柜串口，副柜接安卓哪个串口，就填哪个串口

        /****************  如果带现金  则需要如下设置  **********************/
//        TcnShareUseData.getInstance().setCashPayOpen(true);  //设置是否启用现金支付
//        TcnShareUseData.getInstance().setBoardSerPortMDB("/dev/ttyS2");    //此处MDB主板设备接安卓哪个串口，就填哪个串口

        //先运行程序之后，请将TcnKey目录的tcn_sdk_device_id.txt文件发给我们，授权才能使用，每台机器都必须先授权。


        /*******************************      故障代码表见 VendIF  这个文件 **************************************/


    }
    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
//    private SerialPort mSerialPort = null;

//    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
//        if (mSerialPort == null) {
//			/* Read serial port parameters */
//            SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
//            Log.d("TAG", "getSerialPort: "+sp.getAll());
//            String path = sp.getString("DEVICE", "");
//            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
//            Log.d("TAG", "getSerialPort: "+path+"+++++++++"+baudrate);
//			/* Check parameters */
//            if ( (path.length() == 0) || (baudrate == -1)) {
//                throw new InvalidParameterException();
//            }
//
//			/* Open the serial port */
////            mSerialPort = new SerialPort(new File(path), baudrate, 0);
//        }
//        return mSerialPort;
//    }
}
