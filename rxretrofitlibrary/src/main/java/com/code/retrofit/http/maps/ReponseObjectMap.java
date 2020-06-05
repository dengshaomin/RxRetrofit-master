package com.code.retrofit.http.maps;


import com.code.retrofit.http.models.ServerModel;

import io.reactivex.functions.Function;

public class ReponseObjectMap<T> implements Function<ServerModel<T>, T> {

    @Override
    public T apply(ServerModel<T> tServerModel) throws Exception {

        return tServerModel != null ? tServerModel.getData() : null;
    }
}
