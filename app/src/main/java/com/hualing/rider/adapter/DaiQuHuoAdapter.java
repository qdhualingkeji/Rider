package com.hualing.rider.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.QianWangQuCanActivity;
import com.hualing.rider.entity.DaiQiangDanEntity;
import com.hualing.rider.entity.DaiQuHuoEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.util.IntentUtil;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaiQuHuoAdapter extends BaseAdapter {

    private List<DaiQuHuoEntity.DataBean> mData;
    private Activity context;

    public DaiQuHuoAdapter(Activity context){
        this.context = context;
        mData = new ArrayList<DaiQuHuoEntity.DataBean>();
    }

    public void setNewData(){

        RequestParams params = AsynClient.getRequestParams();
        Gson gson = new Gson();

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/"+ GlobalData.Service.GET_DAI_QU_HUO, context, params, new GsonHttpResponseHandler() {
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
                DaiQuHuoEntity daiQuHuoEntity = gson.fromJson(rawJsonResponse, DaiQuHuoEntity.class);
                if (daiQuHuoEntity.getCode() == 100) {
                    mData = daiQuHuoEntity.getData();
                    notifyDataSetChanged();
                }
            }
        });
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
            convertView = context.getLayoutInflater().inflate(R.layout.item_dai_qu_huo,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        final DaiQuHuoEntity.DataBean dataBean = mData.get(position);
        holder.mPriceTV.setText("￥"+dataBean.getPrice());
        holder.mQhAddressTV.setText(dataBean.getQhAddress());
        holder.mShAddressTV.setText(dataBean.getShAddress());
        holder.mQwqcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goQWQC(dataBean);
            }
        });

        return convertView;
    }

    private void goQWQC(DaiQuHuoEntity.DataBean daiQuHuo){
        Bundle bundle = new Bundle();
        bundle.putSerializable("daiQuHuo",daiQuHuo);
        IntentUtil.openActivityForResult(context, QianWangQuCanActivity.class,-1,bundle);
    }

    class ViewHolder {
        @BindView(R.id.price_tv)
        TextView mPriceTV;
        @BindView(R.id.qh_address_tv)
        TextView mQhAddressTV;
        @BindView(R.id.sh_address_tv)
        TextView mShAddressTV;
        @BindView(R.id.qwqcBtn)
        CardView mQwqcBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
