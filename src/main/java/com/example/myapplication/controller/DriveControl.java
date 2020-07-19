package com.example.myapplication.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 作者：Jiancheng,Song on 2016/3/28 09:43
 * 邮箱：m68013@qq.com
 */
public class DriveControl {
    private static DriveControl m_Instance = null;
    private static final String TAG = "DriveControl";

    public static final int SHIP_STATUS_SHIPING          = 1;
    public static final int SHIP_STATUS_SUCCESS          = 2;
    public static final int SHIP_STATUS_FAIL             = 3;

    public static final int ERR_CODE_0             = 0;   //正常
    public static final int ERR_CODE_1             = 1;   //光电开关在没有发射的情况下也有信号输出
    public static final int ERR_CODE_2             = 2;    //发射有改变的时候 也没有信号输出
    public static final int ERR_CODE_3             = 3;     //出货时一直有输出信号 不能判断好坏
    public static final int ERR_CODE_4             = 4;    //没有检测到出货
    public static final int ERR_CODE_16             = 16;   //P型MOS管有短路
    public static final int ERR_CODE_17             = 17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_18             = 18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_19             = 19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_32             = 32;    //N型MOS管有短路;
    public static final int ERR_CODE_33             = 33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_34             = 34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_35             = 35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_48             = 48;    //电机短路;
    public static final int ERR_CODE_49             = 49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_50             = 50;    //电机短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_51             = 51;    //电机短路;出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_64             = 64;    //电机断路;
    public static final int ERR_CODE_65             = 65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_66             = 66;    //电机断路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_67             = 67;    //电机断路;出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_80             = 80;    //RAM出错,电机转动超时。
    public static final int ERR_CODE_81             = 81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
    public static final int ERR_CODE_82             = 82;    //接收到数据不完整。
    public static final int ERR_CODE_83             = 83;    //校验不正确。
    public static final int ERR_CODE_84             = 84;    //地址不正确。
    public static final int ERR_CODE_86             = 86;    //货道不存在。
    public static final int ERR_CODE_87             = 87;    //返回故障代码有错超出范围。
    public static final int ERR_CODE_90             = 90;    //连续多少次转动正常但没检测到商品售出。
    public static final int ERR_CODE_91             = 91;    //其它故障小于。
    public static final int ERR_CODE_255            = 255;   //货道号不存在

    public static final int CMD_SHIP                       = 0;      // 出货
    public static final int CMD_SELF_CHECK                = 1;      // 自检
    public static final int CMD_RESET                      = 2; //复位命令，所有货道转一圈
   // public static final int CMD_REQUEST_REPEAT            = 3;  //请求重发，收到驱动板数据不正确，让驱动板再发一次上次命令
    public static final int CMD_TEST_MODE                  = 4;  //测试模式，所有电机慢慢转动，用于检测货道的检测开关是否正常 这命令发了后只能断电才能停止
    public static final int CMD_SET_SLOTNO_SPRING          = 5;  //0x68~0x74 设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public static final int CMD_SET_SLOTNO_BELTS          = 6;  //0x68~0x74 设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public static final int CMD_SET_SLOTNO_ALL_SPRING     = 7;  //全部设置为弹簧货道
    public static final int CMD_SET_SLOTNO_ALL_BELT       = 8;   //全部设置为皮带货道
    public static final int CMD_QUERY_SLOTNO_EXISTS       = 9; //0x78+货道：查询命令，检测货道是否存在
    public static final int CMD_SET_SLOTNO_SINGLE         = 10; //0XC9：设置单货道，如果该货道本来是双货道，则自动将两个货道都改为单货道
    public static final int CMD_SET_SLOTNO_DOUBLE         = 11; //0XCA：设置双货道，将该货道号和下一货道号合并为双货道
    public static final int CMD_SET_SLOTNO_ALL_SINGLE     = 12; //0xCB：全部设置为单货道
    public static final int CMD_SELECT_SLOTNO                    = 15;   // 选择货道
    public static final int CMD_BUSY                    = 16;   // 系统忙
    public static final int CMD_SHIP_TEST                    = 17;   // 测试货道
    public static final int CMD_REQ_QUERY_SLOTNO_EXISTS       = 18; //0x78+货道：查询命令，检测货道是否存在

