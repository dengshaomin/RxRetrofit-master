package com.code.retrofit.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.text.TextUtils;

import com.code.retrofit.http.interceptors.CacheInterceptor;
import com.code.retrofit.http.interceptors.OkhttpRetryInterceptor;
import com.code.retrofit.http.convertfactory.json.FastJsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiFactory {

    private static volatile ApiFactory mApiFactory;

    private Map<String, Object> apiCachePool = new HashMap<>();

    private String mainHostUrl = null;

    private OkHttpClient mOkHttpClient;

    private Context mApplicationContext;

    private static ApiFactory getInstance() {
        if (mApiFactory == null) {
            synchronized (ApiFactory.class) {
                if (mApiFactory == null) {
                    mApiFactory = new ApiFactory();
                }
            }
        }
        return mApiFactory;
    }


    private void initInternal(Context context, String mainHost) {
        if (context == null) {
            throw new RuntimeException("context must not be null");
        }
        mApplicationContext = context.getApplicationContext() == null ? context : context.getApplicationContext();
        mainHostUrl = mainHost;
    }

    public static void init(Context context, String mainHost) {
        ApiFactory.getInstance().initInternal(context, mainHost);
    }

    public static void init(Context context) {
        init(context, null);
    }
//    private <T> T createServerApi(Class<T> clazz) {
//        return createServerApi(clazz, null);
//    }

    public static <T> T createServerApi(Class<T> clazz) {
        return createServerApi(clazz, null);
    }

    public static <T> T createServerApi(Class<T> clazz, String url) {
        return ApiFactory.getInstance().createServerApiInternal(clazz, url);
    }

    private <T> T createServerApiInternal(Class<T> clazz, String url) {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not be null");
        }
        if (apiCachePool == null) {
            apiCachePool = new HashMap<>();
        }
        for (String key : apiCachePool.keySet()) {
            if (TextUtils.equals(clazz.getName(), key)) {
                return (T) apiCachePool.get(key);
            }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TextUtils.isEmpty(url) ? mainHostUrl : url)
                .client(getOkHttpClient())
                .addConverterFactory(FastJsonConverterFactory.create())
//                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        T t = retrofit.create(clazz);
        apiCachePool.put(clazz.getName(), t);
        return t;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
//            File httpCacheDirectory = new File(MineApplication.applicationContext.getCacheDir(), "okhttpCache");
//            int cacheSize = 10 * 1024 * 1024; // 10 MiB
//            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Config.OkHttpClient.connectTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(Config.OkHttpClient.writeTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(Config.OkHttpClient.readTimeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(new OkhttpRetryInterceptor.Builder().build())
                    .addInterceptor(new CacheInterceptor(mApplicationContext))
                    .retryOnConnectionFailure(true)
//                    .addNetworkInterceptor(NetCacheInterceptor)
//                    .addInterceptor(OfflineCacheInterceptor)
//                    .cache(cache)
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 有网时候的缓存
     */
//    static final Interceptor NetCacheInterceptor = new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            Response response = chain.proceed(request);
//            int onlineCacheTime = 20;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
//            return response.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + onlineCacheTime)
//                    .removeHeader("Pragma")
//                    .build();
//        }
//    };
//
//    /**
//     * 没有网时候的缓存
//     */
//    static final Interceptor OfflineCacheInterceptor = new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            if (NetWorkUtils.off(mApplicationContext)) {
//                int offlineCacheTime = 20;//离线的时候的缓存的过期时间
//                request = request.newBuilder()
////                        .cacheControl(FORCE_NETWORK) //强制使用缓存,如果没有缓存数据,则抛出504(only-if-cached)
////                        .cacheControl(FORCE_CACHE)    //强制使用网络,不使用任何缓存.
////                        .cacheControl(new CacheControl
////                                .Builder()
////                                .maxStale(60,TimeUnit.SECONDS)
////                                .onlyIfCached()
////                                .build()
////                        ) 两种方式结果是一样的，写法不同
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
//                        .build();
//            }
//            return chain.proceed(request);
//        }
//    };


}
