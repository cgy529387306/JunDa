package com.android.mb.zzha.entity;

import com.zhouyou.http.model.ApiResult;

public class CustomApiResult<T> extends ApiResult<T>{
    private int status;
    private String msg;
    private long expires_in;
    private T token;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public T getToken() {
        return token;
    }

    public void setToken(T token) {
        this.token = token;
    }
}
