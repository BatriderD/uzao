package com.zhaotai.uzao.ui.brand;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.brand.adapter.BrandListAdapter;

import butterknife.BindView;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 品牌列表页
 */

public class BrandListActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, BrandListActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_list);
    }

    @Override
    protected void initData() {
        Api.getDefault().getBrandList("0")
                .compose(RxHandleResult.<PageInfo<BrandBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<BrandBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<BrandBean> brandBeanPageInfo) {
                        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                        BrandListAdapter mAdapter = new BrandListAdapter(brandBeanPageInfo.list);
                        mRecycler.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                BrandBean item = (BrandBean) adapter.getItem(position);
                                if (item != null)
                                    BrandActivity.launch(mContext, item.sequenceNBR);
                            }
                        });
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
