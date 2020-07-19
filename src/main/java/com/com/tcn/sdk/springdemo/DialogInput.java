package com.com.tcn.sdk.springdemo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapp.R;

public class DialogInput extends Dialog {
	public static final int BUTTON_ID_CANCEL    = 1;
	public static final int BUTTON_ID_SURE      = 2;

	public static final int BUTTON_TYPE_INPUT        = 1;
	public static final int BUTTON_TYPE_NO_INPUT      = 2;
	public static final int BUTTON_TYPE_INPUT_ONE        = 3;

	private int m_iBtnType = BUTTON_TYPE_INPUT;

	private int m_iFlag = 0;

	private Context m_Context = null;
	private EditText m_EditText = null;
	private EditText m_EditText_end = null;
	private TextView m_Text = null;
	private TextView dialog_start_text = null;
	private TextView dialog_end_text = null;
	private Button dialog_input_cancel_button = null;
	private Button dialog_input_sure_button = null;

	public DialogInput(Context context) {
		super(context, R.style.ui_base_Dialog_bocop);
		m_Context = context;
		init();
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}


	private void init() {
		View contentView = View.inflate(m_Context, R.layout.ui_base_dialog_input, null);
		setContentView(contentView);
		setCancelable(false);
		m_Text = (TextView)findViewById(R.id.dialog_input_text);
		m_Text.setTextSize(20);
		m_EditText = (EditText)findViewById(R.id.dialog_input_editText);
		m_EditText_end = (EditText)findViewById(R.id.dialog_input_editText_end);

		dialog_start_text = (TextView)findViewById(R.id.dialog_start_text);
		dialog_end_text = (TextView)findViewById(R.id.dialog_end_text);

		dialog_input_cancel_button = (Button)findViewById(R.id.dialog_input_cancel_button);
		dialog_input_cancel_button.setOnClickListener(m_ClickListener);
		dialog_input_cancel_button.setTextSize(20);
		dialog_input_sure_button = (Button)findViewById(R.id.dialog_input_sure_button);
		dialog_input_sure_button.setOnClickListener(m_ClickListener);
		dialog_input_sure_button.setTextSize(20);
		getWindow().setWindowAnimations(Resources.getAnimResourceID(R.anim.ui_base_alpha_in));
	}

	public void deInit() {
		setOnCancelListener(null);
		setOnShowListener(null);
		setOnDismissListener(null);
		if (dialog_input_cancel_button != null) {
			dialog_input_cancel_button.setOnClickListener(null);
			dialog_input_cancel_button = null;
		}
		if (dialog_input_sure_button != null) {
			dialog_input_sure_button.setOnClickListener(null);
			dialog_input_sure_button = null;
		}
		m_Context = null;
		m_EditText = null;
		m_EditText_end = null;
		dialog_start_text = null;
		dialog_end_text = null;
		m_Text = null;
	}

	public void setButtonTiTle(String title) {
		if (m_Text != null) {
			m_Text.setText(title);
		}
	}

	public void setButtonTiTleColor(int color) {
		if (m_Text != null) {
			m_Text.setTextColor(color);
		}
	}

	public void setButtonTiTle(int resId) {
		if (m_Text != null) {
			m_Text.setText(resId);
		}
	}

	public void setButtonTiTleSize(float size) {
		if (m_Text != null) {
			m_Text.setTextSize(size);
		}
	}

