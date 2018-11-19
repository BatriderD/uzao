package com.zhaotai.uzao.ui.person.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.person.setting.adapter.TagsAdapter;
import com.zhaotai.uzao.ui.person.setting.contract.ChangeTagsConstract;
import com.zhaotai.uzao.ui.person.setting.presenter.ChangeTagsPresenter;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 修改标签页面activity
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ChangeTagsActivity extends BaseActivity implements ChangeTagsConstract.View {

    //    载体：sample，商品：spu，素材：material，设计师：designer，主题：theme，品牌：brand
    public static final String TAG_TYPE = "tag_type";
    public static final String TAG_TYPE_sample = "sample";
    public static final String TAG_TYPE_spu = "spu";
    public static final String TAG_TYPE_material = "material";
    public static final String TAG_TYPE_designer = "designer";
    public static final String TAG_TYPE_theme = "theme";
    public static final String TAG_TYPE_brand = "brand";

    public static final String SELECTED_TAG = "selected_tag";
    @BindView(R.id.recycler)
    public RecyclerView recyclerView;


    //已选择的标签
    @BindView(R.id.id_flowlayout)
    public TagFlowLayout mFlowLayout;

    private ChangeTagsPresenter mPresenter;
    private TagsAdapter mAdapter;
    private int MAXCOUNT = 5;
    private ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedItem = new ArrayList<>();
    private TagAdapter tagAdapter;
    private String tagType;

    public static void launch(Context context, String type) {
        Intent intent = new Intent(context, ChangeTagsActivity.class);
        intent.putExtra(TAG_TYPE, type);
        context.startActivity(intent);
    }

    public static void launch(Context context, String type, ArrayList<String> tags) {
        Intent intent = new Intent(context, ChangeTagsActivity.class);
        intent.putExtra(TAG_TYPE, type);
        intent.putStringArrayListExtra(SELECTED_TAG, tags);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_tags);
        mTitle.setText(getString(R.string.choose_tags));
        mPresenter = new ChangeTagsPresenter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TagsAdapter();
        recyclerView.setAdapter(mAdapter);
        //已选择的标签
        tagAdapter = new TagAdapter<CategoryTagsBean.ChildrenBean.TagsBean>(selectedItem) {
            @Override
            public View getView(FlowLayout parent, int position, CategoryTagsBean.ChildrenBean.TagsBean s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.item_flowlayout_selected_tags, null);
                tv.setText(s.getTagName());
                return tv;
            }
        };

        mFlowLayout.setAdapter(tagAdapter);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position == -1) {
                    return false;
                }
                CategoryTagsBean.ChildrenBean.TagsBean remove = selectedItem.remove(position);
                List<CategoryTagsBean.ChildrenBean> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    CategoryTagsBean.ChildrenBean childrenBean = data.get(i);
                    List<CategoryTagsBean.ChildrenBean.TagsBean> tags = childrenBean.getTags();
                    for (int j = 0; j < tags.size(); j++) {
                        CategoryTagsBean.ChildrenBean.TagsBean tagsBean = tags.get(j);
                        if (tagsBean.equals(remove)) {
                            tagsBean.setSelected(false);
                            mFlowLayout.setAdapter(tagAdapter);
                            mAdapter.notifyDataSetChanged();
                            return true;
                        }
                    }
                }
                mFlowLayout.setAdapter(tagAdapter);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        tagType = intent.getStringExtra(TAG_TYPE);
        ArrayList<String> spuTag = intent.getStringArrayListExtra(SELECTED_TAG);


        mPresenter.getTagList(tagType, spuTag);
        showContent();
        mAdapter.setOnMyclickerListener(new TagsAdapter.MyItemClickListener() {

            @Override
            public void itemClick(View view, CategoryTagsBean.ChildrenBean.TagsBean item) {

                if (selectedItem.contains(item)) {
                    selectedItem.remove(item);
                    item.setSelected(false);
                    mFlowLayout.setAdapter(tagAdapter);
                } else {
                    if (selectedItem.size() < MAXCOUNT) {
                        selectedItem.add(item);
                        item.setSelected(true);
                        mFlowLayout.setAdapter(tagAdapter);
                    } else {
                        ToastUtil.showShort("最多只能选择" + MAXCOUNT + "个标签");
                    }
                }
//                if (item.isSelected()) {
//                    selectedItem.remove(item);
//                    item.setSelected(false);
//                    mFlowLayout.setAdapter(tagAdapter);
//                } else {
//                    if (selectedItem.size() < MAXCOUNT) {
//                        selectedItem.add(item);
//                        item.setSelected(true);
//                        mFlowLayout.setAdapter(tagAdapter);
//                    } else {
//                        ToastUtil.showShort("最多只能选择" + MAXCOUNT + "个标签");
//                    }
//                }
                mAdapter.notifyDataSetChanged();
                mFlowLayout.invalidate();
            }
        });
    }


    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showTagList(CategoryTagsBean personTagsBeen) {
        mAdapter.setNewData(personTagsBeen.getChildren());
    }

    @Override
    public void showPersonTagSuccess() {
        EventBus.getDefault().post(new EventBean<>(selectedItem, EventBusEvent.CHOOSE_TAG_Person_Tag_FINISH));
        finish();
    }

    /**
     * 已选择的标签
     */
    @Override
    public void showSelectedTags(ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedTagBeans) {
        selectedItem = selectedTagBeans;
        tagAdapter = new TagAdapter<CategoryTagsBean.ChildrenBean.TagsBean>(selectedItem) {
            @Override
            public View getView(FlowLayout parent, int position, CategoryTagsBean.ChildrenBean.TagsBean s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.item_flowlayout_selected_tags, null);
                tv.setText(s.getTagName());
                return tv;
            }
        };
        mFlowLayout.setAdapter(tagAdapter);
    }

    /**
     * 标签选择完成
     */
    @OnClick(R.id.right_btn)
    public void onFinishTagsSelected() {
        if (TAG_TYPE_designer.equals(tagType)) {
            mPresenter.setPersonalTags(selectedItem);
        } else if (TAG_TYPE_spu.equals(tagType)) {
            EventBus.getDefault().post(new EventBean<>(selectedItem, EventBusEvent.CHOOSE_TAG_SPU_TAG_FINISH));
            finish();
        }else if (TAG_TYPE_theme.equals(tagType)) {
            EventBus.getDefault().post(new EventBean<>(selectedItem, EventBusEvent.CHOOSE_TAG_THEME_TAG_FINISH));
            finish();
        }
    }
}
