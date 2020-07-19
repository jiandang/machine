package com.example.myapplication.controller;

/**
 * Created by Administrator on 2017/6/7.
 */
public class TcnProtoResultDef {

    public static final int CMD_NO_DATA_RECIVE       = -10;

    public static final int SHIP_SHIPING             = 1; //出货中
    public static final int SHIP_SUCCESS             = 2; //出货成功
    public static final int SHIP_FAIL                = 3; //出货失败

    public static final int STATUS_INVALID		             = -1;
    public static final int STATUS_FREE		             = 1;
    public static final int STATUS_BUSY		             = 2;
    public static final int STATUS_WAIT_TAKE_GOODS		 = 3;

    public static final int DO_NONE   = -1;
    public static final int DO_START   = 0;
    public static final int DO_END   = 1;

}
