package com.hualing.rider.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hualing.rider.R;

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
                break;
        }
    }

    private boolean checkIfInfoPerfect(){
        if(checkPhoneValue()){
            if(checkTrueNameValue()){
                if(checkCardIDValue()){
                    return true;
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

    private void commitDataToWeb(){

    }

    public void MyToast(String s) {
        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
