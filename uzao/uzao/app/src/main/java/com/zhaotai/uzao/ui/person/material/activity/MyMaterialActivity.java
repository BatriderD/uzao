package com.zhaotai.uzao.ui.person.material.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MyBoughtMaterialCategoryBean;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.material.activity.MaterialCategoryActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.material.adapter.MyMaterialAdapter;
import com.zhaotai.uzao.ui.person.material.adapter.MyMaterialCategoryAdapter;
import com.zhaotai.uzao.ui.person.material.adapter.MyUploadMaterialAdapter;
import com.zhaotai.uzao.ui.person.material.contract.MyMaterialContract;
import com.zhaotai.uzao.ui.person.material.presenter.MyMaterialPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 我的素材
 */

public class MyMaterialActivity extends BaseActivity implements MyMaterialContract.View {

    @BindView(R.id.rl_bought_material)
    public RelativeLayout rl_bought_material;

    @BindView(R.id.tv_bought_material_num)
    public TextView tv_bought_num;
    @BindView(R.id.tv_bought_material)
    public TextView tv_bought_name;

    @BindView(R.id.tv_my_upload_material)
    public TextView tv_my_upload_material;

    @BindView(R.id.iv_arr)
    public ImageView iv_arr;

    @BindView(R.id.recycler)
    public RecyclerView recycler;

    @BindView(R.id.rl_rl_category)
    public RecyclerView rlRlCategory;

    @BindView(R.id.ll_my_material_category)
    public LinearLayout ll_category;

    @BindView(R.id.v_bought_line)
    public View v_bought_line;
    @BindView(R.id.v_upload_line)
    public View v_upload_line;

    @BindView(R.id.tool_bar_right_img)
    public ImageView iv_bar_right_img;

