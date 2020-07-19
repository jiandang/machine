package com.example.myapplication.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.com.tcn.sdk.springdemo.LoadingDialog;
import com.com.tcn.sdk.springdemo.OutDialog;
import com.com.tcn.sdk.springdemo.TcnUtilityUI;
import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.CouponPickingActivity;
import com.example.myapplication.InitActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.adapter.PayAdapter;
import com.example.myapplication.bean.DrinkBean;
import com.example.myapplication.bean.Order;
import com.example.myapplication.bean.SettingBean;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.GlideUtils;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.MyGridView;
import com.example.myapplication.util.PageIndicatorView;
import com.example.myapplication.util.PageRecyclerView;
import com.example.myapplication.util.Utils;
import com.tcn.springboard.control.TcnVendEventID;
import com.tcn.springboard.control.TcnVendEventResultID;
import com.tcn.springboard.control.TcnVendIF;
import com.tcn.springboard.control.VendEventInfo;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * 这是饮料零食页
 */
public class DrinksAndSnacksFragment extends Fragment {
    private static final String TAG = "DrinksAndSnacksFragment";
    @InjectView(R.id.ll_xianJin)
    LinearLayout ll_xianJin;
    @InjectView(R.id.ll_kaquan)
    LinearLayout ll_kaquan;
    @InjectView(R.id.gv)
    MyGridView gv;
    @InjectView(R.id.v)
    View v;

    private PageRecyclerView mRecyclerView = null;
    private List<DrinkBean> dataList = null;
    private List<SettingBean> list = null;
    private PageRecyclerView.PageAdapter myAdapter = null;
    private PayAdapter adapter = null;
    private LinearLayout ll_shop, ll_pay;
    private Button btn_return;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private String shopName;//商品名称
    private String shopPrice;//商品价格
    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig, daoConfig1;
    private String goods_no;//商品编号
    private String ImgUrl;//商图片
    private String key;
    private String tunnel_no;//货道编号
    private boolean isFirst = true;//是否第一次进来
    private String tunnel_code;//货道号
    private long timeStamp;//时间戳（秒级）
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private MainActivity.MyTouchListener myTouchListener;
    private int delayMillis;//返回待机页的时间
    private int order_status;//订单状态
    private int trade_status;//支付状态
    private int refund_status;//退款状态
    private int deliver_status;//出货状态
    private String channel_code;//支付方式
    private String stock;//库存
    private boolean isFlag;//第一次存数据
    private int code = -1;//二维码获取成功or失败 0：失败 1：成功
    private ImageButton ib_return;

    public DrinksAndSnacksFragment() {

    }

    private Runnable runnable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    break;
                case 0:
                    setData();
                    break;
            }
        }
    };
    private String order_no;//订单生成的时候返回的
    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    //1.创建Handler实例
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handle: +++++" + msg);
            int style1 = spf.getInt("style1", 0);//获取待机页的样式
            if (style1 == 0) {
                style1 = 1;
            }
//            cancle = true;
            handler.removeCallbacks(runnable);
            if (mActivity.isDestroyed() || mActivity.isFinishing()) {
                return;
            }
            Intent intent = new Intent(mActivity, InitActivity.class);
            intent.putExtra("styleDaiJi", style1);
            Log.d(TAG, "handleMessage: " + getActivity());
            Log.d(TAG, "handleMessage1: " + mActivity);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    };
    //2.创建一个Runnable
    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            //用户5秒没操作了
            Log.d(TAG, "run: aaaaaaaaaaaaaaaaa");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinks_and_snacks, container, false);
        ButterKnife.inject(this, view);
        configLog();
        //初始化数据库配置信息
        initDaoConfig();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        editor = spf.edit();
        int style = spf.getInt("style", 0);
        if (style == 0 || style == 1) {
//            v.setVisibility(View.VISIBLE);
        }
//        isFlag = spf.getBoolean("isFlag", false);//true:修改 false: 添加
//        Log.d(TAG, "onCreateView: "+isFlag);
        init(view);
        initData();
        mRecyclerView.setVisibility(View.INVISIBLE);//因为第一次进来的数据不全，所以需要隐藏掉
        setData();
        initState();
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_shop.setVisibility(View.VISIBLE);
                ll_pay.setVisibility(View.GONE);
                handler.removeCallbacks(runnable);
            }
        });
        boolean showXianJin = spf.getBoolean("showXianJin", false);
        if (showXianJin) {
            ll_xianJin.setVisibility(View.VISIBLE);
        } else {
            ll_xianJin.setVisibility(View.GONE);
        }
        boolean showKaQuan = spf.getBoolean("showKaQuan", false);
        Log.d(TAG, "onCreateViewDrink: " + showKaQuan);

        if (showKaQuan) {
            ll_kaquan.setVisibility(View.VISIBLE);
        } else {
            ll_kaquan.setVisibility(View.GONE);
        }
        touch();
//        if (TcnVendIF.getInstance().isServiceRunning()) {
//        } else {
//            getActivity().startService(new Intent(getContext(), VendService.class));
//        }
        return view;
    }

    //fragment中的touch事件方法
    private void touch() {
        /* Fragment中，注册
        * 接收MainActivity的Touch回调的对象
        * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
        */
        // 处理手势事件
        //手指下来的时候,取消之前绑定的Runnable
        //手指离开屏幕，发送延迟消息 ，5秒后执行
        myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {    //手指下来的时候,取消之前绑定的Runnable
                        handler1.removeCallbacks(runnable1);
                        Log.d(TAG, "onTouchEventD: ++++++++++++");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {    //手指离开屏幕，发送延迟消息 ，5秒后执行
                        handler1.removeMessages(0);
                        String returnTime = spf.getString("returnTime", "");
                        if (!returnTime.equals("") && returnTime != null) {
                            delayMillis = Integer.parseInt(returnTime);
                            Log.d(TAG, "onTouchEvent: " + delayMillis);
//                            TcnUtility.getToast(getActivity(),delayMillis+"/s");
                            handler1.sendEmptyMessageDelayed(0, 1000 * delayMillis);
                        } else {
                            TcnUtility.getToast(getActivity(), "请先设置返回时间");//目前先写，后期可能会删掉
                        }
                        break;
                    }
                }
            }
        };
        // 将myTouchListener注册到分发列表 （放在onResume()了）
