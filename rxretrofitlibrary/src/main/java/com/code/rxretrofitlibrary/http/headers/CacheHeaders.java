package com.code.rxretrofitlibrary.http.headers;

public class CacheHeaders {

    public static String HEADER_FILED = "cache";

    public static class STATUS {

        public static final String ERROR = "error";

        public static final String NORMAL = "normal";

        public static final String NO = "no";
    }

    // 无网、网络异常、接口失败优先展示缓存
    public static final String ERROR = "cache:error";

    //用于页面的秒刷,永远展示上次缓存，没有则请求网络数据
    public static final String NORMAL = "cache:normal";

    //不需要缓存
    public static final String NO = "cache:no";

    //app采取默认缓存模式
    public static final String DEFAULT = STATUS.NO;


    // 客户端可以缓存
    private static final String PRIVATE = "Cache-Control:private";

    // 客户端和代理服务器都可缓存（前端的同学，可以认为public和private是一样的）
    private static final String MAX_AGE = "Cache-Control:max-age=xxx";

    // 缓存的内容将在 xxx 秒后失效
    private static final String NO_CACHE = "Cache-Control:no-cache";

    // 需要使用对比缓存来验证缓存数据（后面介绍）
    private static final String PUBLIC = "Cache-Control:public";

    // 所有内容都不会缓存，强制缓存，对比缓存都不会触发（对于前端开发来说，缓存越多越好，so...基本上和它说886）
    private static final String NO_STORE = "Cache-Control:no-store";
}
