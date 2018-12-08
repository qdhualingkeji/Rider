package com.hualing.rider.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hualing.rider.R;
import com.hualing.rider.global.TheApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.dot1)
    Button mDot1;
    @BindView(R.id.dot2)
    Button mDot2;
    @BindView(R.id.dot3)
    Button mDot3;
    private MyPagerAdapter mPagerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        getScreenSize();

        mToolBar.setTitle(getResources().getString(R.string.app_name));//设置Toolbar标题
        //        mToolBar.setTitle("二维码追溯-员工模式");//设置Toolbar标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(mToolBar);
        //        mToolBar.setOnMenuItemClickListener(onMenuItemClick);
        //getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int width = (int) (TheApplication.getScreenWidth()/3);
        int height=100;
        mDot1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot3.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mDot1.setSelected(true);
    }

    @OnClick({R.id.dot1,R.id.dot2,R.id.dot3})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.dot1:
                changeDot(0);
                break;
            case R.id.dot2:
                changeDot(1);
                break;
            case R.id.dot3:
                changeDot(2);
                break;
        }
    }

    /**
     * 改变选卡
     * */
    private void changeDot(int position){
        //首先全部置为未选中
        mDot1.setSelected(false);
        mDot2.setSelected(false);
        mDot3.setSelected(false);
        //其次单独设置选中的
        switch (position) {
            case 0:
                Log.e("position========",""+position);
                mDot1.setSelected(true);
                break;
            case 1:
                mDot2.setSelected(true);
                break;
            case 2:
                mDot3.setSelected(true);
                break;
        }
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        TheApplication.setScreenHeight(display.getHeight());
        TheApplication.setScreenWidth(display.getWidth());
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private class MyPagerAdapter extends PagerAdapter{

        //要切换的View
        private View view1;
        private View view2;
        private View view3;
        //view集合
        private List<View> views;

        public MyPagerAdapter(){
            views = new ArrayList<>();
            view1 = View.inflate(MainActivity.this,R.layout.banner_layout_one_pager,null);
            views.add(view1);

            view2 = View.inflate(MainActivity.this,R.layout.banner_layout_two_pager,null);
            views.add(view2);

            view3 = View.inflate(MainActivity.this,R.layout.banner_layout_three_pager,null);
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
}
