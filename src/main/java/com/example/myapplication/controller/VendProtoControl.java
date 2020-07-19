package com.example.myapplication.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.SerialPortController;

/**
 * Created by Administrator on 2017/6/2.
 */
public class VendProtoControl {
    private static final String TAG = "VendProtoControl";
    private static VendProtoControl m_Instance = null;

    public static final int ERROR_SPRING_CODE_0             = 0;   //正常
    public static final int ERROR_SPRING_CODE_1             = 1;   //光电开关在没有发射的情况下也有信号输出
    public static final int ERROR_SPRING_CODE_2             = 2;    //发射有改变的时候 也没有信号输出
    public static final int ERROR_SPRING_CODE_3             = 3;     //出货时一直有输出信号 不能判断好坏
    public static final int ERROR_SPRING_CODE_4             = 4;    //没有检测到出货
    public static final int ERROR_CODE_16             = 16;   //P型MOS管有短路
    public static final int ERROR_SPRING_CODE_17             = 17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_18             = 18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_19             = 19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_32             = 32;    //N型MOS管有短路;
    public static final int ERROR_SPRING_CODE_33             = 33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_34             = 34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_35             = 35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_48             = 48;    //电机短路;
    public static final int ERROR_SPRING_CODE_49             = 49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_50             = 50;    //电机短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_51             = 51;    //电机短路;出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_64             = 64;    //电机断路;
    public static final int ERROR_SPRING_CODE_65             = 65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_66             = 66;    //电机断路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_67             = 67;    //电机断路;出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_80             = 80;    //RAM出错,电机转动超时。
    public static final int ERROR_SPRING_CODE_81             = 81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
    public static final int ERROR_SPRING_CODE_82             = 82;    //接收到数据不完整。
    public static final int ERROR_SPRING_CODE_83             = 83;    //校验不正确。
    public static final int ERROR_SPRING_CODE_84             = 84;    //地址不正确。
    public static final int ERROR_SPRING_CODE_86             = 86;    //货道不存在。
    public static final int ERROR_SPRING_CODE_87             = 87;    //返回故障代码有错超出范围。
    public static final int ERROR_SPRING_CODE_90             = 90;    //连续多少次转动正常但没检测到商品售出。
    public static final int ERROR_SPRING_CODE_91             = 91;    //其它故障小于。


    public static final int ERROR_CODE_255             = 255;    //货道号不存在

    private volatile boolean m_bShiping = false;
    private volatile String m_strPayMethod = "-1";

    private volatile byte m_bGroupNumber = (byte)0x00;
    private volatile int m_iCurrentSerptGrp = DriveControl.GROUP_SERIPORT_1;

    private volatile Handler m_ReceiveHandler = null;
    private volatile Handler m_SendHandler = null;

    private volatile boolean m_isTestingSlotNo = false;
    private CopyOnWriteArrayList<Integer> m_slotNoTestList = null;


