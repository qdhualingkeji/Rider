package com.hualing.rider.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.entity.RiderEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.util.AllActivitiesHolder;
import com.hualing.rider.util.IntentUtil;
import com.hualing.rider.util.SharedPreferenceUtil;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends BaseActivity {

    private static final long DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                if (SharedPreferenceUtil.getUserType()== UserType.NON_SELECTED) {
//                    IntentUtil.openActivity(LaunchActivity.this,UserTypePickActivity.class);
//                }else if(SharedPreferenceUtil.getUserType()== UserType.EMPLOYEE){
                if (SharedPreferenceUtil.ifHasLocalRiderInfo()) {
                    //测试
//                        IntentUtil.openActivity(LaunchActivity.this,EmployeeMainActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toLogin();
                        }
                    });
                }else {
                    IntentUtil.openActivity(LaunchActivity.this,MainActivity.class);
                    AllActivitiesHolder.removeAct(LaunchActivity.this);
                }
//                }else if (SharedPreferenceUtil.getUserType()== UserType.GUEST){
//                    IntentUtil.openActivity(LaunchActivity.this,GuestMainActivity.class);
//                }
            }
        },DELAY);
    }

    private void toLogin(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("phone", SharedPreferenceUtil.getRiderInfo()[0]);
        params.put("password", SharedPreferenceUtil.getRiderInfo()[1]);

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/"+ GlobalData.Service.LOGIN, this, params, new GsonHttpResponseHandler() {
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
                RiderEntity riderEntity = gson.fromJson(rawJsonResponse, RiderEntity.class);
                if (riderEntity.getCode() == 100) {
                    RiderEntity.DataBean riderData = riderEntity.getData();
                    GlobalData.riderID = riderData.getRiderID();
                    GlobalData.phone = riderData.getPhone();
                    GlobalData.password = riderData.getPassword();
                    GlobalData.trueName = riderData.getTrueName();

                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    MyToast(riderEntity.getMessage());
                    IntentUtil.openActivity(LaunchActivity.this,LoginActivity.class);
                }
                AllActivitiesHolder.removeAct(LaunchActivity.this);

            }
        });
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }

    public void MyToast(String s) {
        Toast.makeText(LaunchActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
