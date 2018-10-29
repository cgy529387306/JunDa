package com.android.mb.zzha.adapter;

import com.android.mb.zzha.R;
import com.android.mb.zzha.entity.Log1;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Qiujz on 2017/7/3.
 */

public class Log1Adapter extends BaseQuickAdapter<Log1, BaseViewHolder> {


    public Log1Adapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final Log1 item) {
        helper.setText(R.id.tv_money, item.getIntval()+"元");
        helper.setText(R.id.tv_time, item.getCreatetime());
        helper.setText(R.id.tv_detail, item.getSource());
    }
}
