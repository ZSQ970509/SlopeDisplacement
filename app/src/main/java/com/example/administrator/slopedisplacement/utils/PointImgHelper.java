package com.example.administrator.slopedisplacement.utils;

import android.text.TextUtils;

import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.widget.pointImg.LineBean;
import com.example.administrator.slopedisplacement.widget.pointImg.PointBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PointImgHelper {
    /**
     * 区域 状态为面 点偏移百分比
     */
    private final static int pointOffsetScale = 4;

    /**
     * 添加巡航(区域)相关点、线、面
     *
     * @param areaList
     * @param pointBeanList
     * @param lineBeanList
     * @param dottedLineList
     */
    public static void areaPoint(List<GetDatSchemeAreaListJson.ListBean> areaList, ArrayList<PointBean> pointBeanList, List<LineBean> lineBeanList, List<LineBean> dottedLineList) {
        if (areaList == null || areaList.size() == 0)
            return;
        //区域列表里的点线
        for (GetDatSchemeAreaListJson.ListBean area : areaList) {
            if (area == null || TextUtils.isEmpty(area.getAreaType()))
                continue;
            switch (area.getAreaType()) {
                case "1": {//区域
                    double startX;
                    double startY;
                    double endX;
                    double endY;
                    if (area.getNewMonitor() == null || area.getNewMonitor().size() == 0) {//为空只花两个点
                        startX = Double.parseDouble(area.getOx1());
                        startY = Double.parseDouble(area.getOy1());
                        endX = Double.parseDouble(area.getOx2());
                        endY = Double.parseDouble(area.getOy2());
                        String areaName = area.getAreaNmae();
                        PointBean pointStart = new PointBean();
                        pointStart.setXScale(startX);
                        pointStart.setYScale(startY);
                        PointBean pointEnd = new PointBean();
                        pointEnd.setXScale(endX);
                        pointEnd.setYScale(endY);

                        if (startX < endX || (startX == endX && startY > endY)) {
                            pointStart.setPointName(areaName);
                        } else {
                            pointEnd.setPointName(areaName);
                        }
                        //添加newMonitorId用于启动和关闭点的动画
                        List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> newMonitorBeanList = area.getNewMonitor();
                        if (newMonitorBeanList != null && newMonitorBeanList.size() >= 2) {
                            pointStart.setmMonitorID(area.getNewMonitor().get(0).getMonitorID());
                            pointEnd.setmMonitorID(area.getNewMonitor().get(1).getMonitorID());
                        }

                        pointStart.setPointIndex(pointBeanList.size());
                        pointBeanList.add(pointStart);

                        pointEnd.setPointIndex(pointBeanList.size());
                        pointBeanList.add(pointEnd);
                        lineBeanList.add(new LineBean(pointStart.getPointIndex(), pointEnd.getPointIndex()));
                        break;
                    } else {
                        startX = Double.parseDouble(area.getOx1());
                        startY = Double.parseDouble(area.getOy1());
                        endX = Double.parseDouble(area.getOx2());
                        endY = Double.parseDouble(area.getOy2());
                        int num = area.getNewMonitor().size();
                        boolean isPointVertical = onPointVertical(Double.parseDouble(area.getAha1()), Double.parseDouble(area.getAha2()), Double.parseDouble(area.getAva1()), Double.parseDouble(area.getAva2()));
                        int pointBeanListIndex = pointBeanList.size();
                        //添加点
                        for (int i = 0; i < num / 2; i++) {
                            PointBean pointBean1 = new PointBean();
                            double scaleX = pointDistance(startX, endX, i, num / 2 - 1);
                            double scaleY = pointDistance(startY, endY, i, num / 2 - 1);
                            pointBean1.setXScale(scaleX);
                            pointBean1.setYScale(scaleY);
                            pointBean1.setmMonitorID(area.getNewMonitor().get(i * 2).getMonitorID());
                            if (i == 0) {
                                pointBean1.setPointName(area.getAreaNmae());
                            }
                            pointBean1.setPointIndex(pointBeanListIndex + i * 2);

                            PointBean pointBean2 = new PointBean();
                            if (isPointVertical) {
                                pointBean2.setXScale(scaleX + pointOffsetScale);
                                pointBean2.setYScale(scaleY);
                            } else {
                                pointBean2.setXScale(scaleX);
                                pointBean2.setYScale(scaleY + pointOffsetScale);
                            }
                            pointBean2.setmMonitorID(area.getNewMonitor().get(i * 2 + 1).getMonitorID());
                            pointBean2.setPointIndex(pointBeanListIndex + i * 2 + 1);
                            pointBeanList.add(pointBean1);
                            pointBeanList.add(pointBean2);
                        }
                        //添加虚线
                        if (num > 2) {
                            dottedLineList.add(new LineBean(pointBeanList.get(pointBeanListIndex).getPointIndex(), pointBeanList.get(pointBeanListIndex + 1).getPointIndex()));
                            dottedLineList.add(new LineBean(pointBeanList.get(pointBeanListIndex + num - 2).getPointIndex(), pointBeanList.get(pointBeanListIndex + num - 1).getPointIndex()));
                        }
                        //添加实线
                        lineBeanList.add(new LineBean(pointBeanList.get(pointBeanListIndex).getPointIndex(), pointBeanList.get(pointBeanListIndex + num - 2).getPointIndex()));
                        break;
                    }
                }
                case "2": {//线
                    String areaName = area.getAreaNmae();
                    double startX = Double.parseDouble(area.getOx1());
                    double startY = Double.parseDouble(area.getOy1());
                    double endX = Double.parseDouble(area.getOx2());
                    double endY = Double.parseDouble(area.getOy2());

                    PointBean pointStart = new PointBean();
                    pointStart.setXScale(startX);
                    pointStart.setYScale(startY);
                    PointBean pointEnd = new PointBean();
                    pointEnd.setXScale(endX);
                    pointEnd.setYScale(endY);

                    if (startX < endX || (startX == endX && startY > endY)) {
                        pointStart.setPointName(areaName);
                    } else {
                        pointEnd.setPointName(areaName);
                    }
                    //添加newMonitorId用于启动和关闭点的动画
                    List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> newMonitorBeanList = area.getNewMonitor();
                    if (newMonitorBeanList != null && newMonitorBeanList.size() >= 2) {
                        pointStart.setmMonitorID(area.getNewMonitor().get(0).getMonitorID());
                        pointEnd.setmMonitorID(area.getNewMonitor().get(1).getMonitorID());
                    }

                    pointStart.setPointIndex(pointBeanList.size());
                    pointBeanList.add(pointStart);

                    pointEnd.setPointIndex(pointBeanList.size());
                    pointBeanList.add(pointEnd);
                    lineBeanList.add(new LineBean(pointStart.getPointIndex(), pointEnd.getPointIndex()));
                    break;
                }
                case "3": {//点
                    List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> newMonitorBeanList = area.getNewMonitor();
                    int num = newMonitorBeanList.size();
                    String areaName = area.getAreaNmae();
                    for (int i = 0; i < num; i++) {
                        PointBean pointBean = new PointBean();
                        pointBean.setXScale(Double.parseDouble(newMonitorBeanList.get(i).getOx()));
                        pointBean.setYScale(Double.parseDouble(newMonitorBeanList.get(i).getOy()));
                        pointBean.setPointIndex(pointBeanList.size());
                        pointBean.setPointName(areaName + "_" + (i + 1));
                        pointBean.setmMonitorID(newMonitorBeanList.get(i).getMonitorID());
                        pointBeanList.add(pointBean);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 添加定点相关的点
     *
     * @param fixedList
     * @param pointBeanList
     */
    public static void fixedPoint(List<GetDatSchemeFixedListJson.ListBean> fixedList, ArrayList<PointBean> pointBeanList) {
        if (fixedList == null || fixedList.size() == 0)
            return;
        PointBean pointBean;
        for (GetDatSchemeFixedListJson.ListBean fixed : fixedList) {
            pointBean = new PointBean();
            pointBean.setXScale(Double.parseDouble(fixed.getOx()));
            pointBean.setYScale(Double.parseDouble(fixed.getOy()));
            pointBean.setPointIndex(pointBeanList.size());
            pointBean.setPointName(fixed.getFixedName());
            pointBean.setmMonitorID(fixed.getFixedID());
            pointBeanList.add(pointBean);
        }
    }

    /**
     * 点的方向
     *
     * @return true 向右 false 向下
     */
    private static boolean onPointVertical(double aha1, double aha2, double ava1, double ava2) {
        //垂直角度差
        double fTotleAva = Math.abs(ava1 - ava2);
        //垂直角度过零判断（处理）
        if ((ava1 > 180 && ava2 < 180)) {
            fTotleAva = (360 - ava1) + ava2;
        } else if (ava2 > 180 && ava1 < 180) {
            fTotleAva = (360 - ava2) + ava1;
        }
        //水平角度差
        double fTotleAha = Math.abs(aha1 - aha2);

        //水平角度差值与垂直角度差值比较
        //水平(false) 垂直(true)
        return fTotleAha < fTotleAva;
    }

    /**
     * 计算两个点之间n/m处点的坐标 (在start点和end点之间的n/m处点的坐标)
     *
     * @param start 起始位置
     * @param end   结束位置
     * @param n     分子
     * @param m     分母
     * @return 坐标
     */
    private static double pointDistance(double start, double end, int n, int m) {
        return (end - start) * n / m + start;
    }
}
