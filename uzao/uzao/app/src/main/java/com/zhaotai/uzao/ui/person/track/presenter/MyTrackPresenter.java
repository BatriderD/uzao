package com.zhaotai.uzao.ui.person.track.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.MyTrackBean;
import com.zhaotai.uzao.bean.MyTrackResultBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.track.contract.MyTrackContract;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 足迹presenter
 * author : ZP
 * date: 2017/12/1 0001.
 */

public class MyTrackPresenter extends MyTrackContract.Presenter {
    private MyTrackContract.View mView;
    private final List<String> ids;

    public MyTrackPresenter(Context context, MyTrackContract.View view) {
        this.mContext = context;
        this.mView = view;
        ids = new ArrayList<>();
    }

    @Override
    public void getMyTrackList(final int start, final boolean isLoadingfinal, final List<MyTrackBean> lists, final boolean isSelected) {
        Api.getDefault().getMyTrackList(start)
                .compose(RxHandleResult.<PageInfo<MyTrackResultBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyTrackResultBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<MyTrackResultBean> stringPageInfo) {
                        //更新我的足迹数量
                        PersonInfo personInfo = new PersonInfo();
                        personInfo.code = EventBusEvent.CHANGE_TRACK_NUM;
                        personInfo.myFootprintCount = String.valueOf(stringPageInfo.totalRows);
                        EventBus.getDefault().post(personInfo);
                        if (isLoadingfinal && start == 0) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        parseData(lists, stringPageInfo.list, stringPageInfo, isSelected);

                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                        System.out.println("足迹查询失败" + message);
                    }
                });

    }

    /**
     * 遍历选中状态
     */
    public boolean checkSelectState(List<MyTrackBean> data) {
        for (MyTrackBean track : data) {
            if (!track.isHeader) {
                if (!track.getFootprintModel().isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 删除 足迹
     */
    public void deleteTrack(List<MyTrackBean> data) {
        ids.clear();
        for (MyTrackBean track : data) {
            if (!track.isHeader) {
                if (track.getFootprintModel().isSelected()) {
                    ids.add(track.getFootprintModel().getSequenceNBR());
                }
            }
        }
        if (ids.size() > 0) {
            Api.getDefault().deleteMyTrack(ids)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(mContext, true) {
                        @Override
                        public void _onNext(String s) {
                            getMyTrackList(0, false, new ArrayList<MyTrackBean>(), false);
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("删除失败");
                        }
                    });
        }
    }

    /**
     * 处理选中数据
     */
    @Override
    public void parseData(List<MyTrackBean> lists, List<MyTrackResultBean> newList, PageInfo<MyTrackResultBean> stringPageInfo, boolean isSelected) {
        ArrayList<MyTrackBean> addList = new ArrayList<>();
        if (lists.size() == 0) {
//            之前是空的  往里增加新数据
            for (int i = 0; i < newList.size(); i++) {
                MyTrackResultBean myTrackResultBean = newList.get(i);
                MyTrackBean myTrackBean1 = new MyTrackBean(true, "第一个日期", -1);
                myTrackBean1.setDate(myTrackResultBean.getDate());
                addList.add(myTrackBean1);
                List<MyTrackBean.FootprintModelsBean> footprintModels = myTrackResultBean.getFootprintModels();
                for (int j = 0; j < footprintModels.size(); j++) {
                    MyTrackBean myTrackBean = new MyTrackBean(false, "一条内容", j);
                    myTrackBean.setDate(myTrackResultBean.getDate());
                    myTrackResultBean.getFootprintModels().get(j).setSelected(isSelected);
                    myTrackBean.setFootprintModel(myTrackResultBean.getFootprintModels().get(j));
                    addList.add(myTrackBean);
                }
            }
        } else {
//            之前有数据
//            拿出最后一天的数据 判断和 新请求到的数据是不是一天的 是一天就略过
            MyTrackBean myTrackBeanOld = lists.get(lists.size() - 1);
            for (int i = 0; i < newList.size(); i++) {
                MyTrackResultBean myTrackResultBean = newList.get(i);
                if (!myTrackBeanOld.getDate().equals(myTrackResultBean.getDate())) {
//                   如果不是同一天 就加一个头
                    MyTrackBean myTrackBean1 = new MyTrackBean(true, "第一个日期", -1);
                    myTrackBean1.setDate(myTrackResultBean.getDate());
                    addList.add(myTrackBean1);
                }
                List<MyTrackBean.FootprintModelsBean> footprintModels = myTrackResultBean.getFootprintModels();
                for (int j = 0; j < footprintModels.size(); j++) {
                    MyTrackBean myTrackBean = new MyTrackBean(false, "一条内容", j);
                    myTrackBean.setDate(myTrackResultBean.getDate());
                    myTrackResultBean.getFootprintModels().get(j).setSelected(isSelected);
                    myTrackBean.setFootprintModel(myTrackResultBean.getFootprintModels().get(j));
                    addList.add(myTrackBean);
                }
            }
        }
        mView.showMyTrackList(addList, stringPageInfo);
    }


}
