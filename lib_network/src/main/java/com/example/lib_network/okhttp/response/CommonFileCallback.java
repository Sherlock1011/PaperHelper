package com.example.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lib_network.okhttp.exception.OkHttpException;
import com.example.lib_network.okhttp.response.listener.DisposeDataHandle;
import com.example.lib_network.okhttp.response.listener.DisposeDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Sherlock
 * 文件类型响应处理
 */
public class CommonFileCallback implements Callback {
    private final int NETWORK_ERROR = -1;
    private final int IO_ERROR = -2;
    private final String EMPTY_MSG = "";

    /**
     * 处理进度的事件类型
     */
    private static final int PROGRESS_MESSAGE = 0x01;

    /**
     * 将其他线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDownloadListener mListener;

    /**
     * 文件路径
     */
    private String mFilePath;

    /**
     * 当前进度
     */
    private int mProgress;

    public CommonFileCallback(DisposeDataHandle handle){
        this.mListener = handle.downloadListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int)msg.obj);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if(file != null){
                    mListener.onSuccess(file);
                }
                else{
                    mListener.onFailure(new OkHttpException(IO_ERROR, EMPTY_MSG));
                }
            }
        });
    }
    /**
     * 回调处理
     * @param response 回调信息
     * @return
     */
    private File handleResponse(Response response){
        if(response == null){
            return null;
        }
        InputStream inputStream = null;
        File file;
        FileOutputStream fos = null;
        byte[] buffer = new byte[2048];

        /**
         * 计算进度相关变量
         */
        int length;
        double currentLength = 0;
        double sumLength;

        //读数据流
        try{
            //检查文件是否存在
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = response.body().contentLength();

            while((length = inputStream.read(buffer)) != -1){
                fos.write(buffer, 0, buffer.length);
                currentLength += length;
                mProgress = (int)((currentLength / sumLength) * 100);
                //对外发送当前进度
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, mProgress).sendToTarget();
            }
            fos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
            file = null;
        }
        finally {
            try{
                if(fos != null){
                    fos.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }
            catch (Exception e){
                file = null;
            }
        }
        return file;
    }

    /**
     * 检查文件路径
     * 检查是否存在该文件，如果不存在则创建该文件
     * @param localFilePath
     */
    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
