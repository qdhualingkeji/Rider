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
import com.hualing.rider.adapter.DaiQuHuoAdapter;
import com.hualing.rider.entity.DaiQuHuoEntity;

import java.util.List;

public class DaiQuHuoNode implements OnGetRoutePlanResultListener {
    private PlanNode qhStNode;
    private PlanNode qhEnNode;
    private PlanNode shStNode;
    private PlanNode shEnNode;

    public PlanNode getQhStNode() {
        return qhStNode;
    }

    public void setQhStNode(PlanNode qhStNode) {
        this.qhStNode = qhStNode;
    }

    public PlanNode getQhEnNode() {
        return qhEnNode;
    }

    public void setQhEnNode(PlanNode qhEnNode) {
        this.qhEnNode = qhEnNode;
    }

    public PlanNode getShStNode() {
        return shStNode;
    }

    public void setShStNode(PlanNode shStNode) {
        this.shStNode = shStNode;
    }

    public PlanNode getShEnNode() {
        return shEnNode;
    }

    public void setShEnNode(PlanNode shEnNode) {
        this.shEnNode = shEnNode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    private float toQhdjl;
    private float toShdjl;
    private String orderNumber;
    private float qhSyTime;
    private float shSyTime;

    // 搜索相关
    RoutePlanSearch mSearch = null;
    private DaiQuHuoAdapter daiQuHuoAdapter;

    public DaiQuHuoNode(DaiQuHuoAdapter daiQuHuoAdapter){
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        this.daiQuHuoAdapter =daiQuHuoAdapter;
    }

    public void drivingSearch(){
        if(qhStNode!=null&&qhEnNode!=null)
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(qhStNode).to(qhEnNode));
        else
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(shStNode).to(shEnNode));
    }

    public void initKm(){
        //for (int i=0;i<dqdNodeList.size();i++){
        //DaiQiangDanNode node = dqdNodeList.get(i);

        List<DaiQuHuoEntity.DataBean> mData = daiQuHuoAdapter.getmData();
        //Log.e("ToQc=,ToSc=111111==",toQcdjl+","+toScdjl);
        for (int i=0;i<mData.size();i++) {
            DaiQuHuoEntity.DataBean dataBean = mData.get(i);
            if(orderNumber!=null&&orderNumber.equals(dataBean.getOrderNumber())) {
                //Log.e("ToQc=,ToSc=,orNur=",toQcdjl+","+toScdjl+","+orderNumber+","+dataBean.getOrderNumber());
                if(toShdjl==0.0) {
                    dataBean.setToQhdjl(toQhdjl);
                    dataBean.setQhSyTime(qhSyTime);
                }
                else {
                    dataBean.setToShdjl(toShdjl);
                    dataBean.setShSyTime(shSyTime);
                }
            }
        }

        //}
        DaiQuHuoAdapter.jiSuanPosition++;
        if(DaiQuHuoAdapter.jiSuanPosition==daiQuHuoAdapter.getDqhNodeList().size()) {//当已计算出的数量等于适配器里集合的元素数量时，适配器再发出第二次通知，刷新出路程来
            daiQuHuoAdapter.notifyDataSetChanged();
            DaiQuHuoAdapter.jiSuanPosition = 0;//发出通知之后，把数量标志清零
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

            float durationFloat = (float) duration/1000;
            //Log.e("durationFloat===",""+durationFloat);
            if(qhStNode==null&&qhEnNode==null){//说明是送货点
                //Log.e("setToScdjl===",""+durationFloat);
                toShdjl = durationFloat;
                shSyTime = (float)duration/1330;
            }
            else{
                //Log.e("setToQcdjl===",""+durationFloat);
                toQhdjl = durationFloat;
                qhSyTime = (float)duration/1330;
            }
            //Log.e("toShdjl===",""+toShdjl);
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
