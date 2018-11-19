package com.zhaotai.uzao.ui.designer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.EventRecommendBean;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.brand.BrandSearchActivity;
import com.zhaotai.uzao.ui.category.goods.adapter.RewardAdapter;
import com.zhaotai.uzao.ui.designer.contract.DesignerContract;
import com.zhaotai.uzao.ui.designer.fragment.DesignerMaterialFragment;
import com.zhaotai.uzao.ui.designer.fragment.DesignerProductFragment;
import com.zhaotai.uzao.ui.designer.fragment.DesignerThemeFragment;
import com.zhaotai.uzao.ui.designer.presenter.DesignerHomePagePresenter;
import com.zhaotai.uzao.ui.order.activity.DesignerRewardPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.poster.PosterActivity;
import com.zhaotai.uzao.utils.GlideCircleTransform;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.MyCoordinatorTabLayout;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/5/18
 * Created by LiYou
 * Description : 设计师主页
 */

public class DesignerActivity extends BaseFragmentActivity implements DesignerContract.View, View.OnClickListener {

    @BindView(R.id.coordinatortablayout)
    MyCoordinatorTabLayout mCoordinatorTabLayout;

    @BindView(R.id.vp)
    ViewPager mViewPager;

    private String designerId;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    private ImageView mIvHead;
    private ImageView mHeadBg;
    private TextView mNickName;
    private TextView mRegionAndProfessionAndFans;
    private TextView mAttention;
    private TextView mReward;
    private PopupWindow window;
    private PersonBean personBean;

    private DesignerHomePagePresenter mPresenter;

    private boolean isAttention = false;
    private TextView mIntro;

