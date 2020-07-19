package com.com.tcn.sdk.springdemo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapp.R;


/**
 * Created by Administrator on 2017/10/23.
 */
public class ButtonEditSelectD extends RelativeLayout {
    public static final int BUTTON_TYPE_EDIT = 1;
    public static final int BUTTON_TYPE_QUERY = 2;
    public static final int BUTTON_TYPE_EDIT_QUERY = 3;
    public static final int BUTTON_TYPE_SELECT_QUERY = 4;
    public static final int BUTTON_TYPE_SELECT_SECOND_QUERY = 5;
    public static final int BUTTON_TYPE_SELECTTWO_QUERY = 6;
    public static final int BUTTON_TYPE_SELECT_INPUT_QUERY = 7;
    public static final int BUTTON_TYPE_SELECT_SECOND_INPUT_QUERY = 8;
    public static final int BUTTON_TYPE_SELECTTWO_INPUT_QUERY = 9;
    public static final int BUTTON_TYPE_SELECT = 10;

    public static final int BUTTON_ID_QUERY = 0;
    public static final int BUTTON_ID_EDIT = 1;
    public static final int BUTTON_ID_EDIT_SECOND = 2;
    public static final int BUTTON_ID_SELECT = 3;
    public static final int BUTTON_ID_SELECT_SECOND = 4;

    private int m_btn_type = BUTTON_TYPE_EDIT;
    private LinearLayout btn_edit_linearLayout = null;
    private LinearLayout btn_edit_linearLayout_2 = null;
    private TextView btn_edit_name = null;
    private Button btn_edit_query_btn = null;
    private EditText btn_edit_edit_select = null;
    private EditText btn_edit_edit_select_2 = null;
    private TextView btn_edit_select_line = null;
    private TextView btn_edit_select_line_2 = null;
    private TextView btn_edit_display = null;
    private Button btn_edit_select_btn = null;
    private Button btn_edit_select_btn_2 = null;
    private EditText btn_edit_edit_input = null;


    public ButtonEditSelectD(Context context) {
        super( context );
        initView(context);
    }

    public ButtonEditSelectD(Context context, AttributeSet attrs) {
        super( context, attrs );
        initView(context);
    }

    public ButtonEditSelectD(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.ui_base_button_edit_seletct_d_layout, this);
        btn_edit_linearLayout = (LinearLayout)findViewById(R.id.btn_edit_linearLayout);
        btn_edit_linearLayout_2 = (LinearLayout)findViewById(R.id.btn_edit_linearLayout_2);
        btn_edit_name = (TextView)findViewById(R.id.btn_edit_name);
        btn_edit_query_btn = (Button)findViewById(R.id.btn_edit_query_btn);
        btn_edit_query_btn.setOnClickListener(m_ClickListener);

        btn_edit_edit_select = (EditText)findViewById(R.id.btn_edit_edit_select);
        btn_edit_edit_select.setOnClickListener(m_ClickListener);

        btn_edit_edit_select_2 = (EditText)findViewById(R.id.btn_edit_edit_select_2);
        btn_edit_edit_select_2.setOnClickListener(m_ClickListener);

        btn_edit_select_line = (TextView)findViewById(R.id.btn_edit_select_line);
        btn_edit_select_line_2 = (TextView)findViewById(R.id.btn_edit_select_line_2);

        btn_edit_select_btn = (Button)findViewById(R.id.btn_edit_select_btn);
        btn_edit_select_btn.setOnClickListener(m_ClickListener);

        btn_edit_select_btn_2 = (Button)findViewById(R.id.btn_edit_select_btn_2);
        btn_edit_select_btn_2.setOnClickListener(m_ClickListener);

        btn_edit_edit_input = (EditText)findViewById(R.id.btn_edit_edit_input);

