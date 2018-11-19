package com.zhaotai.uzao.ui.person.setting.presenter;

import android.content.Context;
import android.util.Log;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.setting.contract.ChangeTagsConstract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 修改标签presenter
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ChangeTagsPresenter extends ChangeTagsConstract.Presenter {
    private ChangeTagsConstract.View mView;

    public ChangeTagsPresenter(Context context, ChangeTagsConstract.View view) {
        mContext = context;
        this.mView = view;
    }

    @Override
    public void getTagList(String type, final List<String> selectedTags) {
        Api.getDefault().getTagList(type).compose(RxHandleResult.<CategoryTagsBean>handleResult())
                .subscribe(new RxSubscriber<CategoryTagsBean>(mContext) {
                    @Override
                    public void _onNext(CategoryTagsBean tagBean) {
                        mView.showContent();
                        releaseData(tagBean, selectedTags);
//                        mView.showTagList(tagBean);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });

    }

    private void releaseData(CategoryTagsBean tagBean, List<String> selectedTags) {
        ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedTagBeans = new ArrayList<>();
        if (selectedTags == null || selectedTags.size() == 0) {
            mView.showTagList(tagBean);
            return;
        }
        List<CategoryTagsBean.ChildrenBean> children = tagBean.getChildren();
        for (int i = 0; i < children.size(); i++) {
            List<CategoryTagsBean.ChildrenBean.TagsBean> tags = children.get(i).getTags();
            for (int j = 0; j < tags.size(); j++) {
                CategoryTagsBean.ChildrenBean.TagsBean tagsBean = tags.get(j);
                String tagCode = tagsBean.getTagCode();
                for (int k = 0; k < selectedTags.size(); k++) {
                    if (tagCode.equals(selectedTags.get(k))) {
                        tagsBean.setSelected(true);
                        selectedTagBeans.add(tagsBean);
                    }
                }
            }
        }
        mView.showTagList(tagBean);
        mView.showSelectedTags(selectedTagBeans);
    }

    @Override
    public void setPersonalTags(ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> selectedItem) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.tags = new ArrayList<>();
        for (int i = 0; i < selectedItem.size(); i++) {
            String tagCode = selectedItem.get(i).getTagCode();
            personInfo.tags.add(new PersonInfo.TagsBean(tagCode));
        }
        Api.getDefault().changePersonInfo(personInfo)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext, true) {
                    @Override
                    public void _onNext(PersonBean personBean) {
                        mView.showPersonTagSuccess();
                        Log.d("标签", "_onNext: " + personBean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });
    }
}
