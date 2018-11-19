package com.zhaotai.uzao.ui.design.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ToastUtil;

public class EditorChangeTextDialog extends Dialog implements View.OnClickListener {

    private String mText;
    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_save;

    public EditText et_input;

    public TextView tv_cancel;

    public TextView tv_sure;


    private View.OnClickListener mClickListener;
    private OnTextchangeListener listener;

    public EditorChangeTextDialog(Activity context, String text) {
        super(context);
        this.mText = text;
        this.context = context;
    }

    public EditorChangeTextDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_editor_input);

        et_input = (EditText) findViewById(R.id.et_editor_input);
        tv_cancel = (TextView) findViewById(R.id.cancel);
        tv_sure = (TextView) findViewById(R.id.sure);

        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);

            /*
             * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() - PixelUtil.dp2px(24)); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        et_input.setText(mText);
        et_input.setSelection(mText.length());
        this.setCancelable(true);
    }

    public void setText(String text) {
        mText = text;
        et_input.setText(mText);
        et_input.setSelection(mText.length());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sure:
                if (listener != null) {
                    String text = et_input.getText().toString();
                    if (et_input.getText().length() > 0) {
                        listener.onChangeText(this, text);
                    } else {
                        ToastUtil.showShort("请输入合适的文字");
                    }
                }

                break;
            case R.id.cancel:
                if (listener != null) {
                    listener.onCancel(this);
                }
                break;
        }
        this.dismiss();
    }

    public EditorChangeTextDialog setListener(OnTextchangeListener cancelListener) {
        this.listener = cancelListener;
        return this;
    }

    public interface OnTextchangeListener {
        void onChangeText(Dialog dialog, String Text);

        void onCancel(Dialog dialog);
    }
}