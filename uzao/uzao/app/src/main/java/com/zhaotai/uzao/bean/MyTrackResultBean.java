package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2017/12/2 0002.
 */

public class MyTrackResultBean {


    /**
     * date : 2017-12-02
     * footprintModels : [{"changedFields":"","description":"","extend1":"","extend2":"","extend3":"","price":2,"priceY":"0.02","recDate":"1512201633000","recStatus":"A","recUserId":"910338411292893184","sequenceNBR":"936867565903306752","spuId":"911855899797139456","spuName":"熊本熊三联抱枕","thumbnail":"911855744402411520.png","userId":"910338411292893184"}]
     */

    private String date;
    private List<MyTrackBean.FootprintModelsBean> footprintModels;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MyTrackBean.FootprintModelsBean> getFootprintModels() {
        return footprintModels;
    }

    public void setFootprintModels(List<MyTrackBean.FootprintModelsBean> footprintModels) {
        this.footprintModels = footprintModels;
    }


}
