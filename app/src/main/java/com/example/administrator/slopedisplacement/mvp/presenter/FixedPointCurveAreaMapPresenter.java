package com.example.administrator.slopedisplacement.mvp.presenter;


import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedChartsByDateTopJson;
import com.example.administrator.slopedisplacement.exception.ApiException;
import com.example.administrator.slopedisplacement.http.HttpObserver;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.BasePresenter;
import com.example.administrator.slopedisplacement.mvp.contact.CruiseCurveAreaMapContact;
import com.example.administrator.slopedisplacement.mvp.contact.FixedPointCurveAreaMapContact;
import com.example.administrator.slopedisplacement.mvp.model.CruiseCurveAreaMapModel;
import com.example.administrator.slopedisplacement.mvp.model.FixedPointCurveAreaMapModel;

import java.util.List;

/**
 * 定点折线图Presenter
 */

public class FixedPointCurveAreaMapPresenter extends BasePresenter<FixedPointCurveAreaMapContact.View> implements FixedPointCurveAreaMapContact.Presenter {
    @Override
    public void GetSchemeFixedChartsByDateTop(String schemeID, String fixedId, int timeType, String selDate, String uid) {
        new FixedPointCurveAreaMapModel()
                .GetSchemeFixedChartsByDateTop(schemeID, fixedId, "", timeType, selDate, uid)
                .compose(getIView().bindLifecycle())
                .subscribe(new HttpObserver<HttpResponse<List<GetSchemeFixedChartsByDateTopJson>>>() {
                    @Override
                    public void onSuccess(HttpResponse<List<GetSchemeFixedChartsByDateTopJson>> json) {
                        getIView().hideLoading();
                        getIView().onGetSchemeFixedChartsByDateTopSuccess(json);
                    }

                    @Override
                    public void onFail(ApiException msg) {
                        getIView().hideLoading();
                        getIView().onGetSchemeFixedChartsByDateTopFail(msg.getMessage());
                    }
                });
    }


}
