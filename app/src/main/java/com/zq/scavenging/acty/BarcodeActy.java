package com.zq.scavenging.acty;

import android.content.Intent;
import android.haobin.barcode.BarcodeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zq.scavenging.R;
import com.zq.scavenging.adapter.LabelListAdapter;
import com.zq.scavenging.bean.Label;
import com.zq.scavenging.util.BeepManager;
import com.zq.scavenging.util.FileUtil;
import com.zq.scavenging.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SXJ on 2018/7/19 11:08
 * E-Mail Address：2394905398@qq.com
 */

public class BarcodeActy extends BaseActy {

    private final int Handler_SHOW_RESULT = 1999;
    private BeepManager beepManager;
    private BarcodeManager barcodeManager;
    private byte[] codeBuffer;
    private int isFirst;
    private int type;
    private Intent intent;
    private TextView tv_time;
    private List<Label> setting;
    private List<Label> list;
    private ListView listView;
    private LabelListAdapter adapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Handler_SHOW_RESULT:
                    if (null != codeBuffer) {
                        String str = new String(codeBuffer);
                        addLable(str);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_barcode);

        initView();
    }

    private void initView() {
        intent = getIntent();
        type = intent.getIntExtra("type", 1);
        tv_time = (TextView) findViewById(R.id.tv_time);
        if (type == 1) {
            initTitleBar(R.id.title, R.drawable.back, 0, "入库", "保存", R.color.bg_blue, R.color.white);
            tv_time.setText("入库时间");
        } else {
            initTitleBar(R.id.title, R.drawable.back, 0, "出库", "保存", R.color.bg_blue, R.color.white);
            tv_time.setText("出库时间");
        }
        beepManager = new BeepManager(this, true, false);
        if (barcodeManager == null) {
            barcodeManager = BarcodeManager.getInstance();
        }
        barcodeManager.Barcode_Open(this, dataReceived);
        setting = JSON.parseArray(FileUtil.loadFromSDFile("setting.txt"), Label.class);
        list = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        adapter = new LabelListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tb_left:
                finish();
                break;
            case R.id.tv_right:
                save();
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (barcodeManager == null) {
            barcodeManager = BarcodeManager.getInstance();
        }
        barcodeManager.Barcode_Open(this, dataReceived);
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (null != barcodeManager) {
            barcodeManager.Barcode_Close();
            barcodeManager.Barcode_Stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        unregisterReceiver(f4Receiver);
        super.onDestroy();
    }

    BarcodeManager.Callback dataReceived = new BarcodeManager.Callback() {

        @Override
        public void Barcode_Read(byte[] buffer, String codeId, int errorCode) {
            // TODO Auto-generated method stub
            if (null != buffer) {
                codeBuffer = buffer;
                Message msg = new Message();
                msg.what = Handler_SHOW_RESULT;
                mHandler.sendMessage(msg);
                barcodeManager.Barcode_Stop();
                Log.e("---->", "stop");
            }
        }
    };

    //按键的时候
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_F12) {
            Log.d("UHFMainActivity", "onKeyDown :F12");
            if (null != barcodeManager && isFirst == 0) {
                barcodeManager.Barcode_Start();
                isFirst = 1;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    //松开按钮的时候
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_F12) {
            Log.d("UHFMainActivity", "onKeyUp :F12");
            if (null != barcodeManager) {
                barcodeManager.Barcode_Stop();
            }
            isFirst = 0;
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void addLable(String str) {
        int position = 0;
        for (Label bean : list) {
            if (bean.getLabelStr().equals(str)) {
                break;
            }
            position++;
        }
        if (position == list.size()) {
            Label label = new Label();
            label.setLabelStr(str);
            label.setTime(Utility.getNowTime("yyyy-MM-dd HH:mm:ss"));
            int position1 = 0;
            for (Label bean : setting) {
                if (bean.getLabelStr().equals(str)) {
                    label.setName(bean.getName());
                    break;
                }
                position1++;
            }
            if (position1 == setting.size()) {
                label.setName("其他");
            }
            beepManager.play();
            list.add(label);
            adapter.notifyDataSetChanged();
        }
    }

    private void save() {
        JSONArray post = (JSONArray) JSON.toJSON(list);
        if (type == 1) {
            FileUtil.saveFile(post.toJSONString(), "/sdcard/出入库/", "入库-" + Utility.getNowTime("yyyyMMddHHmm") + ".txt");
        } else {
            FileUtil.saveFile(post.toJSONString(), "/sdcard/出入库/", "出库-" + Utility.getNowTime("yyyyMMddHHmm") + ".txt");
        }
    }
}

