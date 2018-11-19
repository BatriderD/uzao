package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * time:2017/4/7
 * description:
 * author: LiYou
 */

public class BaseResult<T> implements Serializable {
    private String code;
    private String message;
    private T result;
    private int status;
    private String traceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", status=" + status +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
