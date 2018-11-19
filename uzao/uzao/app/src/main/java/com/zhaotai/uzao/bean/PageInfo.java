package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public class PageInfo<T> {

    public int currentPage;
    public boolean hasNextPage;
    public List<T> list;
    public int pageRecorders;   //每次加载多少条数据
    public int pageStartRow;    //开始的位置
    public int totalPages;
    public int totalRows;
}
