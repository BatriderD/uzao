package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/1/15 0015.
 */

public class UnPayMaterialBean implements Serializable {
    /**
     * sourceMaterialModels : [{"agencyCode":"SUPER_ADMIN","approveIdea":"","authPeriod":12,"canReupload":"N","categoryCode":"A0002","categoryCode1":"A","categoryCode2":"A00","categoryNames":"精灵>皮卡丘>喜庆","changedFields":"","commentCount":24,"createDate":"1515413161000","description":"","designIdea":"蝴蝶的素材","designer":null,"extend1":"","extend2":"","extend3":"","favoriteCount":0,"fileMime":"JPG","fileName":"01d1f65717468132f8758c9b82e3fe.jpg","fileRefer":"950280555063164928.jpg","fileSize":"0.25M","lockDate":null,"lockStatus":"N","lockUserId":"","obtained":false,"periodUnit":"month","price":1,"priceY":"0.01","promotionPrice":null,"recDate":"1515413836000","recStatus":"A","recUserId":"9527","recUserName":"管理员","rewardCount":0,"saleMode":"charge","salesCount":4,"sequenceNBR":"950337795958890496","source":"public","sourceMaterialCode":"950337795958890497","sourceMaterialName":"蝴蝶的素材一","status":"published","tags":[],"thumbnail":"950280542262149120.jpg","totalEarns":0,"upvoteCount":0,"useCount":0,"userId":"908658603425058816","viewCount":0},{"agencyCode":"SUPER_ADMIN","approveIdea":"","authPeriod":12,"canReupload":"N","categoryCode":"A0002","categoryCode1":"A","categoryCode2":"A00","categoryNames":"精灵>皮卡丘>喜庆","changedFields":"","commentCount":3,"createDate":"1515413946000","description":"","designIdea":"蝴蝶设计的很哈珀很好","designer":null,"extend1":"","extend2":"","extend3":"","favoriteCount":0,"fileMime":"JPG","fileName":"01d1f65717468132f8758c9b82e3fe.jpg","fileRefer":"950280555063164928.jpg","fileSize":"0.25M","lockDate":null,"lockStatus":"N","lockUserId":"","obtained":false,"periodUnit":"month","price":1,"priceY":"0.01","promotionPrice":null,"recDate":"1515413970000","recStatus":"A","recUserId":"9527","recUserName":"管理员","rewardCount":0,"saleMode":"charge","salesCount":2,"sequenceNBR":"950341089213075456","source":"public","sourceMaterialCode":"950341089213075457","sourceMaterialName":"蝴蝶的素材二","status":"published","tags":[],"thumbnail":"950280542262149120.jpg","totalEarns":0,"upvoteCount":0,"useCount":0,"userId":"908658603425058816","viewCount":0},{"agencyCode":"SUPER_ADMIN","approveIdea":"","authPeriod":12,"canReupload":"N","categoryCode":"A0002","categoryCode1":"A","categoryCode2":"A00","categoryNames":"精灵>皮卡丘>喜庆","changedFields":"","commentCount":4,"createDate":"1515479984000","description":"","designIdea":"谌荣的素材三收费的","designer":null,"extend1":"","extend2":"","extend3":"","favoriteCount":1,"fileMime":"JPG","fileName":"01d1f65717468132f8758c9b82e3fe.jpg","fileRefer":"950280555063164928.jpg","fileSize":"0.25M","lockDate":null,"lockStatus":"N","lockUserId":"","obtained":false,"periodUnit":"month","price":1,"priceY":"0.01","promotionPrice":null,"recDate":"1515479997000","recStatus":"A","recUserId":"9527","recUserName":"管理员","rewardCount":0,"saleMode":"charge","salesCount":5,"sequenceNBR":"950618073449156608","source":"public","sourceMaterialCode":"950618073449156609","sourceMaterialName":"谌荣的素材三收费的","status":"published","tags":[],"thumbnail":"950280542262149120.jpg","totalEarns":0,"upvoteCount":0,"useCount":0,"userId":"908963586427121664","viewCount":0}]
     * totalAmountY : 0.03
     */

    private String totalAmountY;
    private List<MaterialDetailBean> sourceMaterialModels;

    public String getTotalAmountY() {
        return totalAmountY;
    }

    public void setTotalAmountY(String totalAmountY) {
        this.totalAmountY = totalAmountY;
    }

    public List<MaterialDetailBean> getSourceMaterialModels() {
        return sourceMaterialModels;
    }

    public void setSourceMaterialModels(List<MaterialDetailBean> sourceMaterialModels) {
        this.sourceMaterialModels = sourceMaterialModels;
    }

}
