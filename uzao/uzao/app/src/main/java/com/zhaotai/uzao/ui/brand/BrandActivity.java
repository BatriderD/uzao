package com.zhaotai.uzao.ui.brand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.brand.contract.BrandContract;
import com.zhaotai.uzao.ui.brand.fragment.BrandMaterialFragment;
import com.zhaotai.uzao.ui.brand.fragment.BrandProductFragment;
import com.zhaotai.uzao.ui.brand.fragment.BrandThemeFragment;
import com.zhaotai.uzao.ui.brand.presenter.BrandPresenter;
import com.zhaotai.uzao.ui.discuss.activity.DiscussMainListActivity;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;
import com.zhaotai.uzao.widget.MyCoordinatorTabLayout;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Time: 2017/5/18
 * Created by LiYou
 * Description : 品牌主页
 */

public class BrandActivity extends BaseFragmentActivity implements BrandContract.View, View.OnClickListener {

    @BindView(R.id.coordinatortablayout)
    MyCoordinatorTabLayout mCoordinatorTabLayout;

    @BindView(R.id.vp)
    ViewPager mViewPager;

    private String brandId;
    private MyPagerAdapter mAdapter;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    private ImageView mIvHead;
    private TextView mNickName;
    private TextView mAttentionNum;
    private TextView mAttention;
    private TextView mIntro;
    private BrandBean brandBean;
    //判断是否关注
    private boolean isAttention;

    private ImageView mBg;
    private BrandPresenter mPresenter;

    /**
     * @param id 设计师id
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, BrandActivity.class);
        intent.putExtra(GlobalVariable.DESIGNERID, id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_designer);
        brandId = getIntent().getStringExtra(GlobalVariable.DESIGNERID);
        initFragments();
        initViewPager();
        mCoordinatorTabLayout
                .setupWithViewPager(mViewPager);

        FrameLayout headFrameLayout = mCoordinatorTabLayout.getHeadView();
        View headView = View.inflate(mContext, R.layout.head_brand, null);
        headFrameLayout.addView(headView);

        mIvHead = (ImageView) headView.findViewById(R.id.iv_brand_head);
        mNickName = (TextView) headView.findViewById(R.id.iv_brand_name);
        mAttentionNum = (TextView) headView.findViewById(R.id.iv_brand_fans);
        mAttention = (TextView) headView.findViewById(R.id.tv_brand_attention);
        mIntro = (TextView) headView.findViewById(R.id.iv_brand_intro);
        mBg = (ImageView) headView.findViewById(R.id.iv_bg);
        LinearLayout mLlIntro = (LinearLayout) headView.findViewById(R.id.ll_brand_intro);
        mCoordinatorTabLayout.setToolTitle("品牌主页");
        mCoordinatorTabLayout.getBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCoordinatorTabLayout.getSearch().setOnClickListener(this);
        ImageView share = mCoordinatorTabLayout.getShare();
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);
        mLlIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandIntroActivity.launch(mContext, brandBean);
            }
        });
        mAttention.setOnClickListener(this);
        findViewById(R.id.tv_brand_discuss).setOnClickListener(this);

        mCoordinatorTabLayout.setTranslucentNavigationBar(this);
    }


    @Override
    protected void initData() {
        mPresenter = new BrandPresenter(this, this);
        mPresenter.getBrandInfo(brandId);
    }

    private void initFragments() {
        mFragments.add(BrandThemeFragment.newInstance(brandId));
        mFragments.add(BrandProductFragment.newInstance(brandId));
        mFragments.add(BrandMaterialFragment.newInstance(brandId));
        mTitles.add("场景");
        mTitles.add("商品");
        mTitles.add("素材");
    }

    private void initViewPager() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_brand_attention://关注
                if (LoginHelper.getLoginStatus()) {
                    if (isAttention) {
                        mPresenter.cancelAttentionBrand(brandId);
                    } else {
                        mPresenter.attentionBrand(brandId);
                    }
                } else {
                    LoginActivity.launch(mContext);
                }
                break;
            case R.id.tv_brand_discuss://讨论专区
                if (LoginHelper.getLoginStatus()) {
                    DiscussMainListActivity.launch(BrandActivity.this, brandId, DiscussMainListActivity.TYPE_BRAND);
                } else {
                    LoginActivity.launch(mContext);
                }
                break;
            case R.id.iv_search:
                switch (mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition()) {
                    case 0://主题
                        BrandSearchActivity.launch(mContext, brandId, BrandSearchActivity.THEME);
                        break;
                    case 1://商品
                        BrandSearchActivity.launch(mContext, brandId, BrandSearchActivity.PRODUCT);
                        break;
                    case 2://素材
                        BrandSearchActivity.launch(mContext, brandId, BrandSearchActivity.MATERIAL);
                        break;
                }
                break;

            case R.id.iv_share:
                switch (mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition()) {
                    case 0://主题
                        openShare();
                        break;
                    case 1://商品
                        openShare();
                        break;
                    case 2://素材
                        openShare();
                        break;
                }
                break;
        }
    }

    /**
     * 分享
     */
    private void openShare() {
        UMShareListener shareListener = new UMShareListener() {
            /**
             * SHARE_MEDIA 分享开始的回调
             *
             * @param platform 平台类型
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * 分享成功的回调
             *
             * @param platform 平台类型
             */
            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtil.showShort("分享成功");
            }

            /**
             * 分享失败的回调
             *
             * @param platform 平台类型
             * @param t        错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                ToastUtil.showShort("分享失败");
            }

            /**
             * 分享取消的回调
             *
             * @param platform 平台类型
             */
            @Override
            public void onCancel(SHARE_MEDIA platform) {
                ToastUtil.showShort("取消分享");

            }
        };

