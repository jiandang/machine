package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class CouponPickingActivity extends AppCompatActivity {

    private static final String TAG = "CouponPickingActivity";
    @InjectView(R.id.et_coupon)
    EditText et_coupon;
    @InjectView(R.id.btn1)
    Button btn1;
    @InjectView(R.id.btn2)
    Button btn2;
    @InjectView(R.id.btn3)
    Button btn3;
    @InjectView(R.id.btn4)
    Button btn4;
    @InjectView(R.id.btn5)
    Button btn5;
    @InjectView(R.id.btn6)
    Button btn6;
    @InjectView(R.id.btn7)
    Button btn7;
    @InjectView(R.id.btn8)
    Button btn8;
    @InjectView(R.id.btn9)
    Button btn9;
    @InjectView(R.id.btn0)
    Button btn0;
    @InjectView(R.id.btnQ)
    Button btnQ;
    @InjectView(R.id.btnW)
    Button btnW;
    @InjectView(R.id.btnE)
    Button btnE;
    @InjectView(R.id.btnR)
    Button btnR;
    @InjectView(R.id.btnT)
    Button btnT;
    @InjectView(R.id.btnY)
    Button btnY;
    @InjectView(R.id.btnU)
    Button btnU;
    @InjectView(R.id.btnI)
    Button btnI;
    @InjectView(R.id.btnO)
    Button btnO;
    @InjectView(R.id.btnP)
    Button btnP;
    @InjectView(R.id.btnA)
    Button btnA;
    @InjectView(R.id.btnS)
    Button btnS;
    @InjectView(R.id.btnD)
    Button btnD;
    @InjectView(R.id.btnF)
    Button btnF;
    @InjectView(R.id.btnG)
    Button btnG;
    @InjectView(R.id.btnH)
    Button btnH;
    @InjectView(R.id.btnJ)
    Button btnJ;
    @InjectView(R.id.btnK)
    Button btnK;
    @InjectView(R.id.btnL)
    Button btnL;
    @InjectView(R.id.btnDelete)
    ImageButton btnDelete;
    @InjectView(R.id.btnZ)
    Button btnZ;
    @InjectView(R.id.btnX)
    Button btnX;
    @InjectView(R.id.btnC)
    Button btnC;
    @InjectView(R.id.btnV)
    Button btnV;
    @InjectView(R.id.btnB)
    Button btnB;
    @InjectView(R.id.btnN)
    Button btnN;
    @InjectView(R.id.btnM)
    Button btnM;
    @InjectView(R.id.btnConfirm)
    ImageButton btnConfirm;
    @InjectView(R.id.vv)
    ImageView vv;
    private String key;
    private SharedPreferences spf;
    private String tunnel_code;//货道号
    private String tunnel_no;//货道编号
    private String goods_no;//商品编号
    private String order_no;//订单生成的时候返回的
    private String price;//商品价格
    private Runnable runnable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private long timeStamp;
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private int order_status;
    private int trade_status;
    private int refund_status;
    private int deliver_status;
    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_picking);
        ButterKnife.inject(this);
        //初始化数据库配置信息
        initSignin(-1);
        initDaoConfig();
        spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        tunnel_code = intent.getStringExtra("tunnel_code");
        tunnel_no = intent.getStringExtra("tunnel_no");
        goods_no = intent.getStringExtra("goods_no");
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
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0, R.id.btnQ, R.id.btnW,
            R.id.btnE, R.id.btnR, R.id.btnT, R.id.btnY, R.id.btnU, R.id.btnI,
            R.id.btnO, R.id.btnP, R.id.btnA, R.id.btnS, R.id.btnD, R.id.btnF,
            R.id.btnG, R.id.btnH, R.id.btnJ, R.id.btnK, R.id.btnL, R.id.btnDelete,
            R.id.btnZ, R.id.btnX, R.id.btnC, R.id.btnV, R.id.btnB, R.id.btnN, R.id.btnM, R.id.btnConfirm})
    public void onViewClicked(View view) {
        String s = et_coupon.getText().toString();
        switch (view.getId()) {
            case R.id.btn1:
                String btn1 = this.btn1.getText().toString();
                et_coupon.setText(s + btn1);
                break;
            case R.id.btn2:
                String btn2 = this.btn2.getText().toString();
                et_coupon.setText(s + btn2);
                break;
            case R.id.btn3:
                String btn3 = this.btn3.getText().toString();
                et_coupon.setText(s + btn3);
                break;
            case R.id.btn4:
                String btn4 = this.btn4.getText().toString();
                et_coupon.setText(s + btn4);
                break;
            case R.id.btn5:
                String btn5 = this.btn5.getText().toString();
                et_coupon.setText(s + btn5);
                break;
            case R.id.btn6:
                String btn6 = this.btn6.getText().toString();
                et_coupon.setText(s + btn6);
                break;
            case R.id.btn7:
                String btn7 = this.btn7.getText().toString();
                et_coupon.setText(s + btn7);
                break;
            case R.id.btn8:
                String btn8 = this.btn8.getText().toString();
                et_coupon.setText(s + btn8);
                break;
            case R.id.btn9:
                String btn9 = this.btn9.getText().toString();
                et_coupon.setText(s + btn9);
                break;
            case R.id.btn0:
                String btn0 = this.btn0.getText().toString();
                et_coupon.setText(s + btn0);
                break;
            case R.id.btnQ:
                String btnQ = this.btnQ.getText().toString();
                et_coupon.setText(s + btnQ);
                break;
            case R.id.btnW:
                String btnW = this.btnW.getText().toString();
                et_coupon.setText(s + btnW);
                break;
            case R.id.btnE:
                String btnE = this.btnE.getText().toString();
                et_coupon.setText(s + btnE);
                break;
            case R.id.btnR:
                String btnR = this.btnR.getText().toString();
                et_coupon.setText(s + btnR);
                break;
            case R.id.btnT:
                String btnT = this.btnT.getText().toString();
                et_coupon.setText(s + btnT);
                break;
            case R.id.btnY:
                String btnY = this.btnY.getText().toString();
                et_coupon.setText(s + btnY);
                break;
            case R.id.btnU:
                String btnU = this.btnU.getText().toString();
                et_coupon.setText(s + btnU);
                break;
            case R.id.btnI:
                String btnI = this.btnI.getText().toString();
                et_coupon.setText(s + btnI);
                break;
            case R.id.btnO:
                String btnO = this.btnO.getText().toString();
                et_coupon.setText(s + btnO);
                break;
            case R.id.btnP:
                String btnP = this.btnP.getText().toString();
                et_coupon.setText(s + btnP);
                break;
            case R.id.btnA:
                String btnA = this.btnA.getText().toString();
                et_coupon.setText(s + btnA);
                break;
            case R.id.btnS:
                String btnS = this.btnS.getText().toString();
                et_coupon.setText(s + btnS);
                break;
            case R.id.btnD:
                String btnD = this.btnD.getText().toString();
                et_coupon.setText(s + btnD);
                break;
            case R.id.btnF:
                String btnF = this.btnF.getText().toString();
                et_coupon.setText(s + btnF);
                break;
            case R.id.btnG:
                String btnG = this.btnG.getText().toString();
                et_coupon.setText(s + btnG);
                break;
            case R.id.btnH:
                String btnH = this.btnH.getText().toString();
                et_coupon.setText(s + btnH);
                break;
            case R.id.btnJ:
                String btnJ = this.btnJ.getText().toString();
                et_coupon.setText(s + btnJ);
                break;
            case R.id.btnK:
                String btnK = this.btnK.getText().toString();
                et_coupon.setText(s + btnK);
                break;
            case R.id.btnL:
                String btnL = this.btnL.getText().toString();
                et_coupon.setText(s + btnL);
                break;
            case R.id.btnDelete:
                String coupon = et_coupon.getText().toString();
                String substring = null;
                if (coupon != null && !coupon.equals("")) {
                    substring = coupon.substring(0, coupon.length() - 1);
                    et_coupon.setText(substring);
                }
                break;
            case R.id.btnZ:
                String btnZ = this.btnZ.getText().toString();
                et_coupon.setText(s + btnZ);
                break;
            case R.id.btnX:
                String btnX = this.btnX.getText().toString();
                et_coupon.setText(s + btnX);
                break;
            case R.id.btnC:
                String btnC = this.btnC.getText().toString();
                et_coupon.setText(s + btnC);
                break;
            case R.id.btnV:
                String btnV = this.btnV.getText().toString();
                et_coupon.setText(s + btnV);
                break;
            case R.id.btnB:
                String btnB = this.btnB.getText().toString();
                et_coupon.setText(s + btnB);
                break;
            case R.id.btnN:
                String btnN = this.btnN.getText().toString();
                et_coupon.setText(s + btnN);
                break;
            case R.id.btnM:
                String btnM = this.btnM.getText().toString();
                et_coupon.setText(s + btnM);
                break;
            case R.id.btnConfirm:
                Log.d(TAG, "onViewClicked: " + et_coupon.getText().toString());
                if (!et_coupon.getText().toString().equals("") && et_coupon.getText().toString() != null) {
                    getData("coupon");
                }
                break;
        }
        et_coupon.setSelection(et_coupon.getText().toString().length());
    }

    //持续向后台发送订单查询
    int a;

    private void sockerStar() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 3);
                Log.d(TAG, "run: ------------------------>" + key);
                String url = Comment.URL + "/order/query";