    public static final int CMD_SET_TEMP_CONTROL_OR_NOT       = 20; //设置是否使用温度控制   0 不使用   非0 使用
    public static final int CMD_SET_COOL              = 21; //设置是制冷还是加热     0 加热     非0 制冷
    public static final int CMD_SET_HEAT              = 22; //设置是制冷还是加热     0 加热     非0 制冷
    public static final int CMD_SET_TEMP        = 23; //设置制冷或加热的目标温度   有符号整数
    public static final int CMD_SET_GLASS_HEAT         = 24; //设置是否使用玻璃加热   0 不使用   非0 使用
    public static final int CMD_READ_CURRENT_TEMP        = 25; //读取当前机柜内温度
    public static final int CMD_SET_LIGHT_OPEN        = 26; //LED灯条控制     PARAM=0XAA  打开LED灯条  PARAM=0X55 关闭LED灯条
    public static final int CMD_SET_LIGHT_CLOSE        = 27; //LED灯条控制     PARAM=0XAA  打开LED灯条  PARAM=0X55 关闭LED灯条
    public static final int CMD_SET_BUZZER_OPEN        = 28; //控制本机蜂鸣器  PARAM=0XAA  蜂鸣器响起   PARAM=0X55 蜂鸣器响声关闭
    public static final int CMD_SET_BUZZER_CLOSE        = 29; //控制本机蜂鸣器  PARAM=0XAA  蜂鸣器响起   PARAM=0X55 蜂鸣器响声关闭
    public static final int CMD_READ_DOOR_STATUS        = 30; //读取门碰开关状态       0 关门     非0 开门

    public static final int STATUS_DO_NONE               = -1;    //
    public static final int STATUS_DO_SELECT              = 1;    //选择货道
    public static final int STATUS_DO_SHIPING             = 2;    //出货

    public static final int SELECT_FAIL   = -1;
    public static final int SELECT_SUCCESS   = 0;

    public static final int GROUP_SERIPORT_1             = 1;    //串口1
    public static final int GROUP_SERIPORT_2              = 2;    //串口2
    public static final int GROUP_SERIPORT_3             = 3;    //串口3
    public static final int GROUP_SERIPORT_4             = 4;    //串口4
    
    private volatile boolean m_bQueryingAllSlotNo = false;
    private volatile boolean m_bHaveSlotInfo = false;
    private volatile boolean m_bUseCheck = true;
    private volatile int m_iReqNextCount = 0;
    private volatile int m_iCurrentNum = 0;
    private volatile int m_iCmdType = -1;
    private volatile int m_iStatusDo = STATUS_DO_NONE;
    private volatile int m_iRepeatCount = 0;
    private volatile byte m_bGroupNumber = (byte)0x00;
    private volatile int m_iCurrentSerptGrp = GROUP_SERIPORT_1;
    private volatile StringBuffer m_read_sbuff = new StringBuffer();

    private Handler m_handlerReceive = null;

    private Timer m_TimerGetData = null;
    private TimerTask m_TimerTaskGetData = null;

    private WriteThread m_WriteThread = null;



