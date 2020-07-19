package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.bean.AdvertisementBean;
import com.example.myapplication.bean.SheBeiBean;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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

import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;

public class InitActivity extends AppCompatActivity {
    private static final String TAG = "InitActivity";
    private RelativeLayout rl_singleImg, rl_upImg, rl_downImg, rl_null;
    private ImageView iv_logo, iv_no1, iv_no2, iv_no3, iv_no4, iv_no5;
    private TextView tv_machineId, tv_phone, tv_tel;
    private VideoView vv1;
    private String key;
    private Banner mBanner;
    private List<String> images;
    private List<String> titles;
    private List<AdvertisementBean> list, list1;
    private SharedPreferences spf;
    private int moshi;
    private SheBeiBean bean;
    private long timeStamp;

    private Runnable runnable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private boolean isFirst;
    private SharedPreferences.Editor editor;
    private Bitmap bitmap;
    private String terminal_no;//终端编号（设备定义的）
    private String str;
    private Dialog dialog;
    private Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        moshi = getIntent().getIntExtra("styleDaiJi", 0);
        if (moshi == 0) {
            moshi = 1;
        }
        Log.d(TAG, "onCreateMoshi: " + moshi);
        if (moshi == 1) {
            setContentView(R.layout.activity_init);
        } else if (moshi == 3) {
            setContentView(R.layout.activity_init_up);
        } else if (moshi == 2) {
            setContentView(R.layout.activity_init_down);
        } else if (moshi == 4) {
            setContentView(R.layout.activity_init_null);
        }
        spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        editor = spf.edit();
        configLog();
        //默认为第一次
        boolean isUpdate = spf.getBoolean("isUpdate",false);
        if (!isUpdate) {
            editor.putBoolean("isUpdate", true);
            editor.commit();
            startActivity(new Intent(InitActivity.this, GuideActivity.class));
            finish();
            return;
        }
//        else{
//            editor.putBoolean("isUpdate", false);
//            editor.commit();
//        }
        Comment.terminal_code = spf.getString("ANDROID_ID", "");
        Log.d(TAG, "onCreateMoshi: " + Comment.terminal_code);
        init();
        initData(-1);
        Log.d(TAG, "onClick: sssssssssssssssssssssssssssssssssssssssss");
        rl_singleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //有效期
                if (time()) {
                    return;
                }
                int style = spf.getInt("style", 0);
                Intent intent = new Intent(InitActivity.this, MainActivity.class);
                Log.d(TAG, "onClick: " + style);
                intent.putExtra("styleShopping", style);
                intent.putExtra("shebei", bean);
                startActivity(intent);
                finish();
                handler.removeCallbacks(runnable);
            }
        });

        if (moshi != 1 && moshi > 0) {
            video();
        }

        isFirst = spf.getBoolean("isFirst", false);//false:连点5次进管理页面 true: 点击弹出二维码
        Log.d(TAG, "onCreate: " + isFirst);
        if (!isFirst) {
            editor.putBoolean("isFirst", true);
            editor.commit();
            //logo连点5次进设备管理(第一次进可以点击)
            iv_logo.setEnabled(true);
            iv_logo.setOnClickListener(new View.OnClickListener() {
                long[] mHits = new long[5];
                @Override
                public void onClick(View v) {
                    //有效期
                    if (time()) {
                        return;
                    }
                    //src 拷贝的源数组
                    //srcPos 从源数组的那个位置开始拷贝.
                    //dst 目标数组
                    //dstPos 从目标数组的那个位子开始写数据
                    //length 拷贝的元素的个数
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 2500)) {
                        Log.d("TAG", "恭喜你，连点了5次.");
                        Intent intent = new Intent(InitActivity.this, EquipmentAndManageActivity.class);
                        intent.putExtra("shebei", bean);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        } else {
            iv_logo.setEnabled(true);
//            sendDoor();
            iv_logo.setOnClickListener(new View.OnClickListener() {
                long[] mHits = new long[3];
                @Override
                public void onClick(View v) {
                    //有效期
                    if (time()) {
                        return;
                    }
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 1500)) {
                        Log.d("TAG", "恭喜你，连点了3次.");
                        // 弹出二维码
                        ecode();
                    }
                }
            });
        }

    }

    //持续发送查询门控的指令
    private void sendDoor() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 30);
                String serialPort2 = spf.getString("serialport2", "");
                if (serialPort2 != null && !serialPort2.equals("")) {
                    String substring2 = serialPort2.substring(serialPort2.length() - 2);
                    Log.d("TAG", "onCreateView: " + substring2);
                    zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring2,InitActivity.this);
                    String door = "00FFDF2055AA";
                    String chekDoor = zhongjiAisleSerial.checkDoor(door);
                    Log.d(TAG, "onSendDoor: " + chekDoor);
