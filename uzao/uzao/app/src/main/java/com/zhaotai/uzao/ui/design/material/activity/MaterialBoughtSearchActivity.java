package com.zhaotai.uzao.ui.design.material.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.ui.design.material.adapter.MaterialSearchBoughtShowListAdapter;
import com.zhaotai.uzao.ui.design.material.presenter.MaterialBoughtSearchPresenter;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description ：我已上传的主题搜索
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MaterialBoughtSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private MaterialSearchBoughtShowListAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MaterialBoughtSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new MaterialSearchBoughtShowListAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new MaterialBoughtSearchPresenter(this, this);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("name", searchWord);
        params.put("status", "inExpired");
        return params;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyMaterialBean bean = mAdapter.getData().get(position);
        if (bean.data != null) {
            //通知编辑器添加一个素材
            AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
            addEditorMaterialBean.thumbnail = bean.getThumbnail();
            addEditorMaterialBean.sourceMaterialId = bean.getSourceMaterialCode();
            addEditorMaterialBean.fileMime = bean.data.fileMime;
            addEditorMaterialBean.resizeScale = bean.data.resizeScale;
            EventBus.getDefault().post(addEditorMaterialBean);
        } else {
            ToastUtil.showShort("素材信息错误");
        }
    }
}
