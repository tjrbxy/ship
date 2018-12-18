package com.alixlp.ship.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {
    /**
     * 这个方法是判断网络状态是否可用的
     */
    public static boolean networkAvailable(Context context) {
        boolean bisConnFlag = false;
        //1.获取网络连接的管理对象
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //2.通过管理者对象拿到网络的信息
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            //3.网络状态是否可用的返回值
            bisConnFlag = network.isAvailable();
        }
        return bisConnFlag;
    }
}
