package com.zhaotai.uzao.ui.person.attention;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.person.attention.adapter.AttentionBrandAdapter;
import com.zhaotai.uzao.ui.person.attention.contract.AttentionBrandContract;
import com.zhaotai.uzao.ui.person.attention.presenter.AttentionBrandPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/22
 * Created by LiYou
 * Description : 关注的品牌搜索
 */

public class AttentionBrandSearchActivity extends BaseActivity implements AttentionBrandContract.View, TextView.OnEditorActionListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.etd_text)
    EditWithDelView mEtSearch;

    @BindView(R.id.recycler_brand)
    RecyclerView mRecycler;

    private AttentionBrandAdapter mAdapter;
    private AttentionBrandPresenter mPresenter;
    private PageInfo<BrandBean> data = new PageInfo<>();
    private AlertDialog.Builder dialog;
    private String searchWord ="";

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AttentionBrandSearchActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_attention_brand);

        mAdapter = new AttentionBrandAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final BrandBean info = (BrandBean) adapter.getItem(position);
                if (info == null) return;
                switch (view.getId()) {
                    case R.id.rl_attention:
                        //品牌主页
                        BrandActivity.launch(mContext, info.brandId);
                        break;
                    case R.id.tv_brand_attention:
                        //取消关注
                        if (dialog == null) {
                            dialog = new AlertDialog.Builder(mContext);
                            dialog.setMessage("确定不再关注此品牌?");
                            dialog.setCancelable(false);
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.cancelAttentionBrand(info.brandId, position);
                                }
                            });
                        }
                        dialog.show();
                        break;
                }
            }
        });
        mEtSearch.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
        if (mPresenter == null){

            mPresenter = new AttentionBrandPresenter(this, this);
        }
        mPresenter.getBrandList(0, searchWord);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            ToastUtil.showShort("请填写品牌名字");
            return;
        }
        showLoading();
        searchWord = mEtSearch.getText().toString();
        mPresenter.getBrandList(0, searchWord);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
                ToastUtil.showShort("请填写商品名字");
            } else {
                showLoading();
                searchWord = mEtSearch.getText().toString();
                mPresenter.getBrandList(0, searchWord);
            }
            return true;
        }
        return false;
    }

    @Override
    public void showBrandList(PageInfo<BrandBean> list) {
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
    public void cancelAttention(int position) {
        mAdapter.remove(position);
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
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getBrandList(start, searchWord);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
