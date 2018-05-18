package com.example.administrator.slopedisplacement.http;

import com.example.administrator.slopedisplacement.exception.ErrorType;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * http响应参数实体类
 * 通过Gson解析属性名称需要与服务器返回字段对应,或者使用注解@SerializedName
 */

public class HttpResponse<T> {
    /**
     * 描述信息
     */
    @SerializedName("msg")
    private String msg;
    /**
     * 状态码
     */
    @SerializedName("result")
    private int code;
    /**
     * 数据对象[成功返回对象,失败返回错误说明]
     */
    @SerializedName("data")
    private T data;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String response = "[http response]" + "{\"code\": " + code + ",\"msg\":" + msg + ",\"data\":" + new Gson().toJson(data) + "}";
        return response;
    }
}
