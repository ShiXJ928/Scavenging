package com.zq.scavenging.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zq.scavenging.R;

public class ChooseTextDialog extends Dialog {
    private TextView sure;
    private TextView cancel;
    private TextView content;
    private TextView title;

    public interface Onclick {
        void sure();

        void cancel();
    }

    private void assignViews() {
        sure = (TextView) findViewById(R.id.sure);
        cancel = (TextView) findViewById(R.id.cancel);
        content = (TextView) findViewById(R.id.text2);
        title = (TextView) findViewById(R.id.title);
    }

    public ChooseTextDialog(Context context, final Onclick onclick, String titlest, String textst, String surest, String cancelst) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dlg_choose_text);
        assignViews();
        title.setText(titlest);
        content.setText(textst);
        sure.setText(surest);
        cancel.setText(cancelst);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onclick.sure();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onclick.cancel();
                dismiss();
            }
        });
        ChooseTextDialog.this.setCanceledOnTouchOutside(true);
    }
}
