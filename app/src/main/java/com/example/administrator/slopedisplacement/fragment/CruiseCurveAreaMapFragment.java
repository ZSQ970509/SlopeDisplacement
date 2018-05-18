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
import com.example.administrator.slopedisplacement.bean.AreaMapBean;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.http.HttpResponse;
import com.example.administrator.slopedisplacement.mvp.contact.CruiseCurveAreaMapContact;
import com.example.administrator.slopedisplacement.mvp.presenter.CruiseCurveAreaMapPresenter;
import com.example.administrator.slopedisplacement.type.ShiftData;
import com.example.administrator.slopedisplacement.utils.FormatUtils;
import com.example.administrator.slopedisplacement.utils.L;
import com.example.administrator.slopedisplacement.utils.chart.ChartDataTypeEnum;
import com.example.administrator.slopedisplacement.utils.chart.ChartMarkerDataBean;
import com.example.administrator.slopedisplacement.utils.chart.ChartMarkerView;
import com.example.administrator.slopedisplacement.utils.chart.ChartUtils;
import com.example.administrator.slopedisplacement.utils.TimePickerUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 巡航曲线区域图
 */

public class CruiseCurveAreaMapFragment extends BaseMvpLazyFragment<CruiseCurveAreaMapPresenter> implements CruiseCurveAreaMapContact.View {
    private GetDatSchemeAreaListJson mArealListJson;
    @BindView(R.id.spnCruiseDataAreaMap)
    Spinner spnCruiseDataAreaMap;
    @BindView(R.id.spnCruiseDataMonitorMap)
    Spinner spnCruiseDataMonitorMap;
    @BindView(R.id.spnCruiseDataTypeMap)
    Spinner spnCruiseDataTypeMap;
    @BindView(R.id.tvTimeMap)
    TextView tvTimeMap;
    @BindView(R.id.tvMapName)
    TextView tvMapName;
    @BindView(R.id.lcAreaMap)
    LineChart lcAreaMap;
    @BindView(R.id.btnTimeForDayMap)
    TextView tvDay;
    @BindView(R.id.btnTimeForMonthMap)
    TextView tvMonth;
    @BindView(R.id.btnTimeForYearMap)
    TextView tvYear;
    private int mTimeType = 2;
    private CommonAdapter<String> mSpnAdapterDataType;
    private CommonAdapter<GetDatSchemeAreaListJson.ListBean> mSpnAdapterArea;
    private CommonAdapter<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> mSpnAdapterMonitor;
    private String mSchemeID = "";
    private ShiftData mShiftData = new ShiftData(0);
    private List<AreaMapBean> mAreaChartData = new ArrayList<>();//折线图数据

    public static CruiseCurveAreaMapFragment newInstance(GetDatSchemeAreaListJson areaListJson, String schemeID) {
        CruiseCurveAreaMapFragment cruiseDataFragment = new CruiseCurveAreaMapFragment();
        if (areaListJson == null || areaListJson.getList() == null || areaListJson.getList().isEmpty()) {
            cruiseDataFragment.setDataEmpty();
        } else {
            cruiseDataFragment.mArealListJson = areaListJson;
            cruiseDataFragment.mSchemeID = schemeID;
        }
        return cruiseDataFragment;
    }

    @Override
    protected CruiseCurveAreaMapPresenter loadPresenter() {
        return new CruiseCurveAreaMapPresenter();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cruise_curve_area_map;
    }

    @Override
    public void initView() {
        ChartUtils.initLineChart(lcAreaMap, this.getContext());
        if (mIsDataEmpty)
            return;
        initSpnArea();
        initSpnCruiseDataTypeMap();
        initSpnCruiseDataTypeMap();
    }

