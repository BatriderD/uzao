package com.zhaotai.uzao.ui.order.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.adapter.ChooseTransportCompanyAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Time: 2018/8/2 0002
 * Created by LiYou
 * Description : 选择物流公司
 */
public class ChooseTransportCompanyActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choose_transport_company);
        mTitle.setText("选择物流公司");
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void initData() {
        Api.getDefault().getAllDictionary("expressCode")
                .compose(RxHandleResult.<List<DictionaryBean>>handleResult())
                .subscribe(new RxSubscriber<List<DictionaryBean>>(mContext, true) {
                    @Override
                    public void _onNext(final List<DictionaryBean> dictionaryBeans) {
                        ChooseTransportCompanyAdapter mAdapter = new ChooseTransportCompanyAdapter(dictionaryBeans);
                        mRecycler.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                DictionaryBean dictionaryBean = dictionaryBeans.get(position);
                                Intent intent = new Intent();
                                intent.putExtra("entryKey", dictionaryBean.entryKey);
                                intent.putExtra("entryName", dictionaryBean.description);
                                setResult(RESULT_OK, intent);
                                finish();
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
