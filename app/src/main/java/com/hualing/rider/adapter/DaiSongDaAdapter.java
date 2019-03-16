package com.hualing.rider.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.DaiSongDaMapActivity;
import com.hualing.rider.activities.MainActivity;
import com.hualing.rider.entity.DaiSongDaEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.model.DaiSongDaNode;
import com.hualing.rider.util.AllActivitiesHolder;
import com.hualing.rider.util.IntentUtil;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaiSongDaAdapter extends BaseAdapter {

    private List<DaiSongDaEntity.DataBean> mData;
    private List<DaiSongDaNode> dsdNodeList;

    public List<DaiSongDaEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<DaiSongDaEntity.DataBean> mData) {
        this.mData = mData;
    }

    public List<DaiSongDaNode> getDsdNodeList() {
        return dsdNodeList;
    }

    public void setDsdNodeList(List<DaiSongDaNode> dsdNodeList) {
        this.dsdNodeList = dsdNodeList;
    }

    private static final int DaiSongCanMap=113;
    private MainActivity context;
    public static int jiSuanPosition=0;
    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public DaiSongDaAdapter(MainActivity context){
        this.context = context;
        mData = new ArrayList<DaiSongDaEntity.DataBean>();
        dsdNodeList = new ArrayList<DaiSongDaNode>();
    }


    public void setNewData(){

        RequestParams params = AsynClient.getRequestParams();
        Gson gson = new Gson();

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/"+ GlobalData.Service.GET_DAI_SONG_DA, context, params, new GsonHttpResponseHandler() {
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
                DaiSongDaEntity daiSongDaEntity = gson.fromJson(rawJsonResponse, DaiSongDaEntity.class);
                if (daiSongDaEntity.getCode() == 100) {
                    mData = daiSongDaEntity.getData();
                    notifyDataSetChanged();
                    initDaiSongDaNode(mData);
                    jiSuanDaiSongDaKm();
                }
            }
        });
    }

    /**
     * 初始化待送达地点
     */
    public void initDaiSongDaNode(List<DaiSongDaEntity.DataBean> dsdList){
        DaiSongDaNode qhNode = null;
        DaiSongDaNode shNode = null;
        int dsdListSize = dsdList.size();
        for (int i = 0; i<dsdListSize; i++) {
            DaiSongDaEntity.DataBean dataBean = dsdList.get(i);
            qhNode = new DaiSongDaNode(this);
            qhNode.setQhStNode(PlanNode.withLocation(new LatLng(latitude,longitude)));
            qhNode.setQhEnNode(PlanNode.withLocation(new LatLng(dataBean.getQhLatitude(),dataBean.getQhLongitude())));
            qhNode.setOrderNumber(dataBean.getOrderNumber());
            dsdNodeList.add(qhNode);

            shNode = new DaiSongDaNode(this);
            shNode.setShStNode(PlanNode.withLocation(new LatLng(dataBean.getQhLatitude(),dataBean.getQhLongitude())));
            shNode.setShEnNode(PlanNode.withLocation(new LatLng(dataBean.getShLatitude(),dataBean.getShLongitude())));
            shNode.setOrderNumber(dataBean.getOrderNumber());
            dsdNodeList.add(shNode);
            if(i==5)
                break;
        }
    }

    public void jiSuanDaiSongDaKm(){
        int dsdSize = dsdNodeList.size();
        for (int i = 0;i<dsdSize; i++) {
            DaiSongDaNode dqhNode = dsdNodeList.get(i);
            //Log.e("dqhNode==",dqhNode.getOrderNumber()+"");
            dqhNode.drivingSearch();
        }
    }

    public void showProgressDialog(){
        context.hideProgressDialog();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_dai_song_da,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        final DaiSongDaEntity.DataBean dataBean = mData.get(position);
        holder.mSyTimeTV.setText(String.format("%.2f",(dataBean.getQhSyTime()+dataBean.getShSyTime()))+"分钟内送达");
        holder.mPriceTV.setText("￥"+dataBean.getPrice());

        float durationFloatQh = 0;
        float durationQh = dataBean.getToQhdjl();
        if(durationQh>=1000) {
            durationFloatQh = durationQh / 1000;
            holder.mToQhdjlTV.setText(String.format("%.2f",durationFloatQh)+"km");
        }
        else {
            durationFloatQh = durationQh;
            holder.mToQhdjlTV.setText(String.format("%.2f",durationFloatQh)+"m");
        }

        float durationFloatSh = 0;
        float durationSh = dataBean.getToShdjl();
        if(durationSh>=1000) {
            durationFloatSh = durationSh / 1000;
            holder.mToShdjlTV.setText(String.format("%.2f",durationFloatSh)+"km");
        }
        else {
            durationFloatSh = durationSh;
            holder.mToShdjlTV.setText(String.format("%.2f",durationFloatSh)+"m");
        }

        holder.mQhAddressTV.setText(dataBean.getQhAddress());
        holder.mShAddressTV.setText(dataBean.getShAddress());
        View layout1 = convertView.findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMap(dataBean,position);
            }
        });
        View layout2 = convertView.findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMap(dataBean,position);
            }
        });
        View layout3 = convertView.findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMap(dataBean,position);
            }
        });
        holder.mQrsdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSongDa(dataBean.getOrderNumber(),dataBean.getAccountToken(),position);
            }
        });

        return convertView;
    }

    private void confirmSongDa(String orderNumber, String accountToken, final int position) {
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber", orderNumber);
        params.put("accountToken",accountToken);

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/" + GlobalData.Service.CONFIRM_SONG_DA, context, params, new GsonHttpResponseHandler() {
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
                if (daiSongDaEntity.getCode() == 100) {
                    mData.remove(position);
                    notifyDataSetChanged();
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goMap(DaiSongDaEntity.DataBean daiSongDa,int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("daiSongDa",daiSongDa);
        bundle.putInt("position",position);
        IntentUtil.openActivityForResult(context, DaiSongDaMapActivity.class,DaiSongCanMap,bundle);
    }

    class ViewHolder {
        @BindView(R.id.sy_time_tv)
        TextView mSyTimeTV;
        @BindView(R.id.price_tv)
        TextView mPriceTV;
        @BindView(R.id.to_qhdjl_tv)
        TextView mToQhdjlTV;
        @BindView(R.id.to_shdjl_tv)
        TextView mToShdjlTV;
        @BindView(R.id.qh_address_tv)
        TextView mQhAddressTV;
        @BindView(R.id.sh_address_tv)
        TextView mShAddressTV;
        @BindView(R.id.qrsdBtn)
        CardView mQrsdBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
