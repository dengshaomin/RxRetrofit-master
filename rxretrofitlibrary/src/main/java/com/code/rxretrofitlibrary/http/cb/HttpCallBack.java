package com.code.rxretrofitlibrary.http.cb;


import com.code.rxretrofitlibrary.http.exception.HttpException;

public interface HttpCallBack<T> {

    void onSuccess(T data);

    void onError(HttpException httpException);
}
