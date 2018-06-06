package com.android.mb.junda.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mb.junda.R;
import com.android.mb.junda.utils.ActivityManager;
import com.android.mb.junda.utils.Utils;


/**
 * Created by chenqm on 17/7/18.
 */
public class BaseActivity extends AppCompatActivity {

    private LinearLayout linActionbarBack,llActionbar;
    private TextView txvActionbarTitle;
    private ImageView txvActionbarAction;
    private RelativeLayout rlyRoot;
    private  TextView tv_name;
    private  ImageView img_head;
    private TextView tv_show;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.common_actionbar_back);
        ActivityManager.getInstance().putActivity(getClass().getName(), this);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.StatusBarIconManager.MIUI(this, Utils.StatusBarIconManager.TYPE.BLACK);
            Utils.StatusBarIconManager.Flyme(this, Utils.StatusBarIconManager.TYPE.BLACK);
        }
    }

    private void initView() {
        rlyRoot = (RelativeLayout) findViewById(R.id.rly_root);
        llActionbar = (LinearLayout) findViewById(R.id.ll_actionbar);
        linActionbarBack = (LinearLayout) findViewById(R.id.lin_actionbar_back);
        txvActionbarTitle = (TextView) findViewById(R.id.txv_actionbar_title);
        txvActionbarAction = (ImageView) findViewById(R.id.imv_actionbar_action);
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_show = (TextView) findViewById(R.id.tv_show);
        linActionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightBtnClick(v);
            }
        });
    }

    protected void onRightBtnClick(View v){

    }


    public void applyBackground() {
        // TODO Auto-generated method stub
        getWindow().getDecorView().getRootView().setBackgroundColor(getResources().getColor(R.color.base_background_color));
    }

    /**
     * 重点是重写setContentView，让继承者可以继续设置setContentView
     * 重写setContentView
     *
     * @param resId
     */
    @Override
    public void setContentView(int resId) {
        applyBackground();
        View view = getLayoutInflater().inflate(resId, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.ll_actionbar);
        if (null != rlyRoot)
            rlyRoot.addView(view, lp);
    }

    /**
     * 设置右边文字
     *
     * @param c
     */
    public void setRightText(CharSequence c) {
        if (tv_show != null)
            tv_show.setVisibility(View.VISIBLE);
            tv_show.setText(c);
    }

    /**
     * 设置右边文字
     *
     * @param resId
     */
    public void setRightText(int resId) {
        if (tv_show != null)
            tv_show.setVisibility(View.VISIBLE);
            tv_show.setText(resId);
    }

    /**
     * 设置中间标题文字
     *
     * @param c
     */
    public void setTitleText(CharSequence c) {
        if (txvActionbarTitle != null)
            txvActionbarTitle.setText(c);
    }

    /**
     * 设置中间标题文字
     *
     * @param resId
     */
    public void setTitleText(int resId) {
        if (txvActionbarTitle != null)
            txvActionbarTitle.setText(resId);
    }

    /**
     * 设置名字
     *
     * @param c
     */
    public void setNameText(CharSequence c) {
        if (tv_name != null){
                tv_name.setVisibility(View.VISIBLE);
                tv_name.setText(c);
            }
    }

    /**
     * 设置名字
     *
     * @param c
     */
    public void setNameAndNoClickText(CharSequence c) {
        if (tv_name != null) {
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(c);
            tv_name.setEnabled(false);
            linActionbarBack.setEnabled(false);
        }
    }


    /**
     * 设置有按钮
     */
    public void setImageRightButton(int resId, View.OnClickListener listener){
        txvActionbarAction.setVisibility(View.VISIBLE);
        txvActionbarAction.setImageResource(resId);
        txvActionbarAction.setOnClickListener(listener);
    }

    public void hideRightButton(){
        txvActionbarAction.setVisibility(View.GONE);
    }

    /**
     * 设置有按钮
     */
    public void setImageLeftButton(int resId, View.OnClickListener listener){
        linActionbarBack.setVisibility(View.VISIBLE);
        img_head.setImageResource(resId);
        linActionbarBack.setOnClickListener(listener);
    }

    /**
     * 返回箭头有按钮
     */
    public void setBackButton(int resId){
        img_head.setVisibility(View.VISIBLE);
        img_head.setImageResource(resId);
    }


    /**
     * @ author:gy 2014年8月7日 下午2:53:01
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 隐藏返回按钮
     */
    public void hideBack(){
        linActionbarBack.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示返回按钮
     */
    public void showBack(){
        linActionbarBack.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏标题栏
     */
    public void hideActionbar(){
        llActionbar.setVisibility(View.GONE);
    }

    /**
     * 隐藏标题栏
     */
    public void showActionbar(){
        llActionbar.setVisibility(View.VISIBLE);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

}

