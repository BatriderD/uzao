package com.zhaotai.uzao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.StringUtil;

public class CustomSureDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;

    public CustomSureDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomSureDialog(Context context, String contenxt) {
        super(context);
        this.content = content;
        this.mContext = context;
    }

    public CustomSureDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CustomSureDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CustomSureDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CustomSureDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public CustomSureDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CustomSureDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    public CustomSureDialog setListener(OnCloseListener cancelListener) {
        this.listener = cancelListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_commom);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        contentTxt = (TextView) findViewById(R.id.content);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        findViewById(R.id.cancel_dialog).setOnClickListener(this);


        contentTxt.setText(content);
        if (!StringUtil.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!StringUtil.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
            case R.id.cancel_dialog:
                dismiss();
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}