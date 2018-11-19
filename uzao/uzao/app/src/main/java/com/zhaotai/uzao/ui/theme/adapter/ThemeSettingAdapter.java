package com.zhaotai.uzao.ui.theme.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.widget.CustomGridLayoutManager;

import java.util.List;

/**
 * description: 主题设置adapter
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ThemeSettingAdapter extends BaseQuickAdapter<ThemeModuleBean, BaseViewHolder> {


    private OnModuleClickListener onModuleClickListener;

    public ThemeSettingAdapter() {
        super(R.layout.item_theme_setting);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ThemeModuleBean item) {
        RecyclerView mRecycler = helper.getView(R.id.rc_item_module);
//        设置adapter
        helper.setText(R.id.tv_intro, item.getDescription());

        CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(mContext, 3);
        gridLayoutManager.setScrollEnabled(false);
        mRecycler.setLayoutManager(gridLayoutManager);
        final ThemeSettingModuleAdapter themeSettingModuleAdapter = new ThemeSettingModuleAdapter();
        mRecycler.setAdapter(themeSettingModuleAdapter);
        themeSettingModuleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onModuleClickListener != null) {
                    onModuleClickListener.onModuleClick(item.entityType,item.isCanBeEdit(), themeSettingModuleAdapter, view, position);
                }
            }
        });
        List<ThemeModuleBean.ThemeContentModel> moduleBeens = item.getThemeContentModels();
        themeSettingModuleAdapter.setNewData(moduleBeens);

        //简介
        EditText ed_intro = helper.getView(R.id.tv_intro);
        final TextView tv_theme_intro_left = helper.getView(R.id.tv_intro_left);
        if (item.getDescription() == null) {
            item.setDescription("");
        }
        tv_theme_intro_left.setText("剩余" + (GlobalVariable.THEME_ABOUT_ME_SIZE - item.getDescription().length()) + "字");
        ed_intro.setFilters(new InputFilter[]{new InputFilter.LengthFilter(GlobalVariable.THEME_ABOUT_ME_SIZE)});
        ed_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = GlobalVariable.THEME_ABOUT_ME_SIZE - s.length();
                tv_theme_intro_left.setText("剩余" + length + "字");
            }
        });

        //新的点击事件
        helper.addOnClickListener(R.id.tv_add_module)
                .addOnClickListener(R.id.tv_del_module)
                .addOnClickListener(R.id.tv_edit_module);
        //页面可否编辑
        TextView tvEditor = helper.getView(R.id.tv_edit_module);
        TextView tvDel = helper.getView(R.id.tv_del_module);
        if (item.isCanBeEdit()) {
            tvEditor.setText("保存");
            tvEditor.setSelected(true);
            tvDel.setVisibility(View.VISIBLE);
            tv_theme_intro_left.setVisibility(View.VISIBLE);
            ed_intro.setFocusable(true);
            ed_intro.setFocusableInTouchMode(true);
        } else {
            tvEditor.setText("编辑");
            tvEditor.setSelected(false);
            tvDel.setVisibility(View.GONE);
            tv_theme_intro_left.setVisibility(View.INVISIBLE);
            ed_intro.setFocusable(false);
            ed_intro.setFocusableInTouchMode(false);
        }
    }

    public void setOnModuleClickListener(OnModuleClickListener onModuleClickListener) {
        this.onModuleClickListener = onModuleClickListener;
    }
    public interface OnModuleClickListener {
        void onModuleClick(String entityType, boolean ableAdd, ThemeSettingModuleAdapter adapter, View view, int position);
    }

}
