package com.code.rxretrofit.http.cb;


import com.code.rxretrofit.http.exception.HttpException;

public interface HttpCallBack<T> {

    void onSuccess(T data);

    void onError(HttpException httpException);
}
