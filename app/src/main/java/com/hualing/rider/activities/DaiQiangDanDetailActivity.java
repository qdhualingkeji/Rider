package com.hualing.rider.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
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
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.adapter.DQDProductAdapter;
import com.hualing.rider.entity.DaiQiangDanDetailEntity;
import com.hualing.rider.entity.DaiQiangDanEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.overlayutil.MyDrivingRouteOverlay;
import com.hualing.rider.overlayutil.OverlayManager;
import com.hualing.rider.util.AllActivitiesHolder;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.hualing.rider.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DaiQiangDanDetailActivity extends BaseActivity implements BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

    // 地图控件
    @BindView(R.id.baiduMapView)
    TextureMapView mMapView;
    BaiduMap mBaidumap;
    @BindView(R.id.qcd_name_tv)
    TextView qcdNameTV;
    @BindView(R.id.qcd_address_tv)
    TextView qcdAddressTV;
    @BindView(R.id.scd_address_tv)
    TextView scdAddressTV;
    @BindView(R.id.orderTime_tv)
    TextView orderTimeTV;
    @BindView(R.id.qu_can_dian_iv)
    ImageView qucandianIV;
    @BindView(R.id.song_can_dian_iv)
    ImageView songcandianIV;
    @BindView(R.id.sy_time_tv)
    TextView syTimeTV;
    @BindView(R.id.to_qcdjl_tv)
    TextView toQcdjlTV;
    @BindView(R.id.to_qcddw_tv)
    TextView toQcddwTV;
    @BindView(R.id.sum_price_tv)
    TextView sumPriceTV;
    @BindView(R.id.sum_quantity_tv)
    TextView sumQuantityTV;
    @BindView(R.id.product_lv)
    ListView productLV;
    @BindView(R.id.order_number_tv)
    TextView orderNumberTV;
    @BindView(R.id.qiangdanBtn)
    CardView qiangdanBtn;
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
    private PlanNode qcStNode;
    private PlanNode qcEnNode;
    private PlanNode scStNode;
    private PlanNode scEnNode;
    private boolean isSongCan=false;
    private boolean isRpSongCan=false;
    private boolean haveSCD=false;
    private boolean haveQCD=false;
    private boolean isDrived=false;
    private DQDProductAdapter dqdProductAdapter;
    private boolean isFirstLoc = true; // 是否首次定位
    private float syTime;
    private double longitude;
    private double latitude;
    private double qcLongitude;
    private double qcLatitude;
    private double scLongitude;
    private double scLatitude;
    private int position;

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

        position = getIntent().getIntExtra("position",0);
        DaiQiangDanEntity.DataBean daiQiangDan = (DaiQiangDanEntity.DataBean) getIntent().getSerializableExtra("daiQiangDan");
        qcLongitude=daiQiangDan.getQcLongitude();
        qcLatitude=daiQiangDan.getQcLatitude();
        qcdNameTV.setText(daiQiangDan.getQcShopName());
        qcdAddressTV.setText(daiQiangDan.getQcAddress());
        scLongitude=daiQiangDan.getScLongitude();
        scLatitude=daiQiangDan.getScLatitude();
        scdAddressTV.setText(daiQiangDan.getScAddress());
        orderTimeTV.setText(daiQiangDan.getOrderTime());

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

        String orderNumber = daiQiangDan.getOrderNumber();
        dqdProductAdapter = new DQDProductAdapter(DaiQiangDanDetailActivity.this);
        dqdProductAdapter.setNewData(orderNumber);
        productLV.setAdapter(dqdProductAdapter);

        orderNumberTV.setText(orderNumber);
    }

    public void jiSuanPrice(){
        Double sumPrice=0.0;
        Integer sumQuantity=0;
        int count = dqdProductAdapter.getCount();
        Log.e("count=====",""+count);
        for (int i=0;i<count;i++){
            DaiQiangDanDetailEntity.DataBean product = (DaiQiangDanDetailEntity.DataBean)dqdProductAdapter.getItem(i);
            sumPrice += product.getPrice();
            sumQuantity += product.getQuantity();
        }
        sumPriceTV.setText("餐品（总价￥"+sumPrice+"）");
        sumQuantityTV.setText(String.valueOf(sumQuantity));
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

    private void rePosition(SuggestAddrInfo sai){
        List<PoiInfo> startPoiInfo = sai.getSuggestStartNode();
        if(startPoiInfo!=null){
            LatLng location = startPoiInfo.get(0).location;
            if(isRpSongCan) {
                Log.e("scStNode:lat,long===",location.latitude+","+location.longitude);
                scStNode = PlanNode.withLocation(new LatLng(location.latitude, location.longitude));
            }
            else {
                Log.e("qcStNode:lat,long===",location.latitude+","+location.longitude);
                qcStNode = PlanNode.withLocation(new LatLng(location.latitude, location.longitude));
            }
        }
        List<PoiInfo> endPoiInfo = sai.getSuggestEndNode();
        if(endPoiInfo!=null){
            LatLng location = endPoiInfo.get(0).location;
            if(isRpSongCan) {
                Log.e("scEnNode:lat,long===",location.latitude+","+location.longitude);
                scEnNode = PlanNode.withLocation(new LatLng(location.latitude, location.longitude));
            }
            else {
                Log.e("qcEnNode:lat,long===",location.latitude+","+location.longitude);
                qcEnNode = PlanNode.withLocation(new LatLng(location.latitude, location.longitude));
            }
        }
        if(isRpSongCan) {
            if(!haveSCD) {
                //Log.e("333333333", "333333333333");
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(scStNode).to(scEnNode));
            }
        }
        else {
            if(!haveQCD) {
                //Log.e("44444444444", "444444444");
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qcStNode).to(qcEnNode));
            }
        }
        //Log.e("isRpSongCan===", "" + isRpSongCan);
        isRpSongCan=true;
        /*
        Log.e("addressSize===", "" + poiInfo.size());
        for (int i = 0;i<poiInfo.size();i++) {
            Log.e("location===", "" + poiInfo.get(i).location);
            Log.e("city===", "" + poiInfo.get(i).city);
            Log.e("address===", "" + poiInfo.get(i).address);
        }
        */
    }

    public void drivingSearch(){
        qcStNode = PlanNode.withLocation(new LatLng(latitude,longitude));
        qcEnNode = PlanNode.withLocation(new LatLng(qcLatitude,qcLongitude));
        //qcEnNode = PlanNode.withCityNameAndPlaceName(loaclcity, "双珠路288号东方金石");
        //Log.e("qcLatitude===",""+qcLatitude);
        //Log.e("qcLongitude===",""+qcLongitude);

        scStNode = PlanNode.withLocation(new LatLng(qcLatitude,qcLongitude));
        scEnNode = PlanNode.withLocation(new LatLng(scLatitude,scLongitude));

        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qcStNode).to(qcEnNode));
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(scStNode).to(scEnNode));
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
        //mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qcStNode).to(qcEnNode));
        //mSearch.drivingSearch((new DrivingRoutePlanOption()).from(scStNode).to(scEnNode));
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
            Log.e("DrivingRouteResult=","抱歉，未找到结果");
            SuggestAddrInfo sai = result.getSuggestAddrInfo();
            //rePosition(sai);
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Log.e("DrivingRouteResult=","地址有歧义");
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            SuggestAddrInfo sai = result.getSuggestAddrInfo();
            //MyToast("地址有歧义");
            //rePosition(sai);

            /*
            List<PoiInfo> poiInfo = result.getSuggestAddrInfo().getSuggestEndNode();
            Log.e("addressSize===", "" + poiInfo.size());
            for (int i = 0;i<poiInfo.size();i++) {
                Log.e("location===", "" + poiInfo.get(i).location);
                Log.e("city===", "" + poiInfo.get(i).city);
                Log.e("address===", "" + poiInfo.get(i).address);
            }
            */

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
            DrivingRouteLine routeLine = result.getRouteLines().get(0);
            overlay.setData(routeLine);
            //Log.e("isSongCan111===", "" + isSongCan);
            if(isSongCan) {
                overlay.setSongCan(isSongCan);
                haveSCD=true;
            }
            else {
                int duration = routeLine.getDistance();
                syTime = (float)duration/1330;
                float durationFloat = 0;
                if(duration>=1000) {
                    durationFloat = duration / 1000;
                    toQcddwTV.setText("km");
                }
                else {
                    durationFloat = duration;
                    toQcddwTV.setText("m");
                }
                toQcdjlTV.setText(String.format("%.2f",durationFloat));
                syTimeTV.setText("剩余"+String.format("%.2f",syTime)+"分钟");
                haveQCD=true;
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

            Log.e("++++++++",""+(longitude));
            if(longitude==0||latitude==0) {
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                Log.e("Lat===", "" + longitude + "," + latitude);

                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaidumap.setMyLocationData(locData);
                Log.e("33333333","3333333333");
            }
            else{
                if(!isDrived) {
                    drivingSearch();
                    isDrived=true;
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

    @Override
    protected void onPause() {
        mMapView.onPause(); super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume(); super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.qiangdanBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.qiangdanBtn:
                String orderNumber = orderNumberTV.getText().toString();
                qiangDan(orderNumber,position);
                break;
        }
    }

    private void qiangDan(String orderNumber, final int position){
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber",orderNumber);
        params.put("riderId",GlobalData.riderID);

        AsynClient.post(MyHttpConfing.confirmQiangDan, DaiQiangDanDetailActivity.this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {

            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======",""+rawJsonResponse);

                Gson gson = new Gson();
                DaiQiangDanEntity daiQiangDanEntity = gson.fromJson(rawJsonResponse, DaiQiangDanEntity.class);
                String message = daiQiangDanEntity.getMessage();
                Toast.makeText(DaiQiangDanDetailActivity.this,message,Toast.LENGTH_LONG).show();
                if (daiQiangDanEntity.getCode() == 100) {
                    Intent ii = new Intent();
                    ii.putExtra("position", position);
                    setResult(RESULT_OK,ii);
                    AllActivitiesHolder.removeAct(DaiQiangDanDetailActivity.this);
                }
            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(DaiQiangDanDetailActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
