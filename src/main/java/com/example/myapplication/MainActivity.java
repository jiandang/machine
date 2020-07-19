package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.myapp.R;
import com.example.myapplication.adapter.MFragmentPagerAdapter;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.fragment.CouponPickingFragment;
import com.example.myapplication.fragment.DrinksAndSnacksFragment;
import com.example.myapplication.fragment.TransportationCardFragment;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends FragmentActivity {
    private TextView tv_machineId;
    private ImageView iv_logo;
    private String str;
    private Dialog dialog;
    //饮料零食
    private TextView drinkTextView;
    private ImageView iv_drink;
    private LinearLayout ll_drink;
    //日用百货
    private TextView dailyTextView;
    private ImageView iv_daily;
    private LinearLayout ll_daily;
    //交通卡
    private TextView transportTextView;
    private ImageView iv_transport;
    private LinearLayout ll_transport;
    //卡券取货
    private TextView cardTextView;
    private ImageView iv_card;
    private LinearLayout ll_card;
    //实现Tab滑动效果
    private ViewPager mViewPager;
    private Banner mBanner;

    //动画图片
//    private ImageView cursor;
    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    //动画图片宽度
    private int bmpW;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;

    public static final String TAG = "MainActivity";
    private int level;
    private MFragmentPagerAdapter adapter;

    private int moshi;//模式
    private List<String> images;
    private List<String> titles;
    private VideoView vv1;
    private SharedPreferences spf;
    private long timeStamp;
    private String key;
    private String terminal_no;//终端编号（设备定义的）
    private Bitmap bitmap;
    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        moshi = intent.getIntExtra("styleShopping", 0);
        if (moshi == 0) {
            moshi = 1;
        }
        Log.d(TAG, "onCreateMain2: " + moshi);
        if (moshi == 1) {
            setContentView(R.layout.activity_main);
            //初始化
            init();
            initData();
        } else if (moshi == 2) {
            setContentView(R.layout.activity_main1);
            //初始化
            init();
            initData();
            video();
        } else if (moshi == 3) {
            setContentView(R.layout.activity_main2);
            //初始化
            init();
            initData();
            initBanner();
        }
        spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        terminal_no = spf.getString("terminal_no", "");
        tv_machineId.setText(terminal_no);
        play(0);
        //初始化Fragment
        InitFragment();
        //初始化ViewPager
        InitViewPager();
        //初始化ImageView
//        InitImageView();
        iv_logo.setOnClickListener(new View.OnClickListener() {
            long[] mHits = new long[3];
            @Override
            public void onClick(View v) {
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
    //弹出二维码
    private void ecode() {
        //fragment 里app下的AlertDialog要加样式
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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
                TcnUtility.getToast(MainActivity.this, "获取失败");
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
                        TcnUtility.getToast(MainActivity.this,err_desc);
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    TcnUtility.getToast(MainActivity.this, "登录成功");
                    Serializable bean = getIntent().getSerializableExtra("shebei");
                    Intent intent = new Intent(MainActivity.this, EquipmentAndManageActivity.class);
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
                TcnUtility.getToast(MainActivity.this, "获取失败");
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
                            TcnUtility.getToast(MainActivity.this,"请扫码");
                            return;
                        }
                        TcnUtility.getToast(MainActivity.this,err_desc);
                        dialog.dismiss();
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    TcnUtility.getToast(MainActivity.this, "获取成功");
                    Serializable bean = getIntent().getSerializableExtra("shebei");
                    Intent intent = new Intent(MainActivity.this, EquipmentAndManageActivity.class);
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
    private MediaPlayer mediaPlayer;
    // 初始化MediaPlayer
    private void play(final int result) {
        try {
            if (result == 0) {
                mediaPlayer = MediaPlayer.create(this, R.raw.welcome);
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.stickcard);
            }
//            // 设置指定的流媒体地址
//            mediaPlayer.setDataSource(path);
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//            // 通过异步的方式装载媒体资源
//            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕 开始播放流媒体
                    mediaPlayer.start();
//                    Toast.makeText(MainActivity.this, "开始播放", 0).show();
                }
            });
            // 设置循环播放
            // mediaPlayer.setLooping(true);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被调用
//                    Toast.makeText(MainActivity.this, "播放完毕", 0).show();
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
                    replay(result);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(this, "播放失败", 0).show();
        }
    }

    //重新播放
    protected void replay(int result) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
