package com.example.lib_network.okhttp;

import androidx.annotation.NonNull;

import com.example.lib_network.okhttp.response.CommonFileCallback;
import com.example.lib_network.okhttp.response.CommonJsonCallback;
import com.example.lib_network.okhttp.response.listener.DisposeDataHandle;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Sherlock
 * 网络层对外接口
 * 用来发送get、post请求的网络工具类，包括设置一些请求的共用参数
 */
public class CommonOkHttpClient {
    /**
     * 超时时间30s
     */
    private static final int TIME_OUT = 60;
    private static OkHttpClient mOkHttpClient;

    //完成对okHttpClient的初始化
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //对所有域名默认都是信任的
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        });

        //添加超时时间
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //允许重定向
        okHttpClientBuilder.followRedirects(true);
        mOkHttpClient = okHttpClientBuilder.build();
    }


    /**
     * 获取OkHttpClient单例
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * get
     * @param request
     * @param handle
     * @return
     */
    public static Call get(Request request, DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * post
     * @param request
     * @param handle
     * @return
     */
    public static Call post(Request request,DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * download file
     * @param request
     * @param handle
     * @return
     */
    public static Call downLoadFile(Request request,DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
