package com.alixlp.ship.fragment.order;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.alixlp.ship.R;
import com.alixlp.ship.activity.order.OrderDetailActivity;
import com.alixlp.ship.adapter.BaseRecyclerAdapter;
import com.alixlp.ship.adapter.SmartViewHolder;
import com.alixlp.ship.bean.Order;
import com.alixlp.ship.biz.OrderBiz;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.T;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alixlp.ship.R.id.refreshLayout;

/**
 * 使用示例-ViewPager页面
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerOrderFragment extends Fragment implements OnRefreshListener,
        OnRefreshLoadMoreListener {

    private static final String TAG = "ViewPagerOrderFragment-app";

    public enum Item {
        NestedShipped("待发货", SmartFragment.class),
        NestedWaiting("待录单", SmartFragment.class),
        NestedRecorded("已录单", SmartFragment.class),
        NestedDelay("已推迟", SmartFragment.class);
        public String name;
        public Class<? extends Fragment> clazz;

        Item(String name, Class<? extends Fragment> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }

    private static OrderBiz mOrderBiz = new OrderBiz();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RefreshLayout mRefreshLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    // 搜索
    private RadioGroup mRadioGroup; // 单选按钮
    private int mType = 1; // 时间范围类型 1近30天，2 全部时间
    private EditText mEditText; // 搜索框
    private ImageButton mImageButton; // 搜索 按钮


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        mRefreshLayout = (RefreshLayout) root.findViewById(refreshLayout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this); // 刷新和加载监听器
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        mViewPager = (ViewPager) root.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tableLayout);
        // viewPager 适配器
        mViewPagerAdapter = new ViewPagerAdapter(Item.values());
        mViewPager.setAdapter(mViewPagerAdapter);
        // mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // mRefreshLayout.autoRefresh();  // 选中自动刷新
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager, true);

        mRadioGroup = (RadioGroup) root.findViewById(R.id.RadioGroup);
        mEditText = (EditText) root.findViewById(R.id.search_txt);
        mImageButton = root.findViewById(R.id.searchBt);

        // 时间范围切换
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.RadioButton1) {
                    mType = 1;
                } else {
                    mType = 2;
                }
            }
        });
        // 搜索
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                if (text.equals("")) {
                    T.showToast("请输入搜索条件");
                }
                mViewPagerAdapter.fragments[mViewPager.getCurrentItem()]
                        .searchRefresh(mRefreshLayout, mType, text);

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
/*        if( mRefreshLayout != null)
            mRefreshLayout.autoRefresh(); // 自动刷新*/
    }

    // 下拉刷新代码
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

        mViewPagerAdapter.fragments[mViewPager.getCurrentItem()]
                .onRefresh(refreshLayout, mViewPager.getCurrentItem());

    }

    /**
     * 上拉加载
     *
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        mViewPagerAdapter.fragments[mViewPager.getCurrentItem()]
                .onLoadMore(refreshLayout, mViewPager.getCurrentItem());
    }

    /**
     * ViewPager 适配器
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final Item[] items;
        private final SmartFragment[] fragments;

        ViewPagerAdapter(Item... items) {
            super(getChildFragmentManager());
            this.items = items;
            this.fragments = new SmartFragment[items.length];
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items[position].name;
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                fragments[position] = new SmartFragment(position);
            }
            return fragments[position];
        }
    }

    public static class SmartFragment extends Fragment implements AdapterView.OnItemClickListener {

        private RecyclerView mRecyclerView;
        private BaseRecyclerAdapter<Order> mAdapter;
        private List<Order> mDatas = new ArrayList<>();
        private int currPage = 2;
        private int currentItem;

        public SmartFragment() {

        }

        @SuppressLint("ValidFragment")
        public SmartFragment(int currentItem) {
            this.currentItem = currentItem;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
                savedInstanceState) {
            return new RecyclerView(inflater.getContext());
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
            // 加载数据
            // if (this.currentItem == 0)
            loadData(this.currentItem);
            mAdapter = new BaseRecyclerAdapter<Order>(mDatas, R.layout.item_order_list, this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Order model, int position) {
                    holder.text(R.id.id_tv_order_sn, model.getOrderid());
                    holder.text(R.id.id_tv_name, model.getName());
                    holder.text(R.id.id_tv_tel, model.getTel());
                    holder.text(R.id.id_tv_address, model.getAddress());
                    String goods = "";
                    for (int index = 0; index < model.getGoods().size(); index++) {
                        goods += model.getGoods().get(index).getTitle() + " * " + model.getGoods
                                ().get
                                (index).getNum() + "\n";
                    }
                    holder.text(R.id.id_tv_goods, goods);
                    holder.textColorId(R.id.id_tv_name, R.color.colorTextName);
                }
            };
            mRecyclerView.setAdapter(mAdapter); // 设置适配器


        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("OID", mDatas.get(position).getId());
            bundle.putInt("ORDERSTATUS", this.currentItem);
            intent.putExtra("ORDER", bundle);
            startActivity(intent);

        }

        /**
         * 加载第一页数据
         *
         * @return
         */
        private List<Order> loadData(int currentItem) {

            Map parms = new HashMap();
            parms.put("currPage", 1 + "");
            parms.put("orderStatus", currentItem + "");
            mOrderBiz.listByPage(parms, new CommonCallback<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(List<Order> response, String info) {
                    if (response.size() == 0) {
                        T.showToast("无结果");
                        return;
                    }
                    mDatas.clear();
                    mDatas.addAll(response);
                    mAdapter.refresh(mDatas);
                }
            });

            /*mOrderBiz.listByPage(1, currentItem, new CommonCallback<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(List<Order> response, String info) {
                    if (response.size() == 0) {
                        T.showToast("无结果");
                        return;
                    }
                    mDatas.clear();
                    mDatas.addAll(response);
                    mAdapter.refresh(mDatas);
                }
            });*/
            return mDatas;
        }

        /**
         * 下拉刷新
         *
         * @param refreshLayout
         */
        public void onRefresh(final RefreshLayout refreshLayout, int currentItem) {

            Map parms = new HashMap();
            parms.put("currPage", 1 + "");
            parms.put("orderStatus", currentItem + "");
            mOrderBiz.listByPage(parms, new CommonCallback<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(List<Order> response, String info) {
                    mDatas.clear();
                    if (response.size() == 0) {
                        T.showToast("无结果");
                    } else {
                        mDatas.addAll(response);
                        mAdapter.refresh(mDatas);
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);
                    }
                }
            });

        }

        /**
         * 上啦加载
         *
         * @param refreshLayout
         */
        public void onLoadMore(final RefreshLayout refreshLayout, int currentItem) {
            Map parms = new HashMap();
            parms.put("currPage", currPage++ + "");
            parms.put("orderStatus", currentItem + "");
            mOrderBiz.listByPage(parms, new CommonCallback<List<Order>>() {
                // mOrderBiz.listByPage(currPage++, currentItem, new CommonCallback<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    --currPage;
                    T.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(List<Order> response, String info) {
                    if (response.size() == 0) {
                        T.showToast("数据全部加载完毕");
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    } else {
                        mDatas.addAll(response);
                        mAdapter.loadMore(mDatas);
                        refreshLayout.finishLoadMore();
                    }
                }
            });
        }


        /**
         * 搜索
         *
         * @param refreshLayout
         * @param mType
         * @param text
         */
        public void searchRefresh(final RefreshLayout refreshLayout, int mType, String text) {
            Map parms = new HashMap();
            parms.put("currPage", 1 + "");
            parms.put("orderStatus", currentItem + "");
            parms.put("type", mType + "");
            parms.put("keywords", text);
            mOrderBiz.listByPage(parms, new CommonCallback<List<Order>>() {
                //  mOrderBiz.listByPage(1, this.currentItem, mType, text, new CommonCallback<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(List<Order> response, String info) {
                    if (response.size() == 0) {
                        T.showToast("无结果");
                    }
                    mDatas.clear();
                    mDatas.addAll(response);
                    mAdapter.refresh(mDatas);
                    refreshLayout.finishRefresh();
                    refreshLayout.setNoMoreData(false);
                }
            });
        }
    }
}
