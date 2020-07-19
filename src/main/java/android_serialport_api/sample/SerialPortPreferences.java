/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;

import com.com.tcn.sdk.springdemo.controller.VendApplication;
import com.example.myapp.R;
import com.example.myapplication.EquipmentAndManageActivity;
import com.example.myapplication.util.Comment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class SerialPortPreferences extends PreferenceActivity {

	private VendApplication mApplication;
	private SerialPortFinder mSerialPortFinder;
	private ListPreference baudrates;
	private int serial;
	private ListPreference devices;
	private SharedPreferences.Editor editor;
	private SharedPreferences spf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (VendApplication) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		addPreferencesFromResource(R.xml.serial_port_preferences);
		setContentView(R.layout.preference_set);
		configLog();
		//0：读卡器串口  1：现金模块  2：主柜
		serial = getIntent().getIntExtra("serial", 0);
		spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
		editor = spf.edit();
		// Devices
		devices = (ListPreference)findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				devices.setSummary(preference.getSummary());
				Log.d("TAG", "onPreferenceChange: "+ devices.getSummary());
//				Intent intent = new Intent();
//				intent.putExtra("devices", devices.getSummary());
//				intent.putExtra("baudrates",baudrates.getSummary());
				gLogger.debug("选择的串口值："+devices.getSummary());
				gLogger.debug("点击哪个按钮跳转的："+serial);
				if (serial == 0) {
					editor.putString("serialport", (String) devices.getSummary());
					Comment.SERIALPORT = (String) devices.getSummary();
//					setResult(0,intent);
				} else if (serial == 1) {
					editor.putString("serialport1", (String) devices.getSummary());
					Comment.SERIALPORT1 = (String) devices.getSummary();
//					setResult(1,intent);
				} else if (serial == 2) {
					editor.putString("serialport2", (String) devices.getSummary());
					Comment.SERIALPORT2 = (String) devices.getSummary();
//					setResult(2,intent);
				} else if (serial == 3) {
					editor.putString("serialport2_1", (String) devices.getSummary());
					Comment.SERIALPORT2_1 = (String) devices.getSummary();
//					setResult(3,intent);
				} else if (serial == 4) {
					editor.putString("serialport2_2", (String) devices.getSummary());
					Comment.SERIALPORT2_2 = (String) devices.getSummary();
//					setResult(4,intent);
				}
				editor.commit();
				Log.d("TAG", "onPreferenceChange: "+spf.getString("serialport",""));
				gLogger.debug("存储的读卡器串口值："+Comment.SERIALPORT);
				gLogger.debug("存储的主柜串口值："+Comment.SERIALPORT2);
				return true;
			}
		});

		// Baud rates
//		baudrates = (ListPreference)findPreference("BAUDRATE");
//		baudrates.setSummary(baudrates.getValue());
//		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				preference.setSummary((String)newValue);
//				baudrates.setSummary(preference.getSummary());
//				Intent intent = new Intent();
//				intent.putExtra("devices", devices.getSummary());
//				intent.putExtra("baudrates", baudrates.getSummary());
//				setResult(0,intent);
//				Log.d("TAG", "onCreate2: "+intent.getStringExtra("devices")+"++++++"+intent.getStringExtra("baudrates"));
//				return true;
//			}
//		});

//		Intent intent = new Intent();
//		intent.putExtra("devices", devices.getSummary());
////        intent.putExtra("baudrates", baudrates.getSummary());
//		setResult(0,intent);
//        Log.d("TAG", "onCreate: "+intent.getStringExtra("devices")+"++++++"+intent.getStringExtra("baudrates"));
	}
	public void serial_back(View v) {
		Comment.SIGN = 1;
		Intent intent = new Intent(SerialPortPreferences.this, EquipmentAndManageActivity.class);
//		intent.putExtra("sign", 1);
		editor.putInt("sign",1);
		editor.commit();
//        intent.putExtra("baudrates", baudrates.getSummary());
		startActivity(intent);
		gLogger.debug("设置串口页跳转前的sign："+Comment.SIGN);
//		if (serial == 0) {
//			setResult(0,intent);
//		} else if (serial == 1) {
//			setResult(1,intent);
//		} else if (serial == 2) {
//			setResult(2,intent);
//		} else if (serial == 3) {
//			setResult(3,intent);
//		} else if (serial == 4) {
//			setResult(4,intent);
//		}
		this.finish();
	}
	private Logger gLogger;

	public void configLog() {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
		// Set the root log level
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();

		//gLogger = Logger.getLogger(this.getClass());
		gLogger = Logger.getLogger("SerialPortPreferences");
	}
}
