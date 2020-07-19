package com.example.myapplication.controller;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import android_serialport_api.SerialPortController;
import android_serialport_api.SerialPortFinder;


/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/28 16:36
 * 邮箱：m68013@qq.com
 */
public class TcnVendIF {
    private static final String TAG = "TcnVendIF";

    private volatile int m_iEventIDTemp = -1;
    private volatile int m_ilParam1 = -1;

    private static VendControl m_VendControl = null;
    private Context m_context = null;




    private static class SingletonHolder {
        private static TcnVendIF instance = new TcnVendIF();
        private SingletonHolder() {
            //do nothing
        }

    }

    public static TcnVendIF getInstance() {
        return SingletonHolder.instance;
    }

    public void init(Context context) {
        if (null == context) {
            return;
        }
        m_context = context;
        TcnShareUseData.getInstance().init(context);
        SerialPortController.getInstance().init(context);
    }

    public void startWorkThread() {
        if (null != m_VendControl) {
            m_VendControl.quit();
            m_VendControl = null;
        }
        m_VendControl = new VendControl(m_context,"VendControl");
        m_VendControl.start();
    }

    public void stopWorkThread() {

        if (m_VendControl != null) {
            m_VendControl.quit();
            m_VendControl = null;
        }
    }

    public boolean isServiceRunning() {
        if (null == m_context) {
            return false;
        }
        ActivityManager manager = (ActivityManager) m_context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(1000)) {
            if (("com.tcn.controller.VendService").equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setGroupNumber(int grp) {
        VendProtoControl.getInstance().setGroupNumber(grp);
    }

    /**
     * 判断是否是含小数
     * @param data
     * @return
     */
    public boolean isContainDeciPoint(String data) {
        if (null == data) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$");
        return pattern.matcher(data).matches();
    }

    /**
     * 判断是否全部由数字组成
     * @param data
     * @return
     */
    public boolean isDigital(String data) {
        if ((null == data) || (data.length() < 1)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(data).matches();
    }


    /************************** common end *******************************************/



    /************************** vend start *******************************************/


    public void reqSlotNoInfoOpenSerialPort() {
        VendProtoControl.getInstance().reqSlotNoInfoOpenSerialPort();

    }

    public SerialPortFinder getSerialPortFinder() {
        return SerialPortController.getInstance().getSerialPortFinder();
    }

    public void reqSlotNoInfo() {
        VendProtoControl.getInstance().reqSlotNoInfo();
    }

    public void reqQuerySlotStatus(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqQuerySlotStatus(slotNo);
    }

    public void reqSelfCheck() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSelfCheck();
    }

    public void reqReset() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqReset();
    }

    public void reqSetSpringSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSetSpringSlot(slotNo);
    }

    public void reqSetBeltsSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSetBeltsSlot(slotNo);
    }

    public void reqSpringAllSlot() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSpringAllSlot();
    }

    public void reqBeltsAllSlot() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqBeltsAllSlot();
    }

    public void reqSingleSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSingleSlot(slotNo);
    }

    public void reqDoubleSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqDoubleSlot(slotNo);
    }

    public void reqSingleAllSlot() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSingleAllSlot();
    }

    public void reqTestMode(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTestMode();
    }

    public void reqWriteDataShipTest(int start, int end) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTestSlotNo(start, end);
    }

    /************************** vend end *******************************************/

    private final Handler m_cEventHandlerForUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            handleMessageToUI(msg.what,(Bundle)msg.obj);
        }
    };

    public void handleMessageToUI(int what, Bundle bundle) {
        Bundle msgBundle = bundle;

        int iEventID = msgBundle.getInt("eID");
        int lParam1 = msgBundle.getInt("lP1");
        int lParam2 = msgBundle.getInt("lP2");
        long lParam3 = msgBundle.getLong("lP3");
        String lParam4 = msgBundle.getString("lP4");
        notifyUI(iEventID, lParam1, lParam2, lParam3, lParam4);
    }

    public void sendMsgToUI(int iEventID, int lParam1, int lParam2, long lParam3, String lParam4) {

        if ((m_iEventIDTemp == iEventID) && (m_ilParam1 == lParam1)) {
            TcnUtility.removeMessages(m_cEventHandlerForUI, iEventID);
        }

        m_iEventIDTemp = iEventID;
        m_ilParam1 = lParam1;

        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", lParam1);
        msgBundle.putInt("lP2", lParam2);
        msgBundle.putLong("lP3", lParam3);
        msgBundle.putString("lP4", lParam4);
        TcnUtility.sendMsg(m_cEventHandlerForUI, iEventID, -1, -1, msgBundle);
    }

    public void sendMsgToUIDelay(int iEventID,long delayMillis) {
        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", -1);
        msgBundle.putInt("lP2", -1);
        msgBundle.putLong("lP3", -1);
        msgBundle.putString("lP4", null);
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
        TcnUtility.sendMsgDelayed(m_cEventHandlerForUI, iEventID, -1, delayMillis, msgBundle);
    }

    public void sendMsgToUIDelay(int iEventID,int lParam1, int lParam2,long lParam3,long delayMillis, String lParam4) {
        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", lParam1);
        msgBundle.putInt("lP2", lParam2);
        msgBundle.putLong("lP3", lParam3);
        msgBundle.putString("lP4", lParam4);
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
        TcnUtility.sendMsgDelayed(m_cEventHandlerForUI, iEventID, -1, delayMillis, msgBundle);
    }

    public void removeMsgToUI(int iEventID) {
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
    }


    // VendEventListener interface
    public interface VendEventListener {
        public void VendEvent(VendEventInfo cEventInfo);
    }

    private final CopyOnWriteArrayList<VendEventListener> m_Callbacks = new CopyOnWriteArrayList<VendEventListener>();

    public void registerListener (VendEventListener callback) {
        synchronized (m_Callbacks) {
            if (null == callback) {
                return;
            }

            if (!(m_Callbacks.contains(callback))) {
                m_Callbacks.add(callback);
            }
        }
    }

    public void unregisterListener (VendEventListener callback) {
        synchronized (m_Callbacks) {
            if (null == callback) {
                return;
            }
            if (m_Callbacks.contains(callback)) {
                m_Callbacks.remove(callback);
            }

        }
    }

    private void sendNotifyToUI(VendEventInfo cEventInfo) {
        synchronized (m_Callbacks) {
            for (VendEventListener c : m_Callbacks) {
                c.VendEvent(cEventInfo);
            }
        }
    }

    private void notifyUI(int iEventID, int lParam1, int lParam2, long lParam3, String lParam4) {
        VendEventInfo cEventInfo = new VendEventInfo();

        cEventInfo.SetEventID(iEventID);
        cEventInfo.SetlParam1(lParam1);
        cEventInfo.SetlParam2(lParam2);
        cEventInfo.SetlParam3(lParam3);
        cEventInfo.SetlParam4(lParam4);

        sendNotifyToUI(cEventInfo);
    }

}
