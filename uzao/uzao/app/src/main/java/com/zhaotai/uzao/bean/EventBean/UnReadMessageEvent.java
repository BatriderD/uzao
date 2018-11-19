package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2018/3/28
 * Created by LiYou
 * Description : 未读消息
 */

public class UnReadMessageEvent {

    public UnReadMessageEvent(int messageCount) {
        this.messageCount = messageCount;
    }

    public int messageCount;
}
