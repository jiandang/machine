package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.bean.GoodsManageBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ASXY_home on 2018-06-12.
 */

public class BuHuoListViewAdapter extends BaseAdapter {
    private List<GoodsManageBean> data;
    private LayoutInflater layoutInflater;

    public BuHuoListViewAdapter(Context context, List<GoodsManageBean> data) {
        this.data = data;
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
            convertView = layoutInflater.inflate(R.layout.gridview_set_goods, null);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_goodsName = convertView.findViewById(R.id.tv_goodsName);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            // 使用tag来存储数据
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsManageBean bean = data.get(position);
        holder.tv_num.setText(bean.getNum());
        holder.tv_goodsName.setText(bean.getGoodsName());
        double price0 = bean.getPrice() / 100;
        DecimalFormat df = new DecimalFormat("#0.0");
        String price = df.format(price0);
        holder.tv_price.setText(price);

        return convertView;
    }
    class ViewHolder{
        TextView tv_num;//商品号
        TextView tv_goodsName;//商品名称
        TextView tv_price;//商品价格
    }
}
