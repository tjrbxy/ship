package com.alixlp.ship.biz;

import android.util.Log;

import com.alixlp.ship.bean.Goods;
import com.alixlp.ship.bean.Order;
import com.alixlp.ship.constants.Constant;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

public class OrderBiz extends BaseBiz {

    private static final String TAG = "OrderBiz-app";
    private final String token = (String) SPUtils.getInstance().get(Constant.TOKEN, "");

    /**
     * 订单详情页
     *
     * @param oid
     * @param commonCallback
     */
    public void orderDetail(int oid, CommonCallback<Order> commonCallback) {

        Log.d(TAG, "orderDetail: " + this.BASE_API);
        String token = (String) SPUtils.getInstance().get(Constant.TOKEN, "");
        OkHttpUtils
                .get()
                .url(this.BASE_API + "/order/detail")
                .tag(this)
                .addParams("oid", oid + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);
    }

    /**
     * 订单列表-搜索
     *
     * @param parms
     * @param commonCallback
     */
    public void listByPage(Map parms, CommonCallback<List<Order>> commonCallback) {

        String token = (String) SPUtils.getInstance().get(Constant.TOKEN, "");
        Log.d(TAG, "listByPage-new: " + this.BASE_API + "/order/" + parms);
        parms.put("token", token);
        OkHttpUtils
                .get()
                .url(this.BASE_API + "/order")
                .tag(this)
                .params(parms)
                .build()
                .execute(commonCallback);
    }

    /**
     * @param oid
     * @param kid
     * @param code
     * @param commonCallback
     */
    public void express(int oid, int kid, String code, CommonCallback<List> commonCallback) {

        OkHttpUtils
                .post()
                .url(this.BASE_API + "/order/express")
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

        OkHttpUtils
                .post()
                .url(this.BASE_API + "/order/kd")
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

        Log.d(TAG, "scanGoods: " + this.BASE_API + "/order/scanGoods");
        OkHttpUtils
                .post()
                .url(this.BASE_API + "/order/scanGoods")
                .addParams("oid", oid + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);

    }


    /**
     * 主动发货扫入产品
     *
     * @param params
     * @param commonCallback
     */
    public void orderActiveScan(Map params, CommonCallback<List<Goods>> commonCallback) {

        params.put("token", SPUtils.getInstance().get(Constant.TOKEN, ""));
        OkHttpUtils
                .post()
                .url(this.BASE_API + "/goods/goodsCacheList")
                .params(params)
                .build()
                .execute(commonCallback);
    }

    /**
     * 确认无误发货
     *
     * @param params
     * @param commonCallback
     */
    public void orderActiveSend(Map params, CommonCallback<Order> commonCallback) {

        params.put("token", this.token);
        Log.d(TAG, "orderActiveSend: " + this.BASE_API + "/order/orderActiveSend");
        OkHttpUtils
                .post()
                .url(this.BASE_API + "/order/orderActiveSend")
                // .url(this.BASE_API + "/login")
                .params(params)
                .build()
                .execute(commonCallback);

    }
}
