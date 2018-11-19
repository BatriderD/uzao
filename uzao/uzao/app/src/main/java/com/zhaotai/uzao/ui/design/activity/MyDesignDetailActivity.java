package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/5
 * Created by LiYou
 * Description : 我的设计详情页
 */

public class MyDesignDetailActivity extends BaseActivity {

    @BindView(R.id.right_btn)
    TextView mRightBtn;
    @BindView(R.id.iv_design_detail_img)
    ImageView mImg;
    @BindView(R.id.tv_design_detail_name)
    TextView mDesignName;
    @BindView(R.id.tv_design_detail_time)
    TextView mDesignTime;
    @BindView(R.id.tv_design_detail_idea)
    TextView mDesignIdea;

    private static final String EXTRA_KEY_DESIGN_DETAIL_INFO = "extra_key_design_detail_info";
    private DesignBean info;

    public static void launch(Context context, DesignBean data) {
        Intent intent = new Intent(context, MyDesignDetailActivity.class);
        intent.putExtra(EXTRA_KEY_DESIGN_DETAIL_INFO, data);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_design_detail);
        mTitle.setText("我的设计");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("重新编辑");

    }

    @Override
    protected void initData() {
        info = (DesignBean) getIntent().getSerializableExtra(EXTRA_KEY_DESIGN_DETAIL_INFO);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + info.thmbnail, mImg);
        mDesignName.setText(info.designName);
        mDesignTime.setText(TimeUtils.millis2String(Long.parseLong(info.createTime)));
        mDesignIdea.setText(info.designIdea);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.right_btn)
    public void onReEditor() {
        if (info != null) {
            switch (info.designType) {
                case "2d":
                    String isTemplate = StringUtil.isEmpty(info.templateSpuId) ? "N" : "Y";
                    EditorActivity.launch2D(mContext, info.mkuId, info.templateSpuId, isTemplate, info.sequenceNBR);
                    break;
                case "3d":
                    BlenderDesignActivity.launch(mContext, info.sampleId, info.sequenceNBR);
                    break;
            }
        } else {
            ToastUtil.showShort("信息错误");
        }
    }

}
