package com.example.administrator.slopedisplacement.type;

import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedChartsByDateTopJson;

import java.util.List;

/**
 * 折线图里的位移数据类型
 */

public class ShiftData {
    //阶段位移   累计位移   单次位移    距离
    private int mPosition;

    public ShiftData(int position) {
        mPosition = position;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public List<Double> getShiftDataList(GetSchemeFixedChartsByDateTopJson dateTopJson) {
        switch (mPosition) {
            case 0:
                return dateTopJson.getShift();
            case 1:
                return dateTopJson.getAddShift();
            case 2:
                return dateTopJson.getNowShift();
            case 3:
                return dateTopJson.getObd();
            default:
                return dateTopJson.getShift();
        }
    }

    public List<Double> getShiftDataList(AreaMapBean dateTopJson) {
        switch (mPosition) {
            case 0:
                return dateTopJson.getShift();
            case 1:
                return dateTopJson.getAddShift();
            case 2:
                return dateTopJson.getNowShift();
            case 3:
                return dateTopJson.getObd();
            default:
                return dateTopJson.getShift();
        }
    }
}
