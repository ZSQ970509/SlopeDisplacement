package com.example.administrator.slopedisplacement.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.base.BaseMvpLazyFragment;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedChartsByDateTopJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.contact.FixedPointCurveAreaMapContact;
import com.example.administrator.slopedisplacement.mvp.presenter.FixedPointCurveAreaMapPresenter;
import com.example.administrator.slopedisplacement.type.ShiftData;
import com.example.administrator.slopedisplacement.utils.FormatUtils;
import com.example.administrator.slopedisplacement.utils.L;
import com.example.administrator.slopedisplacement.utils.TimePickerUtils;
import com.example.administrator.slopedisplacement.utils.chart.ChartDataTypeEnum;
import com.example.administrator.slopedisplacement.utils.chart.ChartUtils;
import com.example.administrator.slopedisplacement.utils.chart.CustomAxisX;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 定点曲线区域图
 */

public class FixedPointCurveAreaMapFragment extends BaseMvpLazyFragment<FixedPointCurveAreaMapPresenter> implements FixedPointCurveAreaMapContact.View {
    @BindView(R.id.tvFixedTime)
    TextView tvFixedTime;

    @BindView(R.id.spnFixedPointData)
    Spinner spnFixedPointData;
    @BindView(R.id.spnFixedPointDataType)
    Spinner spnFixedPointDataType;
    @BindView(R.id.tvFixedMapName)
    TextView tvFixedMapName;
    @BindView(R.id.lcFixedPoint)
    LineChart mChart;
    @BindView(R.id.btnFixedTimeForDay)
    TextView tvDay;
    @BindView(R.id.btnFixedTimeForMonth)
    TextView tvMonth;
    @BindView(R.id.btnFixedTimeForYear)
    TextView tvYear;
    private CommonAdapter<GetDatSchemeFixedListJson.ListBean> mSpnAdapterData;
    private CommonAdapter<String> mSpnAdapterDataType;
    private int mTimeType = 2;
    private String mSchemeId;
    private GetDatSchemeFixedListJson mFixedListJson;
    private List<GetSchemeFixedChartsByDateTopJson> mFixedChartData = new ArrayList<>();//折线图数据
    private ShiftData mShiftData = new ShiftData(0);

    public static FixedPointCurveAreaMapFragment newInstance(GetDatSchemeFixedListJson fixedListJson, String schemeId) {
        FixedPointCurveAreaMapFragment fixedPointCurveAreaMapFragment = new FixedPointCurveAreaMapFragment();
        if (fixedListJson == null || fixedListJson.getList() == null || fixedListJson.getList().isEmpty()) {
            fixedPointCurveAreaMapFragment.setDataEmpty();
        } else {
            fixedPointCurveAreaMapFragment.mSchemeId = schemeId;
            fixedPointCurveAreaMapFragment.mFixedListJson = fixedListJson;
        }
        return fixedPointCurveAreaMapFragment;
    }

    @Override
    protected FixedPointCurveAreaMapPresenter loadPresenter() {
        return new FixedPointCurveAreaMapPresenter();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_fixed_point_curve_area_map;
    }

    @Override
    public void initView() {
        ChartUtils.initLineChart(mChart, this.getContext());
        if (mIsDataEmpty)
            return;
        initSpnArea();
        initSpnCruiseDataTypeMap();
        //为图表设置一个选择监听器
    }

