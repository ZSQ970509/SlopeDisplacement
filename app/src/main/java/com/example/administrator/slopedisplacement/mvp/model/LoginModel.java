package com.example.administrator.slopedisplacement.mvp.model;



import com.example.administrator.slopedisplacement.http.NetTransformer;
import com.example.administrator.slopedisplacement.http.RetrofitUtils;
import com.example.administrator.slopedisplacement.mvp.IModel;

import io.reactivex.Observable;

/**
 * Created by administr8ation on 2017/9/8.
 */

public class LoginModel implements IModel {
    public Observable login(String passWord, String userName) {
        return RetrofitUtils.Instance
                .getApiService()
                .login(passWord, userName)
                .compose(NetTransformer.compose());
    }
    public Observable updateLoginMessage(String userName, String clentid, String uid) {
        return RetrofitUtils.Instance
                .getApiService()
                .updateLoginMessage(userName, clentid,uid)
                .compose(NetTransformer.compose());
    }
    public Observable updatedVersion(String packageName) {
        return RetrofitUtils.Instance
                .getApiService()
                .updatedVersion(packageName)
                .compose(NetTransformer.compose());
    }
}
