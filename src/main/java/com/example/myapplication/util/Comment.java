package com.example.myapplication.util;

/**
 * Created by Think on 2019/1/14.
 */

public class Comment {
//    public static String URL = "http://app.machine.shu-pay.com";
    public static String URL = "http://app.qiyuwulian.top";
//    public static String URL = "http://app.machine.shu-pay.com";
//    public static String URL_IMG_OR_VIDEO = "http://file.machine.shu-pay.com";
    public static String URL_IMG_OR_VIDEO = "http://file.qiyuwulian.top";
//    public static String URL_IMG_OR_VIDEO = "http://file.machine.shu-pay.com";
    //点为
    public static String terminal_code = "001004";//终端编号（平台定义）
    public static String signatureMiYao = "1168432392755111";
    public static String KEY = "";//秘钥
    public static String ORDER_NO = "";//订单号
    public static boolean isClose = true;//判断串口是否已关闭
    public static String SERIALPORT = "";//读卡器串口值
    public static String SERIALPORT1 = "";//现金串口值
    public static String SERIALPORT2 = "";//主柜串口值
    public static String SERIALPORT2_1 = "";//副柜1串口值
    public static String SERIALPORT2_2 = "";//副柜2串口值
    public static int SIGN = 0;//跳转设置串口页面用
//    public static String terminal_code = "001003";//终端编号（平台定义）
//    public static String signatureMiYao = "3945713946274190";

//    public static String terminal_code = "8986001200ACBDA01234";//终端编号（平台定义）
//    public static String signatureMiYao = "12345678";

//    public static String terminal_code = "00010004";//终端编号（平台定义）
//    public static String signatureMiYao = "3791701310928994";
    //中吉
//    public static String terminal_code = "8986001200ACBDA01234";//终端编号（平台定义）
//    public static String signatureMiYao = "12345678";


    public static String getTerminal_code() {
        return terminal_code;
    }

    public static void setTerminal_code(String terminal_code) {
        Comment.terminal_code = terminal_code;
    }

    public static String TEST_PRICE = "0.1";//测试价格
}
