package com.read.chajian.api.callback;


import com.read.chajian.api.exception.ApiException;

public interface ResultInterface<T>{

    void onSucc(T result);

    void onFail(ApiException apiError);

}
