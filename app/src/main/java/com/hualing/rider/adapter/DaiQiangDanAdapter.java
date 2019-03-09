package com.hualing.rider.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.DaiQiangDanDetailActivity;
import com.hualing.rider.activities.MainActivity;
import com.hualing.rider.entity.DaiQiangDanEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.model.DaiQiangDanNode;
import com.hualing.rider.util.IntentUtil;
import com.hualing.rider.util.MyLocationListener;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaiQiangDanAdapter extends BaseAdapter {

    public List<DaiQiangDanEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<DaiQiangDanEntity.DataBean> mData) {
        this.mData = mData;
    }

    private List<DaiQiangDanEntity.DataBean> mData;
    private MainActivity context;
    public LocationClient mLocationClient;
    public BDLocationListener myListener;
    private DecimalFormat decimalFormat=new DecimalFormat("0.0");
    public static int jiSuanPosition=0;

    public List<DaiQiangDanNode> getDqdNodeList() {
        return dqdNodeList;
    }

    public void setDqdNodeList(List<DaiQiangDanNode> dqdNodeList) {
        this.dqdNodeList = dqdNodeList;
    }

    private List<DaiQiangDanNode> dqdNodeList;

    private String loaclcity = null;

    public DaiQiangDanAdapter(MainActivity context){
        this.context = context;
        mData = new ArrayList<DaiQiangDanEntity.DataBean>();
        dqdNodeList = new ArrayList<DaiQiangDanNode>();

        /*
        // 声明LocationClient类
        mLocationClient = new LocationClient(context.getApplicationContext());
        myListener = new MyLocationListener(mLocationClient);
        // 注册监听
        mLocationClient.registerLocationListener(myListener);

        getLocation();
        */
    }

    public void setNewData(){

        RequestParams params = AsynClient.getRequestParams();
        Gson gson = new Gson();

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/"+ GlobalData.Service.GET_DAI_QIANG_DAN, context, params, new GsonHttpResponseHandler() {
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
                if (daiQiangDanEntity.getCode() == 100) {
                    mData = daiQiangDanEntity.getData();
                    notifyDataSetChanged();//这是适配器第一次发出通知，先获取地址，为后面计算路程提供的
                    initDaiQiangDanNode(mData);
                    jiSuanDaiQiangDanKm();
                }
            }
        });
    }

    /** 获得所在位置经纬度及详细地址 */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

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
            qcNode = new DaiQiangDanNode(this);
            //qcNode.setQcStNode(PlanNode.withCityNameAndPlaceName(loaclcity, "山东省青岛市黄岛区隐珠镇向阳岭路7号"));
            //qcNode.setQcEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, dataBean.getQcAddress()));
            qcNode.setQcStNode(PlanNode.withLocation(new LatLng(35.875561,120.048224)));
            qcNode.setQcEnNode(PlanNode.withLocation(new LatLng(dataBean.getQcLatitude(),dataBean.getQcLongitude())));
            qcNode.setOrderNumber(dataBean.getOrderNumber());
            //Log.e("qcNode111==",qcNode.getOrderNumber());
            dqdNodeList.add(qcNode);

            scNode = new DaiQiangDanNode(this);
            //Log.e("ScLongitude==",dataBean.getScLongitude()+"");
            //Log.e("ScAddress==",dataBean.getScAddress());
            //scNode.setScStNode(PlanNode.withCityNameAndPlaceName(loaclcity, dataBean.getQcAddress()));
            //scNode.setScEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, dataBean.getScAddress()));
            scNode.setScStNode(PlanNode.withLocation(new LatLng(dataBean.getQcLatitude(),dataBean.getQcLongitude())));
            scNode.setScEnNode(PlanNode.withLocation(new LatLng(dataBean.getScLatitude(),dataBean.getScLongitude())));
            //scNode.setScStNode(PlanNode.withLocation(new LatLng(35.88220442808485,120.04091561768301)));
            //scNode.setScEnNode(PlanNode.withLocation(new LatLng(35.88425874859243,120.05011426610518)));
            scNode.setOrderNumber(dataBean.getOrderNumber());
            dqdNodeList.add(scNode);
            if(i==5)
                break;
        }
    }

    public void jiSuanDaiQiangDanKm(){
        int dqdSize = dqdNodeList.size();
        for (int i = 0;i<dqdSize; i++) {
            DaiQiangDanNode dqdNode = dqdNodeList.get(i);
            //Log.e("dqdNode==",dqdNode.getOrderNumber()+"");
            dqdNode.drivingSearch();
        }
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_dai_qiang_dan,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DaiQiangDanEntity.DataBean daiQiangDan = mData.get(position);
        View layout1 = convertView.findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(daiQiangDan);
            }
        });
        View layout2 = convertView.findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(daiQiangDan);
            }
        });
        View layout3 = convertView.findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(daiQiangDan);
            }
        });
        View qiangdanBtn = convertView.findViewById(R.id.qiangdanBtn);
        qiangdanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.mSyTimeTV.setText("剩余"+decimalFormat.format((float)daiQiangDan.getSyTime())+"分钟");

        float durationFloatQc = 0;
        float durationQc = daiQiangDan.getToQcdjl();
        if(durationQc>=1000) {
            durationFloatQc = durationQc / 1000;
            holder.mToQcddwTV.setText("km");
        }
        else {
            durationFloatQc = durationQc;
            holder.mToQcddwTV.setText("m");
        }
        holder.mToQcdjlTV.setText(decimalFormat.format(durationFloatQc));

        float durationFloatSc = 0;
        float durationSc = daiQiangDan.getToScdjl();
        if(durationSc>=1000) {
            durationFloatSc = durationSc / 1000;
            holder.mToScddwTV.setText("km");
        }
        else {
            durationFloatSc = durationSc;
            holder.mToScddwTV.setText("m");
        }
        holder.mToScdjlTV.setText(decimalFormat.format(durationFloatSc));
        holder.mQcShopNameTV.setText(daiQiangDan.getQcShopName());
        holder.mQcAddressTV.setText(daiQiangDan.getQcAddress());
        holder.mScAddressTV.setText(daiQiangDan.getScAddress());
        holder.orderNumber=daiQiangDan.getOrderNumber();

        return convertView;
    }

    private void goDetail(DaiQiangDanEntity.DataBean daiQiangDan){
        Bundle bundle = new Bundle();
        bundle.putSerializable("daiQiangDan",daiQiangDan);
        IntentUtil.openActivityForResult(context, DaiQiangDanDetailActivity.class,-1,bundle);
    }

    class ViewHolder {
        @BindView(R.id.sy_time_tv)
        TextView mSyTimeTV;
        @BindView(R.id.to_qcdjl_tv)
        TextView mToQcdjlTV;
        @BindView(R.id.to_qcddw_tv)
        TextView mToQcddwTV;
        @BindView(R.id.to_scdjl_tv)
        TextView mToScdjlTV;
        @BindView(R.id.to_scddw_tv)
        TextView mToScddwTV;
        @BindView(R.id.qc_shop_name_tv)
        TextView mQcShopNameTV;
        @BindView(R.id.qc_address_tv)
        TextView mQcAddressTV;
        @BindView(R.id.sc_address_tv)
        TextView mScAddressTV;
        private String orderNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