//            Toast.makeText(this, "重新播放", 0).show();
            return;
        }
        play(result);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        super.onResume();
    }

    /**
     * 初始化tab
     */
    private void init() {
        tv_machineId = findViewById(R.id.tv_machineId);
        iv_logo = findViewById(R.id.iv_logo);
        //饮料零食tab
        drinkTextView = (TextView) findViewById(R.id.drink_text);
        iv_drink = findViewById(R.id.iv_drink);
        ll_drink = findViewById(R.id.ll_drink);
        //日用百货tab
        dailyTextView = (TextView) findViewById(R.id.daily_text);
        iv_daily = findViewById(R.id.iv_daily);
        ll_daily = findViewById(R.id.ll_daily);
        //交通卡tab
        transportTextView = (TextView) findViewById(R.id.transport_text);
        iv_transport = findViewById(R.id.iv_transport);
        ll_transport = findViewById(R.id.ll_transport);
        //设备管理tab
        cardTextView = (TextView) findViewById(R.id.card_text);
        iv_card = findViewById(R.id.iv_card);
        ll_card = findViewById(R.id.ll_card);
        //添加点击事件
        ll_drink.setOnClickListener(new MyOnClickListener(0));
//        ll_daily.setOnClickListener(new MyOnClickListener(1));
        ll_transport.setOnClickListener(new MyOnClickListener(1));
        ll_card.setOnClickListener(new MyOnClickListener(2));
        if (moshi == 3) {
            mBanner = findViewById(R.id.banner);
            //设置banner样式
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片加载器
            mBanner.setImageLoader(new GlideImageLoader());
        } else if (moshi == 2) {
            vv1 = findViewById(R.id.vv1);
        }
    }

    //请求图片
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(MainActivity.this).load(path).into(imageView);
        }
    }

    //获取图片
    private void initBanner() {
        titles = new ArrayList<>();
        images = new ArrayList<>();
//        final int listSize = getIntent().getIntExtra("setListNum", 0);
        moshi = getIntent().getIntExtra("styleShopping", 0);
        if (moshi == 0) {
            titles.add("");
            titles.add("");
            titles.add("");
            images.add(Comment.URL_IMG_OR_VIDEO + "/image/ad20181118093800.jpg");
            images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314521777492.jpg");
            images.add("https://www.33lc.com/article/UploadPic/2012-7/201272314564149557.jpg");
            setBannerData();
            return;
        }
        String url = Comment.URL + "/terminal/advertisement";//  advertisement/list  terminal/advertisement 两个都是广告的接口
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", "8986001200ACBDA01234");//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponseA:失败" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseA:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
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
                        if (ad_type.equals("image")) {
                            titles.add(ad_name);
                            images.add(Comment.URL_IMG_OR_VIDEO + "/image/" + filename);
                        }
                    }
                    setBannerData();
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
        mBanner.startAutoPlay();
        //设置图片集合
        mBanner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titles);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    //视频播放
    private void video() {
        //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
        String videoUrl1 = Environment.getExternalStorageDirectory().getPath() + "/fl1234.mp4";

//        //网络视频
//        List<SettingBean> list = (List<SettingBean>) getIntent().getSerializableExtra("setVideoList");
//        String imgUrl = null;
//        if (list != null) {
//            imgUrl = list.get(0).getImgUrl();
//        } else {
//            imgUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        }
        String url = Comment.URL + "/terminal/advertisement";//  advertisement/list  terminal/advertisement 两个都是广告的接口
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", "8986001200ACBDA01234");//终端编号（平台定义）
//        params.put("direction", String.valueOf(v));
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&key=" + key);
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
                            vv1.setMediaController(new MediaController(MainActivity.this));
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

    //终端签到
    //    String param = "terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    private void initData() {
        String url = Comment.URL + "/terminal/signin";
        timeStamp = getTimeStamp();
        Log.d("xxxxx", timeStamp + "");
        key = Comment.KEY;
        //auth_sign MD5加密
//        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
//        final Map<String, String> params = new HashMap<>();
//        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
//        params.put("auth_sign", ciphertext);//签名
//
//        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.d("TAG", "onResponseMainData:失败" + e);
//                Log.d("TAG", "onResponseMainData:失败" + params);
////                initData();
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                Log.d("TAG", "onResponseMainData:成功" + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    key = jsonObject.getString("key");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrinksAndSnacksFragment fragment = (DrinksAndSnacksFragment) adapter.getItem(0);
        fragment.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        // 在activity结束的时候回收资源
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            Toast.makeText(MainActivity.this, "播放停止", 0).show();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        Log.d(TAG, "onDestroy: +++++");
        if (vv1 != null && vv1.isPlaying()) {
            vv1.stopPlayback();
        }
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        adapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        mViewPager.setAdapter(adapter);

        //让ViewPager缓存3个页面
        mViewPager.setOffscreenPageLimit(3);

        //设置默认打开第一页
//        mViewPager.setCurrentItem(0, true);//true:页面切换慢，false:页面切换快

        //将顶部文字恢复默认值
        resetTextViewTextColor();
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        if (width < height) {
            ll_drink.setBackgroundResource(R.drawable.footer_bg_xuanzhong1);
            ll_transport.setBackgroundResource(R.drawable.footer_bg1);
        } else {
            ll_drink.setBackgroundResource(R.drawable.footer_bg_xuanzhong2);
            ll_transport.setBackgroundResource(R.drawable.footer_bg2);
        }
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_drink.getLayoutParams();
        linearParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics()));
        ll_drink.setLayoutParams(linearParams);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
