package com.alixlp.ship.biz;

import com.alixlp.ship.bean.Agent;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class AgentBiz extends BaseBiz {

    public void agentInfo(String id, CommonCallback<Agent> commonCallback) {

        String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");
        OkHttpUtils
                .get()
                .url(this.BASE_API + "/user/agentInfo")
                .addParams("id", id + "")
                .addParams("token", token)
                .build()
                .execute(commonCallback);

    }
}
