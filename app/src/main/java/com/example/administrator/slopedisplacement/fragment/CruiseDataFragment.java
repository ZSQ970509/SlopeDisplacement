package com.example.administrator.slopedisplacement.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.adapter.CruiseDataAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpLazyFragment;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeMonitorListLogJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.CruiseDataContact;
import com.example.administrator.slopedisplacement.mvp.presenter.CruiseDataPresenter;
import com.example.administrator.slopedisplacement.utils.JumpToUtils;
import com.example.administrator.slopedisplacement.utils.TimePickerUtils;
import com.example.administrator.slopedisplacement.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 巡航流水数据
 */

public class CruiseDataFragment extends BaseMvpLazyFragment<CruiseDataPresenter> implements CruiseDataContact.View {
    @BindView(R.id.rvCruiseData)
    RecyclerView mRecyclerView;
    @BindView(R.id.btCruiseDataStart)
    TextView mTvCruiseDataStart;
    @BindView(R.id.btCruiseDataEnd)
    TextView mTvCruiseDataEnd;
    /**
     * 区域下拉列表
     */
    @BindView(R.id.spnCruiseDataArea)
    Spinner mSpnCruiseDataArea;
    /**
     * 监控点下拉列表
     */
    @BindView(R.id.spnCruiseDataMonitor)
    Spinner mSpnCruiseDataMonitor;

    private CruiseDataAdapter mAdapter;
    private GetDatSchemeAreaListJson mArealListJson;

    private List<GetSchemeMonitorListLogJson.ListBean> mAdapterData = new ArrayList<>();
    private int mPageIndex = 1;//当前的页数
    private int mPageSize = 15;//每页的数量
    private int mPageSizeNum = 0;//数据的总数
    /**
     * 用户选中的开始时间
     */
    private String mSelectTimeStart = "";
    /**
     * 用户选中的结束时间
     */
    private String mSelectTimeEnd = "";
    /**
     * 区域下拉列表里显示的数据
     */
    List<GetDatSchemeAreaListJson.ListBean> mAreaList = new ArrayList<>();
    /**
     * 监测点下拉列表里显示的数据
     */
    List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> mNewMonitorList = new ArrayList<>();
    private CommonAdapter<GetDatSchemeAreaListJson.ListBean> mSpnAdapterArea;
    private CommonAdapter<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> mSpnAdapterMonitor;
    private String mSchemeID = "";

    @Override
    protected CruiseDataPresenter loadPresenter() {
        return new CruiseDataPresenter();
    }

    public static CruiseDataFragment newInstance(GetDatSchemeAreaListJson areaListJson, String schemeID) {
        CruiseDataFragment cruiseDataFragment = new CruiseDataFragment();
        if (areaListJson == null || areaListJson.getList() == null || areaListJson.getList().isEmpty()) {
            cruiseDataFragment.setDataEmpty();
        } else {
            cruiseDataFragment.mArealListJson = areaListJson;
            cruiseDataFragment.mSchemeID = schemeID;
        }
        return cruiseDataFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cruise_data;
    }

