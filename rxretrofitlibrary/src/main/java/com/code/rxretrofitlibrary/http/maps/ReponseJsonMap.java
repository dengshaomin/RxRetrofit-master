package com.code.rxretrofitlibrary.http.maps;


import com.code.rxretrofitlibrary.http.models.ServerModel;

import io.reactivex.functions.Function;

public class ReponseJsonMap<T> implements Function<T, T> {


    @Override
    public T apply(T strs) throws Exception {

        return strs;
    }
}
