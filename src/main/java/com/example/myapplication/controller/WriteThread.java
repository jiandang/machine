package com.example.myapplication.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.SerialPortController;

/**
 * Created by Administrator on 2017/9/6.
 */
public class WriteThread  extends Thread {

    private final static String TAG = "WriteThread";

    public static final int SERIAL_PORT_TYPE_1        = 1;
    public static final int SERIAL_PORT_TYPE_2        = 2;
    public static final int SERIAL_PORT_TYPE_3        = 3;
    public static final int SERIAL_PORT_TYPE_4        = 4;

    private static final long CMD_OVERTIME_VALUE        = 600;

    private volatile boolean m_bIsRun = true;
    private volatile boolean m_bIsBusy = false;
    private volatile boolean m_bWriting = false;
    private volatile boolean m_bReSend = false;
    private volatile boolean m_bHasNewDada = false;
    private volatile boolean m_bNotShowLog = false;

    private volatile int m_iReSendCount = 0;
    private volatile long m_lOverTime = CMD_OVERTIME_VALUE;

    private volatile MsgToWrite m_curretnMsg = null;
    private CopyOnWriteArrayList<MsgToWrite> m_sendMsgList = new CopyOnWriteArrayList<MsgToWrite>();

    private Handler m_SendHandler = null;



    public WriteThread( )
    {
        setName("WriteThread");
    }

    public void setSendHandler(Handler handler) {
        m_SendHandler = handler;
    }

    public void setReSendCount(int count) {
        m_iReSendCount = count;
    }

    public void setCmdOverTime(long timeMillis) {
        m_lOverTime = timeMillis;
    }

    public void setNotShowLog(boolean notShowLog) {
        m_bNotShowLog = notShowLog;
    }

    public boolean isBusy() {
        return m_bIsBusy;
    }

    public void setBusy(boolean busy) {
        if (!m_bNotShowLog) {
        }
        m_bIsBusy = busy;
        if (!busy) {
            m_bWriting = false;
        }
    }

    public void setBusyAndReSend(boolean busy,boolean reSend) {
        m_bIsBusy = busy;
        m_bReSend = reSend;
        if (!busy) {
            m_bWriting = false;
        }
    }

    public boolean isOvertime(MsgToWrite msg) {

        boolean bRet = false;
        if (null == msg) {
            return bRet;
        }

        long subTime = System.currentTimeMillis() - msg.getCmdTime();

        if (subTime > 5000) {
            bRet = true;
        }
        return bRet;
    }

    public void startWriteThreads()
    {
        this.m_bIsRun = true;
        synchronized (this)
        {
            notify();
        }

        start();
    }

    public void stopWriteThreads()
    {
        this.m_bIsRun = false;
        synchronized (this)
        {
            notify();
        }
    }

    private void addMsgToSendList(MsgToWrite msg)
    {
        m_bIsBusy = true;
        synchronized (m_sendMsgList) {
            msg.setCmdTime(System.currentTimeMillis());
            if (!m_bNotShowLog) {
            }
            this.m_sendMsgList.add(msg);
        }

        synchronized (this)
        {
            if (!m_bNotShowLog) {
            }
            m_bHasNewDada = true;
            notify();
        }
    }

    // 使用socket发送消息
    private boolean writeData(int serialPortType, byte[] data)
    {

        boolean bRet = false;

        if (null == data)
        {
            return bRet;
        }

        try
        {
            if (!m_bNotShowLog) {
                Log.i(TAG, "writeData() data: "+TcnUtility.bytesToHexString(data)+" serialPortType: "+serialPortType);
            }

            m_bWriting = true;
            if (SERIAL_PORT_TYPE_1 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediately(data);
            }
            bRet = true;
        } catch (Exception e)
        {
            m_bWriting = false;
            m_bIsBusy = false;
        }

        return bRet;
    }

    // 发送消息
    private boolean writeData(int serialPortType, String data)
    {

        boolean bRet = false;

        if (null == data)
        {
            return bRet;
        }

        try
        {
            if (!m_bNotShowLog) {
                Log.i(TAG, "writeData() data: "+data+" serialPortType: "+serialPortType);
            }
            m_bWriting = true;
            if (SERIAL_PORT_TYPE_1 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediately(data.getBytes());
            }
            else {

            }
            bRet = true;
        } catch (Exception e)
        {
            m_bWriting = false;
            m_bIsBusy = false;
        }

        return bRet;
    }

    public void sendMsg(int serialPortType, int cmdType, long cmdOverTimeSpan, byte[] data)
    {
        if (data != null) {
            if (!m_bNotShowLog) {
            }
            MsgToWrite msg = new MsgToWrite(serialPortType,cmdType,-1, cmdOverTimeSpan, data);
            addMsgToSendList(msg);
        }
    }

    public void sendMsg(int serialPortType, int cmdType, int num, long cmdOverTimeSpan, byte[] data)
    {
        if (data != null) {
            if (!m_bNotShowLog) {
            }
            MsgToWrite msg = new MsgToWrite(serialPortType,cmdType,num,cmdOverTimeSpan, data);
            addMsgToSendList(msg);
        }
    }

