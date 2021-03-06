package com.android.mb.zzha.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mb.zzha.R;
import com.android.mb.zzha.constants.ProjectConstants;
import com.android.mb.zzha.entity.CustomApiResult;
import com.android.mb.zzha.entity.LoginResp;
import com.android.mb.zzha.utils.Helper;
import com.android.mb.zzha.utils.JsonHelper;
import com.android.mb.zzha.utils.NavigationHelper;
import com.android.mb.zzha.utils.PreferencesHelper;
import com.android.mb.zzha.utils.ToastHelper;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;


/**
 * 登录
 *
 * @author chenqm on 2018/1/15.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView btn_login;  // 登录
    private EditText edit_phone; //手机号码
    private EditText edit_pwd; //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initOnClickListener();
    }

    private void initView() {
        hideBack();
        setTitleText("登录");
        btn_login = (TextView) findViewById(R.id.btn_login);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
    }

    private void initOnClickListener() {
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {//登录
            doLogin();
        }
    }

    private void doLogin() {
        final String mobile = edit_phone.getText().toString().trim();
        String password = edit_pwd.getText().toString().trim();
        if (Helper.isEmpty(mobile)) {
            ToastHelper.showToast("请输入账号");
            return;
        }else if (Helper.isEmpty(password)){
            ToastHelper.showToast("请输入密码");
            return;
        }
        EasyHttp.post(ProjectConstants.Url.USER_LOGIN)
                .params("username",mobile)
                .params("password",password)
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
                                ToastHelper.showToast("登录成功");
                                PreferencesHelper.getInstance().putBoolean(ProjectConstants.Preferences.KEY_IS_LOGIN,true);
                                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN,loginResp.getToken());
                                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_USERNAME,mobile);
                                NavigationHelper.startActivity(LoginActivity.this,MainActivity.class,null,true);
                            }else{
                                ToastHelper.showToast(loginResp.getMsg());
                            }
                        }
                    }
                }) {
                });
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


}
