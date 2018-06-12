package com.example.administrator.slopedisplacement.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * 权限帮助类
 */

public class PermissionsUtils {
    /**
     * 个推推送需要的权限
     */
    public static final String[] PERMISSION_GETUI = {
            Manifest.permission.READ_PHONE_STATE,//读写 sd card 权限非常重要
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_ADMIN};//read phone state用于获取 imei 设备信息
    /**
     * 申请权限(多个申请结果会合并成一个，即PermissionCall只会被调用一次)
     *
     * @param activity    activity
     * @param permissions 权限名
     */
    public static void requestPermissions(Activity activity, String[] permissions, PermissionCall call) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.request(permissions)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        call.onSuccess();
                    } else {
                        call.onFail();
                    }
                });
    }
    /**
     * 权限回调，用于处理申请成功和失败的回调
     */
    public interface PermissionCall {
        /**
         * 请求权限成功
         */
        void onSuccess();

        /**
         * 请求权限失败
         */
        void onFail();
    }
}
