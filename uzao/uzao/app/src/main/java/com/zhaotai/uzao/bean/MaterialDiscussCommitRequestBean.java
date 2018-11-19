package com.zhaotai.uzao.bean;

/**
 * description: 素材评论请求类
 * author : ZP
 * date: 2018/1/11 0011.
 */

public class MaterialDiscussCommitRequestBean {
    private String commentBody;
    private String firstCommentId;
    private String parentId;
    private String extend1;

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public MaterialDiscussCommitRequestBean(String commentBody, String firstCommentId, String parentId) {
        this.commentBody = commentBody;
        this.firstCommentId = firstCommentId;
        this.parentId = parentId;
    }


    @Override
    public String toString() {
        return "MaterialDiscussCommitRequestBean{" +
                "commentBody='" + commentBody + '\'' +
                ", firstCommentId='" + firstCommentId + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }

    public MaterialDiscussCommitRequestBean(String commentBody) {
        this.commentBody = commentBody;
    }
}
