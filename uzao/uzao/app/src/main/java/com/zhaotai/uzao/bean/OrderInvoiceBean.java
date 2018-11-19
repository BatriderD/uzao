package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * Time: 2017/11/29
 * Created by LiYou
 * Description :
 */

public class OrderInvoiceBean implements Serializable{
    public String invoiceType; //发票类型
    public String invoiceTitle; //发票抬头
    public String invoiceContent; //发票内容
    public String invoiceUserType ; // 发票用户类型(个人、公司) personal company
    public String idNumber ; //身份证
    public String userName ; //姓名
    public String invoiceNumber ; //纳税人识别号
}
