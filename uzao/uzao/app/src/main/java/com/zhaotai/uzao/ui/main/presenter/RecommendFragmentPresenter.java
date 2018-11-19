package com.zhaotai.uzao.ui.main.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.RecommendBean;
import com.zhaotai.uzao.bean.RecommendRequestBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.main.contract.RecommendContract;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 请求推荐分类页面
 */

public class RecommendFragmentPresenter extends RecommendContract.Presenter {
    private RecommendContract.View mView;
    private Type mListType = new TypeToken<List<RecommendBean.ValueBean>>() {
    }.getType();
    public RecommendFragmentPresenter(Context context, RecommendContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获取推荐的数据类
     */
    @Override
    public void getRecommendBean() {
        Api.getDefault().getRecommendData("appRecommend")
                .compose(RxHandleResult.<RecommendRequestBean>handleResult())
                .subscribe(new RxSubscriber<RecommendRequestBean>(mContext) {
                    @Override
                    public void _onNext(RecommendRequestBean bean) {
                        resolveData(bean);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopRefresh();
                        mView.showNetworkFail(message);
                    }
                });
    }

    /**
     * 改变对设计师的关注状态
     * @param userId 用户id
     * @param position 在列表中的位置
     * @param isAttention 关注状态 true 关注 false未关注
     */
    @Override
    public void changeAttention(String userId, int position, boolean isAttention) {
        if (!isAttention) {
            cancelDesigner(userId, position);
        } else {
            attentionDesigner(userId, position);
        }
    }


    /**
     * 关注设计师
     *
     * @param id 设计师id
     */
    @Override
    public void attentionDesigner(String id, final int pos) {
        Api.getDefault().attentionDesigner(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        //将指定位置的设计师设计成已关注
                        mView.showDesignerStatus(pos, true);
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }


    /**
     * 取消关注设计师
     *
     * @param id 设计师id
     */
    @Override
    public void cancelDesigner(String id, final int pos) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        Api.getDefault().cancelAttentionDesigner(idList)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.showDesignerStatus(pos, false);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据请求失败");
                    }
                });
    }

    /**
     * 判断设计师是否被我 关注
     *
     * @param recommendBeans  推荐的数据列表
     * @param id   设计师id
     * @param pos  size
     * @param body 关注类
     */
    @Override
    public void isPayAttention(final ArrayList<RecommendBean> recommendBeans, String id, final int pos, final RecommendBean.AuthorContentBody body) {
        if (LoginHelper.getLoginStatus()) {
            //登录才请求
            Api.getDefault().isDesignerPayAttention(id)
                    .compose(RxHandleResult.<Boolean>handleResult())
                    .subscribe(new RxSubscriber<Boolean>(mContext) {
                        @Override
                        public void _onNext(Boolean aBoolean) {
                            body.setIsAttention(!aBoolean ? "Y" : "N");
                            System.out.println("查询状态" + !aBoolean);
                            if (recommendBeans != null && recommendBeans.size() >= pos) {
                                RecommendBean.ValueBean valueBean = recommendBeans.get(pos).valueBean;
                                if (valueBean != null) {
                                    String contentBody = GsonUtil.getGson().toJson(body);
                                    valueBean.setContentBody(contentBody);
                                }
                            }
                            mView.notifyItemChange(pos, body);
                        }

                        @Override
                        public void _onError(String message) {
                            body.setIsAttention(null);
                        }
                    });

        }
    }

    /**
     * 解析封装成可用的列表数据
     * 根据类型和单行 还是i一行两格的现实来处理数据。
     * @param bean 请求到的数据
     */
    private void resolveData(RecommendRequestBean bean) {
        ArrayList<RecommendBean> recommendBeans = new ArrayList<>();
        RecommendBean addBean;
        if (bean != null) {
            //便利数据
            List<RecommendRequestBean.ChildrenBean> children = bean.getChildren();
            for (RecommendRequestBean.ChildrenBean childrenBean : children) {
                String groupCode = childrenBean.getGroupCode();
                if (groupCode == null) {
                    continue;
                }
                String groupName = childrenBean.getGroupName();
                if (groupName == null) {
                    groupName = "";
                }
                String groupType = childrenBean.getGroupType();
                if (groupType == null) {
                    groupType = "";
                }
                //根据数据类型处理数据
                String strValues = GsonUtil.getGson().toJson(childrenBean.getValues());
                switch (groupCode) {
                    case "appRecommendBanner":
                        //推荐轮播
                        addBean = transRecommendBean(RecommendBean.BANNERS, groupName, groupType, strValues);
                        addBean.inSidePos = -2;
                        recommendBeans.add(addBean);
                        break;
                    case "appRecommendAd":
                        //一日一设
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));
                        addBean = transRecommendBean(RecommendBean.DAY_DESIGN, groupName, groupType, strValues);
                        recommendBeans.add(addBean);
                        break;

                    case "appRecommendTheme":
                        //热门主题
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));
                        addBean = transRecommendBean(RecommendBean.HOT_THEME, groupName, groupType, strValues);
                        recommendBeans.add(addBean);
                        recommendBeans.add(new RecommendBean(RecommendBean.END, groupName));
                        break;
                    case "appRecommendDesigner":
                        //设计达人
