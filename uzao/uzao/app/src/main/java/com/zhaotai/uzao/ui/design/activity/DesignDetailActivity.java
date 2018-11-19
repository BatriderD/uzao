package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.TimeUtils;

import butterknife.BindView;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description : 设计详情页
 */

public class DesignDetailActivity extends BaseActivity{

    @BindView(R.id.iv_design_detail_img)
    ImageView mImg;
    @BindView(R.id.tv_design_detail_name)
    TextView mDesignName;
    @BindView(R.id.tv_design_detail_time)
    TextView mDesignTime;
    @BindView(R.id.tv_design_detail_idea)
    TextView mDesignIdea;

    private static final String EXTRA_KEY_DESIGN_DETAIL_INFO = "extra_key_design_detail_info";

    public static void launch(Context context, DesignBean data){
        Intent intent = new Intent(context,DesignDetailActivity.class);
        intent.putExtra(EXTRA_KEY_DESIGN_DETAIL_INFO,data);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_design_detail);
        mTitle.setText("我的设计");
    }

    @Override
    protected void initData() {
        DesignBean info = (DesignBean) getIntent().getSerializableExtra(EXTRA_KEY_DESIGN_DETAIL_INFO);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + info.thmbnail, mImg);
        mDesignName.setText(info.designName);
        mDesignTime.setText(TimeUtils.millis2String(Long.parseLong(info.createTime)));
        mDesignIdea.setText(info.designIdea);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
