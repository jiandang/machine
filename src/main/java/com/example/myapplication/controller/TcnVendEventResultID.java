package com.example.myapplication.controller;

/**
 * Created by Administrator on 2017/6/9.
 */
public class TcnVendEventResultID {

    public static final int CMD_NO_DATA_RECIVE        = -10;

    public static final int QR_CODE_GENERATE_SUCCESS = 1;
    public static final int QR_CODE_GENERATE_FAILED = -1;

    public static final int MDB_PAYOUT_START = -1;
    public static final int MDB_PAYOUT_END = 0;

    public static final int SHIP_SHIPING             = 1; //出货中
    public static final int SHIP_SUCCESS             = 2; //出货成功
    public static final int SHIP_FAIL                = 3; //出货失败

    public static final int FAIL                   = -1; //操作失败
    public static final int SUCCESS                = 0; //操作成功

    public static final int STATUS_INVALID		             = -1;
    public static final int STATUS_FREE		             = 1;
    public static final int STATUS_BUSY		             = 2;
    public static final int STATUS_WAIT_TAKE_GOODS		 = 3;

    public static final int CMD_DETECT_LIGHT_INVALID        = -1;
    public static final int CMD_DETECT_LIGHT_BLOCKED        = 0;  //检测升降机光检   表示挡住
    public static final int CMD_DETECT_LIGHT_NOT_BLOCKED   = 1; //检测升降机光检   表示没挡住

    public static final int CMD_DETECT_SHIP_INVALID        = -1;
    public static final int CMD_DETECT_SHIP_NO_GOODS      = 0;  //0 表示没有货物，1 表示有货物
    public static final int CMD_DETECT_SHIP_HAVE_GOODS    = 1;  //0 表示没有货物，1 表示有货物

    public static final int DO_INVALID   = -1;
    public static final int DO_OPEN   = 1;     //开门
    public static final int DO_CLOSE   = 2;     //关门


    public static final int DO_NONE   = -1;
    public static final int DO_START   = 0;
    public static final int DO_END   = 1;

    /******************* lift start *************************************************************/
    public static final int ADDR_FLOOR_0                    = 0;     //第一层层高
    public static final int ADDR_FLOOR_1                    = 1;     //第二层层高
    public static final int ADDR_FLOOR_2                    = 2;     //第三层层高
    public static final int ADDR_FLOOR_3                    = 3;     //第四层层高
    public static final int ADDR_FLOOR_4                    = 4;     //第五层层高
    public static final int ADDR_FLOOR_5                    = 5;     //第六层层高
    public static final int ADDR_FLOOR_6                    = 6;     //第七层层高
    public static final int ADDR_FLOOR_7                    = 7;     //第八层层高
    public static final int ADDR_LIGHT_DEGREE              = 8;     //光检衰减度
    public static final int ADDR_LIGHT_DIRRECTION          = 9;     //光检方向
    public static final int ADDR_OPEN_DOOR_TIMEOUT         = 10;     //等待顾客开门超时时间
    public static final int ADDR_LOCK_DOOR_TIMEOUT         = 11;     //顾客关门后，锁门等待时间
    public static final int ADDR_CURRENT_MAX_1          = 12;     //电机1最大电流
    public static final int ADDR_CURRENT_MIN_1          = 13;     //电机1最小电流
    public static final int ADDR_CURRENT_BREAK_1          = 14;     //电机1断开电流
    public static final int ADDR_CURRENT_MAX_2          = 15;     //电机2最大电流
    public static final int ADDR_CURRENT_MIN_2          = 16;     //电机2最小电流
    public static final int ADDR_CURRENT_BREAK_2          = 17;     //电机2断开电流
    public static final int ADDR_CURRENT_MAX_3          = 18;     //电机3最大电流
    public static final int ADDR_CURRENT_MIN_3          = 19;     //电机3最小电流
    public static final int ADDR_CURRENT_BREAK_3          = 20;     //电机3断开电流
    public static final int ADDR_CURRENT_MAX_4          = 21;     //电机4最大电流
    public static final int ADDR_CURRENT_MIN_4          = 22;     //电机4最小电流
    public static final int ADDR_CURRENT_BREAK_4          = 23;     //电机4断开电流
    public static final int ADDR_CURRENT_MAX_5          = 24;     //电机5最大电流
    public static final int ADDR_CURRENT_MIN_5          = 25;     //电机5最小电流
    public static final int ADDR_CURRENT_BREAK_5          = 26;     //电机5断开电流
    public static final int ADDR_LIFTER_TYPE          = 27;     //1:弹簧机 2:坡带机
    public static final int ADDR_SHIP_DETECT          = 28;     //1:表示有出货检测 2:表示没有出货检测
    public static final int ADDR_MAX_VOLTAGE          = 29;     //升降机最大电压
    public static final int ADDR_RISE_BUFFER_STEP     = 30;     //上升缓冲步数
    public static final int ADDR_RISE_BUFFER_SPEED     = 31;     //上升缓冲速度
    public static final int ADDR_GODOWN_SPEED         = 32;     //下降速度
    public static final int ADDR_DOWN_DEC_SPEED         = 33;     //下降速度步数
    public static final int ADDR_BACK_SPEED              = 34;     //回原点速度
    public static final int ADDR_BACK_STEP              = 35;     //回原点步数
    public static final int ADDR_LEAVE_LIGHT_SPEED     = 36;     //离开光检速度
    public static final int ADDR_SHIP_FAULT_DOWN_STEP     = 37;     //出货口卡货向下运行步数filter
    public static final int ADDR_SHIP_FAULT_UP_STEP     = 38;     //出货口卡货向上运行步数
    public static final int ADDR_LIGTH_FILTER_TIME     = 39;     //升降机光检滤波时间
    public static final int ADDR_LIGTH_OUT_STEP_MAX     = 40;     //升降机最高高出光检步数(通过该参数判断该层有没有光检)
    public static final int ADDR_LIGTH_BLOCK_TIME_MAX     = 41;     //正常出货挡住光检的最大时间，超过该时间，认为卡货
    public static final int ADDR_ANTI_THEFT_BOARD     = 42;     //0:表示有防盗推板  1:表示没有防盗推板
    public static final int ADDR_TEMPERATURE         = 43;     //当前温度（单位0.1℃）
    public static final int ADDR_LIGHT_TRACK_SHIP_CLEAR         = 44;     //履带出货光检清零功能 1：表示使能
    public static final int ADDR_DOOR_SWITCH         = 45;     //柜门门控开关 1：表示有   0：表示没有

    /******************* lift end *************************************************************/

}
