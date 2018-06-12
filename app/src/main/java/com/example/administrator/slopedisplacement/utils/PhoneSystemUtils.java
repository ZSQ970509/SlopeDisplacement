package com.example.administrator.slopedisplacement.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.application.ProApplication;
import com.example.administrator.slopedisplacement.base.BaseActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * 手机系统相关工具类
 */

public class PhoneSystemUtils {
    //检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            System.out.println("this is a xiaomi device");
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } else {
            return false;
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageInfo packageInfo = ProApplication.getInstance().getPackageManager().getPackageInfo(ProApplication.getInstance().getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getPackageName() {
        return ProApplication.getInstance().getPackageName();
    }

    public static void installApk(File file, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) { //适配安卓7.0
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkFileUri = FileProvider.getUriForFile(context.getApplicationContext(),
                    context.getPackageName(), file);
            i.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + file.toString()),
                    "application/vnd.android.package-archive");// File.toString()会返回路径信息
        }
        /*Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        */
        context.startActivity(i);
    }

    /**
     * 下载PDF文件
     *
     * @param url 下载地址
     */
    public static void downloadFile(final BaseActivity activity, final String url) {
        if (null == activity)
            return;
        if (TextUtils.isEmpty(url)) {
            activity.showToast("下载地址错误");
            return;
        }
//        deleteFile(url);

        RequestParams requestParams = new RequestParams(url);  // 下载地址
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator+"cache_download"+File.separator +"slope.apk";
        requestParams.setSaveFilePath(path); // 为RequestParams设置文件下载后的保存路径
//        requestParams.setAutoRename(false); // 下载完成后自动为文件命名
        L.e("下载地址" + url);
        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progessbar));
        mProgressDialog.setMessage("正在下载更新");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressNumberFormat("");
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {
                installApk(result,activity);
                mProgressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.e("下载失败");
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                L.e("取消下载");
                mProgressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                L.e("结束下载");
                mProgressDialog.dismiss();
            }

            @Override
            public void onWaiting() {
                // 网络请求开始的时候调用
                L.e("等待下载");
            }

            @Override
            public void onStarted() {
                // 下载的时候不断回调的方法
                L.e("开始下载");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                // 当前的下载进度和文件总大小
                L.e("正在下载中......");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMessage("正在下载中......");
                mProgressDialog.show();
                mProgressDialog.setMax((int) total);
                mProgressDialog.setProgress((int) current);
            }
        });
    }

    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;
        try {
            File file = new File(filePath);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            L.e(e.getMessage());
        }

    }
}
