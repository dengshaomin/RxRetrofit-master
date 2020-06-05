package com.code.rxretrofit.http.interceptors;

import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;


import com.code.rxretrofit.http.caches.CacheManager;
import com.code.rxretrofit.http.caches.CacheType;
import com.code.rxretrofit.http.headers.CacheHeaders;

import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CacheInterceptor implements Interceptor {

    private Context context;

    public CacheInterceptor(Context context) {
        this.context = context.getApplicationContext() == null ? context:context.getApplicationContext();
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();
        String cacheHead = request.header(CacheHeaders.HEADER_FILED);
        if (TextUtils.isEmpty(cacheHead)) {
            cacheHead = CacheHeaders.DEFAULT;
        }
        if (!TextUtils.equals(cacheHead, CacheHeaders.STATUS.NO)) {
            final String url = request.url().url().toString();
            String responStr = null;
//            String reqBodyStr = getPostParams(request);
            if (TextUtils.equals(cacheHead, CacheHeaders.STATUS.NORMAL)) {
                Response response = getCacheResponse(request);
                if (response != null) {
                    asyncRequestNet(request, chain);
                    return response;
                }
            }
            try {
                Response response = chain.proceed(request);
                // 只有在网络请求返回成功之后，才进行缓存处理
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        responStr = responseBody.string();
                        if (!TextUtils.isEmpty(responStr)) {
                            //存缓存，以链接+参数进行MD5编码为KEY存
                            CacheManager.getInstance(context).setCache(CacheManager.encryptMD5(url), responStr);
                        }
//                        CacheManager.getInstance(context).setCache(CacheManager.encryptMD5(url + reqBodyStr), responStr);//存缓存，以链接+参数进行MD5编码为KEY存
                    }
                    return getOnlineResponse(response, responStr);
                } else {
                    return chain.proceed(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 发生异常了，这里就开始去缓存，但是有可能没有缓存，那么就需要丢给下一轮处理了
                Response response = null;
                if (TextUtils.equals(cacheHead, CacheHeaders.STATUS.ERROR) || TextUtils.equals(cacheHead, CacheHeaders.STATUS.NORMAL)) {
                    response = getCacheResponse(request);
                }
                if (response == null) {
                    //丢给下一轮处理
                    return chain.proceed(request);
                } else {
                    return response;
                }
            }
        } else {
            return chain.proceed(request);
        }
    }

    private void asyncRequestNet(final Request request, final Chain chain) {
        Schedulers.newThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String responStr = responseBody.string();
                            if (!TextUtils.isEmpty(responStr)) {
                                CacheManager.getInstance(context)
                                        .setCache(CacheManager.encryptMD5(request.url().toString()), responStr);//存缓存，以链接+参数进行MD5编码为KEY存
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Response getCacheResponse(Request request) {
        String url = request.url().url().toString();
//        String params = getPostParams(request);
//        String cacheStr = CacheManager.getInstance(context).getCache(CacheManager.encryptMD5(url + params));//取缓存，以链接+参数进行MD5编码为KEY取
        String cacheStr = CacheManager.getInstance(context).getCache(CacheManager.encryptMD5(url));//取缓存，以链接+参数进行MD5编码为KEY取
        if (cacheStr == null) {
            return null;
        }
        Response response = new Response.Builder()
                .code(200)
                .body(ResponseBody.create(null, cacheStr))
                .request(request)
                .message(CacheType.DISK_CACHE)
                .protocol(Protocol.HTTP_1_1)
                .build();
        return response;
    }

    private Response getOnlineResponse(Response response, String body) {
        ResponseBody responseBody = response.body();
        return new Response.Builder()
                .code(response.code())
                .body(ResponseBody.create(responseBody == null ? null : responseBody.contentType(), body))
                .request(response.request())
                .message(response.message())
                .protocol(response.protocol())
                .build();
    }

    /**
     * 获取在Post方式下。向服务器发送的参数
     */
    private String getPostParams(Request request) {
        String reqBodyStr = "";
        String method = request.method();
        if ("POST".equals(method)) // 如果是Post，则尽可能解析每个参数
        {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i)).append(",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                }
                reqBodyStr = sb.toString();
                sb.delete(0, sb.length());
            }
        }
        return reqBodyStr;
    }
}

