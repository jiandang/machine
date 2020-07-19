package com.example.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ASXY_home on 2018-06-22.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**重新设置高度
         *这个方法就是根据传入的大小和模式生成一个MeasureSpec类型的32位int值。
         * 用两位来表示模式，剩下30位表示大小。 所以传入的Integer.MAX_VALUE >> 2就是30位的最大值
         *模式是MeasureSpec.AT_MOST即表示子视图最多只能是specSize中指定的大小，最大不超过这个大小
         */
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
