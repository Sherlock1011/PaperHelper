package com.example.lib_network.okhttp.response.listener;

/**
 * @author Sherlock
 * 回调：与应用层进行通信
 * 业务逻辑层真正处理的地方，包括java层异常和业务层异常
 */
public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     * @param reasonObj
     */
    void onFailure(Object reasonObj);
}
