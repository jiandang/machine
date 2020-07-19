package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapplication.util.Comment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.tv_terminalCode)
    TextView tv_terminal_code;
    @InjectView(R.id.btn_update)
    Button btn_update;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        spf = getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        editor = spf.edit();
        editor.putString("ANDROID_ID",ANDROID_ID);
        editor.commit();
        Comment.terminal_code = ANDROID_ID;
        tv_terminal_code.setText(ANDROID_ID);
    }

    @OnClick(R.id.btn_update)
    public void onClick() {
        startActivity(new Intent(GuideActivity.this,InitActivity.class));
        finish();
    }
}
