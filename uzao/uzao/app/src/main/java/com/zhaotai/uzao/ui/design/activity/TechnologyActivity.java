package com.zhaotai.uzao.ui.design.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.TechnologyEvent;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.TechnologyBean;
import com.zhaotai.uzao.ui.design.adapter.TechnologyAdapter;
import com.zhaotai.uzao.ui.design.contract.TechnologyContract;
import com.zhaotai.uzao.ui.design.presenter.TechnologyPresenter;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 工艺页面
 * author : ZP
 * date: 2017/12/13 0013.
 */

public class TechnologyActivity extends BaseActivity implements TechnologyContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_technology)
    public RecyclerView rcTechnology;


    @BindView(R.id.tv_technology_title)
    public TextView tv_technology_title;

    @BindView(R.id.tv_technology_content)
    public TextView tv_technology_content;

    @BindView(R.id.iv_technology_content)
    public ImageView iv_technology_content;

    @BindView(R.id.tool_next)
    public ImageView tool_next;

    private TechnologyPresenter mPresenter;
    private TechnologyAdapter technologyAdapter;
    private PageInfo<TechnologyBean> data;

    /**
     * 调起本页面
     */

    public static void launch(Context context, String technologyBeens, int pos) {
        Intent intent = new Intent(context, TechnologyActivity.class);
        intent.putExtra("technologyBeans", technologyBeens);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_technology);

        mPresenter = new TechnologyPresenter(this, this);

        technologyAdapter = new TechnologyAdapter();
        rcTechnology.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rcTechnology.setAdapter(technologyAdapter);
        technologyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//               换内容
                technologyAdapter.setselected(position);
                showSelectedInfo(technologyAdapter.getItem(position));
            }
        });
        tv_technology_content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }

    @Override
    protected void initData() {
//        ArrayList<TechnologyBean> technologyBeens = (ArrayList<TechnologyBean>) getIntent().getSerializableExtra("technologyBeens");
        String technologyBeens = getIntent().getStringExtra("technologyBeans");
        Type mType = new TypeToken<List<TechnologyBean>>() {
        }.getType();
        List<TechnologyBean> jsonBean = GsonUtil.getGson().fromJson(technologyBeens, mType);

        if (jsonBean != null && jsonBean.size() != 0) {
            technologyAdapter.setNewData(jsonBean);
            int pos = getIntent().getIntExtra("pos", 0);
            if (jsonBean.size() != 0 && jsonBean.size() >= pos) {
                TechnologyBean technologyBean = jsonBean.get(pos);
                technologyBean.selected = true;
                showSelectedInfo(technologyBean);
                technologyAdapter.setselected(pos);
            }
        } else {
            mPresenter.getTechnology(Constant.PAGING_HOME);
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    public void showTechnologyList(PageInfo<TechnologyBean> technologyBeanPageInfos) {
        this.data = technologyBeanPageInfos;
        if (technologyBeanPageInfos.currentPage == 1) {
            TechnologyBean technologyBean = technologyBeanPageInfos.list.get(0);
            technologyBean.selected = true;
            showSelectedInfo(technologyBean);
            technologyAdapter.setNewData(technologyBeanPageInfos.list);
            technologyAdapter.setselected(0);
        } else {
            technologyAdapter.addData(technologyBeanPageInfos.list);
        }
    }

    @Override
    public void showTechnologyListFailed() {

    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getTechnology(start);
        } else {
            technologyAdapter.loadMoreEnd();
        }
    }

    /**
     * 更改选中的工艺信息
     *
     * @param bean 工艺信息
     */
    private void showSelectedInfo(TechnologyBean bean) {
        tv_technology_title.setText(bean.getCraftName());
        tv_technology_content.setText(bean.getDescription());
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + bean.getThumbnail(), iv_technology_content);
    }

    /**
     * 下一步按钮点击事件
     */
    @OnClick(R.id.tool_next)
    public void onSelectedTechnology() {
        int selected = technologyAdapter.getSelected();
        TechnologyBean bean = technologyAdapter.getData().get(selected);
        TechnologyEvent technologyEvent = new TechnologyEvent(selected, bean.getCraftName());
        EventBus.getDefault().post(new EventBean<>(technologyEvent, EventBusEvent.SELECTED_TECHNOLOGY));
        finish();
    }
}
