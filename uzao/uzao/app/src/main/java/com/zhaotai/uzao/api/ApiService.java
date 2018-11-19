package com.zhaotai.uzao.api;

import com.zhaotai.uzao.bean.*;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.post.AddContentToThemeBody;
import com.zhaotai.uzao.bean.post.AfterSalesGoodsInfo;
import com.zhaotai.uzao.bean.post.DesignInfo;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceAddressSelectActivity;
import com.zhaotai.uzao.ui.productOrder.presenter.ProducePayPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * time:2017/4/7
 * description:
 * author: LiYou
 */

public interface ApiService {

    /**
     * 验证码登录
     */
    @POST("freeapis-web-security/1/authentication/public/smscode/mobile")
    Observable<BaseResult<TokenInfo>> loginSms(@Body LoginInfo loginInfo);

    /**
     * 获取匿名token
     */
    @POST("freeapis-web-security/1/authentication/anonymous")
    Call<ResponseBody> anonymousLogin(@Body TokenBean body);

    /**
     * 获取匿名token
     */
    @POST("freeapis-web-security/1/authentication/anonymous")
    Observable<BaseResult<TokenInfo>> anonymousRxLogin(@Body TokenBean body);

    /**
     * 获取注册用的验证码
     * AUTH 登录用的
     */
    @GET("freeapis-web-security/1/sms/verifyingcode/AUTH/{iphone}")
    Observable<BaseResult<String>> getSmsCode(@Path("iphone") String iphone);

    /**
     * 获取个人信息
     */
    @GET("freeapis-web-publicuser/1/publicuser/me")
    Observable<BaseResult<PersonBean>> getPersonInfo();

    /**
     * 获取个人信息 -- 带 未读消息 关注 收藏数
     */
    @GET("freeapis-web-publicuser/1/publicuser/mobile/me")
    Observable<BaseResult<PersonBean>> getPersonMobileInfo();

    /**
     * 修改个人信息
     */
    @PUT("freeapis-web-publicuser/1/publicuser/me")
    Observable<BaseResult<PersonBean>> changePersonInfo(@Body PersonInfo info);

    /**
     * 获取未读消息数量
     */
    @GET("freeapis-web-notification/1/message/count")
    Observable<BaseResult<Integer>> getUnReadCount();


    /**
     * 获取标签信息
     */
    @GET("/freeapis-web-systemctl/1/tagCategory/{tagType}")
    Observable<BaseResult<CategoryTagsBean>> getTagList(@Path("tagType") String tagType);


    /**
     * 昵称是否被占用
     */
    @GET("freeapis-web-publicuser/1/publicuser/nickName/available")
    Observable<BaseResult<Boolean>> availableNickName(@Query("nickName") String nickName);

//    /**
//     * 获取首页轮播图
//     */
//    @GET("/freeapis-web-asi/1/asi/SUPER_ADMIN/BANNER/FREEAPIS/2/allvalues")
//    Observable<BaseResult<MainBannerBean>> getMainBannerData();
//

    /**
     * 获取首页tab数据
     * freeapis-web-product/1/navigate/enable/treeview/async/app
     */
    @GET("freeapis-web-product/1/navigate/enable/treeview/async/app")
    Observable<BaseResult<List<MainTabBean>>> getMainTabData(@Query("parentCode") String code);

    /**
     * 获取首页tab数据
     * freeapis-web-product/1/navigate/enable/treeview/async/app
     */
    @GET("freeapis-web-product/1/navigate/enable/treeview/sync/app")
    Observable<BaseResult<MainTabBean>> getMainTabDataAsync(@Query("parentCode") String code);

    /**
     * 获取收货地址
     */
    @GET("freeapis-web-publicuser/1/addresses/public/me")
    Observable<BaseResult<List<AddressBean>>> getAddressData();

    /**
     * 添加地址
     */
    @POST("freeapis-web-publicuser/1/addresses")
    Observable<BaseResult<AddressBean>> addAddress(@Body AddressBean addressBean);

    /**
     * 删除地址
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/addresses", hasBody = true)
    Observable<BaseResult<String>> deleteAddress(@Body List<String> addressList);

    /**
     * 修改地址
     */
    @PUT("freeapis-web-publicuser/1/addresses")
    Observable<BaseResult<AddressBean>> modifyAddress(@Query("id") String sequenceNBR, @Body AddressBean addressBean);

    /**
     * 修改默认地址
     */
    @PUT("freeapis-web-publicuser/1/addresses/{sequenceNBR}/default")
    Observable<BaseResult<String>> changeDefaultAddress(@Path("sequenceNBR") String sequenceNBR);

    /**
     * 获取所有省
     */
    @GET("freeapis-web-systemctl/1/location/provinces")
    Observable<BaseResult<List<RegionBean>>> getProvinces();

    /**
     * 获取所有市
     */
    @GET("freeapis-web-systemctl/1/location/{locationCode}/cities")
    Observable<BaseResult<List<RegionBean>>> getCityList(@Path("locationCode") String locationCode);

    /**
     * 获取分类列表
     *
     * @param code 一级分类code
     */
    @GET("freeapis-web-product/1/categories/enable/treeview/async")
    Observable<BaseResult<List<CategoryBean>>> getCategory(@Query("parentCode") String code);

    /**
     * 获取一级分类
     */
    @GET("freeapis-web-search/1/spuSearch/category")
    Observable<BaseResult<CategoryBean>> getCategory();

    /**
     * 获取spu 商品列表  categoryCode1 一级分类  categoryCode2 二级分类
     */
    @GET("freeapis-web-product/1/spu/portal/page?sidx=priority,sortNum&sort=asc,desc")
    Observable<BaseResult<PageInfo<GoodsBean>>> getGoodsList(@Query("categoryCode1") String categoryCode,
                                                             @Query("categoryCode2") String parentCode,
                                                             @Query("start") int start,
                                                             @Query("length") int length);

    /**
     * 获取 活动列表
     */
    @GET("freeapis-web-search/1/activitySpuSearch/portal/page?length=10")
    Observable<BaseResult<ProductListBean<GoodsBean>>> getActivityGoodsList(@QueryMap Map<String, String> params);

    /**
     * 获取spu 商品列表
     */
    @GET("freeapis-web-product/1/spu/portal/page")
    Observable<BaseResult<PageInfo<GoodsBean>>> getGoodsListFromSpuName(@Query("spuName") String spuName,
                                                                        @Query("start") int start,
                                                                        @Query("length") int length);


    /**
     * 上传文件
     */
    @Multipart
    @POST("freeapis-web-storage/1/storage")
    Observable<BaseResult<UploadFileBean>> uploadTheFile(@Part MultipartBody.Part file);

    /**
     * 暂选
     * 上传多文件
     */
    @Multipart
    @POST("freeapis-web-storage/1/storage/multi")
    Observable<BaseResult<List<UploadFileBean>>> uploadTheFiles(@Part List<MultipartBody.Part> file);


    /**
     * 此上传接口是上传到个人的图片中 需单独保存
     * 上传到我的设计接口
     */
    @Multipart
    @POST("freeapis-web-storage/1/storage/my/design")
    Observable<BaseResult<UploadFileBean>> uploadDesignFile(@Part MultipartBody.Part file);


    /**
     * 多文件上传
     * aftersale 售后
     */
    @Multipart
    @POST("freeapis-web-storage/1/storage/multi/{path}")
    Observable<BaseResult<List<String>>> uploadMultiFile(@Path("path") String path,
                                                         @Part List<MultipartBody.Part> file);

    /**
     * 获取商品详情
     * basicInfo 基本信息
     * designInfo 轮播图 和商品详情
     * sampleSpu 属性
     * designer 设计师信息
     */
    @GET("freeapis-web-product/1/spu/portal/{id}")
    Observable<BaseResult<GoodsDetailMallBean>> getGoodsDetail(@Path("id") String id,
                                                               @Query("spuInfoParts") String all);

    /**
     * 获取商品首评论
     */
    @GET("freeapis-web-business/1/comments/firstComment/spu/{spuId}")
    Observable<BaseResult<CommentBean>> getFirstComment(@Path("spuId") String spuId);

    /**
     * 获取 设计师列表
     */
    @GET("freeapis-web-publicuser/1/publicuser/designer/page")
    Observable<BaseResult<PageInfo<PersonBean>>> getDesignerList(@Query("nickName") String nickName,
                                                                 @Query("start") int start,
                                                                 @Query("length") int length);

    /**
     * 获取 个人关注的设计师列表
     */
    @GET("freeapis-web-publicuser/1/favorites/designer/page?length=10")
    Observable<BaseResult<PageInfo<PersonBean>>> getMyAttentionDesignerList(@Query("start") int start, @Query("designerName") String designerName);

    /**
     * 获取 设计师详情
     */
    @GET("freeapis-web-publicuser/1/publicuser/designer/{designerId}")
    Observable<BaseResult<PersonBean>> getDesignerInfo(@Path("designerId") String designerId);

