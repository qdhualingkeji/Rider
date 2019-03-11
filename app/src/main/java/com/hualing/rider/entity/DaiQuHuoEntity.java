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
        private float toQhdjl;
        private float toShdjl;

        public float getToQhdjl() {
            return toQhdjl;
        }

        public void setToQhdjl(float toQhdjl) {
            this.toQhdjl = toQhdjl;
        }

        public float getToShdjl() {
            return toShdjl;
        }

        public void setToShdjl(float toShdjl) {
            this.toShdjl = toShdjl;
        }

        private float qhSyTime;
        private float shSyTime;

        public float getQhSyTime() {
            return qhSyTime;
        }

        public void setQhSyTime(float qhSyTime) {
            this.qhSyTime = qhSyTime;
        }

        public float getShSyTime() {
            return shSyTime;
        }

        public void setShSyTime(float shSyTime) {
            this.shSyTime = shSyTime;
        }
        private double qhLongitude;
        private double qhLatitude;
        private double shLongitude;
        private double shLatitude;

        public double getQhLongitude() {
            return qhLongitude;
        }

        public void setQhLongitude(double qhLongitude) {
            this.qhLongitude = qhLongitude;
        }

        public double getQhLatitude() {
            return qhLatitude;
        }

        public void setQhLatitude(double qhLatitude) {
            this.qhLatitude = qhLatitude;
        }

        public double getShLongitude() {
            return shLongitude;
        }

        public void setShLongitude(double shLongitude) {
            this.shLongitude = shLongitude;
        }

        public double getShLatitude() {
            return shLatitude;
        }

        public void setShLatitude(double shLatitude) {
            this.shLatitude = shLatitude;
        }
    }
}
