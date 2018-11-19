package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/9/19
 * Created by LiYou
 * Description : 3d 模型信息
 */

public class ThreeDimensionalBean {
    public String bin;
    public String json;
    public String binName;
    public String jsonName;
    public String sequenceNBR;

    public List<Sample3dObjectModel> sample3dObjectModels;

    public static class Sample3dObjectModel implements Serializable {
        public String name;
        public String alias;
        public String selectable;
        public String canChangeTexture;
        public String canCustomize;
        public Option sample3dOptionModel;
        public String sequenceNBR;
        public boolean isSelected;
        public String craftId;
        public List<TechnologyBean> craftModels;
    }

    public static class Option implements Serializable {
        public String images;
        public String objectName;
        public String returnCoefficient;
        public String coefficient;
        public String optionMeta;
        public String uvFace;
    }

}
