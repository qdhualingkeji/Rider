package com.hualing.rider.entity;

import java.io.Serializable;
import java.util.List;

public class DaiQuHuoEntity {

    private int code;
    private String message;
    private List<DaiQuHuoEntity.DataBean> data;

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

    public List<DaiQuHuoEntity.DataBean> getData() {
        return data;
    }

    public void setData(List<DaiQuHuoEntity.DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String qhAddress;
        private String price;

        public String getQhAddress() {
            return qhAddress;
        }

        public void setQhAddress(String qhAddress) {
            this.qhAddress = qhAddress;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShAddress() {
            return shAddress;
        }

        public void setShAddress(String shAddress) {
            this.shAddress = shAddress;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        private String shAddress;
        private String orderNumber;
    }
}
