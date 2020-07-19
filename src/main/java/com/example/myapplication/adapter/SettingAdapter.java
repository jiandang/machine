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
import com.example.myapplication.bean.SettingBean;
import com.example.myapplication.util.GlideUtils;

import java.util.List;

/**
 * Created by ASXY_home on 2018-08-06.
 */

public class SettingAdapter extends BaseAdapter {
    private List<SettingBean> data;
    private Context context;


    public SettingAdapter(List<SettingBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    private int clickTemp = -1;

    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
        Log.d("TAG", "setSeclection: " + clickTemp);

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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_setting, null);
            holder.iv_manage = convertView.findViewById(R.id.iv_manage);
            holder.tv_manage = convertView.findViewById(R.id.tv_manage);
            holder.ll_img = convertView.findViewById(R.id.ll_img);//图片选中时用的边框
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SettingBean bean = data.get(position);
        holder.tv_manage.setText(bean.getImgName());
        GlideUtils.load(context, bean.getImgUrl(), holder.iv_manage);
//        holder.iv_manage.setImageResource(R.drawable.image);
//        holder.ll_img.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
        // 点击改变选中listItem的背景色
//        if (clickTemp == position) {
//            holder.ll_img.setBackgroundResource(R.drawable.shape_biankuang_3baeed_alpha_wu_yuan_jiao);
//        } else {
//            holder.ll_img.setBackgroundResource(R.drawable.shape_biankuang_wu_se_alpha_wu_yuan_jiao);
//        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_manage;
        TextView tv_manage;
        LinearLayout ll_img;
    }
}
