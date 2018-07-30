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

import com.UHF.scanlable.UfhData;
import com.zq.scavenging.R;
import com.zq.scavenging.adapter.EquipmentListAdapter;
import com.zq.scavenging.bean.EquipmentBean;
import com.zq.scavenging.bean.ShelvesInfo;
import com.zq.scavenging.util.BeepManager;
import com.zq.scavenging.util.LoveDao;
import com.zq.scavenging.util.ToastUtil;
import com.zq.scavenging.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AIERXUAN on 2018/6/22.
 */

public class EquipmentEntryActy extends BaseActy {

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
    private static final int MSG_UPDATE_LISTVIEW = 0;
    private final int Handler_SHOW_RESULT = 1999;
    private Map<String, Long> data;
    private Timer timer;
    private BeepManager beepManager;
    private ScanManager mScanManager;
    private List<EquipmentBean> list;
    private ListView listView;
    private EquipmentListAdapter adapter;
    private Intent intent;
    private String name;
    private TextView tv_save;
    private int type;
    private TextView tv_type;
    private int postionType;
    private List<ShelvesInfo> shelvesInfos;
    private String barcodeStr = null;
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            UfhData.UhfGetData.OpenUhf(57600, (byte) 0x00, 4, 1, null);
            dlg.dismiss();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_utf_entry);

        initView();
    }

    private void initView() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        postionType = intent.getIntExtra("postionType", 0);
        if (postionType == 1) {
            initTitleBar(R.id.title, R.drawable.back, R.drawable.change, Utility.getShelvesName(name), null, R.color.bg_blue, R.color.white);
        } else {
            initTitleBar(R.id.title, R.drawable.back, R.drawable.change, name, null, R.color.bg_blue, R.color.white);
        }
        type = 1;
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_save = (TextView) findViewById(R.id.tv_save);
        if (type == 1) {
            tv_type.setText("当前模式：电子标签");
        } else {
            tv_type.setText("当前模式：条形码");
        }
        tv_save.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        shelvesInfos = new ArrayList<>();
        adapter = new EquipmentListAdapter(this, shelvesInfos);
        listView.setAdapter(adapter);
        beepManager = new BeepManager(this, true, false);
        dlg.show();
        thread.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tb_left:
                finish();
                break;
            case R.id.tb_right:
                stopTimer();
                if (null != mScanManager) {
                    mScanManager.closeScanner();
                }
                if (type == 1) {
                    type = 2;
                    if (mScanManager == null) {
                        mScanManager = new ScanManager();
                    }
                    mScanManager.openScanner();
                    tv_type.setText("当前模式：条形码");
                } else {
                    type = 1;
                    tv_type.setText("当前模式：电子标签");
                    mScanManager.closeScanner();
                }
                break;
            case R.id.tv_save:
                LoveDao.insertLoveList(list);
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_UPDATE_LISTVIEW:
                    //取出扫描的标签号
                    data = UfhData.scanResult6c;
                    if (data.size() != 0) {
                        //判断标签是不是房间编号
//                        String labelID = data.toString();
                        List<String> stringList = new ArrayList<>();
                        String str[] = data.toString().replace("{", "")
                                .replace("}", "").replace(" ", "").split(",");
                        stringList = Arrays.asList(str);
                        int change = 0;
                        for (String string : stringList) {
                            string = string.substring(0, 8);
                            Log.e("----->2", string);
                            int position = 0;
                            for (EquipmentBean bean : list) {
                                if (bean.getName().equals(string)) {
                                    break;
                                }
                                position++;
                            }
                            if (position == list.size() && string.substring(0, 2).equals("06") && !
                                    string.substring(2, 4).equals("FF")) {
                                EquipmentBean equipmentBean = new EquipmentBean();
                                equipmentBean.setName(string);
                                equipmentBean.setNameStr(Utility.getEquipName(string));
                                equipmentBean.setPostion(name);
                                equipmentBean.setType(1);
                                equipmentBean.setPostionType(postionType);
                                list.add(equipmentBean);
                                addData(equipmentBean);
                                change++;
                            }
                        }
                        if (change > 0) {
                            beepManager.setLoop(change - 1);
                            beepManager.play();
                            beepManager.setLoop(0);
                            adapter.notifyDataSetChanged();
                        }
                        UfhData.scanResult6c.clear();
                    }
                    break;
                case Handler_SHOW_RESULT:
                    if (null != barcodeStr) {
                        String str = barcodeStr;
                        barcodeStr = null;
                        Log.e("---------->str", str);
                        int num = 0;
                        for (EquipmentBean bean : list) {
                            if (bean.getName().equals(str)) {
                                break;
                            }
                            num++;
                        }
                        if (num == list.size()) {
                            if (str.substring(0, 2).equals("06") && !str.substring(2, 4).equals("FF")) {
                                EquipmentBean equipmentBean = new EquipmentBean();
                                equipmentBean.setName(str);
                                equipmentBean.setNameStr(Utility.getEquipName(str));
                                equipmentBean.setPostion(name);
                                equipmentBean.setType(2);
                                equipmentBean.setPostionType(postionType);
                                list.add(equipmentBean);
                                addData(equipmentBean);
                                adapter.notifyDataSetChanged();
                                beepManager.play();
                            } else {
                                beepManager.play();
                                ToastUtil.show("标签类型不正确");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }//end of switch
            super.handleMessage(message);
        }
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
            handler.sendMessage(msg);
        }
    };

    private void addData(EquipmentBean bean) {
        int i = 0;
        for (ShelvesInfo info : shelvesInfos) {
            if (info.getName() == bean.getNameStr()) {
                info.setNum(info.getNum() + 1);
                break;
            }
            i++;
        }
        if (i == shelvesInfos.size()) {
            ShelvesInfo info = new ShelvesInfo();
            info.setNum(1);
            info.setName(bean.getNameStr());
            shelvesInfos.add(info);
        }
    }

    //按键的时候
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_SYSRQ) {
            Log.d("UHFMainActivity", "onKeyDown :F12");
            //使能扫描
            if (type == 1) {
                startTimer();
                if (null != mScanManager) {
                    mScanManager.closeScanner();
                }
            } else {
                stopTimer();
                if (null != mScanManager) {
                    mScanManager.openScanner();
                }
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
        if (keyCode == KeyEvent.KEYCODE_SYSRQ) {
            Log.d("UHFMainActivity", "onKeyUp :F12");
            stopTimer();
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mScanManager == null) {
            mScanManager = new ScanManager();
        }
        if (type == 1) {
            mScanManager.closeScanner();
        } else {
            mScanManager.openScanner();
        }
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
    }

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
        super.onDestroy();
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UfhData.read6c(1, 0);
                handler.removeMessages(MSG_UPDATE_LISTVIEW);
                handler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
