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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hualing.rider.R;
import com.hualing.rider.adapter.MyPagerAdapter;
import com.hualing.rider.entity.DaiQiangDanEntity;
import com.hualing.rider.global.TheApplication;
import com.hualing.rider.model.DaiQiangDanNode;
import com.hualing.rider.overlayutil.MyDrivingRouteOverlay;
import com.hualing.rider.widget.pull2refresh.MyListener;
import com.hualing.rider.widget.pull2refresh.PullToRefreshLayout;
import com.hualing.rider.widget.pull2refresh.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.stateSpinner)
    Spinner mStateSpinner;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.dot1)
    Button mDot1;
    @BindView(R.id.dot2)
    Button mDot2;
    @BindView(R.id.dot3)
    Button mDot3;
    private List<String> stateList;
    private MyPagerAdapter mPagerAdapter;
    private ArrayAdapter<String> stateAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    // 搜索相关
    RoutePlanSearch mSearch = null;
    private String loaclcity = null;
    private List<DaiQiangDanNode> dqdNodeList;
    private int dqdPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

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

        //设置导航Icon，必须在setSupportActionBar(toolbar)之后设置
        mToolBar.setNavigationIcon(R.drawable.p003);

        stateList = new ArrayList<String>();
        stateList.add("接单中");
        stateAdapter=new ArrayAdapter<String>(this,R.layout.item_state,stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateSpinner.setAdapter(stateAdapter);

        dqdNodeList = new ArrayList<DaiQiangDanNode>();
        mPagerAdapter = new MyPagerAdapter(MainActivity.this);
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

    /**
     * 初始化待抢单地点
     */
    public void initDaiQiangDanNode(List<DaiQiangDanEntity.DataBean> dqdList){
        DaiQiangDanNode qcNode = null;
        DaiQiangDanNode scNode = null;
        int dqdListSize = dqdList.size();
        for (int i = 0; i<dqdListSize; i++) {
            DaiQiangDanEntity.DataBean dataBean = dqdList.get(i);
            qcNode = new DaiQiangDanNode();
            qcNode.setQcStNode(PlanNode.withCityNameAndPlaceName(loaclcity, "青岛尼莫"));
            qcNode.setQcEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, "青岛颐和国际"));
            qcNode.setOrderNumber(dataBean.getOrderNumber());
            dqdNodeList.add(qcNode);

            scNode = new DaiQiangDanNode();
            scNode.setScStNode(PlanNode.withCityNameAndPlaceName(loaclcity, "青岛颐和国际"));
            scNode.setScEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, "青岛远雄国际广场"));
            qcNode.setOrderNumber(dataBean.getOrderNumber());
            dqdNodeList.add(scNode);
        }
    }

    public List<DaiQiangDanNode> getDqdNodeList(){
        return dqdNodeList;
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
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置标题栏不可用
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.activity_main;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void jiSuanDaiQiangDanKm(){
        int dqdSize = dqdNodeList.size();
        for (int i = 0;i<dqdSize; i++) {
            DaiQiangDanNode dqdNode = dqdNodeList.get(i);
            if(dqdNode.getQcStNode()!=null&&dqdNode.getQcEnNode()!=null)
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(dqdNode.getQcStNode()).to(dqdNode.getQcEnNode()));
            else
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(dqdNode.getScStNode()).to(dqdNode.getScEnNode()));
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //nodeIndex = -1;
            //mBtnPre.setVisibility(View.VISIBLE);
            //mBtnNext.setVisibility(View.VISIBLE);
            //route = result.getRouteLines().get(0);
            //MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            //mBaidumap.setOnMarkerClickListener(overlay);
            //routeOverlay = overlay;
            DrivingRouteLine routeLine = result.getRouteLines().get(0);
            //overlay.setData(routeLine);
            //if(isSongCan)
                //overlay.setSongCan(isSongCan);
            //else {
                int duration = routeLine.getDistance();
                /*
                syTime = (float)duration/1330;
                float durationFloat = (float) duration/1000;
                toQcdjlTV.setText(decimalFormat.format(durationFloat));
                syTimeTV.setText("剩余"+decimalFormat.format((float)syTime)+"分钟");
                */
            //}
            //isSongCan=true;
            //overlay.addToMap();
            //overlay.zoomToSpan();

            DaiQiangDanNode dqdNode = dqdNodeList.get(dqdPosition);
            float durationFloat = (float) duration/1000;
            Log.e("result=","111111111111");
            if(dqdNode.getQcStNode()==null&&dqdNode.getQcEnNode()==null){//说明是送餐点
                dqdNode.setToScdjl(durationFloat);
            }
            else{
                dqdNode.setToQcdjl(durationFloat);
            }
            dqdPosition++;
            if(dqdPosition==dqdNodeList.size())
                mPagerAdapter.getmAdapter1().initKm();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
