package com.ttsea.mvp.base;


import android.content.Context;
import android.os.Handler;

import com.ttsea.jlibrary.common.JLog;
import com.ttsea.jlibrary.jasynchttp.server.http.Http;
import com.ttsea.jlibrary.utils.AppInformationUtils;
import com.ttsea.jlibrary.utils.NetWorkUtils;
import com.ttsea.jlibrary.utils.Utils;
import com.ttsea.mvp.customHttpClient.HttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/15 10:44 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/15 10:44.
 */
public abstract class BaseRequestWork {
    private final String TAG = "BaseRequestWork";

    private final int defaultMaxAgeSeconds = 0;
    private final int defaultMaxStaleSeconds = 3;
    private final String defaultUserAgent = "mvp";
    private final String defaultPlatform = "android";

    public Context mContext;
    /** 获取网络数据返回的结果码 */
    public int resultCode = -1;
    /** 获取网络数据返回的结果信息 */
    public String msg = null;
    private Handler callbackHandler;

    public BaseRequestWork(Context context) {
        this.mContext = context;
        this.callbackHandler = new Handler();
    }

    /**
     * 添加一个请求<br/>
     * 默认方法为POST<br/>
     * 默认不缓存数据<br/>
     * 请求头默认会有("User-Agent", {@link #defaultUserAgent})<br/>
     * requestTag默认为{@link #getRequestTag()}<br/>
     *
     * @param url         url
     * @param params      请求体，默认会有("platform", {@link #defaultPlatform})和("version", "这里是app版本号")
     * @param requestCode 自定义请求code
     */
    public void addRequest(String url, Map<String, String> params, final int requestCode) {
        addRequest(url, Http.Method.POST, params, requestCode);
    }

    /**
     * 添加一个请求<br/>
     * 默认不缓存数据<br/>
     * 请求头默认会有("User-Agent", {@link #defaultUserAgent})<br/>
     * requestTag默认为{@link #getRequestTag()}<br/>
     *
     * @param url         url
     * @param method      请求方法, see {@link com.ttsea.jlibrary.jasynchttp.server.http.Http.Method}
     * @param params      请求体，默认会有("platform", {@link #defaultPlatform})和("version", "这里是app版本号")
     * @param requestCode 自定义请求code
     */
    public void addRequest(String url, String method, Map<String, String> params, final int requestCode) {
        addRequest(url, method, params, false, requestCode);
    }

    /**
     * 添加一个请求<br/>
     * 请求头默认会有("User-Agent", {@link #defaultUserAgent})<br/>
     * requestTag默认为{@link #getRequestTag()}<br/>
     * maxAgeSeconds默认为{@link #defaultMaxAgeSeconds}<br/>
     * maxStaleSeconds默认为{@link #defaultMaxStaleSeconds}
     *
     * @param url         url
     * @param method      请求方法, see {@link com.ttsea.jlibrary.jasynchttp.server.http.Http.Method}
     * @param params      请求体，默认会有("platform", {@link #defaultPlatform})和("version", "这里是app版本号")
     * @param shouldCache 是否需要缓存，若是设置为true，则会将method设置为GET，因为OkHttp3只会缓存使用GET方法的请求<br/>
     *                    1.为true时，会将method改为"GET",并且将requestBody拼接到url后面，拼接完后requestBody将会设置为null<br/>
     *                    拼接后如：http://www.baidu.com/index.php?platform=android&version=1.0<br/>
     *                    2.为false时:不修改<br/>
     * @param requestCode 自定义请求code
     */
    public void addRequest(String url, String method, Map<String, String> params, boolean shouldCache, final int requestCode) {
        addRequest(url, method, null, params, getRequestTag(), shouldCache, defaultMaxStaleSeconds, requestCode);
    }

