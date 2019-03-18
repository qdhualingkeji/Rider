package com.hualing.rider.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.hualing.rider.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.phoneValue)
    EditText phoneValue;
    @BindView(R.id.trueNameValue)
    EditText trueNameValue;
    @BindView(R.id.cardIDValue)
    EditText cardIDValue;
    @BindView(R.id.passwordValue)
    EditText passwordValue;
    @BindView(R.id.password2Value)
    EditText password2Value;
    @BindView(R.id.codeValue)
    EditText codeValue;
    @BindView(R.id.registerBtn)
    CardView registerBtn;

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
        return R.layout.activity_register;
    }

    @OnClick({R.id.registerBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.registerBtn:
                if(checkIfInfoPerfect()){
                    commitDataToWeb();
                }
                break;
        }
    }

    /**
     * 验证信息是否合法
     * @return
     */
    private boolean checkIfInfoPerfect(){
        if(checkPhoneValue()){
            if(checkTrueNameValue()){
                if(checkCardIDValue()){
                    if(checkPasswordValue()) {
                        if(checkPassword2Value()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
    * 验证手机号
    * */
    private boolean checkPhoneValue(){
        String phone = phoneValue.getText().toString();
        if(TextUtils.isEmpty(phone)){
            MyToast("手机号不能为空");
            return false;
        }
        else{
            return true;
        }
    }

    /*
    * 验证真实姓名
    * */
    private boolean checkTrueNameValue(){
        String trueName = trueNameValue.getText().toString();
        if(TextUtils.isEmpty(trueName)){
            MyToast("真实姓名不能为空");
            return false;
        }
        else{
            return true;
        }
    }

    /*
    * 验证身份证号
    * */
    private boolean checkCardIDValue(){
        String cardID = cardIDValue.getText().toString();
        if(TextUtils.isEmpty(cardID)){
            MyToast("身份证号不能为空");
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * 验证密码
     * @return
     */
    private boolean checkPasswordValue(){
        String password = passwordValue.getText().toString();
        if(TextUtils.isEmpty(password)){
            MyToast("密码不能为空");
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * 验证确认密码
     * @return
     */
    private boolean checkPassword2Value(){
        String password = passwordValue.getText().toString();
        String password2 = password2Value.getText().toString();
        if(TextUtils.isEmpty(password2)){
            MyToast("确认密码不能为空");
            return false;
        }
        if(!password.equals(password2)){
            MyToast("两次密码不一致");
            return false;
        }
        else{
            return true;
        }
    }

    private void commitDataToWeb(){
        String phone = phoneValue.getText().toString();
        String trueName = trueNameValue.getText().toString();
        String cardID = cardIDValue.getText().toString();
        String password = passwordValue.getText().toString();
        RequestParams params = AsynClient.getRequestParams();
        params.put("phone", phone);
        params.put("trueName", trueName);
        params.put("cardID", cardID);
        params.put("password", password);
        Gson gson = new Gson();

        AsynClient.post(MyHttpConfing.registered, this, params, new GsonHttpResponseHandler() {
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
                    MyToast(successEntity.getMessage());
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(RegisterActivity.this);
                }
                else {
                    MyToast(successEntity.getMessage());
                }

            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
