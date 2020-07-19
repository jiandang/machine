package com.example.myapplication.fragment.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 这是现金管理页
 */
public class MoneyManageFragment extends Fragment {

    @InjectView(R.id.tv5)
    TextView tv5;
    @InjectView(R.id.tv10)
    TextView tv10;
    @InjectView(R.id.tv20)
    TextView tv20;
    @InjectView(R.id.tv50)
    TextView tv50;
    @InjectView(R.id.tv100)
    TextView tv100;
    @InjectView(R.id.ll_zhiBi)
    LinearLayout ll_zhiBi;
    @InjectView(R.id.tv0)
    TextView tv0;
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.ll_yingBi)
    LinearLayout ll_yingBi;
    @InjectView(R.id.btn_confirm)
    Button btn_confirm;
    @InjectView(R.id.tv_pay)
    TextView tv_pay;
    @InjectView(R.id.tv_zhaoLing)
    TextView tv_zhaoLing;
    private String zhiBiM;//设置纸币金额
    private String yingBiM;//设置硬币金额

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_money_manage, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv5, R.id.tv10, R.id.tv20, R.id.tv50, R.id.tv100,
            R.id.tv0, R.id.tv1, R.id.btn_confirm,R.id.tv_pay, R.id.tv_zhaoLing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv5:
                initZhiBiBackground();
                tv5.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                zhiBiM = tv5.getText().toString();
                break;
            case R.id.tv10:
                initZhiBiBackground();
                tv10.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                zhiBiM = tv10.getText().toString();
                break;
            case R.id.tv20:
                initZhiBiBackground();
                tv20.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                zhiBiM = tv20.getText().toString();
                break;
            case R.id.tv50:
                initZhiBiBackground();
                tv50.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                zhiBiM = tv50.getText().toString();
                break;
            case R.id.tv100:
                initZhiBiBackground();
                tv100.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                zhiBiM = tv100.getText().toString();
                break;
            case R.id.tv0:
                tv1.setBackgroundColor(Color.WHITE);
                tv0.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                yingBiM = tv0.getText().toString();
                break;
            case R.id.tv1:
                tv0.setBackgroundColor(Color.WHITE);
                tv1.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                yingBiM = tv1.getText().toString();
                break;
            case R.id.btn_confirm:
                break;
            case R.id.tv_pay:
                ll_zhiBi.setVisibility(View.VISIBLE);
                tv_pay.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_pay.setTextColor(getResources().getColor(R.color.white));
                tv_zhaoLing.setBackgroundColor(getResources().getColor(R.color.white));
                tv_zhaoLing.setTextColor(getResources().getColor(R.color.main_top_tab_color));
                initZhiBiBackground();
                tv5.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                tv1.setBackgroundColor(Color.WHITE);
                tv0.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                break;
            case R.id.tv_zhaoLing:
                ll_zhiBi.setVisibility(View.GONE);
                tv_zhaoLing.setBackgroundColor(getResources().getColor(R.color.background1));
                tv_zhaoLing.setTextColor(getResources().getColor(R.color.white));
                tv_pay.setBackgroundColor(getResources().getColor(R.color.white));
                tv_pay.setTextColor(getResources().getColor(R.color.main_top_tab_color));
                tv1.setBackgroundColor(Color.WHITE);
                tv0.setBackgroundResource(R.drawable.shape_biankuang_3baeed_wu_yuan_jiao);
                break;
        }
    }
    //初始化纸币背景颜色
    private void initZhiBiBackground(){
        tv5.setBackgroundColor(Color.WHITE);
        tv10.setBackgroundColor(Color.WHITE);
        tv20.setBackgroundColor(Color.WHITE);
        tv50.setBackgroundColor(Color.WHITE);
        tv100.setBackgroundColor(Color.WHITE);
    }
}
