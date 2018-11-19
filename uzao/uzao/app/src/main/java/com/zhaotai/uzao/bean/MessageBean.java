package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息实体类
 */

public class MessageBean implements Serializable{
    public String messageContent;
    public String messageTitle;
    public String sequenceNBR;
    public String hasReaded;
    public String recDate;
}
