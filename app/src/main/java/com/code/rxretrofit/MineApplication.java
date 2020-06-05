package com.code.rxretrofit;

import android.app.Application;
import android.content.Context;

public class MineApplication extends Application {

    public static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationContext = base;
    }
}
