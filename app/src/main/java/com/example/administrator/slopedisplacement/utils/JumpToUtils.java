package com.example.administrator.slopedisplacement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.slopedisplacement.activity.AlarmChartViewActivity;
import com.example.administrator.slopedisplacement.activity.DataReportActivity;
import com.example.administrator.slopedisplacement.activity.HKVideoActivity;
import com.example.administrator.slopedisplacement.activity.RtspVideoActivity;
import com.example.administrator.slopedisplacement.activity.HuXinVideoActivity;
import com.example.administrator.slopedisplacement.activity.PlanLayoutOfPanoramaActivity;
import com.example.administrator.slopedisplacement.activity.SelectProjectActivity;
import com.example.administrator.slopedisplacement.activity.SelectSchemeActivity;
import com.example.administrator.slopedisplacement.activity.ShowMonitoringImgActivity;
import com.example.administrator.slopedisplacement.bean.IVMS_8700_Bean;
import com.example.administrator.slopedisplacement.bean.SchemeBean;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;

/**
 * Activity跳转
 */

public class JumpToUtils {
    public static final String KEY_AREAL_LIST = "key_areal_list";
    public static final String KEY_FIXED_LIST = "key_fixed_list";
    public static final String KEY_SCHEME_ID = "key_scheme_id";

    /**
     * 跳转到数据报表页面
     *
     * @param activity
     */
    public static void toDataReportActivity(Activity activity, GetDatSchemeAreaListJson arealListJson, GetDatSchemeFixedListJson fixedListJson, String schemeID) {
        Intent intent = new Intent(activity, DataReportActivity.class);
        intent.putExtra(KEY_AREAL_LIST, arealListJson);
        intent.putExtra(KEY_FIXED_LIST, fixedListJson);
        intent.putExtra(KEY_SCHEME_ID, schemeID);
        activity.startActivity(intent);
    }

    public static final String KEY_WIDE_ANGLE_IMG = "key_wide_angle_img";
    public static final String KEY_LONG_FOCAL_IMG = "key_long_focal_img";
    public static final String KEY_TITLE = "key_title";

    /**
     * 跳转到显示检测图(长焦图和广角图)页面
     *
     * @param activity
     * @param wideAngleImg 广角图
     * @param longFocalImg 长焦图
     * @param recordID     序号
     * @param monitorID    监测点
     * @param nowShift     本次位移
     */
    public static void toShowMonitoringImgActivity(Activity activity, String wideAngleImg, String longFocalImg, int recordID, int monitorID, String nowShift) {
        Intent intent = new Intent(activity, ShowMonitoringImgActivity.class);
        intent.putExtra(KEY_TITLE, "监测图查看(序号：" + recordID + "，监测点：" + monitorID + ") 本次位移：" + nowShift + "mm");
        intent.putExtra(KEY_WIDE_ANGLE_IMG, wideAngleImg);
        intent.putExtra(KEY_LONG_FOCAL_IMG, longFocalImg);
        activity.startActivity(intent);
    }


    public static final String KEY_CAMID = "key_camId";
    public static final String KEY_SCHEMEID = "key_schemeID";
    public static final String KEY_IVMS_8700_BEAN = "key_ivms_8700_bean";
    public static final String KEY_FROM_PUSH = "key_from_push";

    /**
     * 跳转到主页面页面
     *
     * @param activity
     * @param camId    设备id
     * @param schemeID 方案id
     */
    public static void toPlanLayoutOfPanoramaActivity(Activity activity, String camId, String schemeID, IVMS_8700_Bean ivms_8700_bean) {
        Intent intent = new Intent(activity, PlanLayoutOfPanoramaActivity.class);
        intent.putExtra(KEY_CAMID, camId);
        intent.putExtra(KEY_SCHEMEID, schemeID);
        intent.putExtra(KEY_IVMS_8700_BEAN, ivms_8700_bean);
        intent.putExtra(KEY_FROM_PUSH, false);//是否来至推送
        activity.startActivity(intent);
    }

