package com.read.chajian.api.callback;

import com.lzy.okgo.model.Response;
import com.read.chajian.api.exception.ApiException;
import com.read.chajian.api.model.BaseResponse;
import com.read.chajian.util.ToastUtil;


import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by bin on 17/6/4.
 */

public abstract class BaseCallback<T> extends JsonCallback<T> implements ResultInterface<T> {

    public abstract void onSucc(T result);


    public void onFail(ApiException apiError) {
        ToastUtil.showErrorToast(apiError.getMessage());
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response.body() instanceof BaseResponse) {
            onSucc(response.body());
        } else {
            ApiException apiError = new ApiException(-1, "模型错误");
            onFail(apiError);
        }
    }

    @Override
    public void onError(Response<T> response) {
        if (response.getException() instanceof ApiException) {
            onFail((ApiException) response.getException());
        } else {
//            int errCode = CommErrorCode.ERROR_CODE_UNKNOWN;
//            String errMsg = "网络错误，请检查网络是否正常";
//            if (response.getException() != null) {
//                if (response.getRawResponse() != null) {
//                    errCode = response.getRawResponse().code();
//                    errMsg = response.getRawResponse().message();
//                } else if (response.getException() instanceof ConnectException) {
//
//                    String msg = response.getException().getLocalizedMessage();
//                    errCode = CommErrorCode.ERROR_CODE_LOCAL_NETWORK;
//                    errMsg = "无法连接服务器，请检查网络是否正常";
//                } else if (response.getException() instanceof SocketTimeoutException) {
//                    errCode = CommErrorCode.ERROR_CODE_LOCAL_NETWORK;
//                    errMsg = "连接服务器超时";
//                }
//            } else {
//                if (response.getRawResponse() != null) {
//                    errCode = response.getRawResponse().code();
//                    errMsg = response.getRawResponse().message();
//                }
//            }
//
//            ApiException apiError = new ApiException(errCode, errMsg);
//            onFail(apiError);
        }
    }


}
