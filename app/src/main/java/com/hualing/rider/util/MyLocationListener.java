package com.hualing.rider.util;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;

public class MyLocationListener implements BDLocationListener {

    private LatLng latLng;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean isFirstLoc = true; // 是否首次定位

    public MyLocationListener(LocationClient mLocationClient){
        this.mLocationClient=mLocationClient;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // 非空判断
        if (location != null) {
            // 根据BDLocation 对象获得经纬度以及详细地址信息
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String address = location.getAddrStr();
            Log.e("latitude===",""+latitude);
            if (mLocationClient.isStarted()) {
                // 获得位置之后停止定位
                mLocationClient.stop();
            }
        }
    }

}