    /**
     * 添加一个请求<br/>
     * 请求头默认会有("User-Agent", {@link #defaultUserAgent})<br/>
     * maxAgeSeconds默认为{@link #defaultMaxAgeSeconds}<br/>
     *
     * @param url             url
     * @param method          请求方法, see {@link com.ttsea.jlibrary.jasynchttp.server.http.Http.Method}
     * @param headers         请求头，默认会有("User-Agent", "mvp")
     * @param params          请求体，默认会有("platform", {@link #defaultPlatform})和("version", "这里是app版本号")
     * @param requestTag      请求标识，可根据这个tag来取消请求
     * @param shouldCache     是否需要缓存，若是设置为true，则会将method该为GET，因为OkHttp3只会缓存使用GET方法的请求<br/>
     *                        1.为true时，会将method改为"GET",并且将requestBody拼接到url后面，拼接完后requestBody将会设置为null<br/>
     *                        拼接后如：http://www.baidu.com/index.php?platform=android&version=1.0<br/>
     *                        2.为false时:不修改<br/>
     * @param maxStaleSeconds CacheControl: max-age参数，该参数只会在shouldCache为true并且网络可用的时候才生效<br/>
     *                        表示愿意接受超过其新鲜度的但不超过指定的秒数(maxStaleSeconds)的响应，（一般设置为3）
     * @param requestCode     自定义请求code
     */
    public void addRequest(String url, String method, Map<String, String> headers, Map<String, String> params, String requestTag,
                           boolean shouldCache, int maxStaleSeconds, final int requestCode) {

        Headers.Builder headersBuilder = new Headers.Builder()
                .set("User-Agent", defaultUserAgent);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                //这里使用set，确保key-value一一对应
                headersBuilder.set(entry.getKey(), entry.getValue());
            }
        }

        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("platform", defaultPlatform)
                .add("version", AppInformationUtils.getVersionName(mContext));
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if ("platform".equals(entry.getKey()) || "version".equals(entry.getKey())) {//跳过重复的值
                    JLog.d(TAG, "Skip repeat value:" + entry.getKey());
                    continue;
                }
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        addRequest(url, method, headersBuilder.build(), bodyBuilder.build(), requestTag, shouldCache,
                defaultMaxAgeSeconds, maxStaleSeconds, requestCode);
    }

    /**
     * 添加一个请求，该方法为私有方法
     *
     * @param url             url
     * @param method          请求方法, see {@link com.ttsea.jlibrary.jasynchttp.server.http.Http.Method}
     * @param headers         请求头
     * @param requestBody     请求体，根据method来设置或者不设置（某些方法是不需要requestBody，如GET方法）
     * @param requestTag      请求标识，可根据这个tag来取消请求
     * @param shouldCache     是否需要缓存，若是设置为true，则会将method该为GET，因为OkHttp3只会缓存使用GET方法的请求<br/>
     *                        1.为true时，会将method改为"GET",并且将requestBody拼接到url后面，拼接完后requestBody将会设置为null<br/>
     *                        拼接后如：http://www.baidu.com/index.php?platform=android&version=1.0<br/>
     *                        2.为false时:不修改<br/>
     * @param maxAgeSeconds   CacheControl: max-age参数，该参数只会在shouldCache为true并且网络可用的时候才生效<br/>
     *                        表示不愿接受年龄大于指定秒数(maxAgeSeconds)的响应（一般设置为0）
     * @param maxStaleSeconds CacheControl: max-age参数，该参数只会在shouldCache为true并且网络可用的时候才生效<br/>
     * @param requestCode     自定义请求code
     */
    private void addRequest(String url, String method, Headers headers, RequestBody requestBody, String requestTag, boolean shouldCache,
                            int maxAgeSeconds, int maxStaleSeconds, final int requestCode) {
        CacheControl cacheControl;
        if (shouldCache) {//如果允许使用缓存
            if (!NetWorkUtils.isAvailable(mContext)) {//网络不可以则强制使用缓存
                cacheControl = CacheControl.FORCE_CACHE;

            } else {//网络可用，则需要根据缓存是否过期来判断是否使用缓存
                cacheControl = new CacheControl.Builder()
                        .maxAge(maxAgeSeconds, TimeUnit.SECONDS)
                        .maxStale(maxStaleSeconds, TimeUnit.SECONDS)
                        .build();
            }

            try {
                //将requestBody参数拼接到urls后面，并将method改为GET
                if (requestBody != null) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String params = buffer.readUtf8();
                    if (!Utils.isEmpty(params)) {
                        url = url + "?" + params;
                    }
                    JLog.d(TAG, "origin method:" + method + ", we will change method as GET, after changed requestBody is null and url:" + url);
                    method = Http.Method.GET;
                    //"GET"方法不需要传入requestBody，否则会引起报错
                    requestBody = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                JLog.e(TAG, "Exception e:" + e.toString());
            }

        } else {
            cacheControl = CacheControl.FORCE_NETWORK;
        }

        //这里需要注意callback是运行在OKHttp线程中的，
        // 所以这里我们使用callbackHandler其设置在主线程中运行
        okhttp3.Callback callback = new okhttp3.Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (call.isCanceled()) {
                            JLog.d(TAG, "call is canceled, request:" + call.request().toString());
                            return;
                        }
                        String errorMsg = "";
                        if (e != null) {
                            errorMsg = e.toString();
                            e.printStackTrace();
                        }
                        JLog.e(TAG, "Exception e:" + errorMsg);
                        handleErrorResponse(errorMsg, requestCode);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (call.isCanceled()) {
                            JLog.d(TAG, "call is canceled, request:" + call.request().toString());
                            return;
                        }
                        JLog.d(TAG, "response:" + response.toString() + ", responseHeaders:" + headerToString(response.headers()));

                        if (response.isSuccessful()) {
                            try {
                                String jsonData = response.body().string();
                                handleNetWorkData(jsonData, requestCode);

                            } catch (IOException e) {
                                e.printStackTrace();
                                String errorMsg = e.toString();
                                JLog.e(TAG, "Exception e:" + errorMsg);
                                handleErrorResponse(errorMsg, requestCode);
                            }

                        } else {
                            String errorMsg = "not handle response code, responseCode:" + response.code();
                            JLog.e(TAG, "Exception e:" + errorMsg);
                            handleErrorResponse(errorMsg, requestCode);
                        }
                    }
                });
            }
        };

        addRequest(url, method, headers, requestBody, requestTag, cacheControl, callback);
        JLog.d(TAG, "shouldCache:" + shouldCache + ", maxAgeSeconds:" + maxAgeSeconds + ", maxStaleSeconds:" + maxStaleSeconds + ", requestCode:" + requestCode);
    }

    /**
     * 添加一个请求，建议不要直接调用
     *
     * @param url          url
     * @param method       请求方法, see {@link com.ttsea.jlibrary.jasynchttp.server.http.Http.Method}
     * @param headers      请求头
     * @param requestBody  请求体，根据method来设置或者不设置（某些方法是不需要requestBody，如GET方法）
     * @param requestTag   请求标识，可根据这个tag来取消请求
     * @param cacheControl 缓存控制，该参数将用于response中，该参数用在：HttpClient.REWRITE_CACHE_CONTROL_INTERCEPTOR类中
     * @param callback     异步请求回调，这里需要注意callback是运行在OKHttp线程中的
     */
    protected void addRequest(String url, String method, Headers headers, RequestBody requestBody, String requestTag,
                              CacheControl cacheControl, okhttp3.Callback callback) {

        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .method(method.toUpperCase(), requestBody)
                .tag(requestTag)
                .headers(headers)
                .cacheControl(cacheControl);

        Request request = builder.build();

        OkHttpClient okHttpClient = HttpClient.getHttpClient(mContext);
        okHttpClient.newCall(request).enqueue(callback);

        JLog.d(TAG, "url:" + request.url() + ", requestBody:" + bodyToString(request) + ", method:" + request.method() + ", tag:" + request.tag());
        JLog.d(TAG, "requestHeaders:" + headerToString(request.headers()));
    }

    /**
     * 请求数据出错后，的处理方法<br/>
     * 这里建议使用 synchronized 修饰该方法，因为同一个界面可能会存在多个请求数据的线程对它操作
     *
     * @param errorMsg    错误信息
     * @param requestCode 用户定义的code
     */
    public abstract void handleErrorResponse(String errorMsg, int requestCode);

    /**
     * 处理请求返回的数据<br/>
     * 这里建议使用 synchronized 修饰该方法，因为同一个界面可能会存在多个请求数据的线程对它操作
     *
     * @param jsonData    请求返回的数据
     * @param requestCode 用户定义的code
     * @return 返回true表示已经处理数据，否则表示未处理数据
     */
    public abstract boolean handleNetWorkData(String jsonData, int requestCode);

    /**
     * 用户自定义的tag，用于标记request，便于取消该请求
     *
     * @return String
     */
    public abstract String getRequestTag();

    /** 通过tag取消指定请求 */
    protected void cancelRequest(String tag) {
        cancelRequest(HttpClient.getHttpClient(mContext), tag);
    }

    /** 通过tag取消指定请求 */
    protected void cancelRequest(OkHttpClient client, String tag) {
        if (client == null || tag == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                JLog.d(TAG, "cancel queued call, url:" + call.request().url());
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                JLog.d(TAG, "cancel running call, url:" + call.request().url());
            }
        }
    }

    /** 取消所有的请求 */
    protected void cancelAllRequest(OkHttpClient client) {
        if (client == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
            JLog.d(TAG, "cancel call, url:" + call.request().url());
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
            JLog.d(TAG, "cancel running call, url:" + call.request().url());
        }
    }

    /** 将request body拼接成String，如："platform=android&version=1.0.1&login_pass=123456&login_name=aaa108" */
    private String bodyToString(Request request) {
        try {
            Request copyRequest = request.newBuilder().build();
            if (copyRequest.body() == null) {
                return null;
            }
            Buffer buffer = new Buffer();
            copyRequest.body().writeTo(buffer);
            return buffer.readUtf8();

        } catch (final IOException e) {
            return "";
        }
    }

    /** 将header拼接成String，如："User-Agent: mvp, Cache-Control: no-cache" */
    private String headerToString(Headers headers) {
        if (headers == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            result.append(headers.name(i)).append(": ").append(headers.value(i)).append(", ");
        }
        return result.toString();
    }
}
