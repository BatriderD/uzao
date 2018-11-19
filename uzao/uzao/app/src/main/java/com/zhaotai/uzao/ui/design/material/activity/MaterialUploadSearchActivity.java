package com.zhaotai.uzao.ui.design.material.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.ui.design.material.adapter.MaterialSearchUploadShowListAdapter;
import com.zhaotai.uzao.ui.design.material.presenter.MaterialUploadSearchPresenter;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description ：我已上传的主题搜索
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MaterialUploadSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private MaterialSearchUploadShowListAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MaterialUploadSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new MaterialSearchUploadShowListAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new MaterialUploadSearchPresenter(this, this);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("materialName", searchWord);
        params.put("status", "published");

        return params;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyUploadMaterialBean myMaterialBean = mAdapter.getData().get(position);
        //通知编辑器添加素材
        AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
        addEditorMaterialBean.thumbnail = myMaterialBean.getThumbnail();
        addEditorMaterialBean.sourceMaterialId = myMaterialBean.getSequenceNbr();
        addEditorMaterialBean.fileMime = myMaterialBean.getFileMime();
        addEditorMaterialBean.resizeScale = myMaterialBean.getScale();
        EventBus.getDefault().post(addEditorMaterialBean);
        finish();
    }
}
