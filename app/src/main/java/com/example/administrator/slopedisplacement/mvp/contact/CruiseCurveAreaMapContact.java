package com.example.administrator.slopedisplacement.mvp.contact;


import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeMonitorListLogJson;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.IView;

import java.util.List;

/**
 * 巡航流水数据Contact
 */

public class CruiseCurveAreaMapContact {
    public interface View extends IView {
        void onGetNewSchemeMonitorChartsByDateTopSuccess(HttpResponse<List<AreaMapBean>> json);

        void onGetNewSchemeMonitorChartsByDateTopFail(String msg);
    }

    public interface Presenter {
        void getNewSchemeMonitorChartsByDateTop(String schemeID, String areaID,  String monitorID, int timeType, String selDate, String uid);
    }
}
