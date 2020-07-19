package com.example.myapplication.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.dwin.navy.serialportapi.com_zhongji_aisle_test;
import com.example.myapp.R;
import com.example.myapplication.CouponPickingActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.myapp.R.id.tv_address;


/**
 * A simple {@link Fragment} subclass.
 * 这是日用百货页
 */
public class DailyAndDepartmentFragment extends Fragment {
    private static final String TAG = "DailyAndDepartment";
    @InjectView(R.id.ll_xianJin)
    LinearLayout ll_xianJin;
    @InjectView(R.id.ll_kaquan)
    LinearLayout llKaquan;
    @InjectView(R.id.gv)
    MyGridView gv;

    private PageRecyclerView mRecyclerView = null;
    private List<DrinkBean> dataList = null;
    private List<SettingBean> list = null;
    private PageRecyclerView.PageAdapter myAdapter = null;
    private PayAdapter adapter = null;
    private LinearLayout ll_shop, ll_pay;
    private Button btn_return;
    private SharedPreferences spf;
    private String shopName;//商品名称
    private String shopPrice;//商品价格
    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig;
    private String goods_no;//商品编号
    private String ImgUrl;//商图片
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
    private String key;
    private String tunnel_no;//货道编号
    private String tunnel_code;//货道号
    private long timeStamp;
    private com_zhongji_aisle_test zhongjiAisleSerial;
    private int order_status;//订单状态
    private int trade_status;//支付状态
    private int refund_status;//退款状态
    private int deliver_status;//出货状态
    private String channel_code;//支付方式

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_and_department, container, false);
        ButterKnife.inject(this, view);
        //初始化数据库配置信息
        initDaoConfig();
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        init(view);
        initData();
        setData();
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_shop.setVisibility(View.VISIBLE);
                ll_pay.setVisibility(View.GONE);
            }
        });
        boolean showXianJin = spf.getBoolean("showXianJin", false);
        Log.d(TAG, "onCreateViewDaily: " + showXianJin);
        if (showXianJin) {
            ll_xianJin.setVisibility(View.VISIBLE);
        } else {
            ll_xianJin.setVisibility(View.GONE);
        }

        return view;
    }

    //获取支付方式
    private void initPayData() {
//        String name[] = {"微信","支付宝","银联"};
        int img[] = {R.drawable.weixin, R.drawable.zhifubao, R.drawable.yinlian};
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
                bean.setImgUrl(Comment.URL_IMG_OR_VIDEO+"/image/" + logo_c);
                bean.setImgPay(channel_code);
                boolean showWeiXin = spf.getBoolean("showWeiXin", false);
                boolean showAlipay = spf.getBoolean("showAlipay", false);
                boolean showUnionpay = spf.getBoolean("showUnionpay", false);
                if (name.equals("微信支付") && showWeiXin) {
                    list.add(bean);
                }
                if (name.equals("支付宝") && showAlipay) {
                    list.add(bean);
                }
                if (name.equals("银联二维码") && showUnionpay) {
                    list.add(bean);
                }
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
                    dialog = new Dialog(getActivity());
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
                            orderCancle();
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
                    initSignin(-1);
                    switch (shopName) {
                        case "微信支付":
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("微信");
                            tv_b.setText("扫码支付");
                            tv_a.setTextColor(getResources().getColor(R.color.white));
                            tv_fangshi.setTextColor(getResources().getColor(R.color.white));
                            tv_b.setTextColor(getResources().getColor(R.color.white));
                            iv_erweima.setImageResource(R.drawable.loading);
                            v.setVisibility(View.GONE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui2);
                            ll_background.setBackgroundColor(getResources().getColor(R.color.color1));
                            break;
                        case "支付宝":
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("支付宝");
                            tv_b.setText("扫码支付");
                            tv_a.setTextColor(getResources().getColor(R.color.white));
                            tv_fangshi.setTextColor(getResources().getColor(R.color.white));
                            tv_b.setTextColor(getResources().getColor(R.color.white));
                            iv_erweima.setImageResource(R.drawable.loading);
                            v.setVisibility(View.GONE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui2);
                            ll_background.setBackgroundColor(getResources().getColor(R.color.color2));
                            break;
                        case "银联二维码":
                            getData(channel_code, iv_erweima);
                            tv_a.setText("请使用");
                            tv_fangshi.setText("银行卡");
                            tv_b.setText("刷卡支付");
                            tv_a.setTextColor(getResources().getColor(R.color.black));
                            tv_fangshi.setTextColor(getResources().getColor(R.color.black));
                            tv_b.setTextColor(getResources().getColor(R.color.black));
                            iv_erweima.setImageResource(R.drawable.image_zhifu_yinlian);
                            v.setVisibility(View.VISIBLE);
                            tv_b.setVisibility(View.VISIBLE);
                            iv_erweima.setVisibility(View.VISIBLE);
                            ib_return.setImageResource(R.drawable.btn_zhifu_fanhui1);
                            ll_background.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                    }
                }
            });
        }
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
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code="+Comment.terminal_code+"&timestamp="+timeStamp + "&key=" + key);
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
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        initSignin(2);
                        handler.removeCallbacks(runnable);
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                    dialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //出货确认
    private void chuHuoConfirm() {
        Log.d(TAG, "orderCancle: " + key);
        //订单取消
        String url = Comment.URL+"/order/deliver";
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("order_no", order_no);//订单生成的时候返回的
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code="+Comment.terminal_code+"&timestamp="+timeStamp + "&tunnel_no=" + tunnel_no + "&key=" + key);
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

    //获取商品数据
    private void initData() {
        dataList = new ArrayList<>();
        String text[] = {"255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶"};
        String tunnel = spf.getString("tunnel", "");
        try {
            JSONArray jsonArray = new JSONArray(tunnel);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String tunnel_no = object.getString("tunnel_no");
                String tunnel_code = object.getString("tunnel_code");
                if (object.getString("good_no") != null && !object.getString("good_no").equals("")) {
                    JSONObject product = object.getJSONObject("product");
                    String category = product.getString("category");
                    if (category.equals("daily")) {
                        String name_show = product.getString("name_show");//商品展示名
                        String pic = product.getString("pic");//商品展示图
                        JSONObject good = object.getJSONObject("good");
                        int price = good.getInt("price");//商品价格
                        String good_no = good.getString("good_no");//商品编号
                        DrinkBean bean = new DrinkBean();
                        bean.setName(name_show);
                        bean.setImgUrl(Comment.URL_IMG_OR_VIDEO+"/image/" + pic);
                        bean.setPrice(price+"");
                        bean.setGoodsNum(good_no);
                        bean.setTunnel_no(tunnel_no);
                        bean.setTunnel_code(tunnel_code);
                        dataList.add(bean);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < text.length; i++) {
//            DrinkBean bean = new DrinkBean();
//            bean.setName(text[i] + i);
////            bean.setImgUrl(img[i]);
//            bean.setPrice(10.00 + i);
//            dataList.add(bean);
//        }
    }

    //设置商品数据
    private void setData() {
        Log.d("TAG", "set: " + dataList);
        if (dataList!=null&& dataList.size() != 0) {
            myAdapter = mRecyclerView.new PageAdapter(dataList, new PageRecyclerView.CallBack() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_drink, parent, false);
                    return new MyHolder(view);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    final DrinkBean drinkBean = dataList.get(position);
                    ((MyHolder) holder).tv_name.setText(drinkBean.getName());
                    ((MyHolder) holder).tv_price.setText("￥" + drinkBean.getPrice());
                    GlideUtils.load(getActivity(), drinkBean.getImgUrl(), ((MyHolder) holder).iv_img);
                    String tunnel_code = drinkBean.getTunnel_code();
                    if(tunnel_code.substring(0,1).equals("1")){
                        ((MyHolder) holder).tv_huodaoNum.setText(tunnel_code.substring(1));
                    }else{
                        ((MyHolder) holder).tv_huodaoNum.setText(tunnel_code);
                    }
                    //商品详情
                    ((MyHolder) holder).btn_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String goods_no = drinkBean.getGoodsNum();
                            String url = Comment.URL+"/good/detail";
    //                        String key = spf.getString("key", "");
                            Log.d(TAG, "getData: " + key);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String str = formatter.format(curDate);
                            Map<String, String> params = new HashMap<>();
                            params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                            params.put("good_no", goods_no);//商品编号
                            params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                            String signature = MD5Util.encrypt("good_no=" + goods_no + "&terminal_code="+Comment.terminal_code+"&timestamp="+timeStamp + "&key=" + key);
                            params.put("signature", signature);//签名参数
                            Log.d(TAG, "getData: " + params);
                            OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("TAG", "onResponse:失败" + e);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("TAG", "onResponse:成功" + response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        jsonObject = jsonObject.getJSONObject("data");
                                        jsonObject = jsonObject.getJSONObject("product");
                                        String name_show = jsonObject.getString("name_show");//商品名称
                                        String brand = jsonObject.getString("brand");//商品品牌
                                        final Dialog dialog = new Dialog(getActivity());
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.shop_detail);
                                        TextView tv_shopName = dialog.findViewById(R.id.tv_shopName);//商品名称
                                        TextView tv_brand = dialog.findViewById(R.id.tv_brand);//商品品牌
                                        tv_shopName.setText(name_show);
                                        tv_brand.setText(brand);
                                        dialog.show();
                                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                                        int width = getAndroiodScreenProperty();
                                        params.width = (int) (width * 0.85);
                                        dialog.getWindow().setAttributes(params);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

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
                            DailyAndDepartmentFragment.this.tunnel_code = drinkBean.getTunnel_code();
                            Log.d(TAG, "onClick: " + shopName);
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
                            DailyAndDepartmentFragment.this.tunnel_code = drinkBean.getTunnel_code();
                            Log.d(TAG, "onClick: " + shopName);
                            //获取支付方式
                            initPayData();
                        }
                    });
                }

                @Override
                public void onItemClickListener(View view, int position) {
                    Toast.makeText(getActivity(), "点击："
                            + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClickListener(View view, int position) {

                }
            });
            mRecyclerView.setAdapter(myAdapter);
        }
    }

    //持续向后台发送订单查询
    private void sockerStar() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 3);
                Log.d(TAG, "run: ------------------------>");
                String url = Comment.URL+"/order/query";
