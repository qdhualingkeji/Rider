package com.hualing.rider.activities;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
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
import com.hualing.rider.baiduMap.DriverMenuActivity;
import com.hualing.rider.overlayutil.DrivingRouteOverlay;
import com.hualing.rider.overlayutil.OverlayManager;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class DaiQiangDanDetailActivity extends BaseActivity implements BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

    // 地图控件
    @BindView(R.id.baiduMapView)
    TextureMapView mMapView;
    BaiduMap mBaidumap;
    @BindView(R.id.qu_can_dian_iv)
    ImageView qucandianIV;
    @BindView(R.id.song_can_dian_iv)
    ImageView songcandianIV;
    private String loaclcity = null;
    boolean useDefaultIcon = false;
    // 浏览路线节点相关
    Button mBtnPre = null;
    // 上一个节点
    Button mBtnNext = null;
    // 下一个节点
    int nodeIndex = -1;
    // 搜索相关
    RoutePlanSearch mSearch = null;
    // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private PlanNode stNode1;
    private PlanNode enNode1;
    private PlanNode stNode2;
    private PlanNode enNode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initMap();

        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        stNode1 = PlanNode.withCityNameAndPlaceName(loaclcity, "青岛尼莫");
        enNode1 = PlanNode.withCityNameAndPlaceName(loaclcity, "青岛颐和国际");

        stNode2 = PlanNode.withCityNameAndPlaceName(loaclcity, "青岛颐和国际");
        enNode2 = PlanNode.withCityNameAndPlaceName(loaclcity, "青岛远雄国际广场");

        AssetManager assetManager = getAssets();
        try {
            InputStream qcdIS = assetManager.open("qu_can_dian.png");
            Bitmap qcdBF = BitmapFactory.decodeStream(qcdIS);
            qucandianIV.setImageBitmap(qcdBF);

            InputStream scdIS = assetManager.open("song_can_dian.png");
            Bitmap scdBF = BitmapFactory.decodeStream(scdIS);
            songcandianIV.setImageBitmap(scdBF);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocClient.setLocOption(option);
        mLocClient.start();
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
        return R.layout.activity_dai_qiang_dan_detail;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode1).to(enNode1));
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode2).to(enNode2));
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
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
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            MyToast("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Log.e("DrivingRouteResult=","地址有歧义");
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            MyToast("地址有歧义");
            return;
        }
        if (result.error == BikingRouteResult.ERRORNO.PERMISSION_UNFINISHED){
            MyToast("没有权限");
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            //mBtnPre.setVisibility(View.VISIBLE);
            //mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay; overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
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
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            /*
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                start_edit.setText(location.getAddrStr());
                MyToast("当前所在位置：" + location.getAddrStr());
                driver_city.setText(location.getCity());
                loaclcity = location.getCity();
            }
            */
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    public void MyToast(String s) {
        Toast.makeText(DaiQiangDanDetailActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
