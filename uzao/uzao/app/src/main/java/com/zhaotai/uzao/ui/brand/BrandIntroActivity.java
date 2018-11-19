package com.zhaotai.uzao.ui.brand;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;

import butterknife.BindView;

/**
 * Time: 2018/1/24
 * Created by LiYou
 * Description : 品牌简介详情
 */

public class BrandIntroActivity extends BaseActivity {

    @BindView(R.id.iv_brand_head)
    ImageView mBrandHead;
    @BindView(R.id.tv_brand_all_name)
    TextView mBrandAllName;
    @BindView(R.id.tv_brand_intro)
    TextView mBrandIntro;
    @BindView(R.id.iv_bg)
    ImageView mBg;

    private BrandBean brand;

    public static void launch(Context context, BrandBean brand) {
        Intent intent = new Intent(context, BrandIntroActivity.class);
        intent.putExtra("brand", brand);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_intro);
    }

    @Override
    protected void initData() {
        brand = (BrandBean) getIntent().getSerializableExtra("brand");

        //品牌logo
        GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + brand.brandLogo, mBrandHead, R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(this));

        mBrandAllName.setText(brand.brandName);

        mBrandIntro.setText(brand.brandAbout);
        GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + brand.brandCover, mBg);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
