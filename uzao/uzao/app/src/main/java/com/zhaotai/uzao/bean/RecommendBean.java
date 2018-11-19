package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/7/27
 * Created by zp
 * Description : 首页bean类
 */

public class RecommendBean implements MultiItemEntity, Serializable {
    //more
    public final static int END = -1;
    //title
    public final static int TITLE = -2;
    //空页面
    public final static int NULL = 0;
    //推荐轮播
    public final static int BANNERS = 1;
    //一日一设
    public final static int DAY_DESIGN = 2;
    //热门主题
    public final static int HOT_THEME = 3;
    //设计达人
    public final static int GOOD_DESIGNER = 4;
    //热卖单品
    public final static int PRODUCTHOTS = 5;
    //原创商品
    public final static int ORIGINAL_PRODUCT = 6;
    //new商品
    public final static int NEW_PRODUCT = 7;
    //new商品
    public final static int NEW_MATERIAL = 8;


    //    给定类型
    private int type;


    //  细分类  0中间 1  头  2 尾
    private int contentType;
    //    名称
    private String title;
    // 内部的顺序位置  如果只有一个的话那么是-1
    public int inSidePos = -1;

    //只有一个实体类的
    public ValueBean valueBean;

    public ArrayList<ValueBean> valueBeans;
    public String groupType;


