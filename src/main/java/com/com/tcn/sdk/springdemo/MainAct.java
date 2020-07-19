package com.com.tcn.sdk.springdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myapp.R;
import com.tcn.springboard.control.PayMethod;
import com.tcn.springboard.control.TcnShareUseData;
import com.tcn.springboard.control.TcnVendEventID;
import com.tcn.springboard.control.TcnVendEventResultID;
import com.tcn.springboard.control.TcnVendIF;
import com.tcn.springboard.control.VendEventInfo;

public class MainAct extends TcnMainActivity {

	private static final String TAG = "MainAct";
	private static final int CMD_SET_SLOTNO_SPRING     = 32;
	private static final int CMD_SET_SLOTNO_BELTS     = 33;
	private static final int CMD_SET_SLOTNO_ALL_SPRING     = 34;
	private static final int CMD_SET_SLOTNO_ALL_BELT     = 35;
	private static final int CMD_SET_SLOTNO_SINGLE     = 36;
	private static final int CMD_SET_SLOTNO_DOUBLE     = 37;
	private static final int CMD_SET_SLOTNO_ALL_SINGLE     = 38;
	private static final int CMD_SET_TEST_MODE    = 39;
	private static final int CMD_TEMP_CONTROL_SELECT     = 40;

	private int singleitem=0;

	private Titlebar m_Titlebar = null;

	private ButtonEditSelectD menu_spr_query_slot = null;
	private ButtonEditSelectD menu_spr_ship_slot = null;
	private ButtonEditSelectD menu_spr_ship_slot_test = null;
	private ButtonEditSelectD menu_spr_reqselect = null;
	private ButtonEditSelectD menu_spr_reset = null;
	private ButtonEditSelectD menu_spr_set_heat_cool = null;
	// private ButtonEditSelectD menu_spr_set_temp = null;
	private ButtonEditSelectD menu_spr_glass_heat_enable = null;
	private ButtonEditSelectD menu_spr_glass_heat_disable = null;
	private ButtonEditSelectD menu_spr_open_led = null;
	private ButtonEditSelectD menu_spr_close_led = null;
	private ButtonEditSelectD menu_spr_buzzer_open = null;
	private ButtonEditSelectD menu_spr_buzzer_close = null;
	private ButtonEditSelectD menu_spr_self_check = null;
	private ButtonEditSelectD menu_spr_set_slot_spring = null;
	private ButtonEditSelectD menu_spr_set_slot_belts = null;
	private ButtonEditSelectD menu_spr_set_slot_spring_all = null;
	private ButtonEditSelectD menu_spr_set_slot_belts_all = null;
	private ButtonEditSelectD menu_spr_set_single_slot = null;
	private ButtonEditSelectD menu_spr_set_double_slot = null;
	private ButtonEditSelectD menu_spr_set_single_slot_all = null;
	private ButtonEditSelectD menu_spr_test_mode = null;

	private ButtonSwitch menu_spr_light_check = null;

	private LinearLayout menu_spr_set_heat_cool_layout = null;
	private EditText menu_spr_set_heat_cool_temp = null;
	private EditText menu_spr_set_heat_cool_start_time = null;
	private EditText menu_spr_set_heat_cool_end_time = null;
	private DialogInput m_DialogFillCash = null;
	private Button func_coin_prestorage = null;
	protected LinearLayout func_coin_open_layout = null;
	protected LinearLayout func_paper_open_layout = null;
	private EditText func_coin_open_edit = null;
	private Button func_coin_open_btn = null;
	private EditText func_paper_open_edit = null;
	private Button func_paper_open_btn = null;

