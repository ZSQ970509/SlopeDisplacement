package com.example.administrator.slopedisplacement.http;


import android.annotation.TargetApi;
import android.os.Build;

import com.example.administrator.slopedisplacement.exception.ApiException;

/**
 * 网络请求监听（成功/失败）
 */

public interface HttpRequest<T> {
    void onSuccess(T t);
    void onFail(ApiException msg);
}
