package com.android.mb.zzha.entity;

/**
 * Created by Administrator on 2018\6\10 0010.
 */

public class Log1 {
    private String intval;
    private String source;
    private String createtime;

    public String getIntval() {
        return intval == null ? "" : intval;
    }

    public void setIntval(String intval) {
        this.intval = intval;
    }

    public String getSource() {
        return source == null ? "" : source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreatetime() {
        return createtime == null ? "" : createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
