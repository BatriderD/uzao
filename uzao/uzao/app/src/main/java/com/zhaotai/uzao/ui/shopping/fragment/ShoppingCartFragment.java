package com.zhaotai.uzao.ui.shopping.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.EventBean.RefreshCartInfo;
import com.zhaotai.uzao.bean.EventBean.UnReadMessageEvent;
import com.zhaotai.uzao.ui.category.goods.activity.ActivityListActivity;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.person.message.activity.MessageCenterActivity;
import com.zhaotai.uzao.ui.shopping.activity.SimilarProductActivity;
import com.zhaotai.uzao.ui.shopping.adapter.MultiCartItem;
import com.zhaotai.uzao.ui.shopping.adapter.ShoppingCartAdapter;
import com.zhaotai.uzao.ui.shopping.contract.ShoppingCartContract;
import com.zhaotai.uzao.ui.shopping.presenter.ShoppingCartPresenter;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SelectGoodsCountDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description :
 */

public class ShoppingCartFragment extends BaseFragment implements ShoppingCartContract.View, OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener, CancelAdapt {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.tool_back)
    ImageView mBack;
    @BindView(R.id.tool_bar_right_img)
    ImageView mBell;
    @BindView(R.id.iv_shopping_bell_num)
    ImageView mBellNum;
    @BindView(R.id.tool_title)
    TextView mTitle;

    //合计
    @BindView(R.id.final_price)
    TextView mFinallyPrice;
    //总额金额
    @BindView(R.id.total_price)
    TextView mTotalPrice;
    //优惠
    @BindView(R.id.cut_price)
    TextView mCutPrice;

    //tab line
    @BindView(R.id.view_shopping_cart_tab_bottom_line_left)
    View mTabBottomLeftLine;
    @BindView(R.id.view_shopping_cart_tab_bottom_line_right)
    View mTabBottomRightLine;

    //未登录布局
    @BindView(R.id.ll_shopping_cart_login_layout)
    LinearLayout mLoginLayout;
    //购物车内容布局
    @BindView(R.id.ll_shop_cart_content)
    LinearLayout mShoppingContent;

    @BindView(R.id.recycler_shopping)
    RecyclerView mRecycler;

    @BindView(R.id.ll_shopping_cart_bottom)
    LinearLayout mLlBottomAccount;

    //全选
    @BindView(R.id.iv_check_all)
    ImageView mIvCheckAll;

    @BindView(R.id.right_btn)
    Button mRightBtn; //编辑 完成
    @BindView(R.id.ll_shopping_cart_account)
    LinearLayout mLlAccount;
    @BindView(R.id.ll_shopping_cart_delete)
    LinearLayout mLlDelete;

    public static final String ARGUMENT_TYPE = "argument_type";
    public static final String FROM_MAIN = "from_main";
    public static final String FROM_DETAIL = "from_detail";

    private ShoppingCartPresenter mPresenter;
    private ShoppingCartAdapter mAdapter;
    private Badge qBadgeView;

    public static ShoppingCartFragment newInstance(String type) {
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TYPE, type);
        shoppingCartFragment.setArguments(bundle);
        return shoppingCartFragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_shopping;
    }

    @Override
    public void initView() {
        switch (getArguments().getString(ARGUMENT_TYPE, "")) {
            case FROM_MAIN://从首页来的:
                mBack.setVisibility(View.GONE);
                break;
            case FROM_DETAIL://从详情来的:
                mBack.setVisibility(View.VISIBLE);
                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
                mBell.setVisibility(View.VISIBLE);
                mBellNum.setVisibility(View.VISIBLE);
                break;
        }
        qBadgeView = new QBadgeView(getActivity()).bindTarget(mBellNum).setBadgeGravity(Gravity.CENTER)
                .setBadgeTextSize(8, true);
        mTitle.setText("购物车");
        mSwipe.setOnRefreshListener(this);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        mIvCheckAll.setTag(false);
        EventBus.getDefault().register(this);
        mRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childPosition = parent.getChildAdapterPosition(view);
                if (mAdapter != null) {
                    MultiCartItem item = mAdapter.getItem(childPosition);
                    assert item != null;
                    if (item.getItemType() == MultiCartItem.TYPE_RECOMMEND) {
                        if (childPosition % 2 == 0) {
                            outRect.left = 26;
                            outRect.right = 8;
                        } else {
                            outRect.left = 8;
                            outRect.right = 26;
                        }
                        outRect.bottom = 16;
                    }
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new ShoppingCartPresenter(this, getActivity());
    }

    @Override
    public void initData() {
        if (SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN)) {
            //已登录
            if (mPresenter != null) {
                mPresenter.getCartList(true);
            }
            showShoppingContentView();
        } else {
            //未登录
            showLoginView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showCartData(List<MultiCartItem> data) {
        if (mAdapter == null) {
            initAdapter(data);
        } else {
            mAdapter.setNewData(data);
        }
    }

    private void initAdapter(final List<MultiCartItem> data) {
        mAdapter = new ShoppingCartAdapter(data);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                switch (data.get(position).getItemType()) {
                    case MultiCartItem.TYPE_NORMAL:
                    case MultiCartItem.TYPE_ACTIVITY:
                    case MultiCartItem.TYPE_INVALID:
                    case MultiCartItem.TYPE_GUESS_TITLE:
                    case MultiCartItem.TYPE_EMPTY_GOODS:
                    case MultiCartItem.TYPE_EMPTY_INVALID_GOODS:
                        return 2;
                    case MultiCartItem.TYPE_RECOMMEND:
                        return 1;
                    default:
                        return 1;
                }
            }
        });

        mAdapter.setOnItemChildClickListener(this);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        final MultiCartItem item = (MultiCartItem) adapter.getItem(position);
        if (item == null) return;
        switch (view.getId()) {
            case R.id.iv_shopping_cart_section_check:
                mPresenter.checkItemState(position, true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.iv_check_goods:
                mPresenter.checkItemState(position, false);
                adapter.notifyDataSetChanged();
                break;
            case R.id.shopping_cart_spu_image://到商品详情
                CommodityDetailMallActivity.launch(_mActivity, item.spuId);
                break;
            case R.id.tv_shopping_cart_section_right_text://去活动列表
                ActivityListActivity.launch(_mActivity);
                break;
            case R.id.shopping_cart_spu_add://数量增加
                if (item.count != null && item.count.equals(item.storeCount)) {
                    ToastUtil.showShort("库存不足");
                    return;
                }
                mPresenter.updateGoodsCount(true, position);
                break;
            case R.id.shopping_cart_spu_sub://数量减少
                mPresenter.updateGoodsCount(false, position);
                break;
            case R.id.tv_shopping_cart_section_right_clear_failure://清空失效商品
                mPresenter.clearInvalidGoods();
                break;
            case R.id.tv_shop_cart_similar_goods://相似商品
                SimilarProductActivity.launch(_mActivity, item.spuId);
                break;
            case R.id.empty_view_btn://去逛逛
                HomeActivity.launch(_mActivity, 1);
                break;
            case R.id.iv_product_spu_image:
                CommodityDetailMallActivity.launch(_mActivity, item.spuId);
                break;
            case R.id.shopping_cart_spu_num://手动添加数量
                SelectGoodsCountDialog dialog = SelectGoodsCountDialog.create(item.count)
                        .setCountListener(new SelectGoodsCountDialog.OnChangeCountListener() {
                            @Override
                            public void onChangeCountListener(String count) {
                                if ("0".equals(count)) {
                                    count = "1";
                                }
                                mPresenter.updateGoodsCount(count, position, item);
                            }
                        });
                dialog.show(getChildFragmentManager(), SelectGoodsCountDialog.TAG);
//                Dialog dialog1 = dialog.getDialog();


                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getCartList(false);
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    /**
     * 显示购物车布局
     */
    @Override
    public void showShoppingContentView() {
        mLoginLayout.setVisibility(View.GONE);
        mShoppingContent.setVisibility(View.VISIBLE);
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("编辑");
    }

    /**
     * 显示登录布局
     */
    @Override
    public void showLoginView() {
        mLoginLayout.setVisibility(View.VISIBLE);
        mShoppingContent.setVisibility(View.GONE);
        mRightBtn.setVisibility(View.GONE);
    }

    /**
     * 更新价格
     *
     * @param countPrice 总额
     * @param finalPrice 合计
     * @param cutPrice   优惠
     */
    @Override
    public void showUpdatePrice(String countPrice, String finalPrice, String cutPrice) {
        //合计
        String countMoney = String.format(getResources().getString(R.string.account), DecimalUtil.getPrice(finalPrice));
        mFinallyPrice.setText(countMoney);
        //总额
        String totalMoney = String.format(getResources().getString(R.string.total_price), DecimalUtil.getPrice(countPrice));
        mTotalPrice.setText(totalMoney);
        //优惠
        String cutMoney = String.format(getResources().getString(R.string.cut_price), DecimalUtil.getPrice(cutPrice));
        mCutPrice.setText(cutMoney);
    }

    /**
     * 全部商品
     */
    @OnClick(R.id.rl_shop_cart_tab_all_goods)
    public void onClickAllGoods() {
        mSwipe.setEnableRefresh(true);
        mTabBottomLeftLine.setVisibility(View.VISIBLE);
        mTabBottomRightLine.setVisibility(View.GONE);
        if (mPresenter.isShowBottomAccount()) {
            mLlBottomAccount.setVisibility(View.GONE);
        } else {
            mLlBottomAccount.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.setNewData(mPresenter.getAllGoods());
        }
    }

    /**
     * 失效商品
     */
    @OnClick(R.id.rl_shop_cart_tab_failure_goods)
    public void onClickFailureGoods() {
        mSwipe.setEnableRefresh(false);
        mTabBottomLeftLine.setVisibility(View.GONE);
        mTabBottomRightLine.setVisibility(View.VISIBLE);
        mLlBottomAccount.setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.setNewData(mPresenter.getInvalidGoods());
        }
    }

    @Override
    public void showSelectAll(boolean isSelectAll) {
        mIvCheckAll.setTag(isSelectAll);
        if (isSelectAll) {
            mIvCheckAll.setImageResource(R.drawable.icon_circle_selected);
        } else {
            mIvCheckAll.setImageResource(R.drawable.icon_circle_unselected);
        }
    }

    @Override
    public void notifyItem(int position) {
        if (mAdapter != null)
            mAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyDataChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBottomAccount(boolean isVisible) {
        if (isVisible) {
            mLlBottomAccount.setVisibility(View.VISIBLE);
        } else {
            mLlBottomAccount.setVisibility(View.GONE);
        }
    }

    /**
     * 编辑按钮显示隐藏
     *
     * @param isVisible true显示 false隐藏
     */
    @Override
    public void setModifyBtnVisible(boolean isVisible) {
        if (isVisible) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void setModifyBtnReset() {
        mRightBtn.setText("编辑");
        mLlAccount.setVisibility(View.VISIBLE);
        mLlDelete.setVisibility(View.GONE);
    }

    /**
     * 全选
     */
    @OnClick(R.id.iv_check_all)
    public void onClickAllSelect() {
        Boolean isSelect = (Boolean) mIvCheckAll.getTag();
        mPresenter.setSelectAll(!isSelect);
        mPresenter.updateSelectPrice();
        mAdapter.notifyDataSetChanged();
        mIvCheckAll.setTag(!isSelect);
        if (!isSelect) {
            mIvCheckAll.setImageResource(R.drawable.icon_circle_selected);
        } else {
            mIvCheckAll.setImageResource(R.drawable.icon_circle_unselected);
        }
    }

    /**
     * 结算
     */
    @OnClick(R.id.shopping_cart_account)
    public void onClickAccount() {
        mPresenter.closeAccount();
    }

    /**
     * 编辑完成
     */
    @OnClick(R.id.right_btn)
    public void onClickEditAndSave() {
        if (mRightBtn.getText().equals("完成")) {
            mRightBtn.setText("编辑");
            mLlAccount.setVisibility(View.VISIBLE);
            mLlDelete.setVisibility(View.GONE);
        } else {
            mRightBtn.setText("完成");
            mLlAccount.setVisibility(View.GONE);
            mLlDelete.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 删除
     */
    @OnClick(R.id.btn_shopping_cart_delete)
    public void onClickDelete() {
        if (mPresenter != null)
            mPresenter.deleteGoods();
    }

    /**
     * 移动到收藏夹
     */
    @OnClick(R.id.btn_shopping_cart_collect)
    public void onClickCollect() {
        if (mPresenter != null)
            mPresenter.moveToCollect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PersonInfo info) {
        //退出登录
        if (info.code.equals(EventBusEvent.LOG_OUT)) {
            showLoginView();
        }
        //登录成功
        if (info.code.equals(EventBusEvent.REQUEST_PERSON_INFO)) {
            showShoppingContentView();
            if (mPresenter != null) {
                mPresenter.getCartList(false);
            }
        }
    }

    @OnClick(R.id.tool_bar_right_img)
    public void onMessage() {
        if (LoginHelper.getLoginStatus()) {
            MessageCenterActivity.launch(_mActivity);
        } else {
            LoginActivity.launch(_mActivity);
        }
    }

    /**
     * 登录
     */
    @OnClick(R.id.shopping_cart_login_btn)
    public void onClickLogin() {
        LoginActivity.launch(_mActivity);
    }

    /**
     * 刷新购物车
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshCartInfo info) {
        if (mPresenter != null)
            mPresenter.getCartList(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        if (EventBusEvent.ReLoading.equals(event.eventType)) {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadMessageEvent unReadMessageEvent) {
        qBadgeView.setBadgeNumber(unReadMessageEvent.messageCount);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
