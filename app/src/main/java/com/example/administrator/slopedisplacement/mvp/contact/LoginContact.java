package com.example.administrator.slopedisplacement.mvp.contact;


import com.example.administrator.slopedisplacement.bean.LoginBean;
import com.example.administrator.slopedisplacement.bean.UpdateBean;
import com.example.administrator.slopedisplacement.bean.json.UpdateVersionJson;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.IView;

import retrofit2.http.Field;

/**
 * Created by administration on 2017/9/4.
 */

public class LoginContact {
    public interface View extends IView {
        void onLoginSuccess(LoginBean loginBean);

        void onLoginFail(String msg);

        void onUpdateLoginMessageSuccess(HttpResponse httpResponse);

        void onUpdateLoginMessageFail(String msg);

        void onUpdatedVersionSuccess(UpdateVersionJson httpResponse);

        void onUpdatedVersionFail(String msg);
    }

    public interface Presenter {
        void login(String passWord, String userName);

        void updateLoginMessage(String userName, String clentid, @Field("uid") String uid);

        void updatedVersion();
    }
}
