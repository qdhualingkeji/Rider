package com.hualing.rider.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hualing.rider.R;
import com.hualing.rider.activities.MainActivity;
import com.hualing.rider.widget.pull2refresh.MyListener;
import com.hualing.rider.widget.pull2refresh.PullToRefreshLayout;
import com.hualing.rider.widget.pull2refresh.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    //要切换的View
    private View view1;
    private View view2;
    private View view3;
    //view集合
    private List<View> views;
    private PullableListView mListView1;
    PullToRefreshLayout mRefresher1;
    private DaiQiangDanAdapter mAdapter1;

    public MyPagerAdapter(MainActivity mainActivity){
        views = new ArrayList<>();
        view1 = View.inflate(mainActivity, R.layout.banner_layout_one_pager,null);
        mListView1 = view1.findViewById(R.id.listView);
        mRefresher1 = view1.findViewById(R.id.refresher);

        mRefresher1.setOnRefreshListener(new MyListener());
        mAdapter1 = new DaiQiangDanAdapter(mainActivity);
        mListView1.setAdapter(mAdapter1);
        views.add(view1);

        view2 = View.inflate(mainActivity,R.layout.banner_layout_two_pager,null);
        views.add(view2);

        view3 = View.inflate(mainActivity,R.layout.banner_layout_three_pager,null);
        views.add(view3);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
