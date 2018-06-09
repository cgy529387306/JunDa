package com.android.mb.junda.adapter;

import com.android.mb.junda.R;
import com.android.mb.junda.entity.Log2;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Qiujz on 2017/7/3.
 */

public class Log2Adapter extends BaseQuickAdapter<Log2, BaseViewHolder> {


    public Log2Adapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final Log2 item) {
        helper.setText(R.id.tv_user, item.getUsername());
        helper.setText(R.id.tv_time, item.getCrtdate());
    }
}
