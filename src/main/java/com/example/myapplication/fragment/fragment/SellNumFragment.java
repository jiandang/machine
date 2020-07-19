package com.example.myapplication.fragment.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapplication.bean.SellNumBean;
import com.example.myapplication.controller.TcnUtility;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.PageIndicatorView;
import com.example.myapplication.util.PageRecyclerView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * 这是销售统计页
 */
public class SellNumFragment extends Fragment {
    private static final String TAG = "SellNumFragment";
    @InjectView(R.id.cusom_swipe_view1)
    PageRecyclerView cusom_swipe_view;
    @InjectView(R.id.indicator)
    PageIndicatorView indicator;
    @InjectView(R.id.tv_date)
    TextView tv_date;
    @InjectView(R.id.tv_date1)
    TextView tv_date1;
    @InjectView(R.id.btn_query)
    Button btn_query;
    private List<SellNumBean> dataListD = null;
    private List<SellNumBean> dataList = null;
    private PageRecyclerView.PageAdapter myAdapter = null;
    private long timeStamp;
    private String key;
    private boolean isFirst = true;//是否第一次进来
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_num, container, false);
        ButterKnife.inject(this, view);
        configLog();
        SimpleDateFormat sdfY = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdfY.format(new Date());//当前日期
        tv_date.setText(date);
        cusom_swipe_view.setIndicator(indicator);
        // 设置行数和列数
        cusom_swipe_view.setPageSize(8, 1);
        // 设置页间距
        cusom_swipe_view.setPageMargin(30);
        cusom_swipe_view.setVisibility(View.INVISIBLE);
        initData();
        tv_date.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        tv_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        tv_date1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        tv_date1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        return view;
    }

    public void initSellNum() {
        SimpleDateFormat sdfY = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdfY.format(new Date());//当前日期
        tv_date.setText(date);
        tv_date1.setText(date);
        initData();
//        setData();
    }

    @OnClick(R.id.btn_query)
    public void onClick() {
        initData();
    }

    //日期选择器
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                if (dayOfMonth < 10) {
                    tv_date.setText(year + "-" + monthOfYear + "-0" + dayOfMonth);
                } else {
                    tv_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
        gLogger = Logger.getLogger("SellNumFragment");
    }
    //设置商品统计数据
    private void setData() {
        if (dataListD.size() != 0 && dataListD != null && cusom_swipe_view != null) {
            gLogger.debug("数据非空验证："+(dataListD.size()!= 0));
            gLogger.debug("控件非空验证："+(cusom_swipe_view != null));
            Log.d(TAG, "setData: " + dataListD);
            myAdapter = cusom_swipe_view.new PageAdapter(dataListD, new PageRecyclerView.CallBack() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sell_num, parent, false);
                    return new MyHolder(view);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    final SellNumBean bean = dataListD.get(position);
                    ((MyHolder) holder).tv_rank.setText(bean.getRank());
                    ((MyHolder) holder).tv_goodsName.setText(bean.getGoodsName());
                    ((MyHolder) holder).tv_dayNum.setText(bean.getDayNum());
//                    ((MyHolder) holder).tv_goodsName1.setText(bean.getGoodsName1());
//                    ((MyHolder) holder).tv_monthNum.setText(bean.getMonthNum());
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
            cusom_swipe_view.setAdapter(myAdapter);
            if (!isFirst) {
                cusom_swipe_view.setVisibility(View.VISIBLE);
            } else {
                isFirst = false;
            }
        }
        //添加Android自带的分割线
//        cusom_swipe_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    //获取商品统计数据
    private void initData() {
        dataListD = new ArrayList<>();
        dataList = new ArrayList<>();
        if (tv_date != null) {
            String date = tv_date.getText().toString();
            final int[] daySum = {0};//日销量总计
            final int[] monthSum = {0};//月销量总计
            String text[] = {"255ml统一冰红茶", "255ml统一绿茶", "255ml康师傅冰红茶",
                    "255ml康师傅绿茶", "康师傅冰糖雪梨", "姚家矿泉水", "娃哈哈矿泉水",
                    "冰露", "可口可乐", "雪碧", "美年达"};
            int monthNum[] = {20, 18, 16, 15, 13, 12, 10, 8, 6, 6, 5};
            String url = Comment.URL + "/report/good";
            final Map<String, String> params = new HashMap<>();
            params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
            params.put("date", date);//Unix时间戳（秒级）
            params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
//        String signature = MD5Util.encrypt("terminal_code="+Comment.terminal_code + "&timestamp=" + timeStamp  + "&key=" + key);
            String signature = MD5Util.encrypt("date=" + date + "&terminal_code=" + Comment.terminal_code + "&timestamp=" + timeStamp + "&key=" + key);
            Log.d("TAG", "initProfile: " + signature);
            params.put("signature", signature);//签名参数
            OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", "onResponse:失败" + e);
                    i = 0;
                    initSignin(0);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("TAG", "onResponse:成功" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean code = jsonObject.getBoolean("code");
                        if (!code) {
                            i = 0;
                            initSignin(0);
                        } else {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray day = data.getJSONArray("day");
                            for (int i = 0; i < day.length(); i++) {
                                JSONObject object = day.getJSONObject(i);
                                int count = object.getInt("count");//日销售统计
                                String good_name = object.getString("good_name");//商品名称
                                SellNumBean bean = new SellNumBean();
                                bean.setGoodsName(good_name);//商品名称
                                bean.setRank((i + 1) + "");//名次
                                bean.setDayNum(count + "");//日销量数量
                                daySum[0] += count;
    //                            bean.setRank("合计：");
    //                            if (daySum[0] != 0) {
    //                                bean.setDayNum(daySum[0] + "");
    //                            }
                                dataListD.add(bean);
                            }
    //                        JSONArray month = data.getJSONArray("month");
    //                        for (int i = 0; i < month.length(); i++) {
    //                            JSONObject object = month.getJSONObject(i);
    //                            int count = object.getInt("count");//月销售统计
    //                            String good_name1 = object.getString("good_name");//商品名称
    //                            SellNumBean bean;
    //                            if (dataListD.size() >i) {
    //                                bean = dataListD.get(i);
    //                            } else {
    //                                bean = new SellNumBean();
    //                            }
    //                            bean.setGoodsName1(good_name1);//商品名称
    //                            bean.setRank((i + 1) + "");//名次
    //                            bean.setMonthNum(count + "");//月销量数量
    //                            monthSum[0] += count;
    ////                            dataList.clear();
    //                            dataList.add(bean);
    //                        }
                            SellNumBean bean = new SellNumBean();
                            bean.setRank("合计：");
                            if (daySum[0] != 0) {
                                bean.setDayNum(daySum[0] + "");
                            }
                            dataListD.add(bean);
    //                        bean.setMonthNum(monthSum[0] + "");
    //                        dataList.add(bean);
                            // 设置数据
                            setData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
//        for (int i = 0; i < text.length; i++) {
//            SellNumBean bean = new SellNumBean();
//            bean.setGoodsName(text[i]);//商品名称
////            bean.setImgUrl(img[i]);
//            bean.setRank((i+1)+"");//名次
//            bean.setDayNum((text.length-i)+"");//日销量数量
//            bean.setMonthNum(monthNum[i]+"");//月销量数量
//            daySum[0] += (text.length-i);
//            monthSum[0] += monthNum[i];
//            dataList.add(bean);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //初始化Hodler
    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_rank = null;
        public TextView tv_goodsName = null;
        //        public TextView tv_goodsName1 = null;
        public TextView tv_dayNum = null;
//        public TextView tv_monthNum = null;


        public MyHolder(View itemView) {
            super(itemView);
            tv_rank = (TextView) itemView.findViewById(R.id.tv_rank);//名次
            tv_goodsName = (TextView) itemView.findViewById(R.id.tv_goodsName);//商品名称（日）
            tv_dayNum = (TextView) itemView.findViewById(R.id.tv_dayNum);//日销售数量
//            tv_goodsName1 = (TextView) itemView.findViewById(R.id.tv_goodsName1);//商品名称（月）
//            tv_monthNum = (TextView) itemView.findViewById(R.id.tv_monthNum);//月销售数量
        }
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
                        initData();
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
