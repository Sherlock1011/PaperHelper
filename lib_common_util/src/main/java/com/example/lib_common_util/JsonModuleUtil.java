package com.example.lib_common_util;

import com.google.gson.Gson;

/**
 * @author Sherlock
 * Json和实例对象转换工具类
 */
public class JsonModuleUtil {
    /**
     * 实例对象转Json字符串
     * @param obj
     * @return
     */
    public static String parseModuleToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * Json字符串转实例对象
     * @param jsonString
     * @param mClass
     * @return
     */
    public static Object parseJsonToModule(String jsonString, Class<?> mClass){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, mClass);
    }
}
