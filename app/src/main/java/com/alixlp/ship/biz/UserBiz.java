package com.alixlp.ship.biz;

import com.alixlp.ship.bean.User;
import com.alixlp.ship.constants.Constant;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class UserBiz extends BaseBiz {
    private static final String TAG = "UserBiz-app";

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param commonCallback
     */
    public void login(String username, String password, CommonCallback<User> commonCallback) {

        OkHttpUtils
                .post()
                .url(this.BASE_API + "/login")
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
    public void logout(CommonCallback<List> commonCallback) {

        String token = (String) SPUtils.getInstance().get(Constant.TOKEN, "");

        OkHttpUtils
                .get()
                .url(this.BASE_API + "/login/logout")
                .tag(this)
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }
}