    public RecommendBean(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public RecommendBean(int type) {
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }




    @Override
    public int getItemType() {
        return type;
    }




    public static class ValueBean {
        public String GROUP_CODE;
        public String _id;
        public String entityType;
        public String referName;
        public String contentBody;
        public String contentBody1;
        public String contentBody2;
        public String contentBody3;
        public String referType1;
        public String referType2;
        public String referType3;
        public String imageName;
        public String referType;
        public String image;
        public String designer;
        public String alias;

        public String getReferName() {
            return referName;
        }

        public void setReferName(String referName) {
            this.referName = referName;
        }

        public String getContentBody() {
            return contentBody;
        }

        public void setContentBody(String contentBody) {
            this.contentBody = contentBody;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getReferType() {
            return referType;
        }

        public void setReferType(String referType) {
            this.referType = referType;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class Designer{
        public String nickName;
        public String avatar;
        public String userId;

    }
    public static class BaseContentBody {

    }

    /**
     * 主题的contentBody
     */
    public static class ThemeContentBody extends BaseContentBody {

        /**
         * sequenceNBR : 961450113622962176
         * name : 古风
         * cover : u2/M00/01/03/ooYBAFqY7m6ABy2VAABaMQx7MWU084.jpg
         * wapUrl : themeTemplate@debug/961450113622962176wap.html
         * webUrl : themeTemplate@debug/961450113622962176pc.html
         * userName :
         * userId : 909692370717585408
         * type : user
         * viewCount : 11
         * spuCount : 0
         * commentCount : 5
         * favoriteCount : 0
         * templateId : 968429223700189184
         * materialCount : 0
         * brandId : 0
         * description : 古风
         */

        private String sequenceNBR;
        private String name;
        private String cover;
        private String wapUrl;
        private String webUrl;
        private String userName;
        private String userId;
        private String type;
        private int viewCount;
        private int spuCount;
        private int commentCount;
        private int favoriteCount;
        private String templateId;
        private int materialCount;
        private String brandId;
        private String description;
        public String nickName;


        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getWapUrl() {
            return wapUrl;
        }

        public void setWapUrl(String wapUrl) {
            this.wapUrl = wapUrl;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public int getSpuCount() {
            return spuCount;
        }

        public void setSpuCount(int spuCount) {
            this.spuCount = spuCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(int favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public int getMaterialCount() {
            return materialCount;
        }

        public void setMaterialCount(int materialCount) {
            this.materialCount = materialCount;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    /**
     * 用户的contentBody
     */
    public static class AuthorContentBody extends BaseContentBody {

        private String aboutMe;
        private String nickName;
        private int favoriteCount;
        private int followCount;
        private int myDesignSpuCount;
        private String avatar;
        private String userId;
        private String cityName;

        public String getIsAttention() {
            return isAttention;
        }

        public void setIsAttention(String isAttention) {
            this.isAttention = isAttention;
        }

        public String isAttention;
        private List<TagsBean> tags;

        public String getAboutMe() {
            return aboutMe;
        }

        public void setAboutMe(String aboutMe) {
            this.aboutMe = aboutMe;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(int favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public int getMyDesignSpuCount() {
            return myDesignSpuCount;
        }

        public void setMyDesignSpuCount(int myDesignSpuCount) {
            this.myDesignSpuCount = myDesignSpuCount;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * tagCode : 0104
             * tagName : 信阳
             */

            private String tagCode;
            private String tagName;

            public String getTagCode() {
                return tagCode;
            }

            public void setTagCode(String tagCode) {
                this.tagCode = tagCode;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }
        }
    }

    /**
     * 商品的contentBody
     */
    public static class ProductContentBody extends BaseContentBody {

        private DesignerBean designer;
        private String description;
        private String spuName;
        private String displayPriceY;
        private int displayPrice;
        private String sequenceNBR;
        private String thumbnail;
        private String activityIcon;
        private String spuType;

        public String getIsTemplate() {
            return isTemplate;
        }

        public void setIsTemplate(String isTemplate) {
            this.isTemplate = isTemplate;
        }

        private String isTemplate;


        public String getDesignType() {
            return designType;
        }

        public void setDesignType(String designType) {
            this.designType = designType;
        }

        private String designType;
        private List<?> tagInfo;

        public DesignerBean getDesigner() {
            return designer;
        }

        public void setDesigner(DesignerBean designer) {
            this.designer = designer;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSpuName() {
            return spuName;
        }

        public void setSpuName(String spuName) {
            this.spuName = spuName;
        }

        public String getDisplayPriceY() {
            return displayPriceY;
        }

        public void setDisplayPriceY(String displayPriceY) {
            this.displayPriceY = displayPriceY;
        }

        public int getDisplayPrice() {
            return displayPrice;
        }

        public void setDisplayPrice(int displayPrice) {
            this.displayPrice = displayPrice;
        }

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getActivityIcon() {
            return activityIcon;
        }

        public void setActivityIcon(String activityIcon) {
            this.activityIcon = activityIcon;
        }

        public String getSpuType() {
            return spuType;
        }

        public void setSpuType(String spuType) {
            this.spuType = spuType;
        }

        public List<?> getTagInfo() {
            return tagInfo;
        }

        public void setTagInfo(List<?> tagInfo) {
            this.tagInfo = tagInfo;
        }

        public static class DesignerBean {
            /**
             * nickName : 请叫我拆塔小达人
             * userId : 908263618104045568
             * avatar : u2/M00/00/70/ooYBAFpsUt6AdVVKAAM-XjYMDZ4009.jpg
             */

            public String nickName;
            public String userId;
            public String avatar;
            public String aboutMe;
            public String myDesignSpuCount;
        }
    }

    public static class BrandContentBody extends BaseContentBody{

        /**
         * sequenceNBR : 978192784827711488
         * brandName : 任天堂
         * brandLogo : u2/M00/01/B5/ooYBAFq4tJGAYHPJAAAtMKrcR8s036.png
         * brandAbout :
         * description :
         */

        private String sequenceNBR;
        private String brandName;
        private String brandLogo;
        private String brandAbout;
        private String description;

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandLogo() {
            return brandLogo;
        }

        public void setBrandLogo(String brandLogo) {
            this.brandLogo = brandLogo;
        }

        public String getBrandAbout() {
            return brandAbout;
        }

        public void setBrandAbout(String brandAbout) {
            this.brandAbout = brandAbout;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class TopicContentContentBody extends BaseContentBody{

        /**
         * sequenceNBR : 976714458430734336
         * webUrl : topicActivity@dev/976714458430734337.html
         * name : iOOOS
         * topicType : mobile
         * description : iOOOS
         */

        private String sequenceNBR;
        private String webUrl;
        private String name;
        private String topicType;
        private String description;

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTopicType() {
            return topicType;
        }

        public void setTopicType(String topicType) {
            this.topicType = topicType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
    /**
     * 素材的contentBody
     */
    public static class MaterialContentBody extends BaseContentBody {
        private String sequenceNBR;
        private String name;
        private String thumbnail;
        private Object designer;
        private String userId;
        private String source;
        private int viewCount;
        private int useCount;
        private int commentCount;
        private int favoriteCount;
        private String designIdea;

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Object getDesigner() {
            return designer;
        }

        public void setDesigner(Object designer) {
            this.designer = designer;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(int favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public String getDesignIdea() {
            return designIdea;
        }

        public void setDesignIdea(String designIdea) {
            this.designIdea = designIdea;
        }
    }
}