//                String key = spf.getString("key", "");
                Map<String, String> params = new HashMap<>();
                params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
                params.put("order_no", order_no);//订单生成的时候返回的
                params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
                String signature = MD5Util.encrypt("order_no=" + order_no + "&terminal_code="+Comment.terminal_code+"&timestamp="+timeStamp + "&key=" + key);
                params.put("signature", signature);//签名参数
                OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("TAG", "onResponse:失败" + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("TAG", "onResponse:成功S" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean code = jsonObject.getBoolean("code");
                            if (!code) {
                                initSignin(1);
                                handler.removeCallbacks(runnable);
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
                                                dialog.cancel();
                                                int model_id = Integer.parseInt(spf.getString("model_id", ""));//1：中吉 2：点为
                                                if (model_id == 1) {
                                                    String code1 = tunnel_code.substring(1);
                                                    int slotNo = Integer.parseInt(code1);//出货的货道号
                                                    String shipMethod = channel_code; //出货方法,微信支付出货，此处自己可以修改。
                                                    String amount = String.valueOf(shopPrice);    //支付的金额（元）,自己修改
                                                    String tradeNo = order_no;//支付订单号，每次出货，订单号不能一样，此处自己修改。
                                                    TcnVendIF.getInstance().reqShip(slotNo,shipMethod,amount,tradeNo);
                                                }else if (model_id == 2){
                                                    final Dialog dialog = new Dialog(getActivity());
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setContentView(R.layout.pay_and_chu_huo);
                                                    final TextView tv = dialog.findViewById(R.id.tv_tu);
                                                    final TextView tv1 = dialog.findViewById(R.id.tv_tu1);
                                                    final ImageView iv = dialog.findViewById(R.id.iv_tu);
                                                    tv.setText("支付成功，等待商品出货");
                                                    tv1.setText("");
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
                                                            String code = tunnel_code.substring(1);
                                                            String first = tunnel_code.substring(0, 1);
                                                            int tunnel_code = Integer.parseInt(code);//除第一位剩下的数字
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
                                                                zhongjiAisleSerial = com_zhongji_aisle_test.getInstance(substring2,getContext());
                                                                String getId = "0101000000000000000000000000000000007188";
                                                                String chekChuHuo = zhongjiAisleSerial.checkChuHuo1(getId);//获取id
//                                                            String chekChuHuo = zhongjiAisleSerial.checkChuHuo(tunnel_code);
                                                                Log.d(TAG, "onViewClicked: " + chekChuHuo);
                                                                TcnUtility.getToast(getActivity(), "出货返回的数据是：" + chekChuHuo);
                                                                if (chekChuHuo != null && !chekChuHuo.equals("")) {
                                                                    String data = "0105"+code+"03"+"00"+"00000000000000000000000000";//启动电机  03：三线电机  00：无光幕 aisleNum:货道号（电机号）
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
                                                                    order_status = -1;
                                                                    trade_status = 1;
                                                                    refund_status = 1;
                                                                    deliver_status = 0;
                                                                    orderCancle();
                                                                }
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
                                                                    Log.i("tag", "所有数据:"+all.toString());
                                                                } catch (DbException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                ll_shop.setVisibility(View.VISIBLE);
                                                                ll_pay.setVisibility(View.GONE);
                                                            } else {
                                                                TcnUtility.getToast(getActivity(), "请先设置货道串口");
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
        handler.postDelayed(runnable, 3000);//线程时间1秒刷新
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
                            order_status = 1;
                            trade_status = 1;
                            refund_status = 0;
                            deliver_status = 1;
                            chuHuoConfirm();
                        } else {
                            tv.setText("设备异常，出货失败");
                            tv1.setText("系统将为您自动退款");
                            iv.setImageResource(R.drawable.fail);
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
    //获取商品支付的二维码等数据
    private void getData(final String payment, final ImageView iv_erweima) {
        String url = Comment.URL+"/order/create";
        String terminal_no = spf.getString("terminal_no", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("terminal_order_no", terminal_no + "-" + str);//00010001：terminal_no设备定义的终端编号
        params.put("good_no", goods_no);//商品编号
        params.put("payment", payment);//支付方式
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("good_no=" + goods_no + "&payment=" + payment + "&terminal_code="+Comment.terminal_code + "&terminal_order_no=" + terminal_no + "-" + str+"&timestamp="+timeStamp + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        initSignin(-1);
                        getData(payment, iv_erweima);
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    //订单生成的时候返回的
                    order_no = data.getString("order_no");
                    JSONObject pay_info = data.getJSONObject("pay_info");
                    String code_img_url = pay_info.getString("code_img_url");//二维码图片
                    Log.d(TAG, "onResponse: " + code_img_url);
                    GlideUtils.load(getActivity(), code_img_url, iv_erweima);
                    sockerStar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        handler.removeCallbacks(runnable);
        /*
        * * 触摸事件的注销 */
//        ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
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
                tv_a.setTextColor(getResources().getColor(R.color.black));
                tv_fangshi.setTextColor(getResources().getColor(R.color.black));
                tv_b.setVisibility(View.GONE);
                iv_erweima.setVisibility(View.GONE);
                v.setVisibility(View.VISIBLE);
                ib_return.setImageResource(R.drawable.btn_zhifu_fanhui1);
                ll_background.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @OnClick(R.id.ll_kaquan)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), CouponPickingActivity.class);
        intent.putExtra("goods_no", goods_no);
        intent.putExtra("tunnel_no", tunnel_no);
        intent.putExtra("tunnel_code", tunnel_code);
        startActivity(intent);
    }

    ///获取屏幕分辨率：宽
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
        public TextView tv_name = null;
        public TextView tv_price = null;
        public ImageView iv_img = null;
        public Button btn_detail = null;
        public Button btn_buy = null;

        public MyHolder(View itemView) {
            super(itemView);
            tv_huodaoNum = (TextView) itemView.findViewById(R.id.tv_huodaoNum);//货道号
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
        mRecyclerView.setPageSize(4, 5);
        // 设置页间距
        mRecyclerView.setPageMargin(30);
    }

    //初始化控件状态
    public void initState() {
        initSignin(-1);
        ll_shop.setVisibility(View.VISIBLE);
        ll_pay.setVisibility(View.GONE);
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
    private void initSignin(final int flag) {
        String url = Comment.URL+"/terminal/signin";
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
                    sockerStar();
                } else if (flag == 2) {
                    orderCancle();
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
