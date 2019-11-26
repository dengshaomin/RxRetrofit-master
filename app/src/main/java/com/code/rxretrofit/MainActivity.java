package com.code.rxretrofit;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.code.rxretrofitlibrary.http.HttpUtils;
import com.code.rxretrofitlibrary.http.RetrofitHttpUtil;
import com.code.rxretrofitlibrary.http.cb.HttpCallBack;
import com.code.rxretrofitlibrary.http.exception.HttpException;
import com.code.rxretrofitlibrary.http.models.ServerModel;

public class MainActivity extends AppCompatActivity {

    private ApiServer mApiServer;

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.get).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }

    private void request() {
        if (mApiServer == null) {
            mApiServer = RetrofitHttpUtil.createServerApi(ApiServer.class, "http://www.baidu.com?");
        }
        HttpUtils.executeObjectDialog(this, mApiServer.testApi1(new HashMap<String, String>()), new HttpCallBack<TestModel>() {
            @Override
            public void onSuccess(TestModel data) {
                Log.e(TAG, JSON.toJSONString(data));
            }

            @Override
            public void onError(HttpException httpException) {
                Log.e(TAG, JSON.toJSONString(httpException));
            }
        });
    }
}