    private MyMaterialPresenter mPresenter;
    private PageInfo<MyMaterialBean> data;
    private MyMaterialAdapter myMaterialAdapter;
    private MyMaterialCategoryAdapter myMaterialCategoryAdapter;
    private String categoryCode = "";
    private MyUploadMaterialAdapter myUploadAdapter;
    private PageInfo<MyUploadMaterialBean> upLoadData;
    private int totalCount;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyMaterialActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_material);
        mTitle.setText("我的素材");
        iv_bar_right_img.setVisibility(View.VISIBLE);
        iv_bar_right_img.setImageResource(R.drawable.icon_search_black);

        recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rlRlCategory.setLayoutManager(new GridLayoutManager(mContext, 3));
        ((SimpleItemAnimator) rlRlCategory.getItemAnimator()).setSupportsChangeAnimations(false);
        //设置默认view选中
        rl_bought_material.setSelected(true);
        v_bought_line.setVisibility(View.VISIBLE);
        v_upload_line.setVisibility(View.INVISIBLE);
        mPresenter = new MyMaterialPresenter(this, this);
        myMaterialAdapter = new MyMaterialAdapter();
        recycler.setAdapter(myMaterialAdapter);
        myMaterialAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (data.hasNextPage) {
                    int start = data.pageStartRow + data.pageRecorders;
                    //加载列表数据
                    mPresenter.getMyBoughtMaterial(start, categoryCode);
                } else {
                    myMaterialAdapter.loadMoreEnd();
                }
            }
        }, recycler);
        myMaterialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MaterialDetailActivity.launch(MyMaterialActivity.this, myMaterialAdapter.getData().get(position).getSourceMaterialId());
            }
        });
        myMaterialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                MyMaterialBean myMaterialBean = myMaterialAdapter.getData().get(position);
                switch (id) {
                    case R.id.tv_material_del:
                        //删除素材
                        mPresenter.delMaterial(myMaterialBean.getSequenceNBR(), position);
                        break;
                    case R.id.tv_material_buy_again:
                        //重新购买 跳转页面
                        MaterialDetailActivity.launch(MyMaterialActivity.this, myMaterialAdapter.getData().get(position).getSourceMaterialId());
                        break;
                }
            }
        });


        myMaterialCategoryAdapter = new MyMaterialCategoryAdapter();
        rlRlCategory.setAdapter(myMaterialCategoryAdapter);
        myMaterialCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyBoughtMaterialCategoryBean myBoughtMaterialCategoryBean = myMaterialCategoryAdapter.getData().get(position);
                myMaterialCategoryAdapter.setselected(position);
                ll_category.setVisibility(View.GONE);
                mPresenter.getMyBoughtMaterial(0, myBoughtMaterialCategoryBean.getCode());
                tv_bought_name.setText(myBoughtMaterialCategoryBean.getName());
                if (position == 0) {
                    tv_bought_num.setVisibility(View.VISIBLE);
                } else {
                    tv_bought_num.setVisibility(View.GONE);
                }
            }
        });

        myMaterialAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty_1, "还没有任何素材，快快去购买哦", getString(R.string.empty_btn), new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                MaterialCategoryActivity.launch(mContext);
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getMyBoughtMaterial(0, categoryCode);
        mPresenter.getMyBoughtMaterialCategory();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    public void selectedMyBoughtMaterial(boolean selectLeft) {
        if (selectLeft) {
            v_bought_line.setVisibility(View.VISIBLE);
            v_upload_line.setVisibility(View.INVISIBLE);
            if (rl_bought_material.isSelected()) {
                ll_category.setVisibility(ll_category.getVisibility() == View.GONE || ll_category.getVisibility()
                        == View.INVISIBLE && myMaterialCategoryAdapter.getData().size() != 0 ? View.VISIBLE : View.GONE);
            } else {
                rl_bought_material.setSelected(true);
                tv_my_upload_material.setSelected(false);
                categoryCode = "";
                recycler.setAdapter(myMaterialAdapter);
                List<MyBoughtMaterialCategoryBean> data = myMaterialCategoryAdapter.getData();
                if (data != null && data.size() > 0) {
                    MyBoughtMaterialCategoryBean myBoughtMaterialCategoryBean = data.get(myMaterialCategoryAdapter.getSelected());
                    if (myBoughtMaterialCategoryBean != null && myBoughtMaterialCategoryBean.getCode() != null) {
                        mPresenter.getMyBoughtMaterial(0, myBoughtMaterialCategoryBean.getCode());
                    }
                }
            }


        } else {
            v_bought_line.setVisibility(View.INVISIBLE);
            v_upload_line.setVisibility(View.VISIBLE);
            rl_bought_material.setSelected(false);
            ll_category.setVisibility(View.GONE);
            tv_my_upload_material.setSelected(true);
            if (myUploadAdapter == null) {
                myUploadAdapter = new MyUploadMaterialAdapter();
                myUploadAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        if (upLoadData.hasNextPage) {
                            int start = upLoadData.pageStartRow + upLoadData.pageRecorders;
                            //加载列表数据
                            mPresenter.getUpLoadMaterialList(start);
                        } else {
                            myUploadAdapter.loadMoreEnd();
                        }
                    }
                }, recycler);
                myUploadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        MaterialDetailActivity.launch(MyMaterialActivity.this, myUploadAdapter.getData().get(position).sequenceNBR);
                    }
                });
                myUploadAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty_1, "还没有上传任何素材，快快去设计吧");
            }
            recycler.setAdapter(myUploadAdapter);
            mPresenter.getUpLoadMaterialList(0);
        }

    }

    @Override
    public void showMaterialList(PageInfo<MyMaterialBean> data) {
        this.data = data;
        myMaterialAdapter.loadMoreComplete();
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            myMaterialAdapter.setNewData(data.list);
            totalCount = data.totalRows;
            tv_bought_num.setText("(" + totalCount + ")");
        } else {
//            不是第一页 就刷新
            myMaterialAdapter.addData(data.list);
        }
    }

    @Override
    public void showCategoryList(List<MyBoughtMaterialCategoryBean> beans) {
        beans.add(0, new MyBoughtMaterialCategoryBean("已购素材", ""));
        myMaterialCategoryAdapter.setNewData(beans);
        myMaterialCategoryAdapter.setselected(0);
    }

    @Override
    public void showUploadList(PageInfo<MyUploadMaterialBean> info) {
        this.upLoadData = info;
        myUploadAdapter.loadMoreComplete();
        if (upLoadData.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            myUploadAdapter.setNewData(upLoadData.list);
        } else {
//            不是第一页 就刷新
            myUploadAdapter.addData(upLoadData.list);
        }
    }

    @Override
    public void showDelSuccess(int position) {
        myMaterialAdapter.remove(position);
        totalCount = totalCount - 1;
        tv_bought_num.setText("(" + totalCount + ")");
        ToastUtil.showShort("删除成功");
    }


    /**
     * 进入搜索页面
     */
    @OnClick(R.id.tool_bar_right_img)
    public void goSearch() {
        boolean bought = rl_bought_material.isSelected();
        if (bought) {
            MyUpLoadMaterialSearchActivity.launch(mContext);
        } else {
            MyUpLoadMaterialSearchActivity.launch(mContext);
        }

    }


    /**
     * 切换搜索类别按钮
     *
     * @param view 点击的搜索view
     */
    @OnClick({R.id.rl_bought_material, R.id.tv_my_upload_material})
    public void chooseMaterialType(View view) {
        if (R.id.rl_bought_material == view.getId()) {
            selectedMyBoughtMaterial(true);

        } else {
            selectedMyBoughtMaterial(false);
        }
    }

    @Override
    public void stopLoadingMore() {

    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {

    }
}
