package com.com.tcn.sdk.springdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.example.myapp.R;

public class LoadingDialog extends Dialog {
	private static final int CHANGE_TITLE_WHAT = 1;
	private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
	private static final int MAX_SUFFIX_NUMBER = 3;
	private static final char SUFFIX = '.';

	private int timeCount = 0;
	private int m_iMaxTime = 60000;
	private boolean cancelable = true;

	private Context m_Context = null;
	private TextView tv;
	private TextView load_text;
	private RotateAnimation mAnim;


	private Handler handler = new Handler(){
		private int num = 0;

		public void handleMessage(android.os.Message msg) {
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

				if (isShowing()) {
					handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
				} else {
					timeCount = 0;
					num = 0;
				}
			}
		};
	};

	public LoadingDialog(Context context, String load, String tv) {
		super(context, R.style.ui_base_Dialog_bocop);
		m_Context = context;
		init(load, tv);
	}

	private void init(String load, String tv1) {
		View contentView = View.inflate(m_Context, ResourceUtil.getLayoutId(m_Context, "app_activity_custom_loding_dialog_layout"), null);
		//contentView.setBackgroundResource(R.drawable.dialoback);
		setContentView(contentView);
		setCancelable(false);
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelable) {
					dismiss();
				}
			}
		});
		tv = (TextView) findViewById(ResourceUtil.getId(m_Context, "tv"));
		tv.setText(tv1);
		load_text=(TextView) findViewById(ResourceUtil.getId(m_Context, "load_text"));
		load_text.setText(load);
//		initAnim();
		getWindow().setWindowAnimations(Resources.getAnimResourceID(R.anim.ui_base_alpha_in));
	}

	public void deInit() {
		setShowTime(0);
		setOnCancelListener(null);
		setOnShowListener(null);
		setOnDismissListener(null);
		if (mAnim != null) {
			mAnim.cancel();
			mAnim = null;
		}
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		tv = null;
		load_text = null;
		m_Context = null;
	}

	private void initAnim() {
		mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
		mAnim.setDuration(2000);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setRepeatMode(Animation.RESTART);
		mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
	}

	@Override
	public void show() {
		super.show();
		timeCount = 0;
		handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
	}

	@Override
	public void dismiss() {
//		mAnim.cancel();
		super.dismiss();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void cancel() {
		super.cancel();
		handler.removeCallbacksAndMessages(null);
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

	public void setLoadText(String str) {
		if (load_text != null) {
			load_text.setText(str);
		}

	}

	public void setShowTime(int second) {
		timeCount = 0;
		m_iMaxTime = second * 1000;
	}
}
