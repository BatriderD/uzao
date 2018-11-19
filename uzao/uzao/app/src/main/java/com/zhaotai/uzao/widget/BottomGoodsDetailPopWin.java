package com.zhaotai.uzao.widget;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.widget.popupwindow.BasePopupWindow;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 商品详情 底部弹框
 */

public class BottomGoodsDetailPopWin extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public BottomGoodsDetailPopWin(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.pop_commodity_detail_type_tt, null);
        return popupView;
    }

    public View getView(){
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.detail_type_back).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_type_back:
                dismiss();
                break;
            default:
                break;
        }
    }
}
