package com.zhaotai.uzao.ui.theme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerPostBean;
import com.zhaotai.uzao.ui.post.activity.PublishPostActivity;
import com.zhaotai.uzao.ui.theme.activity.PostDetailActivity;
import com.zhaotai.uzao.ui.theme.adapter.SceneManagerPostAdapter;
import com.zhaotai.uzao.ui.theme.contract.SceneManagerPostFragmentContract;
import com.zhaotai.uzao.ui.theme.presenter.SceneManagerPostFragmentPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;
import com.zhaotai.uzao.widget.popupwindow.MyPopupWindow;

import butterknife.BindView;

/**
 * description:从收藏的商品选择到主题模块
 * author : ZP
 * date: 2018/1/31 0031.
 */

public class SceneManagerPostFragment extends BaseFragment implements SceneManagerPostFragmentContract.View, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private static final String THEME_ID = "ThemeId";
    private static final String ABLE_MANAGE = "ABLE_MANAGE";
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private SceneManagerPostFragmentPresenter mPresenter;

    private SceneManagerPostAdapter mAdapter;
    private PageInfo<SceneManagerPostBean> data;
    private MyPopupWindow myPopupWindow;
    private int mSelectedPos = 0;
    private String themeId;
    private boolean ableManage;

    public static SceneManagerPostFragment newInstance(String themeId, boolean ableManage) {
        SceneManagerPostFragment sceneManagerPostFragment = new SceneManagerPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(THEME_ID, themeId);
        bundle.putBoolean(ABLE_MANAGE, ableManage);
        sceneManagerPostFragment.setArguments(bundle);
        return sceneManagerPostFragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.fragment_scene_post;
    }

    @Override
    public void initView() {
        ableManage = getArguments().getBoolean(ABLE_MANAGE, false);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        //内容列表
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new SceneManagerPostAdapter(ableManage);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mAdapter.setEmptyStateView(getActivity(), R.drawable.ic_empty_post, getString(R.string.empty_post));
        mAdapter.setOnItemChildClickListener(this);


    }

    @Override
    public void initPresenter() {
        mPresenter = new SceneManagerPostFragmentPresenter(getActivity(), this);
    }

    @Override
    public void initData() {

        if (themeId == null) {
            themeId = getArguments().getString(THEME_ID, "");
        }
        mPresenter.getPostList(0, true, themeId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 列表元素点击事件
     *
     * @param adapter  adapter
     * @param view     点击的view
     * @param position 位置
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SceneManagerPostBean item = mAdapter.getItem(position);
        PostDetailActivity.launch(getActivity(), item.get_id(), item.getContentBody(),themeId);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getPostList(start, false, themeId);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showPostData(PageInfo<SceneManagerPostBean> beans) {
        this.data = beans;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void showDelPostSuccess(int mSelectedPos) {
        ToastUtil.showShort("删除成功");
        mAdapter.remove(mSelectedPos);
    }

    @Override
    public void showDelPostFailed() {
        ToastUtil.showShort("删除失败");
    }

    @Override
    public void showTopPostSuccess() {
        ToastUtil.showShort("置顶成功");
        mPresenter.getPostList(0, false, themeId);
    }

    @Override
    public void showTopPostFailed() {
        ToastUtil.showShort("置顶失败");
    }

    @Override
    public void showEssencePostSuccess(int mSelectedPos) {
        ToastUtil.showShort("加精成功");
        SceneManagerPostBean item = mAdapter.getItem(mSelectedPos);
        item.setIsEssence(item.getIsEssence().equals("N") ? "Y" : "N");
        mAdapter.notifyItemChanged(mSelectedPos);
    }

    @Override
    public void showEssencePostFailed() {
        ToastUtil.showShort("加精失败");
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d("我点击了", "onItemChildClick: " + position);
        displayDialog(view, "Y".equals(mAdapter.getItem(position).getIsEssence()));
        mSelectedPos = position;
    }

    private void displayDialog(View view, boolean isEssence) {
        if (myPopupWindow == null) {
            myPopupWindow = new MyPopupWindow(getActivity());
            myPopupWindow.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SceneManagerPostBean item = mAdapter.getItem(mSelectedPos);
                    if (item == null) {
                        return;
                    }
                    switch (v.getId()) {
                        case R.id.tv_pop_more_del:
                            mPresenter.delPost(item.get_id(), mSelectedPos);
                            break;
                        case R.id.tv_pop_more_top:
                            mPresenter.topPost(item.get_id());
                            break;

                        case R.id.tv_pop_more_editor:
                            String themeId = item.getThemeId();
                            String id = item.get_id();
                            PublishPostActivity.launch(getActivity(), item);
                            ToastUtil.showShort("帖子：" + id + "主题:" + themeId);
                            break;
                        case R.id.tv_pop_more_good:
                            mPresenter.essencePost(item.get_id(), mSelectedPos);
                            break;
                    }
                    myPopupWindow.dismiss();
                }
            });
        }
        int windowPos[] = calculatePopWindowPos(view, myPopupWindow.mContentView);
        int xOff = (int) PixelUtil.dp2px(80);// 可以自己调整偏移
        windowPos[0] = windowPos[0] - xOff;
        Log.d("windowPos", "displayDialog: " + windowPos[0] + " ," + windowPos[1]);
        myPopupWindow.setIsEncess(isEssence);
        myPopupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
//        myPopupWindow.showAsDropDown(view, 0, 0);
        lightOff();


        /**
         * 消失时屏幕变亮
         */
        myPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();

                layoutParams.alpha = 1.0f;

                getActivity().getWindow().setAttributes(layoutParams);
            }
        });
    }

    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);     // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();     // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }


    /**
     * 显示时屏幕变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();

        layoutParams.alpha = 0.3f;

        getActivity().getWindow().setAttributes(layoutParams);
    }

    /**
     * 停止加载更多状态
     */
    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void loadingMoreFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SceneManagerPost", "onActivityResult: " + requestCode + requestCode);
        if (resultCode == 98) {
            mPresenter.getPostList(0, false, themeId);
        }
    }

}
