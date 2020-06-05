package com.code.rxretrofit.http.models;

import java.io.Serializable;

public class ServerModel<T> implements Serializable {

    public static final String SUCCESS_CODE = "A00000";

    private String code;

    private T data;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