    /**
     * 判断 设计师是否被关注
     */
    @GET("freeapis-web-publicuser/1/favorites/designer/{designerId}/isMyInterest")
    Observable<BaseResult<Boolean>> isDesignerPayAttention(@Path("designerId") String designerId);

    /**
     * 关注设计师
     */
    @POST("freeapis-web-publicuser/1/favorites/designer/{designerId}")
    Observable<BaseResult<String>> attentionDesigner(@Path("designerId") String designerId);

    /**
     * 取消关注设计师
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/favorites/designer", hasBody = true)
    Observable<BaseResult<String>> cancelAttentionDesigner(@Body List<String> idList);

    /**
     * 获取收藏的商品列表
     */
    @GET("freeapis-web-publicuser/1/favorites/designSpu/page?length=10")
    Observable<BaseResult<PageInfo<GoodsBean>>> getCollectionProductList(@QueryMap Map<String, String> params);


    /**
     * 获取设计师列表页
     * <p>
     * /1/designerSearch/portal/page
     */
    @GET("freeapis-web-search/1/designerSearch/portal/page?length=10")
    Observable<BaseResult<PageInfo<DesignerBean>>> getDesignerList(@QueryMap Map<String, String> params);

    /**
     * 获取收藏的素材列表
     */
    @GET("freeapis-web-publicuser/1/favorites/material/page?length=10")
    Observable<BaseResult<PageInfo<MaterialListBean>>> getCollectionMaterialList(@QueryMap Map<String, String> params);

    /**
     * 获取收藏的主题列表
     */
    @GET("freeapis-web-publicuser/1/favorites/theme/page?length=10")
    Observable<BaseResult<PageInfo<ThemeBean>>> getCollectionThemeList(@QueryMap Map<String, String> params);

    /**
     * 获取收藏的一级分类 type = designSpu/material
     */
    @GET("freeapis-web-publicuser/1/favorites/{type}/categoryCode")
    Observable<BaseResult<List<CategoryCodeBean>>> getCollectionCategoryCode(@Path("type") String type);


    /**
     * 查看主题状态主题
     * freeapis-web-publicuser/1/favorites/theme/977066065936875520/isMyFavorite
     */
    @GET("freeapis-web-publicuser/1/favorites/theme/{themeId}/isMyFavorite ")
    Observable<BaseResult<Boolean>> getThemeCollectStatus(@Path("themeId") String themeId);

    /**
     * 收藏主题
     */
    @POST("freeapis-web-publicuser/1/favorites/theme/{themeId}")
    Observable<BaseResult<String>> collectTheme(@Path("themeId") String themeId);

    /**
     * 删除收藏的主题
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/favorites/theme", hasBody = true)
    Observable<BaseResult<String>> deleteCollectTheme(@Body List<String> idList);

    /**
     * 收藏商品
     */
    @POST("freeapis-web-publicuser/1/favorites/designSpu/{spuId}")
    Observable<BaseResult<String>> collectProduct(@Path("spuId") String id);

    /**
     * 判断商品是否收藏
     */
    @GET("freeapis-web-publicuser/1/favorites/designSpu/{spuId}/isMyFavorite")
    Observable<BaseResult<Boolean>> isMyFavorite(@Path("spuId") String id);

    /**
     * 删除收藏的商品
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/favorites/designSpu", hasBody = true)
    Observable<BaseResult<String>> deleteCollectProduct(@Body List<String> idList);

    /**
     * 根据状态 获取我的商品列表
     * Constant.MY_PRODUCT_PAGE_PARAM 我的商品页分页参数
     */
    @GET("freeapis-web-product/1/designSpu/my/page?length=" + Constant.MY_PRODUCT_PAGE_PARAM)
    Observable<BaseResult<PageInfo<GoodsBean>>> getMyProductList(@Query("start") int start,
                                                                 @Query("status") String status);

    /**
     * 获取 消息列表
     */
    @GET("freeapis-web-notification/1/message/page?length=15")
    Observable<BaseResult<PageInfo<MessageBean>>> getMessageList(@Query("start") int start);

    /**
     * 获取 消息详情列表
     * /message/app/page
     */
    @GET("freeapis-web-notification/1/message/app/page?length=" + Constant.MESSAGE_DETAIL_PAGE_PARAM)
    Observable<BaseResult<PageInfo<MessageDetailBean>>> getMessageDetailList(@QueryMap Map<String, String> params);

    /**
     * 获取 消息详情评论列表
     * /message/app/page
     */
    @GET("freeapis-web-notification/1/message/page?length=" + Constant.MESSAGE_DETAIL_PAGE_PARAM)
    Observable<BaseResult<PageInfo<MessageDetailBean>>> getMessageCommentDetailList(@QueryMap Map<String, String> params);

    /**
     * 获取消息中心列表
     */
    @GET("freeapis-web-notification/1/message/center")
    Observable<BaseResult<List<MessageCenterBean>>> getMessageCenterList();


    /**
     * 删除消息
     */
    @HTTP(method = "DELETE", path = "freeapis-web-notification/1/message", hasBody = true)
    Observable<BaseResult<String>> deleteMessageList(@Body List<String> list);

    /**
     * 将未读 设为已读
     */
    @PUT("freeapis-web-notification/1/message")
    Observable<BaseResult<String>> setMessageRead(@Body List<String> list);

    /**
     * 获取个人未读消息数量
     */
    @GET("freeapis-web-notification/1/message/count")
    Observable<BaseResult<String>> getMessageCount();

    /**
     * 加入购物车
     */
    @POST("freeapis-web-business/1/cart")
    Observable<BaseResult<ShoppingPropertyBean>> addShoppingCart(@Body ShoppingPropertyBean data);

    /**
     * 获取购物车
     */
    @GET("freeapis-web-business/1/cart")
    Observable<BaseResult<ShoppingCartBean>> getShoppingCart();

    /**
     * 删除购物车商品
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/cart", hasBody = true)
    Observable<BaseResult<ShoppingCartBean>> deleteCartGoods(@Body List<String> list);

    /**
     * 清空购物车失效商品
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/cart/invalidCarts")
    Observable<BaseResult<ShoppingCartBean>> deleteInvalidCartGoods();

    /**
     * 商品移动到收藏夹
     */
    @POST("freeapis-web-business/1/cart/favorites")
    Observable<BaseResult<ShoppingCartBean>> moveToCollect(@Body List<String> list);

    /**
     * 更新购物车商品数量
     */
    @POST("freeapis-web-business/1/cart/{cartId}/{cartCount}")
    Observable<BaseResult<ShoppingCartBean>> updateCartCount(@Path("cartId") String cartId,
                                                             @Path("cartCount") String cartCount);

    /**
     * 售出订单列表
     */
    @GET("freeapis-web-business/1/order/designerSold/page?length=15")
    Observable<BaseResult<PageInfo<OrderGoodsBean>>> getSoleOrderList(
            @Query("start") int start);

    /**
     * 售后订单列表
     * <p>
     * <p>
     * {{freeapis-web-business}}/afterSaleApply/my/product/page?start=0&length=20
     */
    @GET("freeapis-web-business/1/afterSaleApply/my/product/page?length=20")
    Observable<BaseResult<PageInfo<AfterSalesBean>>> getAfterSalesList(
            @Query("start") int start);

    /**
     * 创建订单
     */
    @POST("freeapis-web-business/1/order/productOrder")
    Observable<BaseResult<List<OrderGoodsBean>>> createOrder(@Body CreateOrderBean orderInfo);

    /**
     * 创建设计商品订单
     */
    @POST("freeapis-web-business/1/order/sampleDesignOrder")
    Observable<BaseResult<List<OrderGoodsBean>>> createDesignOrder(@Body CreateOrderBean orderInfo);

    /**
     * 创建素材订单
     */
    @POST("freeapis-web-business/1/order/materialOrder")
    Observable<BaseResult<List<OrderGoodsBean>>> createMaterialOrder(@Body List<String> materialIds);

    /**
     * 查看物流信息
     *
     * @param OrderId 订单id
     * @return 物流信息
     */
    @GET("freeapis-web-business/1/transport/orderTransport/{orderId}")
    Observable<BaseResult<TransportBean>> getOrderTransportList(@Path("orderId") String OrderId);

    /**
     * 查看物流信息
     *
     * @param transportId 物流id
     * @return 物流信息
     */
    @GET("freeapis-web-business/1/transport/{transportId}/by/transportNo")
    Observable<BaseResult<TransportBean>> getTransportList(@Path("transportId") String transportId);

    /**
     * 查看物流信息
     *
     * @param orderNo 大货订单id
     * @return 物流信息
     */
    @GET("/freeapis-web-business/1/transport/produceOrder/{orderNo}")
    Observable<BaseResult<TransportBean>> getProduceTransportList(@Path("orderNo") String orderNo);

