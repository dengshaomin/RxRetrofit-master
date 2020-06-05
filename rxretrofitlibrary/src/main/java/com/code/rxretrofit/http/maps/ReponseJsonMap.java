package com.code.rxretrofit.http.maps;


import io.reactivex.functions.Function;

public class ReponseJsonMap<T> implements Function<T, T> {

//    public ReponseJsonMap(Observable observable) {
//        if(observable == null) return;
//        Type mySuperClass = observable.getClass().getGenericSuperclass();
//        Type type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[0];
//        type.toString()
//        System.out.println(type);
//    }

    @Override
    public T apply(T strs) throws Exception {
        return strs;
    }
}
