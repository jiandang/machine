package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapplication.bean.DrinkBean;

import java.util.List;

/**
 * Created by ASXY_home on 2018-08-06.
 */

public class DrinkAdapter extends BaseAdapter {
    private List<DrinkBean> data;
    private Context context;

    public DrinkAdapter(List<DrinkBean> data, Context context) {
        this.data = data;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_drink, null);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.btn_detail = convertView.findViewById(R.id.btn_detail);
            holder.btn_buy = convertView.findViewById(R.id.btn_buy);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        DrinkBean bean = data.get(position);
        holder.tv_name.setText(bean.getName().replaceAll("\r\n|\r|\n| ", ""));
        holder.iv_img.setImageResource(R.mipmap.ic_launcher);
        holder.tv_price.setText("￥"+bean.getPrice());
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "详情", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "购买", Toast.LENGTH_SHORT).show();

            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView iv_img;//商品图
        TextView tv_name;//商品名
        TextView tv_price;//商品价格
        Button btn_detail;//详情
        Button btn_buy;//购买
    }
}
