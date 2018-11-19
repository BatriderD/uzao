package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ArtWorkBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.design.adapter.MyFilterAdapter;
import com.zhaotai.uzao.ui.design.contract.MyFilterContract;
import com.zhaotai.uzao.ui.design.presenter.MyFilterPresenter;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:滤镜页面
 * author : ZP
 * date: 2017/11/9 0009.
 */

public class MyFilterActivity extends BaseActivity implements MyFilterContract.View {

    private static final String INTENT_MASK_INFO = "intent_mask_info";
    //展示图片
    @BindView(R.id.sqimg)
    public ImageView sq;
    //滤镜列表
    @BindView(R.id.rc_filter)
    public RecyclerView rcFilter;

    private MyFilterPresenter mPresenter;

    private StickerDataInfo info;
    private MyFilterAdapter mAdapter;

    /**
     * 调起本页面
     */

    public static void launch(Context context, StickerDataInfo info) {
        Intent intent = new Intent(context, MyFilterActivity.class);
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

        setContentView(R.layout.activity_filter);

//      设置黑色状态栏
        StatusBarUtil.setColor(this, Color.BLACK);
//        初始化filter
        rcFilter.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//      初始化滤镜
        mPresenter = new MyFilterPresenter(mContext, this);

    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        this.finish();
    }

    @OnClick(R.id.iv_finish)
    public void goFinish() {
        EventBus.getDefault().post(new EventBean<>(info, EventBusEvent.NOTIFIED_STICKER_CHANGED));
        finish();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        info = (StickerDataInfo) intent.getSerializableExtra(INTENT_MASK_INFO);
        mAdapter = new MyFilterAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showLoadingDialog();
                ArtWorkBean artWorkBean = mAdapter.getData().get(position);
                mPresenter.getFilterPic(artWorkBean.getEntryKey(), info.url);
            }
        });

        rcFilter.setAdapter(mAdapter);
        mPresenter.getFilterList(info.url);
        // 显示loading
        showLoadingDialog();
        mPresenter.getCurrentBitmap(info);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageViewBitmap(sq);
    }

    /**
     * recycle控件的图片
     *
     * @param imageView
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

    @Override
    public void showFilterList(List<ArtWorkBean> list) {
        ArtWorkBean artWorkBean = new ArtWorkBean();
        artWorkBean.setDescription("原图");
        list.add(0, artWorkBean);
        mAdapter.setNewData(list);
    }

    @Override
    public void showPic(String url, String type) {
        disMisLoadingDialog();
        if (!StringUtil.isEmpty(url) && !StringUtil.isEmpty(type)) {
            info.filterType = "filter";
            info.filterName = type;
            info.filterPic = url;
            info.isAlph = false;
            mPresenter.getCurrentBitmap(info);
        } else {
            info.filterType = "";
            info.filterName = "";
            info.filterPic = url;
            info.isAlph = false;
            mPresenter.getCurrentBitmap(info);
        }
    }


    /**
     * 显示当前正确的bitmap
     *
     * @param bitmap 图片
     */
    @Override
    public void showCurrentBitmap(Bitmap bitmap) {
        disMisLoadingDialog();
        if (bitmap == null) {
            ToastUtil.showShort("图片显示异常");
        } else {
            releaseImageViewBitmap(sq);
            sq.setImageBitmap(bitmap);
        }
    }
}
