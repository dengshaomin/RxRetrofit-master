package com.code.rxretrofit.http;


import android.content.Context;
import android.util.Log;

import com.code.rxretrofit.http.cb.HttpCallBack;
import com.code.rxretrofit.http.exception.ExceptionType;
import com.code.rxretrofit.http.exception.HttpException;
import com.code.rxretrofit.http.maps.ReponseJsonMap;
import com.code.rxretrofit.http.maps.ReponseObjectMap;
import com.code.rxretrofit.http.observers.ReponseObserver;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class HttpUtils {

    private static String TAG = "HttpUtils";

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, String mainHost) {
        if (context == null) {
            throw new IllegalArgumentException("context not be null!");
        }
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage());
            }
        });
        ApiFactory.init(context, mainHost);
    }


    /**
     * 返回json str,HttpCallBack<string> and API:Observable<String>
     */
    public static void execute(Context context, Observable observable, HttpCallBack httpCallBack) {
        if (context == null || observable == null) {
            if (httpCallBack != null) {
                httpCallBack.onError(new HttpException(ExceptionType.DEFAULT, ExceptionType.DEFAULT, ""));
            }
            return;
        }
        observable = transformObservableJson(context, observable);
        observable.subscribe(new ReponseObserver(context, httpCallBack, false));
    }
    /**
     * 返回指定的数据结构
     * 展示加载框
     * 如接口还在请求此时关闭activity，此方法会发生crash：MainActivity has leaked window DecorView@83f150[] that was originally added here
     * */
//    public static void executeObjectDialog(Context context, Observable observable, HttpCallBack httpCallBack) {
//        if (context == null || observable == null) {
//            if (httpCallBack != null) {
//                httpCallBack.onError(new HttpException(ExceptionType.DEFAULT, ExceptionType.DEFAULT, ""));
//            }
//            return;
//        }
//        observable = transformObservableObject(context, observable);
//        observable.subscribe(new ReponseObserver(context, httpCallBack, true));
//    }

    /**
     * 返回json str,HttpCallBack<string> and API:Observable<String>
     * 展示加载框
     */
//    public static void executeJsonDialog(Context context, Observable observable, HttpCallBack httpCallBack) {
//        if (context == null || observable == null) {
//            if (httpCallBack != null) {
//                httpCallBack.onError(new HttpException(ExceptionType.DEFAULT, ExceptionType.DEFAULT, ""));
//            }
//            return;
//        }
//        observable = transformObservableJson(context, observable);
//        observable.subscribe(new ReponseObserver(context, httpCallBack, true));
//
//    }
    private static Observable transformObservableObject(Context context, Observable observable) {
        observable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        if (context instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxActivity) {
            observable = observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxFragmentActivity) {
            observable = observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }
        observable = observable.map(new ReponseObjectMap());
        return observable;
    }

    private static Observable transformObservableJson(Context context, Observable observable) {
        observable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        if (context instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxActivity) {
            observable = observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxFragmentActivity) {
            observable = observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }
        observable = observable.map(new ReponseJsonMap());
        return observable;
    }

}
