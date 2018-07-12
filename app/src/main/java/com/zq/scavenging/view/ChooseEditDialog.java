package com.zq.scavenging.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zq.scavenging.R;

public class ChooseEditDialog extends Dialog {
    private TextView sure;
    private TextView cancel;
    private EditText content;
    private TextView title;

    public interface Onclick {
        void sure(String nameStr);

        void cancel();
    }

    private void assignViews() {
        sure = (TextView) findViewById(R.id.sure);
        cancel = (TextView) findViewById(R.id.cancel);
        content = (EditText) findViewById(R.id.text2);
        title = (TextView) findViewById(R.id.title);
    }

    public ChooseEditDialog(Context context, final Onclick onclick, String titlest, String textst, String surest, String cancelst) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dlg_choose_edit);
        assignViews();
        title.setText(titlest);
        content.setHint(textst);
        sure.setText(surest);
        cancel.setText(cancelst);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onclick.sure(content.getText().toString());
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
        ChooseEditDialog.this.setCanceledOnTouchOutside(true);
    }
}
