package com.android.mb.junda.constants;

/**
 * 常量配置
 *
 * @author cgy
 */
public class ProjectConstants {


    /**
     * Bundle参数（k-v）
     */
    public class BundleExtra {
        public static final String KEY_WEB_DETAIL_URL = "KEY_WEB_DETAIL_URL";
        public static final String KEY_WEB_DETAIL_TITLE = "KEY_WEB_DETAIL_TITLE";
        public static final String KEY_IS_SET_TITLE = "KEY_IS_SET_TITLE";
    }

    /**
     * Activity 请求码
     *
     * @author cgy
     */
    public class ActivityRequestCode {
        public static final int REQUEST_HOME_LOCATION = 1001;
        public static final int REQUEST_SELECT_ADDRESS = 1002;
        public static final int REQUEST_BIND_PHONE = 1003;
        public static final int REQUEST_CERTIFY_INFO = 1004;
        public static final int REQUEST_SELECT_TYPE = 1005;
    }


    public static class Url {
        /**
         * APP的接口地址
         */
        public static final String INDEX_URL = "https://www.zzha.vip";

        /**
         * 账号密码登录接口
         */
        public static final String USER_LOGIN = "/app/login";

        /**
         * 发送信息
         */
        public static final String USER_POST_INFO = "/app/logininfo";

        /**
         * 获取日志
         */
        public static final String GET_LOG_INFO = "/app/getlog";

        /**
         * 退出登录
         */
        public static final String GET_LOGOUT = "/app/logout";
    }

    /**
     * XML相关配置的键值
     *
     * @author cgy
     */
    public static final class Preferences {
        /**
         * 极光推送registrationid
         */
        public static final String KEY_REGISTRATION_ID = "KEY_REGISTRATION_ID";
        /**
         * TOKEN
         */
        public static final String KEY_CURRENT_TOKEN = "KEY_CURRENT_TOKEN";
        /**
         * isLogin
         */
        public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
        /**
         * username
         */
        public static final String KEY_USERNAME = "KEY_USERNAME";

    }

}