    /**
     * 获取我的订单 -- 全部订单
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=")
    Observable<BaseResult<PageInfo<OrderBean>>> getAllOrderList(
            @Query("start") int start);

    /**
     * 我的订单 -- 待付款
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=waitPay")
    Observable<BaseResult<PageInfo<OrderBean>>> getWaitPayOrderList(
            @Query("start") int start);

    /**
     * 我的订单 -- 待审核
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=waitApprove")
    Observable<BaseResult<PageInfo<OrderBean>>> getWaitApproveOrderList(
            @Query("start") int start);

    /**
     * 我的订单 -- 待评价
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=waitEvaluate")
    Observable<BaseResult<PageInfo<OrderBean>>> getWaitCommendOrderList(
            @Query("start") int start);

    /**
     * 我的订单 -- 待发货
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=waitDelivery")
    Observable<BaseResult<PageInfo<OrderBean>>> getWaitDeliveryOrderList(
            @Query("start") int start);

    /**
     * 我的订单 -- 待收货
     */
    @GET("freeapis-web-business/1/order/status/myPage?length=10&status=waitReceive")
    Observable<BaseResult<PageInfo<OrderBean>>> getWaitReceiveOrderList(
            @Query("start") int start);

    /**
     * 订单详情
     */
    @GET("freeapis-web-business/1/order/myDetail/{orderId}")
    Observable<BaseResult<OrderDetailBean>> getOrderDetail(@Path("orderId") String orderId);

    /**
     * 申请售后--商品
     */
    @POST("freeapis-web-business/1/afterSaleApply/product")
    Observable<BaseResult<ApplyAfterSalesBean>> applyAfterSales(@Body ApplyAfterSalesBean data);

    /**
     * 获取支付信息 alipay  wx
     */
    @POST("freeapis-web-trade/1/trade/payment/{way}")
    Observable<BaseResult<ChargeBean>> getPayInfo(@Path("way") String way, @Body PayInfo payInfo);

    /**
     * 通知后台支付成功
     */
    @PUT("freeapis-web-trade/1//trade/payment/callback/manual/{tradeNo}")
    Observable<BaseResult<PayCallBackBean>> callbackPay(@Path("tradeNo") String tradeNo, @Query("spuIds") String spuIds);

    /**
     * 订单删除
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/order/{orderId}")
    Observable<BaseResult<String>> deleteOrder(@Path("orderId") String orderId);

    /**
     * 取消订单
     */
    @PUT("freeapis-web-business/1/order/public/cancel")
    Observable<BaseResult<String>> cancelOrder(@Body OrderGoodsBean orderId);

    /**
     * 确认收货  Product":"Product"//载体或者商品Product or SampleDesign
     */
    @PUT("freeapis-web-business/1/order/receipt/Product")
    Observable<BaseResult<OrderBean>> confirmOrder(@Body OrderItemBean packageOrderNo);

    /**
     * 修改订单地址
     */
    @PUT("freeapis-web-business/1/order/receiver")
    Observable<BaseResult<String>> modifyOrderAddress(@Body OrderAddressBean orderAddressBean);

    /**
     * 申请售后详情 -- 从订单进去
     */
    @GET("freeapis-web-business/1/afterSaleApply/mobile/product/{NBR}")
    Observable<BaseResult<ApplyAfterSaleDetailBean>> applyAfterDetail(@Path("NBR") String nbr);

    /**
     * 售后详情 == 从售后列表进去
     */
    @GET("freeapis-web-business/1/afterSaleApply/my/product/{applyNo}")
    Observable<BaseResult<ApplyAfterSaleDetailBean>> myApplyAfterDetail(@Path("applyNo") String applyNo);

    /**
     * 判断此商品 是否自己的
     */
    @GET("freeapis-web-business/1/afterSaleApply/product/{orderDetailId}")
    Observable<BaseResult<ApplyAfterSaleDetailBean>> isMyProduct(@Path("orderDetailId") String orderDetailId);

    /**
     * 申请售后--商品信息
     */
    @GET("freeapis-web-business/1/afterSaleApply/product/{packageOrderNo}")
    Observable<BaseResult<ApplyAfterSaleDetailBean>> getApplyAfterSalesProductInfo(@Path("packageOrderNo") String packageOrderNo);


    /**
     * 获取我的优惠券列表
     * 0 永久 1 固定期限 2非固定期限（有效期）
     * N :未使用 / Y: 已使用 / D:已失效
     */
    @GET("freeapis-web-business/1/coupon/my/status/{status}?length=10")
    Observable<BaseResult<PageInfo<DiscountCouponBean>>> getDiscountList(@Path("status") String status, @Query("start") int start);

    /**
     * 注册登录模块
     * 手机号注册
     */
    @POST("freeapis-web-security/1/authentication/public")
    Observable<BaseResult<PersonBean>> register(@Body RegisterRequestBean bean);

    /**
     * 注册登录模块
     * 找回密码
     */
    @PUT("freeapis-web-publicuser/1/publicuser/forget/password")
    Observable<BaseResult<String>> forgetPassword(@Body ForgetRequestBean bean);

    /**
     * 注册登录模块
     * 密码登录
     * {{freeapis-web-security}}/authentication/public/userNamePassword/mobile
     */
    @POST("freeapis-web-security/1/authentication/public/userNamePassword/mobile")
    Observable<BaseResult<PsdAndAccLoginSuccessBean>> login(@Body RequestLoginBean bean);

    /**
     * 注册登录模块
     * 第三方登录
     */
    @POST("freeapis-web-security/1/authentication/public/thirdParty/{method}")
    Observable<BaseResult<PsdAndAccLoginSuccessBean>> third_part_login(@Path("method") String userId
            , @Body RequestThirdPartLoginBean bean);

    /**
     * 设置模块
     * 修改密码
     * <p>
     * publicuser/password/withOldPassword
     * {{freeapis-web-security}}/authentication/public/userNamePassword/mobile
     */
    @PUT("freeapis-web-publicuser/1/publicuser/password/withOldPassword")
    Observable<BaseResult<String>> changePassword(@Body ChangePasswordRequestBean bean);

    /**
     * 设置模块
     * 设置新密码
     * <p>
     * {{freeapis-web-publicuser}}/publicuser/newPassword
     */
    @PUT("freeapis-web-publicuser/1/publicuser/newPassword")
    Observable<BaseResult<String>> setNewPassword(@Body RequestNewPasswordBean bean);

    /**
     * 三方登录绑定电话号码
     * <p>
     * /freeapis-web-publicuser/1/publicuser/bindMobile
     */
    @PUT("freeapis-web-publicuser/1/publicuser/bindMobile")
    Observable<BaseResult<String>> bindPhoneNumber(@Body RequestBindPhoneBean bean);


    /**
     * 意见反馈
     */
    @POST("freeapis-web-systemctl/1/feedback")
    Observable<BaseResult<RequestFeedBackBean>> postFeedBack(@Body RequestFeedBackBean feedBackBean);

    /**
     * 根据id查询购物的合计价格
     */
    @GET("freeapis-web-business/1/cart/selected")
    Observable<BaseResult<ShoppingCartBean>> getShoppingCartPrice(@Query("cartIds") String cartIds);

    /**
     * 根据id 获取拆单后列表
     */
    @GET("freeapis-web-business/1/order/waitPay/orderList")
    Observable<BaseResult<PayOrderBean>> getPayOrderList(@Query("orderNos") String orderIds);

    /**
     * 根据金额获取可用优惠券
     */
    @GET("freeapis-web-business/1/coupon/my/alvailable/withMoney/{money}")
    Observable<BaseResult<List<DiscountCouponBean>>> getNumDiscount(@Path("money") String money);

    /**
     * 检测是否有新版本
     */
    @GET("freeapis-web-asi/1/asi/support/app/isNeedForceUpdate/{versionCode}/Android")
    Observable<BaseResult<VersionBean>> getNewVersion(@Path("versionCode") String versionCode);

    /**
     * 下载文件--更新app
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downLoadApp(@Url() String url);

    /**
     * 下载
     */
    @Streaming
    @GET()
    Call<ResponseBody> retrofitDownload(@Url() String url);

    /**
     * 提交评价
     */
    @POST("/freeapis-web-business/1/comments")
    Observable<BaseResult<List<CommentBean>>> applyComment(@Body List<CommentBean> commentBeen);

    /**
     * 获取商品评论列表
     */
    @GET("/freeapis-web-business/1/comments/order/{orderNo}")
    Observable<BaseResult<List<CommentBean>>> getOrderCommentList(@Path("orderNo") String orderId);

    /**
     * 分页获取评价列表
     */
    @GET("/freeapis-web-business/1/comments/portal/{entityId}/page?length=15")
    Observable<BaseResult<CommentListBean>> getStateCommentList(@Path("entityId") String entityId, @Query("haveImage") String haveImage, @Query("start") int start, @Query("starScore") String starScore);

