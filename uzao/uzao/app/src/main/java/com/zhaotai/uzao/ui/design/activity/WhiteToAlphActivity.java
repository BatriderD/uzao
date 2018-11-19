package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.design.contract.WhiteToAlphlContract;
import com.zhaotai.uzao.ui.design.presenter.WhiteToAlphPresenter;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:白色背景透明化页面
 * author : ZP
 * date: 2017/11/9 0009.
 */

public class WhiteToAlphActivity extends BaseActivity implements WhiteToAlphlContract.View {

    private static final String INTENT_MASK_INFO = "intent_mask_info";
    @BindView(R.id.sqimg)
    public ImageView sq;

    @BindView(R.id.sw)
    public Switch sw;

    private WhiteToAlphPresenter mPresenter;

    private boolean isFirst = true;
    private StickerDataInfo info;

    /**
     * 调起本页面
     */

    public static void launch(Context context, StickerDataInfo info) {
        Intent intent = new Intent(context, WhiteToAlphActivity.class);
        intent.putExtra(INTENT_MASK_INFO, info);
        context.startActivity(intent);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initView() {

        setContentView(R.layout.fix_white_to_alph);

//      设置黑色状态栏
        StatusBarUtil.setColor(this, Color.BLACK);


//       设置开关点击事件
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isFirst) {
                    return;
                }
                showLoadingDialog();
                showCurrentBitmap(isChecked);
            }
        });

        //        创建presenter
        mPresenter = new WhiteToAlphPresenter(this, this);


    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        this.finish();
    }

    /**
     * 完成按钮
     */
    @OnClick(R.id.iv_finish)
    public void goFinish() {
        boolean checked = sw.isChecked();
        EventBean<Boolean> eventBean = new EventBean<>(checked, EventBusEvent.RECEIVE_WHITE_TO_ALPH_URL);
        EventBus.getDefault().post(eventBean);
        disMisLoadingDialog();
        finish();

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        info = (StickerDataInfo) intent.getSerializableExtra(INTENT_MASK_INFO);

        boolean isChanged = info.isAlph;
//         开关显示正确
        sw.setChecked(isChanged);

//        先显示loading
        showLoadingDialog();

//        获得应该显示的图片
        mPresenter.getCurrentBitmap(info, isChanged);
    }

    /**
     * 根据状态显示正确的bitmap
     *
     * @param isChanged 是否透明化
     */
    private void showCurrentBitmap(boolean isChanged) {
        mPresenter.getCurrentBitmap(info, isChanged);
    }


    /**
     * 改变之后的的图片
     * @param bitmap  改变之后图片
     */
    @Override
    public void showChangedBitmap(Bitmap bitmap) {
        isFirst = false;
        if (bitmap == null) {
            ToastUtil.showShort("图片显示异常");
        } else {
            releaseImageViewBitmap(sq);
            sq.setImageBitmap(bitmap);

        }
        disMisLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        disMisLoadingDialog();
        releaseImageViewBitmap(sq);
        super.onDestroy();
    }

    /**
     * 回收控件的图片
     *
     * @param imageView 图片控件
     */
    public void releaseImageViewBitmap(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

}
