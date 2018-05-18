package com.example.administrator.slopedisplacement.utils.chart;

import com.example.administrator.slopedisplacement.utils.L;
import com.github.mikephil.charting.charts.LineChart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.administrator.slopedisplacement.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 图表的工具类
 */

public class ChartUtils {
    private final static String TAG = "ChartUtils";

    public static void initLineChart(LineChart lineChart, Context context) {
        lineChart.setNoDataText(context.getString(R.string.view_empty));
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                lineChart.centerViewToAnimated(entry.getX(), entry.getY(), lineChart.getData().getDataSetByIndex(highlight.getDataSetIndex()).getAxisDependency(), 500);
            }

            @Override
            public void onNothingSelected() {
                Log.i("Nothing selected", "Nothing selected.");
            }
        });
        // 不使用描述文本相关信息
        lineChart.getDescription().setEnabled(false);
        // 手势能否触摸图表
        lineChart.setTouchEnabled(true);
        //减速摩擦系数[0,1]之间，0立刻停止，1，自动转换为0.999f
        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // 将其设置为true以启用图表的拖动（用手指移动图表）（这不会影响缩放）。
        lineChart.setDragEnabled(true);
        //将其设置为true以在X轴和Y轴上为图表启用缩放（通过手势放大和缩小）（这不影响拖动）
        lineChart.setScaleEnabled(true);
        //将此设置为true以绘制网格背景，否则为false
        lineChart.setDrawGridBackground(false);
        //将其设置为true以允许在完全缩小时拖动图表曲面时突出显示。 默认值：true
        lineChart.setHighlightPerDragEnabled(true);

        // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
        lineChart.setPinchZoom(true);

        // 设置背景颜色
        lineChart.setBackgroundResource(R.color.white);
        //使用指定的动画时间在x轴上动画显示图表。
        lineChart.animateX(1500);
        //右侧y轴设置为不使用
        lineChart.getAxisRight().setEnabled(false);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();//刷新
    }

    /**
     * 图表下方 线的标示
     *
     * @param l
     */
    public static void setLegend(Legend l) {
        // 设置图例形式的形状 (线)
        l.setForm(Legend.LegendForm.LINE);
        //字体大小
        l.setTextSize(11f);
        //字体颜色
        l.setTextColor(Color.BLACK);
        //设置图例的垂直对齐
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //设置图例的水平对齐
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //设置图例的方向
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例是否绘制在图表内部或外部
        l.setDrawInside(false);
//        l.setYOffset(11f);
    }

    /**
     * 设置自定义的x轴（底部）
     *
     * @param xAxis
     * @param list
     */
    public static void setXAxis(XAxis xAxis, List<String> list) {
        if (list == null || list.isEmpty()) {
            L.e(TAG, "setXAxis的数据为空");
            return;
        }
        CustomAxisX formatter = new CustomAxisX(list);
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        //将其设置为true以启用绘制该轴的网格线。
        xAxis.setDrawGridLines(false);
        //x轴在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);//设置x轴间距
//        xAxis.setLabelCount(7);//设置
        xAxis.setValueFormatter(formatter);
    }

    /**
     * 设置y轴（左侧）
     *
     * @param leftAxis
     */
    public static void setLeftYAxis(YAxis leftAxis) {
        leftAxis.setTextColor(Color.BLACK);//y轴字的颜色
        leftAxis.setSpaceTop(5f);//将顶部轴空间设置为整个范围的百分比。默认10f（即10%）
        //通过调用此方法，先前设置的任何自定义最大值将被重置，并且计算会自动完成。
//        leftAxis.setAxisMaximum(maxNum);
        //通过调用此方法，先前设置的任何自定义最小值将被重置，并自动完成计算。
//        leftAxis.setAxisMinimum(minNum);
        leftAxis.setDrawGridLines(true);    //将此设置为true，以便绘制该轴的网格线
        leftAxis.setGranularityEnabled(false);  //在轴值间隔上启用/禁用粒度控制。
        //y轴单位设置
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "(m)";
            }
        });
    }

    /**
     * 获取线的数据
     *
     * @param lineChart      图表
     * @param dataList       数据
     * @param chartDataIndex 线在图表里对应的index
     * @param color          线和图表下方标示的颜色
     * @param name           图表下方标示的名称
     * @return
     */
    public static LineDataSet getLineDataSet(LineChart lineChart, List<Double> dataList, int chartDataIndex, int color, String name, ChartDataTypeEnum chartDataTypeEnum) {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            yVals.add(new Entry(i, Float.parseFloat(dataList.get(i) + ""), chartDataTypeEnum));
        }
        LineDataSet set;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0 && lineChart.getData().getDataSetByIndex(chartDataIndex) != null) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(chartDataIndex);
            set.setValues(yVals);
        } else {
            set = new LineDataSet(yVals, name);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(color);//设置线的颜色
            set.setLineWidth(2.5f);//设置线的宽度
            if (chartDataTypeEnum == ChartDataTypeEnum.DATA2 || chartDataTypeEnum == ChartDataTypeEnum.DATA3)
                set.enableDashedLine(10f, 10f, 0f);//设置成虚线

            //顶点圆
            set.setCircleRadius(4f);//设置顶点的半径
            set.setCircleColor(color);//设置顶点的颜色

            //顶点上方显示的值
            set.setDrawValues(false);//关闭顶点上方显示y值
            set.setValueTextColor(Color.BLACK);//顶点上方显示的字颜色
//            set.setValueTextSize(10f);//顶点上方显示的字大小

            //指引线
//            set.setHighlightEnabled(false);//关闭指引线宽度
//            set.setHighlightLineWidth(1f); //指引线宽度
            set.setHighLightColor(Color.GRAY); //指引线的颜色。
        }
        return set;
    }
}
