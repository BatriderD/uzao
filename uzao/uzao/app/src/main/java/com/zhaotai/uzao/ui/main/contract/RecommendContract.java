package com.zhaotai.uzao.ui.main.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BasePullDownView;
import com.zhaotai.uzao.bean.RecommendBean;

import java.util.ArrayList;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 新版首页推荐
 */

public interface RecommendContract {

    interface View extends BasePullDownView {

        void showData(ArrayList<RecommendBean> recommendBeens);

        void showDesignerStatus(int pos, boolean b);

        void notifyItemChange(int pos, RecommendBean.AuthorContentBody body);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getRecommendBean();

        public abstract void changeAttention(String userId, int position, boolean p2);

        public abstract void cancelDesigner(String id, int pos);

        public abstract void attentionDesigner(String id, final int pos);

        public abstract void isPayAttention(ArrayList<RecommendBean> recommendBeans, String id, int pos, RecommendBean.AuthorContentBody body);
    }
}
