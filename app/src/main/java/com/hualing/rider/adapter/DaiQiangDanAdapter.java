package com.hualing.rider.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.rider.R;
import com.hualing.rider.activities.DaiQiangDanDetailActivity;
import com.hualing.rider.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaiQiangDanAdapter extends BaseAdapter {

    private List<String> mData;
    private Activity context;

    public DaiQiangDanAdapter(Activity context){
        this.context = context;
        mData = new ArrayList<String>();
        mData.add("aaa");
        mData.add("bbb");
        mData.add("bbb");
        mData.add("bbb");
        mData.add("bbb");
        mData.add("bbb");
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

        View layout1 = convertView.findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail();
            }
        });
        View layout2 = convertView.findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail();
            }
        });
        View layout3 = convertView.findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail();
            }
        });
        View qiangdanBtn = convertView.findViewById(R.id.qiangdanBtn);
        qiangdanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private void goDetail(){
        IntentUtil.openActivityForResult(context, DaiQiangDanDetailActivity.class,-1,null);
    }

    class ViewHolder {
        /*
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.dh)
        TextView mDh;
        @BindView(R.id.date)
        TextView mDate;
        */

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