//        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = (screenW / 4);

        //设置动画图片宽度
//        setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new DrinksAndSnacksFragment());
        ll_daily.setVisibility(View.GONE);
//        fragmentArrayList.add(new DailyAndDepartmentFragment());
        fragmentArrayList.add(new TransportationCardFragment());
        ll_card.setVisibility(View.GONE);
//        fragmentArrayList.add(new CouponPickingFragment());
        fragmentManager = getSupportFragmentManager();

    }

    /**
     * tab点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_drink);
                    } else if (currIndex == 2) {//从页卡1跳转转到页卡3
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_drink);
                    } else if (currIndex == 3) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_drink);
                    }
                    if (currIndex != 0) {
                        DrinksAndSnacksFragment fragment = (DrinksAndSnacksFragment) adapter.getItem(position);
                        fragment.initState();
                    }
                    break;

                //当前为页卡2
                case 1:
                    //从页卡2跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_daily);
                        tab(ll_transport);
                    } else if (currIndex == 2) { //从页卡2跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_daily);
                        tab(ll_transport);
                    } else if (currIndex == 3) { //从页卡2跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_daily);
                        tab(ll_transport);
                    }
//                    DailyAndDepartmentFragment fragment1 = (DailyAndDepartmentFragment) adapter.getItem(position);
//                    fragment1.initState();
                    TransportationCardFragment fragment2 = (TransportationCardFragment) adapter.getItem(position);
                    play(1);
                    fragment2.initKongJian();
                    break;

                //当前为页卡3
                case 2:
                    //从页卡3跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_transport);
                        tab(ll_card);
                    } else if (currIndex == 1) {//从页卡3跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_transport);
                        tab(ll_card);
                    } else if (currIndex == 3) {//从页卡3跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
//                        tab(ll_transport);
                        tab(ll_card);
                    }
                    CouponPickingFragment fragment3 = (CouponPickingFragment) adapter.getItem(position);
                    fragment3.init();
                    break;
                //当前为页卡4
                case 3:
                    //从页卡4跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_card);
                    } else if (currIndex == 1) {//从页卡4跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_card);
                    } else if (currIndex == 2) {//从页卡4跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        resetTextViewTextColor();
                        //导航栏选择样式
                        tab(ll_card);
                    }
//                    CouponPickingFragment fragment3 = (CouponPickingFragment) adapter.getItem(position);
//                    fragment3.init();
                    break;
            }
            currIndex = position;

            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
//            cursor.startAnimation(animation);

        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    //导航栏选择样式
    private void tab(LinearLayout ll) {
//        ll.setBackgroundResource(R.drawable.footer_bg_xuanzhong);
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        if (width < height) {
            ll.setBackgroundResource(R.drawable.footer_bg_xuanzhong1);
        } else {
            ll.setBackgroundResource(R.drawable.footer_bg_xuanzhong2);
        }
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) ll.getLayoutParams();
        linearParams3.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics()));
        ll.setLayoutParams(linearParams3);
    }

    /**
     * 将顶部文字背景恢复默认值
     */
    private void resetTextViewTextColor() {
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        if (width < height) {
            ll_drink.setBackgroundResource(R.drawable.footer_bg1);
            ll_transport.setBackgroundResource(R.drawable.footer_bg1);
        } else {
            ll_drink.setBackgroundResource(R.drawable.footer_bg2);
            ll_transport.setBackgroundResource(R.drawable.footer_bg2);
        }
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_drink.getLayoutParams();
        linearParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        ll_drink.setLayoutParams(linearParams);

//        ll_daily.setBackgroundResource(R.drawable.footer_bg);
//        LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) ll_daily.getLayoutParams();
//        linearParams1.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
//        ll_daily.setLayoutParams(linearParams1);

        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) ll_transport.getLayoutParams();
        linearParams2.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        ll_transport.setLayoutParams(linearParams2);

//        ll_card.setBackgroundResource(R.drawable.footer_bg);
//        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) ll_card.getLayoutParams();
//        linearParams3.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
//        ll_card.setLayoutParams(linearParams3);
    }
}
