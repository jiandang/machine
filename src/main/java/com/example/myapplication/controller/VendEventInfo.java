package com.example.myapplication.controller;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/28 16:28
 * 邮箱：m68013@qq.com
 */
public class VendEventInfo {
    public int  	m_iEventID;
    public int 	m_lParam1;
    public int 	m_lParam2;
    public long 	m_lParam3;
    public String 	m_lParam4;

    public void SetEventID(int iEventID){m_iEventID = iEventID;};
    public void SetlParam1(int lParam1){m_lParam1 = lParam1;};
    public void SetlParam2(int lParam2){m_lParam2 = lParam2;};
    public void SetlParam3(long lParam3){m_lParam3 = lParam3;};
    public void SetlParam4(String lParam4){m_lParam4 = lParam4;};

    public int  GetEventID(){return m_iEventID;};
    public int  GetlParam1(){return m_lParam1;};
    public int  GetlParam2(){return m_lParam2;};
    public long GetlParam3(){return m_lParam3;};
    public String GetlParam4(){return m_lParam4;};
}