    public void sendMsg(int serialPortType, int cmdType,long cmdOverTimeSpan, String data)
    {
        if (data != null) {
            MsgToWrite msg = new MsgToWrite(serialPortType,cmdType,-1,cmdOverTimeSpan, data.getBytes());
            addMsgToSendList(msg);
        }
    }

    private void sleepTime(long time) {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private CopyOnWriteArrayList<MsgToWrite> getNewMessageList() {
        CopyOnWriteArrayList<MsgToWrite> msgToWriteList = new CopyOnWriteArrayList<MsgToWrite>();
        if (null == m_sendMsgList) {
            return msgToWriteList;
        }
        for (MsgToWrite info:m_sendMsgList) {
            msgToWriteList.add(info);
        }
        return msgToWriteList;
    }

    private boolean isCmdReSendOverTime(long timeMillis) {
        boolean bRet = false;
        if (Math.abs(System.currentTimeMillis() - timeMillis) > 1000) {
            bRet = true;
        }
        return bRet;
    }

    private boolean isCmdReciveOverTime(long timeMillis,long overTimeSpan) {
        boolean bRet = false;
        if (Math.abs(System.currentTimeMillis() - timeMillis) > overTimeSpan) {
            bRet = true;
        }
        return bRet;
    }

    private void sendData(boolean removeOld,int what, int arg1, int arg2, Object data) {
        if (null == m_SendHandler) {
            return;
        }
        if (removeOld) {
            m_SendHandler.removeMessages(what);
        }
        Message message = m_SendHandler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = data;
        m_SendHandler.sendMessage(message);
    }

    @Override
    public void run() {
        super.run();
        while (m_bIsRun) {

            synchronized (m_sendMsgList) {

                while (m_sendMsgList.size() > 0) {
                    // 发送消息
                    CopyOnWriteArrayList<MsgToWrite> msgToWriteList = getNewMessageList();
                    if (!m_bNotShowLog) {

                    }

                    for (MsgToWrite msg : msgToWriteList) {
                        if (!m_bNotShowLog) {
//
                        }
                        if (!m_bWriting) {
                            long lReSendTime = System.currentTimeMillis();
                            while (m_bReSend) {
                                if (!m_bWriting) {
                                    if (m_curretnMsg != null) {
                                        writeData(m_curretnMsg.getSerialType(),m_curretnMsg.getData());
                                        sleepTime(20);
                                    } else {
                                        m_bReSend = false;
                                        sleepTime(20);
                                    }
                                }
                                long lTime = System.currentTimeMillis();
                                while (m_bWriting) {
                                    sleepTime(20);
                                    if (isCmdReciveOverTime(lTime,m_curretnMsg.getOverTimeSpan())) {
                                        if (!m_bNotShowLog) {

                                        }
                                        for (int i = 0; i < m_iReSendCount; i++) {
                                            sleepTime(20);
                                            if (m_bWriting) {
                                                if (!isOvertime(m_curretnMsg)) {
                                                    writeData(m_curretnMsg.getSerialType(),m_curretnMsg.getData());
                                                    sleepTime(300);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        if (!m_bNotShowLog) {

                                        }
                                        if (m_bWriting) {
                                            m_bWriting = false;
                                            m_bIsBusy = false;
                                        }
                                    }
                                }
                                if (!m_bNotShowLog) {

                                }
                                if (isCmdReSendOverTime(lReSendTime)) {
                                    m_bReSend = false;
                                }

                            }
                            if (!m_bNotShowLog) {

                            }

                            if (!isOvertime(msg)) {
                                m_curretnMsg = msg;
                                writeData(msg.getSerialType(),msg.getData());
                            } else {
                                m_curretnMsg = null;
                            }
                            m_sendMsgList.remove(msg);
                        }
                        if (!m_bNotShowLog) {

                        }

                        long lTime = System.currentTimeMillis();
                        while (m_bWriting) {
                            sleepTime(20);
                            if (!m_bNotShowLog) {

                            }

                            if (isCmdReciveOverTime(lTime,msg.getOverTimeSpan())) {
                                if (!m_bNotShowLog) {

                                }

                                for (int i = 0; i < m_iReSendCount; i++) {
                                    sleepTime(50);
                                    if (m_bWriting) {
                                        if (!isOvertime(msg)) {
                                            writeData(msg.getSerialType(),msg.getData());
                                            sleepTime(300);
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (!m_bNotShowLog) {

                                }

                                if (m_bWriting) {
                                    m_bWriting = false;
                                    m_bIsBusy = false;
                                }
                            }
                            if (!m_bNotShowLog) {

                            }

                        }
                        if (!m_bNotShowLog) {

                        }

                    }
                    if (!m_bNotShowLog) {

                    }

                }
                m_bHasNewDada = false;
            }
            if (!m_bNotShowLog) {

            }

            synchronized (this)
            {
                try
                {
                    if (!m_bHasNewDada) {
                        if (!m_bNotShowLog) {
                        }
                        wait();
                    }
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }// 发送完消息后，线程进入等待状态
            }
        }
    }
}
