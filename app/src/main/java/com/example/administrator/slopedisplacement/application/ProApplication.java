package com.example.administrator.slopedisplacement.application;


import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.administrator.slopedisplacement.pushmi.PushMiUtils;
import com.example.administrator.slopedisplacement.utils.L;
import com.example.administrator.slopedisplacement.utils.PhoneSystemUtils;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hik.mcrsdk.talk.TalkClientSDK;
import com.hikvision.sdk.VMSNetSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;

import org.xutils.x;


/**
 * Created by GaoSheng on 2016/9/13.
 * 应用,主要用来做一下初始化的操作
 */

public class ProApplication extends MultiDexApplication {
    private static Context mContext;
    // public LocationService locationService;
    public Vibrator mVibrator;

    private static ProApplication mInstance;

    public static ProApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        // SDKInitializer.initialize(getApplicationContext());
        mContext = this;
        mInstance = this;
        L.isDebug = true;//true显示log日志，false不显示
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (PhoneSystemUtils.isMIUI()) {
            PushMiUtils.init();
        }

        //海康8700初始化
        MCRSDK.init();
        // 初始化RTSP
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        // 初始化语音对讲
        TalkClientSDK.initLib();
        // SDK初始化
        VMSNetSDK.init(this);
        x.Ext.init(this);//Xutils初始化,这一步之后, 我们就可以在任何地方使用x.app()来获取Application的实例了.
        x.Ext.setDebug(true); // 是否输出debug日志
    }

    public static ProApplication getIns() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}