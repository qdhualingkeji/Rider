package com.hualing.rider.model;

import android.util.Log;

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
import com.hualing.rider.adapter.DaiQiangDanAdapter;
import com.hualing.rider.entity.DaiQiangDanEntity;

import java.util.ArrayList;
import java.util.List;

public class DaiQiangDanNode implements OnGetRoutePlanResultListener {
    private PlanNode qcStNode;
    private PlanNode qcEnNode;
    private PlanNode scStNode;
    private float toQcdjl;
    private float toScdjl;
    private String orderNumber;
    // 搜索相关
    RoutePlanSearch mSearch = null;
    private DaiQiangDanAdapter daiQiangDanAdapter;

    public DaiQiangDanNode(DaiQiangDanAdapter daiQiangDanAdapter){
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        this.daiQiangDanAdapter =daiQiangDanAdapter;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getToQcdjl() {
        return toQcdjl;
    }

    public void setToQcdjl(float toQcdjl) {
        this.toQcdjl = toQcdjl;
    }

    public float getToScdjl() {
        return toScdjl;
    }

    public void setToScdjl(float toScdjl) {
        this.toScdjl = toScdjl;
    }

    public PlanNode getQcStNode() {
        return qcStNode;
    }

    public void setQcStNode(PlanNode qcStNode) {
        this.qcStNode = qcStNode;
    }

    public PlanNode getQcEnNode() {
        return qcEnNode;
    }

    public void setQcEnNode(PlanNode qcEnNode) {
        this.qcEnNode = qcEnNode;
    }

    public PlanNode getScStNode() {
        return scStNode;
    }

    public void setScStNode(PlanNode scStNode) {
        this.scStNode = scStNode;
    }

    public PlanNode getScEnNode() {
        return scEnNode;
    }

    public void setScEnNode(PlanNode scEnNode) {
        this.scEnNode = scEnNode;
    }

    private PlanNode scEnNode;

    public void drivingSearch(){
        if(qcStNode!=null&&qcEnNode!=null)
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qcStNode).to(qcEnNode));
        else
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(scStNode).to(scEnNode));
    }

    public void initKm(){
        //for (int i=0;i<dqdNodeList.size();i++){
            //DaiQiangDanNode node = dqdNodeList.get(i);

            List<DaiQiangDanEntity.DataBean> mData = daiQiangDanAdapter.getmData();
            //Log.e("ToQc=,ToSc=111111==",toQcdjl+","+toScdjl);
            for (int i=0;i<mData.size();i++) {
                DaiQiangDanEntity.DataBean dataBean = mData.get(i);
                //Log.e("ToQc=,ToSc=,orNur=",toQcdjl+","+toScdjl+","+orderNumber+","+dataBean.getOrderNumber());
                if(orderNumber!=null&&orderNumber.equals(dataBean.getOrderNumber())) {
                    /*
                    if(toScdjl==0.0) {
                        dataBean.setToQcdjl(toQcdjl);
                    }
                    else {
                        dataBean.setToScdjl(toScdjl);
                    }
                    */
                }
            }

        //}
        daiQiangDanAdapter.notifyDataSetChanged();
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

            float durationFloat = (float) duration/1000;
            //Log.e("durationFloat===",""+durationFloat);
            if(qcStNode==null&&qcEnNode==null){//说明是送餐点
                //Log.e("setToScdjl===",""+durationFloat);
                setToScdjl(durationFloat);
            }
            else{
                //Log.e("setToQcdjl===",""+durationFloat);
                setToQcdjl(durationFloat);
            }
            initKm();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
