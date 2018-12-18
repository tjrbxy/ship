package com.alixlp.ship.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected boolean isHideAppTitle = true;
    protected boolean isHideSysTitle = false;

    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.onInitVariable(); //初始化变量
        // 隐藏标题
        if (this.isHideAppTitle) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        // 全屏
        if (this.isHideSysTitle) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage("加载中...");
        // 构建View,绑定事件
        this.onInitView(savedInstanceState);
        // 请求数据
        this.onRequestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        mLoadingDialog = null;
    }


    protected void startLoadingProgress() {
        mLoadingDialog.show();
    }

    protected void stopLoadingProgress() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 参数
     */
    protected abstract void onInitVariable();

    /**
     * 出事化UI,布局载入操作
     *
     * @param savedInstanceState
     */
    protected abstract void onInitView(final Bundle savedInstanceState);

    protected abstract void onRequestData();
}
