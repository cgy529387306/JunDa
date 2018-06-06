package com.android.mb.junda.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.mb.junda.R;
import com.android.mb.junda.constants.ProjectConstants;
import com.android.mb.junda.tts.SystemTTS;
import com.android.mb.junda.utils.ActivityManager;
import com.android.mb.junda.utils.DialogHelper;
import com.android.mb.junda.utils.NavigationHelper;
import com.android.mb.junda.utils.PreferencesHelper;
import com.android.mb.junda.utils.ToastHelper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        hideBack();
        setTitleText(R.string.app_name);
        setRightText("退出");
        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemTTS systemTTS = SystemTTS.getInstance(getApplicationContext());
                systemTTS.playText("你好,我是小白");
            }
        });
    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        loginOut();
    }


    private static final long DOUBLE_CLICK_INTERVAL = 2000;
    private long mLastClickTimeMills = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastClickTimeMills > DOUBLE_CLICK_INTERVAL) {
            ToastHelper.showToast("再按一次返回退出");
            mLastClickTimeMills = System.currentTimeMillis();
            return;
        }
        finish();
    }

    /**
     * 退出登录
     */
    private void loginOut(){
        DialogHelper.showConfirmDialog(MainActivity.this, "注销", "确定要退出当前账号？", true,
                R.string.dialog_positive, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesHelper.getInstance().putBoolean(ProjectConstants.Preferences.KEY_IS_LOGIN,false);
                        PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN,"");
                        NavigationHelper.startActivity(MainActivity.this,LoginActivity.class,null,true);
                        ActivityManager.getInstance().closeAllActivityExceptOne(LoginActivity.class.getName());
                    }

                }, R.string.dialog_negative, null);
    }


}
