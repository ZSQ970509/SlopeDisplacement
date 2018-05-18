package com.example.administrator.slopedisplacement.utils.chart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ChartMarkerDataBean {
    private String dataName1;
    private String dataName2;
    private String dataName3;
    private List<Double> data1 = new ArrayList<>();
    private List<Double> data2 = new ArrayList<>();
    private List<Double> data3 = new ArrayList<>();

    public String getDataName1() {
        return dataName1;
    }

    public void setDataName1(String dataName1) {
        this.dataName1 = dataName1;
    }

    public String getDataName2() {
        return dataName2;
    }

    public void setDataName2(String dataName2) {
        this.dataName2 = dataName2;
    }

    public String getDataName3() {
        return dataName3;
    }

    public void setDataName3(String dataName3) {
        this.dataName3 = dataName3;
    }

    public List<Double> getData1() {
        return data1;
    }

    public void setData1(List<Double> data1) {
        this.data1 = data1;
    }

    public List<Double> getData2() {
        return data2;
    }

    public void setData2(List<Double> data2) {
        this.data2 = data2;
    }

    public List<Double> getData3() {
        return data3;
    }

    public void setData3(List<Double> data3) {
        this.data3 = data3;
    }
}