    @Override
    public void initView() {
        if (mIsDataEmpty)
            return;
        mAdapter = new CruiseDataAdapter(R.layout.item_cruise_data, mAdapterData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        //加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mPageIndex++;
            if (mPageIndex > (mPageSizeNum + mPageSize - 1) / mPageSize) {
                showToast("已经是最后一页了");
                mAdapter.loadMoreEnd();
            } else {
                getCruiseData();
            }
        }, mRecyclerView);
        mAdapter.setOnItemChildClickListener((BaseQuickAdapter baseQuickAdapter, View view, int i) -> {
            switch (view.getId()) {
                case R.id.tvItemCruiseDataNowShift:
                    GetSchemeMonitorListLogJson.ListBean item = (GetSchemeMonitorListLogJson.ListBean) baseQuickAdapter.getItem(i);
                    JumpToUtils.toShowMonitoringImgActivity(getActivity(), item.getPhoto1(), item.getPhoto2(), mPageSizeNum - i, item.getMonitorID(), item.getNowShift());
                    break;
            }
        });
        initSpnArea(mArealListJson.getList());
        initSpnMonitor(mArealListJson.getList().get(mSpnCruiseDataArea.getSelectedItemPosition()).getNewMonitor());

    }

    /**
     * 初始化区域的Spinner,并添加默认"全部"选项
     *
     * @param list 数据
     */
    private void initSpnArea(List<GetDatSchemeAreaListJson.ListBean> list) {
        mSpnAdapterArea = new CommonAdapter<GetDatSchemeAreaListJson.ListBean>(getActivity(), R.layout.item_spinner, mAreaList) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeAreaListJson.ListBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getAreaNmae());
            }
        };

        mSpnCruiseDataArea.setAdapter(mSpnAdapterArea);
        mSpnCruiseDataArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetDatSchemeAreaListJson.ListBean list = (GetDatSchemeAreaListJson.ListBean) parent.getAdapter().getItem(position);
                initSpnMonitor(list.getNewMonitor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mAreaList.clear();
        mSpnAdapterArea.clear();
        GetDatSchemeAreaListJson.ListBean defaultArea = new GetDatSchemeAreaListJson.ListBean();
        defaultArea.setAreaID("");
        defaultArea.setAreaNmae("全部");
        mAreaList.add(defaultArea);
        if (list != null && list.size() != 0)
            mAreaList.addAll(list);
        mSpnAdapterArea.addAll(mAreaList);
        mSpnAdapterArea.notifyDataSetChanged();
        mSpnCruiseDataArea.setSelection(0);
    }

    /**
     * 初始化监控点的Spinner,并添加默认"全部"选项
     *
     * @param list 数据
     */
    private void initSpnMonitor(List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> list) {
        mSpnAdapterMonitor = new CommonAdapter<GetDatSchemeAreaListJson.ListBean.NewMonitorBean>(getActivity(), R.layout.item_spinner, mNewMonitorList) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeAreaListJson.ListBean.NewMonitorBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getMonitorID());
            }
        };
        mSpnCruiseDataMonitor.setAdapter(mSpnAdapterMonitor);
        mNewMonitorList.clear();
        mSpnAdapterMonitor.clear();
        GetDatSchemeAreaListJson.ListBean.NewMonitorBean defaultNewMonitor = new GetDatSchemeAreaListJson.ListBean.NewMonitorBean();
        defaultNewMonitor.setMonitorID("全部");
        mNewMonitorList.add(defaultNewMonitor);
        if (list != null && list.size() != 0)
            mNewMonitorList.addAll(list);
        mSpnAdapterMonitor.addAll(mNewMonitorList);
        mSpnAdapterMonitor.notifyDataSetChanged();
        mSpnCruiseDataMonitor.setSelection(0);
    }

    @Override
    protected void lazyLoad() {
        if (mIsFragmentVisible && mIsInitView && mIsFirstLazyLoad) {
            mIsFirstLazyLoad = false;
            if (mIsDataEmpty) {
                showToast(R.string.view_empty);
            } else {
                getCruiseData();
            }
        }
    }

    /**
     * 获取巡航流水数据
     */
    private void getCruiseData() {
        String areaID = mAreaList.get(mSpnCruiseDataArea.getSelectedItemPosition()).getAreaID() + "";
        String monitorID = mNewMonitorList.get(mSpnCruiseDataMonitor.getSelectedItemPosition()).getMonitorID() + "";
        if (monitorID.equals("全部"))
            monitorID = "";
        mPresenter.getSchemeMonitorListLog(mSchemeID, areaID, mSelectTimeStart, mSelectTimeEnd, monitorID, mPageIndex, mPageSize, UserInfoPref.getUserId());
    }

    @Override
    public void onGetSchemeMonitorListLogSuccess(GetSchemeMonitorListLogJson jsonData) {
        mPageSizeNum = jsonData.getTotalCount();
        if (mAdapter != null) {
            mAdapter.setTotalCount(jsonData.getTotalCount());
            mAdapter.addData(jsonData.getList());
            if (mAdapter.isLoading())
                mAdapter.loadMoreComplete();
        }
    }


    @Override
    public void onGetSchemeMonitorListLogFail(String msg) {
        showToast(msg);
        if (mAdapter != null && mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @OnClick({R.id.btCruiseDataStart, R.id.btCruiseDataEnd, R.id.btCruiseDataSearch})
    void OnClick(View view) {
        if (mIsDataEmpty) {
            showToast(R.string.view_empty);
            return;
        }
        switch (view.getId()) {
            case R.id.btCruiseDataStart:
                TimePickerUtils.showPickerView(getActivity(), "开始时间", mTvCruiseDataStart, mSelectTimeStart, "1234-10-11", mTvCruiseDataEnd.getText().toString());
                break;
            case R.id.btCruiseDataEnd:
                TimePickerUtils.showPickerView(getActivity(), "结束时间", mTvCruiseDataEnd, mSelectTimeEnd, mTvCruiseDataStart.getText().toString(), "");
                break;
            case R.id.btCruiseDataSearch://搜索
                //初始化数据
                mPageIndex = 1;
                mPageSizeNum = 0;
                mSelectTimeStart = mTvCruiseDataStart.getText().toString();
                mSelectTimeEnd = mTvCruiseDataEnd.getText().toString();
                mAdapterData.clear();
                mAdapter.setTotalCount(0);
                mAdapter.setNewData(null);//重新开启下拉加载更多
                getCruiseData();
                break;
        }
    }
}