    /**
     * 获取生产订单列表 状态 closed（关闭）、finished（完成）、pending（处理中)
     */
    @GET("/freeapis-web-business/1/produceOrder/my/page?length=15")
    Observable<BaseResult<PageInfo<ProductOrderBean>>> getProductOrderList(@Query("start") int start, @Query("status") String status);

    /**
     * 生产订单详情
     */
    @GET("/freeapis-web-business/1/produceOrder/my/detail/{orderNo}")
    Observable<BaseResult<ProductOrderDetailBean>> getProductOrderDetail(@Path("orderNo") String orderNo);

    /**
     * 获取大货支付信息 alipay  wx
     */
    @POST("freeapis-web-trade/1/trade/producePayment/{way}")
    Observable<BaseResult<ChargeBean>> getProducePayInfo(@Path("way") String way, @Body ProducePayPresenter.PayInfo payInfo);

    /**
     * 样品确认收货  sampling（打样收货）、produce（大货样收货）
     */
    @PUT("/freeapis-web-business/1/produceOrder/sampleReceipt/{orderNo}/{filterName}")
    Observable<BaseResult<Object>> receiveSampleProduce(@Path("orderNo") String orderNo, @Path("filterName") String type);

    /**
     * 确认收货 -- 打样订单收货、大货和大货打样批量生产收货
     */
    @PUT("/freeapis-web-business/1/produceOrder/receipt/{orderNo}")
    Observable<BaseResult<Object>> receiveProduce(@Path("orderNo") String orderNo);

    /**
     * 提交分批收货地址
     */
    @POST("/freeapis-web-business/1/produceOrder/batchAddress/{orderNo}")
    Observable<BaseResult<Object>> commitBatchAddress(@Path("orderNo") String orderNo, @Body List<ProduceAddressSelectActivity.BatchAddress> address);

    /**
     * 获取汇款识别码
     */
    @POST("/freeapis-web-trade/1/trade/remittancePayment")
    Observable<BaseResult<CompanyRemitBean>> getRemitCode(@Body CompanyRemitBean info);


    /**
     * 获取我的制造列表
     */
    @GET("/freeapis-web-design/1/design/custom/portal/page?length=15")
    Observable<BaseResult<PageInfo<MakeBean>>> getMakeList(@Query("start") int start);

    /**
     * 获取首页接口数据
     */
    @GET("freeapis-web-asi/1/asi/SUPER_ADMIN/{groupCode}/FREEAPIS/2/allvalues")
    Observable<BaseResult<DynamicFormBean>> getHomeData(@Path("groupCode") String groupCode);

    /**
     * 获取推荐接口数据
     */
    @GET("freeapis-web-asi/1/asi/SUPER_ADMIN/{groupCode}/FREEAPIS/2/allvalues")
    Observable<BaseResult<RecommendRequestBean>> getRecommendData(@Path("groupCode") String groupCode);


    /**
     * 获取商城接口数据
     */
    @GET("freeapis-web-asi/1/asi/SUPER_ADMIN/{groupCode}/FREEAPIS/2/allvalues")
    Observable<BaseResult<HomeMallBean>> getMallData(@Path("groupCode") String groupCode);

    /**
     * 获zao接口数据
     */
    @GET("freeapis-web-asi/1/asi/SUPER_ADMIN/{groupCode}/FREEAPIS/2/allvalues")
    Observable<BaseResult<HomeZaoBean>> getZaoData(@Path("groupCode") String groupCode);

    /**
     * 我的设计列表
     */
    @GET("/freeapis-web-design/1/graphic/my/page?length=10")
    Observable<BaseResult<PageInfo<DesignBean>>> getMyDesignList(@Query("start") int start);

    /**
     * 删除我的设计
     */
    @HTTP(method = "DELETE", path = "/freeapis-web-design/1/graphic", hasBody = true)
    Observable<BaseResult<String>> deleteMyDesign(@Body List<String> ids);

    /**
     * 设计师 设计的作品列表
     */
    @GET("/freeapis-web-design/1/design/logo/{designerId}/page?isPrivate=N&length=15")
    Observable<BaseResult<PageInfo<DesignBean>>> getDesignerDesignList(@Path("designerId") String designerId, @Query("start") int start);

    /**
     * 载体详情
     */
    @GET("/freeapis-web-product/1/sampleSpu/portal/{spuId}")
    Observable<BaseResult<TemplateBean>> getTemplateDetail(@Path("spuId") String spuId);


    /**
     * 载体列表
     * sort :  asc（从低到高）,desc（从高到底）
     * sidx :  priority,sortNum     ( salesPrice——价格， designCount——设计数量)
     */
    @GET("/freeapis-web-product/1/spu/portal/sample/page?length=15")
    Observable<BaseResult<PageInfo<TemplateBean>>> getTemplateList(@Query("start") int start,
                                                                   @Query("spuName") String spuName,
                                                                   @Query("categoryCode2") String categoryCode2,
                                                                   @Query("sidx") String sidx,
                                                                   @Query("sort") String sort);

    /**
     * 根据mkuId获取待发布的商品信息
     */
    @GET("/freeapis-web-design/1/design/sample/waitPublish/{mkuId}")
    Observable<BaseResult<TemplateInfoBean>> getWaitPublishTemplateInfo(@Path("mkuId") String mkuId);

    /**
     * 获取3d 待发布商品信息
     */
    @GET("/freeapis-web-design/1/design/sample/waitPublish/3d/{templateId}")
    Observable<BaseResult<TemplateInfoBean>> getWaitPublish3DTemplateInfo(@Path("templateId") String templateId);

    /**
     * 上架设计完成载体到商城 --申请上架
     */
    @POST("/freeapis-web-product/1/designSpu/publish")
    Observable<BaseResult<Object>> putawayTemplateToStore(@Body DesignInfo data);

    /**
     * 载体设计完 --保存商品
     */
    @POST("/freeapis-web-product/1/designSpu/unReviewed")
    Observable<BaseResult<Object>> saveTemplate(@Body TemplateBean data);

    /**
     * 我的商品 --提交审核
     */
    @PUT("/freeapis-web-product/1/designSpu/submit2waitApprove/{spuId}")
    Observable<BaseResult<TemplateBean>> submitMyProductToCheck(@Path("spuId") String spuId);

    /**
     * 购买载体商品
     */
    @POST("/freeapis-web-product/1/designSpu/buy")
    Observable<BaseResult<TemplateBean>> buyTemplate(@Body DesignInfo data);

    /**
     * 我的商品-- 商品详情
     */
    @GET("/freeapis-web-product/1/designSpu/my/{spuId}")
    Observable<BaseResult<TemplateBean>> getMyProductDetail(@Path("spuId") String spuId);

    /**
     * 增加我的足迹
     * <p>
     * {{freeapis-web-publicuser}}/footprint
     */
    @POST("/freeapis-web-publicuser/1/footprint")
    Observable<BaseResult<MyTrackResultBean>> addMyTrack(@Body MyTrackRequestBean bean);

    /**
     * 查询我的足迹
     * <p>
     * {{freeapis-web-publicuser}}/footprint
     */
    @GET("/freeapis-web-publicuser/1/footprint/page?recDate=&length=10")
    Observable<BaseResult<PageInfo<MyTrackResultBean>>> getMyTrackList(@Query("start") int start);

    /**
     * 删除浏览记录
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/footprint", hasBody = true)
    Observable<BaseResult<String>> deleteMyTrack(@Body List<String> ids);

    /**
     * 我的商品 --编辑完商品 保存商品
     */
    @PUT("/freeapis-web-product/1/designSpu/my/unReviewed/{spuId}")
    Observable<BaseResult<TemplateBean>> saveMyProductTemlate(@Path("spuId") String spuId, @Body TemplateBean data);

    /**
     * 我的商品 -- 编辑完商品 提交审核
     */
    @PUT("/freeapis-web-product/1/designSpu/{spuId}")
    Observable<BaseResult<TemplateBean>> submitMyModifyProduct2Check(@Path("spuId") String spuId, @Body TemplateBean data);

    /**
     * 下架商品
     */
    @PUT("/freeapis-web-product/1/designSpu/my/unPublish/{spuId}")
    Observable<BaseResult<GoodsBean>> soldOutProduct(@Path("spuId") String spuId);

    /**
     * 立即上架
     */
    @PUT("/freeapis-web-product/1/designSpu/my/publish/{spuId}")
    Observable<BaseResult<Object>> publishMyProductImmediately(@Path("spuId") String spuId);

    /**
     * 删除我的商品
     */
    @HTTP(method = "DELETE", path = "freeapis-web-product/1/designSpu", hasBody = true)
    Observable<BaseResult<Object>> deleteMyProduct(@Body List<String> spuIds);


    /**
     * 保存设计
     */
    @POST("/freeapis-web-design/1/design/logo")
    Observable<BaseResult<ArtWrokResultBean>> saveArtWork(@Body RequestSaveArtWorkBean bean);

