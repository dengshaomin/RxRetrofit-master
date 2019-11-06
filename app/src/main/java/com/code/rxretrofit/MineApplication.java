package com.code.rxretrofit;

import android.app.Application;
import android.content.Context;

import com.code.rxretrofitlibrary.http.HttpUtils;

public class MineApplication extends Application {

    public static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationContext = base;
        HttpUtils.init(applicationContext);
    }
}
