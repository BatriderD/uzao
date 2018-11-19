package com.zhaotai.uzao.ui.person.fragment;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.RecommendSpuAdapter;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.EventBean.UnReadMessageEvent;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.design.activity.MyDesignActivity;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;
import com.zhaotai.uzao.ui.order.activity.MyAfterSalesActivity;
import com.zhaotai.uzao.ui.order.activity.MyOrderActivity;
import com.zhaotai.uzao.ui.person.address.AddressActivity;
import com.zhaotai.uzao.ui.person.attention.AttentionActivity;
import com.zhaotai.uzao.ui.person.collection.activity.CollectActivity;
import com.zhaotai.uzao.ui.person.contract.PersonFragmentContract;
import com.zhaotai.uzao.ui.person.discount.activity.MyDiscountCouponActivity;
import com.zhaotai.uzao.ui.person.invite.InviteActivity;
import com.zhaotai.uzao.ui.person.material.activity.MyMaterialActivity;
import com.zhaotai.uzao.ui.person.message.activity.MessageCenterActivity;
import com.zhaotai.uzao.ui.person.myproduct.MyProductActivity;
import com.zhaotai.uzao.ui.person.presenter.PersonFragmentPresenter;
import com.zhaotai.uzao.ui.person.setting.PersonInfoActivity;
import com.zhaotai.uzao.ui.person.setting.SettingActivity;
import com.zhaotai.uzao.ui.person.track.activity.MyTrackActivity;
import com.zhaotai.uzao.ui.theme.activity.MySceneActivity;
import com.zhaotai.uzao.ui.theme.activity.MyThemeActivity;
import com.zhaotai.uzao.utils.GlideCircleTransform;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 个人中心
 */

public class PersonFragment extends BaseFragment implements PersonFragmentContract.View, View.OnClickListener {

    @BindView(R.id.recycler_recommend)
    RecyclerView mRecyclerRecommend;

    //    未读消息
    public ImageView ivUnReadView;

    //头像
    public ImageView mHeadImgView;

    //用户名
    public TextView mHeadNameView;

    //收藏的商品
    public TextView mCollectionNumber;
    //关注的设计师
    public TextView mDesignerNumber;

    //我的足迹
    public TextView mFootTrack;

    //我的信息
    public TextView tvPersonInfo;

    //可用优惠券数目
    public TextView tvMyCouponCount;

