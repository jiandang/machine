package com.example.myapplication.fragment.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.adapter.BuHuoGridViewAdapter;
import com.example.myapplication.adapter.BuHuoListViewAdapter;
import com.example.myapplication.adapter.BuHuoListViewAdapter1;
import com.example.myapplication.bean.BuhuoBean;
import com.example.myapplication.bean.DrinkBean;
import com.example.myapplication.bean.GoodsManageBean;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.MyGridView;
import com.example.myapplication.util.MyListView;
import com.example.myapplication.util.NumberAddSubView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


/**
 * A simple {@link Fragment} subclass.
 * 这是补货操作页
 */
public class BuHuoFragment extends Fragment {

    public static final String TAG = "BuHuoFragment";
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
    @InjectView(R.id.num_control)
    NumberAddSubView numControl;
    @InjectView(R.id.btn_tianMan)
    Button btn_tianMan;
    @InjectView(R.id.num_control1)
    NumberAddSubView numControl1;
    @InjectView(R.id.btn_updateGoods)
    Button btn_updateGoods;
    @InjectView(R.id.ll_setGoods)
    LinearLayout ll_setGoods;
    @InjectView(R.id.btn_confirm)
    Button btn_confirm;//更换确认
    @InjectView(R.id.btn_full)
    Button btn_full;//一键补满
    @InjectView(R.id.btn_change)
    Button btn_change;//更换商品
    @InjectView(R.id.btn_Confirm)
    Button btn_Confirm;//补货确认
    @InjectView(R.id.ll_caoZuo)
    LinearLayout ll_caoZuo;
    @InjectView(R.id.ll_buHuo)
    LinearLayout ll_buHuo;
    //    @InjectView(R.id.tv_num)
//    TextView tv_num;
//    @InjectView(R.id.tv_goodsName)
//    TextView tv_goodsName;
//    @InjectView(R.id.tv_huoNum)
//    TextView tv_huoNum;
    @InjectView(R.id.listBuHuo)
    MyListView listviewBuHuo;
    private List<BuhuoBean> dataList;
    private List<DrinkBean> drinkBeanListA;//第一柜的
    private List<DrinkBean> drinkBeanListB;//第二柜的
    private List<DrinkBean> drinkBeanListC;//第三柜的
    private List<DrinkBean> drinkBeanListD;//第四柜的
    private List<DrinkBean> drinkBeanListE;//第五柜的
    private List<DrinkBean> drinkBeanListF;//第六柜的
    private List<GoodsManageBean> list, buHuoList, emptyList, unemptyList;
    private BuHuoGridViewAdapter adapter;
    private BuHuoListViewAdapter adapter1;
    private BuHuoListViewAdapter1 adapterBu;
    private int HuoNum = 0;
    private int j;//girdView item数量用
    private int stock;//货物数量
    private String shopName;//添加的商品的名称
    private SharedPreferences spf;
    private boolean isVisible = true;//是否点击了设置商品
    private int max;//货道最大存储
    private String key;
    private String tunnel_no;//货道编号
    private String good_no;//商品号
    private long timeStamp;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn0;
    private Button btnDel;
    private String barcode;//条形码
    private GoodsManageBean goodsManageBean;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bu_huo, container, false);
        ButterKnife.inject(this, view);
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        ll_caoZuo.setVisibility(View.GONE);
        ll_buHuo.setVisibility(View.GONE);
        btn_confirm.setEnabled(false);
        btn_confirm.setBackgroundResource(R.drawable.shape_cccccc);
        buHuoList = new ArrayList<>();
        //容量
        numControl1.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                numControl1.setValue(value);
                numControl.setMaxValue(numControl1.getValue() - stock);
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (value >= stock) {
                    numControl1.setValue(value);
                    numControl.setMaxValue(numControl1.getValue() - stock);
                    if (numControl.getValue() < (numControl1.getValue() - stock)) {
                        return;
                    } else {
                        numControl.setValue(numControl1.getValue() - stock);
                        HuoNum = numControl1.getValue() - stock;
                    }
                }
            }
        });
        //补货数量
        numControl.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                HuoNum = value;
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                HuoNum = value;
            }
        });
        //更换商品列表点击事件
        changeGoodsClick();
        //一键补满全部货道
        btn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setEnabled(false);
                btn_confirm.setBackgroundResource(R.drawable.shape_cccccc);
                btn_change.setEnabled(false);
                btn_change.setBackgroundResource(R.drawable.shape_cccccc);
                full();
            }
        });
        return view;
    }
    //一键补满所以货道
    private void full() {
        String url = Comment.URL + "/tunnel/full";
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        params.put("signature", signature);//签名
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                initSignin(7);
            }
            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(7);
                    } else {
                        TcnUtility.getToast(getActivity(), "所有货道商品补满成功");
                        initBuHuo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //更换商品列表点击事件
    private void changeGoodsClick() {
        listviewBuHuo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ll_buHuo.setVisibility(View.VISIBLE);
                adapterBu.setSeclection(position);
                adapterBu.notifyDataSetChanged();
                index = position;
                GoodsManageBean goodsManageBean = buHuoList.get(position);
                max = goodsManageBean.getMax();
                stock = goodsManageBean.getStock();
                tunnel_no = goodsManageBean.getTunnel_no();
                Log.d(TAG, "stock: " + stock);
                Log.d(TAG, "max: " + max);
                if(stock == max){
                    btn_tianMan.setBackgroundResource(R.drawable.shape_cccccc);
                }else{
                    btn_tianMan.setBackgroundResource(R.drawable.shape_76c5f0_1);
                }
                numControl(max, stock);
            }
        });
    }

    public void initBuHuo() {
        i = 0;
        initHuoGuiBackground();
        tv_a.setBackgroundColor(getResources().getColor(R.color.background1));
        tv_a.setTextColor(getResources().getColor(R.color.white));
        aisle();
        numControl(0, 0);
        ll_caoZuo.setVisibility(View.GONE);
        getData();
        buHuoList = new ArrayList<>();
    }

    //货柜初始化
    private void aisle() {
        tv_a.setVisibility(View.GONE);
        tv_b.setVisibility(View.GONE);
        tv_c.setVisibility(View.GONE);
        tv_d.setVisibility(View.GONE);
        tv_e.setVisibility(View.GONE);
        tv_f.setVisibility(View.GONE);
    }

    //条形码查商品
    private void barcode() {
        list = new ArrayList<>();
        String url = Comment.URL + "/good/list";
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("barcode", barcode);//条形码
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("barcode=" + barcode + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        Log.d("TAG", "initProfile: " + signature);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                initSignin(5);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(5);
                    } else {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonobj = data.getJSONObject(i);
                            String good_no = jsonobj.getString("good_no");
                            String name = jsonobj.getString("name");
                            String barcode = jsonobj.getString("barcode");
                            double price = jsonobj.getDouble("price");
                            JSONObject product = jsonobj.getJSONObject("product");
                            String pic = product.getString("pic");
                            GoodsManageBean bean = new GoodsManageBean();
                            bean.setNum(good_no);////商品号
                            bean.setGoodsName(name);//商品名称
                            bean.setGoodsImg(Comment.URL_IMG_OR_VIDEO + "/image/" + pic);
                            bean.setPrice(price);//价格
                            bean.setHuoNumber(barcode);//条形码
                            bean.setStock(0);
                            list.add(bean);
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View aalayout = View.inflate(getActivity(), R.layout.layout_set_goods, null);
                    MyListView listview = aalayout.findViewById(R.id.list);
                    builder.setCancelable(true)
                            .setView(aalayout);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    int width = getAndroiodScreenProperty();
                    params.width = (int) (width * 0.85);
                    dialog.getWindow().setAttributes(params);
                    adapter1 = new BuHuoListViewAdapter(getActivity(), list);
                    listview.setAdapter(adapter1);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GoodsManageBean bean = list.get(position);
                            String num = bean.getNum();
                            int stock = bean.getStock();
                            String goodsName = bean.getGoodsName();
                            String goodsImg = bean.getGoodsImg();
                            String huoNumber = bean.getHuoNumber();
                            for (GoodsManageBean goods : buHuoList) {
                                goods.setHuoNumber(huoNumber);
                                goods.setNum(num);
                                goods.setGoodsName(goodsName);
                                goods.setStock(stock);
                                goods.setGoodsImg(goodsImg);
                            }
                            adapterBu = new BuHuoListViewAdapter1(getActivity(), buHuoList);
                            listviewBuHuo.setAdapter(adapterBu);
                            btn_confirm.setEnabled(true);
                            btn_confirm.setBackgroundResource(R.drawable.shape_76c5f0_1);
                            dialog.dismiss();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //数字按钮的点击事件
    private void clickNumButton(final EditText editText) {
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_7 = btn7.getText().toString();
                editText.setText(s + btn_7);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_8 = btn8.getText().toString();
                editText.setText(s + btn_8);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_9 = btn9.getText().toString();
                editText.setText(s + btn_9);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_4 = btn4.getText().toString();
                editText.setText(s + btn_4);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_5 = btn5.getText().toString();
                editText.setText(s + btn_5);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_6 = btn6.getText().toString();
                editText.setText(s + btn_6);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_1 = btn1.getText().toString();
                editText.setText(s + btn_1);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_2 = btn2.getText().toString();
                editText.setText(s + btn_2);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_3 = btn3.getText().toString();
                editText.setText(s + btn_3);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                String btn_0 = btn0.getText().toString();
                editText.setText(s + btn_0);
                int length = editText.getText().toString().length();
                editText.setSelection(length);
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serialPort = editText.getText().toString();
                String substring = null;
                if (serialPort != null && !serialPort.equals("")) {
                    substring = serialPort.substring(0, serialPort.length() - 1);
                    editText.setText(substring);
                    Log.d(TAG, "onViewClicked: " + editText.getText().toString());
                }
            }
        });

    }

    @OnClick({R.id.tv_a, R.id.tv_b, R.id.tv_c, R.id.tv_d, R.id.tv_e, R.id.tv_f, R.id.btn_tianMan, R.id.btn_updateGoods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_a:
                initHuoGuiBackground();
                tv_a.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_a.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListA);
                buHuoList = new ArrayList<>();
                break;
            case R.id.tv_b:
                initHuoGuiBackground();
                tv_b.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_b.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListB);
                break;
            case R.id.tv_c:
                initHuoGuiBackground();
                tv_c.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_c.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListC);
                break;
            case R.id.tv_d:
                initHuoGuiBackground();
                tv_d.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_d.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListD);
                break;
            case R.id.tv_e:
                initHuoGuiBackground();
                tv_e.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_e.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListE);
                break;
            case R.id.tv_f:
                initHuoGuiBackground();
                tv_f.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_f.setTextColor(getResources().getColor(R.color.white));
                numControl(0, 0);
                ll_caoZuo.setVisibility(View.GONE);
                setData(drinkBeanListF);
                break;
            case R.id.btn_tianMan:
                Log.d(TAG, "onViewClicked: " + shopName);
                if (shopName != null && !shopName.equals("") && numControl1.getValue() >= stock) {
                    numControl.setValue(numControl1.getValue() - stock);
                    numControl.setMaxValue(numControl1.getValue() - stock);
                    HuoNum = numControl1.getValue() - stock;
                } else {
                    HuoNum = 0;
                }
                break;
            case R.id.btn_updateGoods:
                //清空数据
                emptyList = new ArrayList<>();//存储stock 为空的
                unemptyList = new ArrayList<>();//存储stock 不为空的
                for (final GoodsManageBean goods : buHuoList) {
                    stock = goods.getStock();
                    String tunnel_code = goods.getTunnel_code();
                    if (stock != 0) {
                        unemptyList.add(goods);
                    } else {
                        TcnUtility.getToast(getActivity(), tunnel_code + "货道库存已空，不要重复清空货道");
                        emptyList.add(goods);
                    }
                }
                Log.d(TAG, "unemptyList: " + unemptyList);
                Log.d(TAG, "emptyList: " + emptyList);
                if (unemptyList.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View aalayout = View.inflate(getActivity(), R.layout.layout_clear, null);
                    Button btn_true = aalayout.findViewById(R.id.btn_true);
                    Button btn_false = aalayout.findViewById(R.id.btn_false);
                    builder.setCancelable(false)
                            .setView(aalayout);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    //取消清空货道
                    btn_false.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    //确定清空货道
                    btn_true.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (GoodsManageBean goods : unemptyList) {
                                stock = goods.getStock();
                                HuoNum = 0 - stock;
                                tunnel_no = goods.getTunnel_no();
                                Log.d(TAG, "adfdfd: " + tunnel_no);
                                adjust("adjust");
                                dialog.dismiss();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }
                break;
        }
    }

    //清空货道数据库存、确认补货数量
    private void adjust(final String type) {
        String url = Comment.URL + "/tunnel/adjust";
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("amount", String.valueOf(HuoNum));//调整数量 ，如果是出货，取负数  清空也是出货，所以是负的
        params.put("type", type);//属于什么类型 adjust：表示调整  replenish:补货
        String remark = null;
        if (type.equals("adjust")) {
            remark = "更换商品";
        } else if (type.equals("replenish")) {
            remark = "补充商品";
        }
        params.put("remark", remark);//操作原因
//                params.put("user_no", tunnel_no);//用户
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("amount=" + HuoNum + "&remark=" + remark + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&tunnel_no=" + tunnel_no + "&type=" + type + "&key=" + key);
        Log.d("TAG", "initProfile: " + signature);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                if (type.equals("adjust")) {
                    initSignin(3);
                } else if (type.equals("replenish")) {
                    initSignin(4);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        if (type.equals("adjust")) {
                            initSignin(3);
                        } else if (type.equals("replenish")) {
                            initSignin(4);
                        }
                    } else {
                        TcnUtility.getToast(getActivity(), "操作成功");
                        btn_tianMan.setBackgroundResource(R.drawable.shape_cccccc);
                        JSONObject request = jsonObject.getJSONObject("request");
                        String tunnel_no = request.getString("tunnel_no");
                        isIn = true;
                        numControl(0, 0);
                        huoDaoDetail(tunnel_no);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //设置货道列表数据
    int index;
    private void setData(List<DrinkBean> list) {
        dataList = new ArrayList<BuhuoBean>();
        for (int i = 1; i < 61; i++) {
            BuhuoBean bean = new BuhuoBean();
            if (i <= list.size()) {
                bean.setBean(list.get(i-1));
            }
            if (i < 10) {
                bean.setNum("0" + i);
            } else {
                bean.setNum(i + "");
            }
            dataList.add(bean);
        }
        adapter = new BuHuoGridViewAdapter(getActivity(), dataList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gv.setEnabled(false);
                ll_caoZuo.setVisibility(View.VISIBLE);
                btn_change.setEnabled(true);
                btn_change.setBackgroundResource(R.drawable.shape_76c5f0_1);
                isIn = false;
                adapter.setSeclection(position);
                Button btn = (Button) view.findViewById(R.id.btn);
                index = position;
//                adapter.notifyDataSetChanged();
                numControl.setValue(0);
                HuoNum = 0;
                BuhuoBean buhuoBean = dataList.get(position);
                DrinkBean bean = buhuoBean.getBean();
//                if (bean != null) {
//                    stock = position+2;
//                    shopName = bean.getName();
//                    Log.d(TAG, "onItemClick: "+ shopName);
//                }else{
//                    stock = 0;
//                    shopName = null;
//                }
                if (!btn.isSelected()) {
                    if (buHuoList.size() < 5) {
                        btn.setSelected(true);
                        btn.setBackgroundResource(R.drawable.shape_3baeed);
                        if (bean != null) {
                            tunnel_no = bean.getTunnel_no();
                            huoDaoDetail(tunnel_no);
                        } else {
                            numControl(0, 0);
                        }
                    } else {
                        TcnUtility.getToast(getActivity(), "最多选5个");
                        gv.setEnabled(true);
                    }
                } else {
                    btn.setSelected(false);
                    if (bean != null) {
                        btn.setBackgroundResource(R.drawable.shape_76c5f0_1);
                    } else {
                        btn.setBackgroundResource(R.drawable.shape_cccccc);
                        numControl(0, 0);
                    }
                    for (GoodsManageBean goods : buHuoList) {
                        if (goods.getIndex() == position) {
                            buHuoList.remove(goods);
                            adapterBu = new BuHuoListViewAdapter1(getActivity(), buHuoList);
                            listviewBuHuo.setAdapter(adapterBu);
                            gv.setEnabled(true);
                            break;
                        }
                    }
                    if(buHuoList.size()== 0){
                        btn_change.setEnabled(false);
                        btn_change.setBackgroundResource(R.drawable.shape_cccccc);
                    }
                }

            }
        });
    }

    //货道详情
    boolean isIn = false;

    private void huoDaoDetail(final String tunnel_no) {
        String url = Comment.URL + "/tunnel/detail";
//                String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&tunnel_no=" + tunnel_no + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                initSignin(1);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(1);
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        stock = jsonObject.getInt("stock");//库存
                        max = jsonObject.getInt("max");//货道最大存储
                        String tunnel_code = jsonObject.getString("tunnel_code");//货道号
                        JSONObject product = jsonObject.getJSONObject("product");
                        shopName = product.getString("name");//商品名称
                        String pic = product.getString("pic");//商品图
                        JSONObject good = jsonObject.getJSONObject("good");
                        barcode = good.getString("barcode");//条形码
                        good_no = good.getString("good_no");//商品号

                        GoodsManageBean goodsManageBean = new GoodsManageBean();
                        goodsManageBean.setNum(good_no);////商品号
                        goodsManageBean.setGoodsName(shopName);//商品名称
                        goodsManageBean.setGoodsImg(Comment.URL_IMG_OR_VIDEO + "/image/" + pic);
                        goodsManageBean.setHuoNumber(barcode);//条形码
                        goodsManageBean.setIndex(index);//点击的货道号，用于在buhuolist里移除对应的商品
                        goodsManageBean.setMax(max);//最大库存
                        goodsManageBean.setStock(stock);//现有库存
                        goodsManageBean.setTunnel_no(tunnel_no);//货道编号
                        goodsManageBean.setTunnel_code(tunnel_code);//货道号
                        if (isIn) {
                            for (GoodsManageBean bean:buHuoList) {
                                if(bean.getTunnel_no().equals(tunnel_no)){
                                    bean.setStock(stock);
                                    break;
                                }
                            }
                        }else{
                            boolean contains = buHuoList.contains(goodsManageBean);
                            Log.d(TAG, "index: "+contains);
                            if(!contains){
                                buHuoList.add(goodsManageBean);
                            }
                        }
                        adapterBu = new BuHuoListViewAdapter1(getActivity(), buHuoList);
                        listviewBuHuo.setAdapter(adapterBu);
                        gv.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //加减按钮
    private void numControl(int max, int stock) {
        //容量
        numControl1.setValue(max);
        numControl1.setMaxValue(max);
        //补货数量
        numControl.setValue(0);
        numControl.setMaxValue(numControl1.getValue() - stock);
    }

    //获取货道列表数据
    private void getData() {
        drinkBeanListA = new ArrayList<>();
        drinkBeanListB = new ArrayList<>();
        drinkBeanListC = new ArrayList<>();
        drinkBeanListD = new ArrayList<>();
        drinkBeanListE = new ArrayList<>();
        drinkBeanListF = new ArrayList<>();
        String text[] = {"255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶"};
        String url = Comment.URL + "/tunnel/listing";
//        String key = spf.getString("key", "");
        Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
        Log.d("TAG", "initProfile: " + signature);
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
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(2);
                    } else {
                        JSONArray data = jsonObject.getJSONArray("data");
                        Log.d(TAG, "onResponse: " + data);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            String tunnel_code = json.getString("tunnel_code");
                            String tunnel_no = json.getString("tunnel_no");
                            if (json.getString("good_no") != null && !json.getString("good_no").equals("")) {
                                JSONObject product = json.getJSONObject("product");
                                String name_show = product.getString("name_show");
                                DrinkBean bean = new DrinkBean();
                                bean.setName(name_show);
                                bean.setTunnel_no(tunnel_no);
//                                String substring = tunnel_code.substring(0, 1);
                                String substring = "1";
                                switch (substring) {
                                    case "1":
                                        drinkBeanListA.add(bean);
                                        tv_a.setVisibility(View.VISIBLE);
                                        break;
                                    case "2":
                                        drinkBeanListB.add(bean);
                                        tv_b.setVisibility(View.VISIBLE);
                                        break;
                                    case "3":
                                        drinkBeanListC.add(bean);
                                        tv_c.setVisibility(View.VISIBLE);
                                        break;
                                    case "4":
                                        drinkBeanListD.add(bean);
                                        tv_d.setVisibility(View.VISIBLE);
                                        break;
                                    case "5":
                                        drinkBeanListE.add(bean);
                                        tv_e.setVisibility(View.VISIBLE);
                                        break;
                                    case "6":
                                        drinkBeanListF.add(bean);
                                        tv_f.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        }
                        setData(drinkBeanListA);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        for(int i = 0;i<text.length;i++){
//            DrinkBean bean = new DrinkBean();
//            bean.setName(text[i]+i);
//            drinkBeanListA.add(bean);
//       }

    }

    //确认更换商品
    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        for (GoodsManageBean goods : buHuoList) {
            good_no = goods.getNum();
            tunnel_no = goods.getTunnel_no();
            Log.d(TAG, "changeGoods: " + tunnel_no);
            changeGoods();
        }
    }

    //更换商品确认
    private void changeGoods() {
        String url = Comment.URL + "/tunnel/good";
        final Map<String, String> params = new HashMap<>();
        params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
        params.put("good_no", good_no);//商品号
        params.put("tunnel_no", tunnel_no);//货道编号
        params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
        String signature = MD5Util.encrypt("good_no=" + good_no + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&tunnel_no=" + tunnel_no + "&key=" + key);
        params.put("signature", signature);//签名参数
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("TAG", "onResponse:失败" + e);
                i = 0;
                initSignin(6);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("TAG", "onResponse:成功" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean code = jsonObject.getBoolean("code");
                    if (!code) {
                        i = 0;
                        initSignin(6);
                    } else {
                        TcnUtility.getToast(getActivity(), "操作成功");
                        isIn = true;
                        numControl(0, 0);
//                        huoDaoDetail(tunnel_no);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //确认补货操作
    @OnClick(R.id.btn_Confirm)
    public void onClick() {
        if (HuoNum > 0) {
            adjust("replenish");
        }
    }

    //更换商品
    @OnClick(R.id.btn_change)
    public void onClick1() {
        emptyList = new ArrayList<>();//存储stock 为空的
        unemptyList = new ArrayList<>();//存储stock 不为空的
        for (final GoodsManageBean goods : buHuoList) {
            stock = goods.getStock();
            if (stock != 0) {
                unemptyList.add(goods);
            } else {
                emptyList.add(goods);
            }
        }
        if (unemptyList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View aalayout = View.inflate(getActivity(), R.layout.layout_barcode, null);
            Button btn_confirm = aalayout.findViewById(R.id.btn_confirm);
            final EditText et_barcode = aalayout.findViewById(R.id.et_barcode);
            btn7 = aalayout.findViewById(R.id.btn_7);
            btn8 = aalayout.findViewById(R.id.btn_8);
            btn9 = aalayout.findViewById(R.id.btn_9);
            btn4 = aalayout.findViewById(R.id.btn_4);
            btn5 = aalayout.findViewById(R.id.btn_5);
            btn6 = aalayout.findViewById(R.id.btn_6);
            btn1 = aalayout.findViewById(R.id.btn_1);
            btn2 = aalayout.findViewById(R.id.btn_2);
            btn3 = aalayout.findViewById(R.id.btn_3);
            btn0 = aalayout.findViewById(R.id.btn_0);
            btnDel = aalayout.findViewById(R.id.btn_del);
            clickNumButton(et_barcode);
            builder.setCancelable(true)
                    .setView(aalayout);
            final AlertDialog dialog = builder.create();
            dialog.show();
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barcode = et_barcode.getText().toString();
                    barcode();
                    dialog.dismiss();
                }
            });
        } else {
            TcnUtility.getToast(getActivity(), "请先清空货道");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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

    //初始化纸币背景颜色
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

    //终端签到
    //    String param = "terminal_code=8986001200ACBDA0123" + "&timestamp=" + timeStamp + "&auth_sign=" + ciphertext;
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
        //auth_sign MD5加密
        String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);
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
                        huoDaoDetail(tunnel_no);
                    } else if (flag == 2) {
                        getData();
                    } else if (flag == 3) {
                        adjust("adjust");
                    } else if (flag == 4) {
                        adjust("replenish");
                    } else if (flag == 5) {
                        barcode();
                    } else if (flag == 6) {
                        changeGoods();
                    } else if (flag == 7) {
                        full();
                    }
                }
            });
        } else {
            Log.d(TAG, "initSignin: " + i);
            TcnUtility.getToast(getActivity(), "请求超时");
        }
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
