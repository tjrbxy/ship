package com.alixlp.ship.activity.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.alixlp.ship.R;
import com.alixlp.ship.activity.BaseActivity;
import com.alixlp.ship.fragment.order.ViewPagerOrderFragment;

public class OrderActivity extends BaseActivity {
    @Override
    protected void onInitVariable() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_list);
        ViewPager viewPager = (ViewPager) findViewById(R.id.content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                return new ViewPagerOrderFragment();
            }
        });
        //状态栏透明和间距处理
        // StatusBarUtil.immersive(this, 0xff000000, 0.1f);
    }

    @Override
    protected void onRequestData() {

    }
}