        btn_edit_display = (TextView)findViewById(R.id.btn_edit_display);
        // LayoutInflater.from(context).inflate(R.layout.button_edit_layout, this, true);
        // View contentView = View.inflate(context, R.layout.button_edit_layout, null);
        // addView(contentView);
    }

    public void setLayouRatio(float  ratioEdit,  float  ratio) {
        LinearLayout.LayoutParams mLayoutParamsoEdit = (LinearLayout.LayoutParams) btn_edit_edit_select.getLayoutParams();
        mLayoutParamsoEdit.width = 0;
        mLayoutParamsoEdit.weight = ratioEdit;

        btn_edit_edit_select.setLayoutParams(mLayoutParamsoEdit);

        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) btn_edit_select_btn.getLayoutParams();
        mLayoutParams.width = 0;
        mLayoutParams.weight = ratio;
        btn_edit_select_btn.setLayoutParams(mLayoutParams);
    }
    public void setButtonType(int type) {
        btn_edit_query_btn.setBackgroundResource(R.drawable.ui_base_btn_selector);
        if (BUTTON_TYPE_SELECTTWO_INPUT_QUERY == type) {
            btn_edit_linearLayout.setVisibility(VISIBLE);
            btn_edit_linearLayout_2.setVisibility(VISIBLE);
            btn_edit_edit_input.setVisibility(VISIBLE);
        } else if (BUTTON_TYPE_QUERY == type) {
            btn_edit_linearLayout.setVisibility(GONE);
            btn_edit_linearLayout_2.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(GONE);
        } else if (BUTTON_TYPE_EDIT_QUERY == type) {
            btn_edit_linearLayout.setVisibility(GONE);
            btn_edit_linearLayout_2.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(VISIBLE);
        } else if (BUTTON_TYPE_SELECT_QUERY == type) {
            btn_edit_edit_select.setEnabled(false);
            btn_edit_linearLayout_2.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(GONE);
        } else if (BUTTON_TYPE_SELECT_SECOND_QUERY == type) {
            btn_edit_edit_select_2.setEnabled(false);
            btn_edit_linearLayout.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(GONE);
        } else if (BUTTON_TYPE_SELECTTWO_QUERY == type) {
            btn_edit_edit_select.setEnabled(false);
            btn_edit_edit_select_2.setEnabled(false);
            btn_edit_edit_input.setVisibility(GONE);
        } else if (BUTTON_TYPE_SELECT_INPUT_QUERY == type) {
            btn_edit_edit_select.setEnabled(false);
            btn_edit_linearLayout_2.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(VISIBLE);
        } else if (BUTTON_TYPE_SELECT_SECOND_INPUT_QUERY == type) {
            btn_edit_edit_select_2.setEnabled(false);
            btn_edit_linearLayout.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(VISIBLE);
        } else if (BUTTON_TYPE_EDIT == type) {
            btn_edit_linearLayout.setVisibility(GONE);
            btn_edit_linearLayout_2.setVisibility(GONE);
        } else if (BUTTON_TYPE_SELECT == type) {
            btn_edit_edit_select.setEnabled(false);
            btn_edit_linearLayout_2.setVisibility(GONE);
            btn_edit_edit_input.setVisibility(GONE);
            btn_edit_query_btn.setVisibility(GONE);
            btn_edit_display.setVisibility(GONE);
        }
        else {

        }
    }

    public void setButtonQueryText(int resid) {
        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setText(resid);
        }
    }

    public void setButtonQueryText(String text) {
        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setText(text);
        }
    }

    public void setButtonQueryTextColor(String color) {
        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setTextColor(Color.parseColor(color));
        }
    }

    public void setButtonQueryTextSize(int size) {
        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setTextSize(size);
        }
    }

    public void setButtonInputTextSize(int size) {
        if (btn_edit_edit_input != null) {
            btn_edit_edit_input.setTextSize(size);
        }
    }

    public void setButtonSelectText(int resid) {
        if (btn_edit_select_btn != null) {
            btn_edit_select_btn.setText(resid);
        }
    }

    public void setButtonSelectTextSecond(int resid) {
        if (btn_edit_select_btn_2 != null) {
            btn_edit_select_btn_2.setText(resid);
        }
    }


    public void setButtonSelectText(String text) {
        if (btn_edit_select_btn != null) {
            btn_edit_select_btn.setText(text);
        }
    }

    public void setButtonSelectTextSecond(String text) {
        if (btn_edit_select_btn_2 != null) {
            btn_edit_select_btn_2.setText(text);
        }
    }

    public void setButtonSelectTextColor(String color) {
        if (btn_edit_select_btn != null) {
            btn_edit_select_btn.setTextColor(Color.parseColor(color));
        }
    }

    public void setButtonSelectTextColorSecond(String color) {
        if (btn_edit_select_btn_2 != null) {
            btn_edit_select_btn_2.setTextColor(Color.parseColor(color));
        }
    }

    public void setButtonDisplayVisibility(int visibility) {
        if (btn_edit_display != null) {
            btn_edit_display.setVisibility(visibility);
        }
    }

    public void setButtonDisplayText(int resid) {
        if (btn_edit_display != null) {
            btn_edit_display.setText(resid);
        }
    }

    public void setButtonDisplayText(String text) {
        if (btn_edit_display != null) {
            btn_edit_display.setText(text);
        }
    }

    public void setButtonDisplayTextColor(String color) {
        if (btn_edit_display != null) {
            btn_edit_display.setTextColor(Color.parseColor(color));
        }
    }

    public void setButtonDisplayTextSize(int size) {
        if (btn_edit_display != null) {
            btn_edit_display.setTextSize(size);
        }
    }

    public void setButtonNameTextColor(int color) {
        if (btn_edit_name != null) {
            btn_edit_name.setTextColor(color);
        }
    }

    public void setButtonNameTextSize(int size) {
        if (btn_edit_name != null) {
            btn_edit_name.setTextSize(size);
        }
    }

    public void setButtonName(int resid) {
        if (btn_edit_name != null) {
            btn_edit_name.setText(resid);
        }
    }

    public void setButtonName(String text) {
        if (btn_edit_name != null) {
            btn_edit_name.setText(text);
        }
    }

    public void setButtonEditTextSize(int size) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setTextSize(size);
        }
    }

    public void setButtonEditTextSizeSecond(int size) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setTextSize(size);
        }
    }

    /*
    //输入类型为没有指定明确的类型的特殊内容类型
    editText.setInputType(InputType.TYPE_NULL);

    //输入类型为普通文本
    editText.setInputType(InputType.TYPE_CLASS_TEXT);

    //输入类型为数字文本
    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

    //输入类型为电话号码
    editText.setInputType(InputType.TYPE_CLASS_PHONE);

    //输入类型为日期和时间
    editText.setInputType(InputType.TYPE_CLASS_DATETIME);
    ....
     */
    public void setInputType(int type) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setInputType(type);
        }
    }

    public void setInputTypeSecond(int type) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setInputType(type);
        }
    }

    public void setInputTypeInput(int type) {
        if (btn_edit_edit_input != null) {
            btn_edit_edit_input.setInputType(type);
        }
    }

    public void setButtonHintText(int resid) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setHint(resid);
        }
    }

    public void setButtonHintTextSecond(int resid) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setHint(resid);
        }
    }

    public void setButtonText(int resid) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setText(resid);
        }
    }

    public void setButtonTextSecond(int resid) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setText(resid);
        }
    }

    public void setButtonText(String text) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setText(text);
        }
    }

    public void setButtonTextSecond(String text) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setText(text);
        }
    }

    public void setButtonEditEnable(boolean enable) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setEnabled(enable);
        }
    }

    public void setButtonEditEnableSecond(boolean enable) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setEnabled(enable);
        }
    }

    public void setErrText(String err) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setError(err);
        }
    }

    public void setErrTextSecond(String err) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setError(err);
        }
    }

    public EditText getButtonEdit() {
        return btn_edit_edit_select;
    }

    public EditText getButtonEditSecond() {
        return btn_edit_edit_select_2;
    }

    public int getButtonEditSelectVisibility() {
        int iVisibility = View.GONE;
        if (null == btn_edit_edit_select) {
            return iVisibility;
        }
        iVisibility = btn_edit_edit_select.getVisibility();
        return iVisibility;
    }

    public int getButtonEditSelectVisibilitySecond() {
        int iVisibility = View.GONE;
        if (null == btn_edit_edit_select_2) {
            return iVisibility;
        }
        iVisibility = btn_edit_edit_select_2.getVisibility();
        return iVisibility;
    }

    public String getButtonEditText() {
        String strData = "";
        if (null == btn_edit_edit_select) {
            return strData;
        }
        strData = btn_edit_edit_select.getText().toString();
        return strData;
    }

    public String getButtonEditTextSecond() {
        String strData = "";
        if (null == btn_edit_edit_select_2) {
            return strData;
        }
        strData = btn_edit_edit_select_2.getText().toString();
        return strData;
    }

    public String getButtonEditInputText() {
        String strData = "";
        if (null == btn_edit_edit_input) {
            return strData;
        }
        strData = btn_edit_edit_input.getText().toString();
        return strData;
    }

    public void setButtonEditInputText(String text) {
        if (null == btn_edit_edit_input) {
            return;
        }
        btn_edit_edit_input.setText(text);
    }

    public void setButtonEnabled(boolean enable) {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setEnabled(enable);
        }
    }

    public void setButtonEnabledSecond(boolean enable) {
        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setEnabled(enable);
        }
    }

    public void setButtonListener(ButtonListener listener) {
        m_ButtonListener = listener;
    }

    public void removeButtonListener() {
        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setText(null);
            btn_edit_edit_select.setOnClickListener(null);
            btn_edit_edit_select = null;
        }
        if (btn_edit_select_btn != null) {
            btn_edit_select_btn.setText(null);
            btn_edit_select_btn.setOnClickListener(null);
            btn_edit_select_btn = null;
        }

        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setText(null);
            btn_edit_edit_select_2.setOnClickListener(null);
            btn_edit_edit_select_2 = null;
        }
        if (btn_edit_select_btn_2 != null) {
            btn_edit_select_btn_2.setText(null);
            btn_edit_select_btn_2.setOnClickListener(null);
            btn_edit_select_btn_2 = null;
        }

        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setText(null);
            btn_edit_query_btn.setOnClickListener(null);
            btn_edit_query_btn = null;
        }
        if (btn_edit_name != null) {
            btn_edit_name.setOnClickListener(null);
            btn_edit_name = null;
        }

        if (btn_edit_query_btn != null) {
            btn_edit_query_btn.setOnClickListener(null);
            btn_edit_query_btn = null;
        }

        if (btn_edit_edit_select != null) {
            btn_edit_edit_select.setOnClickListener(null);
            btn_edit_edit_select = null;
        }

        if (btn_edit_edit_select_2 != null) {
            btn_edit_edit_select_2.setOnClickListener(null);
            btn_edit_edit_select_2 = null;
        }

        if (btn_edit_select_line != null) {
            btn_edit_select_line.setOnClickListener(null);
            btn_edit_select_line = null;
        }

        if (btn_edit_select_line_2 != null) {
            btn_edit_select_line_2.setOnClickListener(null);
            btn_edit_select_line_2 = null;
        }
        if (btn_edit_display != null) {
            btn_edit_display.setOnClickListener(null);
            btn_edit_display = null;
        }

        if (btn_edit_select_btn != null) {
            btn_edit_select_btn.setOnClickListener(null);
            btn_edit_select_btn = null;
        }

        if (btn_edit_select_btn_2 != null) {
            btn_edit_select_btn_2.setOnClickListener(null);
            btn_edit_select_btn_2 = null;
        }

        if (btn_edit_edit_input != null) {
            btn_edit_edit_input.setOnClickListener(null);
            btn_edit_edit_input = null;
        }

        if (btn_edit_linearLayout != null) {
            btn_edit_linearLayout.setLayoutTransition(null);
            btn_edit_linearLayout = null;
        }

        if (btn_edit_linearLayout_2 != null) {
            btn_edit_linearLayout_2.setLayoutTransition(null);
            btn_edit_linearLayout_2 = null;
        }
        m_ButtonListener = null;
        m_ClickListener = null;
        btn_edit_linearLayout = null;
        btn_edit_linearLayout_2 = null;
        btn_edit_name = null;
        btn_edit_query_btn = null;
        btn_edit_edit_select = null;
        btn_edit_edit_select_2 = null;
        btn_edit_select_line = null;
        btn_edit_select_line_2 = null;
        btn_edit_display = null;
        btn_edit_select_btn = null;
        btn_edit_select_btn_2 = null;
        btn_edit_edit_input = null;
    }

    private ButtonListener m_ButtonListener = null;
    public interface ButtonListener {
        public void onClick(View v, int buttonId);
    }

    private ClickListener m_ClickListener = new ClickListener();
    private class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.btn_edit_query_btn == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonEditSelectD.this,BUTTON_ID_QUERY);
                }
            } else if (R.id.btn_edit_edit_select == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonEditSelectD.this,BUTTON_ID_EDIT);
                }
            } else if (R.id.btn_edit_edit_select_2 == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonEditSelectD.this,BUTTON_ID_EDIT_SECOND);
                }
            } else if (R.id.btn_edit_select_btn == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonEditSelectD.this,BUTTON_ID_SELECT);
                }
            } else if (R.id.btn_edit_select_btn_2 == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonEditSelectD.this,BUTTON_ID_SELECT_SECOND);
                }
            }
            else {

            }
        }
    }
}
