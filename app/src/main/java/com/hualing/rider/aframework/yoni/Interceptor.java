package com.hualing.rider.aframework.yoni;

public interface Interceptor {
	boolean intercept(RequestParams params, NetResponse result);
}
