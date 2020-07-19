package com.example.myapplication.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.com.tcn.sdk.springdemo.LoadingDialog;
import com.com.tcn.sdk.springdemo.OutDialog;
import com.com.tcn.sdk.springdemo.TcnUtilityUI;
import com.com.tcn.sdk.springdemo.controller.VendService;
import com.dwin.navy.serialportapi.com_zhongji;
import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.InitActivity;
import com.example.myapplication.adapter.SettingAdapter;
import com.example.myapplication.adapter.SettingAdapter1;
import com.example.myapplication.bean.SettingBean;
import com.example.myapplication.bean.SheBeiBean;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.MyGridView;
import com.google.gson.Gson;
import com.tcn.springboard.control.PayMethod;
import com.tcn.springboard.control.TcnShareUseData;
import com.tcn.springboard.control.TcnVendEventID;
import com.tcn.springboard.control.TcnVendEventResultID;
import com.tcn.springboard.control.TcnVendIF;
import com.tcn.springboard.control.VendEventInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android_serialport_api.sample.SerialPortPreferences;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * 这是系统设置页
 */
public class SettingFragment extends Fragment {
    public static final String TAG = "SettingFragment";
    @InjectView(R.id.tv_machineMessage)
    TextView tv_machineMessage;
    @InjectView(R.id.tv_daiJiManage)
    TextView tv_daiJiManage;
    @InjectView(R.id.tv_shopManage)
    TextView tv_shopManage;
    @InjectView(R.id.tv_serialPort)
    TextView tv_serialPort;
    @InjectView(R.id.tv_payment)
    TextView tv_payment;
    @InjectView(R.id.tv_machineNum)
    TextView tv_machineNum;
    @InjectView(R.id.tv_machineName)
    TextView tv_machineName;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_aisleModel)
    TextView tv_aisleModel;
    @InjectView(R.id.tv_operateWay)
    TextView tv_operateWay;
    @InjectView(R.id.ll_machine)
    LinearLayout ll_machine;
    @InjectView(R.id.ll_style1)
    LinearLayout ll_style1;
    @InjectView(R.id.ll_style2)
    LinearLayout ll_style2;
    @InjectView(R.id.ll_style3)
    LinearLayout ll_style3;
    @InjectView(R.id.ll_style4)
    LinearLayout ll_style4;
    @InjectView(R.id.btn_next)
    Button btn_next;
    @InjectView(R.id.ll_daiJi)
    LinearLayout ll_daiJi;
    @InjectView(R.id.ll_style_1)
    LinearLayout ll_style_1;
    @InjectView(R.id.ll_style_2)
    LinearLayout ll_style_2;
    @InjectView(R.id.ll_style_3)
    LinearLayout ll_style_3;
    @InjectView(R.id.btn_next1)
    Button btn_next1;
    @InjectView(R.id.ll_shopping)
    LinearLayout ll_shopping;
    @InjectView(R.id.gv0)
    MyGridView gv0;
    @InjectView(R.id.ll_tu)
    LinearLayout ll_tu;
    @InjectView(R.id.gv1)
    MyGridView gv1;
    @InjectView(R.id.ll_video)
    LinearLayout ll_video;
    @InjectView(R.id.btn_Confirm)
    Button btn_confirm;
    @InjectView(R.id.ll_serialPort)
    LinearLayout ll_serialPort;
    @InjectView(R.id.ll_payment)
    LinearLayout ll_payment;
    @InjectView(R.id.tv_serialport)
    TextView tv_serialport;
    @InjectView(R.id.tv_money)
    TextView tv_money;
    @InjectView(R.id.tv_aisleTest)
    TextView tv_aisleTest;
    @InjectView(R.id.btn_testSerial)
    Button btn_testSerial;
    @InjectView(R.id.btn_testMoney)
    Button btn_testMoney;
    @InjectView(R.id.btn_testAisle)
    Button btn_testAisle;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.switch_xianJin)
    Switch switchXianJin;
    @InjectView(R.id.switch_wechat)
    Switch switchWeChat;
    @InjectView(R.id.switch_alipay)
    Switch switchAlipay;
    @InjectView(R.id.switch_unionpay)
    Switch switchUnionpay;
    @InjectView(R.id.switch_kaquan)
    Switch switchKaQuan;
    @InjectView(R.id.btn_return)
    Button btn_return;
    @InjectView(R.id.tv_aisleTest_1)
    TextView tv_aisleTest1;
    @InjectView(R.id.btn_testAisle_1)
    Button btn_testAisle1;
    @InjectView(R.id.tv_aisleTest_2)
    TextView tv_aisleTest2;
    @InjectView(R.id.btn_testAisle_2)
    Button btn_testAisle2;
    @InjectView(R.id.et_returnTime)
    EditText et_returnTime;
    @InjectView(R.id.tv_s)
    TextView tv_s;
    @InjectView(R.id.tv_spZhi)
    TextView tv_spZhi;
    @InjectView(R.id.tv_mZhi)
    TextView tv_mZhi;
    @InjectView(R.id.tv_atZhi)
    TextView tv_atZhi;
    @InjectView(R.id.tv_atZhi_1)
    TextView tv_atZhi1;
    @InjectView(R.id.tv_atZhi_2)
    TextView tv_atZhi2;
    @InjectView(R.id.btn_closeApp)
    Button btn_closeApp;

    private int style, style1;
    private List<SettingBean> list, list1, setList, setVideoList, setList1, setVideoList1;
    private SettingAdapter adapter;
    private SettingAdapter1 adapter1;
    private SharedPreferences spf;
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private SharedPreferences.Editor editor;
    private int flag;
    private String key;
    private com_zhongji zhongjiSerial;

    public SettingFragment() {
    }

    private Logger gLogger;

    public void configLog() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("SettingFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, view);
        configLog();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        editor = spf.edit();
        int sign = Comment.SIGN;
        gLogger.debug("跳转的sign:" + sign);
        if (sign == 1) {
            onTv_serialPortClicked();
            Comment.SIGN = 0;
            editor.putInt("sign", 0);
            editor.commit();

        } else {
            init();
        }
        gLogger.debug("sign:" + Comment.SIGN);
//        btn_testSerial.setEnabled(false);
        btn_testMoney.setEnabled(false);
//        btn_testAisle.setEnabled(false);
        btn_testAisle1.setEnabled(false);
        btn_testAisle2.setEnabled(false);
        tv_serialport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_testSerial.setEnabled(true);
                Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
                intent.putExtra("serial", 0);
                startActivity(intent);
                getActivity().finish();
            }
        });
        tv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_testMoney.setEnabled(true);
                Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
                intent.putExtra("serial", 1);
                startActivity(intent);
                getActivity().finish();
            }
        });
        tv_aisleTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_testAisle.setEnabled(true);
                Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
                intent.putExtra("serial", 2);
                startActivity(intent);
                getActivity().finish();
            }
        });
        tv_aisleTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_testAisle1.setEnabled(true);
                Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
                intent.putExtra("serial", 3);
                startActivity(intent);
                getActivity().finish();
            }
        });
        tv_aisleTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_testAisle2.setEnabled(true);
                Intent intent = new Intent(getActivity(), SerialPortPreferences.class);
                intent.putExtra("serial", 4);
                startActivity(intent);
                getActivity().finish();
            }
        });
        switchXianJin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("showXianJin", true);
                    editor.commit();
                } else {
                    editor.putBoolean("showXianJin", false);
                    editor.commit();
                }
            }
        });
        switchWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("showWeiXin", true);
                    editor.commit();
                } else {
                    editor.putBoolean("showWeiXin", false);
                    editor.commit();
                }
            }
        });
        switchAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("showAlipay", true);
                    editor.commit();
                } else {
                    editor.putBoolean("showAlipay", false);
                    editor.commit();
                }
            }
        });
        switchUnionpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("showUnionpay", true);
                    editor.commit();
                } else {
                    editor.putBoolean("showUnionpay", false);
                    editor.commit();
                }
            }
        });
        switchKaQuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("showKaQuan", true);
                    editor.commit();
                } else {
                    editor.putBoolean("showKaQuan", false);
                    editor.commit();
                }
            }
        });
        et_returnTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_s.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    private String devices = "";
    private int baudrates;

    @Override//设置串口的返回值
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            devices = data.getStringExtra("devices");
//            baudrates = Integer.parseInt(data.getStringExtra("baudrates"));
            Log.d("TAG", "onActivityResult: " + devices);