    /**
     * 初始化定点的Spinner
     */
    private void initSpnArea() {
        mSpnAdapterData = new CommonAdapter<GetDatSchemeFixedListJson.ListBean>(getActivity(), R.layout.item_spinner, mFixedListJson.getList()) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeFixedListJson.ListBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getFixedName());
            }
        };

        spnFixedPointData.setAdapter(mSpnAdapterData);
        spnFixedPointData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (mFixedListJson.getList() != null && mFixedListJson.getList().size() >= 0) {
            spnFixedPointData.setSelection(0);
        }
        mSpnAdapterData.notifyDataSetChanged();
    }

    /**
     * 初始化数据类型的spinner
     */
    private void initSpnCruiseDataTypeMap() {
        ArrayList<String> dataType = new ArrayList<String>();
        dataType.add("阶段位移");
        dataType.add("累计位移");
        dataType.add("单次位移");
        dataType.add("距离");
        mSpnAdapterDataType = new CommonAdapter<String>(getActivity(), R.layout.item_spinner, dataType) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.tvItemSpinner, item);
            }
        };
        spnFixedPointDataType.setAdapter(mSpnAdapterDataType);
        spnFixedPointDataType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mShiftData.setPosition(spnFixedPointDataType.getSelectedItemPosition());
                setChartData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (mIsInitView) {
            if (mIsFirstLazyLoad && mIsFragmentVisible) {//只在第一次懒加载&&视图为显示时执行
                if (mIsDataEmpty) {
                    showToast(R.string.view_empty);
                } else {
                    refreshData();
                }
                mIsFirstLazyLoad = false;
            }
        }
    }

    // 设置图表里的数据
    private void setChartData() {
        if (mFixedChartData == null || mFixedChartData.isEmpty() || mFixedChartData.size() < 3 || mFixedChartData.get(0).getTimeName() == null || mFixedChartData.get(0).getTimeName().isEmpty()) {
            L.e("折线图的数据为空");
            mChart.clear();
            return;
        }
        // 添加线（数据）  （数据，位置，颜色，名称）
        LineDataSet setData1 = ChartUtils.getLineDataSet(mChart, mShiftData.getShiftDataList(mFixedChartData.get(0)), 0, Color.RED, mFixedChartData.get(0).getName(), ChartDataTypeEnum.DATA1);
        LineDataSet setData2 = ChartUtils.getLineDataSet(mChart, mFixedChartData.get(1).getData(), 1, Color.rgb(255, 215, 0), mFixedChartData.get(1).getName(), ChartDataTypeEnum.DATA2);
        LineDataSet setData3 = ChartUtils.getLineDataSet(mChart, mFixedChartData.get(2).getData(), 2, Color.rgb(255, 215, 0), mFixedChartData.get(2).getName(), ChartDataTypeEnum.DATA3);
        mChart.setData(new LineData(setData1, setData2, setData3));
        // 获取Legend(图例)  （仅在设置数据后才可以）
        ChartUtils.setLegend(mChart.getLegend());
        ChartUtils.setLeftYAxis(mChart.getAxisLeft());
        ChartUtils.setXAxis(mChart.getXAxis(), mFixedChartData.get(0).getTimeName());
        //刷新图表
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    /**
     * 刷新图表数据
     */
    private void refreshData() {
        mPresenter.GetSchemeFixedChartsByDateTop(mSchemeId, mSpnAdapterData.getItem(spnFixedPointData.getSelectedItemPosition()).getFixedID() + ""
                , mTimeType, tvFixedTime.getText().toString(), UserInfoPref.getUserId());
    }

    @Override
    public void onGetSchemeFixedChartsByDateTopSuccess(HttpResponse<List<GetSchemeFixedChartsByDateTopJson>> json) {
        mFixedChartData = json.getData();
        setChartData();
    }

    @Override
    public void onGetSchemeFixedChartsByDateTopFail(String msg) {
        showMsg(msg);
    }

    @OnClick({R.id.btnFixedTimeForDay, R.id.btnFixedTimeForMonth, R.id.btnFixedTimeForYear})
    void onClick(View view) {
        if (mIsDataEmpty) {
            showToast(R.string.view_empty);
            return;
        }
        switch (view.getId()) {
            case R.id.btnFixedTimeForDay:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvFixedTime, true, true, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvFixedTime.setText(FormatUtils.dateToString(date, true, true));
                        tvFixedMapName.setText("定点日线图");
                        mTimeType = 2;
                        tvDay.setBackgroundResource(R.drawable.btn_blue);
                        tvMonth.setBackgroundResource(R.color.white);
                        tvYear.setBackgroundResource(R.color.white);
                        refreshData();
                    }
                });
                break;
            case R.id.btnFixedTimeForMonth:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvFixedTime, true, false, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvFixedTime.setText(FormatUtils.dateToString(date, true, false));
                        tvFixedMapName.setText("定点月线图");
                        tvDay.setBackgroundResource(R.color.white);
                        tvMonth.setBackgroundResource(R.drawable.btn_blue);
                        tvYear.setBackgroundResource(R.color.white);
                        mTimeType = 1;
                        refreshData();
                    }
                });
                break;
            case R.id.btnFixedTimeForYear:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvFixedTime, false, false, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvFixedMapName.setText("定点年线图");
                        mTimeType = 0;
                        tvFixedTime.setText(FormatUtils.dateToString(date, false, false));
                        tvDay.setBackgroundResource(R.color.white);
                        tvMonth.setBackgroundResource(R.color.white);
                        tvYear.setBackgroundResource(R.drawable.btn_blue);
                        refreshData();
                    }
                });
                break;
        }
    }
}