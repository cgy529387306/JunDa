package com.android.mb.junda.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.mb.junda.constants.ProjectConstants;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;

import cn.jpush.android.api.JPushInterface;

/**
 * @author chenqm on 2017/8/9.
 */

public class MBApplication extends Application{
    private static final String TAG = MBApplication.class.getSimpleName();

    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
        initJPush();
        initEasyHttp();
    }

    private void initEasyHttp(){
        //https://github.com/cgy529387306/RxEasyHttp
        EasyHttp.init(this);//默认初始化,必须调用
        EasyHttp.getInstance()
                //可以全局统一设置全局URL
                .setBaseUrl(ProjectConstants.Url.INDEX_URL)//设置全局URL  url只能是域名 或者域名+端口号
                // 打开该调试开关并设置TAG,不需要就不要加入该行
                .debug("EasyHttp", true)
                //如果使用默认的60秒,以下三行也不需要设置
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 100)
                .setConnectTimeout(60 * 100)
                .setRetryCount(3)//网络不好自动重试3次
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(100 * 1024 * 1024)//设置缓存大小为100M
                .setCacheVersion(1)//缓存版本为1
                .setCertificates();//添加参数签名拦截器
    }

    /**
     * 获取全局Application对象
     */
    public static Context getInstance(){
        if (sInstance == null) {
            Log.e(TAG, "THE APPLICATION OF YOUR PROJECT MUST BE 'MBApplication', OR SOMEONE EXTEND FROM IT");
        }
        return sInstance;
    }

    /**
     * 初始化
     * <p>若未配置manifest可使用此方法初始化</p>
     * @param application 全局context
     */
    public static void init(Context application) {
        sInstance = application;
    }

    private void initJPush(){
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }

}
