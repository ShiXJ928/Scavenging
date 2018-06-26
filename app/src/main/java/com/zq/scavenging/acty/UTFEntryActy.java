package com.zq.scavenging.acty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.zq.scavenging.R;
import com.zq.scavenging.adapter.EquipmentListAdapter;
import com.zq.scavenging.bean.EquipmentBean;
import com.zq.scavenging.util.UfhData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AIERXUAN on 2018/6/22.
 */

public class UTFEntryActy extends BaseActy {

    private static final int MSG_UPDATE_LISTVIEW = 0;
    private Map<String, Integer> data;
    private Timer timer;
    private boolean _isCanceled = true;
    private List<EquipmentBean> list;
    private ListView listView;
    private EquipmentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_utf_entry);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.title, R.drawable.back, 0, "超高频录入", null, R.color.bg_blue, R.color.white);
        listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        adapter = new EquipmentListAdapter(this, list);
        listView.setAdapter(adapter);
        //这个打开RFID阅读器
        int result = UfhData.UhfGetData.OpenUhf(57600, (byte) 0x00, 4, 1, null);

        //打开滴滴的声音
        UfhData.Set_sound(false);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (_isCanceled) {
                    return;
                }
                UfhData.read6c();
                handler.removeMessages(MSG_UPDATE_LISTVIEW);
                handler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
            }
        }, 0, 10);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tb_left:
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
                        for (String string : stringList) {
                            int position = 0;
                            for (EquipmentBean bean : list) {
                                if (bean.getName().equals(string)) {
                                    if (!bean.getPostion().equals(string)) {
                                        bean.setPostion(position + "");
                                        list.set(position, bean);
                                    }
                                    break;
                                }
                                position++;
                            }
                            if (position == list.size()) {
                                list.add(new EquipmentBean(string, string));
                            }
                        }
                        UfhData.scanResult6c.clear();
                    }
                    break;
                default:
                    break;
            }//end of switch
            super.handleMessage(message);
        }
    };

    //按键的时候
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_F12) {
            Log.d("UHFMainActivity", "onKeyDown :F12");
            //使能扫描
            _isCanceled = false;

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
            //关闭扫描
            _isCanceled = true;
            Log.e("----------->", list.size() + "");
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
