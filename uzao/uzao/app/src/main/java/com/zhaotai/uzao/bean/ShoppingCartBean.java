package com.zhaotai.uzao.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/26
 * Created by LiYou
 * Description :  购物车列表实体类
 */

public class ShoppingCartBean implements Serializable{
    public String preferentiaPrice;//优惠价格
    public String countPrice;//商品总价
    public List<Cart> carts;
    public String finalPrice;//最终实际价格（商品总价+运费-优惠）
    public String deliveryPrice; //运费
    public List<ShoppingGoodsBean> invalidCarts;//失效商品集合


    public static class Cart implements Serializable {
        public ActivityModelBean activityModel;
        public List<ShoppingGoodsBean> cartModels;
        public String cut;
        public String full;
        public String minFull;
        public boolean isFailure;//是否是 失效商品

        /**
         * 组是否被选中
         */
        public boolean isGroupSelected;

        public List<ShoppingGoodsBean> getCartModels() {
            return cartModels;
        }

        public void setCartModels(List<ShoppingGoodsBean> cartModels) {
            this.cartModels = cartModels;
        }

        public boolean isGroupSelected() {
            return isGroupSelected;
        }

        public void setIsGroupSelected(boolean groupSelected) {
            isGroupSelected = groupSelected;
        }
    }

}