    /**
     * 推送跳转到主页面页面
     *
     * @param context
     * @param camId    设备id
     * @param schemeID 方案id
     */
    public static void toPlanLayoutOfPanoramaActivityFromPush(Context context, String camId, String schemeID, IVMS_8700_Bean ivms_8700_bean) {
        ActivityUtils.INSTANCE.finishOthersActivity(SelectProjectActivity.class);
        Intent intent = new Intent(context, PlanLayoutOfPanoramaActivity.class);
        intent.putExtra(KEY_CAMID, camId);
        intent.putExtra(KEY_SCHEMEID, schemeID);
        intent.putExtra(KEY_IVMS_8700_BEAN, ivms_8700_bean);
        intent.putExtra(KEY_FROM_PUSH, true);//是否来至推送
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static final String KEY_SCHEMEBEAN = "key_schemeBean";

    /**
     * 跳转到选择方案页面
     *
     * @param activity
     * @param camId      设备id
     * @param schemeBean 方案列表数据
     */
    public static void toSelectSchemeActivity(Activity activity, String camId, SchemeBean schemeBean, IVMS_8700_Bean ivms_8700_bean) {
        Intent intent = new Intent(activity, SelectSchemeActivity.class);
        intent.putExtra(KEY_CAMID, camId);
        intent.putExtra(KEY_SCHEMEBEAN, schemeBean);
        intent.putExtra(KEY_IVMS_8700_BEAN, ivms_8700_bean);
        activity.startActivity(intent);
    }

    /**
     * 跳转到互信播放页面
     *
     * @param activity
     * @param ivms_8700_bean
     */
    public static void toHuXinVideoActivity(Activity activity, IVMS_8700_Bean ivms_8700_bean) {
        Intent intent = new Intent(activity, HuXinVideoActivity.class);
        intent.putExtra(KEY_IVMS_8700_BEAN, ivms_8700_bean);
        activity.startActivity(intent);
    }

    /**
     * 跳转到海康播放页面
     *
     * @param activity
     * @param ivms_8700_bean
     */
    public static void toHKVideoActivity(Activity activity, IVMS_8700_Bean ivms_8700_bean) {
        Intent intent = new Intent(activity, HKVideoActivity.class);
        intent.putExtra(KEY_IVMS_8700_BEAN, ivms_8700_bean);
        activity.startActivity(intent);
    }

    public static final String KEY_VIDEO_RTSP = "key_video_rtsp";

    public static void toRtspVideoAc(Activity activity, String rtsp) {
        Intent intent = new Intent(activity, RtspVideoActivity.class);
        intent.putExtra(KEY_VIDEO_RTSP, rtsp);
        activity.startActivity(intent);
    }

    public static final String KEY_MONITORID = "MonitorID";
    public static final String KEY_AHA = "Aha";
    public static final String KEY_AVA = "Ava";
    public static final String KEY_SHIFT = "Shift";
    public static final String KEY_PATHA = "Patha";
    public static final String KEY_NEXTPATHA = "nextPatha";
    public static final String KEY_OLDPATHA = "oldPatha";

    /**
     * 跳转到查看告警图页面
     *
     * @param activity
     * @param MonitorID
     * @param Aha
     * @param Ava
     * @param Shift
     * @param Patha
     * @param nextPatha
     * @param oldPatha
     */

    public static void toAlarmChartViewActivity(Activity activity, int MonitorID, double Aha, double Ava, double Shift, String Patha, String nextPatha, String oldPatha) {
        Intent intent = new Intent(activity, AlarmChartViewActivity.class);
        intent.putExtra(KEY_MONITORID, MonitorID);
        intent.putExtra(KEY_AHA, Aha);
        intent.putExtra(KEY_AVA, Ava);
        intent.putExtra(KEY_SHIFT, Shift);
        intent.putExtra(KEY_PATHA, Patha);
        intent.putExtra(KEY_NEXTPATHA, nextPatha);
        intent.putExtra(KEY_OLDPATHA, oldPatha);
        activity.startActivity(intent);
    }

}