//        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }

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
                if(list.size() == 1) gv.setNumColumns(1);
                else if(list.size() == 2) gv.setNumColumns(2);
                else if(list.size() == 3) gv.setNumColumns(3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        for(int i = 0;i<name.length;i++){
//            SettingBean bean = new SettingBean();
//            bean.setImgName(name[i]);
//            bean.setImgUrl(img[i]);
//            list.add(bean);
//        }
        Log.d(TAG, "initPayData: " + list);
        setPay();
    }

    //设置支付方式
    Dialog dialog;

    private void setPay() {
        adapter = new PayAdapter(list, getActivity());
        if (gv != null) {
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gv.setEnabled(false);
                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.shop_pay);
                    TextView tv_a = dialog.findViewById(R.id.tv_a);
                    TextView tv_fangshi = dialog.findViewById(R.id.tv_fangshi);
                    TextView tv_b = dialog.findViewById(R.id.tv_b);
                    ImageView iv_erweima = dialog.findViewById(R.id.iv_erweima);//二维码
                    ib_return = dialog.findViewById(R.id.ib_return);

                    ImageView iv_img = dialog.findViewById(R.id.iv_img);//商品图
                    TextView tv_prive = dialog.findViewById(R.id.tv_prive);//商品价格
                    TextView tv_name = dialog.findViewById(R.id.tv_name);//商品名
                    TextView tv_code = dialog.findViewById(R.id.tv_code);//货道号
                    tv_prive.setText("￥" + shopPrice);
                    tv_code.setText(tunnel_code+"-");
                    tv_name.setText(shopName.replaceAll("\r\n|\r|\n| ", ""));
                    GlideUtils.load(getActivity(), ImgUrl, iv_img);
                    LinearLayout ll_background = dialog.findViewById(R.id.ll_background);//背景
                    View v = dialog.findViewById(R.id.v);
                    ib_return.setEnabled(false);
                    ib_return.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (code == 1) { //成功
                                handler.removeCallbacks(runnable);
//                                cancle = true;
                                numCancle = 0;
                                Log.d(TAG, "onClick: +++++++");
                                orderCancle();
                            } else {
                                dialog.cancel();
                            }
                        }
                    });
                    dialog.show();
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    //                params.height = 800 ;
                    int width = getAndroiodScreenProperty();
                    params.width = (int) (width * 0.6);
                    dialog.getWindow().setAttributes(params);
                    SettingBean bean = list.get(position);
                    String shopName = bean.getImgName();
                    channel_code = bean.getImgPay();
                    i = 0;
//                    initSignin(-1);
                    code = -1;
                    switch (shopName) {
                        case "微信支付":
                            q = 0;
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("微信");
                            tv_b.setText("扫码支付");
                            if (isAdded()) {
                                tv_a.setTextColor(getResources().getColor(R.color.white));
                                tv_fangshi.setTextColor(getResources().getColor(R.color.white));
                                tv_b.setTextColor(getResources().getColor(R.color.white));
                                ll_background.setBackgroundColor(getResources().getColor(R.color.color1));
                            }
                            iv_erweima.setImageResource(R.drawable.loading);
                            v.setVisibility(View.GONE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui2);
                            break;
                        case "支付宝":
                            q = 0;
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("支付宝");
                            tv_b.setText("扫码支付");
                            if (isAdded()) {
                                tv_a.setTextColor(getResources().getColor(R.color.white));
                                tv_fangshi.setTextColor(getResources().getColor(R.color.white));
                                tv_b.setTextColor(getResources().getColor(R.color.white));
                                ll_background.setBackgroundColor(getResources().getColor(R.color.color2));
                            }
                            iv_erweima.setImageResource(R.drawable.loading);
                            v.setVisibility(View.GONE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui2);
                            break;
                        case "银联二维码":
                            q = 0;
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("云闪付");
                            tv_b.setText("扫码支付");
                            if (isAdded()) {
                                tv_a.setTextColor(getResources().getColor(R.color.black));
                                tv_fangshi.setTextColor(getResources().getColor(R.color.black));
                                tv_b.setTextColor(getResources().getColor(R.color.black));
                                ll_background.setBackgroundColor(getResources().getColor(R.color.color3));
                            }
                            iv_erweima.setImageResource(R.drawable.loading);
                            v.setVisibility(View.VISIBLE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui1);
                            break;
                    }
                }
            });
        }
    }

    //取消订单
    int numCancle;

    private void orderCancle() {
        Log.d(TAG, "orderCancle: " + key);
        Log.d(TAG, "orderCancle: " + timeStamp);
//        cancle = true;
        handler.removeCallbacks(runnable);
        //订单取消
        String url = Comment.URL + "/order/cancel";
//      String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
//        if (numCancle < 2) {
        OkHttpUtils.post().url(url).params(params).build().connTimeOut(5000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                try {
//                    cancle = true;
                    handler.removeCallbacks(runnable);
                    orderCancle();
                    i = 0;
//                        Thread.sleep(5000);
//                        initSignin(2);
                    numCancle++;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseCancle:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        String err_desc = jsonObject.getString("err_desc");
//                        cancle = true;
                        handler.removeCallbacks(runnable);
                        if (err_desc.equals("验签错误")) {
                            i = 0;
//                            initSignin(2);
                            numCancle++;
                        }
                    } else {
//                        cancle = true;
                        handler.removeCallbacks(runnable);
                    }
                    dialog.cancel();
                    gv.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        }
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
                chuHuoConfirm();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseConfirm:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    gLogger.debug("出货确认返回："+response);
                    initProfile();
                    gv.setEnabled(true);
//                    String url = Comment.URL+"/order/query";
//                    Map<String, String> params = new HashMap<>();
//                    params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
//                    params.put("order_no", order_no);//订单生成的时候返回的
//                    String signature = MD5Util.encrypt("order_no=" + order_no  + "&terminal_code=8986001200ACBDA01234"  + "&key=" + key);
//                    params.put("signature", signature);//签名参数
//                    Log.d(TAG, "run: "+params);
//                    OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            Log.d("TAG", "onResponse:失败" + e);
//                        }
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d("TAG", "onResponse:成功S" + response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    RecyclerView.ViewHolder vh;
    boolean isOne = true;//是否第一次点击商品详情按钮

    //设置商品数据
    private void setData() {
        Log.d(TAG, "setData: " + dataList);
        if (dataList != null && dataList.size() > 0) {
            myAdapter = mRecyclerView.new PageAdapter(dataList, new PageRecyclerView.CallBack() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    int width = getAndroiodScreenProperty();
                    int height = getAndroiodScreenPropertyH();
                    View view;
                    if (width < height) {
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_drink, parent, false);
                    } else {
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_drink1, parent, false);
                    }
                    return new MyHolder(view);
                }

                @Override
                public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
                    vh = holder;

                    if (position > dataList.size()) {
//                        TcnUtility.getToast(getContext(), "数组长度：" + dataList.size() + "+++当前位置：" + position);
                        return;
                    }
                    final DrinkBean drinkBean = dataList.get(position);
                    ((MyHolder) holder).tv_name.setText(drinkBean.getName());
                    ((MyHolder) holder).tv_price.setText("￥" + drinkBean.getPrice());
                    GlideUtils.load(getActivity(), drinkBean.getImgUrl(), ((MyHolder) holder).iv_img);
                    tunnel_code = drinkBean.getTunnel_code();
//                    if (tunnel_code.substring(0, 1).equals("1")) {
//                        ((MyHolder) holder).tv_huodaoNum.setText(tunnel_code.substring(1));
//                    } else {
                    ((MyHolder) holder).tv_huodaoNum.setText(tunnel_code);
//                    }
                    String stock1 = drinkBean.getStock();
                    if (stock1.equals("0")) {
                        ((MyHolder) holder).iv_img.setEnabled(false);
                        ((MyHolder) holder).btn_buy.setEnabled(false);
                        ((MyHolder) holder).tv_soldOut.setVisibility(View.VISIBLE);
                    } else {
                        ((MyHolder) holder).iv_img.setEnabled(true);
                        ((MyHolder) holder).btn_buy.setEnabled(true);
                        ((MyHolder) holder).tv_soldOut.setVisibility(View.GONE);
                    }
                    //商品详情
                    ((MyHolder) holder).btn_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MyHolder) holder).btn_detail.setEnabled(false);
                            goods_no = drinkBean.getGoodsNum();
