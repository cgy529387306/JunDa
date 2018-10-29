package com.android.mb.junda.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.android.mb.junda.R;
import com.android.mb.junda.adapter.MyFragmentPagerAdapter;
import com.android.mb.junda.constants.ProjectConstants;
import com.android.mb.junda.entity.CustomApiResult;
import com.android.mb.junda.entity.LoginResp;
import com.android.mb.junda.fragment.Log1Fragment;
import com.android.mb.junda.fragment.Log2Fragment;
import com.android.mb.junda.tts.TtsManager;
import com.android.mb.junda.utils.ActivityManager;
import com.android.mb.junda.utils.DialogHelper;
import com.android.mb.junda.utils.Helper;
import com.android.mb.junda.utils.JsonHelper;
import com.android.mb.junda.utils.KeepLiveManager;
import com.android.mb.junda.utils.LogHelper;
import com.android.mb.junda.utils.NavigationHelper;
import com.android.mb.junda.utils.PhoneInfo;
import com.android.mb.junda.utils.PreferencesHelper;
import com.android.mb.junda.utils.ProgressDialogHelper;
import com.android.mb.junda.utils.ToastHelper;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private MyFragmentPagerAdapter fragmentAdapter;
    private TextView tvLog1,tvLog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeepLiveManager.getInstance().registerKeepLifeReceiver(this);
        hideActionbar();
        initView();
        setListener();
        doPostLoginInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepLiveManager.getInstance().unregisterKeepLiveReceiver(this);
    }

    private void initView(){
        tvLog1 = findViewById(R.id.tv_log1);
        tvLog2 = findViewById(R.id.tv_log2);
        viewPager = findViewById(R.id.myViewPager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Log1Fragment());
        fragmentList.add(new Log2Fragment());
        fragmentAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentAdapter);
    }

    private void setListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                showIndex(position);
            }


            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tvLog1.setOnClickListener(this);
        tvLog2.setOnClickListener(this);
        findViewById(R.id.tv_action).setOnClickListener(this);
        findViewById(R.id.btnTest).setOnClickListener(this);
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
                        doLogout();
                    }

                }, R.string.dialog_negative, null);
    }
    private void doPostLoginInfo(){
        String userName = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_USERNAME);
        String token = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN);
        String rid = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_REGISTRATION_ID);
        if (Helper.isEmpty(rid)){
            rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        EasyHttp.post(ProjectConstants.Url.USER_POST_INFO)
                .params("username",userName)
                .params("token",token)
                .params("resid",rid)
                .params("machineName", PhoneInfo.getPhoneModel())
                .params("machineType","android")
                .params("systemVersion",PhoneInfo.getDeviceAndroidVersion())
                .execute(new CallBackProxy<CustomApiResult<String>, String>(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (Helper.isNotEmpty(e.getMessage())){
                            ToastHelper.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String response) {
                        LoginResp loginResp = JsonHelper.fromJson(response,LoginResp.class);
                        if (loginResp!=null){
                            if (loginResp.getStatus() == 0){
                                LogHelper.e("报道成功");
                            }else{
                                LogHelper.e(loginResp.getMsg());
                            }
                        }
                    }
                }) {
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_action){
            loginOut();
        }else if (id == R.id.btnTest){
            TtsManager.getInstance(MainActivity.this).playText(getString(R.string.text_tts_source));
        }else if (id == R.id.tv_log1){
            showIndex(0);
            viewPager.setCurrentItem(0);
        }else if (id == R.id.tv_log2){
            showIndex(1);
            viewPager.setCurrentItem(1);
        }
    }

    private void showIndex(int position){
        tvLog1.setTextColor(position==0?getResources().getColor(R.color.base_blue_light):getResources().getColor(R.color.text_color));
        tvLog2.setTextColor(position==1?getResources().getColor(R.color.base_blue_light):getResources().getColor(R.color.text_color));
    }

    private void doLogout(){
        ProgressDialogHelper.showProgressDialog(this,"注销中...");
        String userName = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_USERNAME);
        String rid = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_REGISTRATION_ID);
        if (Helper.isEmpty(rid)){
            rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        EasyHttp.post(ProjectConstants.Url.GET_LOGOUT)
                .params("username",userName)
                .params("resid",rid)
                .execute(new CallBackProxy<CustomApiResult<String>, String>(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ProgressDialogHelper.dismissProgressDialog();
                        if (Helper.isNotEmpty(e.getMessage())){
                            ToastHelper.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String response) {
                        ProgressDialogHelper.dismissProgressDialog();
                        LoginResp loginResp = JsonHelper.fromJson(response,LoginResp.class);
                        if (loginResp!=null){
                            if (loginResp.getStatus() == 0){
                                PreferencesHelper.getInstance().putBoolean(ProjectConstants.Preferences.KEY_IS_LOGIN,false);
                                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN,"");
                                NavigationHelper.startActivity(MainActivity.this,LoginActivity.class,null,true);
                            }else{
                                LogHelper.e(loginResp.getMsg());
                            }
                        }
                    }
                }) {
                });
    }
}
