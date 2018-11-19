package com.zhaotai.uzao.widget;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * 续费/到期/过期提醒弹窗
 * zp
 */
public class RenewalPutAwayDialog extends DialogFragment implements View.OnClickListener {

    private String strContent ="";
    private String title;

    private OnCloseListener listener;
    //0 为过期提示 1为续费
    private int type;
    private String strSure = "确定";

    public static RenewalPutAwayDialog newInstance(int type) {
        Bundle args = new Bundle();
        RenewalPutAwayDialog fragment = new RenewalPutAwayDialog();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_renewal, container);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = (int) (ScreenUtils.getScreenWidth(getActivity()) - 35 * 2 * 2);
            dialogWindow.setAttributes(lp);
        }
    }

    private void initView(View view) {
        TextView contentTxt = (TextView) view.findViewById(R.id.content);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        ImageView ivClose = (ImageView) view.findViewById(R.id.close);
        ImageView ivContent = (ImageView) view.findViewById(R.id.iv_content);

        ivClose.setOnClickListener(this);
        tvSure.setOnClickListener(this);


        contentTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        contentTxt.setText(Html.fromHtml(strContent));

        if (!StringUtil.isEmpty(strSure)) {
            tvSure.setText(strSure);
        }
        if (!StringUtil.isEmpty(title)) {
            titleText.setText(title);
        }

        type = getArguments().getInt("type");
        if (1 == type) {
            ivClose.setVisibility(View.VISIBLE);
            ivContent.setImageResource(R.drawable.ic_renewal);
        } else {
            ivContent.setImageResource(R.drawable.ic_notice);
            ivClose.setVisibility(View.INVISIBLE);
        }

        setCancelable(false);

    }

    public RenewalPutAwayDialog setListener(OnCloseListener listener) {
        this.listener = listener;
        return this;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            //设置弹框的占屏宽        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//    }

    /**
     * 设置文字
     *
     * @param title 设置标题
     * @return this
     */
    public RenewalPutAwayDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置中心文字内容
     *
     * @param contentTxt 文字内容
     * @return this
     */
    public RenewalPutAwayDialog setContentTxt(String contentTxt) {
        strContent = contentTxt;
        return this;
    }

    /**
     * 设置确定按钮文字
     *
     * @param strSure 确定按钮文字
     * @return this
     */
    public RenewalPutAwayDialog setStrSure(String strSure) {
        this.strSure = strSure;
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;

            case R.id.close:
                dismiss();
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(DialogFragment dialog, boolean confirm);
    }
}