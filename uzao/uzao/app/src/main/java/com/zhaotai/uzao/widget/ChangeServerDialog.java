package com.zhaotai.uzao.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.ToastUtil;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Time: 2018/9/3
 * Created by LiYou
 * Description : 切换服务器dialog
 */
public class ChangeServerDialog extends DialogFragment implements View.OnClickListener, CancelAdapt {

    private static final String EXTRA_ARG_TYPE = "extra_arg_type";
    private CheckBox mCbTest;
    private CheckBox mCbDevelop;
    private CheckBox mCbRelease;

    /**
     * 创建dialog
     *
     * @return
     */
    public static ChangeServerDialog create() {
        return new ChangeServerDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_change_server, container);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCbTest = (CheckBox) view.findViewById(R.id.cb_test);
        mCbDevelop = (CheckBox) view.findViewById(R.id.cb_develop);
        mCbRelease = (CheckBox) view.findViewById(R.id.cb_release);
        TextView mNegativeBtn = (TextView) view.findViewById(R.id.neg_btn);
        TextView mPositiveBtn = (TextView) view.findViewById(R.id.pos_btn);
        mNegativeBtn.setOnClickListener(this);
        mPositiveBtn.setOnClickListener(this);
        mNegativeBtn.setText("取消");
        mPositiveBtn.setText("确定");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Point point = new Point();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getSize(point);
        }
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity = Gravity.CENTER;
            attributes.width = point.x - 110;
            attributes.verticalMargin = 0;
            window.setAttributes(attributes);
            window.setBackgroundDrawableResource(R.drawable.def_dialog_bg);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.neg_btn:
                dismiss();
                break;
            case R.id.pos_btn:
                if (mCbTest.isChecked()) {
                    SPUtils.setSharedStringData(GlobalVariable.BASE_URL, "http://192.168.2.120/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_IMAGE_URL, "http://192.168.2.32/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_DESIGN_URL, "http://192.168.2.120/");
                    LoginHelper.exitLogin((AppCompatActivity) getContext());
                }
                if (mCbDevelop.isChecked()) {
                    SPUtils.setSharedStringData(GlobalVariable.BASE_URL, "http://192.168.2.115/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_IMAGE_URL, "http://192.168.2.32/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_DESIGN_URL, "http://192.168.2.115/");
                    LoginHelper.exitLogin((AppCompatActivity) getContext());
                }
                if (mCbRelease.isChecked()) {
                    SPUtils.setSharedStringData(GlobalVariable.BASE_URL, "http://192.168.2.51/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_IMAGE_URL, "http://192.168.2.32/");
                    SPUtils.setSharedStringData(GlobalVariable.BASE_DESIGN_URL, "http://192.168.2.51/");
                    LoginHelper.exitLogin((AppCompatActivity) getContext());
                }
                dismiss();
                ToastUtil.showShort("切换成功，请重新启动App");
                break;
        }
    }
}
