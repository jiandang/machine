package com.example.myapplication.fragment.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapplication.bean.GoodsManageBean;
import com.example.myapplication.util.PageIndicatorView;
import com.example.myapplication.util.PageRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 这是商品管理页
 */
public class GoodsManageFragment extends Fragment {

    @InjectView(R.id.cusom_swipe_view)
    PageRecyclerView cusom_swipe_view;
    @InjectView(R.id.indicator)
    PageIndicatorView indicator;
    @InjectView(R.id.btn_confirm)
    Button btn_confirm;
    private List<GoodsManageBean> dataList = null;
    private PageRecyclerView.PageAdapter myAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goods_manage, container, false);
        ButterKnife.inject(this, view);
        initData();
        cusom_swipe_view.setIndicator(indicator);
        // 设置行数和列数
        cusom_swipe_view.setPageSize(5, 1);
        // 设置页间距
        cusom_swipe_view.setPageMargin(30);
        // 设置数据
        setData();
        return view;
    }
    public void initGoods(){
        initData();
        setData();
    }
    //设置商品信息
    private void setData() {
        myAdapter = cusom_swipe_view.new PageAdapter(dataList, new PageRecyclerView.CallBack() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_goods_manage, parent, false);
                return new MyHolder(view);
            }
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                final GoodsManageBean bean = dataList.get(position);
                ((MyHolder) holder).tv_num.setText(bean.getNum());
                ((MyHolder) holder).tv_goodsName.setText(bean.getGoodsName());
                ((MyHolder) holder).iv_goodsImg.setImageResource(R.drawable.bhc);
                ((MyHolder) holder).tv_price.setText(bean.getPrice()+"");
//                ((MyHolder) holder).btn_add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (bean.getPrice()<10) {
//                            bean.setPrice(bean.getPrice() + 1);
//                            myAdapter.notifyItemChanged(position);
//                        }
//                    }
//                });
//                ((MyHolder) holder).btn_sub.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (bean.getPrice()>1) {
//                            bean.setPrice(bean.getPrice() - 1);
//                            myAdapter.notifyItemChanged(position);
//                        }
//                    }
//                });
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
        //添加Android自带的分割线
//        cusom_swipe_view.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }
    //获取商品信息
    private void initData() {
        dataList = new ArrayList<>();
        String text[] = {"255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶",
                "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶", "255ml统一冰红茶"};
        for (int i = 0; i < text.length; i++) {
            GoodsManageBean bean = new GoodsManageBean();
            bean.setGoodsName(text[i] + i);//商品名称
            bean.setNum((i + 1) + "");//序号
//            bean.setGoodsImg();//商品图片
            bean.setPrice(10.00+i);//价格
            dataList.add(bean);
        }
        Log.d("TAG", "initData: " + dataList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        for (GoodsManageBean bean : dataList)
        Log.d("TAG", "onViewClicked: "+bean.getPrice());
    }

    //初始化Hodler
    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_num = null;
        public TextView tv_goodsName = null;
        public ImageView iv_goodsImg = null;
        public TextView tv_price = null;
//        public Button btn_sub = null;
//        public Button btn_add = null;


        public MyHolder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);//序号
            tv_goodsName = (TextView) itemView.findViewById(R.id.tv_goodsName);//商品名称
            iv_goodsImg = (ImageView) itemView.findViewById(R.id.iv_goodsImg);//商品图片
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//商品价格
//            btn_sub = (Button) itemView.findViewById(R.id.btn_sub);//商品数量减
//            btn_add = (Button) itemView.findViewById(R.id.btn_add);//商品数量加
        }
    }
}
