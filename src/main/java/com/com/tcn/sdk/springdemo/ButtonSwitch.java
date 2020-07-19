package com.com.tcn.sdk.springdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapp.R;


/**
 * Created by Administrator on 2016/8/26.
 */
public class ButtonSwitch extends RelativeLayout {

    private TextView btn_name = null;
    private MySlipSwitch btn_switch = null;


    public ButtonSwitch(Context context) {
        super(context);
        initView(context);
    }

    public ButtonSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ButtonSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.ui_base_button_switch_layout, this);
        btn_name = (TextView)findViewById(R.id.btn_name);
        btn_switch = (MySlipSwitch)findViewById(R.id.btn_switch);
        btn_switch.setImageResource(R.mipmap.switch_bkg_switch,
                R.mipmap.switch_bkg_switch, R.mipmap.switch_btn_slip);
        btn_switch.setOnSwitchListener(m_SwitchListener);
    }

    public void setTextSize(int size) {
        if (btn_name != null) {
            btn_name.setTextSize(size);
        }
    }

    public void setButtonName(int resid) {
        if (btn_name != null) {
            btn_name.setText(resid);
        }
    }

    public void setButtonName(String text) {
        if (btn_name != null) {
            btn_name.setText(text);
        }
    }

    public void setSwitchState(boolean switchState) {
        if (btn_switch != null) {
            btn_switch.setSwitchState(switchState);
        }
    }

    private ButtonListener m_ButtonListener = null;

    public void setButtonListener(ButtonListener listener) {
        m_ButtonListener = listener;
    }

    public void removeButtonListener() {
        if (btn_switch != null) {
            btn_switch.setOnSwitchListener(null);
        }
        m_SwitchListener = null;
        m_ButtonListener = null;
    }

    public interface ButtonListener {
        public void onSwitched(View v, boolean isSwitchOn);
    }

    private SwitchListener m_SwitchListener = new SwitchListener();
    private class SwitchListener implements MySlipSwitch.OnSwitchListener {

        @Override
        public void onSwitched(boolean isSwitchOn) {
            if (m_ButtonListener != null) {
                m_ButtonListener.onSwitched(ButtonSwitch.this, isSwitchOn);
            }
        }
    }
}
