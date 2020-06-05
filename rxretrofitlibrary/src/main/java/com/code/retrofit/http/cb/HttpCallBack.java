package com.code.retrofit.http.cb;


import com.code.retrofit.http.exception.HttpException;

public interface HttpCallBack<T> {

    void onSuccess(T data);

    void onError(HttpException httpException);
}
