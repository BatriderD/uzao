package com.zhaotai.uzao.ui.design.bean;

import com.zhaotai.uzao.bean.LayerMetaJsonBean;

import java.util.List;

/**
 * Time: 2017/12/21
 * Created by LiYou
 * Description :
 */

public class DesignMetaBean {

    public String material ;
    public String aspectId ;//面的名字
    public List<LayerMetaJsonBean.LayerMetaBean> layerMeta ;
    public String thmbnailPath;
    public String thumbnail;
    public String craftId;
    public List<String> materialIds;
    public boolean uvImage = false;

}