    public static synchronized VendProtoControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new VendProtoControl();
        }
        return m_Instance;
    }

    public void initialize(Handler sendHandler) {
        m_SendHandler = sendHandler;
        m_ReceiveHandler = new CommunicationHandler();
        DriveControl.getInstance().init(m_ReceiveHandler);
        reqSlotNoInfoOpenSerialPort();
    }

    public void setGroupNumber(int grp) {
        m_bGroupNumber = Integer.valueOf(grp).byteValue();
    }

    public void setShiping(boolean shiping) {
        m_bShiping = shiping;
    }
    public boolean isShiping() {
        return m_bShiping;
    }

    public void setSendHandler(Handler handler) {
        m_SendHandler = handler;
    }

    private void openSerialPort() {
        SerialPortController.getInstance().setHandler(m_ReceiveHandler);
        SerialPortController.getInstance().openSerialPort("MAINDEVICE", "MAINBAUDRATE");
    }

    public void reqSlotNoInfo() {
        Log.i(TAG, "reqSlotNoInfo");
        DriveControl.getInstance().sendCmdGetData(false,m_iCurrentSerptGrp,1,m_bGroupNumber);

    }

    public void reqSlotNoInfoOpenSerialPort() {
        openSerialPort();
       // reqSlotNoInfo();
    }

    public void reqShipTest(int slotNo,CopyOnWriteArrayList<Integer> slotNoList) {
        if ((null == slotNoList) || (slotNoList.size() < 1)) {
            if (slotNo > 0) {
                if (m_slotNoTestList != null) {
                    m_slotNoTestList.clear();
                }
                reqWriteDataShipTest(slotNo);
            }
            return;
        }
        reqWriteDataShipTest(slotNoList);
    }

    private void cleanShipTestList() {
        m_isTestingSlotNo = false;
        if (m_slotNoTestList != null) {
            m_slotNoTestList.clear();
        }
    }

    private void reqWriteDataShipTest(int slotNo) {
        m_isTestingSlotNo = true;
        DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),m_iCurrentSerptGrp,slotNo,m_bGroupNumber);
    }

    private void reqWriteDataShipTest(CopyOnWriteArrayList<Integer> slotNoList) {
        if ((null == slotNoList) || (slotNoList.size() < 1)) {
            return;
        }
        m_slotNoTestList = slotNoList;
        m_isTestingSlotNo = true;
        DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),m_iCurrentSerptGrp,slotNoList.get(0),m_bGroupNumber);
    }

    public void reqSelectSlotNo(int slotNo) {
        DriveControl.getInstance().reqSelectSlotNo(m_iCurrentSerptGrp,slotNo,m_bGroupNumber);
    }

    /************************************************ 出货方式 start ************************************************/

    public void ship(int slotNo) {
        m_bShiping = true;
        cleanShipTestList();

        DriveControl.getInstance().ship(TcnShareUseData.getInstance().isDropSensorCheck(),m_iCurrentSerptGrp,slotNo,m_bGroupNumber);
    }

    /************************************************ 出货方式 end ************************************************/

    public void reqQuerySlotExists(int slotNo) {
        Log.i(TAG, "reqQuerySlotExists slotNo: "+slotNo+" m_bGroupNumber: "+m_bGroupNumber);
        DriveControl.getInstance().reqQuerySlotExists(DriveControl.GROUP_SERIPORT_1,slotNo,m_bGroupNumber);
    }

    public void selfCheck() {
        DriveControl.getInstance().selfCheck(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void reset() {
        DriveControl.getInstance().reset(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void setSlotSpring(int slotNo) {
        DriveControl.getInstance().setSlotSpring(m_iCurrentSerptGrp,slotNo,m_bGroupNumber);

    }

    public void setSlotBelt(int slotNo) {
        DriveControl.getInstance().setSlotBelt(m_iCurrentSerptGrp,slotNo,m_bGroupNumber);

    }

    public void setSlotAllSpring(int grpId) {
        if (grpId < 0) {
            setSlotAllSpring();
            return;
        }
        DriveControl.getInstance().setSlotAllSpring(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void setSlotAllSpring() {
        DriveControl.getInstance().setSlotAllSpring(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void setSlotAllBelt() {
        DriveControl.getInstance().setSlotAllBelt(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void setSingleSlotno(int slotNo) {
        DriveControl.getInstance().setSingleSlotno(m_iCurrentSerptGrp,slotNo,m_bGroupNumber);
    }

    public void setDoubleSlotno(int slotNo) {
        DriveControl.getInstance().setDoubleSlotno(m_iCurrentSerptGrp,slotNo,m_bGroupNumber);
    }

    public void setAllSlotnoSingle() {
        DriveControl.getInstance().setAllSlotnoSingle(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public void setTestMode() {
        DriveControl.getInstance().testMode(m_iCurrentSerptGrp,m_bGroupNumber);
    }

    public String getPayMedthod() {
        return m_strPayMethod;
    }

    private void OnAnalyseProtocolData(int bytesCount, int boardType, byte[] bytesData) {
        if ((null == bytesData) || (bytesData.length < 1)) {
            return;
        }
        String hexData = TcnUtility.bytesToHexString(bytesData,bytesCount);
        DriveControl.getInstance().protocolAnalyse(hexData);
    }

    private int getShipStatus(int shipStatus) {
        int iArg2 = -1;
        if (DriveControl.SHIP_STATUS_SHIPING == shipStatus) {
            iArg2 = TcnProtoResultDef.SHIP_SHIPING;
        } else if (DriveControl.SHIP_STATUS_SUCCESS == shipStatus) {
            m_bShiping = false;
            iArg2 = TcnProtoResultDef.SHIP_SUCCESS;
        } else if (DriveControl.SHIP_STATUS_FAIL == shipStatus) {
            m_bShiping = false;
            iArg2 = TcnProtoResultDef.SHIP_FAIL;
        } else {
            m_bShiping = false;
        }

        return iArg2;

    }

    private void shipForTestSlot(int slotNo, int shipStatus,int errCode) {
        Log.i(TAG, "shipForTestSlot() slotNo: " + slotNo + " shipStatus: " + shipStatus + " errCode: " + errCode);
        int iShipStatus = getShipStatus(shipStatus);
        sendReceiveData(TcnProtoDef.CMD_TEST_SLOT,slotNo,errCode,iShipStatus);
    }

    private void shipFail(int slotNo,String payMethod) {
        Log.i(TAG, "shipFail slotNo: "+slotNo+" payMethod: "+payMethod);
        sendReceiveData(TcnProtoDef.COMMAND_SHIPMENT_WECHATPAY,slotNo,TcnProtoResultDef.SHIP_FAIL,-1);
    }

    private void handShipData(int slotNo, int shipStatus, int errCode, String payMethod) {
        Log.i(TAG, "handShipData slotNo: "+slotNo+" shipStatus: "+shipStatus+" errCode: "+errCode+" payMethod: "+payMethod);

        int iShipStatus = getShipStatus(shipStatus);

        sendReceiveData(TcnProtoDef.COMMAND_SHIPMENT_WECHATPAY,slotNo,iShipStatus,errCode);

    }

    private void sendReceiveData(int what, int arg1, int arg2, Object data) {
        if (null == m_SendHandler) {
            return;
        }
        Message message = m_SendHandler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = data;
        m_SendHandler.sendMessage(message);
    }

    private void sendNoDataCmd(int cmdType) {
        sendReceiveData(cmdType,TcnProtoResultDef.CMD_NO_DATA_RECIVE,-1,-1);
    }


    private class CommunicationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SerialPortController.SERIAL_PORT_CONFIG_ERROR:
                    Log.i(TAG, "SERIAL_PORT_CONFIG_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_CONFIG_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_SECURITY_ERROR:
                    Log.i(TAG, "SERIAL_PORT_SECURITY_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_SECURITY_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_UNKNOWN_ERROR:
                    Log.i(TAG, "SERIAL_PORT_UNKNOWN_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_UNKNOWN_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_DATA:
                    OnAnalyseProtocolData(msg.arg1, msg.arg2,(byte[])msg.obj);
                    break;
                case DriveControl.CMD_QUERY_SLOTNO_EXISTS:
                    if (DriveControl.getInstance().isQueryingAllSlotNo()) {
                        sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO,msg.arg1,msg.arg2,false);
                    } else {    //查询完成

                        boolean bIsNotLoadAll = false;  //是否加载
                        if ((Boolean)msg.obj != null) {
                            bIsNotLoadAll = ((Boolean)msg.obj).booleanValue();
                        }
                        if (bIsNotLoadAll) {
                            sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO_SINGLE,msg.arg1,msg.arg2,true);
                            break;
                        }
                    }

                    break;
                case DriveControl.CMD_SHIP:
                    handShipData(msg.arg1,msg.arg2,(Integer) msg.obj,m_strPayMethod);
                    break;
                case DriveControl.CMD_SHIP_TEST:
                    shipForTestSlot(msg.arg1,msg.arg2,(Integer)msg.obj);
                    if (m_isTestingSlotNo) {
                        if (msg.arg2 != DriveControl.SHIP_STATUS_SHIPING) {
                            if (m_slotNoTestList != null) {
                                if (m_slotNoTestList.contains(Integer.valueOf(msg.arg1))) {
                                    m_slotNoTestList.remove(Integer.valueOf(msg.arg1));
                                }
                                if (m_slotNoTestList.size() > 0) {
                                    //
                                } else {
                                    m_isTestingSlotNo = false;
                                }
                            } else {
                                m_isTestingSlotNo = false;
                            }
                        }
                    }
                    break;
                case DriveControl.CMD_SELECT_SLOTNO:
                    if (DriveControl.SELECT_SUCCESS == msg.arg2) {
                        sendReceiveData(TcnProtoDef.COMMAND_SELECT_SLOTNO,msg.arg1,-1,null);
                    } else {
                        sendReceiveData(TcnProtoDef.COMMAND_INVALID_SLOTNO,msg.arg1,-1,null);
                    }
                    break;
                case DriveControl.CMD_SELF_CHECK:
                    sendReceiveData(TcnProtoDef.SELF_CHECK,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_RESET:
                    sendReceiveData(TcnProtoDef.CMD_RESET,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_TEST_MODE:
                    break;
                case DriveControl.CMD_SET_SLOTNO_SPRING:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_SPRING,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_BELTS:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_BELTS,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_SPRING:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_SPRING,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_BELT:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_BELT,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_SINGLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_SINGLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_DOUBLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_DOUBLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_SINGLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_SINGLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_REQ_QUERY_SLOTNO_EXISTS:
                    sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO_SINGLE,msg.arg1,msg.arg2,true);
                    sendReceiveData(TcnProtoDef.QUERY_SLOT_STATUS,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_BUSY:
                    sendReceiveData(TcnProtoDef.COMMAND_BUSY,msg.arg1,msg.arg2,null);
                    if (DriveControl.CMD_SHIP == msg.arg1) {
                        shipFail(msg.arg2,m_strPayMethod);  //msg.arg2:   货道号
                        m_bShiping = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
