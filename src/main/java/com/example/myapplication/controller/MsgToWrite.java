package com.example.myapplication.controller;

/**
 * Created by Administrator on 2017/9/6.
 */
public class MsgToWrite {
    private int m_iSerialPortType = -1;
    private int m_iCmdType = -1;
    private int m_iNum = -1;
    private long m_lCmdTime = 0;
    private long m_lCmdOverTimeSpan = 0;

    //要发送的消息
    private byte[] m_msgData;


    public MsgToWrite(int serialPortType,int cmdType,int num,long cmdTime,long cmdOverTimeSpan, byte[] msgData)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdTime = cmdTime;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
    }

    public MsgToWrite(int serialPortType,int cmdType,int num,long cmdOverTimeSpan, byte[] msgData)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
    }

    public byte[] getData()
    {
        return this.m_msgData;
    }

    public int getSerialType()
    {
        return this.m_iSerialPortType;
    }

    public long getCmdTime()
    {
        return this.m_lCmdTime;
    }

    public void setCmdTime(long cmdTime)
    {
        this.m_lCmdTime = cmdTime;
    }

    public int getCmdType()
    {
        return this.m_iCmdType;
    }

    public int getCmdNum()
    {
        return this.m_iNum;
    }

    public long getOverTimeSpan()
    {
        return this.m_lCmdOverTimeSpan;
    }
}