    public static synchronized DriveControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new DriveControl();
        }
        return m_Instance;
    }

    public void init(Handler handlerReceive) {
        m_handlerReceive = handlerReceive;
        m_WriteThread = new WriteThread();
        m_WriteThread.setSendHandler(handlerReceive);
        m_WriteThread.startWriteThreads();
    }

    public boolean isBusy() {
        boolean bRet = false;
        if (m_WriteThread != null) {
            bRet = m_WriteThread.isBusy();
        }
        return bRet;
    }

    public void sendBusyMessage(int cmdType) {
        if (null == m_handlerReceive) {
            return;
        }
        Message message = m_handlerReceive.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.obj = Integer.valueOf(-1);
        m_handlerReceive.sendMessage(message);
    }

    private void sendBusyMessageWithSlotNo(int cmdType, int slotNo) {
        if (null == m_handlerReceive) {
            return;
        }
        Message message = m_handlerReceive.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.arg2 = slotNo;
        message.obj = Integer.valueOf(-1);
        m_handlerReceive.sendMessage(message);
    }

    public void deInit() {
        m_read_sbuff.delete(0,m_read_sbuff.length());
        if (m_handlerReceive != null) {
            m_handlerReceive.removeCallbacksAndMessages(null);
            m_handlerReceive = null;
        }
    }

    public boolean isHasSlotNo() {
        return m_bHaveSlotInfo;
    }

    public int getCurrentSerptGrp() {
        return m_iCurrentSerptGrp;
    }

    public byte getCurrentGroupNumber() {
        return m_bGroupNumber;
    }


    public void sendCmdGetData(boolean isNextGrp,int serptGrp,int addrSlotNo,byte boardGrpNo) {
        startCmdGetDataTimer(isNextGrp,serptGrp,addrSlotNo,boardGrpNo);
    }


    private void stopCmdGetDataTimer() {
        if (m_TimerTaskGetData != null) {
            m_TimerTaskGetData.cancel();
            m_TimerTaskGetData = null;
        }

        if (m_TimerGetData != null) {
            m_TimerGetData.cancel();
            m_TimerGetData.purge();
            m_TimerGetData = null;
        }

    }

    private void startCmdGetDataTimer(final boolean isNextGrp,final int serptGrp,final int addrSlotNo,final byte boardGrpNo) {
        stopCmdGetDataTimer();

        m_bHaveSlotInfo = false;
        m_TimerGetData = new Timer("startCmdGetDataTimer");

        m_iReqNextCount = 0;

        m_TimerTaskGetData = new TimerTask() {

            @Override
            public void run() {

                if (m_bHaveSlotInfo) {
                    stopCmdGetDataTimer();
                } else if (isNextGrp && (m_iReqNextCount > 2)) {
                    stopCmdGetDataTimer();
                    if (m_handlerReceive != null) {
                        m_bQueryingAllSlotNo = false;
                        Message message = m_handlerReceive.obtainMessage();
                        message.what = CMD_QUERY_SLOTNO_EXISTS;
                        message.arg1 = -1;
                        message.obj = Boolean.valueOf(false);
                        m_handlerReceive.sendMessage(message);
                    }

                } else {
                    if (!m_WriteThread.isBusy()) {
                        m_iCurrentSerptGrp = serptGrp;
                        m_iCurrentNum = addrSlotNo;
                        m_bGroupNumber = boardGrpNo;
                        reqSlotNoInfo();
                    }
                }

                if (isNextGrp) {
                    m_iReqNextCount++;
                }
            }

        };
        m_TimerGetData.schedule(m_TimerTaskGetData, 3000, 10000);
    }


    private byte getGroup(String grp) {
        byte bRet = (byte)0xFF;
        byte[] bAddrArry = TcnUtility.hexStringToBytes(grp);
        if ((null == bAddrArry) || (bAddrArry.length < 1)) {
            return bRet;
        }
        bRet = bAddrArry[0];
        return bRet;
    }

    public void writeData(int cmdType,byte[] bytessMsg) {
        long cmdOverTimeSpan = 30000;
        if ((CMD_SHIP == cmdType) || (CMD_SHIP_TEST == cmdType)) {
            if (m_bUseCheck) {
                cmdOverTimeSpan = 30000;   //带光检超时设置25秒
            } else {
                cmdOverTimeSpan = 30000;  //不带光检超时设置20秒
            }
        } else if ((CMD_QUERY_SLOTNO_EXISTS == cmdType) || (CMD_REQ_QUERY_SLOTNO_EXISTS == cmdType)) {
            cmdOverTimeSpan = 500;
        } else if ((CMD_RESET == cmdType) || (CMD_TEST_MODE == cmdType)) {
            cmdOverTimeSpan = 60 * 60 * 1000;
        }
        else {
            cmdOverTimeSpan = 5000;
        }

        if (GROUP_SERIPORT_1 == m_iCurrentSerptGrp) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_1,cmdType,cmdOverTimeSpan,bytessMsg);
        } else if (GROUP_SERIPORT_2 == m_iCurrentSerptGrp) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_2,cmdType,cmdOverTimeSpan,bytessMsg);
        } else if (GROUP_SERIPORT_3 == m_iCurrentSerptGrp) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_3,cmdType,cmdOverTimeSpan,bytessMsg);
        } else if (GROUP_SERIPORT_4 == m_iCurrentSerptGrp) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_4,cmdType,cmdOverTimeSpan,bytessMsg);
        }
        else {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_1,cmdType,cmdOverTimeSpan,bytessMsg);
        }
    }

    private String getGroup(byte grp) {
        String strGrp = "";
        if (grp == ((byte)0x00)) {
            strGrp = "00";
        } else if (grp == ((byte)0x01)) {
            strGrp = "01";
        } else if (grp == ((byte)0x02)) {
            strGrp = "02";
        } else if (grp == ((byte)0x03)) {
            strGrp = "03";
        } else if (grp == ((byte)0x04)) {
            strGrp = "04";
        } else if (grp == ((byte)0x05)) {
            strGrp = "05";
        } else if (grp == ((byte)0x06)) {
            strGrp = "06";
        } else if (grp == ((byte)0x07)) {
            strGrp = "07";
        } else if (grp == ((byte)0x08)) {
            strGrp = "08";
        } else if (grp == ((byte)0x09)) {
            strGrp = "09";
        } else {

        }
        return strGrp;

    }

    public void reqSelectSlotNo(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessageWithSlotNo(CMD_SELECT_SLOTNO,addrSlotNo);
            return;
        }
        m_iStatusDo = STATUS_DO_SELECT;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        querySlotExists(serptGrp,addrSlotNo,boardGrpNo);
    }

    //出货命令
    public void ship(boolean useLightCheck, int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessageWithSlotNo(CMD_SHIP,addrSlotNo);
            return;
        }
        m_bUseCheck = useLightCheck;
        m_iStatusDo = STATUS_DO_SHIPING;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        Message message = m_handlerReceive.obtainMessage();
        message.what = CMD_SHIP;
        message.arg1 = addrSlotNo;
        message.arg2 = SHIP_STATUS_SHIPING;
        message.obj = Integer.valueOf(-1);
        m_handlerReceive.sendMessage(message);

        querySlotExists(serptGrp,addrSlotNo,boardGrpNo);
    }

    //出货命令
    public void shipTest(boolean useLightCheck, int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SHIP_TEST);
            return;
        }
        m_iStatusDo = STATUS_DO_SHIPING;
        m_iCmdType = CMD_SHIP_TEST;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        Message message = m_handlerReceive.obtainMessage();
        message.what = CMD_SHIP_TEST;
        message.arg1 = addrSlotNo;
        message.arg2 = SHIP_STATUS_SHIPING;
        message.obj = Integer.valueOf(-1);
        m_handlerReceive.sendMessage(message);

        ship(useLightCheck,CMD_SHIP_TEST,m_bGroupNumber,m_iCurrentNum);
    }

    //出货命令
    private void ship(boolean useLightCheck, int cmdType, byte addr, int realSlotNo) {
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(cmdType);
            return;
        }
        m_iCmdType = cmdType;

        byte _bSlotNo = Integer.valueOf(realSlotNo).byteValue();
        byte[] bCmdData = new byte[6];
        bCmdData[0] = addr;
        bCmdData[1] = (byte)~addr;
        bCmdData[2] = _bSlotNo;
        bCmdData[3] = (byte)~_bSlotNo;
        if (useLightCheck) {
            bCmdData[4] = (byte)0xAA;
            bCmdData[5] = (byte)0x55;
        } else {
            bCmdData[4] = (byte)0x55;
            bCmdData[5] = (byte)0xAA;
        }
        writeData(cmdType,bCmdData);
    }


    //自检命令
    public void selfCheck(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SELF_CHECK);
            return;
        }

        m_iCmdType = CMD_SELF_CHECK;
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x64;
        bCmdData[3] = (byte)~0x64;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SELF_CHECK,bCmdData);
    }

    //所有货道转一圈
    public void reset(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_RESET);
            return;
        }
        m_iCmdType = CMD_RESET;
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x65;
        bCmdData[3] = (byte)~0x65;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_RESET,bCmdData);
    }

    //请求重发，收到驱动板数据不正确，让驱动板再发一次上次命令
    public void reqRepeatSend() {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x66;
        bCmdData[3] = (byte)~0x66;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(m_iCmdType,bCmdData);
    }

    //测试模式，所有电机慢慢转动，用于检测货道的检测开关是否 正常 这命令发了后只能断电才能停止
    public void testMode(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_TEST_MODE);
            return;
        }

        m_iCmdType = CMD_TEST_MODE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x67;
        bCmdData[3] = (byte)~0x67;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_TEST_MODE,bCmdData);
    }

    //0x68~0x74：设置货道步数，0x74 为弹簧货道，其他为皮带货道
    //0x75：全部设置为弹簧货道
    //0x76：全部设置为皮带货道
    public void setSlotSpring(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_SPRING);
            return;
        }
        m_iCmdType = CMD_SET_SLOTNO_SPRING;

        m_iCurrentSerptGrp = serptGrp;
        
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x74;
        bCmdData[3] = (byte)~0x74;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;
        writeData(CMD_SET_SLOTNO_SPRING,bCmdData);
    }

    //0x68~0x74：设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public void setSlotBelt(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_BELTS);
            return;
        }
        m_iCmdType = CMD_SET_SLOTNO_BELTS;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x68;
        bCmdData[3] = (byte)~0x68;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;
        writeData(CMD_SET_SLOTNO_BELTS,bCmdData);
    }

    //0x75：全部设置为弹簧货道
    public void setSlotAllSpring(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_ALL_SPRING);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_SLOTNO_ALL_SPRING;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x75;
        bCmdData[3] = (byte)~0x75;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_SPRING,bCmdData);
    }

    //0x76：全部设置为皮带货道
    public void setSlotAllBelt(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_ALL_BELT);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_SLOTNO_ALL_BELT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x76;
        bCmdData[3] = (byte)~0x76;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_BELT,bCmdData);
    }

    public void reqQuerySlotExists(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_REQ_QUERY_SLOTNO_EXISTS);
            return;
        }
        m_iCmdType = CMD_REQ_QUERY_SLOTNO_EXISTS;

        m_iCurrentSerptGrp = serptGrp;
         
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte _bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)((byte)0x78+_bSlotNo);
        bCmdData[3] = (byte)~((byte)0x78+_bSlotNo);
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_REQ_QUERY_SLOTNO_EXISTS,bCmdData);
    }


    //0x78+货道：查询命令，检测货道是否存在
    private void querySlotExists(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_QUERY_SLOTNO_EXISTS);
            return;
        }
        m_iCmdType = CMD_QUERY_SLOTNO_EXISTS;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte _bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)((byte)0x78+_bSlotNo);
        bCmdData[3] = (byte)~((byte)0x78+_bSlotNo);
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_QUERY_SLOTNO_EXISTS,bCmdData);
    }

    //0XC9：设置单货道，如果该货道本来是双货道，则自动将两个货道都 改为单货道
    public void setSingleSlotno(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_SINGLE);
            return;
        }
        m_iCmdType = CMD_SET_SLOTNO_SINGLE;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xC9;
        bCmdData[3] = (byte)~0xC9;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;

        /*
        //另一种驱动板 拆分货道
        //组号   255-组号    118   货道号1   货道号1   货道号2
        //货道号2 为 255 的时候拆开货道
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x76;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[3] = bSlotNo;
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)(0xFF);*/

        writeData(CMD_SET_SLOTNO_SINGLE,bCmdData);
    }

    //设置双货道，将该货道号和下一货道号合并为双货道
    public void setDoubleSlotno(int serptGrp,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_DOUBLE);
            return;
        }
        m_iCmdType = CMD_SET_SLOTNO_DOUBLE;

        m_iCurrentSerptGrp = serptGrp;
        m_iCurrentNum = addrSlotNo;
        m_bGroupNumber = boardGrpNo;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCA;
        bCmdData[3] = (byte)~0xCA;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;

        /*
        //另一种驱动板 拆分货道
        //组号   255-组号    118   货道号1   货道号1   货道号2
        //货道号2 为 255 的时候拆开货道   否则 货道号1 和货道号2 合并为1
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x76;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[3] = bSlotNo;
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)(bSlotNo + 0x01);*/

        writeData(CMD_SET_SLOTNO_DOUBLE,bCmdData);
    }

    //0xCB：全部设置为单货道
    public void setAllSlotnoSingle(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_SLOTNO_ALL_SINGLE);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_SLOTNO_ALL_SINGLE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCB;
        bCmdData[3] = (byte)~0xCB;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_SINGLE,bCmdData);
    }

    //设置是否使用温度控制   0 不使用   非0 使用
    public void setTempControl(boolean control,int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_TEMP_CONTROL_OR_NOT);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_TEMP_CONTROL_OR_NOT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCC;
        bCmdData[3] = (byte)~0x33;
        if (control) {
            bCmdData[4] = (byte)0x01;
            bCmdData[5] = (byte)0xFE;
        } else {
            bCmdData[4] = (byte)0x00;
            bCmdData[5] = (byte)0xFF;
        }

        writeData(CMD_SET_TEMP_CONTROL_OR_NOT,bCmdData);
    }

    public void setCool(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_COOL);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_COOL;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCD;
        bCmdData[3] = (byte)~0x32;
        bCmdData[4] = (byte)0x01;
        bCmdData[5] = (byte)0xFE;

        writeData(CMD_SET_COOL,bCmdData);
    }

    public void setHeat(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_HEAT);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_HEAT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCD;
        bCmdData[3] = (byte)~0x32;
        bCmdData[4] = (byte)0x00;
        bCmdData[5] = (byte)0xFF;

        writeData(CMD_SET_HEAT,bCmdData);
    }

    public void setTemp(int temp,int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_TEMP);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_TEMP;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xCE;
        bCmdData[3] = (byte)~0x31;
        byte bTemp = Integer.valueOf(temp).byteValue();
        bCmdData[4] = bTemp;
        bCmdData[5] = (byte)~bTemp;

        writeData(CMD_SET_TEMP,bCmdData);
    }

    public void setGlassHeatEnable(boolean enable, int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_GLASS_HEAT);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_GLASS_HEAT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xD4;
        bCmdData[3] = (byte)~0x2B;
        if (enable) {
            bCmdData[4] = (byte)0x01;;
            bCmdData[5] = (byte)0xFE;
        } else {
            bCmdData[4] = (byte)0x00;;
            bCmdData[5] = (byte)0xFF;
        }

        writeData(CMD_SET_GLASS_HEAT,bCmdData);
    }

    //读取当前机柜内温度  收到指令之后 第三个字节，此参数为当前温度值，为有符号整数
    public void setReadTemp(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_READ_CURRENT_TEMP);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_READ_CURRENT_TEMP;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDC;
        bCmdData[3] = (byte)~0x23;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_READ_CURRENT_TEMP,bCmdData);
    }

    public void setLightOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_LIGHT_OPEN);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_LIGHT_OPEN;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDD;
        bCmdData[3] = (byte)~0x22;
        bCmdData[4] = (byte)0xAA;;
        bCmdData[5] = (byte)0x55;

        writeData(CMD_SET_LIGHT_OPEN,bCmdData);
    }

    public void setLightClose(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_LIGHT_CLOSE);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_LIGHT_CLOSE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDD;
        bCmdData[3] = (byte)~0x22;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_SET_LIGHT_CLOSE,bCmdData);
    }

    public void setBuzzerOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_BUZZER_OPEN);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_BUZZER_OPEN;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDE;
        bCmdData[3] = (byte)~0x21;
        bCmdData[4] = (byte)0xAA;;
        bCmdData[5] = (byte)0x55;

        writeData(CMD_SET_BUZZER_OPEN,bCmdData);
    }

    public void setBuzzerClose(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_SET_BUZZER_CLOSE);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_SET_BUZZER_CLOSE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDE;
        bCmdData[3] = (byte)~0x21;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_SET_BUZZER_CLOSE,bCmdData);
    }

    //读取门碰开关状态       0 关门     非0 开门
    public void setReadDoorStatus(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(CMD_READ_DOOR_STATUS);
            return;
        }
        m_iCurrentSerptGrp = serptGrp;
        m_bGroupNumber = boardGrp;
        m_iCmdType = CMD_READ_DOOR_STATUS;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0xDF;
        bCmdData[3] = (byte)~0x20;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_READ_DOOR_STATUS,bCmdData);
    }

    private void reqSlotNoInfo() {
        if (m_WriteThread.isBusy()) {
            return;
        }

        m_read_sbuff.delete(0,m_read_sbuff.length());
        m_bQueryingAllSlotNo = true;
        m_iStatusDo = STATUS_DO_NONE;
        if (((byte)0xFF) == m_bGroupNumber) {

            return;
        }
        querySlotExists(m_iCurrentSerptGrp,m_iCurrentNum,m_bGroupNumber);
    }

    public boolean isQueryingAllSlotNo() {
        return m_bQueryingAllSlotNo;
    }

    private int getErr(String errHexCode, String status) {
        int iErrCode = ERR_CODE_0;
        if (null == errHexCode) {
            return iErrCode;
        }

        if (null == status) {
            return iErrCode;
        }

        if(status.equalsIgnoreCase("5D")) {   //正常
            return iErrCode;
        }

        if (errHexCode.equals("00")) {
            iErrCode = ERR_CODE_255;   //如果status
        } else if (errHexCode.equals("01")) {
            iErrCode = ERR_CODE_1;    //光电开关在没有发射的情况下也有信号输出
        } else if (errHexCode.equals("02")) {
            iErrCode = ERR_CODE_2;    //发射有改变的时候 也没有信号输出
        } else if (errHexCode.equals("03")) {
            iErrCode = ERR_CODE_3;    //出货时一直有输出信号 不能判断好坏
        } else if (errHexCode.equals("04")) {
            iErrCode = ERR_CODE_4;    //没有检测到出货
        } else if (errHexCode.equals("16")) {
            iErrCode = ERR_CODE_16;    //P型MOS管有短路
        } else if (errHexCode.equals("17")) {
            iErrCode = ERR_CODE_17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("18")) {
            iErrCode = ERR_CODE_18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("19")) {
            iErrCode = ERR_CODE_19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("32")) {
            iErrCode = ERR_CODE_32;    //N型MOS管有短路;
        } else if (errHexCode.equals("33")) {
            iErrCode = ERR_CODE_33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("34")) {
            iErrCode = ERR_CODE_34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("35")) {
            iErrCode = ERR_CODE_35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("48")) {
            iErrCode = ERR_CODE_48;    //电机短路;
        } else if (errHexCode.equals("49")) {
            iErrCode = ERR_CODE_49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("50")) {
            iErrCode = ERR_CODE_50;    //电机短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("51")) {
            iErrCode = ERR_CODE_51;    //电机短路;出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("64")) {
            iErrCode = ERR_CODE_64;    //电机断路;
        } else if (errHexCode.equals("65")) {
            iErrCode = ERR_CODE_65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("66")) {
            iErrCode = ERR_CODE_66;    //电机断路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("67")) {
            iErrCode = ERR_CODE_67;    //电机断路;出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("80")) {
            iErrCode = ERR_CODE_80;    //RAM出错,电机转动超时。
        } else if (errHexCode.equals("81")) {
            iErrCode = ERR_CODE_81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
        } else if (errHexCode.equals("82")) {
            iErrCode = ERR_CODE_82;    //接收到数据不完整。
        } else if (errHexCode.equals("83")) {
            iErrCode = ERR_CODE_83;    //校验不正确。
        } else if (errHexCode.equals("84")) {
            iErrCode = ERR_CODE_84;    //地址不正确。
        } else if (errHexCode.equals("86")) {
            iErrCode = ERR_CODE_86;    //货道不存在。
        } else if (errHexCode.equals("87")) {
            iErrCode = ERR_CODE_87;    //返回故障代码有错超出范围。
        } else if (errHexCode.equals("90")) {
            iErrCode = ERR_CODE_90;    //连续多少次转动正常但没检测到商品售出。
        } else if (errHexCode.equals("91")) {
            iErrCode = ERR_CODE_91;    //其它故障小于。
        } else {
            iErrCode = Integer.parseInt(errHexCode,16);
        }

        return iErrCode;
    }


    private boolean isValidDataPacket(String dataPacket) {
        boolean bRet = false;
        byte[] bDataPacket = TcnUtility.hexStringToBytes(dataPacket);
        if ((null == bDataPacket) || (bDataPacket.length < 1)) {
            return bRet;
        }
        int iLength = bDataPacket.length;
        byte bCheck = 0x00;
        for (int i = 0; i < (iLength - 1); i++) {
            bCheck = (byte)(bCheck + bDataPacket[i]);
        }
        if (bCheck == bDataPacket[iLength - 1]) {
            bRet = true;
        }
        return bRet;
    }

    private void handQuery(boolean useCheck, int querySlotNo,int iErrCode, String hexAddr, String status,Message message) {
        int iAddr = Integer.valueOf(hexAddr,16);
        Log.i(TAG, "handQuery() querySlotNo: "+querySlotNo+" iAddr: "+iAddr+" status: "+status);
        message.what = CMD_QUERY_SLOTNO_EXISTS;
        message.arg1 = m_iCurrentNum;
        message.arg2 = iErrCode;
        boolean bIsNotLoadAll = false;
        if(status.equals("5D")) {   //正常
            if (STATUS_DO_SELECT == m_iStatusDo) {
                bIsNotLoadAll = true;
                m_iStatusDo = STATUS_DO_NONE;
                if (((Integer.valueOf(iAddr).byteValue())) == m_bGroupNumber) {
                    Message msgSelect = m_handlerReceive.obtainMessage();
                    msgSelect.what = CMD_SELECT_SLOTNO;
                    msgSelect.arg1 = m_iCurrentNum;
                    msgSelect.arg2 = SELECT_SUCCESS;
                    m_handlerReceive.sendMessage(msgSelect);
                } else {
                    Log.i(TAG, "handQuery() STATUS_DO_SELECT m_bGroupNumber: "+m_bGroupNumber);
                }
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
            } else if (STATUS_DO_SHIPING == m_iStatusDo) {
                Log.i(TAG, "handQuery() m_bGroupNumber: "+m_bGroupNumber);
                bIsNotLoadAll = true;
                if ((Integer.valueOf(iAddr).byteValue()) == m_bGroupNumber) {

                    if (m_WriteThread != null) {
                        m_WriteThread.setBusy(false);
                    }

                    ship(useCheck,CMD_SHIP,m_bGroupNumber,querySlotNo);

                } else {
                    m_iStatusDo = STATUS_DO_NONE;

                    Message msgShipFail = m_handlerReceive.obtainMessage();
                    msgShipFail.what = CMD_SHIP;
                    msgShipFail.arg1 = m_iCurrentNum;
                    msgShipFail.arg2 = SHIP_STATUS_FAIL;
                    msgShipFail.obj = Integer.valueOf(-1);
                    m_handlerReceive.sendMessage(msgShipFail);

                    if (m_WriteThread != null) {
                        m_WriteThread.setBusy(false);
                    }
                }
            } else {
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
            }
        } else if (status.equals("5C")) {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
        }
        else {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
        }
        m_bHaveSlotInfo = true;
        if (m_bQueryingAllSlotNo) {
            if (querySlotNo < 80) {
                querySlotNo++;
               // byte bGrp = getGroup(hexAddr);
                querySlotExists(m_iCurrentSerptGrp,querySlotNo,m_bGroupNumber);
            } else {
                m_bQueryingAllSlotNo = false;
            }
        }
        message.obj = bIsNotLoadAll;
        m_handlerReceive.sendMessage(message);
    }

    /*
    回复命令：(固定 5 字节) D0 D1 D2 D3 D4
     *D0:组号 D1: 状态(0X5D代表正常，此时 D2 无效； 0X5C代表异常,此时D2表示 错误信息解析)
     */
    private void commondAnalyse(int cmdType,String cmdData) {
        Log.i(TAG, "commondAnalyse() cmdType: "+cmdType+" cmdData: "+cmdData+" m_bUseCheck: "+m_bUseCheck);
        if ((null == cmdData) || (cmdData.length() < 10)) {
            return;
        }

        String addr = cmdData.substring(0,2);
        String status = cmdData.substring(2,4);
        String errInfo = cmdData.substring(4,6);
        String shipStatus = cmdData.substring(6,8);
        int iErrCode = getErr(errInfo,status);
        Message message = m_handlerReceive.obtainMessage();
        if (CMD_SHIP == cmdType) {
            m_iStatusDo = STATUS_DO_NONE;
            boolean bShipStatus = false;
            if (m_bUseCheck) {
                if (shipStatus.equals("AA")) {  //检测到出货
                    bShipStatus = true;
                } else {
                    iErrCode = ERR_CODE_4;   //没有检测到出货
                }
            } else {
                bShipStatus = true;
            }
            Log.i(TAG, "commondAnalyse() CMD_SHIP m_iCurrentNum: "+m_iCurrentNum+" shipStatus: "+shipStatus);
            message.what = CMD_SHIP;
            message.arg1 = m_iCurrentNum;
            if (bShipStatus) {
                message.arg2 = SHIP_STATUS_SUCCESS;
            } else {
                message.arg2 = SHIP_STATUS_FAIL;
            }
            message.obj = Integer.valueOf(iErrCode);
        } else if (CMD_SELF_CHECK == cmdType) {
            message.what = CMD_SELF_CHECK;
            message.arg1 = iErrCode;
        } else if (CMD_RESET == cmdType) {
            message.what = CMD_RESET;
            message.arg1 = iErrCode;
        } else if (CMD_TEST_MODE == cmdType) {

        } else if (CMD_SET_SLOTNO_SPRING == cmdType) {
            message.what = CMD_SET_SLOTNO_SPRING;
            message.arg1 = m_iCurrentNum;
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_BELTS == cmdType) {
            message.what = CMD_SET_SLOTNO_BELTS;
            message.arg1 = m_iCurrentNum;
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_SPRING == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_SPRING;
            message.arg1 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_BELT == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_BELT;
            message.arg1 = iErrCode;
        } else if (CMD_QUERY_SLOTNO_EXISTS == cmdType) {
            handQuery(m_bUseCheck,m_iCurrentNum,iErrCode,addr,status,message);
        } else if (CMD_SET_SLOTNO_SINGLE == cmdType) {
            message.what = CMD_SET_SLOTNO_SINGLE;
            message.arg1 = m_iCurrentNum;
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
            message.what = CMD_SET_SLOTNO_DOUBLE;
            message.arg1 = m_iCurrentNum;
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_SINGLE == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_SINGLE;
            message.arg1 = iErrCode;
        } else if (CMD_SHIP_TEST == cmdType) {
            m_iStatusDo = STATUS_DO_NONE;
            boolean bShipStatus = false;
            if (m_bUseCheck) {
                if (shipStatus.equals("AA")) {  //检测到出货
                    bShipStatus = true;
                }
            } else {
                bShipStatus = true;
            }
            Log.i(TAG, "commondAnalyse() CMD_SHIP_TEST m_iCurrentNum: "+m_iCurrentNum+" shipStatus: "+shipStatus);
            message.what = CMD_SHIP_TEST;
            message.arg1 = m_iCurrentNum;
            if (bShipStatus) {
                message.arg2 = SHIP_STATUS_SUCCESS;
            } else {
                message.arg2 = SHIP_STATUS_FAIL;
            }
            message.obj = Integer.valueOf(iErrCode);
        } else if (CMD_REQ_QUERY_SLOTNO_EXISTS == cmdType) {
            message.what = CMD_REQ_QUERY_SLOTNO_EXISTS;
            message.arg1 = m_iCurrentNum;
            message.arg2 = iErrCode;
        }
        else {

        }

        if (CMD_QUERY_SLOTNO_EXISTS != cmdType) {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
            m_handlerReceive.sendMessage(message);
        }
    }

    public void protocolAnalyse(String strCmdData) {
        if ((null == strCmdData) || (strCmdData.length() <= 0)) {
            Log.i(TAG, "protocolAnalyse() strCmdData: "+strCmdData+" m_read_sbuff: "+m_read_sbuff);
            return;
        }

        try {

            m_read_sbuff.append(strCmdData);

            while ((m_read_sbuff.length()) >= 10) {
                String strGroupNumber = getGroup(m_bGroupNumber);
                int indexGroup = m_read_sbuff.indexOf(strGroupNumber);
                if ((indexGroup < 0)) {     //该组号不存在
                    //命令不对，请求驱动板重发
                    m_read_sbuff.delete(0,m_read_sbuff.length());
                    if (m_iRepeatCount > 1) {
                        m_iRepeatCount = 0;
                    } else {
                        m_iRepeatCount++;
                        reqRepeatSend();
                    }
                    break;  //退出
                } else if (indexGroup > 0) {
                    m_read_sbuff.delete(0,indexGroup);
                } else {

                }

                m_iRepeatCount = 0;
                if (m_read_sbuff.length() < 10) {
                    break;
                }
                String strCmd = m_read_sbuff.substring(0,10);
                if (isValidDataPacket(strCmd)) {    //校验
                    m_read_sbuff.delete(0,10);
                    commondAnalyse(m_iCmdType,strCmd);
                } else {
                    m_read_sbuff.delete(0,2);
                    continue;
                }
            }
        } catch (Exception e) {
            m_read_sbuff.delete(0,m_read_sbuff.length());
            Log.i(TAG, "protocolAnalyse isOrig Exception  e: "+e);
        }
    }
}
