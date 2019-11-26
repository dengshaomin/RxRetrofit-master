package com.code.rxretrofitlibrary.http.observers;

import java.lang.ref.SoftReference;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.code.rxretrofitlibrary.http.cb.HttpCallBack;
import com.code.rxretrofitlibrary.http.dialog.HttpDialog;
import com.code.rxretrofitlibrary.http.exception.ExceptionType;
import com.code.rxretrofitlibrary.http.exception.HttpException;
import com.code.rxretrofitlibrary.utils.NetWorkUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ReponseObserver<T> implements Observer<T> {

    private HttpCallBack mHttpCallBack;

    private SoftReference<Context> mContextSoftReference;

    private boolean showDialog = false;

    private HttpDialog mHttpDialog;

    public ReponseObserver(Context context, HttpCallBack httpCallBack) {
        this.mHttpCallBack = httpCallBack;
        mContextSoftReference = new SoftReference<>(context);
    }

    public ReponseObserver(Context context, HttpCallBack httpCallBack, boolean showDialog) {
        this.mHttpCallBack = httpCallBack;
        this.mContextSoftReference = new SoftReference<>(context);
        this.showDialog = showDialog;

    }

    private void initDialog() {
        if (mHttpDialog == null) {
            mHttpDialog = new HttpDialog(mContextSoftReference.get());
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (showDialog) {
            initDialog();
            showDialog();
        }
    }

    private void showDialog() {
        if (mContextSoftReference.get() != null && mHttpDialog != null && !mHttpDialog.isShowing()) {
            if (mContextSoftReference.get() instanceof Activity && ((Activity) mContextSoftReference.get()).isFinishing()) {
                return;
            }
            if (mHttpDialog == null) {
                mHttpDialog = new HttpDialog(mContextSoftReference.get());
            }
            try {
                mHttpDialog.show();
            } catch (Exception e) {
            }
        }
    }

    private void dismissDialog() {
        if (mHttpDialog != null && mHttpDialog.isShowing()) {
            mHttpDialog.dismiss();
        }
    }

    @Override
    public void onNext(T t) {
        if (mHttpCallBack != null) {
            mHttpCallBack.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mHttpCallBack == null) {
            return;
        }
        if (e == null) {
            e = new HttpException(NetWorkUtils.off(mContextSoftReference.get()) ? ExceptionType.NET : ExceptionType.SERVER
                    , ExceptionType.DEFAULT, "unkonw~");
        } else if (!(e instanceof HttpException)) {
            e = new HttpException(NetWorkUtils.off(mContextSoftReference.get()) ? ExceptionType.NET : ExceptionType.SERVER
                    , ExceptionType.DEFAULT, TextUtils.isEmpty(e.getMessage()) ? "unkonw~" : e.getMessage());
        }
        mHttpCallBack.onError((HttpException) e);
        dismissDialog();
    }

    @Override
    public void onComplete() {
        dismissDialog();
    }
}
