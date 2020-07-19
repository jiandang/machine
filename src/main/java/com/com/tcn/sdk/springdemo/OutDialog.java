package com.com.tcn.sdk.springdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;


/**
 * Created by Administrator on 2016/4/18.
 */
public class OutDialog extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int DISMISS_DIALOG    = 2;
    private static final int DISMISS_DIALOG_DELAYMILLIS = 1000;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final int TIME_TITLE_SY = 5;
    private static final char SUFFIX = '.';
    private RotateAnimation mAnim;

    private Context m_Context = null;
    private ImageView iv_route;
    private TextView tv,time_shyu;
    private TextView tv_point;
    private TextView outnum;
    private boolean cancelable = true;
    private int timeCount = 0;
    private int m_iMaxTime = 60000;
    private int m_itime_sy = 0;
    private String m_sy_fdata = null;
    private StringBuffer m_sy_data_bf = null;


    private Handler handler = new Handler(){
        private int num = 0;


        public void handleMessage(Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num ++;
                for (int i = 0;i < num;i++) {
                    builder.append(SUFFIX);
                }

                if (timeCount++ > (m_iMaxTime/CHNAGE_TITLE_DELAYMILLIS)) {
                    handler.removeMessages(CHANGE_TITLE_WHAT);
                    timeCount = 0;
                    dismiss();
                    return;
                }

                tv_point.setText(builder.toString());
                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            } else if (DISMISS_DIALOG == msg.what) {
                dismiss();
            } else if (msg.what == TIME_TITLE_SY) {
                if (null == m_sy_data_bf) {
                    m_sy_data_bf = new StringBuffer();
                }
                m_sy_data_bf.delete(0,m_sy_data_bf.length());
                m_sy_data_bf.append(m_sy_fdata);
                m_sy_data_bf.append(String.valueOf(msg.arg1));
                m_sy_data_bf.append("s");
                if (time_shyu != null) {
                    time_shyu.setText(m_sy_data_bf.toString());
                }

                if (msg.arg1 > 0) {

                    Message message = handler.obtainMessage();
                    message.what = TIME_TITLE_SY;
                    message.arg1 = msg.arg1 - 1;
                    handler.removeMessages(TIME_TITLE_SY);
                    handler.sendMessageDelayed(message,1000);
                } else {
                    if (time_shyu != null) {
                        time_shyu.setText("");
                    }
                }
            }
            else {

            }
        };
    };

    public OutDialog(Context context, String str, String str1) {
        super(context, R.style.ui_base_Dialog_bocop);
        m_Context = context;
        init(str,str1);
    }

    private void init(String str, String str1) {
        View contentView = View.inflate(getContext(), ResourceUtil.getLayoutId(m_Context, "app_outdialog"), null);
        //contentView.setBackgroundResource(R.drawable.dialoback);
        setContentView(contentView);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        time_shyu = (TextView) findViewById(ResourceUtil.getId(m_Context, "time_shyu"));
        iv_route = (ImageView) findViewById(ResourceUtil.getId(m_Context, "iv_route"));
        tv = (TextView) findViewById(ResourceUtil.getId(m_Context, "tv"));
        tv.setText(str1);
        tv_point = (TextView) findViewById(ResourceUtil.getId(m_Context, "tv_point"));
        outnum = (TextView) findViewById(ResourceUtil.getId(m_Context, "outnum"));
        outnum.setText(str);
        initAnim();
        timeCount = 0;
        getWindow().setWindowAnimations(Resources.getAnimResourceID(R.anim.ui_base_alpha_in));
    }

    public void deInit() {
        setShowTime(0);
        setOnCancelListener(null);
        setOnShowListener(null);
        setOnDismissListener(null);
        if (iv_route != null) {
            iv_route.clearAnimation();
            iv_route = null;
        }
        if (mAnim != null) {
            mAnim.cancel();
            mAnim = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        tv = null;
        tv_point = null;
        outnum = null;
        m_Context = null;
    }

    private void initAnim() {
        mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
        mAnim.setDuration(1500);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        LinearInterpolator lir = new LinearInterpolator();
        mAnim.setInterpolator(lir);
    }

    @Override
    public void show() {
        timeCount = 0;
        iv_route.startAnimation(mAnim);
        handler.removeMessages(CHANGE_TITLE_WHAT);
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
        super.show();
    }

    @Override
    public void dismiss() {
        mAnim.cancel();
        iv_route.clearAnimation();
        handler.removeCallbacksAndMessages(null);
        super.dismiss();
    }



    @Override
    public void cancel() {
        mAnim.cancel();
        iv_route.clearAnimation();
        handler.removeCallbacksAndMessages(null);
        // TODO Auto-generated method stub
        super.cancel();
    }

    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public void setTitle(CharSequence title) {
        tv.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public void showSuccess() {
        handler.removeMessages(CHANGE_TITLE_WHAT);
        mAnim.cancel();
        iv_route.clearAnimation();
        //iv_route.setBackgroundResource(0);
        iv_route.setImageResource(R.mipmap.lyric_search_pressed);
        outnum.setText("");
        tv.setText("出货成功");
        tv_point.setText("");
        tv_point.setVisibility(View.GONE);
        handler.sendEmptyMessageDelayed(DISMISS_DIALOG, DISMISS_DIALOG_DELAYMILLIS);
    }

    public void showFail() {
        handler.removeMessages(CHANGE_TITLE_WHAT);
        mAnim.cancel();
        iv_route.clearAnimation();
        iv_route.setBackgroundResource(0);
        iv_route.setImageResource(R.mipmap.shibai);
        outnum.setText("");
        tv.setText("出货失败");
        tv_point.setText("");
        tv_point.setVisibility(View.GONE);
        handler.sendEmptyMessageDelayed(DISMISS_DIALOG, DISMISS_DIALOG_DELAYMILLIS);
    }

    public void setNumber(String number) {
        timeCount = 0;
        if (number != null && outnum != null) {
            outnum.setText(number);
        }
    }

    public void setText(String text) {
        if (tv != null) {
            tv.setText(text);
        }
    }

    public void setShowTime(int second) {
        m_iMaxTime = second * 1000;
    }

    public void cleanData() {
        m_itime_sy = 0;
        if (time_shyu != null) {
            time_shyu.setText("");
        }
        m_sy_fdata = null;
        handler.removeMessages(TIME_TITLE_SY);
    }

    public void setSyTime(int second,String data) {
        if (second <= 0) {
            handler.removeMessages(TIME_TITLE_SY);
            if (time_shyu != null) {
                time_shyu.setText(data);
            }
            return;
        }
        m_itime_sy = second;
        m_sy_fdata = data;
        Message message = handler.obtainMessage();
        message.what = TIME_TITLE_SY;
        message.arg1 = second;
        handler.removeMessages(TIME_TITLE_SY);
        handler.sendMessage(message);
    }

}
