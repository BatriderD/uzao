package com.zhaotai.uzao.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.StringUtil;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Time: 2018/8/27 0027
 * Created by LiYou
 * Description : 选择商品数量dialog
 */
public class SelectGoodsCountDialog extends DialogFragment implements View.OnClickListener, CancelAdapt, TextWatcher {

    private OnChangeCountListener mListener;

    public static final String TAG = "SelectGoodsCountDialog";

    private static final String EXTRA_ARG_COUNT = "extra_arg_count";
    private String count;
    private EditText mEtSpuNum;


    public static SelectGoodsCountDialog create(String count) {
        Bundle args = new Bundle();
        SelectGoodsCountDialog fragment = new SelectGoodsCountDialog();
        args.putString(EXTRA_ARG_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(
                        getActivity().getWindow().getDecorView().getWindowToken(), 0);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_select_goods_count, container);
        initView(view);
        return view;
    }

    private void initView(View view) {
        count = getArguments().getString(EXTRA_ARG_COUNT);
        mEtSpuNum = (EditText) view.findViewById(R.id.dialog_shopping_cart_spu_num);
        mEtSpuNum.setText(count);
        mEtSpuNum.setSelection(count.length());
        TextView mNegativeBtn = (TextView) view.findViewById(R.id.neg_btn);
        TextView mPositiveBtn = (TextView) view.findViewById(R.id.pos_btn);
        mNegativeBtn.setOnClickListener(this);
        mPositiveBtn.setOnClickListener(this);
        mNegativeBtn.setText("取消");
        mPositiveBtn.setText("确定");

        view.findViewById(R.id.dialog_shopping_cart_spu_sub).setOnClickListener(this);
        view.findViewById(R.id.dialog_shopping_cart_spu_add).setOnClickListener(this);
        mEtSpuNum.addTextChangedListener(this);
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

    public SelectGoodsCountDialog setCountListener(OnChangeCountListener onChangeCountListener) {
        this.mListener = onChangeCountListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (StringUtil.isEmpty(count)) return;
        switch (v.getId()) {
            case R.id.dialog_shopping_cart_spu_sub:
                int intCount = Integer.valueOf(count);
                if (intCount > 1) {
                    count = String.valueOf(intCount - 1);
                    mEtSpuNum.setText(count);
                    mEtSpuNum.setSelection(count.length());
                }
                break;
            case R.id.dialog_shopping_cart_spu_add:
                count = String.valueOf(Integer.valueOf(count) + 1);
                mEtSpuNum.setText(count);
                mEtSpuNum.setSelection(count.length());
                break;
            case R.id.neg_btn:
                dismiss();
                break;
            case R.id.pos_btn:
                dismiss();
                mListener.onChangeCountListener(mEtSpuNum.getText().toString());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            this.count = s.toString();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnChangeCountListener {
        void onChangeCountListener(String count);
    }
}