    /**
     * 获得MKU信息
     * <p>
     * /freeapis-web-design/1/design/sample/maskGroup/{mkuId}
     */
//    @GET("/freeapis-web-design/1/sampleSpu/maskGroup/{mkuId}")
    @GET("/freeapis-web-design/1/design/sample/maskGroup/{mkuId}")
    Observable<BaseResult<MKUCarrierBean>> getMKUInfo(@Path("mkuId") String mkuId);

    /**
     * 获得贴纸tag信息
     */
    @GET("/freeapis-web-systemctl/1/tagCategory/MATERIALTAG?tagName")
    Observable<BaseResult<StickerTagBean>> getStickerTagName();

    /**
     * 根据贴纸tag获得贴纸列表
     */
    @GET("/freeapis-web-product/1/sourceMaterial/page?length=15")
    Observable<BaseResult<PageInfo<MaterialStickerBean>>> getStickersFromTag(@Query("start") int start,
                                                                             @Query("tagCode") String tagCod);

    /**
     * 获取3d 模型资源信息
     */
    @GET("/freeapis-web-design/1/design/sample/3d/objects/{spuId}")
    Observable<BaseResult<ThreeDimensionalBean>> get3dInfo(@Path("spuId") String templateId);

    /**
     * 获取协议
     * REGPORTOCOL 优造中国用户注册服务协议
     * COUPONPROTOCAL 优惠券说明
     * COPYRIGHTPORTOCOL 版权协议
     * VBPPORTOCOL 工程文件保密协议
     */
    @GET("/freeapis-web-asi/1/asi/SUPER_ADMIN/PROTOCOL/FREEAPIS/2/conditionPage?fieldNames=protocolCode&operators=eq")
    Observable<BaseResult<PageInfo<ProtocolBean>>> getRegisterProtocol(@Query("fieldValues") String protocol);

    /**
     * 根据字典编码获取当前商家字典的所有值
     * recommendType : 成品 mallSpuAndSampleSpuRecommend  设计师商品 : designSpuRecommend
     */
    @GET("/freeapis-web-systemctl/1/dictionary/{recommendType}/entries")
    Observable<BaseResult<List<DictionaryBean>>> getAllDictionary(@Path("recommendType") String recommendType);

    /**
     * 根据失效商品ID查询相似商品
     */
    @GET("/freeapis-web-search/1/recommend/invalidSpu/{spuId}")
    Observable<BaseResult<SimilarBean>> getSimilarGoods(@Path("spuId") String spuId);


    /**
     * 历史图片列表
     */
    @GET("/freeapis-web-storage/1/storage/me/page?length=12")
    Observable<BaseResult<PageInfo<MyPicBean>>> getMyPictureList(@Query("start") int start);

    /**
     * 商品列表查询
     * needOption 是否需要筛选 Y需要 N 不需要
     * ppValues 筛选属性
     * queryWord 搜索名称
     * sort 排序 default_ 综合排序 price-asc/price-desc 价格  salesCount-asc/salesCount-desc 销量
     */
    @GET("/freeapis-web-search/1/spuSearch/portal/page?length=10")
    Observable<BaseResult<ProductListBean<GoodsBean>>> getProductList(@QueryMap Map<String, String> map);

    /**
     * 素材列表查询
     * needOption 是否需要筛选
     * categoryCode2 素材的二级分类
     */
    @GET("/freeapis-web-search/1/sourceMaterialSearch/portal/page?length=10")
    Observable<BaseResult<ProductListBean<MaterialListBean>>> getMaterialList(@QueryMap Map<String, String> map);

    /**
     * 素材列表加入主题列表查询
     */
    @GET("/freeapis-web-search/1/sourceMaterialSearch/choose/page?length=10")
    Observable<BaseResult<PageInfo<MaterialListBean>>> getThemeMaterialList(@QueryMap Map<String, String> map);

    /**
     * 商品列表加入主题列表查询
     * /freeapis-web-search/1/spuSearch/choose/page
     */
    @GET("/freeapis-web-search/1/spuSearch/choose/page?length=10")
    Observable<BaseResult<PageInfo<GoodsBean>>> getThemeProductList(@QueryMap Map<String, String> map);

    /**
     * 查询我的主题
     */
    @GET("/freeapis-web-business/1/theme/my/page")
    Observable<BaseResult<PageInfo<ThemeBean>>> getMyThemeList(@QueryMap Map<String, String> map);

    /**
     * 查询我管理的场景
     */
    @GET("/freeapis-web-business/1/theme/my/operator/page")
    Observable<BaseResult<PageInfo<ThemeBean>>> getMyManagerSceneList(@QueryMap Map<String, String> map);

    /**
     * 查询我管理的场景
     */
    @GET("/freeapis-web-business/1/themePosts/page")
    Observable<BaseResult<PageInfo<SceneManagerPostBean>>> getMyManagerScenePostList(@QueryMap Map<String, String> map);


    /**
     * 帖子详情
     */
    @POST("freeapis-web-business/1/themePosts/{id}")
    Observable<BaseResult<String>> getPostDetail(@Path("id") String id);

    /**
     * 删除帖子
     */
    @DELETE("/freeapis-web-business/1/themePosts/{id}")
    Observable<BaseResult<String>> delScenePost(@Path("id") String post);

    /**
     * 精华帖子
     */
    @PUT("/freeapis-web-business/1/themePosts/{id}/essenceStatus")
    Observable<BaseResult<SceneManagerPostBean>> essenceScenePost(@Path("id") String post);

    /**
     * 置顶帖子
     */
    @PUT("/freeapis-web-business/1/themePosts/{id}/top")
    Observable<BaseResult<SceneManagerPostBean>> topScenePost(@Path("id") String post);

    /**
     * 查询我管理的场景相册列表
     */
    @GET("/freeapis-web-business/1/themeAlbum/page")
    Observable<BaseResult<PageInfo<SceneManagerAlbumBean>>> getMyManagerScenePhotoList(@QueryMap Map<String, String> map);


    /**
     * 新增相册
     */
    @POST("/freeapis-web-business/1/themeAlbum/{themeId}")
    Observable<BaseResult<SceneManagerAlbumBean>> addMyManagerSceneAlbum(@Path("themeId") String themeId, @Body SceneManagerAlbumBean data);

    /**
     * 编辑相册
     */
    @PUT("/freeapis-web-business/1/themeAlbum/{albumId}")
    Observable<BaseResult<SceneManagerAlbumBean>> EditorMyManagerSceneAlbum(@Path("albumId") String albumId, @Body SceneManagerAlbumBean data);


    /**
     * 分页获取相册图片
     */
    @GET("/freeapis-web-business/1/themeAlbum/photo/page")
    Observable<BaseResult<PageInfo<ScenePhotoManagerBean>>> getMyManagerSceneAlbumList(@QueryMap Map<String, String> map);

    /**
     * 新增相册图片
     */
    @POST("/freeapis-web-business/1/themeAlbum/photo/{albumId}")
    Observable<BaseResult<List<ScenePhotoManagerBean>>> upAlbumPhoto(@Path("albumId") String albumId, @Body ArrayList<UploadFileBean> beans);


    /**
     * 删除相册图片
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/themeAlbum/photo/{albumId}", hasBody = true)
    Observable<BaseResult<String>> deleteAlbumPhoto(@Path("albumId") String albumId, @Body String[] ids);

    /**
     * 删除相册
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/themeAlbum/{albumId}", hasBody = true)
    Observable<BaseResult<String>> deleteAlbum(@Path("albumId") String albumId);


    /**
     * 查询主题列表
     */
    @GET("/freeapis-web-search/1/themeSearch/portal/page")
    Observable<BaseResult<PageInfo<ThemeListBean>>> getThemeList(@QueryMap Map<String, String> map);

    /**
     * 批量我的删除主题
     */
    @HTTP(method = "DELETE", path = "/freeapis-web-business/1/theme", hasBody = true)
    Observable<BaseResult<List<ThemeBean>>> delMyThemeList(@Body List<String> map);

    /**
     * 查询主题主题模板
     */
    @GET("/freeapis-web-business/1/theme/template/available/page")
    Observable<BaseResult<PageInfo<ThemeTemplateBean>>> getThemeTemplateList(@QueryMap Map<String, String> map);

    /**
     * 查看主题
     */
    @GET("/freeapis-web-business/1/theme/{themeId}")
    Observable<BaseResult<ThemeBean>> checkTheme(@Path("themeId") String themeId);

    /**
     * 修改主题
     */
    @PUT("/freeapis-web-business/1/theme/{themeId}")
    Observable<BaseResult<ThemeBean>> changeTheme(@Path("themeId") String themeId, @Body ThemeBean themeBean);


    /**
     * 查询主题模块
     */
    @GET("/freeapis-web-business/1/theme/themeContent/part/{themeId}")
    Observable<BaseResult<List<ThemeModuleBean>>> getThemeContent(@Path("themeId") String themeId);

