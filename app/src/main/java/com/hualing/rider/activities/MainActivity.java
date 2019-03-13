package com.hualing.rider.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.hualing.rider.R;
import com.hualing.rider.adapter.MyPagerAdapter;
import com.hualing.rider.global.TheApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    // 地图控件
    @BindView(R.id.baiduMapView)
    TextureMapView mMapView;
    BaiduMap mBaidumap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private double longitude;
    private double latitude;
    private boolean initedAdapter=false;
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
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initMap();
        //showProgressDialog();
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
    }

    private void initMap(){
        // 地图初始化
        mBaidumap = mMapView.getMap();
        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
        // 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        /**
         * 设置定位模式 Battery_Saving 低功耗模式 Device_Sensors 仅设备(Gps)模式 Hight_Accuracy
         * 高精度模式
         /   */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置是否打开gps进行定位
        option.setOpenGps(true);
        // 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            //Log.e("++++++++",""+(longitude));
            if(longitude==0||latitude==0) {
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                //Log.e("Lat===", "" + longitude + "," + latitude);

                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaidumap.setMyLocationData(locData);
                //Log.e("33333333","3333333333");
            }
            else{
                if(!initedAdapter) {
                    initMyPagerAdapter();
                    initedAdapter=true;
                }
            }
            /*
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                //start_edit.setText(location.getAddrStr());
                //MyToast("当前所在位置：" + location.getAddrStr());
                //driver_city.setText(location.getCity());
                loaclcity = location.getCity();

                MyToast("========"+location.getLocType());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(DaiQiangDanDetailActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(DaiQiangDanDetailActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                }
                else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(DaiQiangDanDetailActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                }
                else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(DaiQiangDanDetailActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(DaiQiangDanDetailActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                }
                else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(DaiQiangDanDetailActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
            */
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    public void initMyPagerAdapter(){
        mPagerAdapter = new MyPagerAdapter(MainActivity.this,longitude,latitude);
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

    public void showProgressDialog(){
        progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
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
                Log.e("position1========",""+position);
                mDot1.setSelected(true);
                break;
            case 1:
                Log.e("position2========",""+position);
                mDot2.setSelected(true);
                break;
            case 2:
                Log.e("position3========",""+position);
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
}