	public void setButtonCancelVisiable(boolean visiable) {
		if (visiable) {
			if (dialog_input_cancel_button != null) {
				dialog_input_cancel_button.setVisibility(View.VISIBLE);
			}
		} else {
			if (dialog_input_cancel_button != null) {
				dialog_input_cancel_button.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void setButtonTextSize(float size) {
		if (dialog_input_cancel_button != null) {
			dialog_input_cancel_button.setTextSize(size);
		}
		if (dialog_input_sure_button != null) {
			dialog_input_sure_button.setTextSize(size);
		}
	}

	public void setButtonSureText(String text) {
		if (dialog_input_sure_button != null) {
			dialog_input_sure_button.setText(text);
		}
	}

	public void setButtonCancelText(String text) {
		if (dialog_input_cancel_button != null) {
			dialog_input_cancel_button.setText(text);
		}
	}

	public void setButtonFlag(int flag) {
		m_iFlag = flag;
	}

	public int getButtonFlag() {
		return m_iFlag;
	}

	public void setButtonType(int type) {
		m_iBtnType = type;
		if (type == BUTTON_TYPE_INPUT) {
			if (dialog_start_text != null) {
				dialog_start_text.setVisibility(View.VISIBLE);
			}
			if (dialog_end_text != null) {
				dialog_end_text.setVisibility(View.VISIBLE);
			}
			if (m_EditText != null) {
				m_EditText.setVisibility(View.VISIBLE);
			}
			if (m_EditText_end != null) {
				m_EditText_end.setVisibility(View.VISIBLE);
			}
		} else if (type == BUTTON_TYPE_NO_INPUT) {
			if (dialog_start_text != null) {
				dialog_start_text.setVisibility(View.GONE);
			}
			if (dialog_end_text != null) {
				dialog_end_text.setVisibility(View.GONE);
			}
			if (m_EditText != null) {
				m_EditText.setVisibility(View.GONE);
			}
			if (m_EditText_end != null) {
				m_EditText_end.setVisibility(View.GONE);
			}
		}  else if (type == BUTTON_TYPE_INPUT_ONE) {
			if (dialog_start_text != null) {
				dialog_start_text.setVisibility(View.GONE);
			}
			if (dialog_end_text != null) {
				dialog_end_text.setVisibility(View.GONE);
			}
			if (m_EditText != null) {
				m_EditText.setVisibility(View.VISIBLE);
			}
			if (m_EditText_end != null) {
				m_EditText_end.setVisibility(View.GONE);
			}
		}
		else {

		}
	}

	public int getButtonType() {
		return m_iBtnType;
	}

	public void setButtonInputType(int type) {
		if (m_EditText != null) {
			m_EditText.setInputType(type);
		}
		if (m_EditText_end != null) {
			m_EditText_end.setInputType(type);
		}
	}

	public void setButtonError(String error) {
		if (m_EditText != null) {
			m_EditText.setError(error);
		}
	}

	public void setButtonEndError(String error) {
		if (m_EditText_end != null) {
			m_EditText_end.setError(error);
		}
	}

	public void setButtonText(String text) {
		if (m_EditText != null) {
			m_EditText.setText(text);
		}
	}

	public void setButtonEndText(String text) {
		if (m_EditText_end != null) {
			m_EditText_end.setText(text);
		}
	}

	public void setButtonText(int resId) {
		if (m_EditText != null) {
			m_EditText.setText(resId);
		}
	}

	public void setButtonEndText(int resId) {
		if (m_EditText_end != null) {
			m_EditText_end.setText(resId);
		}
	}



	private ClickListener m_ClickListener = new ClickListener();
	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (null == v) {
				return;
			}
			int id = v.getId();
			String mData = null;
			String mDataSecond = null;

			if ((BUTTON_TYPE_INPUT == m_iBtnType) || (BUTTON_TYPE_INPUT_ONE == m_iBtnType)) {
				if (null == m_EditText) {
					dismiss();
					return;
				}
				mData = m_EditText.getText().toString();
				if (m_EditText_end != null) {
					mDataSecond = m_EditText_end.getText().toString();
				}
				if (R.id.dialog_input_cancel_button == id) {
					if (m_ButtonListener != null) {
						m_ButtonListener.onClick(BUTTON_ID_CANCEL,mData,mDataSecond);
					}
				} else if (R.id.dialog_input_sure_button == id) {
					if (m_ButtonListener != null) {
						m_ButtonListener.onClick(BUTTON_ID_SURE,mData,mDataSecond);
					}
				}
			}
			else {
				if (R.id.dialog_input_cancel_button == id) {
					if (m_ButtonListener != null) {
						m_ButtonListener.onClick(BUTTON_ID_CANCEL,mData,mDataSecond);
					}
				} else if (R.id.dialog_input_sure_button == id) {
					if (m_ButtonListener != null) {
						m_ButtonListener.onClick(BUTTON_ID_SURE,mData,mDataSecond);
					}
				}
			}
		}
	}

	public void setButtonListener(ButtonListener listener) {
		m_ButtonListener = listener;
	}

	private ButtonListener m_ButtonListener = null;

	public interface ButtonListener {
		public void onClick(int buttonId, String firstData, String secondData);
	}
}
