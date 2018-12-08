package com.hualing.rider.baiduMap;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.hualing.rider.R;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends Activity {

    private BaiduMap baiduMap;
    private MapView baiduMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        //获取地图控件引用
        baiduMapView = (MapView) findViewById(R.id.baiduMapView);
        baiduMap = baiduMapView.getMap();

        initLocation();
        addCustomElementsDemo();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy );
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");
        // 可选，默认gcj02，设置返回的定位结果坐标系 //
        int span = 1000; //option.setScanSpan(span);
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        LocationClient locationClient = new LocationClient(BaiduMapActivity.this);
        locationClient.setLocOption(option);
    }

    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo() {
        // 添加折线
        LatLng p1 = new LatLng(39.97923, 116.357428);
        /*
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.437428);
        LatLng p4 = new LatLng(39.96923, 116.367428);
        LatLng p5 = new LatLng(39.95923, 116.368428);
        LatLng p6 = new LatLng(39.95323, 116.362428);
        LatLng p7 = new LatLng(39.95423, 116.363428);
        */
        LatLng p8 = new LatLng(39.95123, 116.364428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        /*
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        */
        points.add(p8);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        baiduMap.addOverlay(ooPolyline);
        /*
         * // 添加弧线 OverlayOptions ooArc = new
         * ArcOptions().color(0xAA00FF00).width(4) .points(p1, p2, p3);
         * mBaiduMap.addOverlay(ooArc); // 添加圆 LatLng llCircle = new
         * LatLng(39.90923, 116.447428); OverlayOptions ooCircle = new
         * CircleOptions().fillColor(0x000000FF) .center(llCircle).stroke(new
         * Stroke(5, 0xAA000000)) .radius(1400); mBaiduMap.addOverlay(ooCircle);
         *
         * LatLng llDot = new LatLng(39.98923, 116.397428); OverlayOptions ooDot
         * = new DotOptions().center(llDot).radius(6) .color(0xFF0000FF);
         * mBaiduMap.addOverlay(ooDot); // 添加多边形 LatLng pt1 = new
         * LatLng(39.93923, 116.357428); LatLng pt2 = new LatLng(39.91923,
         * 116.327428); LatLng pt3 = new LatLng(39.89923, 116.347428); LatLng
         * pt4 = new LatLng(39.89923, 116.367428); LatLng pt5 = new
         * LatLng(39.91923, 116.387428); List<LatLng> pts = new
         * ArrayList<LatLng>(); pts.add(pt1); pts.add(pt2); pts.add(pt3);
         * pts.add(pt4); pts.add(pt5); OverlayOptions ooPolygon = new
         * PolygonOptions().points(pts) .stroke(new Stroke(5,
         * 0xAA00FF00)).fillColor(0xAAFFFF00); mBaiduMap.addOverlay(ooPolygon);
         * // 添加文字 LatLng llText = new LatLng(39.86923, 116.397428);
         * OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
         * .fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
         * .position(llText); mBaiduMap.addOverlay(ooText);
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        baiduMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        baiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        baiduMapView.onPause();
    }
}
