package com.zhaotai.uzao.bean.EventBean.myProduct;

/**
 * Time: 2017/9/12
 * Created by LiYou
 * Description : 我的商品--编辑完成
 */

public class ModifyMyProductEvent {

    public ModifyMyProductEvent(int position, String status) {
        this.position = position;
        this.status = status;
    }

    public int position;
    public String status;
}