//                        根据token判断此人的关注状态
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));

                        ArrayList<RecommendBean.ValueBean> valueBeans = GsonUtil.getGson().fromJson(strValues, mListType);
                        for (int i = 0; i < valueBeans.size(); i++) {
                            RecommendBean goodDesignerBean = new RecommendBean(RecommendBean.GOOD_DESIGNER, groupName);
                            RecommendBean.ValueBean valueBean = valueBeans.get(i);
                            if (i == valueBeans.size() - 1) {
                                goodDesignerBean.setContentType(2);
                            } else {
                                goodDesignerBean.setContentType(0);
                            }
                            goodDesignerBean.valueBean = valueBean;

                            goodDesignerBean.inSidePos = -1;

                            recommendBeans.add(goodDesignerBean);
                            String contentBody = valueBean.getContentBody();
                            RecommendBean.AuthorContentBody goodDesignerAuthorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
                            String userId = goodDesignerAuthorContentBody.getUserId();
                            isPayAttention(recommendBeans, userId, recommendBeans.size() - 1, goodDesignerAuthorContentBody);
                        }
                        addBean = new RecommendBean(RecommendBean.END, groupName);
                        addBean.groupType = groupType;
                        recommendBeans.add(addBean);
                        break;
                    case "appRecommendDesign":
                        //原创商品
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));
                        addRecommendBean(recommendBeans, groupType, RecommendBean.ORIGINAL_PRODUCT, groupName, strValues, true);
                        recommendBeans.add(new RecommendBean(RecommendBean.END, groupName));
                        break;

                    case "appRecommendProduct":
                        //最新商品
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));
                        addRecommendBean(recommendBeans, groupType, RecommendBean.NEW_PRODUCT, groupName, strValues, true);
                        recommendBeans.add(new RecommendBean(RecommendBean.END, groupName));
                        break;
                    case "appRecommendMaterial":
                        //最新素材
                        recommendBeans.add(new RecommendBean(RecommendBean.TITLE, groupName));
                        addRecommendBean(recommendBeans, groupType, RecommendBean.NEW_MATERIAL, groupName, strValues, true);
                        recommendBeans.add(new RecommendBean(RecommendBean.END, groupName));
                        break;
                }
            }
        }
        mView.stopRefresh();
        mView.showData(recommendBeans);
    }

    /**
     * 根据group类型 处理json元数据 将其转换成ValueBean 的对象或者列表的ValueBean，并将它赋值给一个定义好的推荐实体类
     *
     * @param type      实体类类型
     * @param title     实体类名称
     * @param groupType json的类型
     * @param json      元数据
     * @return 处理好的推荐实体类
     */
    private RecommendBean transRecommendBean(int type, String title, String groupType, String json) {
        RecommendBean addBean = new RecommendBean(type, title);
        if ("TABLE".equals(groupType)) {
            addBean.valueBeans = GsonUtil.getGson().fromJson(json, mListType);
        } else {
            addBean.valueBean = GsonUtil.getGson().fromJson(json, RecommendBean.ValueBean.class);
        }
        addBean.groupType = groupType;
        return addBean;
    }


    /**
     * 将列表数据转换成可用的数据的
     * @param beans 处理后的数据实体类列表
     * @param groupType 数据类别
     * @param type  数据类别id
     * @param title 标题名称
     * @param json  json元数据
     * @param gridLayout 是否一行两列的显示  true 是 false 单行显示
     */
    private void addRecommendBean(ArrayList<RecommendBean> beans, String groupType, int type, String title, String json, boolean gridLayout) {
        if (beans == null) {
            return;
        }
        ArrayList<RecommendBean.ValueBean> valueBeans = GsonUtil.getGson().fromJson(json, mListType);
        for (int i = 0; i < valueBeans.size(); i++) {
            RecommendBean addBean = new RecommendBean(type, title);
            RecommendBean.ValueBean valueBean = valueBeans.get(i);
            if (i == valueBeans.size() - 1) {
                addBean.setContentType(2);
            } else {
                addBean.setContentType(0);
            }
            addBean.valueBean = valueBean;
            if (gridLayout) {
                addBean.inSidePos = i;
            } else {
                addBean.inSidePos = -1;
            }
            addBean.groupType = groupType;
            beans.add(addBean);
        }
    }

}
