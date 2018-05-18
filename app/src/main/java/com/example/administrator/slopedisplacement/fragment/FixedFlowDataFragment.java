package com.example.administrator.slopedisplacement.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.example.administrator.slopedisplacement.R;

import com.example.administrator.slopedisplacement.adapter.FixedFlowDataAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpLazyFragment;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedListLogJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.FixedFlowDataContact;
import com.example.administrator.slopedisplacement.mvp.presenter.FixedFlowDataPresenter;
import com.example.administrator.slopedisplacement.utils.TimePickerUtils;
import com.example.administrator.slopedisplacement.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 定点流水数据
 */

public class FixedFlowDataFragment extends BaseMvpLazyFragment<FixedFlowDataPresenter> implements FixedFlowDataContact.View {
    @BindView(R.id.rvFixedFlowData)
    RecyclerView mRecyclerView;
    @BindView(R.id.btFixedFlowDataStart)
    TextView mTvFixedFlowDataStart;
    @BindView(R.id.btFixedFlowDataEnd)
    TextView mTvFixedFlowDataEnd;
    /**
     * 定点下拉列表
     */
    @BindView(R.id.spnFixedFlowDataDot)
    Spinner mSpnFixedFlowDataDot;

    private FixedFlowDataAdapter mAdapter;
    private GetDatSchemeFixedListJson mFixedListJson;

    private List<GetSchemeFixedListLogJson.ListBean> mAdapterData = new ArrayList<>();
    private int pageIndex = 1;//页数
    private int pageSize = 15;//每页的数量
    private int pageSizeNum = 0;//数据的总数
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
    List<GetDatSchemeFixedListJson.ListBean> mFixedList = new ArrayList<>();
    /**
     * 监测点下拉列表里显示的数据
     */
    List<GetDatSchemeAreaListJson.ListBean.NewMonitorBean> mNewMonitorList = new ArrayList<>();
    private CommonAdapter<GetDatSchemeFixedListJson.ListBean> mSpnAdapterFixed;
    private String mSchemeID = "";//方案id

    @Override
    protected FixedFlowDataPresenter loadPresenter() {
        return new FixedFlowDataPresenter();
    }

    public static FixedFlowDataFragment newInstance(GetDatSchemeFixedListJson fixedListJson, String schemeId) {
        FixedFlowDataFragment fixedFlowDataFragment = new FixedFlowDataFragment();
        if (fixedListJson == null || fixedListJson.getList() == null || fixedListJson.getList().isEmpty()) {
            fixedFlowDataFragment.setDataEmpty();
        } else {
            fixedFlowDataFragment.mFixedListJson = fixedListJson;
            fixedFlowDataFragment.mSchemeID = schemeId;
        }
        return fixedFlowDataFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_fixed_flow_data;
    }

    @Override
    public void initView() {
        if (mIsDataEmpty)
            return;
        mAdapter = new FixedFlowDataAdapter(R.layout.item_fixed_flow_fata, mAdapterData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        //加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            if (pageIndex > (pageSizeNum + pageSize - 1) / pageSize) {
                showToast("已经是最后一页了");
                mAdapter.loadMoreEnd();
            } else {
                getFixedFlowData();
            }
        }, mRecyclerView);
        initSpnFixedFlow(mFixedListJson.getList());
    }

    /**
     * 初始化定点的Spinner,并添加默认"全部"选项
     *
     * @param list 数据
     */
    private void initSpnFixedFlow(List<GetDatSchemeFixedListJson.ListBean> list) {
        mSpnAdapterFixed = new CommonAdapter<GetDatSchemeFixedListJson.ListBean>(getActivity(), R.layout.item_spinner, mFixedList) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, GetDatSchemeFixedListJson.ListBean item, int position) {
                helper.setText(R.id.tvItemSpinner, item.getFixedName());
            }
        };

        mSpnFixedFlowDataDot.setAdapter(mSpnAdapterFixed);
        mFixedList.clear();
        mSpnAdapterFixed.clear();
        GetDatSchemeFixedListJson.ListBean defaultArea = new GetDatSchemeFixedListJson.ListBean();
        defaultArea.setFixedID("");
        defaultArea.setFixedName("全部");
        mFixedList.add(defaultArea);
        if (list != null && list.size() != 0)
            mFixedList.addAll(list);
        mSpnAdapterFixed.addAll(mFixedList);
        mSpnAdapterFixed.notifyDataSetChanged();
        mSpnFixedFlowDataDot.setSelection(0);
    }

    @Override
    protected void lazyLoad() {
        if (mIsFragmentVisible && mIsInitView && mIsFirstLazyLoad) {
            mIsFirstLazyLoad = false;
            if (mIsDataEmpty) {
                showToast(R.string.view_empty);
            } else {
                getFixedFlowData();
            }
        }
    }

    /**
     * 获取定点流水数据
     */
    private void getFixedFlowData() {
        String fixedID = mFixedList.get(mSpnFixedFlowDataDot.getSelectedItemPosition()).getFixedID() + "";
        mPresenter.getSchemeFixedListLog(mSchemeID, fixedID, mSelectTimeStart, mSelectTimeEnd, "", pageIndex, pageSize, UserInfoPref.getUserId());
    }

    @Override
    public void onGetSchemeFixedListLogSuccess(GetSchemeFixedListLogJson jsonData) {
        pageSizeNum = jsonData.getTotalCount();
        mAdapter.addData(jsonData.getList());
        mAdapter.setTotalCount(jsonData.getTotalCount());
        if (mAdapter != null && mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void onGetSchemeFixedListLogFail(String msg) {
        showToast(msg);
        if (mAdapter != null && mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @OnClick({R.id.btFixedFlowDataStart, R.id.btFixedFlowDataEnd, R.id.btFixedFlowDataSearch})
    void OnClick(View view) {
        if (mIsDataEmpty) {
            showToast(R.string.view_empty);
            return;
        }
        switch (view.getId()) {
            case R.id.btFixedFlowDataStart:
                TimePickerUtils.showPickerView(getActivity(), "开始时间", mTvFixedFlowDataStart, mSelectTimeStart, "1234-10-11", mTvFixedFlowDataEnd.getText().toString());
                break;
            case R.id.btFixedFlowDataEnd:
                TimePickerUtils.showPickerView(getActivity(), "结束时间", mTvFixedFlowDataEnd, mSelectTimeEnd, mTvFixedFlowDataStart.getText().toString(), "");
                break;
            case R.id.btFixedFlowDataSearch://搜索
                //初始化数据
                pageIndex = 1;
                pageSizeNum = 0;
                mSelectTimeStart = mTvFixedFlowDataStart.getText().toString();
                mSelectTimeEnd = mTvFixedFlowDataEnd.getText().toString();
                mAdapterData.clear();
                mAdapter.setTotalCount(0);
                mAdapter.setNewData(null);//重新开启下拉加载更多
                getFixedFlowData();
                break;
        }
    }
}