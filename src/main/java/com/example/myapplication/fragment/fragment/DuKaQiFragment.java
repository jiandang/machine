package com.example.myapplication.fragment.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwin.navy.serialportapi.com_zhongji;
import com.example.myapp.R;
import com.example.myapplication.controller.TcnUtility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 这是读卡器测试页
 */
public class DuKaQiFragment extends Fragment {

    //    @InjectView(R.id.ll_du_ka)
//    LinearLayout ll_duKa;
    @InjectView(R.id.ll_tie_ka)
    LinearLayout ll_tieKa;
    @InjectView(R.id.btn_test)
    Button btn_test;
    @InjectView(R.id.ll_select_fang_shi)
    LinearLayout ll_selectFangShi;
    @InjectView(R.id.iv_tu)
    ImageView iv_tu;
    @InjectView(R.id.tv_zi)
    TextView tv_zi;
    @InjectView(R.id.btn_return)
    Button btn_return;
    @InjectView(R.id.ll_loading)
    LinearLayout ll_loading;
    private com_zhongji zhongjiSerial;
    private SharedPreferences spf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_du_ka_qi, container, false);
        ButterKnife.inject(this, view);
        spf = getActivity().getSharedPreferences("vending_machine", Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.ll_du_ka:
//                ll_duKa.setBackgroundResource(R.drawable.shape_biankuang_3baeed);
//                ll_tieKa.setBackgroundResource(R.drawable.shape_ffffff);
//                break;
//            case R.id.ll_tie_ka:
//                ll_tieKa.setBackgroundResource(R.drawable.shape_biankuang_3baeed);
//                ll_duKa.setBackgroundResource(R.drawable.shape_ffffff);
//                break;
            case R.id.btn_test:
                ll_selectFangShi.setVisibility(View.GONE);
                ll_loading.setVisibility(View.VISIBLE);
                btn_return.setVisibility(View.GONE);
                iv_tu.setVisibility(View.VISIBLE);
                iv_tu.setImageResource(R.drawable.shebeiguanli_zhengzaiceshi);
                tv_zi.setText("正在测试");
//                try {
//                    Thread.sleep(500);
//                    String devices0 = spf.getString("devices0", "");
//                    String baudrates0_1 = spf.getString("baudrates0", "");
//                    Log.d(TAG, "onViewClicked0: " + devices0 + "++++++++" + baudrates0_1);
//                    if (devices0 != null && !devices0.equals("") && !baudrates0_1.equals("")) {
//                        int baudrates0 = Integer.parseInt(baudrates0_1);
//                        SerialPortController.getInstance().openSerialPort(devices0, baudrates0);
////                TcnVendIF.getInstance().reqSlotNoInfoOpenSerialPort();
//                        String sendStr = "0200000010800000000600003C0000BECC01010000D803";
//                        byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
//                        String dataCmd = SerialPortController.getInstance().writeDataII(sendCmd);
//                        Log.d(TAG, "onViewClicked: " + dataCmd);
//                        if (dataCmd != null && !dataCmd.equals("")) {
//                            tv_zi.setText("读卡器连接成功");
//                            iv_tu.setVisibility(View.GONE);
//                            btn_return.setVisibility(View.VISIBLE);
//                        } else {
//                            tv_zi.setText("读卡器连接失败,请重新设置读卡器串口");
//                            iv_tu.setVisibility(View.GONE);
//                            btn_return.setVisibility(View.VISIBLE);
//                        }
//                        SerialPortController.getInstance().closeSerialPort();
//                    } else {
//                        Toast.makeText(getActivity(), "请先设置读卡器串口", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                try {
                    Thread.sleep(500);
                    String serialPort = spf.getString("serialport", "");
                    Log.d("TAG", "onCreateView: " + serialPort);
                    if (serialPort != null&&!serialPort.equals("")) {
                        String substring1 = serialPort.substring(serialPort.length() - 2);
                        zhongjiSerial = com_zhongji.getInstance(substring1);
                        String xunKa = "0200000010800000000600003C0000BECC01010000D803";
                        String checkXunKa = zhongjiSerial.checkXunKa(xunKa);
                        if(checkXunKa != null && !checkXunKa.equals("")){
                            tv_zi.setText("读卡器连接成功");
                            iv_tu.setVisibility(View.GONE);
                            btn_return.setVisibility(View.VISIBLE);
                        }else{
                            tv_zi.setText("读卡器连接失败，请重新设置读卡器串口");
                            iv_tu.setVisibility(View.GONE);
                            btn_return.setVisibility(View.VISIBLE);
                        }
                        zhongjiSerial.closeSerialPort();
                    } else {
                        TcnUtility.getToast(getActivity(),"请先设置读卡器串口");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        ll_loading.setVisibility(View.GONE);
        ll_selectFangShi.setVisibility(View.VISIBLE);

    }
}
