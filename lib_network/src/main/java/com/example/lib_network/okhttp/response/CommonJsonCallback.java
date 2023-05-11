package com.example.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lib_common_util.JsonModuleUtil;
import com.example.lib_network.okhttp.exception.OkHttpException;
import com.example.lib_network.okhttp.response.listener.DisposeDataHandle;
import com.example.lib_network.okhttp.response.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Sherlock
 * 处理json类型的相应response
 */
public class CommonJsonCallback implements Callback {
    private final String EMPTY_MSG = "";

    /**
     * 网络层的异常
     */
    private final int NETWORK_ERROR = -1;

    /**
     * Json解析的异常
     */
    private final int JSON_ERROR = -2;

    /**
     * 未知类型的异常
     */
    private final int OTHER_ERROR = -3;

    /**
     * 字节码 json数据转成的类型
     */
    private Class<?> mClass;
    /**
     * 回调
     */
    private DisposeDataListener mListener;
    /**
     * 发送数据到UI线程
     */
    private Handler mDeliveryHandler;

    public CommonJsonCallback(DisposeDataHandle handle){
        mListener = handle.mListener;
        mClass = handle.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR,e));
            }
        });
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        //获取响应数据
        final String result = response.body().string();
        //将结果post到主线程中
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理请求
     * @param result
     */
    private void handleResponse(String result){
        if(result == null || result.equals("")){
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        //解析json
        try {
            //不需要解析
            if(mClass == null){
                //让应用层自行处理，将原始数据发送回应用层
                mListener.onSuccess(result);
                Log.e("CHAT", result);
            }
            else{
                //解析为实体对象
                Object obj = JsonModuleUtil.parseJsonToModule(result, mClass);
                if(obj != null){
                    //解析成功,将解析对象传回到应用层
                    mListener.onSuccess(obj);
                }
                else{
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        }catch (Exception e){
            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
        }
    }
}
