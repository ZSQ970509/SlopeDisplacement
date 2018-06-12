package com.example.administrator.slopedisplacement.mvp.presenter;


import com.example.administrator.slopedisplacement.application.ProApplication;
import com.example.administrator.slopedisplacement.bean.LoginBean;
import com.example.administrator.slopedisplacement.bean.UpdateBean;
import com.example.administrator.slopedisplacement.bean.json.UpdateVersionJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.exception.ApiException;
import com.example.administrator.slopedisplacement.http.BaseObserver;
import com.example.administrator.slopedisplacement.http.HttpObserver;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.BasePresenter;
import com.example.administrator.slopedisplacement.mvp.contact.LoginContact;
import com.example.administrator.slopedisplacement.mvp.model.LoginModel;
import com.example.administrator.slopedisplacement.utils.PhoneSystemUtils;

import java.util.List;

/**
 * 登录Presenter
 */

public class LoginPresenter extends BasePresenter<LoginContact.View> implements LoginContact.Presenter {
    @Override
    public void login(String passWord, String userName) {
        getIView().showLoading("正在登录中...");
        new LoginModel()
                .login(passWord, userName)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<LoginBean>>() {
                    @Override
                    public void onSuccess(HttpResponse<LoginBean> loginBean) {
                        getIView().hideLoading();
                        getIView().onLoginSuccess(loginBean.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onLoginFail(msg.getMessage().toString());
                    }
                });
    }

    @Override
    public void updateLoginMessage(String userName, String clentid, String uid) {
        getIView().showLoading("正在登录中...");
        new LoginModel()
                .updateLoginMessage(userName, clentid, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse>() {
                    @Override
                    public void onSuccess(HttpResponse httpResponse) {
                        getIView().hideLoading();
                        getIView().onUpdateLoginMessageSuccess(httpResponse);
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onUpdateLoginMessageFail(msg.getMessage().toString());
                    }
                });
    }

    @Override
    public void updatedVersion() {
        getIView().showLoading("正在检测版本中...");
        new LoginModel()
                .updatedVersion(PhoneSystemUtils.getPackageName())
                .compose(getIView().bindLifecycle())
                .subscribe(new BaseObserver<List<UpdateVersionJson>>() {
                    @Override
                    public void onSuccess(List<UpdateVersionJson> httpResponse) {
                        getIView().hideLoading();
                        getIView().onUpdatedVersionSuccess(httpResponse.get(0));
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onLoginFail(msg.getMessage());
                    }
                });
    }

}
