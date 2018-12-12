package com.hualing.rider.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hualing.rider.R;
import com.hualing.rider.entity.SuccessEntity;
import com.hualing.rider.global.GlobalData;
import com.hualing.rider.util.AllActivitiesHolder;
import com.hualing.rider.utils.AsynClient;
import com.hualing.rider.utils.GsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.phoneValue)
    EditText phoneValue;
    @BindView(R.id.passwordValue)
    EditText passwordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.loginBtn,R.id.registerBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.loginBtn:
                String phone = phoneValue.getText().toString();
                String password = passwordValue.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    MyToast("请输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    MyToast("请输入密码");
                    return;
                }
                login(phone,password);
                break;
            case R.id.registerBtn:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                AllActivitiesHolder.removeAct(this);
                break;
        }
    }

    /***
     * 用户登录
     * @param phone
     * @param password
     */
    private void login(String phone, String password){
        RequestParams params = AsynClient.getRequestParams();
        params.put("phone", phone);
        params.put("password", password);
        Gson gson = new Gson();

        AsynClient.post("http://120.27.5.36:8080/htkApp/API/riderAPI/"+GlobalData.Service.LOGIN, this, params, new GsonHttpResponseHandler() {
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
                SuccessEntity successEntity = gson.fromJson(rawJsonResponse, SuccessEntity.class);
                if (successEntity.getCode() == 100) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(LoginActivity.this);
                }
                else {
                    MyToast(successEntity.getMessage());
                }

            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