//            if (resultCode == 0) {
//                editor.putString("serialport", devices);
//                tv_spZhi.setText(devices);//显示读卡器串口号
//            } else if (resultCode == 1) {
//                editor.putString("serialport1", devices);
//                tv_mZhi.setText(devices);//显示现金串口号
//            } else if (resultCode == 2) {
//                editor.putString("serialport2", devices);
//                tv_atZhi.setText(devices);//显示主柜串口号
//                TcnShareUseData.getInstance().setBoardSerPortFirst(devices);    //此处主板串口接安卓哪个串口，就填哪个串口
//            } else if (resultCode == 3) {
//                editor.putString("serialport2_1", devices);
//                tv_atZhi1.setText(devices);//显示副柜1串口号
//            } else if (resultCode == 4) {
//                editor.putString("serialport2_2", devices);
//                tv_atZhi2.setText(devices);//显示副柜2串口号
//            }
//            editor.commit();
        }
    }

    int i = 0;

    @OnClick({R.id.btn_testSerial, R.id.btn_testMoney, R.id.btn_testAisle, R.id.btn_testAisle_1, R.id.btn_testAisle_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_testSerial:
//                String devices0 = spf.getString("devices0", "");
//                String baudrates0_1 = spf.getString("baudrates0", "");
//                Log.d(TAG, "onViewClicked0: " + devices0 + "++++++++" + baudrates0_1);
//                if (devices0 != null && !devices0.equals("") && !baudrates0_1.equals("")) {
//                    int baudrates0 = Integer.parseInt(baudrates0_1);
//                    SerialPortController.getInstance().openSerialPort(devices0, baudrates0);
////                TcnVendIF.getInstance().reqSlotNoInfoOpenSerialPort();
//                    String sendStr = "0200000010800000000600003C0000BECC01010000D803";
//                    byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
//                    String dataCmd = SerialPortController.getInstance().writeDataII(sendCmd);
//                    Log.d(TAG, "onViewClicked: " + dataCmd);
//                    if (dataCmd != null && !dataCmd.equals("")) {
//                        TcnUtility.getToast(getActivity(),"读卡器连接成功");
//                    } else {
//                        TcnUtility.getToast(getActivity(),"读卡器连接失败，请重新设置读卡器串口");
//                    }
//                    SerialPortController.getInstance().closeSerialPort();
//                } else {
//                    TcnUtility.getToast(getActivity(),"请先设置读卡器串口");
//                }
                String serialPort = spf.getString("serialport", "");
                Log.d("TAG", "onCreateView: " + serialPort);
                TcnUtility.getToast(getActivity(), serialPort);
                if (serialPort != null && !serialPort.equals("")) {
                    String substring = serialPort.substring(serialPort.length() - 2);
//                    Log.d("TAG", "onCreateView: " + substring);
                    zhongjiSerial = com_zhongji.getInstance(substring);
//                    zhongjiSerial.openSerialPort();
                    String xunKa = "0200000010800000000600003C0000BECC01010000D803";
                    String checkXunKa = zhongjiSerial.checkXunKa(xunKa);
                    if (checkXunKa != null && !checkXunKa.equals("")) {
                        TcnUtility.getToast(getActivity(), "读卡器连接成功");
                    } else {
                        TcnUtility.getToast(getActivity(), "读卡器连接失败，请重新设置读卡器串口");
                    }
                    zhongjiSerial.closeSerialPort();
                } else {
                    TcnUtility.getToast(getActivity(), "请先设置读卡器串口");
                }
                break;
            case R.id.btn_testMoney:
                String devices1 = spf.getString("devices1", "");
                String baudrates1_1 = spf.getString("baudrates1", "");
                Log.d(TAG, "onViewClicked1: " + devices1 + "++++++++++++++" + baudrates1_1);
                if (devices1 != null && !devices1.equals("") && !baudrates1_1.equals("")) {
                    int baudrates1 = Integer.parseInt(baudrates1_1);
                }
                break;

            case R.id.btn_testAisle://主柜
                String serialPort2 = spf.getString("serialport2", "");
                Log.d("TAG", "onCreateView: " + serialPort2);
                TcnUtility.getToast(getActivity(), serialPort2);
                if (serialPort2 != null && !serialPort2.equals("")) {
                    int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                    if (model_id == 1) {
                        if (TcnVendIF.getInstance().isServiceRunning()) {
                            Log.d(TAG, "onCreateView: +++++++++");
                        } else {
                            Log.d(TAG, "onCreateView: ___________");
                            getActivity().startService(new Intent(getContext(), VendService.class));
                        }
                        TcnShareUseData.getInstance().setBoardSerPortFirst(serialPort2);    //此处主板串口接安卓哪个串口，就填哪个串口
                        int slotNo = 1;//出货的货道号
                        String shipMethod = PayMethod.PAYMETHED_WECHAT; //出货方法,微信支付出货，此处自己可以修改。
                        String amount = "0.1";    //支付的金额（元）,自己修改
                        String tradeNo = "1811020095201811150126888" + i;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                        i = i + 1;
                        TcnVendIF.getInstance().reqShip(slotNo, shipMethod, amount, tradeNo);
                    } else if (model_id == 2) {
                        String substring = serialPort2.substring(serialPort2.length() - 2);
                        zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring, getContext());
                        String getId = "0101000000000000000000000000000000007188";
                        String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
                        if (chekChuHuo == null) {
                            TcnUtility.getToast(getActivity(), "货道串口设置错误，请从新设置货道串口");
                        } else {
                            TcnUtility.getToast(getActivity(), "返回的数据是：" + chekChuHuo);
                            String get = "0105000300000000000000000000000000007048";
                            String chekChu = zhongjiAisleSerial.checkChuHuo1(get);//启动马达
                            TcnUtility.getToast(getActivity(), "返回的数据是：" + chekChu);
                        }
                        zhongjiAisleSerial.closeSerialPort();
                    }
                } else {
                    TcnUtility.getToast(getActivity(), "请先设置货道串口");
                }
                Log.d(TAG, "onViewClicked: ++++++++++++++++++++++++++1" + this.i);
//                String devices2 = spf.getString("devices2", "");
//                String baudrates2_1 = spf.getString("baudrates2", "");
//                Log.d(TAG, "onViewClicked2: " + devices2 + "++++++++++++++" + baudrates2_1);
//                if (devices2 != null && !devices2.equals("") && !baudrates2_1.equals("")) {
//                    int baudrates2 = Integer.parseInt(baudrates2_1);
//                    SerialPortController.getInstance().openSerialPort(devices2, baudrates2);
////                TcnVendIF.getInstance().reqSlotNoInfoOpenSerialPort();
//                    byte[] bytes = Utils.get(1, 1);
//                    String data = SerialPortController.getInstance().writeDataI(bytes);
//                    Log.d(TAG, "onViewClicked: " + data);
//                    if(data == null){
//                        TcnUtility.getToast(getActivity(),"货道串口设置错误，请从新设置货道串口");
//                    }else{
//                        TcnUtility.getToast(getActivity(),"出货返回的数据是：" + data);
//                    }
//                    SerialPortController.getInstance().closeSerialPort();
//                } else {
//                    TcnUtility.getToast(getActivity(),"请先设置货道串口");
//                }
                break;
            //副柜的先不管
            case R.id.btn_testAisle_1://副柜1
                String serialPort2_1 = spf.getString("serialport2_1", "");
                Log.d("TAG", "onCreateView: " + serialPort2_1);
                if (serialPort2_1 != null && !serialPort2_1.equals("")) {
                    String substring = serialPort2_1.substring(serialPort2_1.length() - 2);
                    zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring, getContext());
                    String chekChuHuo = zhongjiAisleSerial.checkChuHuo(1);//前1:去除了第一位的货道号
                    if (chekChuHuo == null) {
                        TcnUtility.getToast(getActivity(), "货道串口设置错误，请从新设置货道串口");
                    } else {
                        TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekChuHuo);
                    }
                    zhongjiAisleSerial.closeSerialPort();
                } else {
                    TcnUtility.getToast(getActivity(), "请先设置货道串口");
                }
                break;
            case R.id.btn_testAisle_2://副柜2
                String serialport2_2 = spf.getString("serialport2_2", "");
                Log.d("TAG", "onCreateView: " + serialport2_2);
                if (serialport2_2 != null && !serialport2_2.equals("")) {
                    String substring = serialport2_2.substring(serialport2_2.length() - 2);
                    zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring, getContext());
                    String chekChuHuo = zhongjiAisleSerial.checkChuHuo(1);//前1:去除了第一位的货道号
                    if (chekChuHuo == null) {
                        TcnUtility.getToast(getActivity(), "货道串口设置错误，请从新设置货道串口");
                    } else {
                        TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekChuHuo);
                    }
                    zhongjiAisleSerial.closeSerialPort();
                } else {
                    TcnUtility.getToast(getActivity(), "请先设置货道串口");
                }
                break;
        }
    }

    private OutDialog m_OutDialog = null;
    private LoadingDialog m_LoadingDialog = null;
    /*
     * 此处监听底层发过来的数据，下面是显示相应操作结果
	 */
    private static int m_iEventID;

    public static int getM_iEventID() {
        return m_iEventID;
    }

    public static void setM_iEventID(int m_iEventID) {
        SettingFragment.m_iEventID = m_iEventID;
    }

    private VendListener m_vendListener = new VendListener();

    public class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            if (null == cEventInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
                return;
            }
            Log.d(TAG, "VendEvent: " + cEventInfo.m_iEventID);
            setM_iEventID(cEventInfo.m_iEventID);
            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.SERIAL_PORT_CONFIG_ERROR:
                    Log.i(TAG, "SERIAL_PORT_CONFIG_ERROR");
                    TcnUtilityUI.getToast(getActivity(), getString(R.string.error_seriport));
                    //打开串口错误，一般是串口配置出错
                    break;
                case TcnVendEventID.SERIAL_PORT_SECURITY_ERROR:
                    ///打开串口错误，一般是串口配置出错
                    break;
                case TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR:
                    //打开串口错误，一般是串口配置出错
                    break;
                case TcnVendEventID.COMMAND_SELECT_GOODS:  //选货成功
                    TcnUtilityUI.getToast(getActivity(), "选货成功");
                    break;
                case TcnVendEventID.COMMAND_INVALID_SLOTNO:
                    TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_notify_invalid_slot), 22).show();
                    break;
                case TcnVendEventID.COMMAND_SOLD_OUT:
                    if (cEventInfo.m_lParam1 > 0) {
                        TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_aisle_name) + cEventInfo.m_lParam1 + getString(R.string.ui_base_notify_sold_out));
                    } else {
                        TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_notify_sold_out));
                    }
                    break;
                case TcnVendEventID.COMMAND_FAULT_SLOTNO:
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.COMMAND_SHIPPING:    //正在出货
                    if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                        if (m_OutDialog == null) {
                            m_OutDialog = new OutDialog(getActivity(), String.valueOf(cEventInfo.m_lParam1), cEventInfo.m_lParam4);
                        } else {
                            m_OutDialog.setText(cEventInfo.m_lParam4);
                        }
                        m_OutDialog.cleanData();
                    } else {
                        if (m_OutDialog == null) {
                            m_OutDialog = new OutDialog(getActivity(), String.valueOf(cEventInfo.m_lParam1), getString(R.string.ui_base_notify_shipping));
                        } else {
                            m_OutDialog.setText(getActivity().getString(R.string.ui_base_notify_shipping));
                        }
                    }
                    m_OutDialog.setNumber(String.valueOf(cEventInfo.m_lParam1));
                    m_OutDialog.show();
                    break;

                case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:    //出货成功
                    if (null != m_OutDialog) {
                        m_OutDialog.cancel();
                    }
                    if (m_LoadingDialog == null) {
                        m_LoadingDialog = new LoadingDialog(getActivity(), getString(R.string.ui_base_notify_shipment_success), getString(R.string.ui_base_notify_receive_goods));
                    } else {
                        m_LoadingDialog.setLoadText(getString(R.string.ui_base_notify_shipment_success));
                        m_LoadingDialog.setTitle(getString(R.string.ui_base_notify_receive_goods));
                    }
                    m_LoadingDialog.setShowTime(3);
                    m_LoadingDialog.show();
                    break;
                case TcnVendEventID.COMMAND_SHIPMENT_FAILURE:    //出货失败
                    if (null != m_OutDialog) {
                        m_OutDialog.cancel();
                    }
                    if (null == m_LoadingDialog) {
                        m_LoadingDialog = new LoadingDialog(getActivity(), getString(R.string.ui_base_notify_shipment_fail), getString(R.string.ui_base_notify_contact_merchant));
                    }
                    m_LoadingDialog.setLoadText(getString(R.string.ui_base_notify_shipment_fail));
                    m_LoadingDialog.setTitle(getString(R.string.ui_base_notify_contact_merchant));
                    m_LoadingDialog.setShowTime(3);
                    m_LoadingDialog.show();
                    break;

                case TcnVendEventID.MDB_RECIVE_PAPER_MONEY:
                    String strBalance = TcnVendIF.getInstance().getBalance();  //余额
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4 + "元", 20).show();
                    break;
                case TcnVendEventID.MDB_RECIVE_COIN_MONEY:
                    strBalance = TcnVendIF.getInstance().getBalance();  //余额
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4 + "元", 20).show();
                    break;
                case TcnVendEventID.MDB_BALANCE_CHANGE:
                    strBalance = TcnVendIF.getInstance().getBalance();  //余额
                    break;
                case TcnVendEventID.MDB_PAYOUT_PAPERMONEY:
                    if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_START) {
                        TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_notify_coin_back), 20).show();
                    } else if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_END) {
                        if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                            TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                        } else {
                            TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                        }

                    } else {

                    }

                    break;
                case TcnVendEventID.MDB_PAYOUT_COINMONEY:   //退硬币
                    if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_START) {
                        TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_notify_coin_back), 20).show();
                    } else if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_END) {
                        if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                            TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                        } else {
                            TcnUtilityUI.getToast(getActivity(), getString(R.string.ui_base_notify_coinback_success), 20).show();
                        }

                    } else {

                    }
                    break;
                case TcnVendEventID.MDB_SHORT_CHANGE_PAPER:
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                    break;
                case TcnVendEventID.MDB_SHORT_CHANGE_COIN:  //确硬币
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                    break;
                case TcnVendEventID.MDB_SHORT_CHANGE:  //找零不足
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4, 20).show();
                    break;
                case TcnVendEventID.CMD_READ_DOOR_STATUS:  //门动作上报
                    if (TcnVendEventResultID.DO_CLOSE == cEventInfo.m_lParam1) {   //关门
                        TcnUtilityUI.getToast(getActivity(), "关门", 20).show();
                    } else if (TcnVendEventResultID.DO_OPEN == cEventInfo.m_lParam1) {   //开门
                        TcnUtilityUI.getToast(getActivity(), "开门", 20).show();
                    } else {

                    }
                    break;
                case TcnVendEventID.TEMPERATURE_INFO:   //cEventInfo.m_lParam4  ：温度
