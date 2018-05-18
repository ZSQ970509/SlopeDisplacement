package com.example.administrator.slopedisplacement.utils.chart;

import android.content.Context;
import android.widget.TextView;

import com.example.administrator.slopedisplacement.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


/**
 *
 */

public class ChartMarkerView extends MarkerView {
    private TextView tvContent1;
    private TextView tvContent2;
    private TextView tvContent3;
    private ChartMarkerDataBean mMarkerDataBean;

    public ChartMarkerView(Context context, ChartMarkerDataBean markerDataBean) {
        this(context, R.layout.widget_marker_view);
        mMarkerDataBean = markerDataBean;
        tvContent1 = (TextView) findViewById(R.id.tvContent1);
        tvContent2 = (TextView) findViewById(R.id.tvContent2);
        tvContent3 = (TextView) findViewById(R.id.tvContent3);
    }

    private ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String content1 = "";
        String content2 = "";
        String content3 = "";
        if (mMarkerDataBean != null) {
            content1 = mMarkerDataBean.getDataName1() + "：" + mMarkerDataBean.getData1().get((int) (e.getX()));
            content2 = mMarkerDataBean.getDataName2() + "：" + mMarkerDataBean.getData2().get((int) (e.getX()));
            content3 = mMarkerDataBean.getDataName3() + "：" + mMarkerDataBean.getData3().get((int) (e.getX()));
        }
        tvContent1.setText(content1);
        tvContent2.setText(content2);
        tvContent3.setText(content3);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
