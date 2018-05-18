package com.example.administrator.slopedisplacement.mvp.contact;


import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedChartsByDateTopJson;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.IView;

import java.util.List;

/**
 * 定点折线图数据Contact
 */

public class FixedPointCurveAreaMapContact {
    public interface View extends IView {
        void onGetSchemeFixedChartsByDateTopSuccess(HttpResponse<List<GetSchemeFixedChartsByDateTopJson>> json);

        void onGetSchemeFixedChartsByDateTopFail(String msg);
    }

    public interface Presenter {
        void GetSchemeFixedChartsByDateTop(String schemeID, String fixedId, int timeType, String selDate, String uid);
    }
}
