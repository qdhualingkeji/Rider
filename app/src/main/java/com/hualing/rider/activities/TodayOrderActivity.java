package com.hualing.rider.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hualing.rider.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TodayOrderActivity extends BaseActivity {

    @BindView(R.id.todayOrderSpinner)
    Spinner mTodayOrderSpinner;
    private List<String> todayOrderList;
    private ArrayAdapter<String> todayOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        todayOrderList = new ArrayList<String>();
        todayOrderList.add("今日订单");
        todayOrderAdapter=new ArrayAdapter<String>(this,R.layout.item_today_order,todayOrderList);
        todayOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTodayOrderSpinner.setAdapter(todayOrderAdapter);
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_today_order;
    }
}
