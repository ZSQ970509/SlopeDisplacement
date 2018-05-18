package com.example.administrator.slopedisplacement.mvp.presenter;


import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeMonitorListLogJson;
import com.example.administrator.slopedisplacement.exception.ApiException;
import com.example.administrator.slopedisplacement.http.HttpObserver;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.BasePresenter;
import com.example.administrator.slopedisplacement.mvp.contact.CruiseCurveAreaMapContact;
import com.example.administrator.slopedisplacement.mvp.contact.CruiseDataContact;
import com.example.administrator.slopedisplacement.mvp.model.CruiseCurveAreaMapModel;
import com.example.administrator.slopedisplacement.mvp.model.CruiseDataModel;

import java.util.List;

/**
 * 巡航流水数据Presenter
 */

public class CruiseCurveAreaMapPresenter extends BasePresenter<CruiseCurveAreaMapContact.View> implements CruiseCurveAreaMapContact.Presenter {
    @Override
    public void getNewSchemeMonitorChartsByDateTop(String schemeID, String areaID,  String monitorID, int timeType, String selDate, String uid) {
        new CruiseCurveAreaMapModel()
                .getNewSchemeMonitorChartsByDateTop(schemeID, areaID, monitorID, timeType, selDate, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<List<AreaMapBean>>>() {
                    @Override
                    public void onSuccess(HttpResponse<List<AreaMapBean>> json) {
                        getIView().hideLoading();
                        getIView().onGetNewSchemeMonitorChartsByDateTopSuccess(json);
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onGetNewSchemeMonitorChartsByDateTopFail(msg.getMessage());
                    }
                });
    }


}