	private OutDialog m_OutDialog = null;
	private LoadingDialog m_LoadingDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background_menu_settings_layout_spr);
		Log.i(TAG, "MainAct onCreate()");
		initView();
	}

    @Override
	protected void onResume() {
		super.onResume();
		TcnVendIF.getInstance().registerListener(m_vendListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (m_Titlebar != null) {
			m_Titlebar.removeButtonListener();
			m_Titlebar = null;
		}

		if (menu_spr_light_check != null) {
			menu_spr_light_check.removeButtonListener();
			menu_spr_light_check = null;
		}

		if (menu_spr_query_slot != null) {
			menu_spr_query_slot.removeButtonListener();
			menu_spr_query_slot = null;
		}

		if (menu_spr_ship_slot != null) {
			menu_spr_ship_slot.removeButtonListener();
			menu_spr_ship_slot = null;
		}

		if (menu_spr_ship_slot_test != null) {
			menu_spr_ship_slot_test.removeButtonListener();
			menu_spr_ship_slot_test = null;
		}

		if (menu_spr_reqselect != null) {
			menu_spr_reqselect.removeButtonListener();
			menu_spr_reqselect = null;
		}

		if (menu_spr_reset != null) {
			menu_spr_reset.removeButtonListener();
			menu_spr_reset = null;
		}

		if (menu_spr_set_heat_cool != null) {
			menu_spr_set_heat_cool.removeButtonListener();
			menu_spr_set_heat_cool = null;
		}

//        if (menu_spr_set_temp != null) {
//            menu_spr_set_temp.removeButtonListener();
//            menu_spr_set_temp = null;
//        }

		if (menu_spr_glass_heat_enable != null) {
			menu_spr_glass_heat_enable.removeButtonListener();
			menu_spr_glass_heat_enable = null;
		}

		if (menu_spr_glass_heat_disable != null) {
			menu_spr_glass_heat_disable.removeButtonListener();
			menu_spr_glass_heat_disable = null;
		}

		if (menu_spr_open_led != null) {
			menu_spr_open_led.removeButtonListener();
			menu_spr_open_led = null;
		}

		if (menu_spr_close_led != null) {
			menu_spr_close_led.removeButtonListener();
			menu_spr_close_led = null;
		}

		if (menu_spr_buzzer_open != null) {
			menu_spr_buzzer_open.removeButtonListener();
			menu_spr_buzzer_open = null;
		}

		if (menu_spr_buzzer_close != null) {
			menu_spr_buzzer_close.removeButtonListener();
			menu_spr_buzzer_close = null;
		}

		if (menu_spr_self_check != null) {
			menu_spr_self_check.removeButtonListener();
			menu_spr_self_check = null;
		}

		if (menu_spr_set_slot_spring != null) {
			menu_spr_set_slot_spring.removeButtonListener();
			menu_spr_set_slot_spring = null;
		}


		if (menu_spr_set_slot_belts != null) {
			menu_spr_set_slot_belts.removeButtonListener();
			menu_spr_set_slot_belts = null;
		}


		if (menu_spr_set_slot_spring_all != null) {
			menu_spr_set_slot_spring_all.removeButtonListener();
			menu_spr_set_slot_spring_all = null;
		}

		if (menu_spr_set_slot_belts_all != null) {
			menu_spr_set_slot_belts_all.removeButtonListener();
			menu_spr_set_slot_belts_all = null;
		}

		if (menu_spr_set_single_slot != null) {
			menu_spr_set_single_slot.removeButtonListener();
			menu_spr_set_single_slot = null;
		}

		if (menu_spr_set_double_slot != null) {
			menu_spr_set_double_slot.removeButtonListener();
			menu_spr_set_double_slot = null;
		}

		if (menu_spr_set_single_slot_all != null) {
			menu_spr_set_single_slot_all.removeButtonListener();
			menu_spr_set_single_slot_all = null;
		}

		if (menu_spr_test_mode != null) {
			menu_spr_test_mode.removeButtonListener();
			menu_spr_test_mode = null;
		}

		if (func_paper_open_btn != null) {
			func_paper_open_btn.setOnClickListener(null);
			func_paper_open_btn = null;
		}

		if (func_coin_open_btn != null) {
			func_coin_open_btn.setOnClickListener(null);
			func_coin_open_btn = null;
		}

		if (m_DialogFillCash != null) {
			m_DialogFillCash.setButtonListener(null);
			m_DialogFillCash.deInit();
		}
		m_OutDialog = null;
		m_TitleBarListener = null;
		m_vendListener = null;
		m_SwitchButtonListener = null;
		m_ButtonEditClickListener = null;
	}

	private void initView() {

		m_Titlebar = (Titlebar) findViewById(R.id.menu_setttings_titlebar);
		if (m_Titlebar != null) {
			m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
			m_Titlebar.setButtonName(R.string.background_menu_settings);
			m_Titlebar.setTitleBarListener(m_TitleBarListener);
		}

		menu_spr_light_check = (ButtonSwitch) findViewById(R.id.menu_spr_light_check);
		if (menu_spr_light_check != null) {
			menu_spr_light_check.setButtonName(R.string.background_menu_drop_sensor_whole);
			menu_spr_light_check.setButtonListener(m_SwitchButtonListener);
			menu_spr_light_check.setTextSize(TcnVendIF.getInstance().getFitScreenSize(22));
			menu_spr_light_check.setSwitchState(TcnShareUseData.getInstance().isDropSensorCheck());
		}

		menu_spr_query_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_query_slot);
		if (menu_spr_query_slot != null) {
			menu_spr_query_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_query_slot.setButtonName(getString(R.string.background_drive_query_slot));
			menu_spr_query_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_query_slot.setButtonQueryText(getString(R.string.background_drive_query));
			menu_spr_query_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_query_slot.setButtonQueryTextColor("#ffffff");
			menu_spr_query_slot.setButtonDisplayTextColor("#4e5d72");
			menu_spr_query_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_query_slot.setButtonListener(m_ButtonEditClickListener);

		}

		menu_spr_ship_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_ship_slot);
		if (menu_spr_ship_slot != null) {
			menu_spr_ship_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_ship_slot.setButtonName("出货例子");
			menu_spr_ship_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_ship_slot.setButtonQueryText("出货");
			menu_spr_ship_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_ship_slot.setButtonQueryTextColor("#ffffff");
			menu_spr_ship_slot.setButtonDisplayTextColor("#4e5d72");
			menu_spr_ship_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_ship_slot.setButtonListener(m_ButtonEditClickListener);

		}

		menu_spr_ship_slot_test = (ButtonEditSelectD) findViewById(R.id.menu_spr_ship_slot_test);
		if (menu_spr_ship_slot_test != null) {
			menu_spr_ship_slot_test.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_ship_slot_test.setButtonName("测试货道");
			menu_spr_ship_slot_test.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_ship_slot_test.setButtonQueryText("测试");
			menu_spr_ship_slot_test.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_ship_slot_test.setButtonQueryTextColor("#ffffff");
			menu_spr_ship_slot_test.setButtonDisplayTextColor("#4e5d72");
			menu_spr_ship_slot_test.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_ship_slot_test.setButtonListener(m_ButtonEditClickListener);

		}

		menu_spr_reqselect = (ButtonEditSelectD) findViewById(R.id.menu_spr_reqselect);
		if (menu_spr_reqselect != null) {
			menu_spr_reqselect.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_reqselect.setButtonName("选择货道");
			menu_spr_reqselect.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_reqselect.setButtonQueryText("选择");
			menu_spr_reqselect.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_reqselect.setButtonQueryTextColor("#ffffff");
			menu_spr_reqselect.setButtonDisplayTextColor("#4e5d72");
			menu_spr_reqselect.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_reqselect.setButtonListener(m_ButtonEditClickListener);

		}

		menu_spr_self_check = (ButtonEditSelectD) findViewById(R.id.menu_spr_self_check);
		if (menu_spr_self_check != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_self_check.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_self_check.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_self_check.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_self_check.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}

			menu_spr_self_check.setButtonQueryText(getString(R.string.background_spring_self_check));
			menu_spr_self_check.setButtonQueryTextColor("#ffffff");
			menu_spr_self_check.setButtonDisplayTextColor("#4e5d72");
			menu_spr_self_check.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_reset = (ButtonEditSelectD) findViewById(R.id.menu_spr_reset);
		if (menu_spr_reset != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_reset.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_reset.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_reset.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_reset.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_reset.setButtonQueryText(getString(R.string.background_spring_reset));
			menu_spr_reset.setButtonQueryTextColor("#ffffff");
			menu_spr_reset.setButtonDisplayTextColor("#4e5d72");
			menu_spr_reset.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_heat_cool = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_heat_cool);
		if (menu_spr_set_heat_cool != null) {
           /* if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_heat_cool.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECTTWO_QUERY);
                menu_spr_set_heat_cool.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_heat_cool.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_heat_cool.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_SECOND_QUERY);
            }*/
			menu_spr_set_heat_cool.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_SECOND_QUERY);
			menu_spr_set_heat_cool.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_heat_cool.setButtonName(R.string.background_spring_set_heat_cool);
			menu_spr_set_heat_cool.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_heat_cool.setButtonQueryTextColor("#ffffff");
			menu_spr_set_heat_cool.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_heat_cool.setButtonListener(m_ButtonEditClickListener);
		}

        /*menu_spr_set_temp = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_temp);
        if (menu_spr_set_temp != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_temp.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECTTWO_QUERY);
                menu_spr_set_temp.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_temp.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_temp.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_SECOND_QUERY);
            }
            menu_spr_set_temp.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_temp.setButtonName(R.string.spring_set_temp);
            menu_spr_set_temp.setButtonQueryText(getString(R.string.background_drive_set));
            menu_spr_set_temp.setButtonQueryTextColor("#ffffff");
            menu_spr_set_temp.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_temp.setButtonListener(m_ButtonEditClickListener);
        }*/

		menu_spr_set_heat_cool_layout = (LinearLayout) findViewById(R.id.menu_spr_set_heat_cool_layout);
		menu_spr_set_heat_cool_temp = (EditText) findViewById(R.id.menu_spr_set_heat_cool_temp);
		menu_spr_set_heat_cool_temp.setTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
		menu_spr_set_heat_cool_temp.setText(String.valueOf(TcnVendIF.getInstance().getTempControlTemp()));
		menu_spr_set_heat_cool_start_time = (EditText) findViewById(R.id.menu_spr_set_heat_cool_start_time);
		menu_spr_set_heat_cool_start_time.setTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
		menu_spr_set_heat_cool_start_time.setText(String.valueOf(TcnVendIF.getInstance().getTempControlStartTime()));
		menu_spr_set_heat_cool_end_time = (EditText) findViewById(R.id.menu_spr_set_heat_cool_end_time);
		menu_spr_set_heat_cool_end_time.setTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
		menu_spr_set_heat_cool_end_time.setText(String.valueOf(TcnVendIF.getInstance().getTempControlEndTime()));
		if (TcnVendIF.getInstance().getTempControl() == 1) {    //制冷
			menu_spr_set_heat_cool_layout.setVisibility(View.VISIBLE);
			menu_spr_set_heat_cool.getButtonEditSecond().setText(UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[0]);
		} else if (TcnVendIF.getInstance().getTempControl() == 2) { //加热
			menu_spr_set_heat_cool_layout.setVisibility(View.VISIBLE);
			menu_spr_set_heat_cool.getButtonEditSecond().setText(UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[1]);
		} else {
			menu_spr_set_heat_cool_layout.setVisibility(View.GONE);
			menu_spr_set_heat_cool.getButtonEditSecond().setText(UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[2]);
		}

		menu_spr_glass_heat_enable = (ButtonEditSelectD) findViewById(R.id.menu_spr_glass_heat_enable);
		if (menu_spr_glass_heat_enable != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_glass_heat_enable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_glass_heat_enable.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_glass_heat_enable.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_glass_heat_enable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_glass_heat_enable.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_glass_heat_enable.setButtonName(R.string.background_spring_glass_heat_open);
			menu_spr_glass_heat_enable.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_glass_heat_enable.setButtonQueryTextColor("#ffffff");
			menu_spr_glass_heat_enable.setButtonDisplayTextColor("#4e5d72");
			menu_spr_glass_heat_enable.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_glass_heat_disable = (ButtonEditSelectD) findViewById(R.id.menu_spr_glass_heat_disable);
		if (menu_spr_glass_heat_disable != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_glass_heat_disable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_glass_heat_disable.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_glass_heat_disable.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_glass_heat_disable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_glass_heat_disable.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_glass_heat_disable.setButtonName(R.string.background_spring_glass_heat_close);
			menu_spr_glass_heat_disable.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_glass_heat_disable.setButtonQueryTextColor("#ffffff");
			menu_spr_glass_heat_disable.setButtonDisplayTextColor("#4e5d72");
			menu_spr_glass_heat_disable.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_open_led = (ButtonEditSelectD) findViewById(R.id.menu_spr_open_led);
		if (menu_spr_open_led != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_open_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_open_led.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_open_led.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_open_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_open_led.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_open_led.setButtonName(R.string.background_spring_led_open);
			menu_spr_open_led.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_open_led.setButtonQueryTextColor("#ffffff");
			menu_spr_open_led.setButtonDisplayTextColor("#4e5d72");
			menu_spr_open_led.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_close_led = (ButtonEditSelectD) findViewById(R.id.menu_spr_close_led);
		if (menu_spr_close_led != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_close_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_close_led.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_close_led.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_close_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_close_led.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_close_led.setButtonName(R.string.background_spring_led_close);
			menu_spr_close_led.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_close_led.setButtonQueryTextColor("#ffffff");
			menu_spr_close_led.setButtonDisplayTextColor("#4e5d72");
			menu_spr_close_led.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_buzzer_open = (ButtonEditSelectD) findViewById(R.id.menu_spr_buzzer_open);
		if (menu_spr_buzzer_open != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_buzzer_open.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_buzzer_open.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_buzzer_open.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_buzzer_open.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_buzzer_open.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_buzzer_open.setButtonName(R.string.background_spring_buzzer_open);
			menu_spr_buzzer_open.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_buzzer_open.setButtonQueryTextColor("#ffffff");
			menu_spr_buzzer_open.setButtonDisplayTextColor("#4e5d72");
			menu_spr_buzzer_open.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_buzzer_close = (ButtonEditSelectD) findViewById(R.id.menu_spr_buzzer_close);
		if (menu_spr_buzzer_close != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_buzzer_close.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_buzzer_close.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_buzzer_close.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_buzzer_close.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}
			menu_spr_buzzer_close.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_buzzer_close.setButtonName(R.string.background_spring_buzzer_close);
			menu_spr_buzzer_close.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_buzzer_close.setButtonQueryTextColor("#ffffff");
			menu_spr_buzzer_close.setButtonDisplayTextColor("#4e5d72");
			menu_spr_buzzer_close.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_slot_spring = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_spring);
		if (menu_spr_set_slot_spring != null) {
			menu_spr_set_slot_spring.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_set_slot_spring.setButtonName(getString(R.string.background_spring_set_slot_spring));
			menu_spr_set_slot_spring.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_slot_spring.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_slot_spring.setButtonQueryTextColor("#ffffff");
			menu_spr_set_slot_spring.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_slot_spring.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_slot_spring.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_set_slot_spring.setButtonListener(m_ButtonEditClickListener);
		}


		menu_spr_set_slot_belts = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_belts);
		if (menu_spr_set_slot_belts != null) {
			menu_spr_set_slot_belts.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_set_slot_belts.setButtonName(getString(R.string.background_spring_set_slot_belts));
			menu_spr_set_slot_belts.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_slot_belts.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_slot_belts.setButtonQueryTextColor("#ffffff");
			menu_spr_set_slot_belts.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_slot_belts.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_set_slot_belts.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_set_slot_belts.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_slot_spring_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_spring_all);
		if (menu_spr_set_slot_spring_all != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_set_slot_spring_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_set_slot_spring_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_set_slot_spring_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_set_slot_spring_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}

			menu_spr_set_slot_spring_all.setButtonName(getString(R.string.background_spring_set_slot_spring_all));
			menu_spr_set_slot_spring_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_slot_spring_all.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_slot_spring_all.setButtonQueryTextColor("#ffffff");
			menu_spr_set_slot_spring_all.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_slot_spring_all.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_slot_belts_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_belts_all);
		if (menu_spr_set_slot_belts_all != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_set_slot_belts_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_set_slot_belts_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_set_slot_belts_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_set_slot_belts_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}

			menu_spr_set_slot_belts_all.setButtonName(getString(R.string.background_spring_set_slot_belts_all));
			menu_spr_set_slot_belts_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_slot_belts_all.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_slot_belts_all.setButtonQueryTextColor("#ffffff");
			menu_spr_set_slot_belts_all.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_slot_belts_all.setButtonListener(m_ButtonEditClickListener);
		}


		menu_spr_set_single_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_single_slot);
		if (menu_spr_set_single_slot != null) {
			menu_spr_set_single_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_set_single_slot.setButtonName(getString(R.string.background_spring_set_single_slot));
			menu_spr_set_single_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_single_slot.setButtonQueryText(getString(R.string.background_spring_set_single));
			menu_spr_set_single_slot.setButtonQueryTextColor("#ffffff");
			menu_spr_set_single_slot.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_single_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_set_single_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_set_single_slot.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_double_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_double_slot);
		if (menu_spr_set_double_slot != null) {
			menu_spr_set_double_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
			menu_spr_set_double_slot.setButtonName(getString(R.string.background_spring_set_double_slot));
			menu_spr_set_double_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_double_slot.setButtonQueryText(getString(R.string.background_spring_set_double));
			menu_spr_set_double_slot.setButtonQueryTextColor("#ffffff");
			menu_spr_set_double_slot.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_double_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			menu_spr_set_double_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
			menu_spr_set_double_slot.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_set_single_slot_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_single_slot_all);
		if (menu_spr_set_single_slot_all != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_set_single_slot_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_set_single_slot_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_set_single_slot_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_set_single_slot_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}

			menu_spr_set_single_slot_all.setButtonName(getString(R.string.background_spring_set_single_slot_all));
			menu_spr_set_single_slot_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_set_single_slot_all.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_set_single_slot_all.setButtonQueryTextColor("#ffffff");
			menu_spr_set_single_slot_all.setButtonDisplayTextColor("#4e5d72");
			menu_spr_set_single_slot_all.setButtonListener(m_ButtonEditClickListener);
		}

		menu_spr_test_mode = (ButtonEditSelectD) findViewById(R.id.menu_spr_test_mode);
		if (menu_spr_test_mode != null) {
			if (UIComBack.getInstance().isMutiGrpSpring()) {
				menu_spr_test_mode.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
				menu_spr_test_mode.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
				menu_spr_test_mode.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
			} else {
				menu_spr_test_mode.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
			}

			menu_spr_test_mode.setButtonName(getString(R.string.background_spring_test_mode));
			menu_spr_test_mode.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
			menu_spr_test_mode.setButtonQueryText(getString(R.string.background_drive_set));
			menu_spr_test_mode.setButtonQueryTextColor("#ffffff");
			menu_spr_test_mode.setButtonDisplayTextColor("#4e5d72");
			menu_spr_test_mode.setButtonListener(m_ButtonEditClickListener);
		}

		func_coin_prestorage = (Button) findViewById(R.id.func_coin_prestorage);
		func_coin_prestorage.setInputType(InputType.TYPE_CLASS_NUMBER);
		func_coin_prestorage.setText(getString(R.string.background_func_coin_prestorage)+"("+ TcnVendIF.getInstance().getMoneyPreStorage()+")");
		func_coin_prestorage.setBackgroundResource(R.drawable.ui_base_btn_selector);
		func_coin_prestorage.setTextColor(Color.parseColor("#ffffff"));
		func_coin_prestorage.setTextSize(20);
		func_coin_prestorage.setOnClickListener(m_ClickListener);

		func_coin_open_layout = (LinearLayout) findViewById(R.id.func_coin_open_layout);
		if (TcnShareUseData.getInstance().isCashPayOpen()) {   //如果使用现金功能，就打开
			func_coin_open_layout.setVisibility(View.VISIBLE);
			func_coin_open_edit = (EditText) findViewById(R.id.func_coin_open_edit);
			func_coin_open_btn = (Button) findViewById(R.id.func_coin_open_set_btn);
			func_coin_open_btn.setBackgroundResource(R.drawable.ui_base_btn_selector);
			func_coin_open_btn.setTextColor(Color.parseColor("#ffffff"));
			func_coin_open_btn.setOnClickListener(m_ClickListener);
		}


		func_paper_open_layout = (LinearLayout) findViewById(R.id.func_paper_open_layout);
		if (TcnShareUseData.getInstance().isCashPayOpen()) {   //如果使用现金功能，就打开
			func_paper_open_layout.setVisibility(View.VISIBLE);
			func_paper_open_edit = (EditText) findViewById(R.id.func_paper_open_edit);
			func_paper_open_btn = (Button) findViewById(R.id.func_paper_open_set_btn);
			func_paper_open_btn.setBackgroundResource(R.drawable.ui_base_btn_selector);
			func_paper_open_btn.setTextColor(Color.parseColor("#ffffff"));
			func_paper_open_btn.setOnClickListener(m_ClickListener);
		}
	}

	private void showSelectDialog(final int type, String title, final EditText v, String selectData, final String[] str) {
		if (null == str) {
			return;
		}
		int checkedItem = -1;
		if ((selectData != null) && (selectData.length() > 0)) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(selectData)) {
					checkedItem = i;
					break;
				}
			}
		}

		singleitem=0;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setSingleChoiceItems(str, checkedItem, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				singleitem=which;
			}
		});
		builder.setPositiveButton(getString(R.string.background_backgroound_ensure), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				v.setText(str[singleitem]);
				if (CMD_TEMP_CONTROL_SELECT == type) {
					if ((UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[0]).equals(str[singleitem])) {
						menu_spr_set_heat_cool_layout.setVisibility(View.VISIBLE);
					} else if ((UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[1]).equals(str[singleitem])) {
						menu_spr_set_heat_cool_layout.setVisibility(View.VISIBLE);
					} else {
						menu_spr_set_heat_cool_layout.setVisibility(View.GONE);
					}
				}
			}
		});
		builder.setNegativeButton(getString(R.string.background_backgroound_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

			}
		});
		builder.show();
	}

	private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
	private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

		@Override
		public void onClick(View v, int buttonId) {
			if (Titlebar.BUTTON_ID_BACK == buttonId) {
				MainAct.this.finish();
			}
		}
	}

	private void showSetConfirm(final int cmdType,final String grp,final String data1,final String data2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
			builder.setTitle(getString(R.string.background_spring_double_ask));
		} else {
			builder.setTitle(getString(R.string.background_drive_modify_ask));
		}
		builder.setPositiveButton(getString(R.string.background_backgroound_ensure), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				int showTimeOut = 5;
				if (CMD_SET_SLOTNO_SPRING == cmdType) {
					if (TcnVendIF.getInstance().isDigital(data1)) {
						TcnVendIF.getInstance().reqSetSpringSlot(Integer.valueOf(data1));
					}

				} else if (CMD_SET_SLOTNO_BELTS == cmdType) {
					if (TcnVendIF.getInstance().isDigital(data1)) {
						TcnVendIF.getInstance().reqSetBeltsSlot(Integer.valueOf(data1));
					}

				} else if (CMD_SET_SLOTNO_ALL_SPRING == cmdType) {
					if (TcnVendIF.getInstance().isDigital(grp)) {
						TcnVendIF.getInstance().reqSpringAllSlot(Integer.valueOf(grp));
					} else {
						TcnVendIF.getInstance().reqSpringAllSlot(-1);
					}

				} else if (CMD_SET_SLOTNO_ALL_BELT == cmdType) {
					if (TcnVendIF.getInstance().isDigital(grp)) {
						TcnVendIF.getInstance().reqBeltsAllSlot(Integer.valueOf(grp));
					} else {
						TcnVendIF.getInstance().reqBeltsAllSlot(-1);
					}

				} else if (CMD_SET_SLOTNO_SINGLE == cmdType) {
					if (TcnVendIF.getInstance().isDigital(data1)) {
						TcnVendIF.getInstance().reqSingleSlot(Integer.valueOf(data1));
					}
				} else if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
					if (TcnVendIF.getInstance().isDigital(data1)) {
						TcnVendIF.getInstance().reqDoubleSlot(Integer.valueOf(data1));
					}
				} else if (CMD_SET_SLOTNO_ALL_SINGLE == cmdType) {
					if (TcnVendIF.getInstance().isDigital(grp)) {
						TcnVendIF.getInstance().reqSingleAllSlot(Integer.valueOf(grp));
					} else {
						TcnVendIF.getInstance().reqSingleAllSlot(-1);
					}

				} else if (CMD_SET_TEST_MODE == cmdType) {
					showTimeOut = 60*60;
					if (TcnVendIF.getInstance().isDigital(grp)) {
						TcnVendIF.getInstance().reqTestMode(Integer.valueOf(grp));
					} else {
						TcnVendIF.getInstance().reqTestMode(-1);
					}

				}
				else {

				}

				if (m_OutDialog == null) {
					m_OutDialog = new OutDialog(MainAct.this, "", getString(R.string.background_drive_setting));
					m_OutDialog.setShowTime(10000);
				}

				if (m_OutDialog != null) {
					m_OutDialog.setShowTime(showTimeOut);
					m_OutDialog.show();
				}

			}
		});
		builder.setNegativeButton(getString(R.string.background_backgroound_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

			}
		});
		builder.show();
	}

	private SwitchButtonListener m_SwitchButtonListener = new SwitchButtonListener();
	private class SwitchButtonListener implements ButtonSwitch.ButtonListener {

		@Override
		public void onSwitched(View v, boolean isSwitchOn) {
			int iId = v.getId();
			if (R.id.menu_spr_light_check == iId) {
				TcnShareUseData.getInstance().setDropSensorCheck(isSwitchOn);
			} else {

			}
		}
	}

	/***********************  现金模块， 没有现金的请忽略 开始************************/

	private BtnClickListener m_ClickListener = new BtnClickListener();
	private class BtnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (null == v) {
				return;
			}
			int id = v.getId();
			if (R.id.func_coin_open_set_btn == id) {
				String mData = func_coin_open_edit.getText().toString();
				if (TcnVendIF.getInstance().isDigital(mData) && (mData.trim().length() == 16)) {
					TcnVendIF.getInstance().openCoinMoney(mData);
				} else {
					TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_func_paper_coin_open_tips));
				}
			} else if (R.id.func_paper_open_set_btn == id) {
				String mData = func_paper_open_edit.getText().toString();
				if (TcnVendIF.getInstance().isDigital(mData) && (mData.trim().length() == 16)) {
					TcnVendIF.getInstance().openPaperMoney(mData);
				} else {
					TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_func_paper_coin_open_tips));
				}
			} else if (R.id.func_coin_prestorage == id) {
				if (null == m_DialogFillCash) {
					m_DialogFillCash = new DialogInput(MainAct.this);
					m_DialogFillCash.setButtonType(DialogInput.BUTTON_TYPE_NO_INPUT);
					m_DialogFillCash.setButtonTiTle(R.string.background_menu_preparemoney_please);
					m_DialogFillCash.setButtonTextSize(16);
					m_DialogFillCash.setButtonSureText(getString(R.string.background_menu_preparemoney_end));
					m_DialogFillCash.setButtonCancelText(getString(R.string.background_menu_preparemoney_clean));
					m_DialogFillCash.setButtonListener(m_DialogFillCashListener);
				}
				TcnVendIF.getInstance().setCoinPreStorageStart();
				m_DialogFillCash.show();
			}
			else {

			}
		}
	}

	private DialogInputAddShowListener m_DialogFillCashListener = new DialogInputAddShowListener();
	private class DialogInputAddShowListener implements DialogInput.ButtonListener {

		@Override
		public void onClick(int buttonId, String firstData, String secondData) {
			if (buttonId ==  DialogInput.BUTTON_ID_SURE) {
				TcnVendIF.getInstance().setCoinPreStorageEnd();
				if (m_DialogFillCash != null) {
					m_DialogFillCash.dismiss();
				}
			} else if (buttonId ==  DialogInput.BUTTON_ID_CANCEL) {
				TcnVendIF.getInstance().clearCoinPreStorage();
				TcnVendIF.getInstance().setCoinPreStorageEnd();
				func_coin_prestorage.setText(getString(R.string.background_func_coin_prestorage)+"("+TcnVendIF.getInstance().getMoneyPreStorage()+")");
				if (m_DialogFillCash != null) {
					m_DialogFillCash.dismiss();
				}
			} else {

			}
		}
	}

	 /***********************  现金模块， 没有现金的请忽略 结束************************/


	int i = 0;
	private ButtonEditClickListener m_ButtonEditClickListener= new ButtonEditClickListener();
	private class ButtonEditClickListener implements ButtonEditSelectD.ButtonListener {
		@Override
		public void onClick(View v, int buttonId) {
			if (null == v) {
				return;
			}
			if (TcnUtilityUI.isFastClick()) {
				return;
			}
			int id = v.getId();
			if (R.id.menu_spr_query_slot == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					String strParam = menu_spr_query_slot.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						TcnVendIF.getInstance().reqQuerySlotStatus(Integer.valueOf(strParam));
					}
				}
			} else if (R.id.menu_spr_ship_slot == id) {//出货
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					String strParam = menu_spr_ship_slot.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						int slotNo = Integer.valueOf(strParam);//出货的货道号
						String shipMethod = PayMethod.PAYMETHED_WECHAT; //出货方法,微信支付出货，此处自己可以修改。
						String amount = "0.1";    //支付的金额（元）,自己修改
						String tradeNo = "1811020095201811150126888"+i;//支付订单号，每次出货，订单号不能一样，此处自己修改。
						TcnVendIF.getInstance().reqShip(slotNo,shipMethod,amount,tradeNo);
						i++;
					}
				}
			} else if (R.id.menu_spr_ship_slot_test == id) {//测试
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					String strParam = menu_spr_ship_slot_test.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						TcnVendIF.getInstance().reqShipTest(1);
					}
				}
			} else if (R.id.menu_spr_reqselect == id) {//选择货道
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					String strParam = menu_spr_reqselect.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						TcnVendIF.getInstance().reqSelectSlotNo(Integer.valueOf(strParam));
					}
				}
			}
			else if (R.id.menu_spr_self_check == id) {//自检
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_self_check.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_self_check.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSelfCheck(UIComBack.getInstance().getGroupSpringId(strParam));
						}
					} else {
						TcnVendIF.getInstance().reqSelfCheck(-1);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_self_check.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_reset == id) {//复位
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_reset.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_reset.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqReset(UIComBack.getInstance().getGroupSpringId(strParam));
							if (m_OutDialog == null) {
								m_OutDialog = new OutDialog(MainAct.this, "", getString(R.string.background_drive_setting));
								m_OutDialog.setShowTime(10000);
							}
							if (m_OutDialog != null) {
								m_OutDialog.setShowTime(60*10);
								m_OutDialog.show();
							}
						}
					} else {
						TcnVendIF.getInstance().reqReset(-1);
						if (m_OutDialog == null) {
							m_OutDialog = new OutDialog(MainAct.this, "", getString(R.string.background_drive_setting));
							m_OutDialog.setShowTime(10000);
						}
						if (m_OutDialog != null) {
							m_OutDialog.setShowTime(60*10);
							m_OutDialog.show();
						}
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_reset.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_set_heat_cool == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_heat_cool.setButtonDisplayText("");
					String strParamSecond = menu_spr_set_heat_cool.getButtonEditTextSecond();
					if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_lift_tips_select_control_action));
					} else {
						if ((UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[0]).equals(strParamSecond)) {
							String temp = menu_spr_set_heat_cool_temp.getText().toString();
							if (!TcnVendIF.getInstance().isNumeric(temp)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入温度值");
								return;
							}
							String startTime = menu_spr_set_heat_cool_start_time.getText().toString();
							if (!TcnVendIF.getInstance().isDigital(startTime)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入制冷开始时间");
								return;
							}
							String endTime = menu_spr_set_heat_cool_end_time.getText().toString();
							if (!TcnVendIF.getInstance().isDigital(endTime)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入制冷结束时间");
								return;
							}
							Log.i(TAG, "setConfigRfgHeat temp: "+temp+" startTime: "+startTime
									+" endTime: "+endTime);

							TcnVendIF.getInstance().reqOpenCoolSpring(-1,Integer.parseInt(temp),
									Integer.parseInt(startTime),Integer.parseInt(endTime));
						} else if ((UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[1]).equals(strParamSecond)) {
							String temp = menu_spr_set_heat_cool_temp.getText().toString();
							if (!TcnVendIF.getInstance().isNumeric(temp)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入温度值");
								return;
							}
							String startTime = menu_spr_set_heat_cool_start_time.getText().toString();
							if (!TcnVendIF.getInstance().isDigital(startTime)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入加热开始时间");
								return;
							}
							String endTime = menu_spr_set_heat_cool_end_time.getText().toString();
							if (!TcnVendIF.getInstance().isDigital(endTime)) {
								TcnUtilityUI.getToast(MainAct.this, "请填入加热结束时间");
								return;
							}
							TcnVendIF.getInstance().reqHeatSpring(-1,Integer.parseInt(temp),
									Integer.parseInt(startTime),Integer.parseInt(endTime));
						} else if ((UIComBack.HEAT_COOL_OFF_SWITCH_SELECT[2]).equals(strParamSecond)) {
							TcnVendIF.getInstance().reqCloseCoolHeatSpring(-1);
						} else {

						}
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_set_heat_cool.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT_SECOND == buttonId) {
					showSelectDialog(CMD_TEMP_CONTROL_SELECT,getString(R.string.background_lift_tips_select_control_action),menu_spr_set_heat_cool.getButtonEditSecond(), "", UIComBack.HEAT_COOL_OFF_SWITCH_SELECT);
				}
				else {

				}

			}
            /* else if (R.id.menu_spr_set_temp == id) {
               if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_temp.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_temp.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
                        } else {
                            String strParamSecond = menu_spr_set_temp.getButtonEditTextSecond();
                            if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                                TcnUtilityUI.getToast(MainAct.this, getString(R.string.lift_tips_select_control_action));
                            } else {
                                TcnVendIF.getInstance().reqSetTemp(UIComBack.getInstance().getGroupSpringId(strParam),Integer.valueOf(strParamSecond));
                            }

                        }
                    } else {
                        String strParamSecond = menu_spr_set_temp.getButtonEditTextSecond();
                        if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                            TcnUtilityUI.getToast(MainAct.this, getString(R.string.lift_tips_select_control_action));
                        } else {
                            TcnVendIF.getInstance().reqSetTemp(-1,Integer.valueOf(strParamSecond));
                        }
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_set_temp.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT_SECOND == buttonId) {
                    showSelectDialog(-1,getString(R.string.lift_tips_select_control_action),menu_spr_set_temp.getButtonEditSecond(), "", TcnCommon.TEMPERATURE_SELECT);
                }
                else {

                }
            } */
			else if (R.id.menu_spr_glass_heat_enable == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_glass_heat_enable.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_glass_heat_enable.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetGlassHeatEnable(UIComBack.getInstance().getGroupSpringId(strParam),true);
						}
					} else {
						TcnVendIF.getInstance().reqSetGlassHeatEnable(-1,true);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_glass_heat_enable.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_glass_heat_disable == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_glass_heat_disable.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_glass_heat_disable.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetGlassHeatEnable(UIComBack.getInstance().getGroupSpringId(strParam),false);
						}
					} else {
						TcnVendIF.getInstance().reqSetGlassHeatEnable(-1,false);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_glass_heat_disable.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_open_led == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_open_led.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_open_led.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetLedOpen(UIComBack.getInstance().getGroupSpringId(strParam),true);
						}
					} else {
						TcnVendIF.getInstance().reqSetLedOpen(-1,true);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_open_led.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_close_led == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_close_led.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_close_led.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetLedOpen(UIComBack.getInstance().getGroupSpringId(strParam),false);
						}
					} else {
						TcnVendIF.getInstance().reqSetLedOpen(-1,false);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_close_led.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_buzzer_open == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_buzzer_open.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_buzzer_open.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetBuzzerOpen(UIComBack.getInstance().getGroupSpringId(strParam),true);
						}
					} else {
						TcnVendIF.getInstance().reqSetBuzzerOpen(-1,true);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_buzzer_open.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_buzzer_close == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_buzzer_close.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_buzzer_close.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							TcnVendIF.getInstance().reqSetBuzzerOpen(UIComBack.getInstance().getGroupSpringId(strParam),false);
						}
					} else {
						TcnVendIF.getInstance().reqSetBuzzerOpen(-1,false);
					}
				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_buzzer_close.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			}
			else if (R.id.menu_spr_set_slot_spring == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_slot_spring.setButtonDisplayText("");
					String strParam = menu_spr_set_slot_spring.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						showSetConfirm(CMD_SET_SLOTNO_SPRING,"",strParam,"");
					}
				}
			} else if (R.id.menu_spr_set_slot_belts == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_slot_belts.setButtonDisplayText("");
					String strParam = menu_spr_set_slot_belts.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						showSetConfirm(CMD_SET_SLOTNO_BELTS,"",strParam,"");
					}
				}
			} else if (R.id.menu_spr_set_slot_spring_all == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_slot_spring_all.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_set_slot_spring_all.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							showSetConfirm(CMD_SET_SLOTNO_ALL_SPRING,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
						}
					} else {
						showSetConfirm(CMD_SET_SLOTNO_ALL_SPRING,"","","");
					}

				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_set_slot_spring_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_set_slot_belts_all == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_slot_belts_all.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_set_slot_belts_all.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							showSetConfirm(CMD_SET_SLOTNO_ALL_BELT,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
						}
					} else {
						showSetConfirm(CMD_SET_SLOTNO_ALL_BELT,"","","");
					}

				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_set_slot_belts_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_set_single_slot == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_single_slot.setButtonDisplayText("");
					String strParam = menu_spr_set_single_slot.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						showSetConfirm(CMD_SET_SLOTNO_SINGLE,"",strParam,"");
					}

				}
			} else if (R.id.menu_spr_set_double_slot == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_double_slot.setButtonDisplayText("");
					String strParam = menu_spr_set_double_slot.getButtonEditInputText();
					if ((null == strParam) || (strParam.length() < 1)) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_input_slotno));
					} else {
						showSetConfirm(CMD_SET_SLOTNO_DOUBLE,"",strParam,"");
					}
				}
			} else if (R.id.menu_spr_set_single_slot_all == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_set_single_slot_all.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_set_single_slot_all.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							showSetConfirm(CMD_SET_SLOTNO_ALL_SINGLE,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
						}
					} else {
						showSetConfirm(CMD_SET_SLOTNO_ALL_SINGLE,"","","");
					}

				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_set_single_slot_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			} else if (R.id.menu_spr_test_mode == id) {
				if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
					menu_spr_test_mode.setButtonDisplayText("");
					if (UIComBack.getInstance().isMutiGrpSpring()) {
						String strParam = menu_spr_test_mode.getButtonEditText();
						if ((null == strParam) || (strParam.length() < 1)) {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.background_drive_tips_select_cabinetno));
						} else {
							showSetConfirm(CMD_SET_TEST_MODE,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
						}
					} else {
						showSetConfirm(CMD_SET_TEST_MODE,"","","");
					}

				} else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
					showSelectDialog(-1,getString(R.string.background_drive_tips_select_cabinetno),menu_spr_test_mode.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
				} else {

				}
			}
			else {

			}
		}
	}

	/*
	 * 此处监听底层发过来的数据，下面是显示相应操作结果
	 */
	private VendListener m_vendListener = new VendListener();
	public  class VendListener implements TcnVendIF.VendEventListener {
		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			if (null == cEventInfo) {
				TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
				return;
			}
			Log.d(TAG,"VendEvent: " + cEventInfo.m_lParam4);
			Log.d(TAG,"VendEvent: " + cEventInfo.m_iEventID);
			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.CMD_QUERY_SLOT_STATUS:   //查询货道是否有故障  0：正常    255：货道号不存在
					menu_spr_query_slot.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SELF_CHECK:
					menu_spr_self_check.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_RESET:
					menu_spr_reset.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_SPRING:  //设置弹簧货道
					menu_spr_set_slot_spring.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_BELTS:  //设置履带货道
					menu_spr_set_slot_belts.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_ALL_SPRING:  //设置所有货道为弹簧货道
					menu_spr_set_slot_spring_all.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_ALL_BELT: //设置所有货道为履带货道
					menu_spr_set_slot_belts_all.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_SINGLE: //设置为单货道
					menu_spr_set_single_slot.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_DOUBLE: //设置为双货道
					menu_spr_set_double_slot.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.SET_SLOTNO_ALL_SINGLE: //设置所有货道为单货道
					menu_spr_set_single_slot_all.setButtonDisplayText(cEventInfo.m_lParam4);
					if (m_OutDialog != null) {
						m_OutDialog.dismiss();
					}
					break;
				case TcnVendEventID.CMD_SET_COOL: //设置制冷
					menu_spr_set_heat_cool.setButtonDisplayText(cEventInfo.m_lParam4);
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_HEAT: //设置加热
					menu_spr_set_heat_cool.setButtonDisplayText(cEventInfo.m_lParam4);
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_TEMP:
					//menu_spr_set_temp.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_GLASS_HEAT_OPEN: //玻璃加热打开
					menu_spr_glass_heat_enable.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_GLASS_HEAT_CLOSE:
					menu_spr_glass_heat_disable.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_LIGHT_OPEN:
					menu_spr_open_led.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_LIGHT_CLOSE:
					menu_spr_close_led.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_BUZZER_OPEN:
					menu_spr_buzzer_open.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_BUZZER_CLOSE:
					menu_spr_buzzer_close.setButtonDisplayText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_SET_COOL_HEAT_CLOSE://关闭玻璃加热
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.COMMAND_SYSTEM_BUSY:
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
					break;

				case TcnVendEventID.SERIAL_PORT_CONFIG_ERROR:
					Log.i(TAG, "SERIAL_PORT_CONFIG_ERROR");
					//TcnUtilityUI.getToast(m_MainActivity, getString(R.string.error_seriport));
					//打开串口错误，一般是串口配置出错
					break;
				case TcnVendEventID.SERIAL_PORT_SECURITY_ERROR:
					///打开串口错误，一般是串口配置出错
					break;
				case TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR:
					//打开串口错误，一般是串口配置出错
					break;
				case TcnVendEventID.COMMAND_SELECT_GOODS:  //选货成功
					TcnUtilityUI.getToast(MainAct.this, "选货成功");
					break;
				case TcnVendEventID.COMMAND_INVALID_SLOTNO:
					TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_notify_invalid_slot), 22).show();
					break;
				case TcnVendEventID.COMMAND_SOLD_OUT:
					if (cEventInfo.m_lParam1 > 0) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_aisle_name) + cEventInfo.m_lParam1 + getString(R.string.ui_base_notify_sold_out));
					} else {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_notify_sold_out));
					}
					break;
				case TcnVendEventID.COMMAND_FAULT_SLOTNO:
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.COMMAND_SHIPPING:    //正在出货
					if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
						if (m_OutDialog == null) {
							m_OutDialog = new OutDialog(MainAct.this, String.valueOf(cEventInfo.m_lParam1), cEventInfo.m_lParam4);
						} else {
							m_OutDialog.setText(cEventInfo.m_lParam4);
						}
						m_OutDialog.cleanData();
					} else {
						if (m_OutDialog == null) {
							m_OutDialog = new OutDialog(MainAct.this, String.valueOf(cEventInfo.m_lParam1), getString(R.string.ui_base_notify_shipping));
						} else {
							m_OutDialog.setText(MainAct.this.getString(R.string.ui_base_notify_shipping));
						}
					}
					m_OutDialog.setNumber(String.valueOf(cEventInfo.m_lParam1));
					m_OutDialog.show();
					break;

				case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:    //出货成功
					if (null != m_OutDialog) {
						m_OutDialog.cancel();
					}
					if (m_LoadingDialog == null) {
						m_LoadingDialog = new LoadingDialog(MainAct.this, getString(R.string.ui_base_notify_shipment_success), getString(R.string.ui_base_notify_receive_goods));
					} else {
						m_LoadingDialog.setLoadText(getString(R.string.ui_base_notify_shipment_success));
						m_LoadingDialog.setTitle(getString(R.string.ui_base_notify_receive_goods));
					}
					m_LoadingDialog.setShowTime(3);
					m_LoadingDialog.show();
					break;
				case TcnVendEventID.COMMAND_SHIPMENT_FAILURE:    //出货失败
					if (null != m_OutDialog) {
						m_OutDialog.cancel();
					}
					if (null == m_LoadingDialog) {
						m_LoadingDialog = new LoadingDialog(MainAct.this, getString(R.string.ui_base_notify_shipment_fail), getString(R.string.ui_base_notify_contact_merchant));
					}
					m_LoadingDialog.setLoadText(getString(R.string.ui_base_notify_shipment_fail));
					m_LoadingDialog.setTitle(getString(R.string.ui_base_notify_contact_merchant));
					m_LoadingDialog.setShowTime(3);
					m_LoadingDialog.show();
					break;

				case TcnVendEventID.MDB_RECIVE_PAPER_MONEY:
					String strBalance = TcnVendIF.getInstance().getBalance();  //余额
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4 + "元", 20).show();
					break;
				case TcnVendEventID.MDB_RECIVE_COIN_MONEY:
					strBalance = TcnVendIF.getInstance().getBalance();  //余额
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4 + "元", 20).show();
					break;
				case TcnVendEventID.MDB_BALANCE_CHANGE:
					strBalance = TcnVendIF.getInstance().getBalance();  //余额
					break;
				case TcnVendEventID.MDB_PAYOUT_PAPERMONEY:
					if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_START) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_notify_coin_back), 20).show();
					} else if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_END) {
						if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
							TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
						} else {
							TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
						}

					} else {

					}

					break;
				case TcnVendEventID.MDB_PAYOUT_COINMONEY:   //退硬币
					if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_START) {
						TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_notify_coin_back), 20).show();
					} else if (cEventInfo.m_lParam1 == TcnVendEventResultID.MDB_PAYOUT_END) {
						if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
							TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
						} else {
							TcnUtilityUI.getToast(MainAct.this, getString(R.string.ui_base_notify_coinback_success), 20).show();
						}

					} else {

					}
					break;
				case TcnVendEventID.MDB_SHORT_CHANGE_PAPER:
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
					break;
				case TcnVendEventID.MDB_SHORT_CHANGE_COIN:  //确硬币
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
					break;
				case TcnVendEventID.MDB_SHORT_CHANGE:  //找零不足
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4, 20).show();
					break;
				case TcnVendEventID.CMD_READ_DOOR_STATUS:  //门动作上报
					if (TcnVendEventResultID.DO_CLOSE == cEventInfo.m_lParam1) {   //关门
						TcnUtilityUI.getToast(MainAct.this, "关门", 20).show();
					} else if (TcnVendEventResultID.DO_OPEN == cEventInfo.m_lParam1) {   //开门
						TcnUtilityUI.getToast(MainAct.this, "开门", 20).show();
					}
					else {

					}
					break;
				case TcnVendEventID.TEMPERATURE_INFO:   //cEventInfo.m_lParam4  ：温度
//					if (m_main_temperature != null) {
//						m_main_temperature.setText(cEventInfo.m_lParam4);
//					}
					break;
				case TcnVendEventID.PROMPT_INFO:
					TcnUtilityUI.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				default:
					break;
			}
		}
	}
}
