package com.example.myapplication.fragment.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.adapter.HuoDaoGridViewAdapter;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.MyGridView;
import com.example.myapplication.util.Utils;
import com.tcn.springboard.control.PayMethod;
import com.tcn.springboard.control.TcnShareUseData;
import com.tcn.springboard.control.TcnVendIF;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * A simple {@link Fragment} subclass.
 * 这是货道测试页
 */
public class HuoDaoFragment extends Fragment {
    public static final String TAG = "HuoDaoFragment";
    @InjectView(R.id.tv_a)
    TextView tv_a;
    @InjectView(R.id.tv_b)
    TextView tv_b;
    @InjectView(R.id.tv_c)
    TextView tv_c;
    @InjectView(R.id.tv_d)
    TextView tv_d;
    @InjectView(R.id.tv_e)
    TextView tv_e;
    @InjectView(R.id.tv_f)
    TextView tv_f;
    @InjectView(R.id.gv)
    MyGridView gv;
    @InjectView(R.id.btn_ceshi)
    Button btn_ceshi;
    private List<String> dataList;
    private HuoDaoGridViewAdapter adapter;
    private int aisleNum;//货道号
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private int j;//girdView item数量用
    private int firstNum;//货道号第一个数字
    private Runnable runnable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_huo_dao, container, false);
        ButterKnife.inject(this, view);
        configLog();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        editor = spf.edit();
        btn_ceshi.setEnabled(false);
        setData();
        return view;
    }

    public void initHuoDao() {
        initHuoGuiBackground();
        tv_a.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_a.setTextColor(getResources().getColor(R.color.white));
        tv_d.setVisibility(View.GONE);
        tv_e.setVisibility(View.GONE);
        tv_f.setVisibility(View.GONE);
        firstNum = 1;
        setData();
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
        gLogger = Logger.getLogger("HuoDaoFragment");
    }
    public void stopSerialPort(){
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        boolean isOpen = spf.getBoolean("isOpen", false);
        Log.d(TAG, "stopSerialPort: "+isOpen);
        gLogger.debug("串口是否打开"+isOpen);
        if(isOpen){
            handler.removeCallbacks(runnable);
            zhongjiAisleSerial.closeSerialPort();
            editor.putBoolean("isOpen",false);
            editor.commit();
        }
    }

    private int i = 0;

    @OnClick({R.id.tv_a, R.id.tv_b, R.id.tv_c, R.id.tv_d, R.id.tv_e, R.id.tv_f, R.id.btn_ceshi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ceshi:
                String serialPort2 = null;
                if (firstNum == 1) {
                    serialPort2 = spf.getString("serialport2", "");
                } else if (firstNum == 2) {
                    serialPort2 = spf.getString("serialport2_1", "");
                } else if (firstNum == 3) {
                    serialPort2 = spf.getString("serialport2_2", "");
                }
                Log.d("TAG", "onCreateView: " + serialPort2);
                if (serialPort2 != null && !serialPort2.equals("")) {
                    int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                    if (model_id == 1) {
                        TcnShareUseData.getInstance().setBoardSerPortFirst(serialPort2);    //此处主板串口接安卓哪个串口，就填哪个串口
                        int slotNo = aisleNum-1;//出货的货道号
                        String shipMethod = PayMethod.PAYMETHED_WECHAT; //出货方法,微信支付出货，此处自己可以修改。
                        String amount = "0.1";    //支付的金额（元）,自己修改
                        String tradeNo = "1811020095201811150126888" + i;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                        i = i + 1;
                        TcnVendIF.getInstance().reqShip(slotNo, shipMethod, amount, tradeNo);
                    } else if (model_id == 2){
                        Log.d(TAG, "aaaaaaaaaaaaaaaa: "+aisleNum);
//                        TcnUtility.getToast(getContext(),"货道号："+aisleNum);
                        String substring = serialPort2.substring(serialPort2.length() - 2);
                        zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring,getContext());
//                    String getId = "0105000300000000000000000000000000007048";
//                        aisleNum = aisleNum-1;
                        editor.putBoolean("isOpen",true);
                        editor.commit();
                        String hexString = Utils.integerToHexString(aisleNum-1);
                        String data = "0105" + hexString + "03" + "00" + "00000000000000000000000000";//启动电机  03：三线电机  00：无光幕 aisleNum:货道号（电机号）
                        byte[] bytes = Utils.hexStr2Byte(data);
                        int crc16Check = Utils.CRC16_Check(bytes, bytes.length);
                        String crc = Utils.integerToHexString(crc16Check);
                        String crc1 = null;
                        String crc2 = null;
                        if(crc.length() == 4){
                            crc1= crc.substring(0, 2);
                            crc2 = crc.substring(2, 4);
                        }else{
                            crc1 = "00";
                            crc2= crc.substring(0, 2);
                        }
                        String getMachine = data + crc2 + crc1;
                        String chekMachine = zhongjiAisleSerial.checkChuHuo1(getMachine);//启动马达
                        if (chekMachine == null) {
                            TcnUtility.getToast(getActivity(), "货道串口设置错误，请从新设置货道串口");
                            zhongjiAisleSerial.closeSerialPort();
                            editor.putBoolean("isOpen",false);
                            editor.commit();
                        } else {
//                            TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekMachine);
                            String s = chekMachine.substring(4, 6);
                            if (s.equals("00")) {
//                                TcnUtility.getToast(getActivity(), "已启动");
                                btn_ceshi.setEnabled(false);
                                //1秒后发送获取电机状态
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        //定时向后台获取订单状态
                                        handler.postDelayed(this, 1000);
                                        String check = "010300000000000000000000000000000000D0E8";
                                        String query = zhongjiAisleSerial.checkChuHuo1(check);//获取马达旋转结果
//                                        TcnUtility.getToast(getActivity(), "返回的数据是：" + query);
                                        if (query != null && !query.equals("")) {
                                            String machineState = query.substring(4, 6);
                                            if (machineState.equals("02")) {
                                                handler.removeCallbacks(runnable);
                                                String result = query.substring(8, 10);
                                                if (result.equals("00")) {
                                                    TcnUtility.getToast(getActivity(), "出货成功");
                                                    btn_ceshi.setEnabled(true);
                                                } else {
                                                    TcnUtility.getToast(getActivity(), "出货失败");
                                                    btn_ceshi.setEnabled(true);
                                                }
                                                zhongjiAisleSerial.closeSerialPort();
                                                editor.putBoolean("isOpen",false);
                                                editor.commit();
                                            } else if (machineState.equals("01")) {
                                                TcnUtility.getToast(getActivity(), "出货中");
                                            }
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1000);//线程时间1秒刷新
                            } else if (s.equals("01")) {
                                TcnUtility.getToast(getActivity(), "无效的电机索引号");
                                zhongjiAisleSerial.closeSerialPort();
                                editor.putBoolean("isOpen",false);
                                editor.commit();
                            }else{
                                TcnUtility.getToast(getActivity(), "另一台电机在转");
                                zhongjiAisleSerial.closeSerialPort();
                                editor.putBoolean("isOpen",false);
                                editor.commit();
                            }
                        }
                    }
                } else {
                    TcnUtility.getToast(getActivity(), "请先设置货道串口");
                }

//                String devices2 = spf.getString("devices2", "");
//                String baudrates = spf.getString("baudrates2", "");
//                Log.d(TAG, "onViewClicked2: "+devices2+"++++++++++++++"+baudrates);
//                if (devices2 != null && !devices2.equals("") && !baudrates.equals("")) {
//                    int baudrates2 = Integer.parseInt(baudrates);
//                    SerialPortController.getInstance().openSerialPort(devices2,baudrates2);
//                    byte[] bytes = Utils.get(aisleNum, firstNum);
//                    String data = SerialPortController.getInstance().writeDataI(bytes);
//                    Log.d(TAG, "onViewClicked: " + data);
//                    if(data == null){
//                        TcnUtility.getToast(getActivity(),"货道串口设置错误，请重新设置货道串口");
//                    }else{
//                        TcnUtility.getToast(getActivity(),"出货返回的数据是：" + data);
//                    }
//                    SerialPortController.getInstance().closeSerialPort();
//                } else {
//                    TcnUtility.getToast(getActivity(),"请先设置货道串口");
//                }
                break;
            case R.id.tv_a:
                initHuoGuiBackground();
                tv_a.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_a.setTextColor(getResources().getColor(R.color.white));
                firstNum = 1;
                setData();
                break;
            case R.id.tv_b:
                initHuoGuiBackground();
                tv_b.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_b.setTextColor(getResources().getColor(R.color.white));
                firstNum = 2;
                setData();
                break;
            case R.id.tv_c:
                initHuoGuiBackground();
                tv_c.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_c.setTextColor(getResources().getColor(R.color.white));
                firstNum = 3;
                setData();
                break;
            case R.id.tv_d:
                initHuoGuiBackground();
                tv_d.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_d.setTextColor(getResources().getColor(R.color.white));
                firstNum = 4;
                setData();
                break;
            case R.id.tv_e:
                initHuoGuiBackground();
                tv_e.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_e.setTextColor(getResources().getColor(R.color.white));
                firstNum = 5;
                setData();
                break;
            case R.id.tv_f:
                initHuoGuiBackground();
                tv_f.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_f.setTextColor(getResources().getColor(R.color.white));
                firstNum = 6;
                setData();
                break;
        }
    }

    //设置货道数量 颜色不同是指有无货道
    private void setData() {
        dataList = new ArrayList<String>();
        int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
        if(model_id == 1){
            for (int i = 1; i < 61; i++) {
                if (i < 10) {
                    dataList.add("0" + i);
                } else {
                    dataList.add(i + "");
                }
            }
        }else if(model_id == 2){
            for (int i = 1; i < 61; i++) {
                if (i < 10) {
                    dataList.add("0" + i);
                } else {
                    dataList.add(i + "");
                }
            }
        }

        adapter = new HuoDaoGridViewAdapter(getActivity(), dataList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.btn);
                btn_ceshi.setEnabled(true);
                aisleNum = Integer.parseInt(tv.getText().toString());//货道号
                Toast.makeText(getActivity(), aisleNum + "", Toast.LENGTH_SHORT).show();
                adapter.setSeclection(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //初始化货柜背景颜色
    private void initHuoGuiBackground() {
        tv_a.setBackgroundColor(Color.WHITE);
        tv_b.setBackgroundColor(Color.WHITE);
        tv_c.setBackgroundColor(Color.WHITE);
        tv_d.setBackgroundColor(Color.WHITE);
        tv_e.setBackgroundColor(Color.WHITE);
        tv_f.setBackgroundColor(Color.WHITE);

        tv_a.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_b.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_c.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_d.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_e.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_f.setTextColor(getResources().getColor(R.color.main_top_tab_color));
    }
}
