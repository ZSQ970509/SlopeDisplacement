package com.example.administrator.slopedisplacement.mvp.model;


import com.example.administrator.slopedisplacement.http.NetTransformer;
import com.example.administrator.slopedisplacement.http.RetrofitUtils;
import com.example.administrator.slopedisplacement.mvp.IModel;

import io.reactivex.Observable;

/**
 * 定点折线图Model
 */

public class FixedPointCurveAreaMapModel implements IModel {
    public Observable GetSchemeFixedChartsByDateTop(String schemeID, String fixedId,  String topNum, int timeType, String selDate, String uid) {
        return RetrofitUtils.Instance
                .getApiService()
                .GetSchemeFixedChartsByDateTop(schemeID, fixedId, topNum, timeType, selDate, uid)
                .compose(NetTransformer.compose());
    }
}
