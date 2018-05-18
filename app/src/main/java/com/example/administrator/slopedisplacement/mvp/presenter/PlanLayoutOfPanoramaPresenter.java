package com.example.administrator.slopedisplacement.mvp.presenter;


import com.example.administrator.slopedisplacement.bean.PanoramaImgBean;
import com.example.administrator.slopedisplacement.bean.SchemeAlarmListBean;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeAlarmJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeMonitorLogJson;
import com.example.administrator.slopedisplacement.exception.ApiException;
import com.example.administrator.slopedisplacement.http.HttpObserver;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.BasePresenter;
import com.example.administrator.slopedisplacement.mvp.contact.PlanLayoutOfPanoramaContact;
import com.example.administrator.slopedisplacement.mvp.model.PlanLayoutOfPanoramaModel;

import java.util.List;

/**
 * 登录Presenter
 */

public class PlanLayoutOfPanoramaPresenter extends BasePresenter<PlanLayoutOfPanoramaContact.View> implements PlanLayoutOfPanoramaContact.Presenter {
    @Override
    public void getPanoramaImg(String camId, String pageindex, String pagesize, String uid) {
        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getPanoramaImg(camId, "", "", pageindex, pagesize, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<PanoramaImgBean>>() {
                    @Override
                    public void onSuccess(HttpResponse<PanoramaImgBean> panoramaImgBean) {
//                        getIView().hideLoading();
                        getIView().onGetPanoramaImgSuccess(panoramaImgBean.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
//                        getIView().hideLoading();
                        getIView().onGetPanoramaImgFail(msg.getMessage().toString());
                    }
                });
    }

    @Override
    public void getSchemeAlarmList(String schemeID, String states, String startTime, String endTime, String pageindex, String pagesize, String uid) {
        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getSchemeAlarmList(schemeID, "3", startTime, endTime, pageindex, pagesize, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<SchemeAlarmListBean>>() {
                    @Override
                    public void onSuccess(HttpResponse<SchemeAlarmListBean> schemeAlarmListBean) {
                        getIView().hideLoading();
                        getIView().onGetSchemeAlarmListSuccess(schemeAlarmListBean.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onGetSchemeAlarmListFail(msg.getMessage().toString());
                    }
                });
    }

    @Override
    public void updateSchemeAlarmByAlarmID(String alarmID, String states, String uid) {
        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .updateSchemeAlarmByAlarmID(alarmID, states, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse>() {
                    @Override
                    public void onSuccess(HttpResponse httpResponse) {
                        getIView().hideLoading();
                        getIView().onUpdateSchemeAlarmByAlarmIDSuccess(httpResponse.getMsg());
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onUpdateSchemeAlarmByAlarmIDFail(msg.getMessage().toString());
                    }
                });
    }


    @Override
    public void getSchemeAlarm(String schemeID, String states, String uid) {
//        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getSchemeAlarm(schemeID, states, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<GetSchemeAlarmJson>>() {
                    @Override
                    public void onSuccess(HttpResponse<GetSchemeAlarmJson> panoramaImgBean) {
//                        getIView().hideLoading();
                        getIView().onGetSchemeAlarmSuccess(panoramaImgBean.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
//                        getIView().hideLoading();
                        getIView().onGetSchemeAlarmFail(msg.getMessage());
                    }
                });
    }

    /**
     * 获取区域(巡航)列表
     *
     * @param schemeID
     * @param uid
     */
    @Override
    public void getDatSchemeAreaList(String schemeID, String uid) {
        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getDatSchemeAreaList(schemeID, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<GetDatSchemeAreaListJson>>() {
                    @Override
                    public void onSuccess(HttpResponse<GetDatSchemeAreaListJson> json) {
//                        getIView().hideLoading();
                        getIView().onGetDatSchemeAreaListSuccess(json.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
//                        getIView().hideLoading();
                        getIView().onGetDatSchemeAreaListFail(msg.getMessage());
                    }
                });

    }

    /**
     * 获取定点列表
     *
     * @param schemeID
     * @param uid
     */
    @Override
    public void getDatSchemeFixedList(String schemeID, String uid) {
        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getDatSchemeFixedList(schemeID, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<GetDatSchemeFixedListJson>>() {
                    @Override
                    public void onSuccess(HttpResponse<GetDatSchemeFixedListJson> httpResponse) {
//                        getIView().hideLoading();
                        getIView().onGetDatSchemeFixedListSuccess(httpResponse.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
//                        getIView().hideLoading();
                        getIView().onGetDatSchemeFixedListFail(msg.getMessage());
                    }
                });
    }

    /**
     * 获取区域监测点日志top1(最新一条)
     *
     * @param schemeID
     * @param uid
     */
    @Override
    public void getSchemeMonitorLog(String schemeID, String uid) {
//        getIView().showLoading("正在加载中...");
        new PlanLayoutOfPanoramaModel()
                .getSchemeMonitorLog(schemeID, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<GetSchemeMonitorLogJson>>() {
                    @Override
                    public void onSuccess(HttpResponse<GetSchemeMonitorLogJson> httpResponse) {
//                        getIView().hideLoading();
                        getIView().getSchemeMonitorLogSuccess(httpResponse.getData());
                    }

                    @Override
                    public void onFail(ApiException msg) {
//                        getIView().hideLoading();
                        getIView().getSchemeMonitorLogFail(msg.getMessage());
                    }
                });
    }
}
