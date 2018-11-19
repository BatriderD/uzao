package com.zhaotai.uzao.bean;

import java.util.List;

public class PosterItemBean {

    public static final String TYPE_TEXT = "String";
    public static final String TYPE_IMAGE = "ImageLink";
    //图片中的二维码
    public static final String FileCode_Code = "qr";
    /**
     * qrShareUrl : shareTemplate@dev/1017322604786147328_976454964555612160share.html
     * designMeta : [{"dataType":"ImageLink","location":{"height":65.011364,"width":100.011364,"left":359,"top":569},"fieldCode":"qr","fieldName":"二维码"},{"dataType":"ImageLink","location":{"height":124.011364,"width":116.011364,"left":148,"top":231},"fieldValue":"u2/M00/01/84/ooYBAFqyYhCAWOubAAiT0ELOYAg839.png","fieldCode":"thumbnail","fieldName":"商品图片"},{"dataType":"String","location":{"height":22.011364,"width":146.01136400000001,"left":136,"top":3},"fieldValue":"模板商品有BUG","fieldCode":"spuName","fieldName":"商品名称"}]
     * posterBackgroundUrl : u2/M00/04/FB/ooYBAFtMURyAKk8bAAJlNESdsbM874.png
     */

    private String qrShareUrl;
    private String posterBackgroundUrl;
    private List<DesignMetaBean> designMeta;

    public String getQrShareUrl() {
        return qrShareUrl;
    }

    public void setQrShareUrl(String qrShareUrl) {
        this.qrShareUrl = qrShareUrl;
    }

    public String getPosterBackgroundUrl() {
        return posterBackgroundUrl;
    }

    public void setPosterBackgroundUrl(String posterBackgroundUrl) {
        this.posterBackgroundUrl = posterBackgroundUrl;
    }

    public List<DesignMetaBean> getDesignMeta() {
        return designMeta;
    }

    public void setDesignMeta(List<DesignMetaBean> designMeta) {
        this.designMeta = designMeta;
    }

    public static class DesignMetaBean {
        /**
         * dataType : ImageLink
         * location : {"height":65.011364,"width":100.011364,"left":359,"top":569}
         * fieldCode : qr
         * fieldName : 二维码
         * fieldValue : u2/M00/01/84/ooYBAFqyYhCAWOubAAiT0ELOYAg839.png
         */

        private String dataType;
        private LocationBean location;
        private String fieldCode;
        private String fieldName;
        private String fieldValue;

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getFieldCode() {
            return fieldCode;
        }

        public void setFieldCode(String fieldCode) {
            this.fieldCode = fieldCode;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public static class LocationBean {
            /**
             * height : 65.011364
             * width : 100.011364
             * left : 359
             * top : 569
             */

            private double height;
            private double width;
            private int left;
            private int top;

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }
        }
    }
}
