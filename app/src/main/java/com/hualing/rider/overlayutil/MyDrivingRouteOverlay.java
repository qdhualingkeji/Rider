package com.hualing.rider.overlayutil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;

import java.util.ArrayList;
import java.util.List;

public class MyDrivingRouteOverlay extends DrivingRouteOverlay {

    private boolean isSongCan=false;

    public MyDrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public boolean isSongCan() {
        return isSongCan;
    }

    public void setSongCan(boolean songCan) {
        isSongCan = songCan;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        if (mRouteLine == null) {
            return null;
        }

        List<OverlayOptions> overlayOptionses = new ArrayList<OverlayOptions>();
        // step node
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {

            for (DrivingRouteLine.DrivingStep step : mRouteLine.getAllStep()) {
                Bundle b = new Bundle();
                b.putInt("index", mRouteLine.getAllStep().indexOf(step));
                if (step.getEntrance() != null) {
                    if(isSongCan){
                        overlayOptionses.add((new MarkerOptions())
                                .position(step.getEntrance().getLocation())
                                .anchor(0.5f, 0.5f)
                                .zIndex(10)
                                .rotate((360 - step.getDirection()))
                                .extraInfo(b)
                                .icon(BitmapDescriptorFactory
                                        .fromAssetWithDpi("song_can_line_node.png")));
                    }
                    else {
                        overlayOptionses.add((new MarkerOptions())
                                .position(step.getEntrance().getLocation())
                                .anchor(0.5f, 0.5f)
                                .zIndex(10)
                                .rotate((360 - step.getDirection()))
                                .extraInfo(b)
                                .icon(BitmapDescriptorFactory
                                        .fromAssetWithDpi("qu_can_line_node.png")));
                    }
                }
                // 最后路段绘制出口点
                if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                        .getAllStep().size() - 1) && step.getExit() != null) {
                    overlayOptionses.add((new MarkerOptions())
                            .position(step.getExit().getLocation())
                            .anchor(0.5f, 0.5f)
                            .zIndex(10)
                            .icon(BitmapDescriptorFactory
                                    .fromAssetWithDpi("Icon_line_node.png")));

                }
            }
        }

        if (mRouteLine.getStarting() != null) {
            if(!isSongCan) {
                overlayOptionses.add((new MarkerOptions())
                        .position(mRouteLine.getStarting().getLocation())
                        .icon(getStartMarker() != null ? getStartMarker() :
                                BitmapDescriptorFactory
                                        .fromAssetWithDpi("me.png")).zIndex(10));
            }
        }
        if (mRouteLine.getTerminal() != null) {
            if(isSongCan){
                overlayOptionses
                        .add((new MarkerOptions())
                                .position(mRouteLine.getTerminal().getLocation())
                                .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                        BitmapDescriptorFactory
                                                .fromAssetWithDpi("song_can_dian.png"))
                                .zIndex(10));
            }
            else {
                overlayOptionses
                        .add((new MarkerOptions())
                                .position(mRouteLine.getTerminal().getLocation())
                                .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                        BitmapDescriptorFactory
                                                .fromAssetWithDpi("qu_can_dian.png"))
                                .zIndex(10));
            }
        }
        // poly line
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {

            List<DrivingRouteLine.DrivingStep> steps = mRouteLine.getAllStep();
            int stepNum = steps.size();


            List<LatLng> points = new ArrayList<LatLng>();
            ArrayList<Integer> traffics = new ArrayList<Integer>();
            int totalTraffic = 0;
            for (int i = 0; i < stepNum ; i++) {
                if (i == stepNum - 1) {
                    points.addAll(steps.get(i).getWayPoints());
                } else {
                    points.addAll(steps.get(i).getWayPoints().subList(0, steps.get(i).getWayPoints().size() - 1));
                }

                totalTraffic += steps.get(i).getWayPoints().size() - 1;
                if (steps.get(i).getTrafficList() != null && steps.get(i).getTrafficList().length > 0) {
                    for (int j = 0;j < steps.get(i).getTrafficList().length;j++) {
                        traffics.add(steps.get(i).getTrafficList()[j]);
                    }
                }
            }

//            Bundle indexList = new Bundle();
//            if (traffics.size() > 0) {
//                int raffic[] = new int[traffics.size()];
//                int index = 0;
//                for (Integer tempTraff : traffics) {
//                    raffic[index] = tempTraff.intValue();
//                    index++;
//                }
//                indexList.putIntArray("indexs", raffic);
//            }
            boolean isDotLine = false;

            if (traffics != null && traffics.size() > 0) {
                isDotLine = true;
            }
            PolylineOptions option = new PolylineOptions().points(points).textureIndex(traffics)
                    .width(7).dottedLine(isDotLine).focus(true)
                    .color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0);
            if (isDotLine) {
                option.customTextureList(getCustomTextureList());
            }
            overlayOptionses.add(option);
        }
        return overlayOptionses;
    }
/*
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
    */
}
