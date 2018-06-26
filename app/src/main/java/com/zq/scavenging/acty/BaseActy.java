package com.zq.scavenging.acty;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.zq.scavenging.application.MyApplication;
import com.zq.scavenging.view.LoadingView;
import com.zq.scavenging.view.TitleBar;

/**
 * Created by sxj on 2016/10/9.
 */
public class BaseActy extends FragmentActivity implements View.OnClickListener {

    public LoadingView dlg;
    public TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg = new LoadingView(this);
        dlg.setCanceledOnTouchOutside(false);
    }

    protected void initTitleBar(int titleBarId, int leftBtn, int rightBtn, String title, String right, int bgColor, int txtColor) {
        titleBar = (TitleBar) findViewById(titleBarId);
        titleBar.titleTV.setText(title);
        if (leftBtn == 0) {
            titleBar.left.setVisibility(View.GONE);
        } else {
            titleBar.left.setImageResource(leftBtn);
            titleBar.left.setVisibility(View.VISIBLE);
            titleBar.left.setOnClickListener(this);
        }
        if (rightBtn == 0) {
            titleBar.right.setVisibility(View.GONE);
        } else {
            titleBar.right.setImageResource(rightBtn);
            titleBar.right.setVisibility(View.VISIBLE);
            titleBar.right.setOnClickListener(this);
        }

        if (right == null) {
            titleBar.tv_right.setVisibility(View.GONE);
        } else {
            titleBar.tv_right.setText(right);
            titleBar.tv_right.setVisibility(View.VISIBLE);
            titleBar.tv_right.setOnClickListener(this);
        }
        titleBar.tv_right.setTextColor(MyApplication.getContext().getResources().getColor(txtColor));
        titleBar.titleTV.setTextColor(MyApplication.getContext().getResources().getColor(txtColor));
        titleBar.tb_rl_title.setBackgroundColor(MyApplication.getContext().getResources().getColor(bgColor));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
