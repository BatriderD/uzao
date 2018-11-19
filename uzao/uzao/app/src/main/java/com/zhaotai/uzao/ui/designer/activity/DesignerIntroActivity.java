package com.zhaotai.uzao.ui.designer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.TagBean;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.BindView;

/**
 * Time: 2018/1/24
 * Created by LiYou
 * Description : 设计师简介
 */

public class DesignerIntroActivity extends BaseActivity {

    @BindView(R.id.iv_designer_intro_bg)
    ImageView mDesignerIntroBg;
    @BindView(R.id.tv_designer_intro_name)
    TextView mName;
    @BindView(R.id.tf_designer_intro)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.tv_designer_intro)
    TextView mIntro;

    public static void launch(Context context, PersonBean personBean) {
        Intent intent = new Intent(context, DesignerIntroActivity.class);
        intent.putExtra("personInfo", personBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_designer_intro);
        mTitle.setText("简介详情");
    }

    @Override
    protected void initData() {
        PersonBean info = (PersonBean) getIntent().getSerializableExtra("personInfo");
        Glide.with(this).load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.background).placeholder(R.drawable.icon_bg_designer_header)
                .error(R.drawable.icon_bg_designer_header).into(mDesignerIntroBg);
        mName.setText(info.nickName);
        if (!StringUtil.isEmpty(info.gender)) {
            if (0 == Integer.valueOf(info.gender)) {
                //女
                Drawable woman = getDrawable(R.drawable.icon_woman);
                assert woman != null;
                woman.setBounds(0, 0, woman.getMinimumWidth(), woman.getMinimumHeight());
                mName.setCompoundDrawables(null, null, woman, null);
            } else {//男
                Drawable man = getDrawable(R.drawable.icon_man);
                assert man != null;
                man.setBounds(0, 0, man.getMinimumWidth(), man.getMinimumHeight());
                mName.setCompoundDrawables(null, null, man, null);
            }
        }

        mTagFlowLayout.setAdapter(new TagAdapter<TagBean>(info.tags) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean s) {
                TextView tagView = new TextView(mContext);
                tagView.setPadding((int) PixelUtil.dp2px(16), 3, (int) PixelUtil.dp2px(16), 3);
                tagView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_info_tag));
                tagView.setTextColor(Color.parseColor("#262626"));
                tagView.setTextSize(10);
                tagView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(16);
                tagView.setLayoutParams(params);
                tagView.setText(s.tagName);
                return tagView;
            }
        });

        mIntro.setText(info.aboutMe);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
