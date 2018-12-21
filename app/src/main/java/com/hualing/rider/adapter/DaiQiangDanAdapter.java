package com.hualing.rider.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.DaiQiangDanDetailActivity;
import com.hualing.rider.activities.LoginActivity;
import com.hualing.rider.activities.MainActivity;
import com.hualing.rider.entity.DaiQiangDanEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.model.DaiQiangDanNode;
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
import butterknife.OnClick;

public class DaiQiangDanAdapter extends BaseAdapter {

    private List<DaiQiangDanEntity.DataBean> mData;
    private MainActivity context;
    private DecimalFormat decimalFormat=new DecimalFormat("0.0");

    public DaiQiangDanAdapter(MainActivity context){
        this.context = context;
        mData = new ArrayList<DaiQiangDanEntity.DataBean>();
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
                    notifyDataSetChanged();
                    context.initDaiQiangDanNode(mData);
                    context.jiSuanDaiQiangDanKm();
                }
            }
        });
    }

    public void initKm(){
        List<DaiQiangDanNode> nodeList = context.getDqdNodeList();
        for (int i=0;i<nodeList.size();i++){
            DaiQiangDanNode node = nodeList.get(i);

            for (int j=0;j<mData.size();j++) {
                DaiQiangDanEntity.DataBean dataBean = mData.get(j);
                if(node.getOrderNumber()!=null&&node.getOrderNumber().equals(dataBean.getOrderNumber())) {
                    if(node.getToScdjl()==0.0) {
                        Log.e("ToQcdjl======",""+node.getToQcdjl());
                        dataBean.setToQcdjl(node.getToQcdjl());
                    }
                    else {
                        Log.e("ToScdjl======",""+node.getToScdjl());
                        dataBean.setToScdjl(node.getToScdjl());
                    }
                }
            }


        }
        notifyDataSetChanged();
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

        holder.mToQcdjlTV.setText(decimalFormat.format(daiQiangDan.getToQcdjl()));
        holder.mToScdjlTV.setText(decimalFormat.format(daiQiangDan.getToScdjl()));
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
        @BindView(R.id.to_qcdjl_tv)
        TextView mToQcdjlTV;
        @BindView(R.id.to_scdjl_tv)
        TextView mToScdjlTV;
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
