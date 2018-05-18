package com.example.administrator.slopedisplacement.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.activity.HuXinVideoActivity;
import com.example.administrator.slopedisplacement.application.ProApplication;
import com.ffcs.surfingscene.function.GeyeUserLogin;
import com.ffcs.surfingscene.function.SurfingScenePlayer;
import com.ffcs.surfingscene.function.onPlayListener;
import com.ffcs.surfingscene.http.HttpCallBack;
import com.ffcs.surfingscene.response.BaseResponse;
import com.ffcs.surfingscene.util.PublicUtils;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.HttpConstants;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;

/**
 *
 */

public class HuXinUtil {


    public static void initVideoSDK(Context context, String userName, String passWord, HttpCallBack<BaseResponse> callBack) {
        PublicUtils.savekey(context, "fzhc", "fzhc1234");//设置appkey和appSecret
        //showLoading("正在加载中...");
        GeyeUserLogin.getInstance(context).userLogin(userName, Long.valueOf(passWord),
                350100, callBack);
    }

    public static void initVideo(SurfingScenePlayer splay, GLSurfaceView glv, String puId, String userName, onPlayListener listener) {
        glv.setVisibility(View.VISIBLE);
        splay.init(glv);
        splay.getProgressValue();

        splay.setPlayListener(listener);

        splay.playerVideoByPuId("086591-1435552375", 2, "350100", 2, 1, userName, 1);
        //splay.playerVideoByPuId(puId, 2, "350100", 2, 1, userName, 1);
    }

    public static void playRtsp(SurfingScenePlayer splay, GLSurfaceView glv, String rtsp, onPlayListener listener) {
        glv.setVisibility(View.VISIBLE);
        splay.init(glv);
        splay.getProgressValue();
        splay.setPlayListener(listener);
//        rtsp = "rtsp://admin:hckj1234@10.1.4.194:554/h264/ch1/main/av_stream";
        splay.playVideo(rtsp, 1, 1);
    }
}
