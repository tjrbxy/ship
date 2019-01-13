package com.alixlp.ship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.alixlp.ship.R;
import com.alixlp.ship.activity.setting.SettingActivity;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.fragment.active.ActiveOrderFragment;
import com.alixlp.ship.fragment.setting.RefreshSettingFragment;
import com.alixlp.ship.fragment.order.ViewPagerOrderFragment;
import com.alixlp.ship.util.NetWorkUtils;
import com.alixlp.ship.util.SPUtils;
import com.alixlp.ship.util.StatusBarUtil;
import com.alixlp.ship.util.T;

/**
 * 首页
 */
public class IndexMainActivity extends AppCompatActivity implements
        OnNavigationItemSelectedListener {
    protected boolean isHideAppTitle = true;
    protected boolean isHideSysTitle = true;
    private static final String TAG = "IndexMainActivity-app";

    private enum TabFragment {
        order(R.id.navigation_order, ViewPagerOrderFragment.class),
        active(R.id.navigation_active, ActiveOrderFragment.class),
        setting(R.id.navigation_setting, RefreshSettingFragment.class);

        private Fragment fragment;
        private final int menuId;
        private final Class<? extends Fragment> clazz;

        TabFragment(@IdRes int menuId, Class<? extends Fragment> clazz) {
            this.menuId = menuId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.menuId == itemId) {
                    return fragment;
                }
            }
            return order;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题
        if (this.isHideAppTitle) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        // 全屏显示
        // 全屏
        if (this.isHideSysTitle) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        // 判断网络是否可用
        if (!NetWorkUtils.networkAvailable(this)) {
            T.showToast("网络不可用");
            return;
        }
// 存储设备信息
        String mImie = (String) SPUtils.getInstance().get("IMIE", "");
        if (mImie.length() == 0) {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            mImie = TelephonyMgr.getDeviceId();
            SPUtils.getInstance().put("IMIE", mImie);
        }

        toActivity();
        setContentView(R.layout.activity_index_main);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id
                .navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return TabFragment.values().length;
            }

            @Override
            public Fragment getItem(int position) {
                return TabFragment.values()[position].fragment();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(TabFragment.values()[position].menuId);
            }
        });

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this, 0xff000000, 0.1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ((ViewPager) findViewById(R.id.content)).setCurrentItem(TabFragment.from(item.getItemId()
        ).ordinal());
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .replace(R.id.content,TabFragment.from(item.getItemId()).fragment())
//                .commit();
        return true;
    }


    private void toActivity() {
        String apiUrl = (String) SPUtils.getInstance().get(Config.APIURL, "");
        String token = (String) SPUtils.getInstance().get(Config.TOKEN, "");
        Integer userId = (Integer) SPUtils.getInstance().get(Config.USERID, 0);

        if (TextUtils.isEmpty(apiUrl)) {
            Intent intent = new Intent(IndexMainActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (userId == 0 || token.length() < 5) {
            Intent intent = new Intent(IndexMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Log.d(TAG, "toActivity: end userid = " + userId + ",token =" + token);
    }
}
