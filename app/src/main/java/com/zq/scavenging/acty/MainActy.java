package com.zq.scavenging.acty;

import android.content.Intent;
import android.haobin.utils.Tools;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zq.scavenging.R;
import com.zq.scavenging.util.ToastUtil;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MainActy extends BaseActy {

    private TextView tv_utf, tv_bar_code, tv_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_main);

        initView();
    }

    private void initView() {
        initTitleBar(R.id.title, R.drawable.back, 0, "首页", null, R.color.bg_blue, R.color.white);
        tv_utf = (TextView) findViewById(R.id.tv_utf);
        tv_utf.setOnClickListener(this);
        tv_bar_code = (TextView) findViewById(R.id.tv_bar_code);
        tv_bar_code.setOnClickListener(this);
        tv_file = (TextView) findViewById(R.id.tv_file);
        tv_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_utf:
//                int uhfMode = Tools.checkUHFModelSetting();
//                Log.e("-------->", "uhfMode:" + uhfMode);
                startActivity(new Intent(this, UTFEntryActy.class));
                break;
            case R.id.tv_bar_code:
                break;
            case R.id.tv_file:
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
