package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.goods.contract.NavigateProductListContract;
import com.zhaotai.uzao.ui.category.goods.presenter.NavigateProductListPresenter;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import butterknife.BindView;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 导航商品列表
 */

public class NavigateProductListActivity extends BaseActivity implements NavigateProductListContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private NavigateProductListPresenter mPresenter;
    private ProductListAdapter mAdapter;
    private PageInfo<GoodsBean> data = new PageInfo<>();
    /**
     * 类型
     */
    private static final String EXTRA_KEY_TYPE = "extra_key_type";
    /**
     * 专题
     */
    private static final String EXTRA_KEY_TYPE_TOPIC = "extra_key_type_topic";
    /**
     * 导航
     */
    private static final String EXTRA_KEY_TYPE_NAVIGATE = "extra_key_type_navigate";
    /**
     * 首页
     */
    private static final String EXTRA_KEY_TYPE_MAIN = "extra_key_type_main";

    /**
     * 自定义主页-导航商品列表
     *
     * @param context 上下文
     * @param code    分类code
     */
    public static void launchNavigate(Context context, String code) {
        Intent intent = new Intent(context, NavigateProductListActivity.class);
        intent.putExtra("code", code);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_NAVIGATE);
        context.startActivity(intent);
    }

    /**
     * 专题-商品列表
     *
     * @param context   上下文
     * @param type      商品类型
     * @param mongoId   mongoId
     * @param imageId   图片Id
     * @param hotspotId hotspotId
     */
    public static void launchTopic(Context context, String type, String mongoId, String imageId, String hotspotId) {
        Intent intent = new Intent(context, NavigateProductListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("mongoId", mongoId);
        intent.putExtra("imageId", imageId);
        intent.putExtra("hotspotId", hotspotId);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_TOPIC);
        context.startActivity(intent);
    }

    /**
     * 主页来的 商品列表
     *
     * @param context    上下文
     * @param groupType  groupType
     * @param groupCode  groupCode
     * @param entityType entityType
     * @param fieldName  fieldName
     * @param id         id
     */
    public static void launchMain(Context context, String groupType, String groupCode, String entityType, String fieldName, String id) {
        Intent intent = new Intent(context, NavigateProductListActivity.class);
        intent.putExtra("groupType", groupType);
        intent.putExtra("groupCode", groupCode);
        intent.putExtra("entityType", entityType);
        intent.putExtra("fieldName", fieldName);
        intent.putExtra("id", id);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_MAIN);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_navigate_product_list);
        mTitle.setText("商品列表");
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mAdapter = new ProductListAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new NavigateProductListPresenter(this, this);
        }
        autoRefresh();
    }

    @Override
    public void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void stopLoadingMore() {
        if (mAdapter != null)
            mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        if (mAdapter != null)
            mAdapter.loadMoreFail();
    }

    @Override
    public void bindData(PageInfo<GoodsBean> data) {
        this.data = data;
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null) {
            switch (getIntent().getStringExtra(EXTRA_KEY_TYPE)) {
                case EXTRA_KEY_TYPE_MAIN:
                    String groupType = getIntent().getStringExtra("groupType");
                    String groupCode = getIntent().getStringExtra("groupCode");
                    String entityType = getIntent().getStringExtra("entityType");
                    String fieldName = getIntent().getStringExtra("fieldName");
                    String id = getIntent().getStringExtra("id");
                    mPresenter.getMainData(0, groupType, groupCode, entityType, fieldName, id);
                    break;
                case EXTRA_KEY_TYPE_NAVIGATE:
                    String code = getIntent().getStringExtra("code");
                    mPresenter.getNavigateData(0, code);
                    break;
                case EXTRA_KEY_TYPE_TOPIC:
                    String type = getIntent().getStringExtra("type");
                    String mongoId = getIntent().getStringExtra("mongoId");
                    String imageId = getIntent().getStringExtra("imageId");
                    String hotspotId = getIntent().getStringExtra("hotspotId");
                    mPresenter.getTopicData(0, type, mongoId, imageId, hotspotId);
                    break;
            }
        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
        if (goodsBean != null) {
            CommodityDetailMallActivity.launch(mContext, goodsBean.id);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (this.data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            switch (getIntent().getStringExtra(EXTRA_KEY_TYPE)) {
                case EXTRA_KEY_TYPE_MAIN:
                    String groupType = getIntent().getStringExtra("groupType");
                    String groupCode = getIntent().getStringExtra("groupCode");
                    String entityType = getIntent().getStringExtra("entityType");
                    String fieldName = getIntent().getStringExtra("fieldName");
                    String id = getIntent().getStringExtra("id");
                    mPresenter.getMainData(start, groupType, groupCode, entityType, fieldName, id);
                    break;
                case EXTRA_KEY_TYPE_NAVIGATE:
                    String code = getIntent().getStringExtra("code");
                    mPresenter.getNavigateData(start, code);
                    break;
                case EXTRA_KEY_TYPE_TOPIC:
                    String type = getIntent().getStringExtra("type");
                    String mongoId = getIntent().getStringExtra("mongoId");
                    String imageId = getIntent().getStringExtra("imageId");
                    String hotspotId = getIntent().getStringExtra("hotspotId");
                    mPresenter.getTopicData(start, type, mongoId, imageId, hotspotId);
                    break;
            }
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