//                String key = spf.getString("key", "");
                Map<String, String> params = new HashMap<>();
                params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                params.put("order_no", order_no);//订单生成的时候返回的
                params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
                params.put("signature", signature);//签名参数
                Log.d(TAG, "run: " + params);
                OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("TAG", "onResponse:失败" + e);
                        i = 0;
//                        initSignin(1);
                        handler.removeCallbacks(runnable);
                        sockerStar();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("TAG", "onResponse:成功S" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean code = jsonObject.getBoolean("code");
                            if (!code) {
                                if (a < 2) {
                                    i = 0;
                                    initSignin(1);
                                    handler.removeCallbacks(runnable);
                                    a++;
                                } else {
                                    handler.removeCallbacks(runnable);
                                    orderCancle();
                                    TcnUtility.getToast(CouponPickingActivity.this, "请求超时");
                                }
                            } else {
                                JSONObject data = jsonObject.getJSONObject("data");
                                order_status = data.getInt("order_status");
                                if (order_status == 0) {
                                    trade_status = data.getInt("trade_status");
                                    if (trade_status == 1) {
                                        refund_status = data.getInt("refund_status");
                                        if (refund_status == 0) {
                                            deliver_status = data.getInt("deliver_status");
                                            if (deliver_status == 0) {
                                                handler.removeCallbacks(runnable);
                                                int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                                                if (model_id == 1) {
                                                    String code1 = tunnel_code.substring(1);
                                                    int slotNo = Integer.parseInt(code1);//出货的货道号
                                                    String shipMethod = "卡券支付"; //出货方法,微信支付出货，此处自己可以修改。
                                                    String amount = String.valueOf(price);    //支付的金额（元）,自己修改
                                                    String tradeNo = order_no;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                                                    TcnVendIF.getInstance().reqShip(slotNo, shipMethod, amount, tradeNo);
                                                } else if (model_id == 2) {
                                                    final Dialog dialog = new Dialog(CouponPickingActivity.this);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setContentView(R.layout.pay_and_chu_huo);
                                                    final TextView tv = dialog.findViewById(R.id.tv_tu);
                                                    final TextView tv1 = dialog.findViewById(R.id.tv_tu1);
                                                    final ImageView iv = dialog.findViewById(R.id.iv_tu);
                                                    tv.setText("支付成功，等待商品出货");
                                                    tv1.setText("");
                                                    iv.setImageResource(R.drawable.zhifuchenggong_jiazai);
                                                    play(2);
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
                                                            String code = tunnel_code.substring(1);
                                                            String first = tunnel_code.substring(0, 1);
                                                            int tunnel_code = Integer.parseInt(code);//除第一位剩下的数字
                                                            int firstNum = Integer.parseInt(first);//货道号第一位数字
                                                            String serialPort2 = null;
                                                            if (firstNum == 1) {
                                                                serialPort2 = spf.getString("serialport2", "");
                                                                Log.d("TAG", "onCreateView: " + serialPort2);
                                                            } else if (firstNum == 2) {
                                                                serialPort2 = spf.getString("serialport2_1", "");
                                                            } else if (firstNum == 3) {
                                                                serialPort2 = spf.getString("serialport2_2", "");
                                                            }
                                                            if (serialPort2 != null && !serialPort2.equals("")) {
                                                                String substring2 = serialPort2.substring(serialPort2.length() - 2);
                                                                zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring2,CouponPickingActivity.this);
                                                                String getId = "0101000000000000000000000000000000007188";
                                                                String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
//                                                            String chekChuHuo = zhongjiAisleSerial.checkChuHuo(tunnel_code);
                                                                Log.d(TAG, "onViewClicked: " + chekChuHuo);
                                                                TcnUtility.getToast(CouponPickingActivity.this, "出货返回的数据是：" + chekChuHuo);
                                                                if (chekChuHuo != null && !chekChuHuo.equals("")) {
                                                                    String data = "0105" + code + "03" + "00" + "00000000000000000000000000";//启动电机  03：三线电机  00：无光幕 aisleNum:货道号（电机号）
                                                                    byte[] bytes = Utils.hexStr2Byte(data);
                                                                    int crc16Check = Utils.CRC16_Check(bytes, bytes.length);
                                                                    String crc = Utils.integerToHexString(crc16Check);
                                                                    String crc1 = crc.substring(0, 2);
                                                                    String crc2 = crc.substring(2, 4);
                                                                    String get = data + crc2 + crc1;
                                                                    String chekMachine = zhongjiAisleSerial.checkChuHuo1(get);//启动马达
                                                                    String s = chekMachine.substring(4, 6);
                                                                    if (s.equals("00")) {
                                                                        TcnUtility.getToast(CouponPickingActivity.this, "已启动");
                                                                        //持续查询电机状态
                                                                        check(tv, tv1, iv);
                                                                    } else if (s.equals("01")) {
                                                                        TcnUtility.getToast(CouponPickingActivity.this, "无效的电机索引号");
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
                                                                zhongjiAisleSerial.closeSerialPort();
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
                                                                    Log.i("tag", "所有数据:" + all.toString());

                                                                } catch (DbException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                finish();
                                                            } else {
                                                                TcnUtility.getToast(CouponPickingActivity.this, "请先设置货道串口");
                                                            }
                                                        }
                                                    };
                                                    handler.postDelayed(runnable, 500);//线程时间0.5秒
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Message message = new Message();
                message.what = -1;
                handler.sendMessage(message);
            }
        };
        handler.postDelayed(runnable, 3000);//线程时间3秒刷新
    }

    private MediaPlayer mediaPlayer;

    // 初始化MediaPlayer 提示音
    private void play(int result) {
        try {
            if (result == 0) {//0：出货失败 1：出货成功 2：购买成功
                mediaPlayer = MediaPlayer.create(CouponPickingActivity.this, R.raw.deliversuccess);
            } else if (result == 1) {
                mediaPlayer = MediaPlayer.create(CouponPickingActivity.this, R.raw.deliverfail);
            } else if (result == 2) {
                mediaPlayer = MediaPlayer.create(CouponPickingActivity.this, R.raw.buysuccess);
            }
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕 开始播放流媒体
                    mediaPlayer.start();
                    Toast.makeText(CouponPickingActivity.this, "开始播放", 0).show();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被调用
                    Toast.makeText(CouponPickingActivity.this, "播放完毕", 0).show();
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
                        Toast.makeText(CouponPickingActivity.this, "重新播放", 0).show();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CouponPickingActivity.this, "播放失败", 0).show();
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
                        TcnUtility.getToast(CouponPickingActivity.this, "出货中");
                    }
                }
            }
        };
        handler.postDelayed(runnable, 1000);//线程时间1秒刷新
    }

    //取消订单
    private void orderCancle() {
        Log.d(TAG, "orderCancle: " + key);
        //订单取消
        String url = Comment.URL + "/order/cancel";
//      String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                initSignin(2);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseC:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //出货确认
    private void chuHuoConfirm() {
        Log.d(TAG, "orderCancle: " + key);
        //出货确认
        String url = Comment.URL + "/order/deliver";
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&tunnel_no=" + tunnel_no + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
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

    //提交订单
    private void getData(String payment) {
        String coupon = et_coupon.getText().toString();
        String url = Comment.URL + "/order/create";
        String terminal_no = spf.getString("terminal_no", "");
        Log.d(TAG, "getData: " + key);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("terminal_order_no", terminal_no + "-" + str);//00010001：terminal_no设备定义的终端编号
        params.put("good_no", goods_no);//商品编号
        params.put("coupon", coupon);//提货码
        params.put("payment", payment);//支付方式
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("coupon=" + coupon + "&good_no=" + goods_no + "&payment=" + payment + "&terminal_code=" + Comment.terminal_code + "&terminal_order_no=" + terminal_no + "-" + str + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "getData: " + params);
        finish();
//        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.d("TAG", "onResponse:失败" + e);
//            }
//            @Override
//            public void onResponse(String response, int id) {
//                Log.d("TAG", "onResponse:成功" + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    //订单生成的时候返回的
//                    order_no = data.getString("order_no");
//                    sockerStar();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        // 在activity结束的时候回收资源
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Toast.makeText(CouponPickingActivity.this, "播放停止", 0).show();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        ButterKnife.reset(this);
        handler.removeCallbacks(runnable);
    }

    //获取屏幕分辨率：宽
    public int getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) getWindowManager();
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
        WindowManager wm = (WindowManager) getWindowManager();
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
                .setDbDir(null) //设置数据库保存的路径getCacheDir().getAbsoluteFile()
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
    //    String param = "terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
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
        String url = Comment.URL + "/terminal/signin";
        timeStamp = getTimeStamp();
        key = Comment.KEY;
        Log.d(TAG, "initState: " + key);
        //auth_sign MD5加密
        /*String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        params.put("auth_sign", ciphertext);//签名
        if (i < 2) {
            OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", "onResponse:失败" + e);
                    Log.d("TAG", "onResponse:失败" + params);
                    try {
                        Thread.sleep(5000);
                        initSignin(flag1);
                        i++;
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
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
                        sockerStar();
                    } else if (flag == 2) {
                        orderCancle();
                    }
                }
            });
        } else {
            Log.d(TAG, "initSignin: " + i);
            TcnUtility.getToast(this, "请求超时");
        }*/
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
}
