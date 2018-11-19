package com.zhaotai.uzao.ui.shopping.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.ShoppingCartBean;
import com.zhaotai.uzao.bean.ShoppingGoodsBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.ConfirmOrderActivity;
import com.zhaotai.uzao.ui.shopping.adapter.MultiCartItem;
import com.zhaotai.uzao.ui.shopping.contract.ShoppingCartContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Time: 2017/5/26
 * Created by LiYou
 * Description :
 */

public class ShoppingCartPresenter extends ShoppingCartContract.Presenter {

    private ShoppingCartContract.View view;
    private List<MultiCartItem> multiList = new ArrayList<>(); //全部商品
    private List<MultiCartItem> multiInvalidList = new ArrayList<>();//失效商品
    private List<MultiCartItem> multiRecommendList = new ArrayList<>();//推荐商品
    private ShoppingCartBean selectGoods;//记录选中的商品
    private List<String> cartIds = new ArrayList<>();//存储选中的商品id
    private boolean isEmpty = false;

    public ShoppingCartPresenter(ShoppingCartContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取购物车
     */
    @Override
    public void getCartList(boolean isLoading) {
        Api.getDefault().getShoppingCart()
                .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, isLoading) {
                    @Override
                    public void _onNext(ShoppingCartBean shoppingCartBean) {
                        view.showContent();
                        view.stopRefresh();
                        changeData(shoppingCartBean);
                        view.showUpdatePrice("0.00", "0.00", "0.00");
                        view.showSelectAll(false);
                        getRecommendSpu();
                    }

                    @Override
                    public void _onError(String message) {
                        view.stopRefresh();
                        view.showNetworkFail(message);
                    }
                });
    }

    /**
     * 获取全部商品
     */
    public List<MultiCartItem> getAllGoods() {
        return this.multiList;
    }

    /**
     * 获取失效商品
     */
    public List<MultiCartItem> getInvalidGoods() {
        return this.multiInvalidList;
    }

    /**
     * 获取购物车推荐商品
     */
    private void getRecommendSpu() {
        Api.getDefault().getRecommendSpuCart()
                .compose(RxHandleResult.<List<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<GoodsBean> recommendSpuList) {
                        changeRecommendData(recommendSpuList);
                        view.showCartData(multiList);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 构造商品数据
     */
    private void changeData(ShoppingCartBean shoppingCartBean) {
        multiList.clear();
        multiInvalidList.clear();
        //添加 普通商品和活动商品
        if (shoppingCartBean.carts != null && shoppingCartBean.carts.size() > 0) {
            view.showBottomAccount(true);
            isEmpty = false;
            view.setModifyBtnVisible(true);
            for (int i = 0; i < shoppingCartBean.carts.size(); i++) {
                ShoppingCartBean.Cart cart = shoppingCartBean.carts.get(i);
                for (int j = 0; j < shoppingCartBean.carts.get(i).cartModels.size(); j++) {
                    MultiCartItem multi;
                    ShoppingGoodsBean goodsBean = shoppingCartBean.carts.get(i).cartModels.get(j);
                    if (cart.activityModel == null) {
                        multi = new MultiCartItem(MultiCartItem.TYPE_NORMAL);
                    } else {
                        multi = new MultiCartItem(MultiCartItem.TYPE_ACTIVITY);
                        multi.activityIcon = cart.activityModel.icon;
                        multi.cut = cart.cut;
                    }
                    multi.groupId = i;
                    multi.isGroupHeader = j == 0;
                    multi.count = goodsBean.count;
                    multi.spuName = goodsBean.spuName;
                    multi.unitPrice = goodsBean.unitPrice;
                    multi.properties = goodsBean.properties;
                    multi.spuPic = goodsBean.spuPic;
                    multi.spuId = goodsBean.spuId;
                    multi.spuType = goodsBean.spuType;
                    multi.cartId = goodsBean.sequenceNBR;
                    multi.storeCount = goodsBean.storeCount;
                    this.multiList.add(multi);
                }
            }
        } else {
            if (shoppingCartBean.invalidCarts != null && shoppingCartBean.invalidCarts.isEmpty()) {
                MultiCartItem multi = new MultiCartItem(MultiCartItem.TYPE_EMPTY_GOODS);
                this.multiList.add(multi);
                view.showBottomAccount(false);
                view.setModifyBtnVisible(false);
                isEmpty = true;
            }
        }
        //添加失效商品
        if (shoppingCartBean.invalidCarts != null && shoppingCartBean.invalidCarts.size() > 0) {
            int invalidCount = shoppingCartBean.invalidCarts.size();
            for (int i = 0; i < shoppingCartBean.invalidCarts.size(); i++) {
                ShoppingGoodsBean goodsBean = shoppingCartBean.invalidCarts.get(i);
                MultiCartItem multi = new MultiCartItem(MultiCartItem.TYPE_INVALID);
                multi.isGroupHeader = i == 0;
                multi.isInvalidGoods = true;
                multi.invalidCount = invalidCount;
                multi.count = goodsBean.count;
                multi.spuName = goodsBean.spuName;
                multi.unitPrice = goodsBean.unitPrice;
                multi.properties = goodsBean.properties;
                multi.spuPic = goodsBean.spuPic;
                multi.spuId = goodsBean.spuId;
                multi.spuType = goodsBean.spuType;
                multi.cartId = goodsBean.sequenceNBR;
                this.multiList.add(multi);
                this.multiInvalidList.add(multi);
            }
        } else {
            MultiCartItem multi = new MultiCartItem(MultiCartItem.TYPE_EMPTY_INVALID_GOODS);
            this.multiInvalidList.add(multi);
        }
    }

    /**
     * 构造推荐数据
     *
     * @param recommendSpuList 推荐
     */
    private void changeRecommendData(List<GoodsBean> recommendSpuList) {
        this.multiRecommendList.clear();
        if (recommendSpuList.size() > 0) {
            this.multiList.add(new MultiCartItem(MultiCartItem.TYPE_GUESS_TITLE));
            this.multiRecommendList.add(new MultiCartItem(MultiCartItem.TYPE_GUESS_TITLE));
            for (int i = 0; i < recommendSpuList.size(); i++) {
                MultiCartItem multi = new MultiCartItem(MultiCartItem.TYPE_RECOMMEND);
                GoodsBean goodsBean = recommendSpuList.get(i);
                multi.description = goodsBean.description;
                multi.spuPic = goodsBean.thumbnail;
                multi.spuName = goodsBean.spuName;
                multi.displayPriceY = goodsBean.displayPriceY;
                multi.spuId = goodsBean.sequenceNBR;
                this.multiList.add(multi);
                this.multiRecommendList.add(multi);
            }
        }
    }

    /**
     * @param position checkbox选中位置
     * @param isHeader 是否分组头
     */
    public void checkItemState(int position, boolean isHeader) {
        boolean isSelectAll = true;
        for (int i = 0; i < multiList.size(); i++) {
            MultiCartItem item = multiList.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (position == i) {
                    if (isHeader) {
                        //点击组头
                        item.isGroupSelect = !item.isGroupSelect;
                        setGroupHeaderSelect(multiList, item.groupId, item.isGroupSelect);
                    } else {
                        //点击组内成员
                        item.isSelect = !item.isSelect;
                        isSelectAllChild(multiList, item.groupId);
                    }
                }
                if (!item.isSelect) {
                    isSelectAll = false;
                }
            }
        }
        view.showSelectAll(isSelectAll);
        //更新价格
        updateSelectPrice();
    }

    /**
     * 表头选中
     *
     * @param data     购物车数据
     * @param groupId  组id
     * @param isSelect 分组头是否被选中
     */
    private void setGroupHeaderSelect(List<MultiCartItem> data, int groupId, boolean isSelect) {
        for (int i = 0; i < data.size(); i++) {
            MultiCartItem item = data.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (groupId == item.groupId) {
                    item.isSelect = isSelect;
                }
            }
        }
    }

    /**
     * 组内所有子选项是否全部被选中
     *
     * @param data    购物车数据
     * @param groupId 组id
     */
    private void isSelectAllChild(List<MultiCartItem> data, int groupId) {
        boolean isSelectAll = true;
        int headerPosition = 0;
        for (int i = 0; i < data.size(); i++) {
            MultiCartItem item = data.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (groupId == item.groupId) {
                    if (item.isGroupHeader) {
                        headerPosition = i;
                    }
                    if (!item.isSelect) {
                        isSelectAll = false;
                    }
                }
            }
        }
        if (isSelectAll) {
            for (int i = 0; i < data.size(); i++) {
                MultiCartItem item = data.get(i);
                if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                    if (groupId == item.groupId) {
                        if (item.isGroupHeader) {
                            item.isGroupSelect = true;
                        }
                    }
                }
            }
        } else {
            data.get(headerPosition).isGroupSelect = false;
        }
    }

    /**
     * 更新选中商品总价
     */
    public void updateSelectPrice() {
        cartIds.clear();
        for (int i = 0; i < multiList.size(); i++) {
            MultiCartItem item = multiList.get(i);
            if (item.isSelect) {
                cartIds.add(item.cartId);
            }
        }
        if (cartIds.size() > 0) {
            String id = "";
            for (int i = 0; i < cartIds.size(); i++) {
                id += cartIds.get(i) + ",";
            }
            Api.getDefault().getShoppingCartPrice(id)
                    .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                    .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, true) {
                        @Override
                        public void _onNext(ShoppingCartBean shoppingCartBean) {
                            selectGoods = shoppingCartBean;
                            if (view != null) {
                                view.showUpdatePrice(shoppingCartBean.countPrice, shoppingCartBean.finalPrice, shoppingCartBean.preferentiaPrice);
                            }
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        } else {
            if (view != null) {
                view.showUpdatePrice("0.00", "0.00", "0.00");
            }
        }
    }

    /**
     * 全选
     */
    public void setSelectAll(boolean isSelectAll) {
        for (int i = 0; i < this.multiList.size(); i++) {
            MultiCartItem item = this.multiList.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                item.isSelect = isSelectAll;
                if (item.isGroupHeader) {
                    item.isGroupSelect = isSelectAll;
                }
            }
        }
    }

    /**
     * 更新商品数量
     */
    public void updateGoodsCount(boolean isPlus, final int position) {
        String currentNum = this.multiList.get(position).count.trim();
        String num = "1";
        if (isPlus) {
            num = String.valueOf(Integer.parseInt(currentNum) + 1);
        } else {
            int i = Integer.parseInt(currentNum);
            if (i > 1) {
                num = String.valueOf(i - 1);
            } else {
                return;
            }
        }
        String cartId = multiList.get(position).cartId;
        final String finalNum = num;
        boolean isShowLoading = !multiList.get(position).isSelect;
        Api.getDefault().updateCartCount(cartId, num)
                .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, isShowLoading) {
                    @Override
                    public void _onNext(ShoppingCartBean shoppingGoodsBean) {
                        multiList.get(position).count = finalNum;
                        view.notifyItem(position);
                        if (multiList.get(position).isSelect) {
                            updateSelectPrice();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 更新商品数量
     */
    public void updateGoodsCount(final String count, final int position, MultiCartItem item) {
        String cartId = item.cartId;
        boolean isShowLoading = !item.isSelect;
        Api.getDefault().updateCartCount(cartId, count)
                .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, isShowLoading) {
                    @Override
                    public void _onNext(ShoppingCartBean shoppingGoodsBean) {
                        multiList.get(position).count = count;
                        view.notifyItem(position);
                        if (multiList.get(position).isSelect) {
                            updateSelectPrice();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 结算
     */
    public void closeAccount() {
        boolean hasSelect = false;
        for (int i = 0; i < this.multiList.size(); i++) {
            MultiCartItem item = this.multiList.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (item.isSelect) {
                    hasSelect = true;
                }
            }
        }
        if (hasSelect) {
            if (selectGoods != null) {
                ConfirmOrderActivity.launch(mContext, selectGoods, "cart");
            }
        } else {
            ToastUtil.showShort("亲，先选择商品！");
        }
    }

    /**
     * 删除商品
     */
    public void deleteGoods() {
        cartIds.clear();
        for (int i = 0; i < this.multiList.size(); i++) {
            MultiCartItem item = this.multiList.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (item.isSelect) {
                    cartIds.add(item.cartId);
                }
            }
        }
        if (cartIds.size() > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setMessage("要删除所选商品?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("取消", null);
            dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Api.getDefault().deleteCartGoods(cartIds)
                            .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                            .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, true) {
                                @Override
                                public void _onNext(ShoppingCartBean shoppingCartBean) {
                                    changeData(shoppingCartBean);
                                    view.showUpdatePrice("0.00", "0.00", "0.00");
                                    view.showSelectAll(false);
                                    view.setModifyBtnReset();
                                    multiList.addAll(multiRecommendList);
                                    view.showCartData(multiList);
                                }

                                @Override
                                public void _onError(String message) {

                                }
                            });
                }
            });
            dialog.show();
        } else {
            ToastUtil.showShort("亲,您还没选择商品");
        }
    }

    /**
     * 移动到收藏夹 商品
     */
    public void moveToCollect() {
        cartIds.clear();
        for (int i = 0; i < this.multiList.size(); i++) {
            MultiCartItem item = this.multiList.get(i);
            if (item.getItemType() == MultiCartItem.TYPE_NORMAL || item.getItemType() == MultiCartItem.TYPE_ACTIVITY) {
                if (item.isSelect) {
                    cartIds.add(item.cartId);
                }
            }
        }
        if (cartIds.size() > 0) {
            Api.getDefault().moveToCollect(cartIds)
                    .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                    .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, true) {
                        @Override
                        public void _onNext(ShoppingCartBean shoppingCartBean) {
                            changeData(shoppingCartBean);
                            view.showUpdatePrice("0.00", "0.00", "0.00");
                            view.showSelectAll(false);
                            view.setModifyBtnReset();
                            multiList.addAll(multiRecommendList);
                            view.showCartData(multiList);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        } else {
            ToastUtil.showShort("亲,您还没选择商品");
        }
    }

    /**
     * 清除购物车失效商品
     */
    public void clearInvalidGoods() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("是否清空失效商品?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Api.getDefault().deleteInvalidCartGoods()
                        .compose(RxHandleResult.<ShoppingCartBean>handleResult())
                        .subscribe(new RxSubscriber<ShoppingCartBean>(mContext, true) {
                            @Override
                            public void _onNext(ShoppingCartBean shoppingCartBean) {
                                multiInvalidList.clear();
                                multiInvalidList.add(new MultiCartItem(MultiCartItem.TYPE_EMPTY_INVALID_GOODS));
                                Iterator<MultiCartItem> iterator = multiList.iterator();
                                while (iterator.hasNext()) {
                                    MultiCartItem item = iterator.next();
                                    if (item.isInvalidGoods) {
                                        iterator.remove();
                                    }
                                }
                                view.notifyDataChanged();
                            }

                            @Override
                            public void _onError(String message) {
                                ToastUtil.showShort("清空失效商品失败");
                            }
                        });
            }
        });
        dialog.show();
    }

    /**
     * 是否显示结算布局
     */
    public boolean isShowBottomAccount() {
        return isEmpty;
    }
}
