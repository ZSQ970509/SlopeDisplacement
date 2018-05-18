package com.example.administrator.slopedisplacement.exception;

/**
 * Created by gaosheng on 2016/11/6.
 * 21:50
 * com.example.gaosheng.myapplication.exception
 */

public interface ErrorType {

    /**
     * 请求成功
     */
    int SUCCESS = 1;
    /**
     * 请求失败
     */
    int FAIL = 0;
    /**
     * 数据为空
     */
    int DATE_NULL = 2;
    /**
     * 未知错误
     */
    int UN_KNOWN_ERROR = 1000;

    /**
     * 网络错误
     */
    int NETWORK_ERROR = 1002;
    /**
     * 网络超时
     */
    int TIME_OUT_ERROR = 1003;
    /**
     * 解析错误
     */
    int JSON_ERROR = 1004;
}