    /**
     * @param id 设计师id
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, DesignerActivity.class);
        intent.putExtra(GlobalVariable.DESIGNERID, id);
        context.startActivity(intent);
    }

    /**
     * 推荐页面跳转到设计师
     *
     * @param id 设计师id
     */
    public static void recommendLaunch(Context context, String id, int pos) {
        Intent intent = new Intent(context, DesignerActivity.class);
        intent.putExtra(GlobalVariable.DESIGNERID, id);
        intent.putExtra("pos", pos);
        intent.putExtra("type", -1);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_designer);
        designerId = getIntent().getStringExtra(GlobalVariable.DESIGNERID);
        initFragments();
        initViewPager();
        mCoordinatorTabLayout.setupWithViewPager(mViewPager);
        //添加头布局  设计师简介
        FrameLayout headFrameLayout = mCoordinatorTabLayout.getHeadView();
        View headView = View.inflate(mContext, R.layout.head_designer, null);
        headFrameLayout.addView(headView);
        LinearLayout mLlIntro = (LinearLayout) headView.findViewById(R.id.ll_designer_intro);
        mIntro = (TextView) headView.findViewById(R.id.iv_designer_intro);
        mIvHead = (ImageView) headView.findViewById(R.id.iv_designer_head);
        mHeadBg = (ImageView) headView.findViewById(R.id.iv_designer_bg);
        mNickName = (TextView) headView.findViewById(R.id.iv_designer_name);
        mReward = (TextView) headView.findViewById(R.id.tv_designer_reward);
        mRegionAndProfessionAndFans = (TextView) headView.findViewById(R.id.iv_designer_region_profession_fans);
        mAttention = (TextView) headView.findViewById(R.id.tv_designer_attention);
        mAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关注 取消关注
                if (isAttention) {
                    mPresenter.cancelDesigner(designerId);
                } else {
                    mPresenter.attentionDesigner(designerId);
                }
            }
        });
        //返回
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
        //打赏按钮
        mReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否登录
                if (!mPresenter.isLogin()) return;
                if (personBean != null) {
                    if (personBean.userId.equals(LoginHelper.getUserId())) {
                        ToastUtil.showShort("不能给自己打赏");
                    } else {
                        //打赏
                        mPresenter.getRewardPrice();
                    }
                }
            }
        });

        mLlIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personBean != null)
                    DesignerIntroActivity.launch(mContext, personBean);
            }
        });
        mCoordinatorTabLayout.setTranslucentNavigationBar(this);
    }

    @Override
    protected void initData() {
        mPresenter = new DesignerHomePagePresenter(this, this);
        //获取设计师信息
        mPresenter.getDesignerInfo(designerId);
        //设计师是否被关注
        if (LoginHelper.getLoginStatus()) {
            mPresenter.isPayAttention(designerId);
        }
    }

    private void initFragments() {
        mFragments.add(DesignerThemeFragment.newInstance(designerId));
        mFragments.add(DesignerProductFragment.newInstance(designerId));
        mFragments.add(DesignerMaterialFragment.newInstance(designerId));

        mTitles.add("场景");
        mTitles.add("商品");
        mTitles.add("素材");
    }

    private void initViewPager() {
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
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
                if (isAttention) {
                    mPresenter.cancelDesigner(designerId);
                } else {
                    mPresenter.attentionDesigner(designerId);
                }
                break;
            case R.id.iv_share://分享
                if (LoginHelper.getLoginStatus()) {
                    mPresenter.hasPoster();
                } else {
                    LoginHelper.goLogin(this);
                }
                break;
            case R.id.iv_search:
                switch (mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition()) {
                    case 0://主题
                        DesignerSearchActivity.launch(mContext, designerId, BrandSearchActivity.THEME);
                        break;
                    case 1://商品
                        DesignerSearchActivity.launch(mContext, designerId, BrandSearchActivity.PRODUCT);
                        break;
                    case 2://素材
                        DesignerSearchActivity.launch(mContext, designerId, BrandSearchActivity.MATERIAL);
                        break;
                }
                break;
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         *  SHARE_MEDIA 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         *  分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(DesignerActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(DesignerActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(DesignerActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                //根据key来区分自定义按钮的类型，并进行对应的操作
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_poster")) {
                    PosterActivity.launchDesigner(DesignerActivity.this, designerId);
                }
            }
        }
    };

    /**
     * 打开分享面板
     */
    @Override
    public void openShareBoard(boolean hasPoster) {
        SnsPlatform snsPlatform = SHARE_MEDIA.createSnsPlatform("umeng_sharebutton_poster", "umeng_sharebutton_poster", "ic_share_poster", "ic_share_poster", 0);
        ShareAction shareAction = new ShareAction(this)
                .withText("uzao")
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setShareboardclickCallback(shareBoardlistener)
                .setCallback(shareListener);
        if (hasPoster) {
            Class aClass = shareAction.getClass();
            try {
                java.lang.reflect.Field field = aClass.getDeclaredField("platformlist");
                field.setAccessible(true);
                List<SnsPlatform> platformlist = (List<SnsPlatform>) field.get(shareAction);
                platformlist.add(0, snsPlatform);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                shareAction.open();
            }
        } else {
            shareAction.open();
        }

    }


    /**
     * 设计师信息
     *
     * @param info 设计师信息
     */
    @Override
    public void showDesignerInfo(PersonBean info) {
        personBean = info;
        mIntro.setText(info.aboutMe);
        //简介
        if (StringUtil.isEmpty(info.aboutMe)) {
            mIntro.setText("这是一个神秘的人 >");
        } else {
            if (info.aboutMe.length() < 9) {
                mIntro.setText(info.aboutMe + " >");
            } else {
                String aboutMe = info.aboutMe.substring(0, 8);
                mIntro.setText(aboutMe + " >");
            }
        }

        //设计师头像
        Glide.with(mContext).load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.avatar)
                .transform(new GlideCircleTransform(mContext))
                .error(R.drawable.ic_default_head).into(mIvHead);
        //设计师背景
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + info.background, mHeadBg
                , R.drawable.icon_bg_designer_header, R.drawable.icon_bg_designer_header);

        //设计师昵称
        mNickName.setText(info.nickName);
        if (!StringUtil.isEmpty(info.gender)) {
            if (0 == Integer.valueOf(info.gender)) {
                //女
                Drawable woman = getDrawable(R.drawable.icon_woman);
                assert woman != null;
                woman.setBounds(0, 0, woman.getMinimumWidth(), woman.getMinimumHeight());
                mNickName.setCompoundDrawables(null, null, woman, null);
            } else {//男
                Drawable man = getDrawable(R.drawable.icon_man);
                assert man != null;
                man.setBounds(0, 0, man.getMinimumWidth(), man.getMinimumHeight());
                mNickName.setCompoundDrawables(null, null, man, null);
            }
        }
        //地区 职业
        String cityName = "未知位置";
        if (!StringUtil.isEmpty(info.cityName)) {
            cityName = info.cityName;
        }
        String profession = "未知职业";
        if (!StringUtil.isEmpty(info.profession)) {
            profession = info.profession;
        }
        mRegionAndProfessionAndFans.setText(cityName + "  |  " + profession + " | " + info.followCount + "粉丝");
        //打赏次数
        if (info.rewardCount > 999) {
            mReward.setText("打赏(999+次)");
        } else if (info.rewardCount == 0) {
            mReward.setText("打赏");
        } else {
            mReward.setText("打赏(" + info.rewardCount + "次)");
        }

    }

    /**
     * 关注设计师
     */
    @Override
    public void attentionDesigner() {
        isAttention = true;
        mAttention.setText("已关注");
        mAttention.setTextColor(Color.parseColor("#262626"));
        mAttention.setBackgroundResource(R.drawable.shape_brand_bg_grey_radius);
    }

    /**
     * 取消关注
     */
    @Override
    public void cancelDesigner() {
        isAttention = false;
        mAttention.setText("关注");
        mAttention.setTextColor(Color.parseColor("#ffffff"));
        mAttention.setBackgroundResource(R.drawable.shape_order_btn_yellow);
    }

    @Override
    public void changeDesigner(boolean add) {

    }

    /**
     * 显示 打赏
     *
     * @param price 打赏金额列表
     */
    @Override
    public void showReward(List<DictionaryBean> price) {
        if (window == null) {
            window = new PopupWindow(mContext);
            View popView = View.inflate(mContext, R.layout.pop_material_reward, null);
            final RelativeLayout popRoot = (RelativeLayout) popView.findViewById(R.id.pop_root);
            final TextView mText1 = (TextView) popView.findViewById(R.id.pop_text);
            final EditText mEtPrice = (EditText) popView.findViewById(R.id.et_reward_price);
            final RecyclerView mRecycler = (RecyclerView) popView.findViewById(R.id.recycler);
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            RewardAdapter mRewardAdapter = new RewardAdapter(price);
            mRecycler.setAdapter(mRewardAdapter);
            final TextView mText2 = (TextView) popView.findViewById(R.id.pop_text1);
            window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            window.setContentView(popView);
            window.setFocusable(true);
            window.setOutsideTouchable(true);
            window.setBackgroundDrawable(new ColorDrawable());
            window.showAtLocation(mCoordinatorTabLayout, Gravity.CENTER, 0, 0);
            window.update();

            popRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });

            mRewardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        DictionaryBean item = (DictionaryBean) adapter.getItem(i);
                        if (item != null) {
                            item.isSelect = position == i;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    DictionaryBean item = (DictionaryBean) adapter.getItem(position);
                    if (item != null) {
                        RewardInfo info = new RewardInfo();
                        info.userId = designerId;
                        info.tipOption = item.entryKey;
                        if (personBean != null)
                            DesignerRewardPayConfirmOrderActivity.launch((Activity) mContext, designerId, personBean.avatar, personBean.nickName, item.entryValue, info);
                    }
                }
            });

            mText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("自定义打赏金额".equals(mText2.getText().toString())) {
                        mText2.setText("确定打赏");
                        mText1.setText("填写打赏金额");
                        mRecycler.setVisibility(View.GONE);
                        mEtPrice.setVisibility(View.VISIBLE);
                    } else if ("确定打赏".equals(mText2.getText().toString())) {
                        RewardInfo info = new RewardInfo();
                        info.userId = designerId;
                        info.tipOption = "ohters";
                        info.amount = mEtPrice.getText().toString();
                        if (personBean != null)
                            DesignerRewardPayConfirmOrderActivity.launch((Activity) mContext, designerId, personBean.avatar, personBean.nickName, mEtPrice.getText().toString(), info);
                    }
                }
            });

            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mText1.setText("选择打赏金额");
                    mText2.setText("自定义打赏金额");
                    mRecycler.setVisibility(View.VISIBLE);
                    mEtPrice.setVisibility(View.GONE);
                }
            });
        } else {
            window.showAtLocation(mCoordinatorTabLayout, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //打赏成功
        if (resultCode == RESULT_OK) {
            if (window != null) {
                window.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int type = getIntent().getIntExtra("type", 0);
        if (type == -1) {
            int pos = getIntent().getIntExtra("pos", 0);
            EventBus.getDefault().post(new EventRecommendBean(pos, isAttention));
        }
    }
}