//					if (m_main_temperature != null) {
//						m_main_temperature.setText(cEventInfo.m_lParam4);
//					}
                    break;
                case TcnVendEventID.PROMPT_INFO:
                    TcnUtilityUI.getToast(getActivity(), cEventInfo.m_lParam4);
                    break;
                default:
                    break;
            }
        }
    }

    private long timeStamp;
    private String terminal_no;//终端编号（设备定义的）

    //默认选中第一个
    public void init() {
        ll_machine.setVisibility(View.VISIBLE);
        ll_daiJi.setVisibility(View.GONE);
        ll_shopping.setVisibility(View.GONE);
        ll_serialPort.setVisibility(View.GONE);
        ll_payment.setVisibility(View.GONE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_machineMessage.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_machineMessage.setTextColor(getResources().getColor(R.color.white));
        SheBeiBean bean = (SheBeiBean) getActivity().getIntent().getSerializableExtra("shebei");
        if (bean != null) {
            terminal_no = bean.getTerminal_no();
            tv_machineNum.setText(terminal_no);//设备编号
            tv_machineName.setText(bean.getTerminal_name());//设备名称
            tv_address.setText(bean.getTerminal_address());//设备地址
            tv_aisleModel.setText(bean.getAisle_model());//货道型号
        }
        String returnTime = spf.getString("returnTime", "");
        if (!returnTime.equals("")) {
            tv_s.setVisibility(View.VISIBLE);
        } else {
            tv_s.setVisibility(View.GONE);
        }
        et_returnTime.setText(returnTime);//显示设置的返回时间

        tv_spZhi.setText(Comment.SERIALPORT);//显示读卡器串口号
        tv_mZhi.setText(Comment.SERIALPORT1);//显示现金串口号
        tv_atZhi.setText(Comment.SERIALPORT2);//显示主柜串口号
        tv_atZhi1.setText(Comment.SERIALPORT2_1);//显示副柜1串口号
        tv_atZhi2.setText(Comment.SERIALPORT2_2);//显示副柜2串口号
    }

    //设置图片数据
    private void setImgData() {
        list = new ArrayList<>();
        if (flag == 2) {
            setList = new ArrayList<>();//销售页用
        } else {
            setList1 = new ArrayList<>();//待机页用
        }
        String img[] = {"https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg",
//                "https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&" +
//                        "sec=1538210263432&di=a6a8805d23c139db0d6fb2d3847868dd&imgtype" +
//                        "=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F91%2F71%2F10E58PIC5zS_1024.jpg",
//                "https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg",
//                "https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&" +
//                        "sec=1538210263432&di=a6a8805d23c139db0d6fb2d3847868dd&imgtype" +
//                        "=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F91%2F71%2F10E58PIC5zS_1024.jpg",
//                "https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg",
//                "https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&" +
//                        "sec=1538210263432&di=a6a8805d23c139db0d6fb2d3847868dd&imgtype" +
//                        "=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F91%2F71%2F10E58PIC5zS_1024.jpg"
        };
        for (int i = 0; i < 5; i++) {
            SettingBean bean = new SettingBean();
            bean.setImgName("图片" + i);
            bean.setImgUrl(img[0]);
            list.add(bean);
        }
        adapter = new SettingAdapter(list, getActivity());
        gv0.setAdapter(adapter);

        gv0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSeclection(position);
                LinearLayout ll_img = (LinearLayout) view.findViewById(R.id.ll_img);
                if (!ll_img.isSelected()) {
                    ll_img.setSelected(true);
                    ll_img.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
                    if (flag == 2) {
                        setList.add(list.get(position));
                    } else {
                        setList1.add(list.get(position));
                    }
                } else {
                    ll_img.setSelected(false);
                    ll_img.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
                    if (flag == 2) {
                        setList.remove(list.get(position));
                    } else {
                        setList1.remove(list.get(position));
                    }
                }
                btn_confirm.setText("确认");
            }
        });
    }

    //设置视频数据
    private void setVideoData() {
        list1 = new ArrayList<>();
        setVideoList = new ArrayList<>();
        String video[] = {"http://www.ysuad.com/video.mp4",
                "http://video.hemuyingshi.com/1f8630514dca4bd989ca36d9fc97a803/" +
                        "0c6265251db44a6f924e89a25cc37cc5-5456d705cfd07e668f702e78be66cb6f.mp4",
                "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                "http://www.ysuad.com/video.mp4",
                "http://video.hemuyingshi.com/1f8630514dca4bd989ca36d9fc97a803/" +
                        "0c6265251db44a6f924e89a25cc37cc5-5456d705cfd07e668f702e78be66cb6f.mp4",};
        for (int i = 0; i < video.length; i++) {
            SettingBean bean = new SettingBean();
            bean.setImgName("视频" + i);
            bean.setImgUrl(video[i]);
            list1.add(bean);
        }
        adapter1 = new SettingAdapter1(list1, getActivity());
        gv1.setAdapter(adapter1);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.setSeclection(position);
                setVideoList.clear();
                setVideoList.add(list1.get(position));
                Log.d(TAG, "onItemClick: " + setVideoList);
                adapter1.notifyDataSetChanged();
                btn_confirm.setText("确认");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //初始化选择背景颜色
    private void initSelectBackground() {
        tv_machineMessage.setBackgroundColor(Color.WHITE);
        tv_daiJiManage.setBackgroundColor(Color.WHITE);
        tv_shopManage.setBackgroundColor(Color.WHITE);
        tv_serialPort.setBackgroundColor(Color.WHITE);
        tv_payment.setBackgroundColor(Color.WHITE);

        tv_machineMessage.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_daiJiManage.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_shopManage.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_serialPort.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_payment.setTextColor(getResources().getColor(R.color.main_top_tab_color));
    }

    //初始化DaiJi样式背景颜色
    private void initDaiJiStyleBackground() {
        ll_style1.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        ll_style2.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        ll_style3.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        ll_style4.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
    }

    //初始化销售页样式背景颜色
    private void initShoppingStyleBackground() {
        ll_style_1.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        ll_style_2.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        ll_style_3.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
    }

    @OnClick(R.id.tv_machineMessage)//设备信息
    public void onTvMachineMessageClicked() {
        ll_machine.setVisibility(View.VISIBLE);
        ll_daiJi.setVisibility(View.GONE);
        ll_shopping.setVisibility(View.GONE);
        ll_serialPort.setVisibility(View.GONE);
        ll_payment.setVisibility(View.GONE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_machineMessage.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_machineMessage.setTextColor(getResources().getColor(R.color.white));
    }

    @OnClick(R.id.tv_daiJiManage)//待机页管理
    public void onTvDaiJiManageClicked() {
        ll_machine.setVisibility(View.GONE);
        ll_daiJi.setVisibility(View.VISIBLE);
        ll_shopping.setVisibility(View.GONE);
        ll_serialPort.setVisibility(View.GONE);
        ll_payment.setVisibility(View.GONE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_daiJiManage.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_daiJiManage.setTextColor(getResources().getColor(R.color.white));
        flag = 1;
        style1 = 1;
    }

    @OnClick(R.id.tv_shopManage)//销售页管理
    public void onTvShopManageClicked() {
        ll_machine.setVisibility(View.GONE);
        ll_daiJi.setVisibility(View.GONE);
        ll_shopping.setVisibility(View.VISIBLE);
        ll_serialPort.setVisibility(View.GONE);
        ll_payment.setVisibility(View.GONE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_shopManage.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_shopManage.setTextColor(getResources().getColor(R.color.white));
        flag = 2;
        style = 1;
    }

    @OnClick(R.id.tv_serialPort)//串口设置
    public void onTv_serialPortClicked() {
        ll_machine.setVisibility(View.GONE);
        ll_daiJi.setVisibility(View.GONE);
        ll_shopping.setVisibility(View.GONE);
        ll_serialPort.setVisibility(View.VISIBLE);
        ll_payment.setVisibility(View.GONE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_serialPort.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_serialPort.setTextColor(getResources().getColor(R.color.white));

        tv_spZhi.setText(spf.getString("serialport", ""));//显示读卡器串口号
        tv_mZhi.setText(spf.getString("serialport1", ""));//显示现金串口号
        tv_atZhi.setText(spf.getString("serialport2", ""));//显示主柜串口号
        tv_atZhi1.setText(spf.getString("serialport2_1", ""));//显示副柜1串口号
        tv_atZhi2.setText(spf.getString("serialport2_2", ""));//显示副柜2串口号
    }

    @OnClick(R.id.tv_payment)//支付方式设置
    public void onTv_paymentClicked() {
        ll_machine.setVisibility(View.GONE);
        ll_daiJi.setVisibility(View.GONE);
        ll_shopping.setVisibility(View.GONE);
        ll_serialPort.setVisibility(View.GONE);
        ll_payment.setVisibility(View.VISIBLE);
        ll_tu.setVisibility(View.GONE);
        ll_video.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        initSelectBackground();
        tv_payment.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_payment.setTextColor(getResources().getColor(R.color.white));
        boolean showXianJin = spf.getBoolean("showXianJin", false);
        Log.d(TAG, "onTv_paymentClicked: " + showXianJin);
        switchXianJin.setChecked(showXianJin);
        boolean showWeiXin = spf.getBoolean("showWeiXin", false);
        switchWeChat.setChecked(showWeiXin);
        boolean showAlipay = spf.getBoolean("showAlipay", false);
        switchAlipay.setChecked(showAlipay);
        boolean showUnionpay = spf.getBoolean("showUnionpay", false);
        switchUnionpay.setChecked(showUnionpay);
        boolean showKaQuan = spf.getBoolean("showKaQuan", false);
        switchKaQuan.setChecked(showKaQuan);
    }

    @OnClick(R.id.ll_style1)//待机样式一
    public void onLlStyle1Clicked() {
        initDaiJiStyleBackground();
        ll_style1.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style1 = 1;
    }

    @OnClick(R.id.ll_style2)//待机样式二
    public void onLlStyle2Clicked() {
        initDaiJiStyleBackground();
        ll_style2.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style1 = 2;
    }

    @OnClick(R.id.ll_style3)//待机样式三
    public void onLlStyle3Clicked() {
        initDaiJiStyleBackground();
        ll_style3.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style1 = 3;
    }

    @OnClick(R.id.ll_style4)//待机样式四
    public void onLlStyle4Clicked() {
        initDaiJiStyleBackground();
        ll_style4.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style1 = 4;
    }

    @OnClick(R.id.btn_next)//待机页按钮
    public void onBtnNextClicked() {
//        if (style1 != 1) {
//            setVideoData();
//            ll_video.setVisibility(View.VISIBLE);
//        } else {
//            ll_video.setVisibility(View.GONE);
//        }
//        if (style1 != 4) {
//            setImgData();
//            ll_tu.setVisibility(View.VISIBLE);
//        } else {
//            ll_tu.setVisibility(View.GONE);
//        }
//        ll_daiJi.setVisibility(View.GONE);
//        btn_confirm.setVisibility(View.VISIBLE);
//        btn_confirm.setText("返回");
        String sty = null;
        if (style1 == 1) {
            sty = "一";
        } else if (style1 == 2) {
            sty = "二";
        } else if (style1 == 3) {
            sty = "三";
        } else if (style1 == 4) {
            sty = "四";
        }
        TcnUtility.getToast(getActivity(), "选择的是样式" + sty);
        editor.putInt("style1", style1);
        editor.commit();
    }

    @OnClick(R.id.ll_style_1)//销售样式一
    public void onLlStyle_1Clicked() {
        initShoppingStyleBackground();
        ll_style_1.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style = 1;
        btn_next1.setText("确认");
    }

    @OnClick(R.id.ll_style_2)//销售样式二
    public void onLlStyle_2Clicked() {
        initShoppingStyleBackground();
        ll_style_2.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style = 2;
//        btn_next1.setText("下一步");
    }

    @OnClick(R.id.ll_style_3)//销售样式三
    public void onLlStyle_3Clicked() {
        initShoppingStyleBackground();
        ll_style_3.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
        style = 3;
//        btn_next1.setText("下一步");
    }

    @OnClick(R.id.btn_next1)//销售页按钮
    public void onBtnNext1Clicked() {
//        if (style == 1) {
//            ll_tu.setVisibility(View.GONE);
//            ll_video.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.GONE);
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.putExtra("styleShopping", style);
//            startActivity(intent);
//            editor.putInt("style", style);
//            editor.commit();
//            getActivity().finish();
//            Log.d("TAG", "onBtnConfirmClicked: 选择的是样式" + style);
//        } else if (style == 2) {
//            setVideoData();
//            ll_video.setVisibility(View.VISIBLE);
//            ll_tu.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.VISIBLE);
//            btn_confirm.setText("返回");
//            ll_shopping.setVisibility(View.GONE);
//        } else if (style == 3) {
//            setImgData();
//            ll_tu.setVisibility(View.VISIBLE);
//            ll_video.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.VISIBLE);
//            btn_confirm.setText("返回");
//            ll_shopping.setVisibility(View.GONE);
//        }
        String sty = null;
        if (style == 1) {
            sty = "一";
        } else if (style == 2) {
            sty = "二";
        } else if (style == 3) {
            sty = "三";
        }
        //返回时间设置
        String returnTime = et_returnTime.getText().toString();
        if (returnTime != null && !returnTime.equals("")) {
            et_returnTime.setSelection(et_returnTime.getText().toString().length());
            Log.d(TAG, "onBtnNext1Clicked: " + et_returnTime.getText().toString());
            editor.putInt("style", style);
            editor.putString("returnTime", returnTime);
            editor.commit();
            TcnUtility.getToast(getActivity(), "选择的是样式" + sty);
        } else {
            TcnUtility.getToast(getActivity(), "请输入返回时间");
        }
    }

    @OnClick(R.id.btn_Confirm)
    public void onBtnConfirmClicked() {
        Log.d("TAG", "onBtnConfirmClicked: 选择的是样式" + style);
        String confirm = btn_confirm.getText().toString();
        if (confirm.equals("返回")) {
            if (flag == 1) {
                onTvDaiJiManageClicked();
            } else {
                onTvShopManageClicked();
            }
        } else {
            if (flag == 1) {
                onTvDaiJiManageClicked();
            } else {
                editor.putInt("style", style);
                Gson gson = new Gson();
                String setlist = gson.toJson(setList);
                editor.putString("setList", setlist);
                String setvideoList = gson.toJson(setVideoList);
                editor.putString("setVideoList", setvideoList);
                editor.commit();
                onTvShopManageClicked();
            }
        }
    }

    @OnClick(R.id.btn_return)
    public void onClick() {
        Intent intent = new Intent(getActivity(), InitActivity.class);
        intent.putExtra("styleDaiJi", style1);
//        intent.putExtra("setListNum", setList1.size());
//        intent.putExtra("setVideoList", (Serializable) setVideoList1);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.btn_closeApp)
    public void onClick1() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //终端签到
    //    String param = "terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    private void initSignin(final int flag) {
        String url = Comment.URL + "/terminal/signin";
        //秒级时间戳获取
        timeStamp = getTimeStamp();
        Log.d("xxxxx", timeStamp + "");
        //auth_sign MD5加密
        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        params.put("auth_sign", ciphertext);//签名

        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                Log.d("TAG", "onResponse:失败" + params);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    key = jsonObject.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag == 0) {

                } else if (flag == 1) {

                } else if (flag == 2) {
                }
            }
        });
    }

    //秒级时间戳获取
    private long getTimeStamp() {
        //秒级时间戳获取
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    //获取屏幕分辨率：宽
    public int getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) getActivity().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)
//        Log.e("12", width + "======" + height);
//        Log.e("123", screenWidth + "======" + screenHeight);
        return width;
    }
}
