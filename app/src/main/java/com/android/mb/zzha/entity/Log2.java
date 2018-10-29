package com.android.mb.zzha.entity;

/**
 * Created by Administrator on 2018\6\10 0010.
 */

public class Log2 {
    private String username;
    private String crtdate;

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCrtdate() {
        return crtdate == null ? "" : crtdate;
    }

    public void setCrtdate(String crtdate) {
        this.crtdate = crtdate;
    }
}
