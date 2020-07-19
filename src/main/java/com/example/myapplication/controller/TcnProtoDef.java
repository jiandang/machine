package com.example.myapplication.controller;

/**
 * Created by Administrator on 2017/6/7.
 */
public class TcnProtoDef {
    //串口
    public static final int SERIAL_PORT_RECEIVE_DATA                = 100;
    public static final int SERIAL_PORT_RECEIVE_DATA_OTHER         = 101;
    public static final int SERIAL_PORT_CONFIG_ERROR                = 102;
    public static final int SERIAL_PORT_SECURITY_ERROR              = 103;
    public static final int SERIAL_PORT_UNKNOWN_ERROR               = 104;

    //出货
    public static final int COMMAND_SHIPMENT_CASHPAY             = 120; //现金购买
    public static final int COMMAND_SHIPMENT_WECHATPAY           = 121; //微信支付出货
    public static final int COMMAND_SHIPMENT_ALIPAY              = 122; //支付宝出货

    public static final int COMMAND_SLOTNO_INFO                  = 144;  //货道信息  此货道类型|货道号|单价|容量|现存|故障|掉货检测开关|此货道销售总数量|此货道销售总金额|商品编码

    public static final int COMMAND_SLOTNO_INFO_SINGLE                  = 145;


    public static final int COMMAND_SELECT_SLOTNO                        = 150; //选择货道
    public static final int COMMAND_INVALID_SLOTNO                       = 151;
    public static final int COMMAND_FAULT_SLOTNO                        = 152;
    public static final int COMMAND_SELECT_FAIL                        = 153;

    public static final int COMMAND_BUSY                              = 180;
    public static final int REQ_CMD_TEST_SLOT                          = 181;
    public static final int CMD_TEST_SLOT                              = 182; //测试货道

    public static final int REQ_QUERY_SLOT_STATUS                   = 190;
    public static final int QUERY_SLOT_STATUS                       = 191;

    public static final int REQ_SELF_CHECK                   = 194;
    public static final int SELF_CHECK                        = 195;
    public static final int REQ_CMD_RESET                   = 196;
    public static final int CMD_RESET                       = 197;
    public static final int REQ_SET_SLOTNO_SPRING                   = 198;
    public static final int SET_SLOTNO_SPRING                       = 199;
    public static final int REQ_SET_SLOTNO_BELTS                   = 200;
    public static final int SET_SLOTNO_BELTS                       = 201;
    public static final int REQ_SET_SLOTNO_ALL_SPRING                   = 202;
    public static final int SET_SLOTNO_ALL_SPRING                       = 203;
    public static final int REQ_SET_SLOTNO_ALL_BELT                   = 204;
    public static final int SET_SLOTNO_ALL_BELT                       = 205;
    public static final int REQ_SET_SLOTNO_SINGLE                   = 206;
    public static final int SET_SLOTNO_SINGLE                       = 207;
    public static final int REQ_SET_SLOTNO_DOUBLE                   = 208;
    public static final int SET_SLOTNO_DOUBLE                       = 209;
    public static final int REQ_SET_SLOTNO_ALL_SINGLE                   = 210;
    public static final int SET_SLOTNO_ALL_SINGLE                       = 211;
    public static final int REQ_SET_TEST_MODE                             = 212;
    public static final int SET_TEST_MODE                       = 213;

}
