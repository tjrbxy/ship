package com.alixlp.ship;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.alixlp.ship.util.DynamicTimeFormat;
import com.alixlp.ship.util.SPUtils;
import com.alixlp.ship.util.T;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;

/**
 * Created by SCWANG on 2017/6/11.
 */

public class App extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull
                    RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 内存泄漏检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        // 提示信息
        T.init(this);
        // 本地存储
        SPUtils.init(this, "setting.ay");


    }

    private static LinkedList<Activity> actList = new LinkedList<Activity>();

    public LinkedList<Activity> getActivityList() {
        return actList;
    }

    /**
     * 添加
     *
     * @param act
     */
    public static void addToActivityList(final Activity act) {
        if (act != null) {
            actList.add(act);
        }
    }

    /**
     * 删除
     *
     * @param act
     */
    public static void removeFromActivityList(final Activity act) {
        if (actList != null && actList.size() > 0 && actList.indexOf(act) != -1) {
            actList.remove(act);
        }
    }

    /**
     * 清理activity
     */
    public static void clearActivityList() {
        for (int i = actList.size() - 1; i >= 0; i--) {
            final Activity act = actList.get(i);
            if (act != null) {
                act.finish();
            }
        }
    }

    /**
     * 退出应用
     */
    public static void exitApp() {
        try {
            clearActivityList();
        } catch (final Exception e) {

        } finally {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
