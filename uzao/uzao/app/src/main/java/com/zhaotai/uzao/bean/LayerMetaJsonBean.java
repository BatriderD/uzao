package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 数据化保存面数据
 * 注意 这里面需要注意的一点有：这里面的数据都是标准数据
 * 所谓标准是载体提供的数据，而不是直接使用的屏幕大小的控件直接产生的的数据（宽高缩放系数等数据都有影响）
 * author : ZP
 * date: 2017/11/25 0025.
 */

public class LayerMetaJsonBean implements Serializable {
    //面名称
    private String aspectId;
    //图层数据
    private List<LayerMetaBean> layerMeta;
    //背景图
    private String thumbnail;
    //工艺id
    private String craftId;
    //使用的素材id列表
    private ArrayList<String> materialIds;

    public String getCraftId() {
        return craftId;
    }

    public void setCraftId(String craftId) {
        this.craftId = craftId;
    }

    public ArrayList<String> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(ArrayList<String> materialIds) {
        this.materialIds = materialIds;
    }


    @Override
    public String toString() {
        return "LayerMetaJsonBean{" +
                "aspectId='" + aspectId + '\'' +
                ", layerMeta=" + layerMeta +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAspectId() {
        return aspectId;
    }

    public void setAspectId(String aspectId) {
        this.aspectId = aspectId;
    }

    public List<LayerMetaBean> getLayerMeta() {
        return layerMeta;
    }

    public void setLayerMeta(List<LayerMetaBean> layerMeta) {
        this.layerMeta = layerMeta;
    }


    /**
     * description: 一个控件元素的数据化对象
     * 这里面数据冗余管理 字段解释有图片或者文字字样的是这种格式的数据有用 如果没有说明的是通用字段
     * author : ZP
     * date: 2017/11/25 0025.
     */
    public static class LayerMetaBean implements Serializable {
        //图片压缩系数
        private float resizeScale;
        /**
         * 给后台的生产图片压缩系数
         * 是标准压缩系数的倍数，
         * 比如一个100*100 像素的图片 放在1000*1000蒙版，当被放大到200*200 的话这个压缩系数就是2
         * 但是我们的图片和蒙版都是压缩的图片 不是真实图片，并且有压缩系数
         * devScale = currentScale /StandScale ;
         * standScale = 蒙版压缩系数 / 图片压缩系数
         */
        private float devScale;
        //类型 目前有2种有文字text 图片bitmap两种
        private String type;
        //旋转角度
        private float rotation;
        //缩放系数
        private float scale;
        //当前序号
        private int index;
        //位置数据
        private LocalBean local;
        //图片是否处理过白色背景透明化
        private boolean transparent;
        //图片地址 半路径
        private String img;
        //裁剪数据
        private SourceRectBean sourceRect;
        //字体大小
        private int fontSize;
        //字体颜色
        private String color;
        //文字字体间隔
        private float letterSpacing;
        //文本内容
        private String text;
        //文本字体名称
        private String fontFamily;
        //图片是否是矢量的
        private boolean vector;
        //图片的素材id
        private String materialId;
        //图片的文件名称
        private String fontFile;
        //文字的方向 horizontal：横向  : vertical 纵向
        private String align;
        // 图片的滤镜类
        private Filter filter;
        //图片是否锁定
        private String locked;
        //字体id
        private String fontId;
        //版本
        private int version;

        public String getFontId() {
            return fontId;
        }

        public void setFontId(String fontId) {
            this.fontId = fontId;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }


        public float getResizeScale() {
            return resizeScale;
        }

        public void setResizeScale(float resizeScale) {
            this.resizeScale = resizeScale;
        }

        public float getDevScale() {
            return devScale;
        }

        public void setDevScale(float devScale) {
            this.devScale = devScale;
        }

        public boolean isVector() {
            return vector;
        }

        public void setVector(boolean vector) {
            this.vector = vector;
        }


        public String getMaterialId() {
            return materialId;
        }

        public void setMaterialId(String materialId) {
            this.materialId = materialId;
        }


        public String getFontFile() {
            return fontFile;
        }

        public void setFontFile(String fontFile) {
            this.fontFile = fontFile;
        }


        public Filter getFilter() {
            return filter;
        }

        public void setFilter(Filter filter) {
            this.filter = filter;
        }

        public String getLocked() {
            return locked;
        }

        public void setLocked(String locked) {
            this.locked = locked;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getRotation() {
            return rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public LocalBean getLocal() {
            return local;
        }

        public void setLocal(LocalBean local) {
            this.local = local;
        }

        public boolean isTransparent() {
            return transparent;
        }

        public void setTransparent(boolean transparent) {
            this.transparent = transparent;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public SourceRectBean getSourceRect() {
            return sourceRect;
        }

        public void setSourceRect(SourceRectBean sourceRect) {
            this.sourceRect = sourceRect;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public float getLetterSpacing() {
            return letterSpacing;
        }

        public void setLetterSpacing(float letterSpacing) {
            this.letterSpacing = letterSpacing;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        /**
         * 位置信息
         */
        public static class LocalBean implements Serializable {

            //左上角坐标
            private float x;
            private float y;
            //控件的宽高
            private float width;
            private float height;

            private String defaultWidth;
            private String defaultHeight;

            public String getDefaultWidth() {
                return defaultWidth;
            }

            public void setDefaultWidth(String defaultWidth) {
                this.defaultWidth = defaultWidth;
            }

            public String getDefaultHeight() {
                return defaultHeight;
            }

            public void setDefaultHeight(String defaultHeight) {
                this.defaultHeight = defaultHeight;
            }

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }

            public float getWidth() {
                return width;
            }

            public void setWidth(float width) {
                this.width = width;
            }

            public float getHeight() {
                return height;
            }

            public void setHeight(float height) {
                this.height = height;
            }
        }

        /**
         *  图片的裁剪数据
         */
        public static class SourceRectBean implements Serializable {
            private int x;
            private int y;
            private int width;
            private int height;

            public SourceRectBean(int x, int y, int width, int height) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }

        /**
         * 图片的滤镜数据
         */
        public static class Filter implements Serializable {
            public Filter(String type, String name, String image) {
                this.type = type;
                this.name = name;
                this.image = image;
            }

            //滤镜类型 普通滤镜 风格化
            private String type;
            //滤镜名称
            private String name;
            //滤镜过后的图片地址
            private String image;
            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

        }
    }
}
