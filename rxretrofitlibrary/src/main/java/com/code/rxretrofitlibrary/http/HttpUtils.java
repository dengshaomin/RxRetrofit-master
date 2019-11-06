package com.code.rxretrofitlibrary.http;


import android.content.Context;
import android.util.Log;

import com.code.rxretrofitlibrary.http.cb.HttpCallBack;
import com.code.rxretrofitlibrary.http.exception.ExceptionType;
import com.code.rxretrofitlibrary.http.exception.HttpException;
import com.code.rxretrofitlibrary.http.maps.ReponseMap;
import com.code.rxretrofitlibrary.http.observers.ReponseObserver;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.RxActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragmentActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class HttpUtils {

    private static String TAG = "HttpUtils";

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage());
            }
        });
        RetrofitHttpUtil.setmApplicationContext(context.getApplicationContext() == null ? context:context.getApplicationContext());
    }

    public static void execute(Context context, Observable observable, HttpCallBack httpCallBack) {
        if (context == null || observable == null) {
            if (httpCallBack != null) {
                httpCallBack.onError(new HttpException(ExceptionType.DEFAULT, ExceptionType.DEFAULT, ""));
            }
            return;
        }
        observable = transformObservable(context, observable);
        observable.subscribe(new ReponseObserver(context, httpCallBack, false));
    }

    public static void executeDialog(Context context, Observable observable, HttpCallBack httpCallBack) {
        if (context == null || observable == null) {
            if (httpCallBack != null) {
                httpCallBack.onError(new HttpException(ExceptionType.DEFAULT, ExceptionType.DEFAULT, ""));
            }
            return;
        }
        observable = transformObservable(context, observable);
        observable.subscribe(new ReponseObserver(context, httpCallBack, true));

    }

    private static Observable transformObservable(Context context, Observable observable) {
        observable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        if (context instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxActivity) {
            observable = observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxFragmentActivity) {
            observable = observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }
        observable = observable.map(new ReponseMap());
        return observable;
    }

}