//                            if (isOne) {
//                                isOne = false;
                            detail(goods_no, ((MyHolder) holder).btn_detail);
//                            }
                        }
                    });
                    //商品购买
                    ((MyHolder) holder).btn_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll_shop.setVisibility(View.GONE);
                            ll_pay.setVisibility(View.VISIBLE);
                            shopName = drinkBean.getName();
                            shopPrice = drinkBean.getPrice();
                            goods_no = drinkBean.getGoodsNum();
                            ImgUrl = drinkBean.getImgUrl();
                            tunnel_no = drinkBean.getTunnel_no();
                            tunnel_code = drinkBean.getTunnel_code();
                            stock = drinkBean.getStock();
                            Log.d(TAG, "onClick: " + shopName);
                            Log.d(TAG, "全局货道号: " + tunnel_code);
                            gLogger.debug("全局货道号:" + tunnel_code);
                            play(3);//提示音
                            gv.setEnabled(true);
                            //获取支付方式
                            initPayData();
                        }
                    });
                    //点击商品图片进行商品购买
                    ((MyHolder) holder).iv_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll_shop.setVisibility(View.GONE);
                            ll_pay.setVisibility(View.VISIBLE);
                            shopName = drinkBean.getName();
                            shopPrice = drinkBean.getPrice();
                            goods_no = drinkBean.getGoodsNum();
                            ImgUrl = drinkBean.getImgUrl();
                            tunnel_no = drinkBean.getTunnel_no();
                            tunnel_code = drinkBean.getTunnel_code();
                            Log.d(TAG, "onClick: " + shopName);
                            Log.d(TAG, "全局货道号: " + tunnel_code);
                            gLogger.debug("全局货道号:" + tunnel_code);
                            play(3);//提示音
                            gv.setEnabled(true);
                            //获取支付方式
                            initPayData();
                        }
                    });
                }

                @Override
                public void onItemClickListener(View view, int position) {
//                    Toast.makeText(getActivity(), "点击："
//                            + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClickListener(View view, int position) {

                }
            });
            mRecyclerView.setAdapter(myAdapter);
        }
        if (!isFirst) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            isFirst = false;
        }
    }

    //商品详情
    private void detail(final String goods_no, final Button btn_detail) {
        String url = Comment.URL + "/good/detail";
        //                        String key = spf.getString("key", "");
        Log.d(TAG, "getData: " + key);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("good_no", goods_no);//商品编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("good_no=" + goods_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "getData: " + params);
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
//                initSignin(0);
                detail(goods_no, btn_detail);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
//                        initSignin(0);
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        jsonObject = jsonObject.getJSONObject("product");
                        String name_show = jsonObject.getString("name_show");//商品名称
                        String brand = jsonObject.getString("brand");//商品品牌
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.shop_detail);
                        TextView tv_shopName = dialog.findViewById(R.id.tv_shopName);//商品名称
                        TextView tv_brand = dialog.findViewById(R.id.tv_brand);//商品品牌
                        tv_shopName.setText(name_show.replaceAll("\r\n|\r|\n| ", ""));
                        tv_brand.setText(brand);
                        dialog.show();
                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        int width = getAndroiodScreenProperty();
                        params.width = (int) (width * 0.85);
                        dialog.getWindow().setAttributes(params);
                        isOne = true;
                        btn_detail.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取商品数据
    private void initData() {
        dataList = new ArrayList<>();
//        String text[] = {"255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
//                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
//                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
//                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
//                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
//                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶"};
        String tunnel = spf.getString("tunnel", "");
        try {
            JSONArray jsonArray = new JSONArray(tunnel);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String tunnel_no = object.getString("tunnel_no");
                String tunnel_code = object.getString("tunnel_code");
                stock = object.getString("stock");
                //暂时用不到了，以后可能会用到
//                if(tunnel_code.substring(0,1).equals("1")){
//                    //查询所有数据
//                    List<Goods> all = db1.findAll(Goods.class);
//                    if(!isFlag&all==null){
//                        try {
//                            //根据配置信息获取操作数据的db对象
//                            db1 = x.getDb(daoConfig1);
//                            Goods goods = new Goods();
//                            goods.setTunnel_code(tunnel_code.substring(1));
//                            goods.setStock(stock);
//                            db1.save(goods);//插入一条数据
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        try {
//                            db1 = x.getDb(daoConfig1);
//                            //先找到要更新的数据,再设置值,然后更新,当然你也可以使用上面查询数据的方式
//                            Goods goods = db1.findById(Goods.class, i+1);//找到主键为3的值
//                            goods.setStock(stock);
//                            db1.update(goods,"stock");//更新stu3列名为age的数据
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
                if (object.getString("good_no") != null && !object.getString("good_no").equals("")) {
                    JSONObject product = object.getJSONObject("product");
                    String name_show = product.getString("name_show");//商品展示名
                    String pic = product.getString("pic");//商品展示图
                    JSONObject good = object.getJSONObject("good");
                    int price = good.getInt("price");//商品价格
                    String good_no = good.getString("good_no");//商品编号
                    DrinkBean bean = new DrinkBean();
                    bean.setName(name_show);
                    bean.setImgUrl(Comment.URL_IMG_OR_VIDEO + "/image/" + pic);
                    Double d = (double) price / 100;//price:获取的单位是分，所以/100
                    DecimalFormat df = new DecimalFormat("#0.00");
                    String shopprice = df.format(d);
                    bean.setPrice(shopprice);
                    bean.setGoodsNum(good_no);
                    bean.setTunnel_no(tunnel_no);
                    bean.setTunnel_code(tunnel_code);
                    bean.setStock(stock);
                    dataList.add(bean);
                }
            }
//            for (int j = 0; j < 30; j++) {
//                DrinkBean bean = new DrinkBean();
//                bean.setName("测试");
//                bean.setPrice("2.00");
//                bean.setGoodsNum("");
//                bean.setTunnel_no("1");
//                bean.setTunnel_code("2");
//                bean.setStock("1");
//                dataList.add(bean);
//            }
            //暂时用不到了，以后可能会用到
//            //查询所有数据
//            List<Goods> all = db1.findAll(Goods.class);
//            if(all!=null){
//                Log.i("tag", "所有数据:" + all.toString());
//                if(all.size()<60){
//                    for(int code = all.size()+1;code<61;code++){
//                        Goods goods = new Goods();
//                        goods.setTunnel_code(String.valueOf(code));
//                        db1.save(goods);//插入一条数据
//                    }
//                    //查询所有数据
//                    List<Order> all_ = db1.findAll(Order.class);
//                    Log.i("tag", "所有数据:" + all_.toString());
//                }
//                editor.putBoolean("isFirst", true);
//                editor.commit();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        catch (DbException e) {
//            e.printStackTrace();
//        }
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

    //持续向后台发送订单查询
    int a;
    boolean cancle = false;//是否取消查询订单用
    private int duration = 3;      //倒计时3秒
    Timer timer = new Timer();
    private Runnable runnableState;

    private void sockerStar() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 3);
//        Log.d(TAG, "run: ------------------------>" + cancle);
//        if (!cancle) {
                String url = Comment.URL + "/order/query";
//                String key = spf.getString("key", "");
                Map<String, String> params = new HashMap<>();
                params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                params.put("order_no", order_no);//订单生成的时候返回的

                params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
                params.put("signature", signature);//签名参数
                Log.d(TAG, "run: " + params);
//                if (a < 2) {
                OkHttpUtils.post().url(url).params(params).build().connTimeOut(2000).execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("TAG", "onResponsesockerStar:失败" + e);
                        i = 0;
//                    try {
//                        if (!cancle) {
//                            Thread.sleep(3000);
//                            sockerStar();
//                            cancle = false;
//                        }
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                        initSignin(1);

                        handler.removeCallbacks(runnable);
                        sockerStar();
                        a++;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("TAG", "onResponsesockerStar:成功S" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean code = jsonObject.getBoolean("code");
                            if (!code) {
                                String err_desc = jsonObject.getString("err_desc");
                                handler.removeCallbacks(runnable);
                                if (err_desc.equals("验签错误")) {
                                    i = 0;
//                                    initSignin(1);
                                    a++;
                                }
                            } else {
                                JSONObject data = jsonObject.getJSONObject("data");
                                order_status = data.getInt("order_status");
                                if (order_status == 0) {
                                    trade_status = data.getInt("trade_status");
                                    if (trade_status == 1) {
//                                    cancle = true;
                                        refund_status = data.getInt("refund_status");
                                        if (refund_status == 0) {
                                            deliver_status = data.getInt("deliver_status");
                                            if (deliver_status == 0) {
                                                handler.removeCallbacks(runnable);
                                                dialog.cancel();
                                                if (Comment.ORDER_NO.equals(order_no)) {
                                                    return;
                                                }
                                                Comment.ORDER_NO = order_no;
                                                gLogger.debug("订单编号：" + Comment.ORDER_NO);
                                                gLogger.debug("支付成功");
                                                play(2);//提示音
                                                int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                                                if (model_id == 1) {
//                                                    String code1 = tunnel_code.substring(1);
                                                    String code1 = tunnel_code;
                                                    int slotNo = Integer.parseInt(code1);//出货的货道号
                                                    String shipMethod = channel_code; //出货方法,微信支付出货，此处自己可以修改。
                                                    String amount = shopPrice;    //支付的金额（元）,自己修改
                                                    String tradeNo = order_no;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                                                    TcnVendIF.getInstance().reqShip(slotNo, shipMethod, amount, tradeNo);
                                                } else if (model_id == 2) {
                                                    final Dialog dialog = new Dialog(getActivity());
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setContentView(R.layout.pay_and_chu_huo);
                                                    final TextView tv = dialog.findViewById(R.id.tv_tu);
                                                    final TextView tv1 = dialog.findViewById(R.id.tv_tu1);
                                                    final ImageView iv = dialog.findViewById(R.id.iv_tu);
                                                    tv.setText("购买成功，等待商品出货");
                                                    tv1.setText("");
                                                    iv.setImageResource(R.drawable.zhifuchenggong_jiazai);
                                                    dialog.show();
                                                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                                                    int width = getAndroiodScreenProperty();
                                                    int height = getAndroiodScreenPropertyH();
                                                    params.width = (int) (width * 0.65);
                                                    params.height = (int) (height * 0.35);
                                                    dialog.getWindow().setAttributes(params);

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                                            String code = tunnel_code.substring(1);
//                                                            String first = tunnel_code.substring(0, 1);
//                                                            String code = tunnel_code;
                                                            String first = "1";
                                                            String tunnel_code1 = tunnel_code;
                                                            Log.d(TAG, "nidayede : 货道号" + tunnel_code1 + "+值：" + first);
//                                                            TcnUtility.getToast(getContext(), "货道号是" + tunnel_code);
                                                            gLogger.debug("局部货道号是" + tunnel_code1);
//                                                            int tunnel_code = Integer.parseInt(tunnel_code);//除第一位剩下的数字
                                                            int firstNum = Integer.parseInt(first);//货道号第一位数字
                                                            String serialPort2 = null;
                                                            //判断firstNum 是几，调对应的串口值  1：serialport2
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
                                                                zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring2, getContext());

                                                                String getId = "0101000000000000000000000000000000007188";
                                                                String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
//                                                            String chekChuHuo = zhongjiAisleSerial.checkChuHuo(tunnel_code);
                                                                Log.d(TAG, "onViewClicked: " + chekChuHuo);
//                                                                TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekChuHuo);
                                                                if (chekChuHuo != null && !chekChuHuo.equals("")) {
                                                                    String check = "010300000000000000000000000000000000D0E8";
                                                                    String query = zhongjiAisleSerial.checkChuHuo1(check);//获取马达旋转结果
                                                                    gLogger.debug("电机状态:" + query);
                                                                    if(query == null || query.equals("")){
                                                                        tv.setText("设备异常，出货失败");
                                                                        tv1.setText("系统将为您自动退款");
                                                                        iv.setImageResource(R.drawable.fail);
                                                                        play(0);//提示音
                                                                        gLogger.debug("出货失败");
                                                                        order_status = -1;
                                                                        trade_status = 1;
                                                                        refund_status = 1;
                                                                        deliver_status = 0;
                                                                        orderCancle();
                                                                        return;
                                                                    }
                                                                    String hexString = Utils.integerToHexString(Integer.parseInt(tunnel_code1)-1);
                                                                    String data = "0105" + hexString + "03" + "00" + "00000000000000000000000000";//启动电机  03：三线电机  00：无光幕 aisleNum:货道号（电机号）
                                                                    byte[] bytes = Utils.hexStr2Byte(data);
                                                                    int crc16Check = Utils.CRC16_Check(bytes, bytes.length);
                                                                    String crc = Utils.integerToHexString(crc16Check);
                                                                    String crc1 = null;
                                                                    String crc2 = null;
                                                                    if (crc.length() == 4) {
                                                                        crc1 = crc.substring(0, 2);
                                                                        crc2 = crc.substring(2, 4);
                                                                    } else {
                                                                        crc1 = "00";
                                                                        crc2 = crc.substring(0, 2);
                                                                    }
                                                                    String get = data + crc2 + crc1;
//                                                                    TcnUtility.getToast(getActivity(), "启动电机指令：" + get);
                                                                    gLogger.debug("启动电机指令：" + get);
                                                                    String chekMachine = zhongjiAisleSerial.checkChuHuo1(get);//启动马达
                                                                    String s = chekMachine.substring(4, 6);
                                                                    if (s.equals("00")) {
                                                                        gLogger.debug("电机已启动");
//                                                                        TcnUtility.getToast(getActivity(), "已启动");
                                                                        //持续查询电机状态
                                                                        check(tv, tv1, iv);
                                                                    } else if (s.equals("01")) {
                                                                        gLogger.debug("电机未启动");
//                                                                        TcnUtility.getToast(getActivity(), "无效的电机索引号");
                                                                        zhongjiAisleSerial.closeSerialPort();
                                                                        tv.setText("设备异常，出货失败");
                                                                        tv1.setText("系统将为您自动退款");
                                                                        iv.setImageResource(R.drawable.fail);
                                                                        play(0);//提示音
                                                                        gLogger.debug("出货失败");
                                                                        order_status = -1;
                                                                        trade_status = 1;
                                                                        refund_status = 1;
                                                                        deliver_status = 0;
                                                                        orderCancle();
                                                                    } else if(s.equals("02")){
                                                                        gLogger.debug("另一台电机在运行");
//                                                                        TcnUtility.getToast(getActivity(), "另一台电机在运行");
                                                                        zhongjiAisleSerial.closeSerialPort();
                                                                        tv.setText("设备异常，出货失败");
                                                                        tv1.setText("系统将为您自动退款");
                                                                        iv.setImageResource(R.drawable.fail);
                                                                        play(0);//提示音
                                                                        gLogger.debug("出货失败");
                                                                        order_status = -1;
                                                                        trade_status = 1;
                                                                        refund_status = 1;
                                                                        deliver_status = 0;
                                                                        orderCancle();
                                                                    }
                                                                } else {
                                                                    tv.setText("设备异常，出货失败");
                                                                    tv1.setText("系统将为您自动退款");
                                                                    iv.setImageResource(R.drawable.fail);
                                                                    play(0);//提示音
                                                                    gLogger.debug("出货失败");
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
                                                                addData();
                                                                ll_shop.setVisibility(View.VISIBLE);
                                                                ll_pay.setVisibility(View.GONE);
                                                                final Handler handler = new Handler();
                                                                runnableState = new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        handler.postDelayed(this, 1000);
                                                                        final String state = tv.getText().toString();
                                                                        if (state.equals("出货成功，请取货") || state.equals("设备异常，出货失败")) {
                                                                            Log.d(TAG, "状态: +" + state);
                                                                            handler.removeCallbacks(runnableState);
                                                                            dialog.dismiss();
//                                                                            timer.schedule(new TimerTask() {
//                                                                                @Override
//                                                                                public void run() {
//                                                                                    mActivity.runOnUiThread(new Runnable() {      // UI thread
//                                                                                        @Override
//                                                                                        public void run() {
//                                                                                            duration--;
//                                                                                            Log.d(TAG, "run: " + duration + "s");
//                                                                                            if (duration < 2) {
//                                                                                                timer.cancel();
//                                                                                                dialog.dismiss();
//                                                                                            }
//                                                                                        }
//                                                                                    });
//                                                                                }
//                                                                            }, 1000, 1000);
                                                                            return;
                                                                        }
                                                                    }
                                                                };
                                                                handler.postDelayed(runnableState, 1000);//线程时间1秒刷新

                                                                try {
                                                                    Thread.sleep(5000);
                                                                    Log.d(TAG, "run: +++++++++++++++=");
//                                                                    dialog.dismiss();
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                TcnUtility.getToast(getActivity(), "请先设置货道串口");
                                                            }
//                                                        String devices2 = spf.getString("devices2", "");
//                                                        String baudrates2_1 = spf.getString("baudrates2", "");
//                                                        Log.d(TAG, "onViewClicked2: " + devices2 + "++++++++++++++" + baudrates2_1);
//                                                        if (devices2 != null && !devices2.equals("") && !baudrates2_1.equals("")) {
//                                                            int baudrates2 = Integer.parseInt(baudrates2_1);
//                                                            SerialPortController.getInstance().openSerialPort(devices2, baudrates2);
//                                                            String code = tunnel_code.substring(1);
//                                                            String first = tunnel_code.substring(0, 1);
//                                                            int tunnel_code = Integer.parseInt(code);//除第一位剩下的数字
//                                                            int firstNum = Integer.parseInt(first);//货道号第一位数字
//                                                            byte[] bytes = Utils.get(tunnel_code, firstNum);
//                                                            String chuHuo = SerialPortController.getInstance().writeDataI(bytes);
//                                                            Log.d(TAG, "onViewClicked: " + chuHuo);
//                                                            TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chuHuo);
//                                                            if (chuHuo != null && !chuHuo.equals("")) {
//                                                                String substring = chuHuo.substring(6, 8);
//                                                                if (substring.equals("AA")) {
//                                                                    tv.setText("出货成功，请取货");
//                                                                    tv1.setText("");
//                                                                    iv.setImageResource(R.drawable.chenggong);
//                                                                    chuHuoConfirm();
//                                                                } else {
//                                                                    tv.setText("设备异常，出货失败");
//                                                                    tv1.setText("系统将为您自动退款");
//                                                                    iv.setImageResource(R.drawable.fail);
//                                                                    orderCancle();
//                                                                }
//                                                            } else {
//                                                                tv.setText("设备异常，出货失败");
//                                                                tv1.setText("系统将为您自动退款");
//                                                                iv.setImageResource(R.drawable.fail);
//                                                                orderCancle();
//                                                            }
//                                                            SerialPortController.getInstance().closeSerialPort();
//                                                        } else {
//                                                            TcnUtility.getToast(getActivity(), "请先设置货道串口");
//
                                                        }
                                                    }, 1500);//线程时间1.5秒;
                                                }
                                            }
                                        }
                                    }
                                } else if (order_status == -1) {
                                    handler.removeCallbacks(runnable);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                } else {
//                    handler.removeCallbacks(runnable);
////                  dialog.dismiss();
////                  orderCancle();
//                    TcnUtility.getToast(getActivity(), "查询订单请求超时");
//                }
                Message message = new Message();
                message.what = -1;
                handler.sendMessage(message);
            }

        };
        handler.postDelayed(runnable, 3000);//线程时间1秒刷新
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            mActivity.runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
                    if (duration < 2) {
                        timer.cancel();

                    }
                }
            });

        }
    };

    private MediaPlayer mediaPlayer;

    // 初始化MediaPlayer 提示音
    private void play(int result) {
        try {
            if (result == 0) {//0：出货失败 1：出货成功 2：购买成功 3：支付方式
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.deliverfail);
            } else if (result == 1) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.deliversuccess);
            } else if (result == 2) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.buysuccess);
            } else if (result == 3) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.payway);
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

    DbManager db;

    //本地数据库添加出货数据
    private void addData() {
        try {
            //根据配置信息获取操作数据的db对象
            db = x.getDb(daoConfig);
            Order order = new Order();
            order.setOrder_status(order_status);
            order.setTrade_status(trade_status);
            order.setRefund_status(refund_status);
            order.setDeliver_status(deliver_status);
            order.setShopName(shopName);
            order.setTunnel_code(tunnel_code);
            order.setShopPrice(shopPrice);
            db.save(order);//插入一条数据

            //查询所有数据
            List<Order> all = db.findAll(Order.class);
            Log.i("tag", "所有数据:" + all.toString());
        } catch (DbException e) {
            e.printStackTrace();
            try {
                db.dropTable(Order.class);//删除表
                addData();
            } catch (DbException e1) {
                e1.printStackTrace();
            }
        }
    }

    //本地数据库添加商品数据
    DbManager db1;
    private Runnable runnableSelect;
    //持续查询电机状态
    private void check(final TextView tv, final TextView tv1, final ImageView iv) {
        final Handler handler = new Handler();
        //1秒后发送获取电机状态
        runnableSelect = new Runnable() {
            @Override
            public void run() {
                gLogger.debug("走进了查询循环");
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000);
                String check = "010300000000000000000000000000000000D0E8";
                String query = zhongjiAisleSerial.checkChuHuo1(check);//获取马达旋转结果
                gLogger.debug("电机状态:" + query);
                if (query != null && !query.equals("")) {
                    String machineState = query.substring(4, 6);
                    if (machineState.equals("02")) {
                        handler.removeCallbacks(runnableSelect);
                        String result = query.substring(8, 10);
                        if (result.equals("00")) {
                            tv.setText("出货成功，请取货");
                            tv1.setText("");
                            iv.setImageResource(R.drawable.chenggong);
                            play(1);//提示音
                            gLogger.debug("出货成功");
                            order_status = 1;
                            trade_status = 1;
                            refund_status = 0;
                            deliver_status = 1;
                            chuHuoConfirm();
                        } else {
                            tv.setText("设备异常，出货失败");
                            tv1.setText("系统将为您自动退款");
                            iv.setImageResource(R.drawable.fail);
                            play(0);//提示音
                            gLogger.debug("出货失败");
                            order_status = -1;
                            trade_status = 1;
                            refund_status = 1;
                            deliver_status = 0;
                            orderCancle();
                        }
                        zhongjiAisleSerial.closeSerialPort();

                    } else if (machineState.equals("01")) {
//                        TcnUtility.getToast(getActivity(), "出货中");
                        tv.setText("出货中，请稍等");
                        tv1.setText("");
                        iv.setImageResource(R.drawable.zhifuchenggong_jiazai);
                    }

                }
            }
        };
        handler.postDelayed(runnableSelect, 1000);//线程时间1秒刷新
    }

    @Override
    public void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        // 将myTouchListener注册到分发列表
        ((MainActivity) mActivity).registerMyTouchListener(myTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
//        cancle = true;
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(runnable1);
        /*
        * * 触摸事件的注销 */
        ((MainActivity) mActivity).unRegisterMyTouchListener(myTouchListener);
        Log.d(TAG, "onPause: ++++++++++++");
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    //返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            int style1 = spf.getInt("style1", 0);//获取待机页的样式
            if (style1 == 0) {
                style1 = 1;
            }
            handler1.removeCallbacks(runnable1);
            /*
             * * 触摸事件的注销 */
            ((MainActivity) mActivity).unRegisterMyTouchListener(myTouchListener);
            Intent intent = new Intent(mActivity, InitActivity.class);
            intent.putExtra("styleDaiJi", style1);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
        return true;
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

//        cancle = true;
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(runnable1);
        ButterKnife.reset(this);
        super.onDestroyView();
        /*
        * * 触摸事件的注销 */
        ((MainActivity) mActivity).unRegisterMyTouchListener(myTouchListener);
        Log.d(TAG, "onDestroyView: +++++");
    }

    private OutDialog m_OutDialog = null;
    private LoadingDialog m_LoadingDialog = null;
    /*
     * 此处监听底层发过来的数据，下面是显示相应操作结果
	 */
    private static int m_iEventID;

    private VendListener m_vendListener = new VendListener();

    public class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            if (null == cEventInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
                return;
            }
            Log.d(TAG, "VendEvent: " + cEventInfo.m_iEventID);
            m_iEventID = cEventInfo.m_iEventID;
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
                            m_OutDialog = new OutDialog(getActivity(), String.valueOf(cEventInfo.m_lParam1), getString(R.string.ui_base_notify_shipping1));
                        } else {
                            m_OutDialog.setText(getActivity().getString(R.string.ui_base_notify_shipping1));
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
                    play(1);
                    order_status = 1;
                    trade_status = 1;
                    refund_status = 0;
                    deliver_status = 1;
                    chuHuoConfirm();
                    addData();
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
                    play(0);
                    order_status = -1;
                    trade_status = 1;
                    refund_status = 1;
                    deliver_status = 0;
                    orderCancle();
                    addData();
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

    //订单提交并获取商品支付的二维码等数据
    int i = 0;
    private String pay;
    private ImageView iv;
    int q;
    int ceshi;

    private void getData(final String payment, final ImageView iv_erweima) {
        pay = payment;
        iv = iv_erweima;
        String url = Comment.URL + "/order/create";
        String terminal_no = spf.getString("terminal_no", "");
        Log.d(TAG, "getData: " + key);
//        TcnUtility.getToast(getActivity(),"秘钥的值："+key);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("terminal_order_no", terminal_no + "-" + str);//00010001：terminal_no设备定义的终端编号
        params.put("good_no", goods_no);//商品编号
        params.put("payment", payment);//支付方式
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("good_no=" + goods_no + "&payment=" + payment + "&terminal_code=" + Comment.terminal_code + "&terminal_order_no=" + terminal_no + "-" + str + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        Log.d(TAG, "getData1: " + params);
        if (q < 1) {
            OkHttpUtils.post().url(url).params(params).build().connTimeOut(2000).execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", "getData:失败" + e);
                    i = 0;
//                    initSignin(3);
                    code = 0;//获取失败
                    getData(payment, iv_erweima);
                    if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                        return;
                    }
                    GlideUtils.load(getActivity(), "", iv_erweima, R.drawable.fail1);
                    ib_return.setEnabled(true);
                    q++;
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("TAG", "getData:成功" + response);
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
                            ib_return.setEnabled(true);
                        } else {
                            code = 1;//获取成功
                            JSONObject data = jsonObject.getJSONObject("data");
                            //订单生成的时候返回的
                            order_no = data.getString("order_no");
                            JSONObject pay_info = data.getJSONObject("pay_info");
                            String code_img_url = pay_info.getString("code_img_url");//二维码图片
                            Log.d(TAG, "onResponse: " + code_img_url);
                            if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                                return;
                            }
                            GlideUtils.load(getActivity(), code_img_url, iv_erweima);
                            ib_return.setEnabled(true);
                            a = 0;
                            cancle = false;
                            sockerStar();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//          orderCancle();
            TcnUtility.getToast(getActivity(), "二维码获取请求超时");
        }
    }

    @OnClick(R.id.ll_xianJin)
    public void onViewClicked(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.shop_pay);
        TextView tv_a = dialog.findViewById(R.id.tv_a);
        TextView tv_fangshi = dialog.findViewById(R.id.tv_fangshi);
        TextView tv_b = dialog.findViewById(R.id.tv_b);
        ImageView iv_erweima = dialog.findViewById(R.id.iv_erweima);//二维码
        ImageButton ib_return = dialog.findViewById(R.id.ib_return);

        ImageView iv_img = dialog.findViewById(R.id.iv_img);//商品图
        TextView tv_prive = dialog.findViewById(R.id.tv_prive);//商品价格
        TextView tv_name = dialog.findViewById(R.id.tv_name);//商品名
        tv_prive.setText("￥" + shopPrice);
        tv_name.setText(shopName);
        GlideUtils.load(getActivity(), ImgUrl, iv_img);
        LinearLayout ll_background = dialog.findViewById(R.id.ll_background);//背景
        View v = dialog.findViewById(R.id.v);

        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.height = 800 ;
        int width = getAndroiodScreenProperty();
        params.width = (int) (width * 0.9);
        dialog.getWindow().setAttributes(params);
        switch (view.getId()) {
            case R.id.ll_xianJin:
                tv_a.setText("等待您的支付");
                tv_fangshi.setText("请投入现金");
                if (isAdded()) {
                    tv_a.setTextColor(getResources().getColor(R.color.black));
                    tv_fangshi.setTextColor(getResources().getColor(R.color.black));
                    ll_background.setBackgroundColor(getResources().getColor(R.color.white));
                }
                tv_b.setVisibility(View.GONE);
                iv_erweima.setVisibility(View.GONE);
                v.setVisibility(View.VISIBLE);
                ib_return.setImageResource(R.drawable.btn_zhifu_fanhui1);
                break;
        }
    }

    @OnClick(R.id.ll_kaquan)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), CouponPickingActivity.class);
        intent.putExtra("goods_no", goods_no);
        intent.putExtra("tunnel_no", tunnel_no);
        intent.putExtra("tunnel_code", tunnel_code);
        intent.putExtra("price", shopPrice);
        startActivity(intent);
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

    //初始化Hodler
    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_huodaoNum = null;
        public TextView tv_soldOut = null;
        public TextView tv_name = null;
        public TextView tv_price = null;
        public ImageView iv_img = null;
        public Button btn_detail = null;
        public Button btn_buy = null;

        public MyHolder(View itemView) {
            super(itemView);
            tv_huodaoNum = (TextView) itemView.findViewById(R.id.tv_huodaoNum);//货道号
            tv_soldOut = (TextView) itemView.findViewById(R.id.tv_soldOut);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            btn_detail = (Button) itemView.findViewById(R.id.btn_detail);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        }
    }

    //初始化部分id
    private void init(View view) {
        ll_pay = view.findViewById(R.id.ll_pay);
        ll_shop = view.findViewById(R.id.ll_shop);
        btn_return = view.findViewById(R.id.btn_return);
        mRecyclerView = (PageRecyclerView) view.findViewById(R.id.cusom_swipe_view);
        // 设置指示器
        PageIndicatorView indicator = (PageIndicatorView) view.findViewById(R.id.indicator);
        mRecyclerView.setIndicator(indicator);
        // 设置行数和列数
        int width = getAndroiodScreenProperty();
        int height = getAndroiodScreenPropertyH();
        if (width < height) {
            mRecyclerView.setPageSize(4, 5);
        } else {
            mRecyclerView.setPageSize(2, 6);
        }
        // 设置页间距
        mRecyclerView.setPageMargin(30);
    }

    //初始化控件状态
    public void initState() {
        i = 0;
        if (getActivity().isDestroyed() || getActivity().isFinishing()) {
            return;
        }
        key = Comment.KEY;
        Log.d(TAG, "initState: " + key);
//        TcnUtility.getToast(getActivity(),"秘钥的值："+key);
        timeStamp = getTimeStamp();
//        initSignin(-1);
        ll_shop.setVisibility(View.VISIBLE);
        ll_pay.setVisibility(View.GONE);
        initProfile();
    }

    /**
     * 初始化获取数据库的配置信息
     */
    public void initDaoConfig() {
        Log.d(TAG, "initDaoConfig: " + getContext().getCacheDir().getAbsoluteFile());
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
        //暂时用不到了，以后可能会用到
//        daoConfig1 = new DbManager.DaoConfig()
//                .setDbName("goods.db")  //设置数据库名称
//                .setDbVersion(1)  //设置数据库版本
//                .setDbDir(null) //设置数据库保存的路径getContext().getCacheDir().getAbsoluteFile()
//                .setAllowTransaction(true) //设置允许开启事务
//                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                    @Override
//                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                        //db.addColumn("表名","需要增加的列名");数据库更新监听
//                    }
//                })
//                .setDbOpenListener(new DbManager.DbOpenListener() {
//                    @Override
//                    public void onDbOpened(DbManager db) {
//                        //开启WAL.对写入加速提示很大
//                        db.getDatabase().enableWriteAheadLogging();
//                    }
//                })
//                .setTableCreateListener(new DbManager.TableCreateListener() {
//                    @Override
//                    public void onTableCreated(DbManager db, TableEntity<?> table) {
//                        Log.i("tag", "onTableCreated: 创建了表:" + table.getName());
//                    }
//                });
    }

    //终端签到
    //    String param = "terminal_code=8986001200ACBDA01234" + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
    //    Log.d("TAG","initData: "+param);
    private void initSignin(final int flag) {
        String url = Comment.URL + "/terminal/signin";
        timeStamp = getTimeStamp();
        //auth_sign MD5加密
        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        params.put("auth_sign", ciphertext);//签名
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "initSignin:失败" + i + e);
                Log.d("TAG", "initSignin:失败" + params);
                code = 0;//获取失败
                if (ib_return != null && iv != null) {
                    if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                        return;
                    }
                    GlideUtils.load(getActivity(), "", iv, R.drawable.fail1);
                    ib_return.setEnabled(true);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "initSignin:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    key = jsonObject.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag == 0) {
//                    detail(goods_no, ((MyHolder) holder).btn_detail);
                } else if (flag == 1) {
                    a = 0;
                    sockerStar();
                } else if (flag == 2) {
                    orderCancle();
                } else if (flag == 3) {
                    q = 0;
                    getData(pay, iv);
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
                initProfile();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponseP:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
//                        TcnUtility.getToast(getActivity(), "获取失败");
                        return;
                    }
//                    TcnUtility.getToast(getActivity(), "获取成功");
//                    Log.d(TAG, "onRe: " + jsonObject.getString("data"));
                    jsonObject = jsonObject.getJSONObject("data");
                    String tunnel = jsonObject.getString("tunnel");//商品列表
                    editor.putString("tunnel", tunnel);
                    editor.commit();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 设置数据
                            initData();
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                    }).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
