package com.zq.scavenging.acty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.haobin.barcode.BarcodeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.zq.scavenging.R;
import com.zq.scavenging.util.BeepManager;

/**
 * Created by AIERXUAN on 2018/6/22.
 */

public class BarCodeEntryActy extends BaseActy {

    private final int Handler_SHOW_RESULT = 1999;
    private final int Handler_Scan = 2000;
    private BeepManager beepManager;
    private BarcodeManager barcodeManager;
    private long nowTime = 0;
    private long lastTime = 0;
    private byte[] codeBuffer;
    private String codeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_bar_code_entry);

        /**
         * 监听橙色按钮按键广播
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jb.action.F4key");
        registerReceiver(f4Receiver, intentFilter);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.title, R.drawable.back, 0, "条形码录入", null, R.color.bg_blue, R.color.white);
        beepManager = new BeepManager(this, true, false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (barcodeManager == null) {
            barcodeManager = BarcodeManager.getInstance();
        }
        barcodeManager.Barcode_Open(this, dataReceived);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        unregisterReceiver(f4Receiver);
        super.onDestroy();
    }

    BarcodeManager.Callback dataReceived = new BarcodeManager.Callback() {

        @Override
        public void Barcode_Read(byte[] buffer, String codeId, int errorCode) {
            // TODO Auto-generated method stub
            if (null != buffer) {
                codeBuffer = buffer;
                BarCodeEntryActy.this.codeId = codeId;
                Message msg = new Message();
                msg.what = Handler_SHOW_RESULT;
                mHandler.sendMessage(msg);
                barcodeManager.Barcode_Stop();
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Handler_SHOW_RESULT:
                    if (null != codeId) {
                        Log.e("---------->codeId", codeId);
                    }
                    if (null != codeBuffer) {
                        String str = new String(codeBuffer);
                        Log.e("---------->str", str);
                        beepManager.play();
                    }
                    break;

                case Handler_Scan:
                    // try {
                    // Thread.sleep(500);
                    // } catch (InterruptedException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    nowTime = System.currentTimeMillis();
                    barcodeManager.Barcode_Stop();
                    // 按键时间不低于200ms
                    if (nowTime - lastTime > 200) {
                        System.out.println("scan(0)");
                        if (null != barcodeManager) {
                            barcodeManager.Barcode_Start();
                        }
                        lastTime = nowTime;
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 捕获扫描物理按键广播
     */
    private BroadcastReceiver f4Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Bundle bundle = intent.getExtras();
            if (intent.hasExtra("F4key")) {
                if (intent.getStringExtra("F4key").equals("down")) {
                    Log.e("trig", "key down");
                    // isContines = true;
                    if (null != barcodeManager) {
                        nowTime = System.currentTimeMillis();

                        if (nowTime - lastTime > 200) {
                            barcodeManager.Barcode_Stop();
                            lastTime = nowTime;
                            if (null != barcodeManager) {
                                barcodeManager.Barcode_Start();
                            }
                        }
                    }
                } else if (intent.getStringExtra("F4key").equals("up")) {
                    Log.e("trig", "key up");
                }
            }
        }
    };
}
