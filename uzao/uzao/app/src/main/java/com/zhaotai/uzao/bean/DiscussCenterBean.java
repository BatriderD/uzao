package com.zhaotai.uzao.bean;

/**
 * description:
 * author : ZP
 * date: 2018/1/11 0011.
 */

public class DiscussCenterBean {
    /**
     * childrenPage : {"currentPage":1,"hasNextPage":false,"hasPreviousPage":false,"list":[{"auditStatus":"Y","avatar":"912564440853905408.png","changedFields":"","commentBody":"wwwwwwww","commentImage":[],"description":"","entityId":"948502063283785728","entityType":"net.freeapis.product.face.entity.SourceMaterial","extend1":"","extend2":"","extend3":"","firstCommentId":"951386666482823168","hasChildren":true,"haveImage":"N","isAudit":"Y","mobile":"","nickName":"优造378065","orderDetailId":null,"orderNo":"","orderProfile":"","otherNickName":"优造378065","parentId":"951416330588893184","productOrderDetailModel":null,"recDate":"1515670352000","recStatus":"A","recUserId":"908655613469315072","sequenceNBR":"951416532506800128","starScore":null,"thumbnail":"","upCommentBody":"","upvote":false,"upvoteCount":0,"userId":"908655613469315072"}],"nextPageIndex":1,"pageEndRow":0,"pageRecorders":10,"pageStartRow":0,"previousPageIndex":1,"totalPages":1,"totalRows":1}
     * parent : {"auditStatus":"Y","avatar":"912564440853905408.png","changedFields":"","commentBody":"龙哥镇宁","commentImage":[],"description":"","entityId":"948502063283785728","entityType":"net.freeapis.product.face.entity.SourceMaterial","extend1":"","extend2":"","extend3":"","firstCommentId":"951386666482823168","hasChildren":false,"haveImage":"N","isAudit":"Y","mobile":"","nickName":"优造378065","orderDetailId":null,"orderNo":"","orderProfile":"","otherNickName":"","parentId":"ROOT","productOrderDetailModel":null,"recDate":"1515663231000","recStatus":"A","recUserId":"908655613469315072","sequenceNBR":"951386666482823168","starScore":null,"thumbnail":"","upCommentBody":"","upvote":true,"upvoteCount":1,"userId":"908655613469315072"}
     */

    private PageInfo<DiscussBean> childrenPage;
    private DiscussBean parent;

    public PageInfo<DiscussBean> getChildrenPage() {
        return childrenPage;
    }

    public void setChildrenPage(PageInfo<DiscussBean> childrenPage) {
        this.childrenPage = childrenPage;
    }

    public DiscussBean getParent() {
        return parent;
    }

    public void setParent(DiscussBean parent) {
        this.parent = parent;
    }



}
