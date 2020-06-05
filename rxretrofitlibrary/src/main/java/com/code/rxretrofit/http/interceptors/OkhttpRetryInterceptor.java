package com.code.rxretrofit.http.interceptors;

import java.io.IOException;
import java.io.InterruptedIOException;

import com.code.rxretrofit.http.Config;
import com.code.rxretrofit.http.Config.OkhttpRetry;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpRetryInterceptor implements Interceptor {

    private int executionCount;//最大重试次数

    private long retryInterval;//重试的间隔

    public OkhttpRetryInterceptor(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int retryNum = 0;
        while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
            final long nextInterval = getRetryInterval();
            try {
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            }
            retryNum++;
            // retry the request
            response = doRequest(chain, request);
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
        }
        return response;
    }

    /**
     * retry间隔时间
     */
    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {

        private int executionCount;

        private long retryInterval;

        public Builder() {
            executionCount = Config.OkhttpRetry.executionCount;
            retryInterval = OkhttpRetry.retryInterval;
        }

        public OkhttpRetryInterceptor.Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public OkhttpRetryInterceptor.Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public OkhttpRetryInterceptor build() {
            return new OkhttpRetryInterceptor(this);
        }
    }

}

