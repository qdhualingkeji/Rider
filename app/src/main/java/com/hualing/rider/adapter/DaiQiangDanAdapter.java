package com.hualing.rider.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.rider.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        return convertView;
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
