package com.alixlp.ship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alixlp.ship.R;
import com.alixlp.ship.bean.User;
import com.alixlp.ship.biz.UserBiz;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.alixlp.ship.util.T;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity-app";

    protected Button mBtLogin;
    protected EditText mUserName;
    protected EditText mPassWord;

    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onInitVariable() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        mUserName = (EditText) findViewById(R.id.username);
        mPassWord = (EditText) findViewById(R.id.password);
        mBtLogin = (Button) findViewById(R.id.bt_login);

        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserName.getText().toString();
                String password = mPassWord.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("用户名或者密码不能为空");
                    return;
                }
                startLoadingProgress();
                // 登陆逻辑
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response, String info) {
                        Log.d(TAG, "onSuccess: " + response);
                        stopLoadingProgress();
                        if (!info.equals("ok")) {
                            T.showToast(info);
                        }
                        // 保存用户信息
                        SPUtils.getInstance().put(Config.TOKEN, response.getToken()); // 記錄Token
                        SPUtils.getInstance().put(Config.USERID, response.getId());  // 記錄用戶id
                        SPUtils.getInstance().put(Config.KUAIDIID, response.getKuaidiid()); //
                        // 記錄綁定快遞ID
                        // 跳转
                        Intent intent = new Intent(LoginActivity.this, IndexMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


            }


        });
    }

    @Override
    protected void onRequestData() {

    }
}
