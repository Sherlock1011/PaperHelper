package com.example.paperhelper.utils;

import com.example.lib_network.okhttp.CommonOkHttpClient;
import com.example.lib_network.okhttp.request.CommonRequest;
import com.example.lib_network.okhttp.request.RequestParams;
import com.example.lib_network.okhttp.response.listener.DisposeDataHandle;
import com.example.lib_network.okhttp.response.listener.DisposeDataListener;
import com.example.lib_network.okhttp.response.listener.DisposeDownloadListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Sherlock
 * 请求网络数据中心
 */
public class RequestCenter {
    private static final String chat_gpt_url = "https://api.openai.com/v1/completions";
    private static final String token = "Bearer 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";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    /**
     * download file from website to location
     * @param urlStr website url
     * @param source
     * @param listener recall
     */
    public static void downloadFileRequest(String urlStr, String source, DisposeDownloadListener listener){
        //create a get request
        Request request = CommonRequest.createGetRequest(urlStr, null);
        CommonOkHttpClient.downLoadFile(request, new DisposeDataHandle(listener ,source));
    }

    /**
     * send prompt to ChatGPT
     * @param question prompt
     * @param listener recall
     */
    public static void questionToGPT(String question, DisposeDataListener listener){
        RequestParams header = new RequestParams();
        header.put("Authorization", token);
        RequestParams body = new RequestParams();
        try {
            body.put("model", "text-davinci-003");
            body.put("prompt", question);
            body.put("max_tokens", 4000);
            body.put("temperature", 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Request request = CommonRequest.createPostRequest(chat_gpt_url, body, header);
        CommonOkHttpClient.post(request,new DisposeDataHandle(listener));
    }

    public static void questionToGPT(String question, Callback callback){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        CommonOkHttpClient.getOkHttpClient().newCall(request).enqueue(callback);
    }

}
