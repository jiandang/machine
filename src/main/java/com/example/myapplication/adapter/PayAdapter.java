package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.bean.SettingBean;
import com.example.myapplication.util.GlideUtils;

import java.util.List;

/**
 * Created by ASXY_home on 2018-08-06.
 */

public class PayAdapter extends BaseAdapter {
    private List<SettingBean> data;
    private Context context;

    public PayAdapter(List<SettingBean> data, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_pay, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SettingBean bean = data.get(position);
        holder.tv.setText(bean.getImgName());
        GlideUtils.load(context, bean.getImgUrl(), holder.iv);
//        holder.iv.setImageResource(bean.getImgUrl());
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
