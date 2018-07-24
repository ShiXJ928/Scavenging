package com.zq.scavenging.acty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.zq.scavenging.R;
import com.zq.scavenging.application.App;
import com.zq.scavenging.bean.EquipmentBean;
import com.zq.scavenging.util.FileUtil;
import com.zq.scavenging.util.LoveDao;
import com.zq.scavenging.util.ToastUtil;
import com.zq.scavenging.util.Utility;
import com.zq.scavenging.view.ChooseTextDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MainActy extends BaseActy {

    private LinearLayout ll_entry, ll_file, ll_warehousing, ll_outgoing;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_main);

        initView();
    }

    private void initView() {
        initTitleBar(R.id.title, 0, 0, "警用装备管理", null, R.color.bg_blue, R.color.white);
        ll_entry = (LinearLayout) findViewById(R.id.ll_entry);
        ll_entry.setOnClickListener(this);
        ll_file = (LinearLayout) findViewById(R.id.ll_file);
        ll_file.setOnClickListener(this);
        ll_warehousing = (LinearLayout) findViewById(R.id.ll_warehousing);
        ll_warehousing.setOnClickListener(this);
        ll_outgoing = (LinearLayout) findViewById(R.id.ll_outgoing);
        ll_outgoing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_entry:
                if (App.sharedUtility.getName().equals("")) {
                    intent = new Intent(MainActy.this, EntryInfoActy.class);
                    startActivity(intent);
                } else {
                    new ChooseTextDialog(this, new ChooseTextDialog.Onclick() {

                        @Override
                        public void sure() {
                            intent = new Intent(MainActy.this, EntryActy.class);
                            startActivity(intent);
                        }

                        @Override
                        public void cancel() {
                            intent = new Intent(MainActy.this, EntryInfoActy.class);
                            startActivity(intent);
                        }
                    }, "提示", "是否继续上次盘点,或者新建一次盘点", "继续", "新建").show();
                }
                break;
            case R.id.ll_file:
                List<EquipmentBean> list = new ArrayList<>();
//                list= JSON.parseArray("[{\"id\":4,\"name\":\"06020102\",\"nameStr\":\"警用装备包\",\"postion\":\"qqq\",\"postionType\":2,\"type\":1},{\"id\":5,\"name\":\"06010002\",\"nameStr\":\"警用装具箱\",\"postion\":\"qqq\",\"postionType\":2,\"type\":1},{\"id\":6,\"name\":\"06010001\",\"nameStr\":\"警用装具箱\",\"postion\":\"qqq\",\"postionType\":2,\"type\":1}]", EquipmentBean.class);
                list.addAll(LoveDao.queryAll());
                for (EquipmentBean bean : list) {
                    bean.setId(Long.parseLong(bean.getName().substring(4, 8), 16));
                }
                if (App.sharedUtility.getName().equals("") || list.size() == 0) {
                    ToastUtil.show("盘点数据为空，不能生成盘点文件");
                } else {
                    JSONObject save = new JSONObject();
                    save.put("InventoryName", App.sharedUtility.getName());
                    save.put("InventoryTime", App.sharedUtility.getTime());
                    save.put("EquipList", list);
                    Log.e("-------------->", save.toJSONString());
                    Log.e("-------------->", save.toString());
                    FileUtil.saveFile(save.toJSONString(), "/sdcard/盘点文件/", "盘点文件" + App.sharedUtility.getName() +
                            Utility.getTimeStr(App.sharedUtility.getTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss") + ".txt");
                }
                App.sharedUtility.setName("");
                App.sharedUtility.setTime("");
                LoveDao.deleteLoveAll();
                break;
            case R.id.ll_warehousing:
                intent = new Intent(MainActy.this, BarcodeActy.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.ll_outgoing:
                intent = new Intent(MainActy.this, BarcodeActy.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                ToastUtil.show("再按一次退出程序");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
