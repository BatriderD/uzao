package com.zhaotai.uzao.bsaerx;

/**
 * time:2017/4/7
 * description: 服务器请求异常--token过期
 * author: LiYou
 */

public class TokenException extends Exception {

    public TokenException(String msg) {
        super(msg);
    }
}
