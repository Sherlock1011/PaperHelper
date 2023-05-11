package com.example.lib_network.okhttp.exception;

/**
 * @author Sherlock
 * 自定义网络框架异常类
 * 返回ecode,emsg到业务层
 */
public class OkHttpException extends Exception{
    private static final long serialVersionUID = 1L;
    /**
     * 服务器返回的错误编码
     */
    private int eCode;
    /**
     * 服务器返回的错误信息
     */
    private Object eMsg;

    public OkHttpException(int eCode, Object eMsg){
        this.eCode = eCode;
        this.eMsg = eMsg;
    }

    public int getECode() {
        return eCode;
    }

    public Object getEMsg() {
        return eMsg;
    }
}
