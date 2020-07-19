package com.example.myapplication;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * Created by ASXY_home on 2018-10-18.
 */

public class SerialHelper {

    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;
    private int iBaudRate;
    private String sPort;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private SerialHelper.ReadThread mReadThread;
    private SerialHelper.SendThread mSendThread;

    public SerialHelper(String sPort, int iBaudRate) {

        this.sPort = "/dev/ttyS3";

        this.iBaudRate = 9600;
        this._isOpen = false;
        this._bLoopData = new byte[]{48};
        this.iDelay = 500;
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }
    //打开时打开监听线程
    public void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(this.sPort), this.iBaudRate, 0);
        mOutputStream = this.mSerialPort.getOutputStream();
        mInputStream = this.mSerialPort.getInputStream();
        mReadThread = new SerialHelper.ReadThread();
        this.mReadThread.start();
        mSendThread = new SerialHelper.SendThread();
        this.mSendThread.setSuspendFlag();
        this.mSendThread.start();
        this._isOpen = true;
    }

    // 关闭线程 释放函数
    public void close() {
        if (this.mReadThread != null) {
            this.mReadThread.interrupt();
        }
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
        this._isOpen = false;
    }
    private class SendThread extends Thread {
        public boolean suspendFlag;
        private SendThread() {
            this.suspendFlag = true;
        }
        public void run() {
            super.run();
            while(!this.isInterrupted()) {
                synchronized(this) {
                    while(this.suspendFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException var5) {
                            var5.printStackTrace();
                        }
                    }
                }
//                SerialHelper.this.send(SerialHelper.this.getbLoopData());
                try {
                    Thread.sleep((long)SerialHelper.this.iDelay);
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }
        }
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }
        public synchronized void setResume() {
            this.suspendFlag = false;
            this.notify();
        }
    }
    private class ReadThread extends Thread {
        private ReadThread() {
        }
        public void run() {
            super.run();
            while(!this.isInterrupted()) {
                try {
                    if (SerialHelper.this.mInputStream == null) {
                        return;
                    }
                    byte[] buffer = new byte[512];
                    int size = SerialHelper.this.mInputStream.read(buffer);
                    if (size > 0) {
//                        ComBean ComRecData = new ComBean(SerialHelper.this.sPort, buffer, size);
//                        SerialHelper.this.onDataReceived(ComRecData);
                    }
                } catch (Throwable var4) {
                    Log.e("error", var4.getMessage());
                    return;
                }
            }
        }
    }
}
