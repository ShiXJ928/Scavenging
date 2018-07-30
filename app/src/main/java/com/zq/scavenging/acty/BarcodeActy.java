package com.zq.scavenging.acty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
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
    private ScanManager mScanManager;
    private int type;
    private Intent intent;
    private TextView tv_time;
    private List<Label> setting;
    private List<Label> list;
    private ListView listView;
    private LabelListAdapter adapter;
    private String barcodeStr = null;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Handler_SHOW_RESULT:
                    if (null != barcodeStr) {
                        String str = barcodeStr;
                        barcodeStr = null;
                        Log.e("---------->", str);
                        addLable(str);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            barcodeStr = new String(barcode, 0, barcodelen);
            Message msg = new Message();
            msg.what = Handler_SHOW_RESULT;
            mHandler.sendMessage(msg);
        }
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
        if (mScanManager == null) {
            mScanManager = new ScanManager();
        }
        mScanManager.openScanner();
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
        if (mScanManager == null) {
            mScanManager = new ScanManager();
        }
        mScanManager.openScanner();
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        registerReceiver(mScanReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (null != mScanManager) {
            mScanManager.closeScanner();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        unregisterReceiver(f4Receiver);
        super.onDestroy();
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

