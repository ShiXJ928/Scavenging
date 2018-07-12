package com.zq.scavenging.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zq.scavenging.R;
import com.zq.scavenging.application.App;
import com.zq.scavenging.util.LoveDao;
import com.zq.scavenging.util.ToastUtil;
import com.zq.scavenging.util.Utility;

/**
 * Created by SXJ on 2018/7/6 14:41
 * E-Mail Address：2394905398@qq.com
 */

public class EntryInfoActy extends BaseActy {

    private EditText et_name;
    private TextView tv_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_entry_info);

        initView();
    }

    private void initView() {
        initTitleBar(R.id.title, R.drawable.back, 0, "盘点人录入", null, R.color.bg_blue, R.color.white);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tb_left:
                finish();
                break;
            case R.id.tv_save:
                if (et_name.getText().toString().replace(" ", "").equals("")) {
                    ToastUtil.show("盘点人不能为空");
                    break;
                }
                App.sharedUtility.setName(et_name.getText().toString());
                App.sharedUtility.setTime(Utility.getNowTime("yyyy-MM-dd HH:mm:ss"));
                LoveDao.deleteLoveAll();
                startActivity(new Intent(EntryInfoActy.this, EntryActy.class));
                finish();
                break;
        }
    }
}
