package com.hualing.rider.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.route.PlanNode;
import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.activities.MainActivity;
import com.hualing.rider.activities.QianWangQuCanActivity;
import com.hualing.rider.entity.DaiQuHuoEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.model.DaiQuHuoNode;
import com.hualing.rider.util.IntentUtil;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaiQuHuoAdapter extends BaseAdapter {

    private List<DaiQuHuoEntity.DataBean> mData;
    private List<DaiQuHuoNode> dqhNodeList;

    public List<DaiQuHuoNode> getDqhNodeList() {
        return dqhNodeList;
    }

    public void setDqhNodeList(List<DaiQuHuoNode> dqhNodeList) {
        this.dqhNodeList = dqhNodeList;
    }

    public List<DaiQuHuoEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<DaiQuHuoEntity.DataBean> mData) {
        this.mData = mData;
    }

    private MainActivity context;
    private String loaclcity = null;
    private DecimalFormat decimalFormat=new DecimalFormat("0.0");
    public static int jiSuanPosition=0;

    public DaiQuHuoAdapter(MainActivity context){
        this.context = context;
        mData = new ArrayList<DaiQuHuoEntity.DataBean>();
        dqhNodeList = new ArrayList<DaiQuHuoNode>();
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
                    initDaiQuHuoNode(mData);
                    jiSuanDaiQiangDanKm();
                }
            }
        });
    }

    /**
     * 初始化待抢单地点
     */
    public void initDaiQuHuoNode(List<DaiQuHuoEntity.DataBean> dqdList){
        DaiQuHuoNode qhNode = null;
        DaiQuHuoNode shNode = null;
        int dqdListSize = dqdList.size();
        for (int i = 0; i<dqdListSize; i++) {
            DaiQuHuoEntity.DataBean dataBean = dqdList.get(i);
            qhNode = new DaiQuHuoNode(this);
            qhNode.setQhStNode(PlanNode.withCityNameAndPlaceName(loaclcity, "山东省青岛市黄岛区隐珠镇向阳岭路7号"));
            qhNode.setQhEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, dataBean.getQhAddress()));
            qhNode.setOrderNumber(dataBean.getOrderNumber());
            //Log.e("qcNode111==",qcNode.getOrderNumber());
            dqhNodeList.add(qhNode);

            shNode = new DaiQuHuoNode(this);
            shNode.setShStNode(PlanNode.withCityNameAndPlaceName(loaclcity, dataBean.getQhAddress()));
            shNode.setShEnNode(PlanNode.withCityNameAndPlaceName(loaclcity, "双珠路288号东方金石"));
            shNode.setOrderNumber(dataBean.getOrderNumber());
            //Log.e("qcNode222==",shNode.getOrderNumber()+"");
            dqhNodeList.add(shNode);
            if(i==5)
                break;
        }
    }

    public void jiSuanDaiQiangDanKm(){
        int dqdSize = dqhNodeList.size();
        for (int i = 0;i<dqdSize; i++) {
            DaiQuHuoNode dqhNode = dqhNodeList.get(i);
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
        holder.mSyTimeTV.setText(decimalFormat.format((float)(dataBean.getQhSyTime()+dataBean.getShSyTime()))+"分钟内送达");
        holder.mPriceTV.setText("￥"+dataBean.getPrice());
        holder.mToQhdjlTV.setText(decimalFormat.format(dataBean.getToQhdjl()));
        holder.mToShdjlTV.setText(decimalFormat.format(dataBean.getToShdjl()));
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
        @BindView(R.id.qwqcBtn)
        CardView mQwqcBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
