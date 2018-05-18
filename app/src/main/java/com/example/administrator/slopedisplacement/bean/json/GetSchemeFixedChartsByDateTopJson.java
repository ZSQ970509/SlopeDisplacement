package com.example.administrator.slopedisplacement.bean.json;

import java.util.List;
public class GetSchemeFixedChartsByDateTopJson {

    /**
     * Obd : [47.444,47.442,47.452,47.439,47.439,47.439,47.443,47.442,47.437,47.434,47.431,47.438,47.436,47.444,47.452,47.433,52.65,52.651,52.653,52.647,52.648,52.648,52.647,52.647,52.647,52.647,52.646,52.646,52.647,52.647,52.645,52.646,52.644,52.645,52.643,52.643,52.642,52.64,52.641,52.64,52.638,52.637,52.636,52.637,52.635,52.636,52.636]
     * Shift : [3.524,-0.002,0.008,-0.005,-0.005,-0.005,-0.001,-0.002,-0.007,-0.01,-0.013,0.007,0.005,0.013,0.008,-0.011,5.206,0.001,0.003,-0.003,-0.002,-0.002,-0.003,-0.003,-0.003,-0.003,-0.004,-0.004,-0.003,-0.003,-0.005,-0.004,-0.006,-0.005,-0.007,-0.007,-0.008,-0.01,-0.009,-0.01,-0.012,-0.013,-0.001,0,-0.002,-0.001,-0.001]
     * AddShift : [3.524,3.522,3.532,3.519,3.519,3.519,3.523,3.522,3.517,3.514,3.511,3.518,3.516,3.524,3.532,3.513,8.73,8.731,8.733,8.727,8.728,8.728,8.727,8.727,8.727,8.727,8.726,8.726,8.727,8.727,8.725,8.726,8.724,8.725,8.723,8.723,8.722,8.72,8.721,8.72,8.718,8.717,8.716,8.717,8.715,8.716,8.716]
     * NowShift : [3.524,-0.002,0.01,-0.013,0,0,0.004,-0.001,-0.005,-0.003,-0.003,0.007,-0.002,0.008,0.008,-0.019,5.217,0.001,0.002,-0.006,0.001,0,-0.001,0,0,0,-0.001,0,0.001,0,-0.002,0.001,-0.002,0.001,-0.002,0,-0.001,-0.002,0.001,-0.001,-0.002,-0.001,-0.001,0.001,-0.002,0.001,0]
     * timeName : ["2018-04-24 11:31","2018-04-24 11:32","2018-04-24 11:34","2018-04-24 11:40","2018-04-24 11:41","2018-04-24 11:42","2018-04-24 11:43","2018-04-24 11:47","2018-04-24 11:49","2018-04-24 11:50","2018-04-24 11:52","2018-04-24 11:54","2018-04-24 11:56","2018-04-24 11:58","2018-04-24 12:00","2018-04-24 12:02","2018-04-24 12:55","2018-04-24 12:57","2018-04-24 13:00","2018-04-24 13:02","2018-04-24 13:04","2018-04-24 13:06","2018-04-24 13:08","2018-04-24 13:10","2018-04-24 13:12","2018-04-24 13:14","2018-04-24 13:16","2018-04-24 13:18","2018-04-24 13:19","2018-04-24 13:21","2018-04-24 13:23","2018-04-24 13:24","2018-04-24 13:25","2018-04-24 13:27","2018-04-24 13:29","2018-04-24 13:31","2018-04-24 13:33","2018-04-24 13:35","2018-04-24 13:37","2018-04-24 13:39","2018-04-24 13:41","2018-04-24 13:43","2018-04-24 13:45","2018-04-24 13:47","2018-04-24 13:49","2018-04-24 13:51","2018-04-24 13:53"]
     * recordID : [93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180]
     * name : 摩托
     * id : 19
     * data : [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
     */

    private String name;
    private String id;
    private List<Double> Obd;
    private List<Double> Shift;
    private List<Double> AddShift;
    private List<Double> NowShift;
    private List<String> timeName;
    private List<Double> recordID;
    private List<Double> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getObd() {
        return Obd;
    }

    public void setObd(List<Double> Obd) {
        this.Obd = Obd;
    }

    public List<Double> getShift() {
        return Shift;
    }

    public void setShift(List<Double> Shift) {
        this.Shift = Shift;
    }

    public List<Double> getAddShift() {
        return AddShift;
    }

    public void setAddShift(List<Double> AddShift) {
        this.AddShift = AddShift;
    }

    public List<Double> getNowShift() {
        return NowShift;
    }

    public void setNowShift(List<Double> NowShift) {
        this.NowShift = NowShift;
    }

    public List<String> getTimeName() {
        return timeName;
    }

    public void setTimeName(List<String> timeName) {
        this.timeName = timeName;
    }

    public List<Double> getRecordID() {
        return recordID;
    }

    public void setRecordID(List<Double> recordID) {
        this.recordID = recordID;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
}
