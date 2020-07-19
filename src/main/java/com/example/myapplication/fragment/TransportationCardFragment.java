package com.example.myapplication.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwin.navy.serialportapi.com_zhongji;
import com.example.myapp.R;
import com.example.myapplication.adapter.PayAdapter1;
import com.example.myapplication.bean.SettingBean;
import com.example.myapplication.bean.TransportCard;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.ClearEditText;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.GlideUtils;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.MyGridView;
import com.example.myapplication.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android_serialport_api.SerialPort;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 * 这是设备管理页
 */
public class TransportationCardFragment extends Fragment {
    public static final String TAG = "Transportation";
    @InjectView(R.id.ll_find)
    LinearLayout ll_find;
    @InjectView(R.id.ll_bu)
    LinearLayout ll_bu;
    @InjectView(R.id.ll_chong)
    LinearLayout ll_chong;
    @InjectView(R.id.ll_bu_chong)
    LinearLayout ll_bu_chong;//补充值
    @InjectView(R.id.ll_leiBiao)
    LinearLayout ll_leiBiao;
    @InjectView(R.id.tv_yu)
    TextView tv_yu;
    @InjectView(R.id.ll_yu)
    LinearLayout ll_yu;
    @InjectView(R.id.ll_yu1)
    LinearLayout ll_yu1;
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.rl_bu)
    RelativeLayout rl_bu;
    @InjectView(R.id.t1)
    TextView t1;
    @InjectView(R.id.t2)
    TextView t2;
    @InjectView(R.id.t3)
    TextView t3;
    @InjectView(R.id.rl_chong)
    RelativeLayout rl_chong;
    @InjectView(R.id.ll_fanying)
    LinearLayout ll_fanying;
    @InjectView(R.id.tv_yu_chong)
    TextView tv_yu_chong;
    @InjectView(R.id.tv_chongzhi)
    TextView tv_chongzhi;
    @InjectView(R.id.tv_card1)
    TextView tv_card1;
    @InjectView(R.id.ll_chongzhi)
    LinearLayout ll_chongzhi;
    @InjectView(R.id.tv_10)
    TextView tv_10;
    @InjectView(R.id.tv_20)
    TextView tv_20;
    @InjectView(R.id.tv_50)
    TextView tv_50;
    @InjectView(R.id.tv_100)
    TextView tv_100;
    @InjectView(R.id.tv_zi)
    ClearEditText tv_zi;
    @InjectView(R.id.ll_select)
    LinearLayout ll_select;
    //    @InjectView(R.id.ll_weiXin)
//    LinearLayout ll_weiXin;
//    @InjectView(R.id.ll_zhiFuzBao)
//    LinearLayout ll_zhiFuzBao;
//    @InjectView(R.id.ll_yinLian)
//    LinearLayout ll_yinLian;
//    @InjectView(R.id.ll_xianJin)
//    LinearLayout ll_xianJin;
    @InjectView(R.id.ll_fangShi)
    LinearLayout ll_fangShi;
    @InjectView(R.id.iv_tu)
    ImageView iv_tu;
    @InjectView(R.id.tv_du)
    TextView tv_du;
    @InjectView(R.id.ll_duka)
    LinearLayout ll_duka;
    @InjectView(R.id.tv_card)
    TextView tv_card;
    @InjectView(R.id.tv_money)
    TextView tv_money;
    @InjectView(R.id.tv_jin)
    TextView tv_jin;
    @InjectView(R.id.tv_jinE)
    TextView tv_jinE;
    @InjectView(R.id.ll_jinE)
    LinearLayout ll_jinE;
    @InjectView(R.id.tv_jin1)
    TextView tv_jin1;
    @InjectView(R.id.tv_jinE1)
    TextView tv_jinE1;
    @InjectView(R.id.ll_jinE1)
    LinearLayout ll_jinE1;
    @InjectView(R.id.tv_success)
    TextView tv_success;
    @InjectView(R.id.ll_yuEShow)
    LinearLayout ll_yuEShow;
    @InjectView(R.id.btn_return)
    Button btn_return;
    @InjectView(R.id.ll_yuEFind)
    LinearLayout ll_yuEFind;
    private int id = 0;
    private String money;
    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    //    private ReadThread mReadThread;
    private SharedPreferences spf;
    //    private boolean flag = true;
    private Map<String, String> map = new HashMap<>();
    /**
     * 中吉串口API
     */
    private com_zhongji zhongjiSerial;
    private int flag;//标记
    private String key;
    private String s9F21;//交易时间
    private String YuE;//余额
    private long timeStamp;//时间戳

    //发送的数据
    private String cardNum;//卡号
    private int start;//当前余额 单位分 不是传10.00 要传1000
    private String order_no;//订单编号
    private String data0015 = "";//15文件
    private String data0005 = "";//05文件
    private String init_result = "";//圈存初始化
    private String first_gac_result = "";//第一次GAC
    private String random = "";//随机数
    private String result;//写卡成功或失败
    private String tac;//tac
    //返回的数据
    private String order_code;//订单code
    private String jinE;//领款金额
    private String type;//卡种
    private String createtime;//订单交易时间
    private String apdu;//写卡指令
    private String arcode;//认证通过的授权码
    private String arpc;//外部认证认证:ARPC
    private String gpoJoint;//GPO拼接顺序

    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig;
    private int width;
    private int height;
    private String new_order_code;//合肥通支付订单号
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        width = getAndroiodScreenProperty();
        height = getAndroiodScreenPropertyH();
        View view;
        if (width < height) {
            view = inflater.inflate(R.layout.fragment_transportation_card, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_transportation_card1, container, false);
        }
        ButterKnife.inject(this, view);
        configLog();
        //初始化数据库配置信息
        initDaoConfig();
//        initKongJian();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        return view;
    }

    //获取串口返回值
    private String init(String sdata) {
//        byte[] sendCmd = TransformUtils.HexString2Bytes(sdata);
//        String dataCmd = SerialPortController.getInstance().writeDataII(sendCmd);
        if (zhongjiSerial == null) {
            Log.d(TAG, "init: 串口设置有问题");
            return "0";
        }
        String dataCmd = zhongjiSerial.checkXunKa(sdata);
        Log.d(TAG, "onViewClicked: " + dataCmd);
//        try {
//            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 115200, 0);
//            Log.d("TAG", "onClick: " + sdata);
//            mInputStream = mSerialPort.getInputStream();
//            mOutputStream = mSerialPort.getOutputStream();
//            byte[] bytes = Utils.hexStr2Byte(data);
//            mOutputStream.write(bytes);
//            mOutputStream.flush();
//            Log.i("test2", "发送成功");
////            Toast.makeText(getActivity(), "发送成功！！！", Toast.LENGTH_SHORT).show();
//            mReadThread = new ReadThread();
//            mReadThread.start();
//        } catch (IOException e) {
//            Log.i("test", "发送失败");
//            Toast.makeText(getActivity(), "发送失败！！！", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
        return dataCmd;
    }

    //打开串口
    private void init() {

        String serialPort = spf.getString("serialport", "");
        Log.d("TAG", "onCreateView: " + serialPort);
        if (serialPort != null && !serialPort.equals("")) {
            String substring1 = serialPort.substring(serialPort.length() - 2);
            Log.d("TAG", "onCreateView: " + substring1);
            zhongjiSerial = com_zhongji.getInstance(substring1);
        }
        //        String devices0 = spf.getString("devices0", "");
//        String baudrates0_1 = spf.getString("baudrates0", "");
//        Log.d(TAG, "onViewClicked0: " + devices0 + "++++++++" + baudrates0_1);
//            if (devices0 != null && !devices0.equals("") && !baudrates0_1.equals("")) {
//                int baudrates0 = Integer.parseInt(baudrates0_1);
//                SerialPortController.getInstance().openSerialPort(devices0, baudrates0);
//            } else {
//                Toast.makeText(getActivity(), "请先设置读卡器串口", Toast.LENGTH_SHORT).show();
//            }
    }

    //初始化控件状态
    public void initKongJian() {
        Log.d(TAG, "initKongJian: " + zhongjiSerial);
        if (zhongjiSerial != null) {
            //发送移卡指令、关闭串口
            String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
            final String dataYiKa = init(yiKa);
            Log.d("TAG", "onViewClicked: " + dataYiKa);
            zhongjiSerial.closeSerialPort();
        }
        ll_leiBiao.setVisibility(View.VISIBLE);
        ll_yuEFind.setVisibility(View.GONE);
        //充值没做，暂时隐藏掉
//        ll_chong.setVisibility(View.GONE);

        ll_fanying.setVisibility(View.GONE);
        ll_duka.setVisibility(View.GONE);
        ll_yu.setVisibility(View.GONE);
        ll_jinE.setVisibility(View.GONE);
        ll_jinE1.setVisibility(View.GONE);
        ll_yuEFind.setVisibility(View.GONE);
        ll_yu1.setVisibility(View.GONE);
        ll_yuEShow.setVisibility(View.GONE);
        rl_bu.setVisibility(View.GONE);
        tv_success.setVisibility(View.GONE);
        ll_select.setVisibility(View.GONE);
        ll_chongzhi.setVisibility(View.GONE);
        rl_chong.setVisibility(View.GONE);
        ll_fangShi.setVisibility(View.GONE);
        iv_tu.setImageResource(R.drawable.zhengzaiduka_xieka);
        //领款
        tv1.setText("1");
        tv2.setText("2");
        tv3.setText("3");
        tv1.setTextColor(getResources().getColor(R.color.white));
        tv1.setBackgroundResource(R.drawable.banner_indicator_selected);
        tv2.setTextColor(getResources().getColor(R.color.black));
        tv2.setBackgroundResource(R.drawable.banner_indicator_selected1);
        tv3.setTextColor(getResources().getColor(R.color.black));
        tv3.setBackgroundResource(R.drawable.banner_indicator_selected1);

        //充值
        t1.setText("1");
        t2.setText("2");
        t3.setText("3");
        t1.setTextColor(getResources().getColor(R.color.white));
        t1.setBackgroundResource(R.drawable.banner_indicator_selected);
        t2.setTextColor(getResources().getColor(R.color.black));
        t2.setBackgroundResource(R.drawable.banner_indicator_selected1);
        t3.setTextColor(getResources().getColor(R.color.black));
        t3.setBackgroundResource(R.drawable.banner_indicator_selected1);
        initSelect();
        tv_10.setBackgroundResource(R.drawable.footer_bg);
        tv_10.setTextColor(getResources().getColor(R.color.white));
        tv_zi.setText("");
        money = "10元";
        flag = 1;
        ll_find.setEnabled(true);
        ll_bu.setEnabled(true);
        ll_chong.setEnabled(true);
        gv.setEnabled(true);
        btn_return.setEnabled(true);
    }

    private MediaPlayer mediaPlayer;

    // 初始化MediaPlayer 提示音
    private void play(int result) {
        try {
            if (result == 0) {//0：贴卡 1：补登成功 2：补登失败 3：充值成功 4：充值失败
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.stickcard);//暂时没用到
            } else if (result == 1) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.fillsuccess);
            } else if (result == 2) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.fillfail);
            } else if (result == 3) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.topupsuccess);
            } else if (result == 4) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.topupfail);
            }
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕 开始播放流媒体
                    mediaPlayer.start();
