package com.zhaotai.uzao.ui.person.attention.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MyAttentionDesignerListAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.designer.activity.NewDesignerListActivity;
import com.zhaotai.uzao.ui.person.attention.contract.AttentionDesignerContract;
import com.zhaotai.uzao.ui.person.attention.presenter.AttentionDesignerPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 关注的设计师
 */

public class AttentionDesignerFragment extends BaseFragment implements AttentionDesignerContract.View, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rc_person_collection_product_content)
    RecyclerView mRecycler;

    private MyAttentionDesignerListAdapter mAdapter;
    private AttentionDesignerPresenter mPresenter;
    private PageInfo<PersonBean> data = new PageInfo<>();

    public static AttentionDesignerFragment newInstance() {
        return new AttentionDesignerFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_my_attention_designer;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new MyAttentionDesignerListAdapter(_mActivity);
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final PersonBean info = (PersonBean) adapter.getItem(position);
                if (info == null) return;
                switch (view.getId()) {
                    case R.id.rl_attention:
                        //设计师主页
                        DesignerActivity.launch(_mActivity, info.designerId);
                        break;
                    case R.id.tv_designer_attention:
                        //取消关注
                        AlertDialog.Builder dialog = new AlertDialog.Builder(_mActivity);
                        dialog.setMessage("确定不再关注此人?");
                        dialog.setCancelable(false);
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.cancelAttentionDesigner(info.designerId, position);
                            }
                        }).show();
                        break;
                }

            }
        });

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_status_empty_attention_designer, "关注感兴趣的,让这里都成为你想看的", "立即关注", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                NewDesignerListActivity.launch(getActivity());
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new AttentionDesignerPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getDesignerList(0);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showDesignerList(PageInfo<PersonBean> list) {
        data = list;
        showContent();
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
        if (!list.hasNextPage) {
            mAdapter.loadMoreEnd();
            mAdapter.disableLoadMoreIfNotFullPage(mRecycler);
        }
        mAdapter.loadMoreComplete();
    }

    /**
     * 取消关注设计师
     *
     * @param position 取消的位置
     */
    @Override
    public void cancelAttention(int position) {
        mAdapter.remove(position);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getDesignerList(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }


    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage info) {
        if (EventBusEvent.REFRESH_ATTENTION.equals(info.eventType)) {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
