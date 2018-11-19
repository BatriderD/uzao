package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description:
 * author : zp
 * date: 2017/8/18
 */

public class HomeMallBean {

    /**
     * agencyCode : SUPER_ADMIN
     * children : [{"agencyCode":"SUPER_ADMIN","children":[],"columns":[],"description":"","displayMeta":"Y","extend1":"","extend2":"","extend3":"","groupCode":"appProductBanner","groupName":"商城轮播","groupOrder":1,"groupType":"TABLE","hasChildren":false,"nodeLevel":4,"parentGroupCode":"appProduct","recDate":"1502414225000","recStatus":"A","recUserId":"864292845320691712","values":[{"_id":"895822816618803200","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProductBanner","entityId":"2","entityType":"FREEAPIS","referName":"","imageName":"8987612_143254729000_2.jpg","referType":"","image":"898010741481734144.jpg","caption":"热热热","referId":"","recDate":"1502937420661","createDate":"1502415777722","contentBody":""},{"_id":"895823000824246272","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProductBanner","entityId":"2","entityType":"FREEAPIS","referName":"","imageName":"79f0f736afc37931748bca84e9c4b74542a911bf.jpg","referType":"","image":"898010782942429184.jpg","caption":"同仁堂","referId":"","recDate":"1502937430899","createDate":"1502415821640","contentBody":""},{"_id":"895823036966563840","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProductBanner","entityId":"2","entityType":"FREEAPIS","referName":"","imageName":"55aa56a5ad230157.jpg!600x600.jpg","referType":"","image":"898010811715354624.jpg","caption":"同仁堂","referId":"","recDate":"1502937437323","createDate":"1502415830257","contentBody":""},{"_id":"895823083808550912","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProductBanner","entityId":"2","entityType":"FREEAPIS","referName":"","imageName":"01e4d85717468132f8758c9b70d852.jpg","referType":"","image":"898010901033058304.jpg","caption":"fsfsdf","referId":"","recDate":"1502937460874","createDate":"1502937460874","contentBody":""}]},{"agencyCode":"SUPER_ADMIN","children":[{"agencyCode":"SUPER_ADMIN","children":[],"columns":[],"description":"","displayMeta":"Y","extend1":"","extend2":"","extend3":"","groupCode":"appProductImg","groupName":"商城热卖图片","groupOrder":1,"groupType":"FORM","hasChildren":false,"nodeLevel":5,"parentGroupCode":"appProductHot","recDate":"1502704970000","recStatus":"A","recUserId":"865121288836952064","values":{"_id":"898011260493266944","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProductImg","entityId":"2","entityType":"FREEAPIS","referName":"清凉一夏商品","imageName":"9aeb1355-1f85-46d9-aa25-9536963b9326.jpg","referType":"productDetail","image":"898011231384829952.jpg","caption":null,"referId":"893437201440956416","recDate":"1502937543402"}},{"agencyCode":"SUPER_ADMIN","children":[],"columns":[],"description":"","displayMeta":"Y","extend1":"","extend2":"","extend3":"","groupCode":"appProducts","groupName":"热卖商品列表","groupOrder":2,"groupType":"TABLE","hasChildren":false,"nodeLevel":5,"parentGroupCode":"appProductHot","recDate":"1502705019000","recStatus":"A","recUserId":"865121288836952064","values":[{"_id":"898135927010918400","AGENCY_CODE":"SUPER_ADMIN","GROUP_CODE":"appProducts","entityId":"2","entityType":"FREEAPIS","referName":"新版懒人沙发","contentBody":"{\"tagInfo\":\"棉布,好的品质\",\"spuName\":\"棉布,好的品质\",\"description\":\"舒适的沙发\",\"displayPriceY\":\"2.01\",\"displayPrice\":201,\"sequenceNBR\":\"894467090977697792\",\"thumbnail\":\"894466371574849536.png\",\"spuType\":\"mall\"}","imageName":"专题四01.png","description":"这是副标题111","referType":"productDetail","image":"898135856215293952.png","caption":null,"referId":"894467090977697792","recDate":"1502967286229","createDate":"1502967266215"}]}],"columns":[],"description":"","displayMeta":"Y","extend1":"","extend2":"","extend3":"","groupCode":"appProductHot","groupName":"商城热卖","groupOrder":2,"groupType":"FORM","hasChildren":true,"nodeLevel":4,"parentGroupCode":"appProduct","recDate":"1502330316000","recStatus":"A","recUserId":"864292845320691712","values":null}]
     * columns : []
     * description :
     * displayMeta : Y
     * extend1 :
     * extend2 :
     * extend3 :
     * groupCode : appProduct
     * groupName : 商城
     * groupOrder : 3
     * groupType : FORM
     * hasChildren : true
     * nodeLevel : 3
     * parentGroupCode : app
     * recDate : 1502270842000
     * recStatus : A
     * recUserId : 864292845320691712
     * values : null
     */

    private String agencyCode;
    private String description;
    private String displayMeta;
    private String extend1;
    private String extend2;
    private String extend3;
    private String groupCode;
    private String groupName;
    private int groupOrder;
    private String groupType;
    private boolean hasChildren;
    private int nodeLevel;
    private String parentGroupCode;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private Object values;

    public List<HomeMallBean> getChildren() {
        return children;
    }

    public void setChildren(List<HomeMallBean> children) {
        this.children = children;
    }

    private List<HomeMallBean> children;
    private List<?> columns;

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayMeta() {
        return displayMeta;
    }

    public void setDisplayMeta(String displayMeta) {
        this.displayMeta = displayMeta;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(int groupOrder) {
        this.groupOrder = groupOrder;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public int getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(int nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getParentGroupCode() {
        return parentGroupCode;
    }

    public void setParentGroupCode(String parentGroupCode) {
        this.parentGroupCode = parentGroupCode;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }

    public String getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(String recUserId) {
        this.recUserId = recUserId;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }


    public List<?> getColumns() {
        return columns;
    }

    public void setColumns(List<?> columns) {
        this.columns = columns;
    }

}
