package com.zhaotai.uzao.ui.person.collection.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.material.activity.MaterialCategoryActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.collection.CollectMaterialSearchActivity;
import com.zhaotai.uzao.ui.person.collection.OnChangeFragmentInterface;
import com.zhaotai.uzao.ui.person.collection.adapter.CollectionMaterialListAdapter;
import com.zhaotai.uzao.ui.person.collection.adapter.PopCategoryAdapter;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionMaterialContract;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;
import com.zhaotai.uzao.ui.person.collection.presenter.CollectionMaterialPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/13
 * Created by LiYou
 * Description : 收藏素材页面
 */

public class CollectionMaterialFragment extends BaseFragment implements CollectionMaterialContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rc_person_collection_product_content)
    RecyclerView mRecycler;

    @BindView(R.id.ll_collect_product)
    LinearLayout mLlTab;

    //管理按钮
    @BindView(R.id.tv_collect_product_title_manager)
    TextView mTitleManager;

    @BindView(R.id.ll_collect_product_bottom)
    RelativeLayout mLlBottom;

    @BindView(R.id.iv_collect_product_all_choose)
    ImageView mImageAllChoose;

    @BindView(R.id.tv_collect_product_num)
    TextView mProductNum;

    @BindView(R.id.view_collect_product_tab_bottom_line_1)
    View mBottomLine1;
    @BindView(R.id.view_collect_product_tab_bottom_line_2)
    View mBottomLine2;

    @BindView(R.id.tv_material_tab_category_name)
    TextView mTabCategoryName;

    private CollectionMaterialListAdapter mAdapter;
    private CollectionMaterialPresenter mPresenter;
    private PageInfo<MaterialListBean> data = new PageInfo<>();
    private boolean allSelectState = false;//记录是否全选
    private boolean isSelectState = false;//默认不可以管理
    private PopupWindow mPop;
    private PopCategoryAdapter mPopAdapter;
    private String categoryCode = "";
    private Map<String, String> params = new HashMap<>();

    public static final int STATUS_NORMAL = 1;//全部素材
    public static final int STATUS_SOLD_OUT = 2;//下架

    private int status = STATUS_NORMAL;

    public static CollectionMaterialFragment newInstance() {
        return new CollectionMaterialFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_collect_material;
    }

    @Override
    public void initView() {
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        mAdapter = new CollectionMaterialListAdapter();
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final MaterialListBean info = (MaterialListBean) adapter.getItem(position);
                if (info == null) return;
                if (mAdapter.getSelectState()) {
                    info.isSelected = !info.isSelected;
                    mAdapter.notifyItemChanged(position);
                    if (mPresenter.checkSelectState(mAdapter.getData())) {
                        mImageAllChoose.setImageResource(R.drawable.icon_circle_selected);
                    } else {
                        mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
                    }
                } else {
                    MaterialDetailActivity.launch(_mActivity, info.materialId);
                }
            }
        });

        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_collect, "快选几件好物放进来吧", getString(R.string.empty_btn), new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                MaterialCategoryActivity.launch(_mActivity);
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new CollectionMaterialPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        mPresenter.getCollectMaterialList(0, mPresenter.createParams(params, status, categoryCode));
        mPresenter.getCollectMaterialCategoryCode();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 切换标题栏
     */
    @OnClick(R.id.tv_collect_top_left)
    public void onClickTopLeft() {
        ((OnChangeFragmentInterface) _mActivity).changeFragment(0);
    }

    /**
     * 主题
     */
    @OnClick(R.id.tv_collect_top_theme)
    public void onClickTopTheme() {
        ((OnChangeFragmentInterface) _mActivity).changeFragment(2);
    }

    @OnClick(R.id.tool_back)
    public void onClickBack() {
        ((OnChangeFragmentInterface) _mActivity).finishView();
    }

    private void initPop() {
        mPop = new PopupWindow(_mActivity);
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View popView = LayoutInflater.from(_mActivity).inflate(R.layout.pop_collect_product, null);
        RecyclerView mCategoryRecycler = (RecyclerView) popView.findViewById(R.id.pop_recycler);
        mPopAdapter = new PopCategoryAdapter();
        mCategoryRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 3));
        mCategoryRecycler.setAdapter(mPopAdapter);
        mPop.setContentView(popView);
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        mPop.setOutsideTouchable(false);
        mPop.setFocusable(true);
    }

    private void showPop() {
        if (mPop != null) {
            mPop.showAsDropDown(mLlTab);
        }
    }

    /**
     * 搜索
     */
    @OnClick(R.id.tv_collect_product_title_search)
    public void onClickSearch() {
        CollectMaterialSearchActivity.launch(_mActivity);
    }

    /**
     * 管理
     */
    @OnClick(R.id.tv_collect_product_title_manager)
    public void onClickManager() {
        if (!isSelectState) return;

        if (mTitleManager.getText().equals("管理")) {
            mTitleManager.setText("取消");
            mLlBottom.setVisibility(View.VISIBLE);
            mAdapter.setSelectState(true);
        } else {
            mTitleManager.setText("管理");
            mLlBottom.setVisibility(View.GONE);
            //重置选中状态
            allSelectState = false;
            mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mPresenter.changeSelectState(data.list, false);
            mAdapter.setSelectState(false);
        }
    }

    /**
     * 全部商品
     */
    @OnClick(R.id.ll_collect_product_all_material)
    public void onClickAllGoods() {
        changeBottomLine(1);
        if (mPop == null) {
            initPop();
            showPop();
        } else {
            showPop();
        }
    }

    /**
     * 下架素材
     */
    @OnClick(R.id.tv_collect_product_out_material)
    public void onClickDownPrice() {
        changeBottomLine(2);
        status = STATUS_SOLD_OUT;
        categoryCode = "";
        hideDelete();
        mPresenter.getCollectMaterialList(0, mPresenter.createParams(params, status, categoryCode));
    }

    /**
     * 隐藏删除
     */
    private void hideDelete() {
        mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
        allSelectState = false;
        mTitleManager.setText("管理");
        mLlBottom.setVisibility(View.GONE);
        mAdapter.setSelectState(false);
    }

    /**
     * 取消收藏素材
     */
    @OnClick(R.id.tv_collect_product_delete)
    public void onClickDelete() {
        //删除收藏的商品
        AlertDialog.Builder alert = new AlertDialog.Builder(_mActivity);
        alert.setMessage("是否删除此素材");
        alert.setNeutralButton("取消", null);
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击删除
                mPresenter.cancelCollection(mAdapter.getData());
            }
        });
        alert.show();
    }

    /**
     * 全选
     */
    @OnClick(R.id.ll_collect_product_bottom)
    public void onClickAllSelect() {
        if (allSelectState) {
            allSelectState = false;
            //变换到未选中
            mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mPresenter.changeSelectState(mAdapter.getData(), false);
            mAdapter.notifyDataSetChanged();
        } else {
            //变换到全选状态
            mImageAllChoose.setImageResource(R.drawable.icon_circle_selected);
            allSelectState = true;
            mPresenter.changeSelectState(mAdapter.getData(), true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCollectionMaterialList(PageInfo<MaterialListBean> list) {
        data = list;
        if (data.list.size() > 0) {
            isSelectState = true;
            if (allSelectState) {
                mPresenter.changeSelectState(data.list, true);
            }
        } else {
            isSelectState = false;
            mTitleManager.setText("管理");
            mLlBottom.setVisibility(View.GONE);
        }

        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    /**
     * 取消收藏
     */
    @Override
    public void cancelCollection() {
        mPresenter.getCollectMaterialList(0, mPresenter.createParams(params, status, categoryCode));
    }

    @Override
    public void showCollectionMaterialCategoryCode(List<CategoryCodeBean> categoryCodeList) {
        initPop();
        mPopAdapter.addData(categoryCodeList);
        mPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryCodeBean item = mPopAdapter.getItem(position);
                if (item == null) return;
                status = STATUS_NORMAL;
                if (position == 0) {
                    categoryCode = "";
                    mPresenter.getCollectMaterialList(0, mPresenter.createParams(params, status, categoryCode));
                } else {
                    categoryCode = item.code;
                    mPresenter.getCollectMaterialList(0, mPresenter.createParams(params, status, categoryCode));
                }
                hideDelete();
                mPop.dismiss();
                mTabCategoryName.setText(item.name);
                List<CategoryCodeBean> data = mPopAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).isSelect = position == i;
                }
                mPopAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 收藏商品数量
     */
    @Override
    public void showProductNum(String num) {
        mProductNum.setText(num);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCollectMaterialList(start, mPresenter.createParams(params, status, categoryCode));
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    public void changeBottomLine(int position) {
        switch (position) {
            case 1:
                mBottomLine1.setVisibility(View.VISIBLE);
                mBottomLine2.setVisibility(View.GONE);
                break;
            case 2:
                mBottomLine1.setVisibility(View.GONE);
                mBottomLine2.setVisibility(View.VISIBLE);
                break;
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

}
