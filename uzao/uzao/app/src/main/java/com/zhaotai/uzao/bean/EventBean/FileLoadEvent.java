package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2017/8/8
 * Created by LiYou
 * Description :
 */

public class FileLoadEvent {


    long total;
    long bytesLoaded;

    public long getBytesLoaded() {
        return bytesLoaded;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadEvent(long total, long bytesLoaded) {
        this.total = total;
        this.bytesLoaded = bytesLoaded;
    }

}
