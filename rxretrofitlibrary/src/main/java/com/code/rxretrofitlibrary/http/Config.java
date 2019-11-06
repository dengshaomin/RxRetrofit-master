package com.code.rxretrofitlibrary.http;

public class Config {

    public static class OkHttpClient {

        //连接超时时间
        public static int connectTimeout = 10000;

        //写超时时间
        public static int writeTimeout = 10000;

        //读超时时间
        public static int readTimeout = 10000;
    }

    //重试相关
    public static class OkhttpRetry {

        //重试次数
        public static int executionCount = 3;

        //重试间隔
        public static int retryInterval = 1000;
    }
    //缓存
    public static class CacheManager {
        //磁盘缓存容量
        public static long diskCacheSize = 20 * 1024 * 1024;
    }
}
