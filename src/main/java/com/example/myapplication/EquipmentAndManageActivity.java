package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.adapter.MFragmentPagerAdapter;
import com.example.myapplication.fragment.fragment.BuHuoFragment;
import com.example.myapplication.fragment.fragment.DuKaQiFragment;
import com.example.myapplication.fragment.fragment.GoodsManageFragment;
import com.example.myapplication.fragment.fragment.HuoDaoFragment;
import com.example.myapplication.fragment.fragment.MoneyManageFragment;
import com.example.myapplication.fragment.fragment.SellNumFragment;
import com.example.myapplication.fragment.fragment.SettingFragment;

import java.util.ArrayList;

public class
EquipmentAndManageActivity extends AppCompatActivity {
    //系统设置
    private TextView tv_set;
    //商品管理
    private TextView tv_goodsManage;
    //补货操作
    private TextView tv_buHuo;
    //货道测试
    private TextView tv_huoDao;
    //现金管理
    private TextView tv_moneyManage;
    //读卡器测试
    private TextView tv_duKaQi;
    //销售统计
    private TextView tv_sellNum;

    //实现Tab滑动效果
    private ViewPager mViewPager;

    //动画图片
    private ImageView cursor;
    //返回
    private ImageView iv_return;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private int position_four;
    private int position_five;
    private int position_six;

    //动画图片宽度
    private int bmpW;
    //当前页卡编号
    private int currIndex = 0;
    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;
    public Context context;
    public static final String TAG = "EquipmentAndManageActivity";
    private MFragmentPagerAdapter adapter;

    private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_and_manage);
        spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        //初始化TextView
        InitTextView();
        //初始化ImageView
        InitImageView();
        //初始化Fragment
        InitFragment();
        //初始化ViewPager
        InitViewPager();
    }

    @Override
    public void onResume() {
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
    private void InitTextView() {

        //系统设置tab
        tv_set = (TextView) findViewById(R.id.tv_set);
        //商品管理tab
        tv_goodsManage = (TextView) findViewById(R.id.tv_goodsManage);
        //补货操作tab
        tv_buHuo = (TextView) findViewById(R.id.tv_buHuo);
        //货道测试tab
        tv_huoDao = (TextView) findViewById(R.id.tv_huoDao);
        //现金管理tab
        tv_moneyManage = (TextView) findViewById(R.id.tv_moneyManage);
        //读卡器测试tab
        tv_duKaQi = (TextView) findViewById(R.id.tv_duKaQi);
        //销售统计tab
        tv_sellNum = (TextView) findViewById(R.id.tv_sellNum);

        //添加点击事件
        tv_set.setOnClickListener(new MyOnClickListener(0));
        tv_goodsManage.setOnClickListener(new MyOnClickListener(1));
        tv_buHuo.setOnClickListener(new MyOnClickListener(2));
        tv_huoDao.setOnClickListener(new MyOnClickListener(3));
        tv_moneyManage.setOnClickListener(new MyOnClickListener(4));
        tv_duKaQi.setOnClickListener(new MyOnClickListener(5));
        tv_sellNum.setOnClickListener(new MyOnClickListener(6));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            int style1 = spf.getInt("style1", 0);//获取待机页的样式
            if (style1 == 0) {
                style1 = 1;
            }
            Intent intent = new Intent(this, InitActivity.class);
            intent.putExtra("styleDaiJi", style1);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        adapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        mViewPager.setAdapter(adapter);

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(6);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);

        //将顶部文字恢复默认值
        resetTextViewTextColor();
        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void init() {
        //设置默认打开第一页
        mViewPager.setCurrentItem(0);
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

        bmpW = (screenW / 7);

        //设置动画图片宽度
//        setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 7.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
        position_four = position_one * 4;
        position_five = position_one * 5;
        position_six = position_one * 6;

    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new SettingFragment());
        fragmentArrayList.add(new GoodsManageFragment());
        fragmentArrayList.add(new BuHuoFragment());
        fragmentArrayList.add(new HuoDaoFragment());
        fragmentArrayList.add(new MoneyManageFragment());
        fragmentArrayList.add(new DuKaQiFragment());
        fragmentArrayList.add(new SellNumFragment());

        //fragment嵌套viewpager需要用getChildFragmentManager()，而不能用getSupportFragmentManager()
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
        HuoDaoFragment item;

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {

                //当前为页卡1
                case 0://系统设置
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) {//从页卡1跳转转到页卡3
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) {//从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) {//从页卡1跳转转到页卡5
                        animation = new TranslateAnimation(position_four, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) {//从页卡1跳转转到页卡6
                        animation = new TranslateAnimation(position_five, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) {//从页卡1跳转转到页卡7
                        animation = new TranslateAnimation(position_six, 0, 0, 0);
                        resetTextViewTextColor();
                        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_set.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    SettingFragment settingFragment = (SettingFragment) adapter.getItem(position);
                    settingFragment.init();
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
                    break;

                //当前为页卡2
                case 1://s商品管理
                    //从页卡2跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) { //从页卡2跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) { //从页卡2跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) { //从页卡2跳转转到页卡5
                        animation = new TranslateAnimation(position_four, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) { //从页卡2跳转转到页卡6
                        animation = new TranslateAnimation(position_five, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) { //从页卡2跳转转到页卡7
                        animation = new TranslateAnimation(position_six, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_goodsManage.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    GoodsManageFragment goodsManageFragment = (GoodsManageFragment) adapter.getItem(position);
                    goodsManageFragment.initGoods();
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
                    break;

                //当前为页卡3
                case 2://补货操作
                    //从页卡3跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 1) {//从页卡3跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) {//从页卡3跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) {//从页卡3跳转转到页卡5
                        animation = new TranslateAnimation(position_four, position_two, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) { //从页卡3跳转转到页卡6
                        animation = new TranslateAnimation(position_five, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) { //从页卡3跳转转到页卡7
                        animation = new TranslateAnimation(position_six, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_buHuo.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    BuHuoFragment buHuoFragment = (BuHuoFragment) adapter.getItem(position);
                    buHuoFragment.initBuHuo();
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
                    break;
                //当前为页卡4
                case 3://货到测试
                    //从页卡4跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 1) {//从页卡4跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) {//从页卡4跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) {//从页卡4跳转转到页卡5
                        animation = new TranslateAnimation(position_four, position_three, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) { //从页卡4跳转转到页卡6
                        animation = new TranslateAnimation(position_five, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) { //从页卡4跳转转到页卡7
                        animation = new TranslateAnimation(position_six, position_one, 0, 0);
                        resetTextViewTextColor();
                        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_huoDao.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    HuoDaoFragment huoDaoFragment = (HuoDaoFragment) adapter.getItem(position);
                    huoDaoFragment.initHuoDao();
                    huoDaoFragment.stopSerialPort();
                    break;
                //当前为页卡5
                case 4://现金管理
                    //从页卡5跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 1) {//从页卡5跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) {//从页卡5跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) {//从页卡5跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) { //从页卡5跳转转到页卡6
                        animation = new TranslateAnimation(position_five, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) { //从页卡5跳转转到页卡7
                        animation = new TranslateAnimation(position_six, position_four, 0, 0);
                        resetTextViewTextColor();
                        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_moneyManage.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
                    break;
                //当前为页卡6
                case 5://读卡器测试
                    //从页卡6跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 1) {//从页卡6跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) {//从页卡6跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) {//从页卡6跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) { //从页卡6跳转转到页卡5
                        animation = new TranslateAnimation(position_four, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 6) { //从页卡6跳转转到页卡7
                        animation = new TranslateAnimation(position_six, position_five, 0, 0);
                        resetTextViewTextColor();
                        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_duKaQi.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
                    break;
                //当前为页卡7
                case 6://销售统计
                    //从页卡7跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 1) {//从页卡7跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 2) {//从页卡7跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 3) {//从页卡7跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 4) { //从页卡7跳转转到页卡5
                        animation = new TranslateAnimation(position_four, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    } else if (currIndex == 5) { //从页卡7跳转转到页卡6
                        animation = new TranslateAnimation(position_five, position_six, 0, 0);
                        resetTextViewTextColor();
                        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                        tv_sellNum.setBackgroundResource(R.drawable.shape_3baeed);
                    }
                    SellNumFragment sellNumFragment = (SellNumFragment) adapter.getItem(position);
                    sellNumFragment.initSellNum();
                    item = (HuoDaoFragment) adapter.getItem(3);
                    item.stopSerialPort();
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

    ;

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor() {

        tv_set.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_goodsManage.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_buHuo.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_moneyManage.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_duKaQi.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_huoDao.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        tv_sellNum.setTextColor(getResources().getColor(R.color.main_top_tab_color));

        tv_set.setBackgroundColor(getResources().getColor(R.color.background));
        tv_goodsManage.setBackgroundColor(getResources().getColor(R.color.background));
        tv_buHuo.setBackgroundColor(getResources().getColor(R.color.background));
        tv_moneyManage.setBackgroundColor(getResources().getColor(R.color.background));
        tv_duKaQi.setBackgroundColor(getResources().getColor(R.color.background));
        tv_huoDao.setBackgroundColor(getResources().getColor(R.color.background));
        tv_sellNum.setBackgroundColor(getResources().getColor(R.color.background));
    }
}
