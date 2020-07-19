package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.myapp.R;
import com.example.myapplication.bean.BuhuoBean;
import com.example.myapplication.bean.DrinkBean;

import java.util.List;

/**
 * Created by ASXY_home on 2018-06-12.
 */

public class BuHuoGridViewAdapter extends BaseAdapter {
    private List data;
    private LayoutInflater layoutInflater;

    public BuHuoGridViewAdapter(Context context, List data) {
        this.data = data;
        Log.d("TAG", "BuHuoGridViewAdapter: ++++++"+data);
        this.layoutInflater = LayoutInflater.from(context);
    }
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
        Log.d("TAG", "setSeclection: "+clickTemp);
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
            convertView = layoutInflater.inflate(R.layout.gridview_bu_huo, null);
            holder.btn = convertView.findViewById(R.id.btn);
            // 使用tag来存储数据
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        BuhuoBean bean = (BuhuoBean) data.get(position);
        holder.btn.setText(bean.getNum());
        DrinkBean drinkBean = bean.getBean();
        // 点击改变选中listItem的背景色
//        if (clickTemp == position) {
//            holder.btn.setBackgroundResource(R.drawable.shape_3baeed);
//        } else {
            if (drinkBean != null) {
                holder.btn.setBackgroundResource(R.drawable.shape_76c5f0_1);
            } else {
                holder.btn.setBackgroundResource(R.drawable.shape_cccccc);
            }
//        }

        return convertView;
    }
    class ViewHolder{
        Button btn;
    }
}
