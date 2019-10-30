package com.hualing.rider.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.DaiQiangDanDetailActivity;
import com.hualing.rider.entity.DaiQiangDanDetailEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.hualing.rider.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DQDProductAdapter extends BaseAdapter {

    private List<DaiQiangDanDetailEntity.DataBean> mData;
    private DaiQiangDanDetailActivity activity;

    public DQDProductAdapter(DaiQiangDanDetailActivity activity){
        this.activity = activity;
        mData = new ArrayList<DaiQiangDanDetailEntity.DataBean>();
    }

    public void setNewData(String orderNumber){

        RequestParams params = AsynClient.getRequestParams();
        //params.put("orderNumber","1801233613912727");
        params.put("orderNumber",orderNumber);
        Log.e("orderNumber======",orderNumber);
        Gson gson = new Gson();

        AsynClient.post(MyHttpConfing.getDaiQiangDanDetail,activity,params,new GsonHttpResponseHandler(){

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
                DaiQiangDanDetailEntity entity = gson.fromJson(rawJsonResponse, DaiQiangDanDetailEntity.class);
                if (entity.getCode() == 100) {
                    mData = entity.getData();
                    notifyDataSetChanged();
                    activity.jiSuanPrice();
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
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = activity.getLayoutInflater().inflate(R.layout.item_dqd_product,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        DaiQiangDanDetailEntity.DataBean product = mData.get(position);
        holder.mProductNameTV.setText(product.getProductName());
        holder.mQuantityTV.setText(String.valueOf(product.getQuantity()));

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.product_name_tv)
        TextView mProductNameTV;
        @BindView(R.id.quantity_tv)
        TextView mQuantityTV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
