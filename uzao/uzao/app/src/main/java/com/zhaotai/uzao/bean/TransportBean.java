package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/6/5
 * Created by LiYou
 * Description : 物流信息
 */

public class TransportBean {

    public String transportNo;//查询的快递单号
    public String icon;//快递图
    public String transportName;//快递名称
    public String telephone;//快递电话
    public Transport transportInfo;

    public class Transport {
        public String status;//0：运单暂无结果，1：查询成功，2：接口出现异常，408：验证码出错
        public int state;//快递单当前的状态 。0：在途中,1：已发货，2：疑难件，3： 已签收 ，4：已退货
        public List<TransportInfo> data;
    }

}
