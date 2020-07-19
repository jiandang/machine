package com.example.myapplication.util;

/**
 * Created by lihaifeng on 16/10/27.
 */
public class Cof {


    public static final String  KEY = "vending_17";

    public static final String APP_IMAGE_CACHE = "imageCache";
    public static final String IS_FIRST = "isFirst";


    public static final String KEY_SWITCH_TMP = "KEY_SWITCH_TMP";
    public static final String KEY_SWITCH_HEAT_GLASS = "KEY_SWITCH_HEAT_GLASS";
    public static final String KEY_SWITCH_LED  = "KEY_SWITCH_LED";
    public static final String KEY_SWITCH_COOL_HOT = "KEY_SWITCH_COOL_HOT";
    public static final String KEY_VALUE_TMP = "KEY_VALUE_TMP";


    public static final String MODEL_01 = "marv_vending_01";//以勒 5,10,10,10,10
    public static final String MODEL_02 = "marv_vending_02";//中吉 6,6,6,4,6
    public static final String MODEL_03 = "marv_vending_03";//中吉 10,10,10,10,10,10
    public static final String MODEL_04 = "marv_vending_04";//中吉 6,6,6,6,6,6


    public static final String KEY_DEVICE_ID = "key_deviceId";


    public static final int SERIAL_PORT_NUM = 1;


    public class Interface {


        /**
         * 正式服务器
         */
//        public static final String URL_DEVICE_LOGIN = "http://www.imstlife.com.cn/VendingServer/deviceLogin.do";
//
////        public static final String URL_DEVICE_LOGIN = "http://47.93.91.40/VendingServer/deviceLogin.do";
//
//        public static final String URL_CHECK_OUT = "http://www.imstlife.com.cn/VendingServer/checkOutState.do";

        public static final String URL_DEVICE_LOGIN = "http://www.u-24.cn/VendingServer/deviceLogin.do";

//        public static final String URL_DEVICE_LOGIN = "http://47.93.91.40/VendingServer/deviceLogin.do";
//
        public static final String URL_CHECK_OUT = "http://www.u-24.cn/VendingServer/checkOutState.do";


        /**
         * 智奇通用检查更新
         */
        public static final String URL_CHECK_UPDATE = "http://www.imstlife.com/getNewestVersion.do";

    }

    public class What {
        public static final int MAIN_LOAD_SUCCESS = 0x01;
        public static final int MAIN_LOAD_ERROR = 0x02;
        public static final int MAIN_LOADING = 0x03;
        public static final int MAIN_NO_NETWORK = 0x04;
        public static final int SHOW_PAY_QR = 0x05;
        public static final int PUSH_CONECTED = 0x06;
        public static final int PUSH_DISCONNECTED = 0x07;
        public static final int REF_DRINK_COUNT = 0x08;
        public static final int REF_WIFI = 0x10;

        public static final int TOAST = 0x12;

        public static final int OUTING_PLS_WAIT = 0x11;

        public static final int NO_NET = 0x13;

        public static final int OK_NET = 0x14;

        public static final int CHECK_LOGIN = 0x15;
    }


    /**
     * 命令发送(固定6字节)
     组号	0XFF-组号	命令	0XFF-命令	0XAA	0X55

     出货命令 货道号(0X01-0X50) (0XAA 0X55有出货检测 0X55 0XAA没有出货检测)

     自检命令 0X64
     以0XAA 0X55结束，每次出货命令前会检查光电开关是否正常（会使出货时间变长）
     以0X55 0XAA结束，每次出货命令前不检查光电开关是否正常（会使出货时间变短点）

     复位命令 0X65 所有货道转一圈 以0xAA 0x55结束

     请求重发 0X66 收到驱动板数据不正确，让驱动板再发一次上次命令 以0xAA 0x55结束

     测试模式 0X67 所有电机慢慢转动，用于检测货道的检测开关是否正常 这命令发了后只能断电才能停止   命令以0xAA 0x55结束

     查询命令 0X78+货道号 检测货道是否存在





     回复值：(固定5字节)
     D0	D1	D2	D3	D4	D5

     D0 组号

     D1 状态(0X5D正常D2无效 0X5C异常,错误信息解析D2)

     D2 =二进制(yyyydddd)
     yyyy=0000 (0x00)	无故障
     yyyy=0001 (0x01)	MOS故障1
     yyyy=0010 (0x02)	MOS故障2
     yyyy=0011 (0x03)	电机故障1
     yyyy=0100 (0x04)	电机故障2
     yyyy=0101 (0x05)	电机故障3

     dddd=0000 (0x00)	无故障
     dddd=0001 (0x01)	光电开关故障1
     dddd=0010 (0x02)	光电开关故障2
     dddd=0011 (0x03)	光电开关故障3

     D4 (0xAA:检测到出货 0x00:没开检测功能或没检测到出货)

     D5 校验和 (D0+D1+D2+D3+D4)%0x100
     */

}
