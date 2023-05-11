package com.example.lib_network.okhttp.request;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Sherlock
 * Request类
 * 提供get方法、post方法，文件上传方法供应用层使用
 */
public class CommonRequest {
    /**
     * 多媒体请求类型常量
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");
    /**
     * 对外创建无请求头的post请求
     * @param url 请求地址
     * @param params post请求参数
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params){
        return createPostRequest(url, params, null);
    }

    /**
     * 对外构建无请求头的get请求
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params){
        return createGetRequest(url, params, null);
    }

    /**
     * 上传文件类型的请求
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParams params){
        //使用构造者模式
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        //指定提交方式为表单提交
        requestBody.setType(MultipartBody.FORM);
        if(params != null){
            for(Map.Entry<String, Object> entry : params.fileParams.entrySet()){
                //如果是文件类型
                if(entry.getValue() instanceof File){
                    requestBody.addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File)entry.getValue()));
                }
                //如果是json实体对象
                else if (entry.getValue() instanceof String){
                    requestBody.addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null,(String)entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }
/* ------------------------------------------------------------------功能实现---------------------------------------------------------------------- */
    /**
     * 创建post请求对象
     * 可以自定义请求头和请求体
     * @param url 请求地址
     * @param params post请求参数
     * @param headers 自定义的请求头
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers){
        //构建请求体 使用FormBody的构建者模式
//        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();
        JSONObject jsonBody = new JSONObject();
        if(params != null){
            for(Map.Entry<String, String> entry : params.urlParams.entrySet()){
                //参数遍历
//                mFormBodyBuilder.add(entry.getKey(), entry.getValue());
                try {
                    jsonBody.put(entry.getKey(),entry.getValue());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),MediaType.get("application/json; charset=utf-8"));

        //构建请求头
        Headers.Builder mHeadersBuilder = new Headers.Builder();
        if(headers != null){
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()){
                //请求头遍历
                mHeadersBuilder.add(entry.getKey(), entry.getValue());
            }
        }



        //构建request
        Request request = new Request.Builder()
                .url(url)
                .headers(mHeadersBuilder.build())
                .post(body).build();
        return request;
    }

    /**
     * 对外构建get请求
     * @param url 请求路径
     * @param params 请求参数
     * @param headers 请求头
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        //add request body
        if(params != null){
            for(Map.Entry<String, String> entry : headers.urlParams.entrySet()){
                //param
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        //add request head
        Headers.Builder mHeadersBuilder = new Headers.Builder();
        if(headers != null){
            for(Map.Entry<String, String> entry : headers.urlParams.entrySet()){
                mHeadersBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        //create request with okhttp
        return new Request.Builder()
                .url(urlBuilder.toString())
                .headers(mHeadersBuilder.build())
                .get()
                .build();
    }
}
