package com.hualing.rider.entity;

import java.io.Serializable;

public class RiderEntity {

    private int code;
    private String message;
    private RiderEntity.DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private Integer riderID;//主键
        private String phone;//用户名（手机号）
        private String password;//密码
        private String trueName;//真实姓名
        private String cardID;//身份证号
        private String bankCardID;//银行卡号
        private String createTime;//注册时间
        private String state;//状态（0未审核、1已审核）
        public Integer getRiderID() {
            return riderID;
        }
        public void setRiderID(Integer riderID) {
            this.riderID = riderID;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getTrueName() {
            return trueName;
        }
        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
        public String getCardID() {
            return cardID;
        }
        public void setCardID(String cardID) {
            this.cardID = cardID;
        }
        public String getBankCardID() {
            return bankCardID;
        }
        public void setBankCardID(String bankCardID) {
            this.bankCardID = bankCardID;
        }
        public String getCreateTime() {
            return createTime;
        }
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getState() {
            return state;
        }
        public void setState(String state) {
            this.state = state;
        }
    }
}
