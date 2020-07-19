package com.example.myapplication.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.bean.Order;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.Utils;
import com.tcn.springboard.control.TcnVendIF;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * 这是卡券提货页
 */
public class CouponPickingFragment extends Fragment {
    public Context context;
    public static final String TAG = "CouponPickingFragment";
    @InjectView(R.id.btn_7)
    Button btn7;
    @InjectView(R.id.btn_8)
    Button btn8;
    @InjectView(R.id.btn_9)
    Button btn9;
    @InjectView(R.id.btn_del)
    Button btnDel;
    @InjectView(R.id.btn_4)
    Button btn4;
    @InjectView(R.id.btn_5)
    Button btn5;
    @InjectView(R.id.btn_6)
    Button btn6;
    @InjectView(R.id.btn_0)
    Button btn0;
    @InjectView(R.id.btn_1)
    Button btn1;
    @InjectView(R.id.btn_2)
    Button btn2;
    @InjectView(R.id.btn_3)
    Button btn3;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;
    @InjectView(R.id.et_coupon)
    EditText et_coupon;
    private SharedPreferences spf;
    private String key;
    private String order_no;//订单号
    private String tunnel_no;//货道编号
    private String tunnel_code;//货道号
    private long timeStamp;
    private com_zhongji_aisle_test zhongjiAisleSerial;
    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig;
    private int order_status;//订单状态
    private int trade_status;//支付状态
    private int refund_status;//退款状态
    private int deliver_status;//出货状态
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
        View view = inflater.inflate(R.layout.fragment_coupon_and_picking, container, false);
        context = getActivity();
        ButterKnife.inject(this, view);
        //初始化数据库配置信息
        initDaoConfig();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        initSignin(-1);
        if (Build.VERSION.SDK_INT >= 11) {
            Class<EditText> cls = EditText.class;
            try {
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(false);
                setShowSoftInputOnFocus.invoke(et_coupon, false);
                setShowSoftInputOnFocus.invoke(et_coupon, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        et_coupon.setInputType(InputType.TYPE_NULL);
        return view;
    }

    @Override
    public void onResume() {
        /**
         * 设置为竖屏
         */
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        // 在activity结束的时候回收资源
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Toast.makeText(getActivity(), "播放停止", 0).show();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_del, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_confirm})
    public void onViewClicked(View view) {
        String s = et_coupon.getText().toString();
        switch (view.getId()) {
            case R.id.btn_7:
                String btn7 = this.btn7.getText().toString();
                et_coupon.setText(s + btn7);
                break;
            case R.id.btn_8:
                String btn8 = this.btn8.getText().toString();
                et_coupon.setText(s + btn8);
                break;
            case R.id.btn_9:
                String btn9 = this.btn9.getText().toString();
                et_coupon.setText(s + btn9);
                break;
            case R.id.btn_del:
                String coupon = et_coupon.getText().toString();
                String substring = null;
                if (coupon != null && !coupon.equals("")) {
                    substring = coupon.substring(0, coupon.length() - 1);
                    et_coupon.setText(substring);
                }
                break;
            case R.id.btn_4:
                String btn4 = this.btn4.getText().toString();
                et_coupon.setText(s + btn4);
                break;
            case R.id.btn_5:
                String btn5 = this.btn5.getText().toString();
                et_coupon.setText(s + btn5);
                break;
            case R.id.btn_6:
                String btn6 = this.btn6.getText().toString();
                et_coupon.setText(s + btn6);
                break;
            case R.id.btn_0:
                String btn0 = this.btn0.getText().toString();
                et_coupon.setText(s + btn0);
                break;
            case R.id.btn_1:
                String btn1 = this.btn1.getText().toString();
                et_coupon.setText(s + btn1);
                break;
            case R.id.btn_2:
                String btn2 = this.btn2.getText().toString();
                et_coupon.setText(s + btn2);
                break;
            case R.id.btn_3:
                String btn3 = this.btn3.getText().toString();
                et_coupon.setText(s + btn3);
                break;
            case R.id.btn_confirm:
                String deliver_code = et_coupon.getText().toString();//提货码
                if (deliver_code != null && !deliver_code.equals("")) {
                    queryOrder();
                }else{
                    TcnUtility.getToast(getActivity(),"请输入取货码");
                }
                break;
        }
        et_coupon.setSelection(et_coupon.getText().toString().length());
    }

    //查询订单
    int a;
    private void queryOrder() {
        final String deliver_code = et_coupon.getText().toString();//提货码
        Log.d(TAG, "onViewClicked: " + deliver_code);
        String url = Comment.URL+"/order/query";
//                String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("deliver_code", deliver_code);
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("deliver_code=" + deliver_code + "&terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "run: " + params);
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i=0;
                queryOrder();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功S" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        String err_desc = jsonObject.getString("err_desc");
                        if (err_desc.equals("验签错误")) {
                            if (a<2) {
                                i=0;
//                                initSignin(0);
                                a++;
                            } else {
                                TcnUtility.getToast(getActivity(),"请求超时");
                            }
                        } else {
                            TcnUtility.getToast(getActivity(), err_desc);
                        }
                    } else {
                        final JSONObject data = jsonObject.getJSONObject("data");
                        order_status = data.getInt("order_status");
                        if (order_status == 0) {
                            trade_status = data.getInt("trade_status");
                            if (trade_status == 1) {
                                refund_status = data.getInt("refund_status");
                                if (refund_status == 0) {
                                    deliver_status = data.getInt("deliver_status");
                                    if (deliver_status == 0) {
                                        order_no = data.getString("order_no");
                                        final String good_no = data.getString("good_no");
                                        int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                                        if (model_id == 1) {
                                            String tunnel = spf.getString("tunnel", "");
                                            try {
                                                JSONArray jsonArray = new JSONArray(tunnel);
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    if (object.getString("good_no").equals(good_no)) {
                                                        tunnel_no = object.getString("tunnel_no");
                                                        tunnel_code = object.getString("tunnel_code");
                                                        JSONObject good = object.getJSONObject("good");
                                                        String price = good.getString("price");//商品价格
                                                        String code1 = tunnel_code.substring(1);
                                                        String first = tunnel_code.substring(0, 1);
                                                        int tunnel_code = Integer.parseInt(code1);//除第一位剩下的数字
                                                        int slotNo = Integer.parseInt(code1);//出货的货道号 90788169
                                                        String shipMethod = "提货码"; //出货方法,微信支付出货，此处自己可以修改。
                                                        String amount = String.valueOf(price);    //支付的金额（元）,自己修改
                                                        String tradeNo = order_no;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                                                        TcnVendIF.getInstance().reqShip(slotNo,shipMethod,amount,tradeNo);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else if (model_id == 2) {
                                            final Dialog dialog = new Dialog(getActivity());
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setContentView(R.layout.pay_and_chu_huo);
                                            final TextView tv = dialog.findViewById(R.id.tv_tu);
                                            final TextView tv1 = dialog.findViewById(R.id.tv_tu1);
                                            final ImageView iv = dialog.findViewById(R.id.iv_tu);
                                            tv.setText("订单查询成功");
                                            tv1.setText("等待商品出货");
                                            iv.setImageResource(R.drawable.zhifuchenggong_jiazai);
                                            dialog.show();
                                            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                                            int width = getAndroiodScreenProperty();
                                            int height = getAndroiodScreenPropertyH();
                                            params.width = (int) (width * 0.65);
                                            params.height = (int) (height * 0.35);
                                            dialog.getWindow().setAttributes(params);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d(TAG, "run: ++++++");
//                                                String devices2 = spf.getString("devices2", "");
//                                                String baudrates2_1 = spf.getString("baudrates2", "");
//                                                Log.d(TAG, "onViewClicked2: " + devices2 + "++++++++++++++" + baudrates2_1);
//                                                if (devices2 != null && !devices2.equals("") && !baudrates2_1.equals("")) {
//                                                    int baudrates2 = Integer.parseInt(baudrates2_1);
//                                                    SerialPortController.getInstance().openSerialPort(devices2, baudrates2);
                                                    String tunnel = spf.getString("tunnel", "");
                                                    try {
                                                        JSONArray jsonArray = new JSONArray(tunnel);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject object = jsonArray.getJSONObject(i);
                                                            if (object.getString("good_no").equals(good_no)) {
                                                                tunnel_no = object.getString("tunnel_no");
                                                                tunnel_code = object.getString("tunnel_code");
                                                                JSONObject good = object.getJSONObject("good");
                                                                String price = good.getString("price");//商品价格
                                                                String code1 = tunnel_code.substring(1);
                                                                String first = tunnel_code.substring(0, 1);
                                                                int tunnel_code = Integer.parseInt(code1);//除第一位剩下的数字
                                                                int firstNum = Integer.parseInt(first);//货道号第一位数字
                                                                String serialPort2 = null;
                                                                //判断firstNum 是几，调对应的串口值  1：serialport2
                                                                if (firstNum == 1) {
                                                                    serialPort2 = spf.getString("serialport2", "");
                                                                    Log.d("TAG", "onCreateView: " + serialPort2);
                                                                }else if(firstNum == 2){
                                                                    serialPort2 = spf.getString("serialport2_1", "");
                                                                }else if(firstNum == 3){
                                                                    serialPort2 = spf.getString("serialport2_2", "");
                                                                }
                                                                if (serialPort2 != null && !serialPort2.equals("")) {
                                                                    String substring2 = serialPort2.substring(serialPort2.length() - 2);
                                                                    zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring2,getContext());
                                                                    String getId = "0101000000000000000000000000000000007188";
                                                                    String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
//                                                            String chekChuHuo = zhongjiAisleSerial.checkChuHuo(tunnel_code);
                                                                    Log.d(TAG, "onViewClicked: " + chekChuHuo);
                                                                    TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekChuHuo);
                                                                    if (chekChuHuo != null && !chekChuHuo.equals("")) {
                                                                        String data = "0105"+code1+"03"+"00"+"00000000000000000000000000";//启动电机  03：三线电机  00：无光幕 aisleNum:货道号（电机号）
                                                                        byte[] bytes = Utils.hexStr2Byte(data);
                                                                        int crc16Check = Utils.CRC16_Check(bytes, bytes.length);
                                                                        String crc = Utils.integerToHexString(crc16Check);
                                                                        String crc1 = crc.substring(0, 2);
                                                                        String crc2 = crc.substring(2, 4);
                                                                        String get = data+crc2+crc1;
                                                                        String chekMachine = zhongjiAisleSerial.checkChuHuo1(get);//启动马达
                                                                        String s = chekMachine.substring(4, 6);
                                                                        if(s.equals("00")){
                                                                            TcnUtility.getToast(getActivity(),"已启动");
                                                                            //持续查询电机状态
                                                                            check(tv, tv1, iv);
                                                                        }else if(s.equals("01")){
                                                                            TcnUtility.getToast(getActivity(),"无效的电机索引号");
                                                                            zhongjiAisleSerial.closeSerialPort();
                                                                        }
                                                                    } else {
                                                                        tv.setText("设备异常，出货失败");
                                                                        tv1.setText("系统将为您自动退款");
                                                                        iv.setImageResource(R.drawable.fail);
                                                                        play(0);
                                                                        order_status = -1;
                                                                        trade_status = 1;
                                                                        refund_status = 1;
                                                                        deliver_status = 0;
                                                                        orderCancle();
                                                                    }
//                                                            SerialPortController.getInstance().closeSerialPort();

                                                                    Log.d(TAG, "onResponse11111111: " + order_status +
                                                                            "++++" + trade_status + "++" + refund_status +
                                                                            "++++++++" + deliver_status);
                                                                    try {
                                                                        //根据配置信息获取操作数据的db对象
                                                                        DbManager db = x.getDb(daoConfig);
                                                                        Order order = new Order();
                                                                        order.setOrder_status(order_status);
                                                                        order.setTrade_status(trade_status);
                                                                        order.setRefund_status(refund_status);
                                                                        order.setDeliver_status(deliver_status);
                                                                        db.save(order);//插入一条数据

                                                                        //查询所有数据
                                                                        List<Order> all = db.findAll(Order.class);
                                                                        Log.i("tag", "所有数据:"+all.toString());
                                                                        try {
                                                                            Thread.sleep(3000);
                                                                            dialog.dismiss();
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    } catch (DbException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    TcnUtility.getToast(getActivity(), "请先设置货道串口");
                                                                }
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 500);//线程时间0.5秒
                                        }
                                    }
                                }
                            }
                        } else if (order_status == 1) {
                            int deliver_status = data.getInt("deliver_status");
                            if (deliver_status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                                Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                                TextView tv_message = aalayout.findViewById(R.id.tv_message);
                                tv_message.setText("提货码已用过，请勿重复使用");
                                builder.setCancelable(false)
                                        .setView(aalayout);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                                btn_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 初始化MediaPlayer 提示音
    private MediaPlayer mediaPlayer;
    private void play(int result) {
        try {
            if (result == 0) {//0：出货失败 1：出货成功
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.deliversuccess);
            } else if (result == 1){
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.deliverfail);
            }
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕 开始播放流媒体
                    mediaPlayer.start();
                    Toast.makeText(getActivity(), "开始播放", 0).show();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被调用
                    Toast.makeText(getActivity(), "播放完毕", 0).show();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 如果发生错误，重新播放
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(0);
                        Toast.makeText(getActivity(), "重新播放", 0).show();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "播放失败", 0).show();
        }
    }

    //持续查询电机状态
    private void check(final TextView tv, final TextView tv1, final ImageView iv) {
        //1秒后发送获取电机状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000);
                String check = "010300000000000000000000000000000000D0E8";
                String query = zhongjiAisleSerial.checkChuHuo1(check);//获取马达旋转结果
                if (query != null && !query.equals("")) {
                    String machineState = query.substring(4, 6);
                    if (machineState.equals("02")) {
                        handler.removeCallbacks(runnable);
                        String result = query.substring(8, 10);
                        if (result.equals("00")) {
                            tv.setText("出货成功，请取货");
                            tv1.setText("");
                            iv.setImageResource(R.drawable.chenggong);
                            play(1);
                            order_status = 1;
                            trade_status = 1;
                            refund_status = 0;
                            deliver_status = 1;
                            chuHuoConfirm();
                        } else {
                            tv.setText("设备异常，出货失败");
                            tv1.setText("系统将为您自动退款");
                            iv.setImageResource(R.drawable.fail);
                            play(0);
                            order_status = -1;
                            trade_status = 1;
                            refund_status = 1;
                            deliver_status = 0;
                            orderCancle();
                        }
                        zhongjiAisleSerial.closeSerialPort();
                    } else if (machineState.equals("01")) {
                        TcnUtility.getToast(getActivity(), "出货中");
                    }
                }
            }
        };
        handler.postDelayed(runnable, 1000);//线程时间1秒刷新
    }

    //出货确认
    private void chuHuoConfirm() {
        Log.d(TAG, "orderCancle: " + key);
        //出货确认
        String url = Comment.URL+"/order/deliver";
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&tunnel_no=" + tunnel_no + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                chuHuoConfirm();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseC:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //取消订单
    private void orderCancle() {
        Log.d(TAG, "orderCancle: " + key);
        //订单取消
        String url = Comment.URL+"/order/cancel";
//      String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i=0;
//                initSignin(1);
                orderCancle();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseC:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i=0;
//                        initSignin(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    //获取屏幕分辨率：高
    public int getAndroiodScreenPropertyH() {
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
        return height;
    }
    /**
     * 初始化获取数据库的配置信息
     */
    public void initDaoConfig() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("order.db")  //设置数据库名称
                .setDbVersion(1)  //设置数据库版本
                .setDbDir(null) //设置数据库保存的路径getContext().getCacheDir().getAbsoluteFile()
                .setAllowTransaction(true) //设置允许开启事务
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //db.addColumn("表名","需要增加的列名");数据库更新监听
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启WAL.对写入加速提示很大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("tag", "onTableCreated: 创建了表:" + table.getName());
                    }
                });
    }
    //终端签到
    //    String param = "terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    int flag1 = -1;
    int flag2 = -1;
    int i;
    private void initSignin(final int flag) {
        if (flag == -1) {
            flag1 = flag;
            Log.d(TAG, "initFlag: " + flag1);
        } else {
            flag2 = flag;
            Log.d(TAG, "initFlag: " + flag2);
        }
        String url = Comment.URL+"/terminal/signin";
        timeStamp = getTimeStamp();
        key = Comment.KEY;
        //auth_sign MD5加密
//        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
//        final Map<String, String> params = new HashMap<>();
//        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
//        params.put("auth_sign", ciphertext);//签名

//        if (i<2) {
//            OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//                    Log.d("TAG", "onResponse:失败" + e);
//                    Log.d("TAG", "onResponse:失败" + params);
//                    try {
//                        Thread.sleep(5000);
//                        initSignin(flag1);
//                        i++;
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    Log.d("TAG", "onResponse:成功" + response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        key = jsonObject.getString("key");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (flag2 == 0) {
//                        queryOrder();
//                    }else if(flag2 == 1){
//                        orderCancle();
//                    }
//                }
//            });
//        }else {
//            Log.d(TAG, "initSignin: " + i);
//            TcnUtility.getToast(getActivity(), "请求超时");
//        }
    }

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
        long timeStamp = date.getTime() / 1000;
        Log.d("xxxxx", timeStamp + "");
        return timeStamp;
    }

    public void init() {
        i=0;
        et_coupon.setText("");
    }
}