//                    Toast.makeText(getActivity(), "开始播放", 0).show();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被调用
//                    Toast.makeText(getActivity(), "播放完毕", 0).show();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 如果发生错误，重新播放
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(0);
//                        Toast.makeText(getActivity(), "重新播放", 0).show();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getActivity(), "播放失败", 0).show();
        }
    }

    @Override
    public void onDestroyView() {
        // 在activity结束的时候回收资源
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            Toast.makeText(getActivity(), "播放停止", 0).show();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(handlers!=null && runnables!=null){
            handlers.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //     R.id.ll_weiXin,R.id.ll_zhiFuzBao, R.id.ll_yinLian, R.id.ll_xianJin,
    @OnClick({R.id.ll_find, R.id.ll_bu, R.id.ll_chong, R.id.ll_leiBiao,
            R.id.tv_yu, R.id.ll_yu, R.id.ll_yu1, R.id.tv1, R.id.tv2, R.id.tv3,
            R.id.rl_bu, R.id.t1, R.id.t2, R.id.t3, R.id.rl_chong, R.id.ll_fanying,
            R.id.tv_chongzhi, R.id.tv_card1, R.id.ll_chongzhi, R.id.tv_10, R.id.tv_20,
            R.id.tv_50, R.id.tv_100, R.id.tv_zi, R.id.tv_return, R.id.ll_select, R.id.ll_fangShi,
            R.id.iv_tu, R.id.tv_du, R.id.ll_duka, R.id.tv_card, R.id.tv_money,
            R.id.tv_jin, R.id.tv_jinE, R.id.ll_jinE, R.id.tv_jin1, R.id.tv_jinE1,
            R.id.ll_jinE1, R.id.ll_yuEShow, R.id.btn_return, R.id.ll_yuEFind,R.id.ll_bu_chong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bu_chong://补充值

                break;
            case R.id.ll_find:
                init();
                a = 0;
                initSignin(-1);
                ll_find.setEnabled(false);
                ll_leiBiao.setVisibility(View.GONE);
                ll_yuEFind.setVisibility(View.VISIBLE);
                ll_yu.setVisibility(View.VISIBLE);
                ll_fanying.setVisibility(View.VISIBLE);
                tv_yu.setText("交通卡余额查询");
                btn_return.setVisibility(View.GONE);
                String xunKa = "0200000010800000000600003C0000BECC01010000D803";
                String data = init(xunKa);
                if (data == null) {
                    data = init(xunKa);
                    if (data == null) {
                        data = init(xunKa);
                        if (data == null) {
                            TcnUtility.getToast(getActivity(), "请重新贴卡");
                            initKongJian();
                            return;
                        }
                    }
                } else if (data.equals("0")) {
                    TcnUtility.getToast(getActivity(), "串口设置有问题");
                    initKongJian();
                    return;
                }
                final Handler handler = new Handler();
                final String finalData = data;
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String substring = finalData.substring(finalData.length() - 8, finalData.length() - 4);
                        if (finalData != null && !finalData.equals("") && substring.equals("9000")) {
                            chaXun();//查询
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                            Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                            TextView tv_message = aalayout.findViewById(R.id.tv_message);
                            tv_message.setText("卡片被移开，请重新放卡");
                            builder.setCancelable(false)
                                    .setView(aalayout);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initKongJian();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                };
                handler.postDelayed(runnable, 500);//线程时间3秒
                break;
            case R.id.ll_bu:
                init();
                a = 0;
                initSignin(-1);
                ll_bu.setEnabled(false);
                ll_leiBiao.setVisibility(View.GONE);
                ll_yuEFind.setVisibility(View.VISIBLE);
                rl_bu.setVisibility(View.VISIBLE);
                ll_fanying.setVisibility(View.VISIBLE);
                tv_yu.setText("交通卡补登充值");
                btn_return.setVisibility(View.GONE);
                id = 10;
                String xunKaBuDeng = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                String xunKaBu = init(xunKaBuDeng);
                if (xunKaBu == null) {
                    xunKaBu = init(xunKaBuDeng);
                    if (xunKaBu == null) {
                        xunKaBu = init(xunKaBuDeng);
                        if (xunKaBu == null) {
                            TcnUtility.getToast(getActivity(), "请重新贴卡");
                            initKongJian();
                            return;
                        }
                    }
                } else if (xunKaBu.equals("0")) {
                    TcnUtility.getToast(getActivity(), "串口设置有问题");
                    initKongJian();
                    return;
                }
                final Handler handlerBuDeng = new Handler();
                final String finalXunKaBu = xunKaBu;
                final Runnable runnableBuDeng = new Runnable() {
                    @Override
                    public void run() {
                        String substring = finalXunKaBu.substring(finalXunKaBu.length() - 8, finalXunKaBu.length() - 4);
                        if (substring.equals("9000")) {
                            selectAID(finalXunKaBu);
                            ll_fanying.setVisibility(View.GONE);
                            ll_duka.setVisibility(View.VISIBLE);
                            tv_du.setText("正在读卡");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                            Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                            TextView tv_message = aalayout.findViewById(R.id.tv_message);
                            tv_message.setText("卡片被移开，请重新放卡");
                            builder.setCancelable(false)
                                    .setView(aalayout);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initKongJian();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                };
                handlerBuDeng.postDelayed(runnableBuDeng, 500);//线程时间10秒
                break;
            case R.id.ll_chong:
                init();
                a = 0;
                initSignin(-1);
                ll_chong.setEnabled(false);
                ll_leiBiao.setVisibility(View.GONE);
                ll_yuEFind.setVisibility(View.VISIBLE);
                rl_chong.setVisibility(View.VISIBLE);
                ll_fanying.setVisibility(View.VISIBLE);
                tv_yu.setText("交通卡在线充值");
                btn_return.setVisibility(View.GONE);
                id = 20;
                String xunKaRecharge = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                String xunRecharge = init(xunKaRecharge);
                if (xunRecharge == null) {
                    xunRecharge = init(xunKaRecharge);
                    if (xunRecharge == null) {
                        xunRecharge = init(xunKaRecharge);
                        if (xunRecharge == null) {
                            TcnUtility.getToast(getActivity(), "请重新贴卡");
                            initKongJian();
                            return;
                        }
                    }
                } else if (xunRecharge.equals("0")) {
                    TcnUtility.getToast(getActivity(), "串口设置有问题");
                    initKongJian();
                    return;
                }
                final Handler handlerRecharge = new Handler();
                final String finalxunRecharge = xunRecharge;
                final Runnable runnableRecharge = new Runnable() {
                    @Override
                    public void run() {
//                        final String data = map.get("data");
//                        Log.d("TAG", "onViewClicked1: " + xunRecharge);
                        String substring = finalxunRecharge.substring(finalxunRecharge.length() - 8, finalxunRecharge.length() - 4);
                        if (substring.equals("9000")) {
                            selectAID(finalxunRecharge);
                            ll_fanying.setVisibility(View.GONE);
                            ll_duka.setVisibility(View.VISIBLE);
                            tv_du.setText("正在读卡");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                            Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                            TextView tv_message = aalayout.findViewById(R.id.tv_message);
                            tv_message.setText("卡片被移开，请重新放卡");
                            builder.setCancelable(false)
                                    .setView(aalayout);
                            final AlertDialog dialog = builder.create();

                            dialog.show();
                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initKongJian();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                };
                handlerRecharge.postDelayed(runnableRecharge, 500);//线程时间10秒
                break;
//            case R.id.ll_leiBiao:
//                break;
//            case R.id.tv_yu:
//                break;
//            case R.id.ll_yu:
//                break;
//            case R.id.ll_yu1:
//                break;
//            case R.id.tv1:
//                break;
//            case R.id.tv2:
//                break;
//            case R.id.tv3:
//                break;
//            case R.id.rl_bu:
//                break;
//            case R.id.t1:
//                break;
//            case R.id.t2:
//                break;
//            case R.id.t3:
//                break;
//            case R.id.rl_chong:
//                break;
//            case R.id.ll_fanying:
//                break;
//            case R.id.tv_chongzhi:
//                break;
//            case R.id.tv_card1:
//                break;
//            case R.id.ll_chongzhi:
//                break;
            case R.id.tv_10:
                initSelect();
                tv_10.setBackgroundResource(R.drawable.footer_bg);
                tv_10.setTextColor(getResources().getColor(R.color.white));
                tv_zi.setText("");
                tv_zi.setHintTextColor(getResources().getColor(R.color.black1));
                money = tv_10.getText().toString();
                flag = 1;
//                if (width > height) {
//                    Log.d(TAG, "onViewClicked: 宽屏");
//
//                }
                break;
            case R.id.tv_20:
                initSelect();
                tv_20.setBackgroundResource(R.drawable.footer_bg);
                tv_20.setTextColor(getResources().getColor(R.color.white));
                tv_zi.setText("");
                tv_zi.setHintTextColor(getResources().getColor(R.color.black1));
                money = tv_20.getText().toString();
                flag = 2;
                break;
            case R.id.tv_50:
                initSelect();
                tv_50.setBackgroundResource(R.drawable.footer_bg);
                tv_50.setTextColor(getResources().getColor(R.color.white));
                tv_zi.setText("");
                tv_zi.setHintTextColor(getResources().getColor(R.color.black1));
                money = tv_50.getText().toString();
                flag = 3;
                break;
            case R.id.tv_100:
                initSelect();
                tv_100.setBackgroundResource(R.drawable.footer_bg);
                tv_100.setTextColor(getResources().getColor(R.color.white));
                tv_zi.setText("");
                tv_zi.setHintTextColor(getResources().getColor(R.color.black1));
                money = tv_100.getText().toString();
                flag = 4;
                break;
            case R.id.tv_zi:
                initSelect();
                tv_zi.setBackgroundResource(R.drawable.footer_bg);
                tv_zi.setTextColor(getResources().getColor(R.color.white));
                tv_zi.setHintTextColor(getResources().getColor(R.color.white));
                flag = 5;
                break;
            case R.id.tv_return:
                initKongJian();
                break;
            case R.id.ll_select:
                break;
//            case R.id.ll_weiXin:
//                ll_fangShi.setVisibility(View.GONE);
//                ll_duka.setVisibility(View.VISIBLE);
//                iv_tu.setImageResource(R.drawable.erweima);
//                tv_du.setText("请打开微信扫码充值");
//                btn_return.setText("返回");
//                id = 20;
//                break;
//            case R.id.ll_zhiFuzBao:
//                ll_fangShi.setVisibility(View.GONE);
//                ll_duka.setVisibility(View.VISIBLE);
//                iv_tu.setImageResource(R.drawable.erweima);
//                tv_du.setText("请打开支付宝扫码充值");
//                btn_return.setText("返回");
//                id = 20;
//                break;
//            case R.id.ll_yinLian:
//                break;
//            case R.id.ll_xianJin:
//                ll_fangShi.setVisibility(View.GONE);
//                ll_duka.setVisibility(View.VISIBLE);
//                iv_tu.setVisibility(View.GONE);
//                tv_du.setText("等待您的支付，请投入现金！");
//                btn_return.setText("返回");
//                id = 20;
//                break;
            case R.id.btn_return:
                if (id == 0) {
                    ll_leiBiao.setVisibility(View.VISIBLE);
                    ll_yuEFind.setVisibility(View.GONE);
                    ll_yu1.setVisibility(View.GONE);
                    ll_yuEShow.setVisibility(View.GONE);
                    ll_find.setEnabled(true);
                    Log.d(TAG, "onViewClicked: " + cardNum + "+++" + start + "+" + order_no +
                            "+++" + data0005 + "++++" + data0015 + "++++" + init_result +
                            "++++" + first_gac_result + "++++" + random +
                            "++++" + result + "++++" + tac +
                            "++++" + jinE + "++++" + type + "++++" + createtime +
                            "++++" + apdu + "++++" + arcode + "++++" + arpc);
                } else if (id == 10) {
                    Double money = Double.valueOf(YuE) + Double.valueOf(jinE);//领款金额和余额
                    gLogger.debug("余额+补登金额："+ money);
                    if(money > 1000){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                        Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                        TextView tv_message = aalayout.findViewById(R.id.tv_message);
                        tv_message.setText("金额超限，充值失败");
                        builder.setCancelable(false)
                                .setView(aalayout);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initKongJian();
                                dialog.dismiss();
                            }
                        });
                        return;
                    }
                    String xun = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                    String xunK = init(xun);
                    if (xunK == null) {
                        init();
                        xunK = init(xun);
                    }
                    final Handler handler1 = new Handler();
                    final String finalXunK = xunK;
                    final Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            String substring = finalXunK.substring(finalXunK.length() - 8, finalXunK.length() - 4);
                            if (substring.equals("9000")) {
                                if (type.equals(hft_card) || type.equals(hft_traffic_card)) {
                                    initGet05File(finalXunK);
                                } else if (type.equals(zhongyin__nine_card) || type.equals(zhongyin_six_card)) {
                                    initGPO(finalXunK);
                                }
                                ll_duka.setVisibility(View.VISIBLE);
                                ll_yuEShow.setVisibility(View.GONE);
                                btn_return.setVisibility(View.GONE);
                                tv_du.setText("正在写卡");
                                tv2.setText("");
                                tv2.setBackgroundResource(R.drawable.wancheng);
                                tv2.setTextColor(getResources().getColor(R.color.white));
                                tv3.setBackgroundResource(R.drawable.banner_indicator_selected);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                                Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                                TextView tv_message = aalayout.findViewById(R.id.tv_message);
                                tv_message.setText("卡片被移开，请重新放卡");
                                builder.setCancelable(false)
                                        .setView(aalayout);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                                btn_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        initKongJian();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    };
                    handler1.postDelayed(runnable1, 500);//线程时间0.5秒
                } else if (id == 14) {
                    ll_leiBiao.setVisibility(View.VISIBLE);
                    ll_yuEFind.setVisibility(View.GONE);
                    ll_yuEShow.setVisibility(View.GONE);
                    rl_bu.setVisibility(View.GONE);
                    tv_success.setVisibility(View.GONE);
                    tv1.setText("1");
                    tv2.setText("2");
                    tv3.setText("3");
                    tv1.setTextColor(getResources().getColor(R.color.white));
                    tv1.setBackgroundResource(R.drawable.banner_indicator_selected);
                    tv2.setTextColor(getResources().getColor(R.color.black));
                    tv2.setBackgroundResource(R.drawable.banner_indicator_selected1);
                    tv3.setTextColor(getResources().getColor(R.color.black));
                    tv3.setBackgroundResource(R.drawable.banner_indicator_selected1);
                    ll_bu.setEnabled(true);

                    Log.d(TAG, "onViewClickeda: " + cardNum + "+++" + start + "+" + order_no +
                            "+++" + data0005 + "++++" + data0015 + "++++" + init_result +
                            "++++" + first_gac_result + "++++" + random +
                            "++++" + result + "++++" + tac + "++++" + order_code +
                            "++++" + jinE + "++++" + type + "++++" + createtime +
                            "++++" + apdu + "++++" + arcode + "++++" + arpc);
                    try {
                        //根据配置信息获取操作数据的db对象
                        DbManager db = x.getDb(daoConfig);
                        TransportCard transport = new TransportCard();
                        transport.setCard_num(cardNum);
                        transport.setBalance(start);
                        transport.setOrder_no(order_no);
                        transport.setData0005(data0005);
                        transport.setData0015(data0015);
                        transport.setInit_result(init_result);
                        transport.setFirst_gac_result(first_gac_result);
                        transport.setRandom(random);
                        transport.setResult(result);
                        transport.setTac(tac);
                        transport.setOrder_code(order_code);
                        transport.setMoney(jinE);
                        transport.setType(type);
                        transport.setCreatetime(createtime);
                        transport.setApdu(apdu);
                        transport.setArcode(arcode);
                        transport.setArpc(arpc);
                        db.save(transport);//插入一条数据

                        //查询所有数据
                        List<TransportCard> all = db.findAll(TransportCard.class);
                        Log.i("tag", "所有数据:" + all.toString());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else if (id == 20) {

                    ll_duka.setVisibility(View.GONE);
                    ll_select.setVisibility(View.VISIBLE);
                    ll_fangShi.setVisibility(View.VISIBLE);
                    gv.setEnabled(true);
                    t1.setText("");
                    t1.setBackgroundResource(R.drawable.wancheng);
                    t2.setTextColor(getResources().getColor(R.color.white));
                    t2.setBackgroundResource(R.drawable.banner_indicator_selected);
                    tv_yu_chong.setText("充值金额：");
                    if (flag == 5) {
                        money = tv_zi.getText().toString() + "元";
                    }
                    tv_chongzhi.setText(money);
//                    btn_return.setText("返回");
//                    id = 21;
                } else if (id == 21) {
                    if (code == 1) { //成功
                        handlers.removeCallbacks(runnables);
                        numCancle = 0;
                        Log.d(TAG, "onClick: +++++++");
                        orderCancle();
                    }
                    ll_duka.setVisibility(View.GONE);
                    ll_chongzhi.setVisibility(View.VISIBLE);
                    ll_select.setVisibility(View.VISIBLE);
                    ll_fangShi.setVisibility(View.VISIBLE);
                    t1.setText("");
                    t1.setBackgroundResource(R.drawable.wancheng);
                    t2.setTextColor(getResources().getColor(R.color.white));
                    t2.setBackgroundResource(R.drawable.banner_indicator_selected);
                    tv_yu_chong.setText("卡内余额：");
                    tv_chongzhi.setText(YuE + "元");
                    btn_return.setText("下一步");
                    btn_return.setVisibility(View.GONE);
                    id = 20;
//                } else if (id == 23) {
//                    ll_duka.setVisibility(View.VISIBLE);
//                    ll_yuEShow.setVisibility(View.GONE);
//                    iv_tu.setVisibility(View.VISIBLE);
//                    iv_tu.setImageResource(R.drawable.zhengzaiduka_xieka);
//                    btn_return.setVisibility(View.GONE);
//                    tv_du.setText("支付成功，正在写卡");
//                    t2.setText("");
//                    t2.setBackgroundResource(R.drawable.wancheng);
//                    t3.setTextColor(getResources().getColor(R.color.black));
//                    t3.setBackgroundResource(R.drawable.banner_indicator_selected);
//                    id = 24;
//                } else if (id == 24) {
//                    ll_duka.setVisibility(View.GONE);
//                    ll_yuEShow.setVisibility(View.VISIBLE);
//                    tv_success.setVisibility(View.VISIBLE);
//                    ll_chongzhi.setVisibility(View.GONE);
//                    ll_jinE.setVisibility(View.VISIBLE);
//                    ll_jinE1.setVisibility(View.VISIBLE);
//                    t3.setText("");
//                    t3.setBackgroundResource(R.drawable.wancheng);
//                    tv_jin.setText("充值金额：");
//                    tv_jin1.setText("充值后金额：");
//                    tv_jinE1.setText((tv_jinE.getText().toString() + tv_money.getText().toString()));
//                    tv_success.setText("充值成功！");
//                    id = 25;
                } else if (id == 25) {
                    ll_leiBiao.setVisibility(View.VISIBLE);
                    ll_yuEFind.setVisibility(View.GONE);
                    ll_yuEShow.setVisibility(View.GONE);
                    rl_chong.setVisibility(View.GONE);
                    tv_success.setVisibility(View.GONE);
                    t1.setText("1");
                    t2.setText("2");
                    t3.setText("3");
                    t1.setTextColor(getResources().getColor(R.color.white));
                    t1.setBackgroundResource(R.drawable.banner_indicator_selected);
                    t2.setTextColor(getResources().getColor(R.color.black));
                    t2.setBackgroundResource(R.drawable.banner_indicator_selected1);
                    t3.setTextColor(getResources().getColor(R.color.black));
                    t3.setBackgroundResource(R.drawable.banner_indicator_selected1);
                    initSelect();
                    tv_10.setBackgroundResource(R.drawable.footer_bg);
                    tv_10.setTextColor(getResources().getColor(R.color.white));
                    tv_zi.setText("");
                    tv_zi.setHintTextColor(getResources().getColor(R.color.black1));
                    money = "10元";
                    flag = 1;
                    ll_chong.setEnabled(true);
                }
                break;
            case R.id.ll_yuEFind:
                break;
        }
    }
    //查询
    private void chaXun() {
        ll_fanying.setVisibility(View.GONE);
        ll_duka.setVisibility(View.VISIBLE);
        tv_du.setText("正在读卡");
        String aid = "00A4040009A00000000386980701";//选合肥通aid
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(aid);
        String data = init(dataXor);
        if (data == null) {
            data = init(dataXor);
            if (data == null) {
                data = init(dataXor);
                if (data == null) {
                    TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                    initKongJian();
                    return;
                }
            }
        }
        final Handler handler = new Handler();
        final String finalData1 = data;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String substring = finalData1.substring(finalData1.length() - 12, finalData1.length() - 8);
                if (finalData1 != null && !finalData1.equals("") && substring.equals("9000")) {
                    String aid = "805c000204";//读余额
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String data = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
//                                                TcnUtility.getToast(getActivity(),data);
                            if (data != null && !data.equals("")) {
                                String substring = data.substring(data.length() - 20, data.length() - 12);
                                Log.d("TAG", "run: " + substring);
                                final int start = Integer.parseInt(substring, 16);
                                Log.d("TAG", "run: " + start);
                                gLogger.debug("住建部读余额返回余额值：" + start);
                                String aid = "00B0950000";//读0015文件
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String data = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (data != null && !data.equals("")) {
                                            String substring = data.substring(10, data.length() - 4);
                                            String sub = substring.substring(20, substring.length() - 8);
//                                                                String applicationId = sub.substring(0, 4);//应用标识
//                                                                String cityCode = sub.substring(4, 8);//城市代码
//                                                                String industryCode = sub.substring(8, 12);//a行业代码
//                                                                String RFU = sub.substring(12, 16);//RFU
//                                                                String enableSign = sub.substring(16, 18);//启用标志 00：未启用 01：启用
                                            String cardNum = sub.substring(32, 40);//卡号
                                            Log.d("TAG", "aaaav: " + substring);
                                            Double d = (double) start / 100;
                                            DecimalFormat df = new DecimalFormat("#0.00");
                                            YuE = df.format(d);
                                            Log.d("TAG", "runD: " + YuE);
                                            tv_money.setText(YuE + "元");
                                            tv_card.setText(cardNum);

                                            //发送移卡指令、关闭串口
                                            String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                            final String dataYiKa = init(yiKa);
                                            Log.d("TAG", "onViewClicked: " + dataYiKa);
                                            zhongjiSerial.closeSerialPort();

                                            ll_duka.setVisibility(View.GONE);
                                            ll_yu.setVisibility(View.GONE);
                                            ll_yu1.setVisibility(View.VISIBLE);
                                            ll_yuEShow.setVisibility(View.VISIBLE);
                                            ll_jinE.setVisibility(View.GONE);
                                            ll_jinE1.setVisibility(View.GONE);
                                            btn_return.setVisibility(View.VISIBLE);
                                            btn_return.setText("确认");
                                            id = 0;
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            } else {
                                TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                initKongJian();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                } else {
                    String aid = "00A4040008A000000333010106";//选中银通aid
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String data = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            String substring = data.substring(data.length() - 12, data.length() - 8);
                            if (data != null && !data.equals("") && substring.equals("9000")) {
                                String aid = "80CA9F7900";//读余额
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String data = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
//                                                            TcnUtility.getToast(getActivity(),data);
                                        if (data != null && !data.equals("")) {
                                            String s = data.substring(10, data.length() - 12);
                                            String[] split = s.split("9F79");
                                            if (split.length != 2) {
                                                TcnUtility.getToast(getActivity(), "请重新贴卡");
                                                initKongJian();
                                                return;
                                            }
                                            String substring = split[1].substring(2);
//                                                                String substring = data.substring(data.length() - 16, data.length() - 12);
                                            Log.d("TAG", "run: " + substring);
                                            final int start = Integer.parseInt(substring);
                                            Log.d("TAG", "run: " + start);
                                            String aid = "00B2011400";//读卡号
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (data != null && !data.equals("")) {
                                                        String substring = data.substring(10, data.length() - 4);
                                                        String sub = substring.substring(20, substring.length() - 8);
                                                        String[] strings = sub.split("5A");
                                                        String s1 = strings[1].substring(0, 2);
                                                        int s = Integer.parseInt(s1, 16);
                                                        String cardNum = strings[1].substring(2, (s * 2 + 1));
//                                                                            String cardNum = sub.substring(8, 27);//卡号
                                                        Log.d("TAG", "aaaavv: " + substring);
                                                        Double d = (double) start / 100;
                                                        DecimalFormat df = new DecimalFormat("#0.00");
                                                        YuE = df.format(d);
                                                        Log.d("TAG", "runD: " + YuE);
                                                        tv_money.setText(YuE + "元");
                                                        tv_card.setText(cardNum);

                                                        //发送移卡指令、关闭串口
                                                        String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                        final String dataYiKa = init(yiKa);
                                                        Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                        zhongjiSerial.closeSerialPort();

                                                        ll_duka.setVisibility(View.GONE);
                                                        ll_yu.setVisibility(View.GONE);
                                                        ll_yu1.setVisibility(View.VISIBLE);
                                                        ll_yuEShow.setVisibility(View.VISIBLE);
                                                        ll_jinE.setVisibility(View.GONE);
                                                        ll_jinE1.setVisibility(View.GONE);
                                                        btn_return.setVisibility(View.VISIBLE);
                                                        btn_return.setText("确认");
                                                        id = 0;
                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        } else {
                                            TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                            initKongJian();
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            } else {
                                String aid = "00A4040008A000000333010101";//选中银通62bin aid
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String data = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        String substring = data.substring(data.length() - 12, data.length() - 8);
                                        if (data != null && !data.equals("") && substring.equals("9000")) {
                                            String aid = "80CA9F7900";//读余额
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
//                                                            TcnUtility.getToast(getActivity(),data);
                                                    if (data != null && !data.equals("")) {
                                                        String s = data.substring(10, data.length() - 12);
                                                        String[] split = s.split("9F79");
                                                        if (split.length != 2) {
                                                            TcnUtility.getToast(getActivity(), "请重新贴卡");
                                                            initKongJian();
                                                            return;
                                                        }
                                                        String substring = split[1].substring(2);
//                                                                String substring = data.substring(data.length() - 16, data.length() - 12);
                                                        Log.d("TAG", "run: " + substring);
                                                        final int start = Integer.parseInt(substring);
                                                        Log.d("TAG", "run: " + start);
                                                        String aid = "00B2011400";//读卡号
                                                        //异或校验  十六进制串
                                                        String dataXor = Utils.dataXor(aid);
                                                        final String data = init(dataXor);
                                                        final Handler handler = new Handler();
                                                        final Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (data != null && !data.equals("")) {
                                                                    String substring = data.substring(10, data.length() - 4);
                                                                    String sub = substring.substring(20, substring.length() - 8);
                                                                    String[] strings = sub.split("5A");
                                                                    String s1 = strings[1].substring(0, 2);
                                                                    int s = Integer.parseInt(s1, 16);
                                                                    String cardNum = strings[1].substring(2, (s * 2 + 1));
//                                                                            String cardNum = sub.substring(8, 27);//卡号
                                                                    Log.d("TAG", "aaaavv: " + substring);
                                                                    Double d = (double) start / 100;
                                                                    DecimalFormat df = new DecimalFormat("#0.00");
                                                                    YuE = df.format(d);
                                                                    Log.d("TAG", "runD: " + YuE);
                                                                    tv_money.setText(YuE + "元");
                                                                    tv_card.setText(cardNum);

                                                                    //发送移卡指令、关闭串口
                                                                    String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                                    final String dataYiKa = init(yiKa);
                                                                    Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                                    zhongjiSerial.closeSerialPort();

                                                                    ll_duka.setVisibility(View.GONE);
                                                                    ll_yu.setVisibility(View.GONE);
                                                                    ll_yu1.setVisibility(View.VISIBLE);
                                                                    ll_yuEShow.setVisibility(View.VISIBLE);
                                                                    ll_jinE.setVisibility(View.GONE);
                                                                    ll_jinE1.setVisibility(View.GONE);
                                                                    btn_return.setVisibility(View.VISIBLE);
                                                                    btn_return.setText("确认");
                                                                    id = 0;
                                                                } else {
                                                                    TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                                    initKongJian();
                                                                }
                                                            }
                                                        };
                                                        handler.postDelayed(runnable, 1);//线程时间3秒
                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        } else {
                                            String aid = "00A4040008A000000632010105";//选交通卡aid
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    String substring = data.substring(data.length() - 12, data.length() - 8);
                                                    if (data != null && !data.equals("") && substring.equals("9000")) {
                                                        String aid = "805c000204";//读余额
                                                        //异或校验  十六进制串
                                                        String dataXor = Utils.dataXor(aid);
                                                        final String readYuE = init(dataXor);
                                                        final Handler handler = new Handler();
                                                        final Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (readYuE != null && !readYuE.equals("")) {
                                                                    String substring = readYuE.substring(readYuE.length() - 20, readYuE.length() - 12);
                                                                    Log.d("TAG", "run: " + substring);
                                                                    final int start = Integer.parseInt(substring, 16);
                                                                    Log.d("TAG", "run: " + start);
                                                                    gLogger.debug("交通卡读余额返回余额值：" + start);
                                                                    String aid = "00B0950000";//15文件
                                                                    //异或校验  十六进制串
                                                                    String dataXor = Utils.dataXor(aid);
                                                                    final String read0015 = init(dataXor);
                                                                    final Handler handler = new Handler();
                                                                    final Runnable runnable = new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if (read0015 != null && !read0015.equals("")) {
                                                                                String substring = read0015.substring(10, read0015.length() - 4);
                                                                                String sub = substring.substring(20, substring.length() - 8);
                                                                                String cardNum = sub.substring(21, 40);//卡号
                                                                                Log.d("TAG", "aaaav: " + substring);
                                                                                Double d = (double) start / 100;
                                                                                DecimalFormat df = new DecimalFormat("#0.00");
                                                                                YuE = df.format(d);
                                                                                Log.d("TAG", "runD: " + YuE);
                                                                                tv_money.setText(YuE + "元");
                                                                                tv_card.setText(cardNum);

                                                                                //发送移卡指令、关闭串口
                                                                                String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                                                final String dataYiKa = init(yiKa);
                                                                                Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                                                zhongjiSerial.closeSerialPort();

                                                                                ll_duka.setVisibility(View.GONE);
                                                                                ll_yu.setVisibility(View.GONE);
                                                                                ll_yu1.setVisibility(View.VISIBLE);
                                                                                ll_yuEShow.setVisibility(View.VISIBLE);
                                                                                ll_jinE.setVisibility(View.GONE);
                                                                                ll_jinE1.setVisibility(View.GONE);
                                                                                btn_return.setVisibility(View.VISIBLE);
                                                                                btn_return.setText("确认");
                                                                                id = 0;
                                                                            } else {
                                                                                TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                                                initKongJian();
                                                                            }
                                                                        }
                                                                    };
                                                                    handler.postDelayed(runnable, 1);//线程时间3秒
                                                                } else {
                                                                    TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                                                    initKongJian();
                                                                }
                                                            }
                                                        };
                                                        handler.postDelayed(runnable, 1);//线程时间3秒
                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒
    }

    @InjectView(R.id.gv)
    MyGridView gv;
    private List<SettingBean> list = null;
    private PayAdapter1 adapter = null;

    //获取支付方式
    private void initPayData() {
//        String name[] = {"微信","支付宝","银联"};
//        int img[] = {R.drawable.weixin, R.drawable.zhifubao, R.drawable.yinlian};
        list = new ArrayList<>();
        String payment = spf.getString("payment", "");
        Log.d(TAG, "initPayData: " + payment);
        try {
            JSONArray jsonArray = new JSONArray(payment);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");//支付方式的名称
                String logo_c = object.getString("logo_c");//支付方式的图标
                //支付方式
                String channel_code = object.getString("channel_code");
                SettingBean bean = new SettingBean();
                bean.setImgName(name);
                bean.setImgUrl(Comment.URL_IMG_OR_VIDEO + "/image/" + logo_c);
                bean.setImgPay(channel_code);
                boolean showWeiXin = spf.getBoolean("showWeiXin", false);
                boolean showAlipay = spf.getBoolean("showAlipay", false);
                boolean showUnionpay = spf.getBoolean("showUnionpay", false);
                //
                if (name.equals("微信支付") && showWeiXin) {
                    list.add(bean);
                }
                if (name.equals("支付宝") && showAlipay) {
                    list.add(bean);
                }
                if (name.equals("银联二维码") && showUnionpay) {
                    list.add(bean);
                }
                if (list.size() == 1) gv.setNumColumns(1);
                else if (list.size() == 2) gv.setNumColumns(2);
                else if (list.size() == 3) gv.setNumColumns(3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "initPayData: " + list);
        setPay();
    }

    private int code = -1;//二维码获取成功or失败 0：失败 1：成功
    private String channel_code;//支付方式

    private void setPay() {
        adapter = new PayAdapter1(list, getActivity(), width, height);
        if (gv != null) {
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                    String zi = tv_zi.getText().toString().trim();
                    Log.d(TAG, "onItemClick: " + zi);
                    if (!zi.equals("")) {
                        int zi_money = Integer.parseInt(zi);
                        if (zi_money % 10 != 0) {
                            TcnUtility.getToast(getActivity(), "充值金额必须为整数和10的倍数");
                            return;
                        }
                    }
                    gv.setEnabled(false);
                    btn_return.setEnabled(false);
                    if (flag == 5) {
                        money = tv_zi.getText().toString() + "元";
                    }
//                    jinE = money.substring(0, money.length() - 1);//测试使用
                    jinE = "1";//测试使用
                    gLogger.debug("余额："+ YuE);
                    gLogger.debug("充值金额："+ jinE);
                    Double money1 = Double.valueOf(YuE) + Double.valueOf(jinE);// 领款/充值金额和余额
                    gLogger.debug("余额+充值金额："+ money1);
                    if(money1 > 1000){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                        Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                        TextView tv_message = aalayout.findViewById(R.id.tv_message);
                        tv_message.setText("金额超限，充值失败");
                        builder.setCancelable(false)
                                .setView(aalayout);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initKongJian();
                                dialog.dismiss();
                            }
                        });
                        return;
                    }
                    SettingBean bean = list.get(position);
                    String shopName = bean.getImgName();
                    channel_code = bean.getImgPay();//支付方式
                    if (type1.equals(hft_card)) { //00:电子钱包（合肥通） 01：电子现金（中银通 92bin）7：电子钱包（交通卡）8：电子现金（中银通 62bin）
                        String xun = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                        String xunK = init(xun);
                        if (xunK == null) {
                            init();
                            xunK = init(xun);
                        }
                        final Handler handler1 = new Handler();
                        final String finalXunK = xunK;
                        final Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                String substring = finalXunK.substring(finalXunK.length() - 8, finalXunK.length() - 4);
                                if (substring.equals("9000")) {
                                    //调合肥通圈存
                                    initQuanCun("");
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                                    Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                                    TextView tv_message = aalayout.findViewById(R.id.tv_message);
                                    tv_message.setText("卡片被移开，请重新放卡");
                                    builder.setCancelable(false)
                                            .setView(aalayout);
                                    final AlertDialog dialog = builder.create();
                                    dialog.show();
                                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initKongJian();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        };
                        handler1.postDelayed(runnable1, 500);//线程时间0.5秒
                    } else if (type1.equals(zhongyin__nine_card) || type1.equals(zhongyin_six_card)) {
                        getData(channel_code, iv_tu);
                    } else if (type1.equals(hft_traffic_card)) {
                        String xun = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                        String xunK = init(xun);
                        if (xunK == null) {
                            init();
                            xunK = init(xun);
                        }
                        final Handler handler1 = new Handler();
                        final String finalXunK = xunK;
                        final Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                String substring = finalXunK.substring(finalXunK.length() - 8, finalXunK.length() - 4);
                                if (substring.equals("9000")) {
                                    //调交通卡圈存
                                    initQuanCun1("");
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                                    Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                                    TextView tv_message = aalayout.findViewById(R.id.tv_message);
                                    tv_message.setText("卡片被移开，请重新放卡");
                                    builder.setCancelable(false)
                                            .setView(aalayout);
                                    final AlertDialog dialog = builder.create();
                                    dialog.show();
                                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initKongJian();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        };
                        handler1.postDelayed(runnable1, 500);//线程时间0.5秒
                    }
                    i = 0;
                    code = -1;
                    switch (shopName) {
                        case "微信支付":
                            q = 0;
//                            getData(channel_code, iv_tu);
                            ll_fangShi.setVisibility(View.GONE);
                            ll_select.setVisibility(View.GONE);
                            ll_duka.setVisibility(View.VISIBLE);
                            tv_yu_chong.setText("充值金额：");
                            if (flag == 5) {
                                money = tv_zi.getText().toString() + "元";
                            }
                            tv_chongzhi.setText(money);
                            iv_tu.setImageResource(R.drawable.loading);
                            tv_du.setText("请打开微信扫码充值");
                            btn_return.setText("返回");
                            btn_return.setVisibility(View.VISIBLE);
                            id = 21;
                            break;
                        case "支付宝":
                            q = 0;
//                            getData(channel_code, iv_tu);
                            ll_fangShi.setVisibility(View.GONE);
                            ll_select.setVisibility(View.GONE);
                            ll_duka.setVisibility(View.VISIBLE);
                            tv_yu_chong.setText("充值金额：");
                            if (flag == 5) {
                                money = tv_zi.getText().toString() + "元";
                            }
                            tv_chongzhi.setText(money);
                            iv_tu.setImageResource(R.drawable.loading);
                            tv_du.setText("请打开支付宝扫码充值");
                            btn_return.setText("返回");
                            btn_return.setVisibility(View.VISIBLE);
                            id = 21;
                            break;
                        case "银联二维码":
                            q = 0;
//                            getData(channel_code, iv_tu);
                            ll_fangShi.setVisibility(View.GONE);
                            ll_select.setVisibility(View.GONE);
                            ll_duka.setVisibility(View.VISIBLE);
                            tv_yu_chong.setText("充值金额：");
                            if (flag == 5) {
                                money = tv_zi.getText().toString() + "元";
                            }
                            tv_chongzhi.setText(money);
                            iv_tu.setImageResource(R.drawable.loading);
                            tv_du.setText("请打开云闪付扫码充值");
                            btn_return.setText("返回");
                            btn_return.setVisibility(View.VISIBLE);
                            id = 21;
                            break;
                    }
                }
            });
        }
    }

    //订单提交并获取充值支付的二维码等数据
    int q;
    private String type1;//电子钱包or电子现金（中银通92bin的）or电子钱包（交通卡）or电子现金（中银通62bin的）
    private  final String hft_card = "00";//电子钱包（合肥通）
    private  final String zhongyin__nine_card = "01";//中银通 92bin
    private  final String zhongyin_six_card = "8";//中银通 62bin
    private  final String hft_traffic_card = "7";//电子钱包（交通卡）
    private void getData(final String payment, final ImageView iv_erweima) {
        btn_return.setEnabled(true);
        gv.setEnabled(true);
        code = 1;
        String url = Comment.URL + "/Hftrechage/create";
        String terminal_no = spf.getString("terminal_no", "");
        Log.d(TAG, "getData: " + key);
//        TcnUtility.getToast(getActivity(),"秘钥的值："+key);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Map<String, String> params = new HashMap<>();
        params.put("cardid", cardNum);//卡号
        params.put("payment", payment);//支付方式
        params.put("type", type1);//支付方式
        String price = "1";
//        String price = this.money.substring(0, money.length() - 1);
        params.put("price", price);//充值金额
        params.put("source", "terminal");//接口渠道 （固定值terminal）
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("terminal_no", terminal_no);//终端编号（设备定义的）
        String terminal_order_no = terminal_no + "-" + str;
        params.put("terminal_order_no", terminal_order_no);//终端订单号 用设备定]义的终端编号+时间戳
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("cardid=" + cardNum + "&payment=" + payment + "&price=" + price + "&source=terminal" + "&terminal_code="
                + Comment.terminal_code + "&terminal_no=" + terminal_no + "&terminal_order_no=" + terminal_order_no + "&timestamp=" + timeStamp + "&type=" + type1+ "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "getData1: " + params);
        if (q < 1) {
            OkHttpUtils.post().url(url).params(params).build().connTimeOut(2000).execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
//                    Log.d("TAG", "getData:二维码失败" + e);
                    gLogger.debug("onResponse:二维码失败" + e);
                    i = 0;
//                    initSignin(3);
                    code = 0;//获取失败
                    getData(payment, iv_erweima);
                    if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                        return;
                    }
                    GlideUtils.load(getActivity(), "", iv_erweima, R.drawable.fail1);
                    btn_return.setEnabled(true);
                    q++;
                }

                @Override
                public void onResponse(String response, int id) {
//                    Log.d("TAG", "getData:二维码成功" + response);
                    gLogger.debug("onResponse:二维码成功" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean code_ = jsonObject.getBoolean("code");
                        if (!code_) {
                            String err_desc = jsonObject.getString("err_desc");
                            TcnUtility.getToast(getActivity(), err_desc);
                            if (err_desc.equals("验签错误")) {
//                                initSignin(3);
                                return;
                            }
                            code = 0;//获取失败
                            if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                                return;
                            }
                            GlideUtils.load(getActivity(), "", iv_erweima, R.drawable.fail1);
                            btn_return.setEnabled(true);
                        } else {
                            code = 1;//获取成功
                            JSONObject data = jsonObject.getJSONObject("data");
                            //订单生成的时候返回的
                            order_no = data.getString("order_no");
                            JSONObject pay_info = data.getJSONObject("pay_info");
                            String code_img_url = pay_info.getString("code_url_image");//二维码图片
                            new_order_code = pay_info.optString("order_code");
                            Log.d(TAG, "onResponse: " + code_img_url);
                            if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                                return;
                            }
                            GlideUtils.load(getActivity(), code_img_url, iv_erweima);
                            btn_return.setEnabled(true);
                            a = 0;
                            cancle = false;
//                            initGPO("");

                            sockerStar();
//                            rechage_init("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            TcnUtility.getToast(getActivity(), "二维码获取请求超时");
        }
    }

    //充值初始化
    private void rechage_init(String order_code) {
        String url = Comment.URL + "/Hftrechage/rechage_init";
        Map<String, String> params = new HashMap<>();
        params.put("card_balance", String.valueOf(start));//余额 传分为单位的（1000）而不是元为单位的（10.00）
        params.put("card_num", cardNum);//卡号
        params.put("order_code", order_no);//订单号
        params.put("type", type1);//卡类型
        String trademoney = money.substring(0, money.length() - 1);
//        params.put("trademoney", trademoney);//充值金额
        Log.d("TAG", "data0005: " + data0005);
        Log.d("TAG", "data0015: " + data0015);
        Log.d("TAG", "init_result: " + init_result);
//            params.put("data0005", data0005);//05文件
//            params.put("data0015", data0015);//15文件
//            params.put("init_result", init_result);//圈存初始化
//            params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//            params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
//            String signature = MD5Util.encrypt("data0005=" + data0005 + "&data0015=" + data0015 + "&init_result=" + init_result + "&order_no=" + order_no+ "&terminal_code="+Comment.terminal_code+"&timestamp="+timeStamp + "&key=" +key);
        Log.d("TAG", "first_gac_result: " + first_gac_result);
        Log.d("TAG", "random: " + random);
//            params.put("first_gac_result", first_gac_result);//第一次GAC
//            params.put("random", random);//随机数
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("card_balance=" + start + "&card_num=" + cardNum + "&order_code=" + order_no + "&terminal_code=" + Comment.terminal_code +
                "&timestamp=" + timeStamp + "&type=" + type1 + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "rechage_init: " + params);
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.d("TAG", "getData:充值初始化失败" + e);
                gLogger.debug("onResponse:充值初始化失败" + e);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.d("TAG", "getData:充值初始化成功" + response);
                gLogger.debug("onResponse:充值初始化成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code_ = jsonObject.getBoolean("code");
                    if (!code_) {
                        String err_desc = jsonObject.getString("err_desc");
                        TcnUtility.getToast(getActivity(), err_desc);
                        if (err_desc.equals("验签错误")) {
//                                initSignin(3);
                            return;
                        }

                    } else {
                        ll_duka.setVisibility(View.VISIBLE);
                        ll_yuEShow.setVisibility(View.GONE);
                        iv_tu.setVisibility(View.VISIBLE);
                        iv_tu.setImageResource(R.drawable.zhengzaiduka_xieka);
                        btn_return.setVisibility(View.GONE);
                        tv_du.setText("支付成功，正在写卡");
                        t2.setText("");
                        t2.setBackgroundResource(R.drawable.wancheng);
                        t3.setTextColor(getResources().getColor(R.color.black));
                        t3.setBackgroundResource(R.drawable.banner_indicator_selected);
                        Log.d(TAG, "onResponseaaa: +++++++++++++++++" + type1);
                        type = type1;
                        getXieKaZhiLing();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Runnable runnables;
    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    break;
                case 0:
                    break;
            }
        }
    };
    //持续向后台发送订单查询
    int b;
    boolean cancle = false;//是否取消查询订单用
    private String new_status ;//新订单状态

    private void sockerStar() {
        //几秒后发送获取订单状态
        runnables = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handlers.postDelayed(this, 1000 * 3);
                String url = Comment.URL + "/Hftrechage/query";
//                String url = Comment.URL + "/order/query";
                Map<String, String> params = new HashMap<>();
                params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                params.put("order_code", new_order_code);//订单生成的时候返回的
                Log.d("TAG", "查询充值订单:url=" + url+",new_order_code="+new_order_code);
//
//                params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                String signature = MD5Util.encrypt("order_no=" + new_order_code + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
                params.put("signature", signature);//签名参数
                Log.d(TAG, "run: " + params);
                OkHttpUtils.post().url(url).params(params).build().connTimeOut(2000).execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("TAG", "onResponsesockerStar:失败" + e);
                        i = 0;
                        handlers.removeCallbacks(runnables);
                        sockerStar();
                        b++;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        gLogger.debug("查询充值订单:成功1111" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean code = jsonObject.getBoolean("code");
                            if (!code) {
                                String err_desc = jsonObject.getString("err_msg");
                                handlers.removeCallbacks(runnables);
                                if (err_desc.equals("验签错误")) {
                                    i = 0;
                                    b++;
                                }
                            } else {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject orderInfoObj = data.optJSONObject("order_info");
                                new_status = orderInfoObj.optString("status");
                                //00：未支付01：支付中02：已支付 03：充值中 04：充值成功05：充值失败06：退款中07：退款成功08：退款失败 09：订单已关闭 10：写卡未知 11：写卡失败

                                if(new_status.equals("02")){
                                    handlers.removeCallbacks(runnables);
                                    createtime = orderInfoObj.getString("createtime");
//                                    String trade_time = orderInfoObj.getString("createtime");
//                                    createtime = Utils.getDate2String(Long.parseLong(trade_time), "yyyyMMddHHmmss");
                                    if (type1.equals(hft_card) || type1.equals(hft_traffic_card)) {
                                        rechage_init("");
                                    } else if (type1.equals(zhongyin__nine_card) || type1.equals(zhongyin_six_card)) {
                                        String xun = "0200000010800000000600003C0000BECC01010000D803";//寻卡
                                        String xunK = init(xun);
                                        if (xunK == null) {
                                            init();
                                            xunK = init(xun);
                                        }
                                        final Handler handler1 = new Handler();
                                        final String finalXunK = xunK;
                                        final Runnable runnable1 = new Runnable() {
                                            @Override
                                            public void run() {
                                                String substring = finalXunK.substring(finalXunK.length() - 8, finalXunK.length() - 4);
                                                if (substring.equals("9000")) {
                                                    initGPO("");
                                                    ll_duka.setVisibility(View.VISIBLE);
                                                    ll_yuEShow.setVisibility(View.GONE);
                                                    btn_return.setVisibility(View.GONE);
                                                    tv_du.setText("正在写卡");
                                                    tv2.setText("");
                                                    tv2.setBackgroundResource(R.drawable.wancheng);
                                                    tv2.setTextColor(getResources().getColor(R.color.white));
                                                    tv3.setBackgroundResource(R.drawable.banner_indicator_selected);
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                    View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                                                    Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                                                    TextView tv_message = aalayout.findViewById(R.id.tv_message);
                                                    tv_message.setText("卡片被移开，请重新放卡");
                                                    builder.setCancelable(false)
                                                            .setView(aalayout);
                                                    final AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            initKongJian();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        };
                                        handler1.postDelayed(runnable1, 500);//线程时间0.5秒
                                    }
                                    return;
                                }
//                                if (order_status == 0) {
//                                    trade_status = data.getInt("trade_status");
//                                    if (trade_status == 1) {
//                                        refund_status = data.getInt("refund_status");
//                                        if (refund_status == 0) {
//                                            deliver_status = data.getInt("deliver_status");
//                                            if (deliver_status == 0) {
//                                                handlers.removeCallbacks(runnables);
//                                                //订单交易时间
//                                                String trade_time = data.getString("trade_time");
//                                                createtime = Utils.getDate2String(Long.parseLong(trade_time), "yyyyMMddHHmmss");
//                                                Log.d(TAG, "onResponse订单交易时间: " + createtime);
//
//                                            }
//                                        }
//                                    }
//                                } else if (order_status == -1) {
//                                    handlers.removeCallbacks(runnables);
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Message message = new Message();
                message.what = -1;
                handlers.sendMessage(message);
            }

        };
        handlers.postDelayed(runnables, 3000);//线程时间1秒刷新
    }

    //取消订单
    int numCancle;

    private void orderCancle() {
        Log.d(TAG, "orderCancle: " + key);
        Log.d(TAG, "orderCancle: " + timeStamp);
        handlers.removeCallbacks(runnables);
        //订单取消
        String url = Comment.URL + "/order/cancel";
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
//        OkHttpUtils.post().url(url).params(params).build().connTimeOut(5000).execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.d("TAG", "onResponse:失败" + e);
//                try {
//                    handlers.removeCallbacks(runnables);
//                    orderCancle();
//                    i = 0;
//                    numCancle++;
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                Log.d("TAG", "onResponseCancle:成功" + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean code = jsonObject.getBoolean("code");
//                    if (!code) {
//                        String err_desc = jsonObject.getString("err_desc");
//                        handlers.removeCallbacks(runnables);
//                        if (err_desc.equals("验签错误")) {
//                            i = 0;
//                            numCancle++;
//                        }
//                    } else {
//                        handlers.removeCallbacks(runnables);
//                    }
//                    gv.setEnabled(true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    //选择aid
    private void selectAID(String xunKaBu) {
        if (xunKaBu != null && !xunKaBu.equals("")) {
            String aid = "00A4040009A00000000386980701";//选合肥通aid
            //异或校验  十六进制串
            String dataXor = Utils.dataXor(aid);
            String dAid = init(dataXor);
            if (dAid == null) {
                dAid = init(dataXor);
                if (dAid == null) {
                    dAid = init(dataXor);
                    if (dAid == null) {
                        TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                        initKongJian();
                        return;
                    }
                }
            }
            final Handler handler = new Handler();
            final String finalDAid = dAid;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String substring = finalDAid.substring(finalDAid.length() - 12, finalDAid.length() - 8);
                    if (finalDAid != null && !finalDAid.equals("") && substring.equals("9000")) {
                        type1 = hft_card;//电子钱包（合肥通）
                        readYuE(finalDAid);
                    } else {
                        String aid = "00A4040008A000000333010106";//选中银通92bin aid
                        //异或校验  十六进制串
                        String dataXor = Utils.dataXor(aid);
                        final String dAid = init(dataXor);
                        if (dAid == null) {
                            TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                            initKongJian();
                            return;
                        }
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
//                                readYuE1(dAid);
                                String substring = dAid.substring(dAid.length() - 12, dAid.length() - 8);
                                Log.d(TAG, "run: " + substring);
                                if (dAid != null && !dAid.equals("") && substring.equals("9000")) {
                                    type1 = zhongyin__nine_card;//中银通 92bin
                                    String[] strings = dAid.split("9F38");
                                    if (strings.length > 0) {
                                        String s1 = strings[1].substring(0, 2);
                                        int s = Integer.parseInt(s1, 16);
                                        gpoJoint = strings[1].substring(2, (s * 2) + 2);//GPO拼接顺序
                                    }
                                    Log.d(TAG, "run: " + gpoJoint);
                                    readYuE1(dAid);
                                } else {
                                    String aid = "00A4040008A000000333010101";//选中银通62bin aid
                                    //异或校验  十六进制串
                                    String dataXor = Utils.dataXor(aid);
                                    final String dAid = init(dataXor);
                                    if (dAid == null) {
                                        TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                                        initKongJian();
                                        return;
                                    }
                                    final Handler handler = new Handler();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
//                                          readYuE1(dAid);
                                            String substring = dAid.substring(dAid.length() - 12, dAid.length() - 8);
                                            Log.d(TAG, "run: " + substring);
                                            if (dAid != null && !dAid.equals("") && substring.equals("9000")) {
                                                type1 = zhongyin_six_card;//中银通 62bin
                                                String[] strings = dAid.split("9F38");
                                                if (strings.length > 0) {
                                                    String s1 = strings[1].substring(0, 2);
                                                    int s = Integer.parseInt(s1, 16);
                                                    gpoJoint = strings[1].substring(2, (s * 2) + 2);//GPO拼接顺序
                                                }
                                                Log.d(TAG, "run: " + gpoJoint);
                                                readYuE1(dAid);
                                            } else {
//                                                if (id == 20) {
                                                String aid = "00A4040008A000000632010105";//选交通卡aid
                                                //异或校验  十六进制串
                                                String dataXor = Utils.dataXor(aid);
                                                final String dAid = init(dataXor);
                                                if (dAid == null) {
                                                    TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                                                    initKongJian();
                                                    return;
                                                }
                                                final Handler handler = new Handler();
                                                final Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        type1 = hft_traffic_card;//电子钱包（交通卡）
                                                        String substring = dAid.substring(dAid.length() - 12, dAid.length() - 8);
                                                        if (dAid != null && !dAid.equals("") && substring.equals("9000")) {
                                                            readYuE(dAid);
                                                        } else {
                                                            TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                                                            initKongJian();
                                                        }
                                                    }
                                                };
                                                handler.postDelayed(runnable, 1);//线程时间3秒
//                                                } else {
//                                                    TcnUtility.getToast(getActivity(),"读卡失败，请重新贴卡");
//                                                    initKongJian();
//                                                }
                                            }
                                        }
                                    };
                                    handler.postDelayed(runnable, 1);//线程时间3秒
                                }
                            }
                        };
                        handler.postDelayed(runnable, 1);//线程时间3秒
                    }
                }
            };
            handler.postDelayed(runnable, 1);//线程时间3秒
        }
    }

    //读取余额(合肥通)
    private void readYuE(String dAid) {
        if (dAid != null && !dAid.equals("")) {
//            Log.d("TAG", "onViewClicked2: " + dAid);
            String aid = "805c000204";//读余额
            //异或校验  十六进制串
            String dataXor = Utils.dataXor(aid);
            final String readYuE = init(dataXor);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    read15File(readYuE);
                }
            };
            handler.postDelayed(runnable, 1);//线程时间3秒
        }
    }

    //读取余额(中银通)
    private void readYuE1(String dAid) {
        if (dAid != null && !dAid.equals("")) {
//            Log.d("TAG", "onViewClicked2: " + dAid);
            String aid = "80CA9F7900";//读余额
            //异或校验  十六进制串
            String dataXor = Utils.dataXor(aid);
            final String readYuE = init(dataXor);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    readFile(readYuE);
                }
            };
            handler.postDelayed(runnable, 1);//线程时间3秒
        }
    }

    //读取15文件（合肥通）
    private void read15File(String readYuE) {
        if (readYuE != null && !readYuE.equals("")) {
//            Log.d("TAG", "onViewClicked3: " + readYuE);
            String substring = readYuE.substring(readYuE.length() - 20, readYuE.length() - 12);
            Log.d("TAG", "run: " + substring);
            start = Integer.parseInt(substring, 16);
            Log.d("TAG", "run: " + start);
            gLogger.debug("读余额返回余额值：" + start);
            String aid = "00B0950000";//15文件
            //异或校验  十六进制串
            String dataXor = Utils.dataXor(aid);
            final String read0015 = init(dataXor);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (read0015 != null && !read0015.equals("")) {
//                        Log.d("TAG", "onViewClicked4: " + read0015);
                        String substring = read0015.substring(10, read0015.length() - 4);
                        String sub = substring.substring(20, substring.length() - 8);
                        data0015 = sub;
//                                                                String applicationId = sub.substring(0, 4);//应用标识
//                                                                String cityCode = sub.substring(4, 8);//城市代码
//                                                                String industryCode = sub.substring(8, 12);//a行业代码
//                                                                String RFU = sub.substring(12, 16);//RFU
//                                                                String enableSign = sub.substring(16, 18);//启用标志 00：未启用 01：启用
                        if (type1.equals(hft_card)) {
                            cardNum = sub.substring(32, 40);//卡号（合肥通）
                        } else if (type1.equals(hft_traffic_card)) {
                            cardNum = sub.substring(21, 40);//卡号（交通卡）
                        }
//                        type1 = sub.substring(28, 32);//电子现金or电子钱包

                        Double d = (double) start / 100;
                        DecimalFormat df = new DecimalFormat("#0.00");
                        YuE = df.format(d);
                        Log.d("TAG", "runD: " + YuE);
                        if (id == 10) {
                            tv_money.setText(YuE + "元");
                            tv_card.setText(cardNum);
                            btn_return.setText("领款");
                            initGetBuDengLingKuanMoney(start, cardNum);
                        } else if (id == 20) {
                            ll_duka.setVisibility(View.GONE);
                            ll_chongzhi.setVisibility(View.VISIBLE);
                            ll_select.setVisibility(View.VISIBLE);
                            ll_fangShi.setVisibility(View.VISIBLE);
                            t1.setText("");
                            t1.setBackgroundResource(R.drawable.wancheng);
                            t2.setTextColor(getResources().getColor(R.color.white));
                            t2.setBackgroundResource(R.drawable.banner_indicator_selected);
                            btn_return.setVisibility(View.VISIBLE);
                            initGet05File("");
                            tv_yu_chong.setText("卡内余额：");
                            tv_chongzhi.setText(YuE + "元");
                            tv_card1.setText(cardNum);
                            btn_return.setText("下一步");
                            btn_return.setVisibility(View.GONE);
                        }
                    } else {
                        TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                        initKongJian();
                    }
                }
            };
            handler.postDelayed(runnable, 1);//线程时间3秒
        } else {
            TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
            initKongJian();
        }
    }

    //读取卡号（中银通）
    private void readFile(String readYuE) {
        if (readYuE != null && !readYuE.equals("")) {
//            Log.d("TAG", "onViewClicked3: " + readYuE);
            String s = readYuE.substring(10, readYuE.length() - 12);
            String[] split = s.split("9F79");
            String substring = split[1].substring(2);
//            String substring = readYuE.substring(readYuE.length() - 16, readYuE.length() - 12);
            Log.d("TAG", "run: " + substring);
            start = Integer.parseInt(substring);
            Log.d("TAG", "run: " + start);
            String aid = "00B2011400";//读卡号
            //异或校验  十六进制串
            String dataXor = Utils.dataXor(aid);
            final String read0015 = init(dataXor);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (read0015 != null && !read0015.equals("")) {
//                        Log.d("TAG", "onViewClicked4: " + read0015);
                        String substring = read0015.substring(10, read0015.length() - 4);
                        String sub = substring.substring(20, substring.length() - 8);
                        data0015 = sub;
//                                                                String applicationId = sub.substring(0, 4);//应用标识
//                                                                String cityCode = sub.substring(4, 8);//城市代码
//                                                                String industryCode = sub.substring(8, 12);//a行业代码
//                                                                String RFU = sub.substring(12, 16);//RFU
//                                                                String enableSign = sub.substring(16, 18);//启用标志 00：未启用 01：启用
                        //卡号
                        String[] strings = sub.split("5A");
                        String s1 = strings[1].substring(0, 2);
                        int s = Integer.parseInt(s1, 16);
                        cardNum = strings[1].substring(2, (s * 2 + 1));

//                        cardNum = sub.substring(8, 27);
                        Log.d("TAG", "aaaavv: " + substring);
                        Double d = (double) start / 100;
                        DecimalFormat df = new DecimalFormat("#0.00");
                        YuE = df.format(d);
                        Log.d("TAG", "runD: " + YuE);
                        if (id == 10) {
                            tv_money.setText(YuE + "元");
                            tv_card.setText(cardNum);
                            btn_return.setText("领款");
                            initGetBuDengLingKuanMoney(start, cardNum);
                        } else if (id == 20) {
                            ll_duka.setVisibility(View.GONE);
                            ll_chongzhi.setVisibility(View.VISIBLE);
                            ll_select.setVisibility(View.VISIBLE);
                            ll_fangShi.setVisibility(View.VISIBLE);
                            t1.setText("");
                            t1.setBackgroundResource(R.drawable.wancheng);
                            t2.setTextColor(getResources().getColor(R.color.white));
                            t2.setBackgroundResource(R.drawable.banner_indicator_selected);
                            btn_return.setVisibility(View.VISIBLE);
                            initPayData();
                            tv_yu_chong.setText("卡内余额：");
                            tv_chongzhi.setText(YuE + "元");
                            tv_card1.setText(cardNum);
                            btn_return.setText("下一步");
                            btn_return.setVisibility(View.GONE);
                        }
                    } else {
                        TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                        initKongJian();
                    }
                }
            };
            handler.postDelayed(runnable, 1);//线程时间3秒
        } else {
            TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
            initKongJian();
        }
    }

    //获取补登待领款金额
    int i;

    private void initGetBuDengLingKuanMoney(final int start, final String cardNum) {
        String url = Comment.URL + "/hft/query";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Log.d("TAG", "initGetBuDengLingKuanMoney: " + str);
        Log.d("TAG", "initGetBuDengLingKuan: " + key);
//        Date date = null;
//        try {
//            date = formatter.parse(str);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long timeStamp = date.getTime() / 1000;
        String terminal_no = spf.getString("terminal_no", "");
        order_no = terminal_no + "-" + str;//00010001：terminal_no设备定义的终端编号
        Map<String, String> params = new HashMap<>();
        params.put("card_num", cardNum);//卡号
        params.put("balance", String.valueOf(start));//当前余额 单位分 不是传10.00 要传1000
        params.put("order_no", String.valueOf(order_no));//终端订单号 用设备定义的终端编号+时间戳
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("balance=" + start + "&card_num=" + cardNum + "&order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:请求失败" + e);
                gLogger.debug("onResponse:请求失败" + e);
                if (i < 2) {
                    try {
                        Thread.sleep(5000);
                        i++;
                        initGetBuDengLingKuanMoney(start, cardNum);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    TcnUtility.getToast(getActivity(), "获取补登金额失败，请从新选择");
                    i = 0;
                    initKongJian();
                }
            }

            @Override
            public void onResponse(String response, int id_) {
                Log.d("TAG", "onResponse:补登返回" + response);
                gLogger.debug("onResponse:补登返回" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (code) {
                        jsonObject = jsonObject.getJSONObject("order");
                        order_code = jsonObject.getString("order_code");
                        jinE = jsonObject.getString("money");
                        //卡种
                        type = jsonObject.getString("type");
                        //订单交易时间
                        createtime = jsonObject.getString("createtime");
                        ll_duka.setVisibility(View.GONE);
                        ll_yuEShow.setVisibility(View.VISIBLE);
                        ll_jinE.setVisibility(View.VISIBLE);
                        ll_jinE1.setVisibility(View.GONE);
                        tv1.setText("");
                        tv1.setBackgroundResource(R.drawable.wancheng);
                        tv2.setTextColor(getResources().getColor(R.color.white));
                        tv2.setBackgroundResource(R.drawable.banner_indicator_selected);
                        tv_jin.setText("补登余额：");
                        btn_return.setVisibility(View.VISIBLE);
                        tv_jinE.setText(jinE + ".00元");
                    } else {
                        String err_desc = jsonObject.getString("err_desc");
                        if (err_desc.equals("无订单")) {
                            err_desc = "没有需要补登的金额";
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View aalayout = View.inflate(getActivity(), R.layout.layout_hint, null);
                        Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
                        TextView tv_message = aalayout.findViewById(R.id.tv_message);
                        tv_message.setText(err_desc);
                        builder.setCancelable(false)
                                .setView(aalayout);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initKongJian();
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //获取05文件
    private void initGet05File(String xunK) {
        String mf = "00A40000023F00";//选MF
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(mf);
        final String getMF = init(dataXor);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                Log.d("TAG", "onViewClicked1: " + getMF);
                if (getMF != null && !getMF.equals("")) {
                    String aid = "00B0850000";//05文件
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String read0005 = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
//                            Log.d("TAG", "onViewClicked1: " + read0005);
                            if (read0005 != null && !read0005.equals("")) {
                                String substring = read0005.substring(10, read0005.length() - 4);
                                String sub = substring.substring(20, substring.length() - 8);
                                data0005 = sub;
                                Log.d("TAG", "runinitGetBuDeng0005File: " + data0005);
                                if (id == 10) {
                                    if (type.equals(hft_card) ) {
                                        initQuanCun(read0005);
                                    }else if(type.equals(hft_traffic_card)){
                                        initQuanCun1(read0005);
                                    }
                                }else if(id == 20){
                                    initPayData();
                                }
                            } else {
                                TcnUtility.getToast(getActivity(), "读取05文件失败，请重新贴卡");
                                initKongJian();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                } else {
                    TcnUtility.getToast(getActivity(), "读取MF失败，请重新贴卡");
                    initKongJian();
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒
    }

    //发GPO指令
    private void initGPO(String xun) {
        String s9F66 = "40000000";//终端交易属性
        String s9F02 = "";
        if (jinE.length() < 3 &&jinE.length()>1) {
            s9F02 = "00000000" + Integer.parseInt(jinE) * 100;//授权金额
        }else if(jinE.length() == 1){
            s9F02 = "000000000" + Integer.parseInt(jinE) * 100;//授权金额
        } else {
            s9F02 = "0000000" + Integer.parseInt(jinE) * 100;//授权金额
        }
        String s9F03 = "000000000000";//其他金额
        String s9F1A = "0156";//终端国家代码
        String s95 = "0000000000";//终端验证结果
        String s5F2A = "0156";//交易货币代码
//        SimpleDateFormat sdfY = new SimpleDateFormat("yyMMdd");
//        String s9A = sdfY.format(new Date());//交易日期
        String s9A = createtime.substring(2, 8);
        String s9C = "63";//交易类型
        String s9F37 = random = Utils.randomHexString(8);//随机数
        String sDF60 = hft_card;//
        String sGPOdata = null;
        if (gpoJoint.equals("9F66049F02069F03069F1A0295055F2A029A039C019F3704DF6001")) {
            sGPOdata = s9F66 + s9F02 + s9F03 + s9F1A + s95 + s5F2A + s9A + s9C + s9F37 + sDF60;
        } else if (gpoJoint.equals("DF60019F66049F02069F03069F1A0295055F2A029A039C019F3704")) {
            sGPOdata = sDF60 + s9F66 + s9F02 + s9F03 + s9F1A + s95 + s5F2A + s9A + s9C + s9F37;
        } else if (gpoJoint.equals("9F6604DF60019F02069F03069F1A0295055F2A029A039C019F3704")) {
            sGPOdata = s9F66 + sDF60 + s9F02 + s9F03 + s9F1A + s95 + s5F2A + s9A + s9C + s9F37;
        }
        if (sGPOdata != null) {
            String string = Utils.integerToHexString(sGPOdata.length() / 2);//40到最后的长度  16进制的
            sGPOdata = "83" + string + sGPOdata;
            String sGPO = Utils.integerToHexString(sGPOdata.length() / 2);//83到最后的长度  16进制的
            sGPOdata = "80A80000" + sGPO + sGPOdata;
        } else {
            TcnUtility.getToast(getActivity(), "GPO指令拼接失败，请重新操作");
            initKongJian();
            return;
        }
        Log.d("TAG", "initGPO: " + sGPOdata);
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(sGPOdata);
        String dataGPO = init(dataXor);
        Log.d("TAG", "dataGPO: " + dataGPO);
        if (dataGPO == null) {
            TcnUtility.getToast(getActivity(), "GPO指令失败，请重新贴卡");
            initKongJian();
            return;
        }
        initFirstGAC();
    }

    //第一次发GAC指令
    private void initFirstGAC() {
        gLogger.debug("金额：" + jinE);
        String s9F02 = "";
        if (jinE.length() < 3 &&jinE.length()>1) {
            s9F02 = "00000000" + Integer.parseInt(jinE) * 100;//授权金额
        }else if(jinE.length() == 1){
            s9F02 = "000000000" + Integer.parseInt(jinE) * 100;//授权金额
        } else {
            s9F02 = "0000000" + Integer.parseInt(jinE) * 100;//授权金额
        }
        String s9F03 = "000000000000";//其他金额
        String s9F1A = "0156";//终端国家代码
        String s95 = "0000000000";//终端验证结果
        String s5F2A = "0156";//交易货币代码
//        SimpleDateFormat sdfY = new SimpleDateFormat("yyMMdd");
//        String s9A = sdfY.format(new Date());//交易日期
        String s9A = createtime.substring(2, 8);
        String s9C = "63";//交易类型
        String s9F37 = random;//随机数
//        SimpleDateFormat sdfH = new SimpleDateFormat("HHmmss");
        //交易时间
//        s9F21 = sdfH.format(new Date());
        s9F21 = createtime.substring(8);
//        String s9F4E = "0000000000000000000000000000000000000000";//商户名称
        String s = "wlyg0001000100000000";//20个转为40个
        String s9F4E = Utils.Chinese2GBK(s);//商户名称 40个=20位  一位=二个
        String sGACdata = s9F02 + s9F03 + s9F1A + s95 + s5F2A + s9A + s9C + s9F37 + s9F21 + s9F4E;
        String sGAC = Utils.integerToHexString(sGACdata.length() / 2);//83到最后的长度  16进制的
        sGACdata = "80AE8000" + sGAC + sGACdata;
        //异或校验  十六进制串
        String dataGAC = Utils.dataXor(sGACdata);
        String dataGac = init(dataGAC);
        if (dataGac == null) {
            TcnUtility.getToast(getActivity(), "FirstGAC指令失败，请重新贴卡");
            initKongJian();
            return;
        }
        String substring = dataGac.substring(10, dataGac.length() - 4);
        String sub = substring.substring(20, substring.length() - 4);
        first_gac_result = sub.toLowerCase();
        Log.d("TAG", "dataFirstGAC: " + dataGac);
        if (id == 10) {//补登
            getXieKaZhiLing();
        } else if (id == 21) {//充值
            rechage_init("");
        }
    }

    //第二次发GAC指令 code:认证通过的授权码
    private void initSecondGAC(String code, String apdu) {
        String s9F02 = "";
        if (jinE.length() < 3 &&jinE.length()>1) {
            s9F02 = "00000000" + Integer.parseInt(jinE) * 100;//授权金额
        }else if(jinE.length() == 1){
            s9F02 = "000000000" + Integer.parseInt(jinE) * 100;//授权金额
        } else {
            s9F02 = "0000000" + Integer.parseInt(jinE) * 100;//授权金额
        }
        String s9F03 = "000000000000";//其他金额
        String s9F1A = "0156";//终端国家代码
        String s95 = "0000000000";//终端验证结果
        String s5F2A = "0156";//交易货币代码
//        SimpleDateFormat sdfY = new SimpleDateFormat("yyMMdd");
//        String s9A = sdfY.format(new Date());//交易日期
        String s9A = createtime.substring(2, 8);
        String s9C = "63";//交易类型
        String s9F37 = random;//随机数
//        String s9F21 = "181030";//交易时间
        String sGACdata = code + s9F02 + s9F03 + s9F1A + s95 + s5F2A + s9A + s9C + s9F37 + s9F21;
        String sGAC = Utils.integerToHexString(sGACdata.length() / 2);//83到最后的长度  16进制的
        sGACdata = "80AE4000" + sGAC + sGACdata;
        //异或校验  十六进制串
        String dataGAC = Utils.dataXor(sGACdata);
        String dataGac = init(dataGAC);
        Log.d("TAG", "dataSecondGAC: " + dataGac);
        if (dataGac == null) {
            TcnUtility.getToast(getActivity(), "SecondGAC指令失败，请重新贴卡");
            initKongJian();
            return;
        }
        sendXieKa(apdu);
    }

    private static Logger gLogger;

    public static void configLog() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("TransportationCardFragment");
    }

    //获取写卡指令
    private void getXieKaZhiLing() {
        String urlZhiLing = Comment.URL + "/hft/apdu";
        Log.d("TAG", "data0015: " + data0015);
        Map<String, String> paramsZhiLing = new HashMap<>();
        paramsZhiLing.put("order_no", String.valueOf(order_no));//终端订单号 用设备定义的终端编号+时间戳
        if (type.equals(hft_card) || type.equals(hft_traffic_card)) {
            paramsZhiLing.put("data0005", data0005);//05文件
            paramsZhiLing.put("data0015", data0015);//15文件
            paramsZhiLing.put("init_result", init_result);//圈存初始化
            paramsZhiLing.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
            paramsZhiLing.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
            String signature = MD5Util.encrypt("data0005=" + data0005 + "&data0015=" + data0015 + "&init_result=" + init_result + "&order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
            paramsZhiLing.put("signature", signature);//签名参数
        } else if (type.equals(zhongyin__nine_card) || type.equals(zhongyin_six_card)) {
            Log.d("TAG", "first_gac_result: " + first_gac_result);
            Log.d("TAG", "random: " + random);
            paramsZhiLing.put("first_gac_result", first_gac_result);//第一次GAC
            paramsZhiLing.put("random", random);//随机数
            paramsZhiLing.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
            paramsZhiLing.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
            String signature = MD5Util.encrypt("first_gac_result=" + first_gac_result + "&order_no=" + order_no + "&random=" + random + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
            paramsZhiLing.put("signature", signature);//签名参数
        }
        Log.d(TAG, "getXieKaZhiLing: " + paramsZhiLing);
        OkHttpUtils.post().url(urlZhiLing).params(paramsZhiLing).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:获取写卡指令失败" + e);
                gLogger.debug("获取写卡指令失败" + e);
                //发送写卡失败结果给后台
                String url = Comment.URL + "/hft/result";
                Map<String, String> params = new HashMap<>();
                params.put("result", "0");//写卡失败
                params.put("order_no", order_no);//终端订单号 暂时用时间戳
//                params.put("tac", tac);//tac
                params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                String signature = MD5Util.encrypt("order_no=" + "00010001-20181212141824" + "&result=" + 0 + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
                params.put("signature", signature);//签名参数
                OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("TAG", "onResponse:失败" + e);
                        gLogger.debug("发送写卡结果失败"  + e);
                        TcnUtility.getToast(getActivity(), "发送写卡结果失败，请您联系工作人员");
                        try {
                            //根据配置信息获取操作数据的db对象
                            DbManager db = x.getDb(daoConfig);
                            TransportCard transport = new TransportCard();
                            transport.setCard_num(cardNum);
                            transport.setBalance(start);
                            transport.setOrder_no(order_no);
                            transport.setData0005(data0005);
                            transport.setData0015(data0015);
                            transport.setInit_result(init_result);
                            transport.setFirst_gac_result(first_gac_result);
                            transport.setRandom(random);
                            transport.setResult(result);
                            transport.setTac(tac);
                            transport.setMoney(jinE);
                            transport.setType(type);
                            transport.setCreatetime(createtime);
                            transport.setApdu(apdu);
                            transport.setArcode(arcode);
                            transport.setArpc(arpc);
                            db.save(transport);//插入一条数据

                            //查询所有数据
                            List<TransportCard> all = db.findAll(TransportCard.class);
                            Log.i("tag", "所有数据:" + all.toString());
                        } catch (DbException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id_) {
                        Log.d("TAG", "onResponse:发送写卡结果成功" + response);
                        gLogger.debug("发送写卡结果成功"  + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //显示领款成功或失败以及金额
                lastShow();
            }

            @Override
            public void onResponse(String response, int id_) {
                Log.d("TAG", "onResponse:获取写卡指令成功" + response);
                gLogger.debug("获取写卡指令成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("false")) {
                        String err_desc = jsonObject.getString("err_desc");
                        if (err_desc.equals("订单不存在，无法获取充值指令")) {
                        }
                        TcnUtility.getToast(getActivity(), err_desc);
                        gLogger.debug("错误提示：" + err_desc);
                        initKongJian();
                        a = 0;
//                        initSignin(2);
                    } else {
                        jsonObject = jsonObject.getJSONObject("order");
                        //写卡指令
                        apdu = jsonObject.getString("apdu");
                        if (apdu.equals("")) {
                            TcnUtility.getToast(getActivity(), "获取写卡指令apdu失败，请重试");
                            initKongJian();
                            return;
                        }
                        if (type.equals(hft_card) || type.equals(hft_traffic_card)) {
                            sendXieKa(apdu);
                        } else {
                            //进行外部认证
                            //认证通过的授权码
                            arcode = jsonObject.getString("arcode");
                            //外部认证认证:ARPC
                            arpc = jsonObject.getString("arpc");
                            if (arcode.equals("") && arpc.equals("")) {
                                TcnUtility.getToast(getActivity(), "获取授权码或认证失败，请重试");
                                initKongJian();
                                return;
                            }
                            String sARPCdata = arpc + arcode;
                            String sARPC = Utils.integerToHexString(sARPCdata.length() / 2);
                            String ARPC = "00820000" + sARPC + sARPCdata;
                            //异或校验  十六进制串
                            String dataARPC = Utils.dataXor(ARPC);
                            String dataArpc = init(dataARPC);
                            Log.d("TAG", "dataArpc: " + dataArpc);
                            if (dataArpc == null) {
                                TcnUtility.getToast(getActivity(), "ARPC指令失败，请重新贴卡");
                                initKongJian();
                                return;
                            }
                            initSecondGAC(arcode, apdu);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //发送写卡指令
    private void sendXieKa(String adpu) {
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(adpu);
        final String xieKa = init(dataXor);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (xieKa != null && !xieKa.equals("")) {
                    String substring = xieKa.substring(xieKa.length() - 12, xieKa.length() - 8);
                    tac = xieKa.substring(xieKa.length() - 20, xieKa.length() - 12);
                    if (substring.equals("9000")) {
                        result = "1";
                    } else {
                        result = "0";
                    }
                    String url = Comment.URL + "/hft/result";
                    Map<String, String> params = new HashMap<>();
                    params.put("result", result);//写卡成功或失败
                    params.put("tac", tac);//tac
                    params.put("order_no", String.valueOf(order_no));//终端订单号 用设备定义的终端编号+时间戳
                    params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                    params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                    String signature = MD5Util.encrypt("order_no=" + order_no + "&result=" + result + "&tac=" + tac + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
                    params.put("signature", signature);//签名参数
                    OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("TAG", "onResponse:发送写卡结果失败" + e);
                            gLogger.debug("发送写卡结果失败");
                            try {
                                //根据配置信息获取操作数据的db对象
                                DbManager db = x.getDb(daoConfig);
                                TransportCard transport = new TransportCard();
                                transport.setCard_num(cardNum);
                                transport.setBalance(start);
                                transport.setOrder_no(order_no);
                                transport.setData0005(data0005);
                                transport.setData0015(data0015);
                                transport.setInit_result(init_result);
                                transport.setFirst_gac_result(first_gac_result);
                                transport.setRandom(random);
                                transport.setResult(result);
                                transport.setTac(tac);
                                transport.setMoney(jinE);
                                transport.setType(type);
                                transport.setCreatetime(createtime);
                                transport.setApdu(apdu);
                                transport.setArcode(arcode);
                                transport.setArpc(arpc);
                                db.save(transport);//插入一条数据

                                //查询所有数据
                                List<TransportCard> all = db.findAll(TransportCard.class);
                                Log.i("tag", "所有数据:" + all.toString());
                            } catch (DbException e1) {
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id_) {
                            Log.d("TAG", "onResponse:发送写卡结果成功" + response);
                            gLogger.debug("发送写卡结果成功" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    lastShow();
                } else {
                    TcnUtility.getToast(getActivity(), "写卡失败，请重新贴卡");
                    initKongJian();
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒
    }

    //显示领款成功或失败以及金额
    private void lastShow() {
        String aid = "00A4040009A00000000386980701";//选aid
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(aid);
        final String getAid = init(dataXor);
        if (getAid == null) {
            TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
            initKongJian();
            return;
        }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String substring = getAid.substring(getAid.length() - 12, getAid.length() - 8);
                if (getAid != null && !getAid.equals("") && substring.equals("9000")) {
                    String aid = "805c000204";//读余额
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String readYuE = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (readYuE != null && !readYuE.equals("")) {
                                String substring = readYuE.substring(readYuE.length() - 20, readYuE.length() - 12);
                                Log.d("TAG", "run: " + substring);
                                final int start = Integer.parseInt(substring, 16);
                                Log.d("TAG", "run: " + start);
                                gLogger.debug("读余额返回余额值：" + start);
                                String aid = "00B0950000";//15文件
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String read0015 = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (read0015 != null && !read0015.equals("")) {
                                            String substring = read0015.substring(10, read0015.length() - 4);
                                            String sub = substring.substring(20, substring.length() - 8);
                                            String cardNum = sub.substring(32, 40);//卡号
                                            Log.d("TAG", "aaaav: " + substring);
                                            Double d = (double) start / 100;
                                            DecimalFormat df = new DecimalFormat("#0.00");
                                            String YuE1 = df.format(d);
                                            Log.d("TAG", "runD: " + YuE1);
                                            tv_card.setText(cardNum);
                                            btn_return.setText("确认");
                                            btn_return.setVisibility(View.VISIBLE);
                                            if (id == 10) {
                                                ll_duka.setVisibility(View.GONE);
                                                ll_yuEShow.setVisibility(View.VISIBLE);
                                                ll_jinE.setVisibility(View.VISIBLE);
                                                ll_jinE1.setVisibility(View.VISIBLE);
                                                tv_success.setVisibility(View.VISIBLE);
                                                tv3.setText("");
                                                tv3.setBackgroundResource(R.drawable.wancheng);
                                                tv_jin.setText("补登余额：");
                                                tv_jin1.setText("补登后余额：");
                                                tv_jinE1.setText(YuE1 + "元");
                                                if (result.equals("1")) {
                                                    tv_success.setText("补登成功！");
                                                    play(1);
                                                } else {
                                                    tv_success.setText("补登失败！");
                                                    play(2);
                                                }
                                                id = 14;
                                            } else if (id == 21) {
                                                ll_duka.setVisibility(View.GONE);
                                                ll_yuEShow.setVisibility(View.VISIBLE);
                                                tv_success.setVisibility(View.VISIBLE);
                                                ll_chongzhi.setVisibility(View.GONE);
                                                ll_jinE.setVisibility(View.VISIBLE);
                                                ll_jinE1.setVisibility(View.VISIBLE);
                                                t3.setText("");
                                                t3.setBackgroundResource(R.drawable.wancheng);
                                                tv_money.setText(YuE + "元");
                                                tv_jin.setText("充值金额：");
                                                tv_jinE.setText(jinE + ".00元");
                                                tv_jin1.setText("充值后金额：");
                                                tv_jinE1.setText(YuE1 + "元");
                                                if (result.equals("1")) {
                                                    tv_success.setText("充值成功！");
                                                    play(3);
                                                } else {
                                                    tv_success.setText("充值失败！");
                                                    play(4);
                                                }
                                                id = 25;
                                            }
                                            //发送移卡指令、关闭串口
                                            String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                            final String dataYiKa = init(yiKa);
                                            Log.d("TAG", "onViewClicked: " + dataYiKa);
                                            zhongjiSerial.closeSerialPort();
                                        } else {
                                            TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                            initKongJian();
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            } else {
                                TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                initKongJian();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                } else {
                    String aid = "00A4040008A000000333010106";//选中银通92bin aid
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String data = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            String substring = data.substring(data.length() - 12, data.length() - 8);
                            if (data != null && !data.equals("") && substring.equals("9000")) {
                                String aid = "80CA9F7900";//读余额
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String data = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (data != null && !data.equals("")) {
                                            String s = data.substring(10, data.length() - 12);
                                            String[] split = s.split("9F79");
                                            String substring = split[1].substring(2);
//                                            String substring = data.substring(data.length() - 16, data.length() - 12);
                                            Log.d("TAG", "run: " + substring);
                                            final int start = Integer.parseInt(substring);
                                            Log.d("TAG", "run: " + start);
                                            String aid = "00B2011400";//读卡号
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (data != null && !data.equals("")) {
                                                        String substring = data.substring(10, data.length() - 4);
                                                        String sub = substring.substring(20, substring.length() - 8);
                                                        String[] strings = sub.split("5A");
                                                        String s1 = strings[1].substring(0, 2);
                                                        int s = Integer.parseInt(s1, 16);
                                                        String cardNum = strings[1].substring(2, (s * 2 + 1));
//                                                        String cardNum = sub.substring(8, 27);//卡号
                                                        Log.d("TAG", "aaaavv: " + substring);
                                                        Double d = (double) start / 100;
                                                        DecimalFormat df = new DecimalFormat("#0.00");
                                                        String YuE1 = df.format(d);
                                                        Log.d("TAG", "runD: " + YuE1);
                                                        tv_card.setText(cardNum);
                                                        btn_return.setText("确认");
                                                        btn_return.setVisibility(View.VISIBLE);
                                                        if (id == 10) {
                                                            ll_duka.setVisibility(View.GONE);
                                                            ll_yuEShow.setVisibility(View.VISIBLE);
                                                            ll_jinE.setVisibility(View.VISIBLE);
                                                            ll_jinE1.setVisibility(View.VISIBLE);
                                                            tv_success.setVisibility(View.VISIBLE);
                                                            tv3.setText("");
                                                            tv3.setBackgroundResource(R.drawable.wancheng);
                                                            tv_jin.setText("补登余额：");
                                                            tv_jin1.setText("补登后余额：");
                                                            tv_jinE1.setText(YuE1 + "元");
                                                            if (result.equals("1")) {
                                                                tv_success.setText("补登成功！");
                                                                play(1);
                                                            } else {
                                                                tv_success.setText("补登失败！");
                                                                play(2);
                                                            }
                                                            id = 14;
                                                        } else if (id == 21) {
                                                            ll_duka.setVisibility(View.GONE);
                                                            ll_yuEShow.setVisibility(View.VISIBLE);
                                                            tv_success.setVisibility(View.VISIBLE);
                                                            ll_chongzhi.setVisibility(View.GONE);
                                                            ll_jinE.setVisibility(View.VISIBLE);
                                                            ll_jinE1.setVisibility(View.VISIBLE);
                                                            t3.setText("");
                                                            t3.setBackgroundResource(R.drawable.wancheng);
                                                            tv_money.setText(YuE + "元");
                                                            tv_jin.setText("充值金额：");
                                                            tv_jinE.setText(jinE + ".00元");
                                                            tv_jin1.setText("充值后金额：");
                                                            tv_jinE1.setText(YuE1 + "元");
                                                            if (result.equals("1")) {
                                                                tv_success.setText("充值成功！");
                                                                play(3);
                                                            } else {
                                                                tv_success.setText("充值失败！");
                                                                play(4);
                                                            }
                                                            id = 25;
                                                        }
                                                        //发送移卡指令、关闭串口
                                                        String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                        final String dataYiKa = init(yiKa);
                                                        Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                        zhongjiSerial.closeSerialPort();

                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        } else {
                                            TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                            initKongJian();
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            } else {
                                String aid = "00A4040008A000000333010101";//选中银通62bin aid
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String data = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        String substring = data.substring(data.length() - 12, data.length() - 8);
                                        if (data != null && !data.equals("") && substring.equals("9000")) {
                                            String aid = "80CA9F7900";//读余额
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (data != null && !data.equals("")) {
                                                        String s = data.substring(10, data.length() - 12);
                                                        String[] split = s.split("9F79");
                                                        String substring = split[1].substring(2);
//                                                      String substring = data.substring(data.length() - 16, data.length() - 12);
                                                        Log.d("TAG", "run: " + substring);
                                                        final int start = Integer.parseInt(substring);
                                                        Log.d("TAG", "run: " + start);
                                                        String aid = "00B2011400";//读卡号
                                                        //异或校验  十六进制串
                                                        String dataXor = Utils.dataXor(aid);
                                                        final String data = init(dataXor);
                                                        final Handler handler = new Handler();
                                                        final Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (data != null && !data.equals("")) {
                                                                    String substring = data.substring(10, data.length() - 4);
                                                                    String sub = substring.substring(20, substring.length() - 8);
                                                                    String[] strings = sub.split("5A");
                                                                    String s1 = strings[1].substring(0, 2);
                                                                    int s = Integer.parseInt(s1, 16);
                                                                    String cardNum = strings[1].substring(2, (s * 2 + 1));
//                                                                  String cardNum = sub.substring(8, 27);//卡号
                                                                    Log.d("TAG", "aaaavv: " + substring);
                                                                    Double d = (double) start / 100;
                                                                    DecimalFormat df = new DecimalFormat("#0.00");
                                                                    String YuE1 = df.format(d);
                                                                    Log.d("TAG", "runD: " + YuE1);
                                                                    tv_card.setText(cardNum);
                                                                    btn_return.setText("确认");
                                                                    btn_return.setVisibility(View.VISIBLE);
                                                                    if (id == 10) {
                                                                        ll_duka.setVisibility(View.GONE);
                                                                        ll_yuEShow.setVisibility(View.VISIBLE);
                                                                        ll_jinE.setVisibility(View.VISIBLE);
                                                                        ll_jinE1.setVisibility(View.VISIBLE);
                                                                        tv_success.setVisibility(View.VISIBLE);
                                                                        tv3.setText("");
                                                                        tv3.setBackgroundResource(R.drawable.wancheng);
                                                                        tv_jin.setText("补登余额：");
                                                                        tv_jin1.setText("补登后余额：");
                                                                        tv_jinE1.setText(YuE1 + "元");
                                                                        if (result.equals("1")) {
                                                                            tv_success.setText("补登成功！");
                                                                            play(1);
                                                                        } else {
                                                                            tv_success.setText("补登失败！");
                                                                            play(2);
                                                                        }
                                                                        id = 14;
                                                                    } else if (id == 21) {
                                                                        ll_duka.setVisibility(View.GONE);
                                                                        ll_yuEShow.setVisibility(View.VISIBLE);
                                                                        tv_success.setVisibility(View.VISIBLE);
                                                                        ll_chongzhi.setVisibility(View.GONE);
                                                                        ll_jinE.setVisibility(View.VISIBLE);
                                                                        ll_jinE1.setVisibility(View.VISIBLE);
                                                                        t3.setText("");
                                                                        t3.setBackgroundResource(R.drawable.wancheng);
                                                                        tv_money.setText(YuE + "元");
                                                                        tv_jin.setText("充值金额：");
                                                                        tv_jinE.setText(jinE + ".00元");
                                                                        tv_jin1.setText("充值后金额：");
                                                                        tv_jinE1.setText(YuE1 + "元");
                                                                        if (result.equals("1")) {
                                                                            tv_success.setText("充值成功！");
                                                                            play(3);
                                                                        } else {
                                                                            tv_success.setText("充值失败！");
                                                                            play(4);
                                                                        }
                                                                        id = 25;
                                                                    }
                                                                    //发送移卡指令、关闭串口
                                                                    String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                                    final String dataYiKa = init(yiKa);
                                                                    Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                                    zhongjiSerial.closeSerialPort();

                                                                } else {
                                                                    TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                                    initKongJian();
                                                                }
                                                            }
                                                        };
                                                        handler.postDelayed(runnable, 1);//线程时间3秒
                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        } else {
                                            String aid = "00A4040008A000000632010105";//选交通卡aid
                                            //异或校验  十六进制串
                                            String dataXor = Utils.dataXor(aid);
                                            final String data = init(dataXor);
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    String substring = data.substring(data.length() - 12, data.length() - 8);
                                                    if (data != null && !data.equals("") && substring.equals("9000")) {
                                                        String aid = "805c000204";//读余额
                                                        //异或校验  十六进制串
                                                        String dataXor = Utils.dataXor(aid);
                                                        final String readYuE = init(dataXor);
                                                        final Handler handler = new Handler();
                                                        final Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (readYuE != null && !readYuE.equals("")) {
                                                                    String substring = readYuE.substring(readYuE.length() - 20, readYuE.length() - 12);
                                                                    Log.d("TAG", "run: " + substring);
                                                                    final int start = Integer.parseInt(substring, 16);
                                                                    Log.d("TAG", "run: " + start);
                                                                    gLogger.debug("读余额返回余额值：" + start);
                                                                    String aid = "00B0950000";//15文件
                                                                    //异或校验  十六进制串
                                                                    String dataXor = Utils.dataXor(aid);
                                                                    final String read0015 = init(dataXor);
                                                                    final Handler handler = new Handler();
                                                                    final Runnable runnable = new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if (read0015 != null && !read0015.equals("")) {
                                                                                String substring = read0015.substring(10, read0015.length() - 4);
                                                                                String sub = substring.substring(20, substring.length() - 8);
                                                                                String cardNum = sub.substring(21, 40);//卡号
                                                                                Log.d("TAG", "aaaav: " + substring);
                                                                                Double d = (double) start / 100;
                                                                                DecimalFormat df = new DecimalFormat("#0.00");
                                                                                String YuE1 = df.format(d);
                                                                                Log.d("TAG", "runD: " + YuE1);
                                                                                tv_card.setText(cardNum);
                                                                                btn_return.setText("确认");
                                                                                btn_return.setVisibility(View.VISIBLE);
                                                                                if (id == 10) {
                                                                                    ll_duka.setVisibility(View.GONE);
                                                                                    ll_yuEShow.setVisibility(View.VISIBLE);
                                                                                    ll_jinE.setVisibility(View.VISIBLE);
                                                                                    ll_jinE1.setVisibility(View.VISIBLE);
                                                                                    tv_success.setVisibility(View.VISIBLE);
                                                                                    tv3.setText("");
                                                                                    tv3.setBackgroundResource(R.drawable.wancheng);
                                                                                    tv_jin.setText("补登余额：");
                                                                                    tv_jin1.setText("补登后余额：");
                                                                                    tv_jinE1.setText(YuE1 + "元");
                                                                                    if (result.equals("1")) {
                                                                                        tv_success.setText("补登成功！");
                                                                                        play(1);
                                                                                    } else {
                                                                                        tv_success.setText("补登失败！");
                                                                                        play(2);
                                                                                    }
                                                                                    id = 14;
                                                                                } else if (id == 21) {
                                                                                    ll_duka.setVisibility(View.GONE);
                                                                                    ll_yuEShow.setVisibility(View.VISIBLE);
                                                                                    tv_success.setVisibility(View.VISIBLE);
                                                                                    ll_chongzhi.setVisibility(View.GONE);
                                                                                    ll_jinE.setVisibility(View.VISIBLE);
                                                                                    ll_jinE1.setVisibility(View.VISIBLE);
                                                                                    t3.setText("");
                                                                                    t3.setBackgroundResource(R.drawable.wancheng);
                                                                                    tv_money.setText(YuE + "元");
                                                                                    tv_jin.setText("充值金额：");
                                                                                    tv_jinE.setText(jinE + ".00元");
                                                                                    tv_jin1.setText("充值后金额：");
                                                                                    tv_jinE1.setText(YuE1 + "元");
                                                                                    if (result.equals("1")) {
                                                                                        tv_success.setText("充值成功！");
                                                                                        play(3);
                                                                                    } else {
                                                                                        tv_success.setText("充值失败！");
                                                                                        play(4);
                                                                                    }
                                                                                    id = 25;
                                                                                }
                                                                                //发送移卡指令、关闭串口
                                                                                String yiKa = "0200000010800000000600003C0000BECC01020000DB03";
                                                                                final String dataYiKa = init(yiKa);
                                                                                Log.d("TAG", "onViewClicked: " + dataYiKa);
                                                                                zhongjiSerial.closeSerialPort();
                                                                            } else {
                                                                                TcnUtility.getToast(getActivity(), "读取卡号失败，请重新贴卡");
                                                                                initKongJian();
                                                                            }
                                                                        }
                                                                    };
                                                                    handler.postDelayed(runnable, 1);//线程时间3秒
                                                                } else {
                                                                    TcnUtility.getToast(getActivity(), "读取余额失败，请重新贴卡");
                                                                    initKongJian();
                                                                }
                                                            }
                                                        };
                                                        handler.postDelayed(runnable, 1);//线程时间3秒
                                                    } else {
                                                        TcnUtility.getToast(getActivity(), "读卡失败，请重新贴卡");
                                                        initKongJian();
                                                    }
                                                }
                                            };
                                            handler.postDelayed(runnable, 1);//线程时间3秒
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒
    }

    //圈存初始化（合肥通）
    private void initQuanCun(String read0005) {
        String aid = "00A40000023F01";//选MF
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(aid);
        final String getMF = init(dataXor);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                Log.d("TAG", "onViewClicked1: " + getMF);
                if (getMF != null && !getMF.equals("")) {
                    String aid = "00200000021234";//验证脱机pin
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String pin = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
//                            Log.d("TAG", "onViewClicked1: " + pin);
                            if (pin != null && !pin.equals("")) {
                                gLogger.debug("金额：" + jinE);
                                String hexString = Utils.integerToHexStringQuanCun(Integer.parseInt(jinE) * 100);
                                String aid = "805000020B010000" + hexString + "800100090000";//圈存初始化 800100090000:测试环境的  800100050000：正式环境的
                                //异或校验  十六进制串
                                String dataXor = Utils.dataXor(aid);
                                final String quanCun = init(dataXor);
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
//                                        Log.d("TAG", "onViewClicked1: " + quanCun);
                                        if (quanCun != null && !quanCun.equals("")) {
                                            String substring = quanCun.substring(10, quanCun.length() - 4);
                                            String sub = substring.substring(20, substring.length() - 8);
                                            init_result = sub;
                                            Log.d("TAG", "runinitGetBuDengLingKuanZhiLing: " + init_result);
                                            Log.d("TAG", "id: " + id);
                                            if (id == 10) {
                                                getXieKaZhiLing();
                                            } else if (id == 21) {
                                                getData(channel_code, iv_tu);
                                            }
                                        } else {
                                            TcnUtility.getToast(getActivity(), "圈存失败，请重新贴卡");
                                            initKongJian();
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, 1);//线程时间3秒
                            } else {
                                TcnUtility.getToast(getActivity(), "验证脱机pin失败，请重新贴卡");
                                initKongJian();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                } else {
                    TcnUtility.getToast(getActivity(), "读取MF失败，请重新贴卡");
                    initKongJian();
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒
    }

    //圈存初始化（交通卡）
    private void initQuanCun1(String read0005) {
        String aid = "00A4040008A000000632010105";//选交通卡aid
        //异或校验  十六进制串
        String dataXor = Utils.dataXor(aid);
        final String pin = init(dataXor);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                            Log.d("TAG", "onViewClicked1: " + pin);
                if (pin != null && !pin.equals("")) {
                    gLogger.debug("金额：" + jinE);
                    String hexString = Utils.integerToHexStringQuanCun(Integer.parseInt(jinE) * 100);
                    String aid = "805000020B010000" + hexString + "800100090000";//圈存初始化  800100090000:测试环境的  800100050000：正式环境的
                    //异或校验  十六进制串
                    String dataXor = Utils.dataXor(aid);
                    final String quanCun = init(dataXor);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
//                                        Log.d("TAG", "onViewClicked1: " + quanCun);
                            if (quanCun != null && !quanCun.equals("")) {
                                String substring = quanCun.substring(10, quanCun.length() - 4);

                                String sub = substring.substring(20, substring.length() - 8);
                                init_result = sub;
                                String mac1 = substring.substring(substring.length() - 16, substring.length() - 4);

                                Log.d("TAG", "runinitGetBuDengLingKuanZhiLing: " + init_result);
                                Log.d("TAG", "runinitGetBuDengLingKuanZhiLing: " + mac1);
                                Log.d("TAG", "id: " + id);
                                if (id == 10) {
                                    getXieKaZhiLing();
                                } else if (id == 21) {
                                    getData(channel_code, iv_tu);
                                }
                            } else {
                                TcnUtility.getToast(getActivity(), "圈存失败，请重新贴卡");
                                initKongJian();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1);//线程时间3秒
                } else {
                    TcnUtility.getToast(getActivity(), "选卡失败，请重新贴卡");
                    initKongJian();
                }
            }
        };
        handler.postDelayed(runnable, 1);//线程时间3秒

    }

    //充值选择的初始化
    private void initSelect() {
        //充值选择按钮背景的初始化
        tv_10.setBackgroundResource(R.drawable.shape_ffffff);
        tv_20.setBackgroundResource(R.drawable.shape_ffffff);
        tv_50.setBackgroundResource(R.drawable.shape_ffffff);
        tv_100.setBackgroundResource(R.drawable.shape_ffffff);
        tv_zi.setBackgroundResource(R.drawable.shape_ffffff);
        //充值选择按钮文字的初始化
        tv_10.setTextColor(getResources().getColor(R.color.black));
        tv_20.setTextColor(getResources().getColor(R.color.black));
        tv_50.setTextColor(getResources().getColor(R.color.black));
        tv_100.setTextColor(getResources().getColor(R.color.black));
        tv_zi.setTextColor(getResources().getColor(R.color.black));
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
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        // 屏幕高度（像素）
        int height = dm.heightPixels;
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)
//        Log.e("12", width + "======" + height);
//        Log.e("123", screenWidth + "======" + screenHeight);
        return height;
    }

    //硬件返回数据
    protected void onDataReceived(final byte[] buffer, final int size) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                String recinfo = Utils.byteArrayToHexString(buffer);
                Log.i("test", "接收到串口信息======" + recinfo);
            }
        });
    }

    /**
     * 初始化获取数据库的配置信息
     */
    public void initDaoConfig() {
        Log.d(TAG, "initDaoConfig: " + getContext().getCacheDir().getAbsoluteFile());
        daoConfig = new DbManager.DaoConfig()
                .setDbName("transportCard.db")  //设置数据库名称
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
    //    String param = "terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    int a;
    int flag1 = -1;
    int flag2 = -1;

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
