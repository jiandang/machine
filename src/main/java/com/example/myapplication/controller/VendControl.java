package com.example.myapplication.controller;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.example.myapp.R;

import java.util.concurrent.CopyOnWriteArrayList;

;


/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/20 20:30
 */
public class VendControl extends HandlerThread {
    private static final String TAG = "VendControl";

    private static TCNCommunicationHandler m_cmunicatHandler = null;


    private Context m_context = null;



    public VendControl(Context context, String name) {
        super(name);
        m_context = context;
    }

    public VendControl(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        initialize();
        super.onLooperPrepared();
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public boolean quit() {
        deInitialize();
        return super.quit();
    }

    private void initialize() {
        m_cmunicatHandler = new TCNCommunicationHandler();
        
        VendProtoControl.getInstance().initialize(m_cmunicatHandler);
    }

    public void deInitialize() {
        if (m_cmunicatHandler != null) {
            m_cmunicatHandler.removeCallbacksAndMessages(null);
            m_cmunicatHandler = null;
        }

    }

    public void reqTestSlotNo(int start, int end) {
        TcnUtility.sendMsg(m_cmunicatHandler, TcnVendCMDDef.WRITE_DATA_SHIP_TEST, start, end, null);
    }

    private void reqShipTest(int slotNo, CopyOnWriteArrayList<Integer> slotNoList) {
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_TEST_SLOT, slotNo, -1, slotNoList);
    }

