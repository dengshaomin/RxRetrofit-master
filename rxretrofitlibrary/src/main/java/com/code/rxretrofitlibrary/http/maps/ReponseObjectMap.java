package com.code.rxretrofitlibrary.http.maps;


import com.code.rxretrofitlibrary.http.models.ServerModel;

import io.reactivex.functions.Function;

public class ReponseObjectMap<T> implements Function<ServerModel<T>, T> {


    @Override
    public T apply(ServerModel<T> tServerModel) throws Exception {

        return tServerModel != null ? tServerModel.getData() : null;
    }
}
