package com.example.administrator.slopedisplacement.activity;


import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.application.ProApplication;
import com.example.administrator.slopedisplacement.base.BaseMvpActivity;
import com.example.administrator.slopedisplacement.bean.LoginBean;
import com.example.administrator.slopedisplacement.bean.UpdateBean;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.contact.LoginContact;
import com.example.administrator.slopedisplacement.mvp.presenter.LoginPresenter;
import com.example.administrator.slopedisplacement.type.LoginStateEnum;
import com.example.administrator.slopedisplacement.utils.PhoneSystemUtils;
import com.orhanobut.logger.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContact.View {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.ivLogin)
    ImageView ivLogin;
    @BindView(R.id.cbSavePassWord)
    CheckBox cbSavePassWord;
    LoginBean loginBean;

    @Override
    protected LoginPresenter loadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (UserInfoPref.getSavePassWord()) {
            etUserName.setText(UserInfoPref.getUserAccount());
            etPassWord.setText(UserInfoPref.getUserPassword());
            cbSavePassWord.setChecked(true);
        }
    }


    @Override
    public void onLoginSuccess(LoginBean loginBean) {
        Logger.e("登录成功" + loginBean.toString());
        this.loginBean = loginBean;
        UserInfoPref.setSavePassWord(cbSavePassWord.isChecked());
        UserInfoPref.saveLoginInfo(etUserName.getText().toString(), loginBean.getUserName(), etPassWord.getText().toString(), loginBean.getUid());
        if (PhoneSystemUtils.isMIUI()) {
            //小米推送注册别名和用户账号
            MiPushClient.setAlias(ProApplication.getInstance(), UserInfoPref.getUserId(), "");
            MiPushClient.setUserAccount(ProApplication.getInstance(), UserInfoPref.getUserId(), "");
            toSelectProAc();
            //mPresenter.updateLoginMessage(etUserName.getText().toString(), UserInfoPref.getGeTuiClientId());
        } else {
            mPresenter.updateLoginMessage(etUserName.getText().toString(), UserInfoPref.getGeTuiClientId(), UserInfoPref.getUserId());
        }
    }

    @Override
    public void onLoginFail(String msg) {
        showToast(msg);
    }

    @Override
    public void onUpdateLoginMessageSuccess(HttpResponse httpResponse) {
        toSelectProAc();
    }

    private void toSelectProAc() {
        Intent intent = new Intent(LoginActivity.this, SelectProjectActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateLoginMessageFail(String msg) {
        showToast(msg);
    }


    @OnClick({R.id.ivLogin})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivLogin:
//                startActivity(new Intent(getActivity(), RtspNewVideoActivity.class));
                if (etUserName.getText().toString().isEmpty()) {
                    showToast("用户名不能为空");
                    return;
                }
                if (etPassWord.getText().toString().isEmpty()) {
                    showToast("密码不能为空");
                    return;
                }

                mPresenter.login(etPassWord.getText().toString(), etUserName.getText().toString());
                break;
        }
    }
}