    /**
     * 创建主题模块xx
     */
    @POST("/freeapis-web-business/1/theme/themeContent/part")
    Observable<BaseResult<ThemeModuleBean>> saveThemeContent(@Body ThemeModuleBean themeModuleBean);

    /**
     * 创建主题模块
     * /freeapis-web-business/1/theme/themeContent/all/{themeId}
     */
    @POST("/freeapis-web-business/1/theme/themeContent/all/{themeId}")
    Observable<BaseResult<ThemeBean>> saveThemeAll(@Path("themeId") String themeId, @Body ThemeAllRequestBean requestBean);

    /**
     * 预览主题模板
     */
    @POST("/freeapis-web-business/1/theme/preview/{themeId}/{templateId}")
    Observable<BaseResult<WebBean>> previewTheme(@Path("themeId") String themeId, @Path("templateId") String templateId, @Body String json);

    /**
     * 修改主题模块
     */
    @PUT("/freeapis-web-business/1/theme/themeContent/part")
    Observable<BaseResult<ThemeModuleBean>> changeThemeContent(@Body ThemeModuleBean themeModuleBean);

    /**
     * 删除主题模块
     */
    @DELETE("/freeapis-web-business/1/theme/themeContent/part/{themeId}/{partNo}")
    Observable<BaseResult<String>> delThemeContent(@Path("themeId") String themeId, @Path("partNo") String partNo);

    /**
     * 创建主题
     */
    @POST("/freeapis-web-business/1/theme")
    Observable<BaseResult<ThemeBean>> saveTheme(@Body ThemeBean themeBean);


    /**
     * 存储元设计
     * <p>
     * /freeapis-web-design/1/graphic
     */
    @POST("/freeapis-web-design/1/graphic")
    Observable<BaseResult<ResultMetaDataBean>> saveMyDesign(@Body RequestSaveMyDesignDataBean bean);

    /**
     * 查询原设计
     * /freeapis-web-design/1/graphic/{id}
     */
    @GET("/freeapis-web-design/1/graphic/{id}")
    Observable<BaseResult<ResultMetaDataBean>> searchMyDesignSpu(@Path("id") String spuId);

    /**
     * 模板载体设计详情
     */
    @GET("/freeapis-web-product/1/designSpu/metadata/{spuId}")
    Observable<BaseResult<String>> getTemplateDesign(@Path("spuId") String spuId);

    /**
     * 修改原设计
     * /freeapis-web-design/1/graphic/{id}
     */
    @PUT("/freeapis-web-design/1/graphic/{id}")
    Observable<BaseResult<ResultMetaDataBean>> changeMyDesignSpu(@Path("id") String id, @Body RequestChangeMetaDataBean bean);

    /**
     * 查询艺术字
     */
    @GET("/freeapis-web-design/1/wordart/page?length=15")
    Observable<BaseResult<PageInfo<ArtFontBean>>> getArtFont(@Query("start") int start);

    /**
     * 默认字体
     */
    @GET("/freeapis-web-design/1/wordart/defaultFont")
    Observable<BaseResult<List<DefaultFontBean>>> getDefaultFont();


    /**
     * 查询工艺
     */
    @GET("/freeapis-web-design/1/craft/page?length=15")
    Observable<BaseResult<PageInfo<TechnologyBean>>> getTechnologyInfo(@Query("start") int start);

    /**
     * 获取分享返利数据
     */
    @GET("/freeapis-web-business/1/promoteBenefitRecord/share")
    Observable<BaseResult<RebateBean>> getRebate();

    /**
     * 获取邀请明细
     */
    @GET("/freeapis-web-business/1/promoteBenefitRecord/award/page?length=15")
    Observable<BaseResult<PageInfo<RebateBean>>> getInviteDetail(@Query("start") int start);

    /**
     * 获取mku个数 true 为1个 false为多个
     */
    @GET("/freeapis-web-design/1/design/sample/mkuCount/{spuId}")
    Observable<BaseResult<Boolean>> checkMkuCountIsSingle(@Path("spuId") String spuId);

    /**
     * 查询mku
     */
    @GET("/freeapis-web-design/1/design/sample/mku/{skuId}")
    Observable<BaseResult<String>> getMkuId(@Path("skuId") String skuId);

    /**
     * 持久化匿名上传的图片
     * /freeapis-web-storage/1/storage/cookie/persistence
     */
    @PUT("/freeapis-web-storage/1/storage/cookie/persistence")
    Observable<BaseResult<String>> upCookiePics(@Body String[] pics);

    /**
     * 获得滤镜图片
     */
    @POST("/freeapis-web-storage/1/artwork/filter/{filterType}")
    Observable<BaseResult<String>> getFilterPic(@Path("filterType") String filterType, @Query("imageId") String imageId);

    /**
     * 获得滤镜示例图片
     */
    @GET("/freeapis-web-systemctl/1/dictionary/FilterExample/entries")
    Observable<BaseResult<List<ArtWorkBean>>> getFilterListPic();

    /**
     * 上传base64图片
     */
    @POST("/freeapis-web-storage/1/storage/base64img")
    Observable<BaseResult<List<UploadFileBean>>> uploadBase64Image(@Body List<String> body);

    /**
     * 素材分类
     */
    @GET("/freeapis-web-search/1/sourceMaterialSearch/category")
    Observable<BaseResult<CategoryBean>> getMaterialCategory();

    /**
     * 素材详情
     */
    @GET("/freeapis-web-product/1/sourceMaterial/portal/{sourceMaterialId}")
    Observable<BaseResult<MaterialDetailBean>> getMaterialDetail(@Path("sourceMaterialId") String id);

    /**
     * 是否收藏
     */
    @GET("/freeapis-web-publicuser/1/favorites/material/{materialId}/isMyFavorite")
    Observable<BaseResult<Boolean>> getIsCollectMaterial(@Path("materialId") String id);

    /**
     * 收藏素材
     */
    @POST("/freeapis-web-publicuser/1/favorites/material/{materialId}")
    Observable<BaseResult<String>> collectMaterial(@Path("materialId") String id);

    /**
     * 取消收藏素材
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/favorites/material", hasBody = true)
    Observable<BaseResult<String>> cancelCollectMaterial(@Body List<String> ids);

    /**
     * 点赞素材主题等/或素材评论 type sourceMaterial,comment
     */
    @POST("/freeapis-web-business/1/upvote/{type}/{entityId}")
    Observable<BaseResult<String>> likeMaterial(@Path("type") String type, @Path("entityId") String entityId);

    /**
     * 取消点赞
     */
    @HTTP(method = "DELETE", path = "freeapis-web-business/1/upvote/{type}/{entityId}")
    Observable<BaseResult<String>> cancelLike(@Path("type") String type, @Path("entityId") String entityId);

    /**
     * 判断是否点赞
     */
    @GET("/freeapis-web-business/1/upvote/{type}/{entityId}/isUpvote")
    Observable<BaseResult<Boolean>> isLike(@Path("type") String type, @Path("entityId") String id);

//    /**
//     * 创建素材订单
//     */
//    @POST("/freeapis-web-trade/1/trade/sourceMaterial/payment/{way}")
//    Observable<BaseResult<ChargeBean>> getMaterialPayInfo(@Path("way") String way, @Body RewardInfo info);

    /**
     * 根据关键字 查询流行词列表
     */
    @GET("freeapis-web-design/1/wordartlib/page?length=15")
    Observable<BaseResult<PageInfo<CatchWordBean>>> getSearchCatchWordPageList(@QueryMap Map<String, String> params);

    /**
     * 查询流行词一级目录
     * <p>
     */
    @GET("/freeapis-web-design/1/wordlibCategory/level/1")
    Observable<BaseResult<List<CatchWordTabBean>>> getCatchWordTabList();

    /**
     * 查询该类下的流行词
     * <p>
     */
    @GET("/freeapis-web-design/1/wordlib/page")
    Observable<BaseResult<PageInfo<CatchWordBean>>> getCatchWordContentList(@QueryMap Map<String, String> params);

    /**
     * 查询艺术字主题列表
     */
    @GET("/freeapis-web-design/1/wordartTopic/topic/page?length=10")
    Observable<BaseResult<PageInfo<ArtFontTabBean>>> getArtFontTabList(@QueryMap Map<String, String> params);

    /**
     * 查询主题下艺术字列表
     */
    @GET("/freeapis-web-design/1/wordartTopic/wordart/page?length=10")
    Observable<BaseResult<PageInfo<ArtFontTopicBean>>> getArtFontTopicList(@QueryMap Map<String, String> params);

    /**
     * 我购买的素材
     * /freeapis-web-publicuser/1/material/status/myPage?start=0&lenght=10&status=
     */
    @GET("/freeapis-web-publicuser/1/material/status/myPage")
    Observable<BaseResult<PageInfo<MyMaterialBean>>> getMyBoughtMaterial(@QueryMap Map<String, String> params);

    /**
     * 删除我购买的素材
     */
    @DELETE("/freeapis-web-publicuser/1/material/{id}")
    Observable<BaseResult<String>> DelMyBoughtMaterial(@Path("id") String id);

