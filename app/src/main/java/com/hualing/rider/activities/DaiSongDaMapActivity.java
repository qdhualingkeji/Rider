package com.hualing.rider.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
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
import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.entity.DaiSongDaEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.overlayutil.MyDrivingRouteOverlay;
import com.hualing.rider.overlayutil.OverlayManager;
import com.hualing.rider.util.AllActivitiesHolder;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.hualing.rider.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

public class DaiSongDaMapActivity extends BaseActivity implements BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

    // 地图控件
    @BindView(R.id.baiduMapView)
    TextureMapView mMapView;
    BaiduMap mBaidumap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 搜索相关
    RoutePlanSearch mSearch = null;
    private PlanNode qhStNode;
    private PlanNode qhEnNode;
    private PlanNode shStNode;
    private PlanNode shEnNode;
    @BindView(R.id.qh_address_tv)
    TextView qhAddressTV;
    @BindView(R.id.sh_address_tv)
    TextView shAddressTV;
    @BindView(R.id.to_qhdjl_tv)
    TextView toQhdjlTV;
    @BindView(R.id.to_shdjl_tv)
    TextView toShdjlTV;
    @BindView(R.id.qrsdBtn)
    CardView qrsdBtn;
    // 下一个节点
    int nodeIndex = -1;
    // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    private boolean isSongCan=false;
    private boolean isDrived=false;
    private double longitude;
    private double latitude;
    private double qhLongitude;
    private double qhLatitude;
    private double shLongitude;
    private double shLatitude;
    private String orderNumber;
    private String accountToken;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        position = getIntent().getIntExtra("position",0);
        DaiSongDaEntity.DataBean daiSongDa = (DaiSongDaEntity.DataBean) getIntent().getSerializableExtra("daiSongDa");
        qhLongitude=daiSongDa.getQhLongitude();
        qhLatitude=daiSongDa.getQhLatitude();
        qhAddressTV.setText(daiSongDa.getQhAddress());

        shLongitude=daiSongDa.getShLongitude();
        shLatitude=daiSongDa.getShLatitude();
        shAddressTV.setText(daiSongDa.getShAddress());

        orderNumber = daiSongDa.getOrderNumber();
        accountToken = daiSongDa.getAccountToken();

        initMap();

        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
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

    public void drivingSearch(){
        qhStNode = PlanNode.withLocation(new LatLng(latitude,longitude));
        qhEnNode = PlanNode.withLocation(new LatLng(qhLatitude,qhLongitude));

        shStNode = PlanNode.withLocation(new LatLng(qhLatitude,qhLongitude));
        shEnNode = PlanNode.withLocation(new LatLng(shLatitude,shLongitude));

        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qhStNode).to(qhEnNode));
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(shStNode).to(shEnNode));
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
        return R.layout.activity_dai_song_da_map;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaidumap.hideInfoWindow();
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
            MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));

            overlay.setSongCan(isSongCan);
            int duration = route.getDistance();
            String durationStr;
            float durationFloat = 0;
            if(duration>=1000) {
                durationFloat = duration / 1000;
                durationStr=String.format("%.2fkm",durationFloat);
            }
            else {
                durationFloat = duration;
                durationStr=String.format("%.2fm",durationFloat);
            }

            if(isSongCan) {
                toQhdjlTV.setText(durationStr);
            }
            else{
                toShdjlTV.setText(durationStr);
            }
            isSongCan=true;
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

            if(longitude==0||latitude==0) {
                longitude=location.getLongitude();
                latitude=location.getLatitude();

                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaidumap.setMyLocationData(locData);
            }
            else{
                if(!isDrived) {
                    drivingSearch();
                    isDrived=true;
                }
            }
        }
    }

    @OnClick({R.id.qrsdBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.qrsdBtn:
                confirmSongDa(orderNumber,position);
                break;
        }
    }

    private void confirmSongDa(String orderNumber, final int position) {
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber", orderNumber);
        params.put("accountToken",accountToken);

        AsynClient.post(MyHttpConfing.confirmSongDa, DaiSongDaMapActivity.this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {

            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======", "" + rawJsonResponse);

                Gson gson = new Gson();
                DaiSongDaEntity daiSongDaEntity = gson.fromJson(rawJsonResponse, DaiSongDaEntity.class);
                String message = daiSongDaEntity.getMessage();
                Toast.makeText(DaiSongDaMapActivity.this, message, Toast.LENGTH_LONG).show();
                if (daiSongDaEntity.getCode() == 0) {
                    Intent ii = new Intent();
                    ii.putExtra("position", position);
                    setResult(RESULT_OK, ii);
                    AllActivitiesHolder.removeAct(DaiSongDaMapActivity.this);
                }
            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(DaiSongDaMapActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
