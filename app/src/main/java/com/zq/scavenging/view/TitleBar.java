package com.zq.scavenging.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zq.scavenging.R;

/**
 * Created by SXJ on 2016/10/11.
 */
public class TitleBar extends RelativeLayout {

    public ImageView left;
    public ImageView right;
    public TextView titleTV;
    public TextView tv_right;
    public RelativeLayout tb_rl_title;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_titlebar, this);

        left = (ImageView) findViewById(R.id.tb_left);
        right = (ImageView) findViewById(R.id.tb_right);
        titleTV = (TextView) findViewById(R.id.tb_tv);
        TextPaint tp = titleTV.getPaint();
        tp.setFakeBoldText(true);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tb_rl_title = (RelativeLayout) findViewById(R.id.tb_rl_title);
    }
}
