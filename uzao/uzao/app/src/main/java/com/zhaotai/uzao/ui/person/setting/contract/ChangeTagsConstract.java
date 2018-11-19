package com.zhaotai.uzao.ui.person.setting.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.CategoryTagsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 标签页管理类
 * author : zp
 * date: 2017/7/20
 */

public interface ChangeTagsConstract {
    interface View extends BaseView {

        void showTagList(CategoryTagsBean personTagsBeen);

        void showPersonTagSuccess();

        void showSelectedTags(ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedTagBeans);
    }

    abstract class Presenter extends BasePresenter {


        public abstract void getTagList(String type, List<String> selectedTags);

        public abstract void setPersonalTags(ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedItem);
    }
}
