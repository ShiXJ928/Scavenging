package com.zq.scavenging.acty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.zq.scavenging.R;
import com.zq.scavenging.adapter.ShelvesInfoAdapter;
import com.zq.scavenging.bean.ShelvesInfo;
import com.zq.scavenging.util.BeepManager;
import com.zq.scavenging.util.LoveDao;
import com.zq.scavenging.util.UfhData;
import com.zq.scavenging.view.ChooseEditDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SXJ on 2018/6/28 17:20
 * E-Mail Address：2394905398@qq.com
 */

public class EntryActy extends BaseActy {

    private static final int MSG_UPDATE_LISTVIEW = 0;
    private Map<String, Integer> data;
    private Timer timer;
    private List<ShelvesInfo> list;
    private ListView listView;
    private ShelvesInfoAdapter adapter;
    private Intent intent;
    private BeepManager beepManager;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_entry);

        initView();
        //数据库读取数据，刷新界面
    }

    private void initView() {
        initTitleBar(R.id.title, R.drawable.back, R.drawable.add, "盘点录入", null, R.color.bg_blue, R.color.white);
        listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        list.addAll(LoveDao.getShelvesInfo());
        adapter = new ShelvesInfoAdapter(this, list);
        listView.setAdapter(adapter);
//        dlg.show();
        //这个打开RFID阅读器
//        UfhData.UhfGetData.OpenUhf(57600, (byte) 0x00, 4, 1, null);
//        dlg.dismiss();
        //打开滴滴的声音
        beepManager = new BeepManager(this, true, false);
        UfhData.Set_sound(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(LoveDao.getShelvesInfo());
        //数据库读取数据，刷新界面
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tb_left:
                finish();
                break;
            case R.id.tb_right:
                new ChooseEditDialog(this, new ChooseEditDialog.Onclick() {
                    @Override
                    public void sure(String nameStr) {
                        intent = new Intent(EntryActy.this, EquipmentEntryActy.class);
                        intent.putExtra("name", nameStr);
                        intent.putExtra("postionType", 2);
//                        startActivityForResult(intent, 101);
                        startActivity(intent);
                    }

                    @Override
                    public void cancel() {

                    }
                }, "新增", "请输入对应的位置信息", "确定", "取消").show();
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
                            Log.e("------->", string);
                            string = string.substring(0, 8);
                            if (string.substring(0, 4).equals("06FF")) {
                                stopTimer();
                                beepManager.play();
                                intent = new Intent(EntryActy.this, EquipmentEntryActy.class);
                                intent.putExtra("name", string);
                                intent.putExtra("postionType", 1);
//                                startActivityForResult(intent, 101);
                                startActivity(intent);
                                break;
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
            if (!UfhData.isDeviceOpen() || first) {
                Log.e("---------->", "open");
                int result = UfhData.UhfGetData.OpenUhf(57600, (byte) 0x00, 4, 1, null);
                first = false;
            }
            startTimer();
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
            stopTimer();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 101 && resultCode == RESULT_OK) {
//            list.clear();
//            list.addAll(LoveDao.getShelvesInfo());
//            //数据库读取数据，刷新界面
//            adapter.notifyDataSetChanged();
//        }
//    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UfhData.read6c();
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