    /**
     * 我购买的素材的类别
     */
    @GET("/freeapis-web-publicuser/1/material/categoryCode")
    Observable<BaseResult<List<MyBoughtMaterialCategoryBean>>> getMyBoughtMaterialCategory(@QueryMap Map<String, String> params);


    /**
     * 我上传的素材列表
     */
    @GET("/freeapis-web-product/1/sourceMaterial/portal/page")
    Observable<BaseResult<PageInfo<MyUploadMaterialBean>>> getUploadMaterial(@QueryMap Map<String, String> params);

    /**
     * 搜索素材列表
     * /freeapis-web-publicuser/1/material/status/myPage?
     */
    @GET("/freeapis-web-publicuser/1/material/status/myPage")
    Observable<BaseResult<PageInfo<MyMaterialBean>>> searchBoughtMaterial(@QueryMap Map<String, String> params);

    //评论相关

    /**
     * 创建评论
     */
    @POST("/freeapis-web-business/1/comments/{type}/{entityId}")
    Observable<BaseResult<MaterialDiscussBean>> commitDiscuss(@Path("type") String type, @Path("entityId") String entityId, @Body MaterialDiscussCommitRequestBean requestBean);


    /**
     * 获取评论列表
     * /freeapis-web-business/1/comment/{type}/{entityId}
     */
    @GET("/freeapis-web-business/1/comments/{type}/{entityId}/page")
    Observable<BaseResult<BasePageWithCount<DiscussBean>>> getDiscussList(@Path("type") String type, @Path("entityId") String entityId, @QueryMap Map<String, String> params);

    /**
     * 评论中心
     */
    @GET("/freeapis-web-business/1/comments/{firstCommentId}/allComment")
    Observable<BaseResult<DiscussCenterBean>> DiscussCenterList(@Path("firstCommentId") String firstCommentId, @QueryMap Map<String, String> params);

    /**
     * 删除评论
     * /freeapis-web-business/1/comments/{type}/{id}
     */
    @DELETE("/freeapis-web-business/1/comments/{type}/{id}")
    Observable<BaseResult<String>> deleteDiscuss(@Path("type") String type, @Path("id") String id);


    /**
     * 查看对话
     */
    @GET("/freeapis-web-business/1/comments/conversation/{commentId} ")
    Observable<BaseResult<PageInfo<DiscussBean>>> getDialogue(@Path("commentId") String commentId);

    //评论相关


    /**
     * 获取素材评论列表
     * http:192.168.2.115/freeapis_web_business/comments/sourceMaterial/素材ID/ROOT?lenght=0&start=10
     */
    @GET("/freeapis-web-business/1/comments/sourceMaterial/{materialId}/page")
    Observable<BaseResult<String>> getMaterialDiscuss(@Path("materialId") String materialId, @QueryMap Map<String, String> params);

    /**
     * 素材评论点赞
     * commentId
     */
    @POST("/freeapis-web-business/1/upvote/comment/{materialId}")
    Observable<BaseResult<String>> materialUpvote(@Path("materialId") String materialId);

    /**
     * 素材评论取消点赞
     */
    @DELETE("/freeapis-web-business/1/upvote/comment/{materialId}")
    Observable<BaseResult<String>> materialDisUpvote(@Path("materialId") String materialId);

    /**
     * 评论点赞
     * commentId
     */
    @POST("/freeapis-web-business/1/upvote/comment/{commentId}")
    Observable<BaseResult<String>> commentUpvote(@Path("commentId") String commentId);

    /**
     * 评论取消点赞
     */
    @DELETE("/freeapis-web-business/1/upvote/comment/{commentId}")
    Observable<BaseResult<String>> commentDisUpvote(@Path("commentId") String commentId);

    /**
     * 对素材进行评论
     */
    @POST("/freeapis-web-business/1/comments/sourceMaterial/{materialId}")
    Observable<BaseResult<MaterialDiscussBean>> commitDiscussMaterial(@Path("materialId") String materialId, @Body MaterialDiscussCommitRequestBean requestBean);

    /**
     * 对素材评论进行评论
     */
    @POST("/freeapis-web-business/1/comments/sourceMaterial/{materialId}")
    Observable<BaseResult<MaterialDiscussBean>> commitDiscussMaterialDiscuss(@Path("materialId") String materialId, @Body MaterialDiscussCommitRequestBean requestBean);

    /**
     * 素材评论中心
     */
    @GET("/freeapis-web-business/1/comments/{firstCommentId}/allComment")
    Observable<BaseResult<MaterialDiscussCenterBean>> MaterialDiscussCenterList(@Path("firstCommentId") String firstCommentId, @QueryMap Map<String, String> params);


    /**
     * 获取免费素材
     */
    @POST("/freeapis-web-publicuser/1/material/{materialId}")
    Observable<BaseResult<String>> getFreeMaterial(@Path("materialId") String id);

    /**
     * 打赏
     */
    @POST("/freeapis-web-trade/1/trade/award/payment/{way}")
    Observable<BaseResult<ChargeBean>> getAwardPayInfo(@Path("way") String way, @Body RewardInfo info);

    /**
     * 打赏设计师
     */
    @POST("/freeapis-web-trade/1/trade/designer/award/payment/{way}")
    Observable<BaseResult<ChargeBean>> getDesignerAwardPayInfo(@Path("way") String way, @Body RewardInfo info);

    /**
     * 关键字联想 type = spu  ,type=material
     */
    @GET("freeapis-web-search/1/suggest")
    Observable<BaseResult<List<AssociateBean>>> getAssociate(@Query("queryWord") String queryWord, @Query("type") String type);

    /**
     * 我的素材3级类别
     */
    @GET("freeapis-web-product/1/sourceMaterialCategory/level/1")
    Observable<BaseResult<List<MaterialTabBean>>> getMaterialTabList();


    /**
     * 根据评论id获取评论内容
     * freeapis-web-business/1/comments/{id}
     */
    @GET("freeapis-web-business/1/comments/{id}")
    Observable<BaseResult<MessageCommentBean>> getCommentData(@Path("id") String id);

    /**
     * 设置消息已读
     */
    @HTTP(method = "PUT", path = "freeapis-web-notification/1/message", hasBody = true)
    Observable<BaseResult<String>> putMessageRead(@Body String[] id);

    /**
     * 过滤素材的列表字符串中未购买的素材
     *
     * @param id 素材列表的字符串
     * @return 未购买的素材列表信息
     */
    @GET("freeapis-web-business/1/order/materialOrderApplication")
    Observable<BaseResult<UnPayMaterialBean>> getUnBoughtMaterialList(@Query("materialIds") String id);

    /**
     * 分页获取品牌信息
     */
    @GET("freeapis-web-product/1/brand/page?length=15")
    Observable<BaseResult<PageInfo<BrandBean>>> getBrandList(@Query("start") String start);

    /**
     * 获取品牌详情
     *
     * @param brandId 品牌Id
     */
    @GET("freeapis-web-product/1/brand/{brandId}")
    Observable<BaseResult<BrandBean>> getBrandIntro(@Path("brandId") String brandId);

    /**
     * 根据素材推荐载体
     */
    @GET("freeapis-web-search/1/recommend/spu/tag/{materialId}")
    Observable<BaseResult<List<GoodsBean>>> getRecommendDesignProduct(@Path("materialId") String materialId);

    /**
     * 关注品牌
     *
     * @param brandIds 品牌Id集合
     */
    @POST("freeapis-web-publicuser/1/favorites/brand")
    Observable<BaseResult<String>> attentionBrand(@Body List<String> brandIds);

    /**
     * 取消关注品牌
     *
     * @param brandIds 品牌Id集合
     */
    @HTTP(method = "DELETE", path = "freeapis-web-publicuser/1/favorites/brand", hasBody = true)
    Observable<BaseResult<String>> cancelAttentionBrand(@Body List<String> brandIds);

    /**
     * 分页查询关注的品牌
     */
    @GET("freeapis-web-publicuser/1/favorites/brand/page?length=10")
    Observable<BaseResult<PageInfo<BrandBean>>> getAttentionBrandList(@Query("start") int start);

    /**
     * 分页查询关注的品牌
     */
    @GET("freeapis-web-publicuser/1/favorites/brand/page?length=10")
    Observable<BaseResult<PageInfo<BrandBean>>> getAttentionBrandList(@Query("start") int start, @Query("brandName") String brandName);

    /**
     * 品牌主页商品
     *
     * @param start     开始位置
     * @param brandId   品牌id
     * @param queryWord 搜索关键字
     */
    @GET("freeapis-web-search/1/spuSearch/brand/home/{brandId}/?length=10")
    Observable<BaseResult<PageInfo<GoodsBean>>> getBrandProduct(@Path("brandId") String brandId, @Query("start") int start,
                                                                @Query("queryWord") String queryWord);

