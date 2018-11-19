package com.zhaotai.uzao.ui.theme.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerAlbumBean;
import com.zhaotai.uzao.ui.theme.activity.ScenePhotoManagerActivity;
import com.zhaotai.uzao.ui.theme.adapter.SceneManagerPhotoAdapter;
import com.zhaotai.uzao.ui.theme.contract.SceneManagerPhotoFragmentContract;
import com.zhaotai.uzao.ui.theme.presenter.SceneManagerPhotoFragmentPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.CustomSureDialog;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import butterknife.BindView;

/**
 * description:从收藏的商品选择到主题模块
 * author : ZP
 * date: 2018/1/31 0031.
 */

public class SceneManagerPhotoFragment extends BaseFragment implements SceneManagerPhotoFragmentContract.View, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private static final String THEME_ID = "THEME_ID";
    private static final String ABLE_MANAGE = "ABLE_MANAGE";
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private SceneManagerPhotoFragmentPresenter mPresenter;

    private SceneManagerPhotoAdapter mAdapter;
    private PopupWindow mPopUpWindow;
    private EditText edThemeName;
    private String themeId;
    private PageInfo<SceneManagerAlbumBean> data;
    private boolean ableManage;

    public static SceneManagerPhotoFragment newInstance(String themeId, boolean ableManage) {
        SceneManagerPhotoFragment fragment = new SceneManagerPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(THEME_ID, themeId);
        bundle.putBoolean(ABLE_MANAGE, ableManage);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.layout_no_swipe;
    }

    @Override
    public void initView() {
        ableManage = getArguments().getBoolean(ABLE_MANAGE, false);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setBackgroundColor(Color.WHITE);
        //内容列表
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SceneManagerPhotoAdapter(ableManage);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
    }

    @Override
    public void initPresenter() {
        mPresenter = new SceneManagerPhotoFragmentPresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        if (themeId == null) {
            themeId = getArguments().getString(THEME_ID, "");
        }
        mPresenter.getPhotoList(0, true, themeId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    /**
     * 列表元素点击事件 封装点击的数据 发送eventBus到主题模块数据
     *
     * @param adapter  adapter
     * @param view     点击的view
     * @param position 位置
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        SceneManagerAlbumBean item = mAdapter.getItem(position);
        if (item != null) {
            if (item.isAdd) {
                showPopAddPhoto();
            } else {
                ScenePhotoManagerActivity.launch(getActivity(), item.getAlbumName(), item.getSequenceNBR(), ableManage);
            }
        }

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getPhotoList(start, false, themeId);
        } else {
            mAdapter.loadMoreEnd();
        }
    }


    /**
     * popUpWindow 弹窗修改相册名称
     */
    private void showPopAddPhoto() {
        if (mPopUpWindow != null) {
            mPopUpWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            View itemView = View.inflate(getActivity(), R.layout.pop_add_new_theme, null);
            itemView.findViewById(R.id.close).setOnClickListener(this);
            itemView.findViewById(R.id.sure).setOnClickListener(this);
            itemView.findViewById(R.id.ed_add_theme_name).setOnClickListener(this);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText("新建相册");
            edThemeName = (EditText) itemView.findViewById(R.id.ed_add_theme_name);
            edThemeName.setHint("请输入相册名称");
            int screenWidth = ScreenUtils.getScreenWidth(getActivity());
            mPopUpWindow = new PopupWindow(itemView, (int) (screenWidth - PixelUtil.dp2px(40)), ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopUpWindow.setFocusable(true);
            mPopUpWindow.setOutsideTouchable(true);
            mPopUpWindow.setBackgroundDrawable(new ColorDrawable());
            mPopUpWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            lightOff();


            /**
             * 消失时屏幕变亮
             */
            mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();

                    layoutParams.alpha = 1.0f;

                    getActivity().getWindow().setAttributes(layoutParams);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                mPopUpWindow.dismiss();
                break;
            case R.id.sure:
                String titleName = edThemeName.getText().toString();
                edThemeName.setText("");
                mPopUpWindow.dismiss();
                mPresenter.addAlbum(themeId, titleName);

                break;
            case R.id.ed_add_theme_name:
                break;
        }
    }

    @Override
    public void showPhotoData(PageInfo<SceneManagerAlbumBean> data) {
        this.data = data;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            if (ableManage) {
                data.list.add(0, new SceneManagerAlbumBean(true));
            }
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void addAlbumSuccess(String name, String albumId) {
        mPresenter.getPhotoList(0, false, themeId);
        ScenePhotoManagerActivity.launch(getActivity(), name, albumId, ableManage);
    }

    @Override
    public void delAlbumSuccess() {
        ToastUtil.showShort("删除成功");
        mPresenter.getPhotoList(0, false, themeId);
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
        Log.d("SceneManatoFragment", "onActivityResult: " + requestCode + requestCode);
        if (resultCode == 99) {
            mPresenter.getPhotoList(0, false, themeId);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if (view.getId() == R.id.iv_photo_del) {
            new CustomSureDialog(getActivity(), "确定删除该相册?")
                    .setContent(getString(R.string.sure_del_this_album))
                    .setNegativeButton(getResources().getString(R.string.cancel))
                    .setPositiveButton(getResources().getString(R.string.sure))
                    .setListener(new CustomSureDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                mPresenter.delAlbum(mAdapter.getItem(position).getSequenceNBR());
                            }
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