    /**
     * 初始化区域的Spinner
     */
    private void initSpnArea() {
        mSpnAdapterArea = new CommonAdapter<GetDatSchemeAreaListJson.ListBean>(getActivity(), R.layout.item_spinner, mArealListJson.getList()) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeAreaListJson.ListBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getAreaNmae());
            }
        };
        spnCruiseDataAreaMap.setAdapter(mSpnAdapterArea);
        spnCruiseDataAreaMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetDatSchemeAreaListJson.ListBean list = (GetDatSchemeAreaListJson.ListBean) parent.getAdapter().getItem(position);
                initSpnMonitor(list.getNewMonitor());
                refreshData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (mArealListJson.getList() != null && mArealListJson.getList().size() != 0) {
            spnCruiseDataAreaMap.setSelection(0);
        }
        mSpnAdapterArea.notifyDataSetChanged();
    }

    /**
     * 初始化数据类型
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
        spnCruiseDataTypeMap.setAdapter(mSpnAdapterDataType);
        spnCruiseDataTypeMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mShiftData.setPosition(spnCruiseDataTypeMap.getSelectedItemPosition());
                setChartData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spnCruiseDataTypeMap.setSelection(0);
    }

    /**
     * 初始化监控点的Spinner,并添加默认"全部"选项
     *
     * @param list 数据
     */
    private void initSpnMonitor(List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> list) {
        mSpnAdapterMonitor = new CommonAdapter<GetDatSchemeAreaListJson.ListBean.NewMonitorBean>(getActivity(), R.layout.item_spinner, list) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeAreaListJson.ListBean.NewMonitorBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getMonitorID());
            }
        };
        spnCruiseDataMonitorMap.setAdapter(mSpnAdapterMonitor);
        spnCruiseDataMonitorMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (list != null && list.size() != 0)
            spnCruiseDataMonitorMap.setSelection(0);
        mSpnAdapterMonitor.notifyDataSetChanged();
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

    private void refreshData() {
        if (mIsInitView && mIsFragmentVisible) {
            String areaID = mSpnAdapterArea.getItem(spnCruiseDataAreaMap.getSelectedItemPosition()).getAreaID() + "";
            String monitorID = mSpnAdapterMonitor.getItem(spnCruiseDataMonitorMap.getSelectedItemPosition()).getMonitorID() + "";
            mPresenter.getNewSchemeMonitorChartsByDateTop(mSchemeID, areaID, monitorID, mTimeType, tvTimeMap.getText().toString(), UserInfoPref.getUserId());
        }
    }

    // 设置图表里的数据
    private void setChartData() {
        if (mAreaChartData == null || mAreaChartData.isEmpty() || mAreaChartData.size() < 3 || mAreaChartData.get(0).getTimeName() == null || mAreaChartData.get(0).getTimeName().isEmpty()) {
            L.e("折线图的数据为空");
            lcAreaMap.clear();
            return;
        }
        // 添加线（数据）  （数据，位置，颜色，名称）
        LineDataSet setData1 = ChartUtils.getLineDataSet(lcAreaMap, mShiftData.getShiftDataList(mAreaChartData.get(0)), 0, Color.RED, mAreaChartData.get(0).getName(), ChartDataTypeEnum.DATA1);
        LineDataSet setData2 = ChartUtils.getLineDataSet(lcAreaMap, mAreaChartData.get(1).getData(), 1, Color.rgb(255, 215, 0), mAreaChartData.get(1).getName(), ChartDataTypeEnum.DATA2);
        LineDataSet setData3 = ChartUtils.getLineDataSet(lcAreaMap, mAreaChartData.get(2).getData(), 2, Color.rgb(255, 215, 0), mAreaChartData.get(2).getName(), ChartDataTypeEnum.DATA3);
        lcAreaMap.setData(new LineData(setData1, setData2, setData3));
        ChartMarkerDataBean markerData = new ChartMarkerDataBean();
        markerData.setDataName1(mAreaChartData.get(0).getName());
        markerData.setDataName2(mAreaChartData.get(1).getName());
        markerData.setDataName3(mAreaChartData.get(2).getName());
        markerData.setData1(mShiftData.getShiftDataList(mAreaChartData.get(0)));
        markerData.setData2(mAreaChartData.get(1).getData());
        markerData.setData3(mAreaChartData.get(2).getData());
        lcAreaMap.setMarker(new ChartMarkerView(getContext(), markerData));
//        ( (ChartMarkerView)lcAreaMap.getMarker()).
        // 获取Legend(图例)  （仅在设置数据后才可以）
        ChartUtils.setLegend(lcAreaMap.getLegend());
        ChartUtils.setLeftYAxis(lcAreaMap.getAxisLeft());
        ChartUtils.setXAxis(lcAreaMap.getXAxis(), mAreaChartData.get(0).getTimeName());
        //刷新图表
        lcAreaMap.notifyDataSetChanged();
        lcAreaMap.invalidate();
    }

    @Override
    public void onGetNewSchemeMonitorChartsByDateTopSuccess(HttpResponse<List<AreaMapBean>> json) {
        mAreaChartData = json.getData();
        setChartData();
    }

    @Override
    public void onGetNewSchemeMonitorChartsByDateTopFail(String msg) {
        showMsg(msg);
    }

    @OnClick({R.id.tvTimeMap, R.id.btnTimeForDayMap, R.id.btnTimeForMonthMap, R.id.btnTimeForYearMap})
    void onClick(View view) {
        if (mIsDataEmpty) {
            showToast(R.string.view_empty);
            return;
        }
        switch (view.getId()) {
            case R.id.tvTimeMap:
                break;
            case R.id.btnTimeForDayMap:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvTimeMap, true, true, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvTimeMap.setText(FormatUtils.dateToString(date, true, true));
                        tvMapName.setText("巡航监测点日线图");
                        tvDay.setBackgroundResource(R.drawable.btn_blue);
                        tvMonth.setBackgroundResource(R.color.white);
                        tvYear.setBackgroundResource(R.color.white);
                        mTimeType = 2;
                        refreshData();
                    }
                });
                break;
            case R.id.btnTimeForMonthMap:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvTimeMap, true, false, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvTimeMap.setText(FormatUtils.dateToString(date, true, false));
                        tvMapName.setText("巡航监测点月线图");
                        tvDay.setBackgroundResource(R.color.white);
                        tvMonth.setBackgroundResource(R.drawable.btn_blue);
                        tvYear.setBackgroundResource(R.color.white);
                        mTimeType = 1;
                        refreshData();
                    }
                });
                break;
            case R.id.btnTimeForYearMap:
                TimePickerUtils.showPickerView(getActivity(), "选择时间", tvTimeMap, false, false, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvMapName.setText("巡航监测点年线图");
                        mTimeType = 0;
                        tvTimeMap.setText(FormatUtils.dateToString(date, false, false));
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