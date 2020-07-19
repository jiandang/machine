package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.bean.GoodsManageBean;
import com.example.myapplication.util.GlideUtils;

import java.util.List;

/**
 * Created by ASXY_home on 2018-06-12.
 */

public class BuHuoListViewAdapter1 extends BaseAdapter {
    private List<GoodsManageBean> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public BuHuoListViewAdapter1(Context context, List<GoodsManageBean> data) {
        this.data = data;
        this.context = context;
        Log.d("TAG", "BuHuoListViewAdapter: ++++++"+data);
        this.layoutInflater = LayoutInflater.from(context);
    }
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            // 获取组件布局
            convertView = layoutInflater.inflate(R.layout.layout_change, null);
            holder.tv_stock = convertView.findViewById(R.id.tv_stock);
            holder.tv_huodaoNum = convertView.findViewById(R.id.tv_huodaoNum);
            holder.tv_goodsName = convertView.findViewById(R.id.tv_goodsName);
            holder.tv_huoNum = convertView.findViewById(R.id.tv_huoNum);
            holder.iv_pic = convertView.findViewById(R.id.iv_pic);
            holder.ll_goods = convertView.findViewById(R.id.ll_goods);
            // 使用tag来存储数据
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsManageBean bean = data.get(position);
        holder.tv_stock.setText(bean.getStock()+"");
        holder.tv_huodaoNum.setText(bean.getTunnel_code());
        holder.tv_goodsName.setText(bean.getGoodsName());
        holder.tv_huoNum.setText(bean.getHuoNumber());
        GlideUtils.load(context,bean.getGoodsImg(),holder.iv_pic);
        // 点击改变选中listItem的背景色
        if (clickTemp == position) {
            holder.ll_goods.setBackgroundResource(R.drawable.shape_76c5f0_2);
        } else {
            holder.ll_goods.setBackgroundColor(context.getResources().getColor(R.color.background));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_stock;//库存
        TextView tv_huodaoNum;//货道号
        TextView tv_goodsName;//商品名称
        TextView tv_huoNum;//条形码
        ImageView iv_pic;//商品图
        LinearLayout ll_goods;//改背景颜色用
    }
}
