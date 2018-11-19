package com.zhaotai.uzao.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.StringUtil;

public class BuyOrPutAwayDialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private boolean enableUp = false;

    public BuyOrPutAwayDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BuyOrPutAwayDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public BuyOrPutAwayDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected BuyOrPutAwayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public BuyOrPutAwayDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public BuyOrPutAwayDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public BuyOrPutAwayDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    public void setEnableUpToSale() {
        this.enableUp = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buy_or_put_away);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setVisibility(!enableUp ? View.VISIBLE : View.INVISIBLE);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);


        contentTxt.setText(content);
        if(!StringUtil.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!StringUtil.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!StringUtil.isEmpty(title)){
            titleTxt.setText(title);
        }
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return (keyCode == KeyEvent.KEYCODE_SEARCH);
            }
        });
        setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
            case R.id.close:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}