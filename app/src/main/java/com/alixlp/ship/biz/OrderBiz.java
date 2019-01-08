package com.alixlp.ship.biz;

import android.util.Log;

import com.alixlp.ship.bean.Goods;
import com.alixlp.ship.bean.Order;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class OrderBiz {

    private static final String TAG = "OrderBiz-app";
    private final String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");

    /**
     * 订单详情页
     *
     * @param oid
     * @param commonCallback
     */
    public void orderDetail(int oid, CommonCallback<Order> commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        Log.d(TAG, "orderDetail: " + baseUrl);
        String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");
        OkHttpUtils
                .get()
                .url(baseUrl + "/order/detail")
                .tag(this)
                .addParams("oid", oid + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }

    /**
     * 列表
     *
     * @param currPage
     * @param orderStatus
     * @param commonCallback
     */
    public void listByPage(int currPage, int orderStatus, CommonCallback<List<Order>>
            commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        OkHttpUtils
                .get()
                .url(baseUrl + "/order")
                .tag(this)
                .addParams("currPage", currPage + "")
                .addParams("orderStatus", orderStatus + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }

    /**
     * 搜索
     *
     * @param currPage
     * @param orderStatus
     * @param commonCallback
     */
    public void listByPage(int currPage, int orderStatus, int mType, String text,
                           CommonCallback<List<Order>>
                                   commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        OkHttpUtils
                .get()
                .url(baseUrl + "/order")
                .tag(this)
                .addParams("currPage", currPage + "")
                .addParams("type", mType + "")
                .addParams("keywords", text)
                .addParams("orderStatus", orderStatus + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }


    public void express(int oid, int kid, String code, CommonCallback<List> commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        OkHttpUtils
                .post()
                .url(baseUrl + "/order/express")
                .addParams("oid", oid + "")
                .addParams("kid", kid + "")
                .addParams("code", code)
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }

    /**
     * 扫码发货
     *
     * @param oid
     * @param code
     * @param commonCallback
     */
    public void kd(int oid, String code, CommonCallback<List<Goods>> commonCallback) {
        Log.d(TAG, "kd: oid=" + oid + " ,code = " + code);
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        OkHttpUtils
                .post()
                .url(baseUrl + "/order/kd")
                .addParams("oid", oid + "")
                .addParams("code", code)
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }


    /**
     * 查询扫入的产品信息
     *
     * @param oid
     * @param commonCallback
     */
    public void scanGoods(int oid, CommonCallback commonCallback) {
        String baseUrl = "http://" + SPUtils.getInstance().get(Config.APIURL, "") +
                "/api.php";
        Log.d(TAG, "scanGoods: " + baseUrl + "/order/scanGoods");
        OkHttpUtils
                .post()
                .url(baseUrl + "/order/scanGoods")
                .addParams("oid", oid + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);

    }
}