    public void reqQuerySlotStatus(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_QUERY_SLOT_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_QUERY_SLOT_STATUS, slotNo, -1, null);
    }

    public void reqSelfCheck() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SELF_CHECK);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SELF_CHECK, -1, -1, null);
    }

    public void reqReset() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_RESET);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_RESET, -1, -1, null);
    }

    public void reqSetSpringSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SPRING);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SPRING, slotNo, -1, null);
    }

    public void reqSetBeltsSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_BELTS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_BELTS, slotNo, -1, null);
    }


    public void reqSpringAllSlot() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING, -1, -1, null);
    }

    public void reqBeltsAllSlot() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT, -1, -1, null);
    }

    public void reqSingleSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SINGLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SINGLE, slotNo, -1, null);
    }

    public void reqDoubleSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_DOUBLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_DOUBLE, slotNo, -1, null);
    }

    public void reqSingleAllSlot() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE, -1, -1, null);
    }

    public void reqTestMode() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_TEST_MODE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_TEST_MODE, -1, -1, null);
    }

    private void OnShipWithMethod(int shipMethod, int slotNo, int shipStatus, int errCode) {
        Log.i(TAG, "OnShip slotNo: "+slotNo+" shipStatus: "+shipStatus+" errCode: "+errCode);

        if (TcnProtoResultDef.SHIP_SHIPING == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPPING, slotNo, -1, -1, null);
        } else if (TcnProtoResultDef.SHIP_SUCCESS == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPMENT_SUCCESS, slotNo, -1, -1, m_context.getString(R.string.notify_shipsuc_rec_notify));
        } else {    //出货失败
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPMENT_FAILURE, slotNo, -1, -1, m_context.getString(R.string.notify_fail_contact));
        }

    }


    private void OnShipForTestSlot(int slotNo,int errCode, int shipStatus) {
        if (TcnProtoResultDef.SHIP_SHIPING == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_SHIPING, null);
        } else if (TcnProtoResultDef.SHIP_SUCCESS == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_SUCCESS, null);
        } else if (TcnProtoResultDef.SHIP_FAIL == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_FAIL, null);
        } else {

        }
    }

    private void OnTestSlotNo(int start, int end) {
        if (start < 1) {
            return;
        }
        if ((start == end) || (end < 1)) {
            reqShipTest(start,null);
            return;
        }
        CopyOnWriteArrayList<Integer> slotNoList = new CopyOnWriteArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            slotNoList.add(i);
        }
        reqShipTest(start,slotNoList);
    }

    //升降机故障代码
    private String getErrCodeMessageSpring(boolean isSet,int errCode) {
        Log.i(TAG, "getErrCodeMessageSpring() errCode: "+errCode);
        StringBuffer errMsg = new StringBuffer();
        if (errCode == VendProtoControl.ERROR_SPRING_CODE_0) {
            if (isSet) {
                errMsg.append(m_context.getString(R.string.drive_success));
            } else {
                errMsg.append(m_context.getString(R.string.drive_errcode_normal));
            }
            return errMsg.toString();
        }
        errMsg.append(m_context.getString(R.string.drive_errcode));
        errMsg.append(errCode);
        errMsg.append(" ");
        if (errCode == VendProtoControl.ERROR_SPRING_CODE_1) {
            errMsg.append(m_context.getString(R.string.spring_errcode_1));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_2) {
            errMsg.append(m_context.getString(R.string.spring_errcode_2));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_3) {
            errMsg.append(m_context.getString(R.string.spring_errcode_3));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_4) {
            errMsg.append(m_context.getString(R.string.spring_errcode_4));
        } else if (errCode == VendProtoControl.ERROR_CODE_16) {
            errMsg.append(m_context.getString(R.string.spring_errcode_16));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_17) {
            errMsg.append(m_context.getString(R.string.spring_errcode_17));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_18) {
            errMsg.append(m_context.getString(R.string.spring_errcode_18));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_19) {
            errMsg.append(m_context.getString(R.string.spring_errcode_19));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_32) {
            errMsg.append(m_context.getString(R.string.spring_errcode_32));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_33) {
            errMsg.append(m_context.getString(R.string.spring_errcode_33));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_34) {
            errMsg.append(m_context.getString(R.string.spring_errcode_34));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_35) {
            errMsg.append(m_context.getString(R.string.spring_errcode_35));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_48) {
            errMsg.append(m_context.getString(R.string.spring_errcode_48));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_49) {
            errMsg.append(m_context.getString(R.string.spring_errcode_49));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_50) {
            errMsg.append(m_context.getString(R.string.spring_errcode_50));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_51) {
            errMsg.append(m_context.getString(R.string.spring_errcode_51));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_64) {
            errMsg.append(m_context.getString(R.string.spring_errcode_64));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_65) {
            errMsg.append(m_context.getString(R.string.spring_errcode_65));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_66) {
            errMsg.append(m_context.getString(R.string.spring_errcode_66));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_67) {
            errMsg.append(m_context.getString(R.string.spring_errcode_67));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_80) {
            errMsg.append(m_context.getString(R.string.spring_errcode_80));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_81) {
            errMsg.append(m_context.getString(R.string.spring_errcode_81));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_82) {
            errMsg.append(m_context.getString(R.string.spring_errcode_82));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_83) {
            errMsg.append(m_context.getString(R.string.spring_errcode_83));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_84) {
            errMsg.append(m_context.getString(R.string.spring_errcode_84));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_86) {
            errMsg.append(m_context.getString(R.string.spring_errcode_86));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_87) {
            errMsg.append(m_context.getString(R.string.spring_errcode_87));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_90) {
            errMsg.append(m_context.getString(R.string.spring_errcode_90));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_91) {
            errMsg.append(m_context.getString(R.string.spring_errcode_91));
        } else if (errCode == VendProtoControl.ERROR_CODE_255) {
            errMsg.append(m_context.getString(R.string.drive_errcode_255));
        }
        else {

        }
        return errMsg.toString();
    }

    private class TCNCommunicationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TcnProtoDef.SERIAL_PORT_CONFIG_ERROR:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_CONFIG_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.SERIAL_PORT_SECURITY_ERROR:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_SECURITY_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.SERIAL_PORT_UNKNOWN_ERROR:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_WECHATPAY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnVendCMDDef.WRITE_DATA_SHIP_TEST:
                    OnTestSlotNo(msg.arg1,msg.arg2);
                    break;
                case TcnProtoDef.REQ_CMD_TEST_SLOT:
                    VendProtoControl.getInstance().reqShipTest(msg.arg1,(CopyOnWriteArrayList<Integer>)msg.obj);
                    break;
                case TcnProtoDef.CMD_TEST_SLOT:
                    OnShipForTestSlot(msg.arg1,msg.arg2,(Integer)msg.obj);
                    break;
                case TcnProtoDef.REQ_QUERY_SLOT_STATUS:
                    VendProtoControl.getInstance().reqQuerySlotExists(msg.arg1);
                    break;
                case TcnProtoDef.QUERY_SLOT_STATUS:

                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_QUERY_SLOT_STATUS, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(false,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SELF_CHECK:
                    VendProtoControl.getInstance().selfCheck();
                    break;
                case TcnProtoDef.SELF_CHECK:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SELF_CHECK, msg.arg1, -1, -1, getErrCodeMessageSpring(false,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_RESET:
                    VendProtoControl.getInstance().reset();
                    break;
                case TcnProtoDef.CMD_RESET:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_RESET, msg.arg1, -1, -1, getErrCodeMessageSpring(false,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_SPRING:
                    VendProtoControl.getInstance().setSlotSpring(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_SPRING:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_SPRING, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_BELTS:
                    VendProtoControl.getInstance().setSlotBelt(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_BELTS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_BELTS, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING:
                    VendProtoControl.getInstance().setSlotAllSpring(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_SPRING:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_SPRING, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT:
                    VendProtoControl.getInstance().setSlotAllBelt();
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_BELT:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_BELT, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_SINGLE:
                    VendProtoControl.getInstance().setSingleSlotno(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_SINGLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_SINGLE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_DOUBLE:
                    VendProtoControl.getInstance().setDoubleSlotno(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_DOUBLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_DOUBLE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE:
                    VendProtoControl.getInstance().setAllSlotnoSingle();
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_SINGLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_SINGLE, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_TEST_MODE:
                    VendProtoControl.getInstance().setTestMode();
                    break;
                default:
                    break;
            }
        }
    }
}
