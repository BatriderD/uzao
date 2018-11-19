package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.xiaopo.flying.sticker.ColorData;
import com.xiaopo.flying.sticker.ColorMatrixUtils;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.design.contract.StrengthenContract;
import com.zhaotai.uzao.ui.design.presenter.StrengthenPresenter;
import com.zhaotai.uzao.widget.MyMoveAbleSeekBar;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:增强页面
 * author : ZP
 * date: 2017/11/9 0009.
 */

public class StrengthenPicActivity extends BaseActivity implements StrengthenContract.View {

    private static final String COLOR_DATA = "COLOR_DATA";
    private static final String PIC_PATH = "PIC_PATH";
    @BindView(R.id.sqimg)
    public ImageView sq;

    @BindView(R.id.sb_strengthen)
    public MyMoveAbleSeekBar seekBar;

    //增强的父布局
    @BindView(R.id.ll_strengthen_btn)
    public LinearLayout llStrengthenBTNS;


    @BindView(R.id.tv_strengthen_artwork)
    public TextView tvStrengthenArtWork;

    @BindView(R.id.tv_strengthen_brightness)
    public TextView tvStrengthenBrightness;

    @BindView(R.id.tv_strengthen_contrast)
    public TextView tvStrengthenContrast;

    @BindView(R.id.tv_strengthen_saturation)
    public TextView tvStrengthenSaturation;

    @BindView(R.id.tv_strengthen_sx)
    public TextView tvStrengthenSx;

    private UITipDialog mLoadingDialog;
    private StrengthenPresenter mPresenter;

    private String picPath;
    private String resultPath;
    private ColorData colorData;

    /**
     * 调起本页面
     */

    public static void launch(Context context, String picPath, ColorData colorData) {
        Intent intent = new Intent(context, StrengthenPicActivity.class);
        intent.putExtra(PIC_PATH, picPath);
        intent.putExtra(COLOR_DATA, colorData);
        context.startActivity(intent);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_strengthen_pic);

        mPresenter = new StrengthenPresenter(this, this);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (tvStrengthenBrightness.isSelected()) {
                    //更改亮度
                    colorData.setBrightness(progress);

                } else if (tvStrengthenContrast.isSelected()) {
                    //对比度
                    colorData.setContrast(progress);


                } else if (tvStrengthenSaturation.isSelected()) {
                    //饱和度
                    colorData.setSaturation(progress);
                } else if (tvStrengthenSx.isSelected()) {
                    colorData.setHue(progress);
                }
                reflashpic();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        this.finish();
    }

    @OnClick(R.id.iv_finish)
    public void goFinish() {
        EventBean<ColorData> colorDataEventBean = new EventBean<>(colorData, EventBusEvent.DESIGN_MATERIAL_COLOR_DATA);
        EventBus.getDefault().post(colorDataEventBean);
        finish();
    }

    @Override
    protected void initData() {
        picPath = getIntent().getStringExtra(PIC_PATH);
        colorData = (ColorData) getIntent().getSerializableExtra(COLOR_DATA);
        showDialog();
        Glide.with(StrengthenPicActivity.this)
                .load(new File(picPath))
                .skipMemoryCache(true)
                .into(new GlideDrawableImageViewTarget(sq) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作
                        cancleDialog();
                    }
                });
        reflashpic();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    /**
     * 刷新相片
     */
    private void reflashpic() {
        sq.setColorFilter(new ColorMatrixColorFilter(ColorMatrixUtils.handleImageEffect2(colorData.getContrast(), colorData.getSaturation(), colorData.getBrightness(), colorData.getHue())));
    }

    @OnClick({R.id.tv_strengthen_artwork, R.id.tv_strengthen_brightness, R.id.tv_strengthen_contrast, R.id.tv_strengthen_saturation, R.id.tv_strengthen_sx})
    public void changeStrengthen(View view) {
        int childCount = llStrengthenBTNS.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (view == llStrengthenBTNS.getChildAt(i)) {
                llStrengthenBTNS.getChildAt(i).setSelected(true);
            } else {
                llStrengthenBTNS.getChildAt(i).setSelected(false);
            }
        }
        switch (view.getId()) {
            case R.id.tv_strengthen_artwork:
//                原图
                colorData = new ColorData();
                seekBar.setProgress(0);
                seekBar.setMoveAble(false);

                break;
            case R.id.tv_strengthen_brightness:
//                亮度
                seekBar.setMoveAble(true);
                seekBar.setProgress(colorData.getBrightProgress());
                break;
            case R.id.tv_strengthen_contrast:
//                对比度
                seekBar.setMoveAble(true);
                seekBar.setProgress(colorData.getContrastProgress());
                break;
            case R.id.tv_strengthen_saturation:
//                饱和度
                seekBar.setMoveAble(true);
                seekBar.setProgress(colorData.getSaturationProgress());
                break;
            case R.id.tv_strengthen_sx:
//                色相
                seekBar.setMoveAble(true);
                seekBar.setProgress(colorData.getHueProgress());
                break;
        }
        reflashpic();

    }


    private void showDialog() {
        mLoadingDialog = new UITipDialog.Builder(this)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.loading))
                .create();
        mLoadingDialog.show();
    }

    private void cancleDialog() {
        if (mLoadingDialog == null) return;
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
