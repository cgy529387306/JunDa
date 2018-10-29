package com.android.mb.zzha.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.mb.zzha.R;
import com.android.mb.zzha.constants.ProjectConstants;
import com.android.mb.zzha.utils.Helper;
import com.android.mb.zzha.utils.NavigationHelper;
import com.android.mb.zzha.utils.PreferencesHelper;

import cn.jpush.android.api.JPushInterface;


/**
 * 起始页
 *
 * @author @author chenqm on 2018/1/15.
 */

public class SplashActivity extends Activity{
    private static final int LOADING_TIME_OUT = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getRegisterId();
        new Handler().postDelayed(new Runnable() {

            public void run() {
                boolean isLogin = PreferencesHelper.getInstance().getBoolean(ProjectConstants.Preferences.KEY_IS_LOGIN);
                if (isLogin){
                    NavigationHelper.startActivity(SplashActivity.this, MainActivity.class, null, true);
                }else{
                    NavigationHelper.startActivity(SplashActivity.this, LoginActivity.class, null, true);
                }
            }
        }, LOADING_TIME_OUT);
    }

    private void getRegisterId(){
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (Helper.isNotEmpty(rid)) {
            PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_REGISTRATION_ID,rid);
        }
    }

}
