package com.zhaotai.uzao.ui.person.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.collection.adapter.CollectionMaterialListAdapter;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionMaterialSearchContract;
import com.zhaotai.uzao.ui.person.collection.presenter.CollectionMaterialSearchPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/11/22
 * Created by LiYou
 * Description : 收藏的素材 搜索
 */

public class CollectMaterialSearchActivity extends BaseActivity implements CollectionMaterialSearchContract.View, TextView.OnEditorActionListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.etd_text)
    EditWithDelView mEtSearch;

    @BindView(R.id.recycler_collect)
    RecyclerView mRecycler;

    private CollectionMaterialListAdapter mAdapter;
    private CollectionMaterialSearchPresenter mPresenter;
    private PageInfo<MaterialListBean> data = new PageInfo<>();
    private String searchWord = "";

    public static void launch(Context context) {
        context.startActivity(new Intent(context, CollectMaterialSearchActivity.class));
    }

    public static void launch(Context context, String type) {
        Intent intent = new Intent(context, CollectMaterialSearchActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_collect_product);

        mAdapter = new CollectionMaterialListAdapter();

        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("design".equals(getIntent().getStringExtra("type"))) {
                    MaterialListBean bean = mAdapter.getData().get(position);

                    AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
                    addEditorMaterialBean.thumbnail = bean.thumbnail;
                    addEditorMaterialBean.sourceMaterialId = bean.materialId;
                    addEditorMaterialBean.fileMime = bean.fileMime;
                    addEditorMaterialBean.resizeScale = Float.valueOf(bean.scale);
                    EventBus.getDefault().post(addEditorMaterialBean);
                    finish();
                } else {
                    //点击跳转素材详情
                    MaterialDetailActivity.launch(mContext, ((MaterialListBean) adapter.getItem(position)).materialId);
                }
            }
        });
        mEtSearch.setOnEditorActionListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new CollectionMaterialSearchPresenter(this, this);
        }
        mPresenter.getCollectMaterialList(0, searchWord);

    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            ToastUtil.showShort("请填写素材名字");
            return;
        }
        showLoading();
        searchWord = mEtSearch.getText().toString().trim();
        mPresenter.getCollectMaterialList(0, searchWord);
    }

    @Override
    public void showCollectionMaterialList(PageInfo<MaterialListBean> list) {
        data = list;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
                ToastUtil.showShort("请填写商品名字");
            } else {
                showLoading();
                searchWord = mEtSearch.getText().toString().trim();
                mPresenter.getCollectMaterialList(0, searchWord);
            }
            return true;
        }
        return false;
    }


    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCollectMaterialList(start, searchWord);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
