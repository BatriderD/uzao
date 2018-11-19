package com.zhaotai.uzao.bean;

import android.net.Uri;

import java.io.File;
import java.util.List;

/**
 * Time: 2017/8/16
 * Created by LiYou
 * Description :
 */

public class CommentBean {
    public List<CommentBean> children;
    public String commentBody;
    public List<String> commentImage;
    public String entityProfile;
    public String orderDetailId;
    public String entityId;
    public String haveComment;
    public String pic;
    public String spuName;

    public String totalCount;
    public String haveImageCount;
    public String recDate;
    public String userId;
    public String userInfo;
    public int starScore;

    public List<Uri> imgList;//用于记录待评价图片
    public List<String> imgName;
    public List<File> imgFiles;

}
