package com.zhaotai.uzao.bean.post;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description :
 */

public class RewardInfo implements Serializable {
    public List<String> materialIds;
    public String materialId;
    public String userId;
    public String tipOption;
    public String amount;
}
