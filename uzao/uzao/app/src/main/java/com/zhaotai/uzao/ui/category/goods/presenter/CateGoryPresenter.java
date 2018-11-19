package com.zhaotai.uzao.ui.category.goods.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.CategoryMultiRightBean;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.CateGoryContract;

import java.util.ArrayList;
import java.util.List;

/**
 * description:  分类类别presenter
 * author : zp
 * date: 2017/8/5
 */

public class CateGoryPresenter extends CateGoryContract.Presenter {

    private CateGoryContract.View mView;

    public CateGoryPresenter(Context context, CateGoryContract.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getLeftList(boolean needReflash) {
        getLeftListFromNet();
//        if (needReflash) {
//            getLeftListFromNet();
//        } else {
//            JSONArray result = ACache.get(mContext).getAsJSONArray(GlobalVariable.ROOTCATEGORYLIST);
//
//            if (result ==null){
//                getLeftListFromNet();
//                return;
//            }
//            Type mType = new TypeToken<List<CategoryBean>>() {
//            }.getType();
//
//            List<CategoryBean> rootBeans = GsonUtil.getGson().fromJson(result.toString(), mType);
//            if (rootBeans == null || rootBeans.size() <= 0) {
//                getLeftListFromNet();
//            } else {
//                mView.showLeftList(rootBeans);
//            }
//        }
    }

    @Override
    public void getLeftListFromNet() {
        //重新获取并缓存
        Api.getDefault().getCategory()
                .compose(RxHandleResult.<CategoryBean>handleResult())
                .subscribe(new RxSubscriber<CategoryBean>(mContext, false) {
                    @Override
                    public void _onNext(CategoryBean categoryBeen) {
                        List<CategoryBean> categoryBeanList = new ArrayList<>();
                        categoryBeanList.add(categoryBeen);
                        mView.showLeftList(categoryBeanList);
//                        缓存本列表
//                        String categoryBeenArray = GsonUtil.getGson().toJson(categoryBeen.children);
//                        ACache.get(mContext).put(GlobalVariable.ROOTCATEGORYLIST, categoryBeenArray);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

    @Override
    public void getRightList(String categoryCode, final int position) {
        Api.getDefault().getCategory(categoryCode)
                .compose(RxHandleResult.<List<CategoryBean>>handleResult())
                .subscribe(new RxSubscriber<List<CategoryBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<CategoryBean> categoryBeen) {
                        mView.showRightList(categoryBeen, position);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

    public List<CategoryMultiRightBean> getRightMultiList(List<MainTabBean> mainTabBeans) {
        List<CategoryMultiRightBean> multi = new ArrayList<>();
        if (mainTabBeans.size() > 0) {
            for (MainTabBean tabSecond : mainTabBeans) {
                CategoryMultiRightBean header = new CategoryMultiRightBean(CategoryMultiRightBean.TYPE_SECTION_HEADER);
                header.categoryName = tabSecond.navigateName;
                multi.add(header);
                if (tabSecond.children.size() > 0) {
                    for (MainTabBean tabThird : tabSecond.children) {
                        CategoryMultiRightBean item = new CategoryMultiRightBean(CategoryMultiRightBean.TYPE_ITEM_IMAGE);
                        item.categoryName = tabThird.navigateName;
                        item.categoryCode = tabThird.navigateCode;
                        item.icon = tabThird.icon;
                        item.associateData = tabThird.associateData;
                        item.associateType = tabThird.associateType;
                        multi.add(item);
                    }
                }
            }
        }
        return multi;
    }

}
