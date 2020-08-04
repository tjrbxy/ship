package com.alixlp.ship.biz;

import android.util.Log;

import com.alixlp.ship.config.Config;
import com.alixlp.ship.util.SPUtils;

public class BaseBiz {
    private static final String TAG = "BaseBiz-app";
    protected String BASE_API = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
            "/api.php";
    protected String token = null;

    public BaseBiz() {
        if (null == this.token) {
            String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");
            Log.d(TAG, "BaseBiz: " + token);
        }
    }
}
