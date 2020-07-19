package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapp.R;
import com.example.myapplication.utils.Logger;


public class Main1Activity extends AppCompatActivity implements View.OnClickListener {

    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);
        init();
    }

    private void init() {
        Button button = (Button) findViewById(R.id.btn_print_Log);
        button.setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_crash).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TestActivity.class);
        if (v.getId() == R.id.btn_next) {
            startActivity(intent);
        } else if (v.getId() == R.id.btn_crash) {
            intent.putExtra("crash", true);
            startActivity(intent);
            //System.gc();
        } else if (v.getId() == R.id.btn_print_Log) {
            Logger.w("这是一条log日志，测试测试测试 这是一条log日志，测试测试测试" + mIndex++);
            Logger.v("这是一条log日志，测试测试测试 这是一条log日志，测试测试测试" + mIndex++);
            Logger.d("这是一条log日志，测试测试测试 这是一条log日志，测试测试测试" + mIndex++);
            Logger.i("这是一条log日志，测试测试测试 这是一条log日志，测试测试测试" + mIndex++);
            Logger.e("这是一条log日志，测试测试测试 这是一条log日志，测试测试测试" + mIndex++);
        }
    }

}
