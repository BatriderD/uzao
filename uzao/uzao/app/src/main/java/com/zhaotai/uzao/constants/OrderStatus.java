package com.zhaotai.uzao.constants;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 订单状态
 */

public class OrderStatus {
    //现金支付
    public static final String CASH = "cash";

    /*** 待支付*/
    public static final String WAIT_PAY = "waitPay";
    /*** 待审核*/
    public static final String WAIT_APPROVE = "waitApprove";
    /*** 审核未通过*/
    public static final String UN_APPROVE = "unapprove";
    /*** 待处理*/
    public static final String WAIT_HANDLE = "waitHandle";
    /*** 待发货*/
    public static final String WAIT_DELIVERY = "waitDelivery";
    /*** 待收货*/
    public static final String WAIT_RECEIVE = "waitReceive";
    /*** 已完成*/
    public static final String FINISHED = "finished";
    /*** 已取消*/
    public static final String CANCELED = "canceled";
    /*** 已关闭*/
    public static final String CLOSED = "closed";
    /*** 后台直接关闭 订单状态*/
    public static final String WAIT_REFUND = "waitRefund";

    //售后的状态
    /*** 待处理*/
    public static final String WAIT_CONFIRM = "waitConfirm";
    /*** 处理中*/
    public static final String CONFIRMING = "confirming";
    /*** 售后失败*/
    public static final String REJECT = "reject";
    /*** 已换货*/
    public static final String REPLACE = "replace";
    /*** 已退货*/
    public static final String RETURNED = "returned";

    /** 待收款*/
    public static final String WAIT_PAY_CONFIRM = "waitPayConfirm";
    /** 待付首款*/
    public static final String WAIT_FIRST_PAY = "waitFirstPay";
    /** 待收首款*/
    public static final String WAIT_FIRST_PAY_CONFIRM = "waitFirstPayConfirm";
    /** 待发大货样*/
    public static final String WAIT_PRODUCE_DELIVERY = "waitProduceDelivery";
    /** 待发打样货*/
    public static final String WAIT_SAMPLING_DELIVER = "waitSamplingDeliver";
    /** 待收大货样*/
    public static final String WAIT_PRODUCE_RECEIVE = "waitProduceReceive";
    /** 待收打样货*/
    public static final String WAIT_SAMPLING_RECEIVE = "waitSamplingReceive";
    /** 待付尾款*/
    public static final String WAIT_LAST_PAY = "waitLastPay";
    /** 待收尾款*/
    public static final String WAIT_LAST_PAY_CONFIRM = "waitLastPayConfirm";

    //打样
    public static final String SAMPLING = "Sampling";
    //大货
    public static final String PRODUCE = "Produce";
    //打样加大货
    public static final String SAMPLING_PRODUCE = "SamplingProduce";


    //智造
    /** 草稿*/
    public static final String DRAFT = "draft";
    /** 基本信息待审核*/
    public static final String WAIT_CONFIRM_DESIGN = "waitConfirmDesign";
    /** 基本信息审核不通过*/
    public static final String DESIGN_UNAPPROVED = "designUnapproved";
    /** 基本信息审核通过*/
    public static final String WAIT_PRODUCE = "waitProduce";
    /** 生产确认待确认*/
    public static final String VBP_WAIT_CONFIRM = "vbpWaitConfirm";
    /** 生产确认  支付*/
    public static final String VBP_NO_TPAY = "vbpNotPay";
    /** 生产确认审核不通过*/
    public static final String VBP_UNAPPROVED = "vbpUnapproved";
    /** 生产确认审核通过*/
    public static final String WAIT_CONFIRM_SUPPLIER = "waitConfirmSupplier";
    /** 生产信息待确认*/
    public static final String WAIT_CONFIRM_PRODUCE = "waitConfirmProduce";
    /** 生产信息审核不通过*/
    public static final String PRODUCE_UNAPPROVED = "produceUnapproved";
    /** 生产信息审核通过*/
    public static final String WAIT_CONFIRM_CONTRACT = "waitConfirmContract";
    /** 合同审核待审核*/
    public static final String CONTRACT_WAIT_APPROVED = "contractWaitApproved";
    /** 合同审核不通过*/
    public static final String CONTRACT_UNAPPROVED = "contractUnapproved";
    /** 合同审核通过*/
    public static final String WAIT_SUBMIT_PRODUCE = "waitSubmitProduce";


    /** 订单类型 素材*/
    public static final String ORDER_TYPE_MATERIAL = "Material";

    /** 订单类型 商品*/
    public static final String ORDER_TYPE_PRODUCT = "Product";
    /** 订单类型 模板商品*/
    public static final String ORDER_TYPE_SAMPLE_DESIGN = "SampleDesign";
}