    /**
     * 品牌主页素材
     *
     * @param start     开始位置
     * @param brandId   品牌id
     * @param queryWord 搜索关键字
     */
    @GET("freeapis-web-search/1/sourceMaterialSearch/brand/home/{brandId}/?length=10")
    Observable<BaseResult<PageInfo<MaterialListBean>>> getBrandMaterial(@Path("brandId") String brandId, @Query("start") int start,
                                                                        @Query("queryWord") String queryWord);

    /**
     * 品牌主题
     *
     * @param start     列表开始位置
     * @param brandId   品牌id
     * @param queryWord 搜索
     */
    @GET("freeapis-web-search/1/themeSearch/brand/home/{brandId}/?length=10")
    Observable<BaseResult<PageInfo<ThemeBean>>> getBrandTheme(@Path("brandId") String brandId, @Query("start") int start,
                                                              @Query("queryWord") String queryWord);

    /**
     * 设计师主页商品
     */
    @GET("freeapis-web-search/1/spuSearch/designer/home/{designerId}?length=10")
    Observable<BaseResult<PageInfo<GoodsBean>>> getDesignerProduct(@Path("designerId") String designerId,
                                                                   @Query("start") int start, @Query("queryWord") String queryWord);

    /**
     * 设计师页面素材
     */
    @GET("freeapis-web-search/1/sourceMaterialSearch/designer/home/{designerId}?length=10")
    Observable<BaseResult<PageInfo<MaterialListBean>>> getDesignerMaterial(@Path("designerId") String designerId,
                                                                           @Query("start") int start, @Query("queryWord") String queryWord);

    /**
     * 设计师页面主题
     */
    @GET("freeapis-web-search/1/themeSearch/designer/home/{designerId}?length=10")
    Observable<BaseResult<PageInfo<ThemeBean>>> getDesignerTheme(@Path("designerId") String designerId,
                                                                 @Query("start") int start, @Query("queryWord") String queryWord);

    /**
     * 主题搜索
     */
    @GET("freeapis-web-search/1/themeSearch/portal/page?length=15")
    Observable<BaseResult<PageInfo<ThemeBean>>> searchTheme(@Query("start") int start, @Query("queryWord") String queryWord);

    /**
     * 添加内容到主题
     */
    @POST("freeapis-web-business/1/theme/themeContent")
    Observable<BaseResult<ThemeBean>> addContentToTheme(@Body AddContentToThemeBody body);

    /**
     * 分页查询主题下的所有备选内容
     */
    @GET("freeapis-web-business/1/theme/themeContent/my/page/{themeId}?length=15")
    Observable<BaseResult<PageInfo<NewAddListBean>>> getNewAddThemeContentList(@Path("themeId") String themeId, @Query("Type") String type, @Query("start") int start);

    /**
     * 购物车推荐商品
     */
    @GET("freeapis-web-search/1/recommend/spu/cart")
    Observable<BaseResult<List<GoodsBean>>> getRecommendSpuCart();

    /**
     * 商品详情页根据商品ID和推荐类型查询推荐商品
     * category material  sample tag
     */
    @GET("freeapis-web-search/1/recommend/spu/{spuId}/{recommendType}")
    Observable<BaseResult<List<GoodsBean>>> getRecommendSpu(@Path("spuId") String spuId, @Path("recommendType") String recommendType);

    /**
     * 素材详情页根据素材ID和分类查询推荐素材
     */
    @GET("freeapis-web-search/1/recommend/material/info/{materialId}")
    Observable<BaseResult<List<MaterialListBean>>> getRecommendMaterial(@Path("materialId") String materialId);

    /**
     * 猜你喜欢
     */
    @GET("freeapis-web-search/1/recommend/guessYouLike")
    Observable<BaseResult<List<GoodsBean>>> getRecommendLike();

    /**
     * 搜索列表推荐商品
     */
    @GET("freeapis-web-search/1/recommend/spu/search")
    Observable<BaseResult<List<GoodsBean>>> getRecommendSpuSearch(@Query("keyWords") String keyWords);

    /**
     * 专题商品列表
     */
    @GET("freeapis-web-search/1/spuSearch/portal/topic/page?length=10")
    Observable<BaseResult<ProductListBean<GoodsBean>>> getTopicProductList(@Query("start") int start,
                                                                           @Query("type") String type,
                                                                           @Query("mongoId") String mongoId,
                                                                           @Query("imageId") String imageId,
                                                                           @Query("hotId") String hotId);

    /**
     * 导航商品列表
     */
    @GET("freeapis-web-search/1/spuSearch/portal/navigate/page?length=10")
    Observable<BaseResult<ProductListBean<GoodsBean>>> getNavigateProductList(@Query("start") int start,
                                                                              @Query("navigateCode") String code);

    /**
     * 首页商品列表
     */
    @GET("freeapis-web-search/1/spuSearch/portal/mongo/condition/page?length=10")
    Observable<BaseResult<ProductListBean<GoodsBean>>> getMainProductList(@Query("start") int start,
                                                                          @Query("groupType") String groupType,
                                                                          @Query("groupCode") String groupCode,
                                                                          @Query("entityType") String entityType,
                                                                          @Query("fieldName") String fieldName,
                                                                          @Query("id") String id);

    /**
     * 搜索列表推荐素材
     */
    @GET("freeapis-web-search/1/recommend/material/search")
    Observable<BaseResult<List<MaterialListBean>>> getRecommendSearchMaterial(@Query("keyWords") String keyWords);

    /**
     * 搜索关键词
     */
    @GET("freeapis-web-search/1/suggest/presetWord")
    Observable<BaseResult<List<String>>> getPresetSearchWord();

    /**
     * 综合搜索
     */
    @GET("freeapis-web-search/1/search/portal/page?")
    Observable<BaseResult<MainSearchBean>> searchAll(@Query("queryWord") String queryWord);

    /**
     * 集合搜索
     */
    @GET("freeapis-web-search/1/search/portal/page?")
    Observable<BaseResult<MainSearchBean>> searchCollection(@Query("collectionId") String collectionId);

    /**
     * 模板商品检验过期数据
     */
    @GET("freeapis-web-product/1/designSpu/validation/templateSpu/{spuId}")
    Observable<BaseResult<ValidateProductBean>> validateTemplateSpu(@Path("spuId") String spuId);

    /**
     * 获取待续费素材
     */
    @POST("freeapis-web-business/1/order/freematerial/{spuId}")
    Observable<BaseResult<MaterialDetailBean>> getRenewMaterial(@Path("spuId") String spuId);

    /**
     * 过滤设计过期数据
     * {{freeapis-web-design}}/graphic/validation/{designId}
     */
    @GET("freeapis-web-design/1/graphic/validation/{designId}")
    Observable<BaseResult<ValidateProductBean>> validateDesign(@Path("designId") String designId);

    /**
     * APP扫描登录二维码后改变二维码Token状态
     */
    @PUT("freeapis-web-security/1/authentication/qr/{qrToken}/status")
    Observable<BaseResult<String>> changePcQrStatus(@Path("qrToken") String qrToken);

    /**
     * App 扫码登录
     */
    @POST("freeapis-web-security/1/authentication/qr/{qrToken}")
    Observable<BaseResult<String>> scanQrLogin(@Path("qrToken") String qrToken);

    /**
     * 海报模块
     * 海报页面模板接口
     *
     * @param posterType   平台类型 或者 场景类型
     * @param usePointType 类型  如 素材类型，商品类型
     */
    @GET("freeapis-web-business/1/posterUse/{posterType}/info")
    Observable<BaseResult<List<PosterTemplateBean>>> getPosterTemplate(@Path("posterType") String posterType, @Query("usePointType") String usePointType, @Query("themeId") String themeId);

    /**
     * 海报模块
     * 海报内容数据接口
     *
     * @param id     海报模板id
     * @param typeId 海报数据id  如 素材的id  商品的id
     * @return
     */
    @GET("freeapis-web-business/1/posterUse/{id}/{typeId}")
    Observable<BaseResult<PosterItemBean>> getPosterData(@Path("id") String id, @Path("typeId") String typeId);

    /**
     * 发布帖子
     */
    @POST("freeapis-web-business/1/themePosts/{themeId}")
    Observable<BaseResult<SceneManagerPostBean>> publishPost(@Path("themeId") String themeId, @Body SceneManagerPostBean postBean);

    /**
     * 修改帖子
     */
    @PUT("freeapis-web-business/1/themePosts")
    Observable<BaseResult<SceneManagerPostBean>> modifyPost(@Body SceneManagerPostBean postBean);

    /**
     * 客户发货
     * afterSaleApplyType：returned/replacement
     */
    @PUT("freeapis-web-business/1/afterSaleApply/{afterSaleApplyType}/customer/delivery")
    Observable<BaseResult<AfterSalesBean>> customerDelivery(@Path("afterSaleApplyType") String afterSaleApplyType, @Body AfterSalesGoodsInfo info);

}
