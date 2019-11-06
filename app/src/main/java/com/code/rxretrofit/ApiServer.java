package com.code.rxretrofit;


import java.util.Map;


import com.code.rxretrofitlibrary.http.headers.CacheHeaders;
import com.code.rxretrofitlibrary.http.models.ServerModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface ApiServer {

    @Headers(CacheHeaders.NO)
    @GET("test")
    Observable<ServerModel<TestModel>> testApi(@QueryMap Map<String, String> params);

    @GET("test")
    Call<ServerModel<TestModel>> testApi1(@QueryMap Map<String, String> params);

}
