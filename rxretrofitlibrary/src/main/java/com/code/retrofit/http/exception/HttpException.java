package com.code.retrofit.http.exception;

public class HttpException extends Throwable{

    private int type;

    private int code;

    private String msg;

    public HttpException(int type, int code, String msg) {
        this.type = type;
        this.code = code;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