    private Badge qBadgeView;
    private PersonFragmentPresenter personFragmentPresenter;
    private RecommendSpuAdapter recommendSpuAdapter;
    private boolean isLazyCompleteInit = false;
    private RelativeLayout mRLGuessYouLikeTitle;

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_person_new;
    }

    @Override
    public void initView() {
        View mHeadView = View.inflate(_mActivity, R.layout.head_personal, null);
        ivUnReadView = (ImageView) mHeadView.findViewById(R.id.iv_person_icon_unread_icon);
        mHeadImgView = (ImageView) mHeadView.findViewById(R.id.iv_person_head_photo);
        mHeadNameView = (TextView) mHeadView.findViewById(R.id.tv_person_nick_name);
        mCollectionNumber = (TextView) mHeadView.findViewById(R.id.tv_person_head_collect_num);
        mDesignerNumber = (TextView) mHeadView.findViewById(R.id.tv_person_head_concerned_designer_num);
        mFootTrack = (TextView) mHeadView.findViewById(R.id.tv_person_head_my_history_num);
        tvPersonInfo = (TextView) mHeadView.findViewById(R.id.tv_person_head_info);
        tvMyCouponCount = (TextView) mHeadView.findViewById(R.id.tv_person_my_coupon_count);
        mRLGuessYouLikeTitle = (RelativeLayout) mHeadView.findViewById(R.id.rl_guess_you_like_title);
        mRLGuessYouLikeTitle.setVisibility(View.GONE);

        mHeadView.findViewById(R.id.iv_person_icon_bell).setOnClickListener(this);
        mHeadView.findViewById(R.id.iv_person_head_photo).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_nick_name).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_head_info).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_person_head_collect_product).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_person_head_doncerned_designer).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_product).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_theme).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_person_head_my_history).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_order).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_order_wait_pay).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_order_wait_commend).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_order_wait_send).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_order_wait_receiving).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_design).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_material).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_invite).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_manager_scene).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_my_after_sell).setOnClickListener(this);
        mHeadView.findViewById(R.id.rl_person_my_coupon).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_receive_address).setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_person_setting).setOnClickListener(this);

        qBadgeView = new QBadgeView(getActivity()).bindTarget(ivUnReadView).setBadgeGravity(Gravity.CENTER)
                .setBadgeTextSize(8, true);
        mRecyclerRecommend.setNestedScrollingEnabled(false);
        mRecyclerRecommend.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        //mRecyclerRecommend.addItemDecoration(new SimpleDividerItemDecoration(20, 10));
        recommendSpuAdapter = new RecommendSpuAdapter();
        mRecyclerRecommend.setAdapter(recommendSpuAdapter);
        recommendSpuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean item = (GoodsBean) adapter.getItem(position);
                if (item != null)
                    CommodityDetailMallActivity.launch(_mActivity, item.sequenceNBR);
            }
        });
        mRecyclerRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childPosition = parent.getChildAdapterPosition(view);
                if (childPosition > 0) {
                    if (childPosition % 2 == 0) {
                        outRect.left = 10;
                        outRect.right = 20;
                    } else {
                        outRect.left = 20;
                        outRect.right = 10;
                    }
                }
                outRect.bottom = 10;
            }
        });
        recommendSpuAdapter.addHeaderView(mHeadView);
    }

    @Override
    public void initPresenter() {
        personFragmentPresenter = new PersonFragmentPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        //根据用户登录状态进行相应操作
        personFragmentPresenter.getLoginStage();
        isLazyCompleteInit = true;
    }

    /**
     * 没有状态页面
     *
     * @return 状态页面
     */
    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //未读消息数
            case R.id.iv_person_icon_bell:
                if (getActivity() != null) {
                    if (!isLogin()) return;
                    MessageCenterActivity.launch(getActivity());
                }
                break;
            //头像点击
            case R.id.iv_person_head_photo:
            case R.id.tv_person_nick_name:
            case R.id.tv_person_head_info:
                if (isLogin()) {
                    //登录跳转个人信息中心
                    PersonInfoActivity.launch(getContext());
                }
                break;
            //收藏的商品
            case R.id.ll_person_head_collect_product:
                if (getActivity() != null) {
                    if (!isLogin()) return;
                    CollectActivity.launch(_mActivity);
                }
                break;
            //关注的设计师
            case R.id.ll_person_head_doncerned_designer:
                if (getActivity() != null) {
                    if (!isLogin()) return;
                    AttentionActivity.launch(getActivity());
                }
                break;
            //我的商品
            case R.id.tv_person_my_product:
                if (getActivity() != null) {
                    if (!isLogin()) return;
                    MyProductActivity.launch(_mActivity);
                }
                break;
            //我的主题
            case R.id.tv_person_my_theme:
                if (getActivity() != null) {
                    if (!isLogin()) return;
                    MyThemeActivity.launch(getActivity());
                }
                break;
            //我的足迹
            case R.id.ll_person_head_my_history:
                if (!isLogin()) return;
                MyTrackActivity.launch(_mActivity);
                break;
            //我的订单
            case R.id.tv_person_my_order:
                if (!isLogin()) return;
                MyOrderActivity.launch(_mActivity, 0);
                break;
            //待付款
            case R.id.tv_person_order_wait_pay:
                if (!isLogin()) return;
                MyOrderActivity.launch(_mActivity, 1);
                break;
            //待评价
            case R.id.tv_person_order_wait_commend:
                if (!isLogin()) return;
                MyOrderActivity.launch(_mActivity, 5);
                break;
            //待发货
            case R.id.tv_person_order_wait_send:
                if (!isLogin()) return;
                MyOrderActivity.launch(_mActivity, 3);
                break;
            //待收货
            case R.id.tv_person_order_wait_receiving:
                if (!isLogin()) return;
                MyOrderActivity.launch(_mActivity, 4);
                break;
            //我的设计
            case R.id.tv_person_my_design:
                if (isLogin()) {
                    //我的作品页面
                    MyDesignActivity.launch(_mActivity);
                }
                break;
            //我的素材
            case R.id.tv_person_my_material:
                if (isLogin()) {
                    MyMaterialActivity.launch(getActivity());
                }
                break;
            //我的邀请
            case R.id.tv_person_my_invite:
                if (isLogin()) {
                    InviteActivity.launch(_mActivity);
                }
                break;
            //售后订单
            case R.id.tv_person_my_after_sell:
                if (!isLogin()) return;
                MyAfterSalesActivity.launch(_mActivity);
                break;
            //优惠券
            case R.id.rl_person_my_coupon:
                if (!isLogin()) return;
                MyDiscountCouponActivity.launch(_mActivity);
                break;
            //收货地址
            case R.id.tv_person_receive_address:
                if (!isLogin()) return;
                AddressActivity.launch(getActivity());
                break;//收货地址
            case R.id.tv_person_my_manager_scene:
                if (!isLogin()) return;
                MySceneActivity.launch(getActivity());
                break;
            //设置
            case R.id.tv_person_setting:
                if (getActivity() != null) {
                    SettingActivity.launch(getActivity());
                }
                break;
        }
    }

    /**
     * 判断是否登录
     */
    private boolean isLogin() {
        boolean loginStatus = LoginHelper.getLoginStatus();
        if (!loginStatus) {
            LoginMsgActivity.launch(_mActivity);
        }
        return loginStatus;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PersonInfo info) {
        if (!isLazyCompleteInit) return;
        switch (info.code) {
            //登录成功后 刷新个人信息
            case EventBusEvent.REQUEST_PERSON_INFO:
                //请求个人信息
                personFragmentPresenter.getLoginStage();
                break;
            //修改头像后 更改头像
            case EventBusEvent.CHANGE_HEAD_IMAGE:
                Glide.with(getActivity()).load(ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(info.avatar))
                        .transform(new GlideCircleTransform(getActivity())).into(mHeadImgView);
                break;
            //退出登录
            case EventBusEvent.LOG_OUT:
                showUnLogin();
                break;
            //修改昵称
            case EventBusEvent.CHANGE_NICK_NAME:
                mHeadNameView.setText(info.nickName);
                break;
            //刷新个人信息
            case EventBusEvent.REQUEST_PERSON:
                personFragmentPresenter.getPersonInfo();
                break;
            //刷新收藏的商品
            case EventBusEvent.REFRESH_COLLECTION:
                int num = Integer.valueOf(mCollectionNumber.getText().toString()) - Integer.valueOf(info.interestDesignSpuCount);
                mCollectionNumber.setText(String.valueOf(num));
                break;
            //新增我的收藏商品

//            //刷新消息未读消息数量
//            case EventBusEvent.REFRESH_UNREAD_COUNT:
//                personFragmentPresenter.getUnHandleMessage();
//                break;
            //修改地区
            case EventBusEvent.CHANGE_REGIN:
                tvPersonInfo.setText(info.cityName + " " + info.profession);
                break;
            //修改职业
            case EventBusEvent.CHANGE_PROFESSION:
                tvPersonInfo.setText(info.cityName + " " + info.profession);
                break;
            //修改我的足迹数量:
            case EventBusEvent.CHANGE_TRACK_NUM:
                mFootTrack.setText(info.myFootprintCount);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        if (EventBusEvent.ReLoading.equals(event.eventType) && isLazyCompleteInit) {
            personFragmentPresenter.getLoginStage();
        }
    }

    @Override
    public void onSupportVisible() {
        if (isLazyCompleteInit) {
            //根据用户登录状态进行相应操作
            if (LoginHelper.getLoginStatus()) {
                //更新新用户信息 并显示
                personFragmentPresenter.getPersonInfo();
                personFragmentPresenter.getUnHandleMessage();
            }
        }
    }

    /**
     * 展示用户信息
     *
     * @param personBean 用户信息类
     */
    @Override
    public void showPersonInfo(PersonBean personBean) {
        //头像
        Glide.with(getActivity()).load(ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(personBean.avatar))
                .placeholder(R.drawable.ic_default_head)
                .transform(new GlideCircleTransform(getActivity())).into(mHeadImgView);
        //未读消息
        personBean.unReadCount = StringUtil.isEmpty(personBean.unReadCount) ? getString(R.string._0) : personBean.unReadCount;
        qBadgeView.setBadgeNumber(Integer.parseInt(personBean.unReadCount));

        //用户名
        mHeadNameView.setText(personBean.nickName);
//        信息
        tvPersonInfo.setVisibility(View.VISIBLE);
        tvPersonInfo.setText(personBean.cityName + " " + personBean.profession);
        //收藏的商品
        mCollectionNumber.setText(personBean.favnum);
        //关注的设计师
        mDesignerNumber.setText(personBean.interestCount);
        //我的足迹
        mFootTrack.setText(personBean.myFootprintCount);
    }

    /**
     * 展示用户未登录状态
     */
    @Override
    public void showUnLogin() {
        //头像
        Glide.with(this).load(R.drawable.ic_default_head).into(mHeadImgView);
        //用户名
        mHeadNameView.setText(R.string.immediately_login);
        tvPersonInfo.setVisibility(View.GONE);
        //收藏的商品
        mCollectionNumber.setText(getString(R.string._0));
        //关注的设计师
        mDesignerNumber.setText(getString(R.string._0));
        //我的足迹
        mFootTrack.setText("0");
        //优惠券数目
        tvMyCouponCount.setVisibility(View.GONE);
        //未读消息
        qBadgeView.setBadgeNumber(0);
        EventBus.getDefault().post(new UnReadMessageEvent(0));
    }


    /**
     * 展示用户未读消息数
     *
     * @param msg 未读消息
     */
    @Override
    public void showUnHandleMessage(int msg) {
        Log.d(this.getClass().toString(), "个人 showUnHandleMessage: ");
        if (qBadgeView != null) {
            qBadgeView.setBadgeNumber(msg);
        }
    }

    @Override
    public void showDiscountSize(int size) {
        //优惠券数目
        tvMyCouponCount.setVisibility(View.VISIBLE);
        tvMyCouponCount.setText(getString(R.string.discount_number, String.valueOf(size)));
    }

    @Override
    public void showRecommend(List<GoodsBean> goodsBeenList) {
        mRLGuessYouLikeTitle.setVisibility(goodsBeenList.isEmpty() ? View.GONE : View.VISIBLE);
        if (recommendSpuAdapter != null)
            recommendSpuAdapter.setNewData(goodsBeenList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLazyCompleteInit = false;
        EventBus.getDefault().unregister(this);
    }
}
