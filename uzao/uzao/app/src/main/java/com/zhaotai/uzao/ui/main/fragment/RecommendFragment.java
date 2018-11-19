package com.zhaotai.uzao.ui.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.EventRecommendBean;
import com.zhaotai.uzao.bean.RecommendBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.designer.activity.NewDesignerListActivity;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;
import com.zhaotai.uzao.ui.main.adapter.RecommendAdapter;
import com.zhaotai.uzao.ui.main.contract.RecommendContract;
import com.zhaotai.uzao.ui.main.presenter.RecommendFragmentPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeListActivity;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.decoration.RecommendItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * description: 新版主页推荐fragment
 * author : zp
 * date: 2017/7/26
 */

public class RecommendFragment extends BaseFragment implements OnRefreshListener, RecommendContract.View, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private RecommendFragmentPresenter mPresenter;
    private RecommendAdapter mAdapter;

    private ArrayList<RecommendBean> data;
    private AlertDialog.Builder dialog;


    @Override
    protected int layoutId() {
        return R.layout.frag_main_child;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        showContent();
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击刷新
                initData();
                //通知mainFragment去请求首页tab信息
                EventBus.getDefault().post(new EventMessage(EventBusEvent.REFRESH_MAIN_DATA));
            }
        });
        //设置下拉刷新监听
        mSwipe.setOnRefreshListener(this);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, 2);
        mRecycler.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void initPresenter() {
        //初始化presenter
        mPresenter = new RecommendFragmentPresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        showContent();
        autoRefresh();
    }

    @Override
    protected boolean hasLazy() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startBannerPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopBannerPlay();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getRecommendBean();
    }

    @Override
    public void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void showData(ArrayList<RecommendBean> recommendBeans) {
        data = recommendBeans;
        if (mAdapter == null) {
            mRecycler.addItemDecoration(new RecommendItemDecoration((int) PixelUtil.dp2px(15), (int) PixelUtil.dp2px(5), data));
            mAdapter = new RecommendAdapter(recommendBeans);
            mRecycler.setAdapter(mAdapter);
            mAdapter.setEnableLoadMore(false);
            //设置adapter的条目占一格还是两格
            mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    switch (data.get(position).getItemType()) {
                        case RecommendBean.NEW_MATERIAL:
                        case RecommendBean.NEW_PRODUCT:
                        case RecommendBean.ORIGINAL_PRODUCT:
                            return 1;
                        case RecommendBean.BANNERS:
                        case RecommendBean.TITLE:
                        case RecommendBean.DAY_DESIGN:
                        case RecommendBean.HOT_THEME:
                        case RecommendBean.GOOD_DESIGNER:
                        case RecommendBean.END:
                            return 2;
                        default:
                            return 1;
                    }
                }
            });
            mAdapter.setOnItemChildClickListener(this);
        } else {
            mAdapter.setNewData(data);
        }
    }

    /**
     * 修改列表中的关注状态
     *
     * @param pos 位置
     * @param b   关注状态 true 关注 false 未关注
     */
    @Override
    public void showDesignerStatus(int pos, boolean b) {
        RecommendBean.ValueBean valueBean = data.get(pos).valueBean;
        String contentBody = valueBean.getContentBody();
        RecommendBean.AuthorContentBody goodDesignerAuthorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
        goodDesignerAuthorContentBody.setIsAttention(b ? "Y" : "N");
        String strGoodDesigner = GsonUtil.getGson().toJson(goodDesignerAuthorContentBody);
        valueBean.setContentBody(strGoodDesigner);
        mAdapter.notifyItemChanged(pos);
    }

    /**
     * 通知区域刷新
     *
     * @param pos  修改的位置
     * @param body 修改用户数据
     */
    @Override
    public void notifyItemChange(int pos, RecommendBean.AuthorContentBody body) {
        if (data != null && data.size() >= pos && mAdapter != null) {
            RecommendBean.ValueBean valueBean = data.get(pos).valueBean;
            if (valueBean != null) {
                String contentBody = GsonUtil.getGson().toJson(body);
                valueBean.setContentBody(contentBody);
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(pos);
        }
    }

    /**
     * 改变关注状态
     *
     * @param userId      用户id
     * @param position    位置
     * @param isAttention 关注状态
     */
    private void changeAttention(final String userId, final int position, boolean isAttention) {
        if (isAttention) {
//            关注状态
            if (dialog == null) {
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("确定不再关注此人?");
                dialog.setCancelable(false);
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.changeAttention(userId, position, false);
                    }
                });
            }
            dialog.show();

        } else {
            //关注
            mPresenter.changeAttention(userId, position, true);
        }
    }

    /**
     * 列表元素点击事件
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_attention:
                //点击关注
                if (LoginHelper.getLoginStatus()) {
                    //已经登录了
                    RecommendBean recommendBean = data.get(position);
                    RecommendBean.ValueBean valueBean = recommendBean.valueBean;
                    final String contentBody = valueBean.getContentBody();
                    final RecommendBean.AuthorContentBody goodDesignerAuthorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
                    String userId = goodDesignerAuthorContentBody.getUserId();
                    changeAttention(userId, position, "Y".equals(goodDesignerAuthorContentBody.isAttention));
                } else {
                    //未登录
                    LoginMsgActivity.launch(getActivity());
                }

                break;
            case R.id.rl_more:
                //更多设计师
                NewDesignerListActivity.launch(getActivity());
                break;

            case R.id.rl_theme_more:
                // 更多主题
                ThemeListActivity.launch(getActivity());
                break;
            case R.id.iv_head:
                //设计师头像
                RecommendBean.ValueBean valueBean = mAdapter.getData().get(position).valueBean;
                RecommendBean.AuthorContentBody authorContentBody = GsonUtil.getGson().fromJson(valueBean.contentBody, RecommendBean.AuthorContentBody.class);
                String userId = authorContentBody.getUserId();
                DesignerActivity.recommendLaunch(getActivity(), userId, position);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventRecommendBean event) {
        //通知修改设计师状态
        showDesignerStatus(event.pos, event.attentionStatus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
