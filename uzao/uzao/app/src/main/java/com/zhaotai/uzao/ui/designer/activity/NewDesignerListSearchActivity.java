package com.zhaotai.uzao.ui.designer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.designer.adapter.DesignerListAdapter;
import com.zhaotai.uzao.ui.designer.contract.NewDesignerListSearchContract;
import com.zhaotai.uzao.ui.designer.presenter.NewDesignerListSearchPresenter;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * description ：添加收藏的素材到主题设置界面
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class NewDesignerListSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, NewDesignerListSearchContract.View {

    private DesignerListAdapter mAdapter;
    private AlertDialog.Builder dialog;
    private String type;

    public static void launch(Context context) {
        Intent intent = new Intent(context, NewDesignerListSearchActivity.class);
        context.startActivity(intent);
    }
    public static void launchAddTheme(Context context,String type) {
        Intent intent = new Intent(context, NewDesignerListSearchActivity.class);
        intent.putExtra("TYPE","ADD_THEME");
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        type = getIntent().getStringExtra("TYPE");
        super.initView();
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new DesignerListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        return mAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new NewDesignerListSearchPresenter(this, this);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("queryWord", searchWord);
        return params;
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if (!LoginHelper.getLoginStatus()) {
            ToastUtil.showShort("请先登录");
            LoginActivity.launch(this);
            return;
        }
        if (mPresenter instanceof NewDesignerListSearchPresenter) {
            final NewDesignerListSearchPresenter presenter = (NewDesignerListSearchPresenter) this.mPresenter;
            final DesignerBean designerBean = mAdapter.getData().get(position);
            String isFavorited = designerBean.data.isFavorited;
            if (designerBean.data != null && "Y".equals(isFavorited)) {
//            关注状态

                if (dialog == null) {
                    dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("确定不再关注此人?");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.cancelDesigner(position, designerBean.userId);
                        }
                    });
                }
                dialog.show();

            } else {
                presenter.attentionDesigner(position, designerBean.userId);
            }
        }

    }

    @Override
    public void changeDesigner(int pos, boolean status) {
        DesignerBean designerBean = mAdapter.getData().get(pos);
        if (status) {
            designerBean.favoriteCount = designerBean.favoriteCount + 1;
        } else {
            designerBean.favoriteCount = designerBean.favoriteCount - 1;
        }
        designerBean.data.isFavorited = status ? "Y" : "N";
        mAdapter.notifyItemChanged(pos);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage info) {
        if (EventBusEvent.REFRESH_ATTENTION.equals(info.eventType)) {
            mPresenter.getCommodityList(0, params);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DesignerBean designerBean = mAdapter.getData().get(position);
        if ("ADD_THEME".equals(type)) {
            ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
            //增加进入新增的进入主题
            contentModel.setEntityType(ThemeModuleBean.TYPE_DESIGNER);
            contentModel.setEntityId(designerBean.userId);
            contentModel.setEntityName(designerBean.nickName);
            contentModel.setEntityPic(designerBean.avatar);
            EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
            finish();
        } else {
            DesignerActivity.launch(this, designerBean.userId);
        }

    }
}
