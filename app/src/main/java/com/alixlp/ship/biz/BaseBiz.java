package com.alixlp.ship.biz;

import android.util.Log;

import com.alixlp.ship.constants.Constant;
import com.alixlp.ship.util.SPUtils;

public class BaseBiz {
    private static final String TAG = "BaseBiz-app";
    protected String BASE_API = "https://" + SPUtils.getInstance().get(Constant.APIURL, "") +
            "/api.php";
    protected String token = null;

    public BaseBiz() {
        if (null == this.token) {
            String token = (String) SPUtils.getInstance().get(Constant.TOKEN, "");
            Log.d(TAG, "BaseBiz: " + token);
        }
    }
}
