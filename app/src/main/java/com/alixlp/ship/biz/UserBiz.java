package com.alixlp.ship.biz;

import com.alixlp.ship.bean.User;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {
    private static final String TAG = "UserBiz-app";

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param commonCallback
     */
    public void login(String username, String password, CommonCallback<User> commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        OkHttpUtils
                .post()
                .url(baseUrl + "/login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    /**
     * 退出登錄
     *
     * @param commonCallback
     */
    public void logout(CommonCallback<User> commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");

        OkHttpUtils
                .get()
                .url(baseUrl + "/login/logout")
                .tag(this)
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }
}
