package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.theme.adapter.AddThemeAdapter;
import com.zhaotai.uzao.ui.theme.contract.AddThemeContract;
import com.zhaotai.uzao.ui.theme.presenter.AddThemePresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 添加主题页面
 */

public class AddThemeActivity extends BaseActivity implements AddThemeContract.View, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.right_btn)
    public Button btnRight;
    private AddThemePresenter mPresenter;
    private AddThemeAdapter mAdapter;
    private boolean addHeadView = true;
    private EditText edThemeName;
    private PopupWindow mPopUpWindow;
    private PageInfo<ThemeBean> data;
    private String entityId;
    private String entityType;

    public static final String TYPE_MATERIAL = "material";
    public static final String TYPE_SPU = "spu";

    public static void launch(Context context, String id, String type) {
        Intent intent = new Intent(context, AddThemeActivity.class);
        intent.putExtra("entityId", id);
        intent.putExtra("entityType", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_theme);
        mTitle.setText("添加到主题");
        btnRight.setText(R.string.save);
        btnRight.setVisibility(View.VISIBLE);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddThemeAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mPresenter = new AddThemePresenter(this, this);
    }

    @Override
    protected void initData() {
        entityType = getIntent().getStringExtra("entityType");
        entityId = getIntent().getStringExtra("entityId");
        mPresenter.getThemeList(0);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }


    private void showPopAddTheme() {
        if (mPopUpWindow != null) {
            mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            View itemView = View.inflate(mContext, R.layout.pop_add_new_theme, null);
            itemView.findViewById(R.id.close).setOnClickListener(this);
            itemView.findViewById(R.id.sure).setOnClickListener(this);
            itemView.findViewById(R.id.ed_add_theme_name).setOnClickListener(this);
            edThemeName = (EditText) itemView.findViewById(R.id.ed_add_theme_name);
            edThemeName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            mPopUpWindow = new PopupWindow(itemView, (int) (screenWidth - PixelUtil.dp2px(40)), ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopUpWindow.setFocusable(true);
            mPopUpWindow.setOutsideTouchable(true);
            mPopUpWindow.setBackgroundDrawable(new ColorDrawable());
            mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                mPopUpWindow.dismiss();
                break;
            case R.id.sure:
                String themeName = edThemeName.getText().toString();
                edThemeName.setText("");
                mPopUpWindow.dismiss();
                mPresenter.addTheme(themeName,entityType,entityId);
                break;
            case R.id.ed_add_theme_name:
                break;
        }

    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void loadingMoreFail() {
        mAdapter.loadMoreFail();
    }


    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getThemeList(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showMyThemeList(PageInfo<ThemeBean> data) {
        if (addHeadView) {
            View headView = View.inflate(mContext, R.layout.item_add_theme_headview, null);
            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopAddTheme();
                }
            });
            mAdapter.addHeaderView(headView);
            addHeadView = false;
        }
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
    public void addThemeSuccess(String sequenceNBR) {
//        增加主题成功
        finish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ThemeBean themeBean = (ThemeBean) adapter.getItem(position);
        if (themeBean != null)
            //
            mPresenter.addContentToTheme(themeBean.sequenceNBR, entityType, entityId);
    }
}
