package com.zhaotai.uzao.bean.EventBean;

/**
 * description:
 * author : zp
 * date: 2017/8/19
 */

public class EventBean<T> {
    private T eventObj;
    private String eventType;

    public EventBean(T eventObj, String eventType) {
        this.eventObj = eventObj;
        this.eventType = eventType;
    }

    public T getEventObj() {
        return eventObj;
    }


    public String getEventType() {
        return eventType;
    }


}