//                    TcnUtility.getToast(InitActivity.this, "门控返回的数据是：" + chekDoor);
                    if (chekDoor != null && !chekDoor.equals("")) {
                        String s = chekDoor.substring(4, 6);
                        String s1 = chekDoor.substring(8);
                        if (s.equals("01") && s1.equals("5E")) {//开门
                            handler.removeCallbacks(runnable);
                            // 弹出二维码
                            ecode();
                        }
                    } else {
                        TcnUtility.getToast(InitActivity.this, "货道串口设置错误，请从新设置货道串口");
                    }
                    zhongjiAisleSerial.closeSerialPort();
                } else {
                    TcnUtility.getToast(InitActivity.this, "请先设置货道串口");
                }
            }
        };
        handler.postDelayed(runnable, 30000);//线程时间30秒刷新
    }

    //弹出二维码
    private void ecode() {
        //fragment 里app下的AlertDialog要加样式
        AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        str = formatter.format(curDate);
        str = String.valueOf(getTimeStamp());
        String shengcheng = "http://admin.qiyuwulian.top/user/terminalauth/" + terminal_no + "/" + str;
        Log.d(TAG, "runAAA: " + shengcheng);

        builder.setCancelable(true);//.setPositiveButton("确认", null)
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_login);
        final EditText et_username = (EditText)dialog.findViewById(R.id.et_username);
        final EditText et_password = (EditText) dialog.findViewById(R.id.et_password);
        Button btn_login = (Button) dialog.findViewById(R.id.btn_login);
        ImageView iv_qrcode = dialog.findViewById(R.id.iv_qrcode);
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        Log.d(TAG, "ecode: "+width+"++++"+height);
        Bitmap qrCode = EncodingUtils.createQRCode(shengcheng, (int) (width * 0.65), (int) (width * 0.65), bitmap);
        iv_qrcode.setImageBitmap(qrCode);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (username.equals("")&&password.equals("")) {
                    //发送获取的结果
                    send();
                } else {
                    Log.d(TAG, "aaaaaaaaaaaaaaa: "+ et_username.getText().toString()+"++++++"+et_password.getText().toString());
                    login(username,password);
                }
            }
        });
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                btnPositive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.d(TAG, "onClick: +++++++++++++++++++++++++++++");
//                        //发送获取的结果
//                        send();
////                        String is_success = "1";
////                        if(is_success.equals("1")){
////                            TcnUtility.getToast(InitActivity.this,"获取成功");
////                            dialog.dismiss();
////                        }else if(is_success.equals("-1")){
////                            TcnUtility.getToast(InitActivity.this,"请扫码");
////                        }else{
////                            TcnUtility.getToast(InitActivity.this,"获取失败");
////                        }
//                    }
//                });
//            }
//        });
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        if (width < height) {
            params.width = (int) (width * 0.65);
            params.height = (int) (height * 0.34);
        } else {
            params.width = (int) (width * 0.34);
            params.height = (int) (height * 0.5);
        }
        dialog.getWindow().setAttributes(params);
    }
    //账号登录
    private void login(String username, String password){
        String url = Comment.URL + "/terminal/usernameauth";
        Map<String, String> params = new HashMap<>();
        params.put("username", username );//账号
        String encrypt = MD5Util.encrypt(password);
        params.put("password", encrypt);//密码
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("password=" + encrypt + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp+ "&username=" + username + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "run: " + params);
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                TcnUtility.getToast(InitActivity.this, "获取失败");
//                sendDoor();
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功S" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        String err_desc = jsonObject.getString("err_desc");
                        TcnUtility.getToast(InitActivity.this,err_desc);
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    TcnUtility.getToast(InitActivity.this, "登录成功");
                    Intent intent = new Intent(InitActivity.this, EquipmentAndManageActivity.class);
                    intent.putExtra("shebei", bean);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //发送获取的结果
    private void send() {
        Log.d(TAG, "send: " + key);
        String url = Comment.URL + "/terminal/userauth";
        Map<String, String> params = new HashMap<>();
        params.put("auth_no", str);//请求里上送的序列号
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("auth_no=" + str + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "run: " + params);
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                TcnUtility.getToast(InitActivity.this, "获取失败");
//                sendDoor();
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功S" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        String err_desc = jsonObject.getString("err_desc");
//                        i = 0;
//                        initData(0);
                        if(err_desc.equals("无验证记录")){
                            TcnUtility.getToast(InitActivity.this,"请扫码");
                            return;
                        }
                        TcnUtility.getToast(InitActivity.this,err_desc);
                        dialog.dismiss();
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    TcnUtility.getToast(InitActivity.this, "获取成功");
                    Intent intent = new Intent(InitActivity.this, EquipmentAndManageActivity.class);
                    intent.putExtra("shebei", bean);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //心跳包
    private void sendHeart() {
        //几秒后发送获取订单状态
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                //定时向后台获取订单状态
//                handler.postDelayed(this, 1000 * 60 * 5);
//                int versionCode = Heart.getVersionCode(InitActivity.this);//当前版本号
//                //获取信号强度
//                Heart.resume();
//                String netWorkInfo = Heart.getNetWorkInfo();
//                if(netWorkInfo != null&&!netWorkInfo.equals("0")){
//                    Log.d(TAG, "onResume: "+netWorkInfo);
//                }
//                //获取货道状态
//                String serialPort2 = spf.getString("serialport2", "");
//                if (serialPort2 != null && !serialPort2.equals("")) {
//                    int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
//                    if (model_id == 1) {
//
//                    } else if (model_id == 2) {
//                        String substring = serialPort2.substring(serialPort2.length() - 2);
//                        zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring);
//                        String getId = "0101000000000000000000000000000000007188";
//                        String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
//                        if (chekChuHuo == null) {
//                            TcnUtility.getToast(InitActivity.this, "货道串口设置错误，请从新设置货道串口");
//                        } else {
//                            TcnUtility.getToast(InitActivity.this, "返回的数据是：" + chekChuHuo);
//                        }
//                        zhongjiAisleSerial.closeSerialPort();
//                    }
//                }else {
//                    TcnUtility.getToast(InitActivity.this, "请先设置货道串口");
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000 * 60 * 5);//线程时间5分钟刷新
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

    //视频播放
    private void video() {
        //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
        String videoUrl1 = Environment.getExternalStorageDirectory().getPath() + "/fl1234.mp4";
        //网络视频
//        List<SettingBean> list = (List<SettingBean>) getIntent().getSerializableExtra("setVideoList");
//        String imgUrl = null;
//        if (list != null) {
//            imgUrl = list.get(0).getImgUrl();
//        } else {
//            imgUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        }
        String url = Comment.URL + "/terminal/advertisement";//  advertisement/list  terminal/advertisement 两个都是广告的接口
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//        params.put("direction", String.valueOf(v));
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponseA:失败" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseV:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String ad_name = object.getString("ad_name");//轮播图名称
                        String ad_type = object.getString("ad_type");//类型
                        int time_start = object.getInt("time_start");//广告生效日期 精确到日
                        int time_end = object.getInt("time_end");//广告结束日期 精确到日
                        boolean long_term = object.getBoolean("long_term");//表示是否受time控制
                        boolean frozen = object.getBoolean("frozen");//表示当前是否冻结，如果冻结就不播放
                        if (ad_type.equals("video")) {
                            String imgUrl = Comment.URL_IMG_OR_VIDEO + "/video/" + object.getString("filename");//视频 "http://file.qiyuwulian.top/video/"
                            Log.d(TAG, "video: " + imgUrl);
                            final Uri uri = Uri.parse(imgUrl);
                            //设置视频控制器
                            vv1.setMediaController(new MediaController(InitActivity.this));
                            //设置视频路径
                            vv1.setVideoURI(uri);
                            //开始播放视频
                            vv1.start();
                            //播放准备
                            vv1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    mp.setLooping(true);
                                }
                            });
                            //播放完成回调
                            vv1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    //设置视频路径
                                    vv1.setVideoURI(uri);
                                    //开始播放视频
                                    vv1.start();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: +");
//        Heart.resume();
        if (isFirst) {
//            sendDoor();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
//        Heart.pause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: +++++");
//        Heart.destory();
        if (vv1 != null && vv1.isPlaying()) {
            vv1.stopPlayback();
        }
        handler.removeCallbacks(runnable);
    }

    //终端签到
    //    String param = "terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    private void initData(final int flag) {
        //提示正在签到
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.signin);
        dialog1.setCancelable(false);
        if (!isFinishing()) {
            dialog1.show();
        }
        WindowManager.LayoutParams params_ = dialog1.getWindow().getAttributes();
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        params_.width = (int) (width * 0.65);
        params_.height = (int) (height * 0.35);
        dialog1.getWindow().setAttributes(params_);
        //网络请求签到
        String url = Comment.URL + "/terminal/signin";
        timeStamp = getTimeStamp();
        Log.d("xxxxx", Comment.terminal_code + "");
        //auth_sign MD5加密
        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);// "12345678":签到秘钥 先用这个，之后会改动的
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        params.put("auth_sign", ciphertext);//签名

        initDialg(flag, url, params);
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
        gLogger = Logger.getLogger("DrinksAndSnacksFragment");
    }
    int i=1;
    private void initDialg(final int flag, final String url, final Map<String, String> params) {
        gLogger.debug("第"+i+"次发送签到请求");
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponseD:失败" + e);
                Log.d("TAG", "onResponseD:失败" + params);
                gLogger.debug("第"+i+"次请求失败");
                try {
                    Thread.sleep(3000);
                    i++;
                    initDialg(flag,url,params);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseD:成功" + response);
                gLogger.debug("第"+i+"次请求成功");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        String err_desc = jsonObject.getString("err_desc");
                        TcnUtility.getToast(InitActivity.this, err_desc);
                        return;
                    }
                    key = jsonObject.getString("key");
                    Comment.KEY = key;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (moshi != 4) {
                    initAdvertise();
                }
                if (flag == 0) {
                    send();
                } else if (flag == -1) {
//                    initProfile();
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

    //终端广告
    private void initAdvertise() {
        titles = new ArrayList<>();
        images = new ArrayList<>();
//        final int listSize = getIntent().getIntExtra("setListNum", 0);
//        moshi = getIntent().getIntExtra("styleDaiJi", 0);
//        if (moshi == 0) {
//            titles.add("");
//            titles.add("");
//            titles.add("");
//            images.add(Comment.URL_IMG_OR_VIDEO+"/image/ad20181118093800.jpg");
//            images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg");
//            images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg");
//            setBannerData();
//            return;
//        }
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        String url = Comment.URL + "/terminal/advertisement";//  advertisement/list  terminal/advertisement 两个都是广告的接口
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//        params.put("direction", String.valueOf(v));
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponseA:失败" + e);
//                dialog1.dismiss();
//                initData(-2);
                initAdvertise();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseA:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
//                        dialog1.dismiss();
//                        initData(-2);
                        initAdvertise();
                        return;
                    }
                    int width = getAndroiodScreenProperty();
                    int height = getAndroiodScreenPropertyH();
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String ad_name = object.getString("ad_name");//轮播图名称
                        String ad_type = object.getString("ad_type");//类型
                        String filename = object.getString("filename");//图片 "http://file.qiyuwulian.top/image/"
                        int time_start = object.getInt("time_start");//广告生效日期 精确到日
                        int time_end = object.getInt("time_end");//广告结束日期 精确到日
                        boolean long_term = object.getBoolean("long_term");//表示是否受time控制
                        boolean frozen = object.getBoolean("frozen");//表示当前是否冻结，如果冻结就不播放
                        String localtion = object.getString("localtion");//横版还是竖版 v:竖版 h:横版
                        if (width < height) {
                            if (ad_type.equals("image")&&localtion.equals("ad_v")) {
                                titles.add("");
                                images.add(Comment.URL_IMG_OR_VIDEO + "/image/" + filename);
//                            AdvertisementBean bean = new AdvertisementBean();
//                            bean.setAd_filename("http://file.qiyuwulian.top/image/" + filename);
//                            bean.setAd_name(ad_name);
//                            list.add(bean);
                            }
                        } else {
                            if (ad_type.equals("image")&&localtion.equals("ad_h")) {
                                titles.add("");
                                images.add(Comment.URL_IMG_OR_VIDEO + "/image/" + filename);
                            }
                        }

                    }
//                    if ( != 0) {
//                        //循环所有轮播图判断存list1里
//                        for (int j = 0; j < list.size(); j++) {
//                            AdvertisementBean bean = list.get(j);
//                            list1.add(bean);
//                        }
//                        Log.d(TAG, "onResponse: "+list);
//                        //通过待机页管理设置的轮播图个数，从list1循环取值，显示
//                        for (int i = 0; i < listSize; i++) {
//                            AdvertisementBean bean1 = list1.get(i);
//                            titles.add(bean1.getAd_name());
//                            images.add(bean1.getAd_filename());
//                        }
//                        Log.d(TAG, "initAdvertise: " + images.size());
//                    } else {
//                        titles.add("");
//                        titles.add("");
//                        titles.add("");
//                        images.add("http://file.qiyuwulian.top/image/ad20181118093800.jpg");
//                        images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg");
//                        images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg");
//                    }
                    setBannerData();
                    initProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //终端信息
    private void initProfile() {
        String url = Comment.URL + "/terminal/profile";
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        Log.d("TAG", "initProfile: " + signature);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponseP:失败" + e);
                dialog1.dismiss();
//                initData(-1);
                initProfile();
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.d("TAG", "onResponseP:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        dialog1.dismiss();
//                        initData(-1);
                        initProfile();
                        return;
                    }
//                    Log.d(TAG, "onRe: " + jsonObject.getString("data"));
                    jsonObject = jsonObject.getJSONObject("data");
                    //终端编号（设备定义的）
                    terminal_no = jsonObject.getString("terminal_no");
                    String name = jsonObject.getString("name");//终端名称
                    String address = jsonObject.getString("address");//终端地址
                    JSONObject model = jsonObject.getJSONObject("model");
                    int model_id = model.getInt("id");
                    String model_name = model.getString("model_name");//设备型号（货道的）

                    String tunnel = jsonObject.getString("tunnel");//商品列表
                    String payment = jsonObject.getString("payment");//支付方式
                    editor.putString("tunnel", tunnel);
                    editor.putString("payment", payment);
                    editor.putString("terminal_no", terminal_no);
                    editor.putString("key", key);//签名用到的key
                    editor.putString("model_id", String.valueOf(model_id));
                    editor.commit();
                    tv_machineId.setText(terminal_no);
                    bean = new SheBeiBean();
                    bean.setTerminal_no(terminal_no);
                    bean.setTerminal_name(name);
                    bean.setTerminal_address(address);
                    bean.setAisle_model(model_name);
                    dialog1.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 轮播图数据
     */
    private void setBannerData() {
        if (!isFinishing()&&!isDestroyed()) {
            mBanner.startAutoPlay();
            //设置图片集合
            mBanner.setImages(images);
            //设置标题集合（当banner样式有显示title时）
            mBanner.setBannerTitles(titles);
            //设置指示器位置（当banner模式中有指示器时）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            mBanner.start();
        }
    }

    //初始化
    private void init() {
        rl_singleImg = findViewById(R.id.ll_singleImg);// 模式一 全图
        rl_upImg = findViewById(R.id.ll_upImg);//模式二 上图下视频
        rl_downImg = findViewById(R.id.ll_downImg);// 模式三 上视频下图
        rl_null = findViewById(R.id.ll_null);// 模式四 全视频

        iv_logo = findViewById(R.id.iv_logo);
//        iv_no1 = findViewById(R.id.iv_no1);
//        iv_no2 = findViewById(R.id.iv_no2);
//        iv_no3 = findViewById(R.id.iv_no3);
//        iv_no4 = findViewById(R.id.iv_no4);
//        iv_no5 = findViewById(R.id.iv_no5);
        tv_machineId = findViewById(R.id.tv_machineId);
        //这两个固定不变的
//        tv_phone = findViewById(R.id.tv_phone);
//        tv_tel = findViewById(R.id.tv_tel);
        vv1 = findViewById(R.id.vv1);

        if (moshi != 4) {
            mBanner = findViewById(R.id.banner);
            //设置banner样式
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            if (isFinishing()) {
                return;
            }
            //设置图片加载器
            mBanner.setImageLoader(new GlideImageLoader());

            //banner点击事件
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    //有效期
                    if (time()) {
                        return;
                    }
//                    TcnUtility.getToast(InitActivity.this, "点击了轮播图");
                    int style = spf.getInt("style", 0);
                    Intent intent = new Intent(InitActivity.this, MainActivity.class);
                    Log.d(TAG, "onClick: " + style);
                    intent.putExtra("styleShopping", style);
                    intent.putExtra("shebei", bean);
                    startActivity(intent);
                    finish();
                    handler.removeCallbacks(runnable);
                }
            });
        }
    }

    //有效期
    private boolean time() {
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
        long time = date.getTime() / 1000;
        Log.d(TAG, "time: " + time);
//        if (time > 1559209656) {  // 1559209656: 2019-05-30 17:47:36
//            TcnUtility.getToast(InitActivity.this, "测试版有效期已过，请重续");
//            return true;
//        }

        return false;
    }

    //请求图片
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(InitActivity.this).load(path).into(imageView);
        }
    }
}
