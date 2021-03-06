package com.code.retrofitdemo;


import java.util.Map;


import com.code.retrofit.http.headers.CacheHeaders;
import com.code.retrofit.http.models.ServerModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public  interface ApiServer {

    @Headers(CacheHeaders.NO)
    @GET("/query")
    Observable<String> excuteJson(@QueryMap Map<String, String> params);

    @GET("/query")
    Observable<ServerModel<TestModel>> excuteObject(@QueryMap Map<String, String> params);
}