        UMImage image = new UMImage(this, R.mipmap.ic_launcher);//bitmap文件

        UMWeb umWeb = new UMWeb("www.baidu.com", "欢迎使用优造中国", "欢迎使用优造中国，最全的定制网站", image);
        new ShareAction(this)
                .withText("uzao")
                .withMedia(umWeb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(shareListener).open();
    }

    /**
     * 品牌信息
     *
     * @param info 品牌数据
     */
    @Override
    public void showBrandInfo(BrandBean info) {
        brandBean = info;
        //品牌logo
        GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + info.brandLogo, mIvHead, R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(this));
        GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + info.brandCover, mBg);
        //品牌名称
        mNickName.setText(info.brandName);
        //设计师的粉丝数
        if (!StringUtil.isEmpty(info.followCount)) {
            //favoriteCount = Integer.valueOf(info.followCount);
            mAttentionNum.setText(info.followCount + "粉丝");
        }
        if (StringUtil.isEmpty(info.brandAbout)) {
            mIntro.setText("这是一个神秘的品牌 >");
        } else {
            if (info.brandAbout.length() < 9) {
                mIntro.setText(info.brandAbout + " >");
            } else {
                String aboutMe = info.brandAbout.substring(0, 8);
                mIntro.setText(aboutMe + " >");
            }
        }

        if ("N".equals(info.isFavorite)) {
            cancelAttention();
        } else {
            attention();
        }
    }

    /**
     * 关注
     */
    @Override
    public void attention() {
        isAttention = true;
        mAttention.setText("已关注");
        mAttention.setTextColor(Color.parseColor("#262626"));
        mAttention.setBackgroundResource(R.drawable.shape_brand_bg_grey_radius);
    }

    /**
     * 未关注
     */
    @Override
    public void cancelAttention() {
        isAttention = false;
        mAttention.setText("关注");
        mAttention.setTextColor(Color.parseColor("#ffffff"));
        mAttention.setBackgroundResource(R.drawable.shape_order_btn_yellow);
    }
}
