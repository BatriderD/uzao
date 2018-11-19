package com.zhaotai.uzao.bean.EventBean;

/**
 * description: 推荐刷新
 * author : zp
 * date: 2017/8/19
 */

public class EventRecommendBean {
    public int pos;
    public boolean attentionStatus;

    public EventRecommendBean(int pos, boolean attentionStatus) {
        this.pos = pos;
        this.attentionStatus = attentionStatus;
    }
}
