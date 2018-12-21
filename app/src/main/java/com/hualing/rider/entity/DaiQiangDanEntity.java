package com.hualing.rider.entity;

import java.io.Serializable;
import java.util.List;

public class DaiQiangDanEntity {

	private int code;
	private String message;
	private List<DataBean> data;

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

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean implements Serializable {

		private String qcShopName;
		private float toQcdjl;

		public float getToQcdjl() {
			return toQcdjl;
		}

		public void setToQcdjl(float toQcdjl) {
			this.toQcdjl = toQcdjl;
		}

		public float getToScdjl() {
			return toScdjl;
		}

		public void setToScdjl(float toScdjl) {
			this.toScdjl = toScdjl;
		}

		private float toScdjl;
		public String getQcShopName() {
			return qcShopName;
		}
		public void setQcShopName(String qcShopName) {
			this.qcShopName = qcShopName;
		}
		public String getQcAddress() {
			return qcAddress;
		}
		public void setQcAddress(String qcAddress) {
			this.qcAddress = qcAddress;
		}
		public String getScAddress() {
			return scAddress;
		}
		public void setScAddress(String scAddress) {
			this.scAddress = scAddress;
		}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		private String qcAddress;
		private String scAddress;
		private String orderNumber;

		public float getSyTime() {
			return syTime;
		}

		public void setSyTime(float syTime) {
			this.syTime = syTime;
		}

		private float syTime;
	}
}
