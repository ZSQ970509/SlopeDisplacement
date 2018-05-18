package com.example.administrator.slopedisplacement.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.adapter.GetSchemeAlarmListAdapter;
import com.example.administrator.slopedisplacement.adapter.PanoramaAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpActivity;
import com.example.administrator.slopedisplacement.bean.IVMS_8700_Bean;
import com.example.administrator.slopedisplacement.bean.PanoramaImgBean;
import com.example.administrator.slopedisplacement.bean.SchemeAlarmListBean;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeAlarmJson;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeMonitorLogJson;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.PlanLayoutOfPanoramaContact;
import com.example.administrator.slopedisplacement.mvp.presenter.PlanLayoutOfPanoramaPresenter;
import com.example.administrator.slopedisplacement.utils.JumpToUtils;
import com.example.administrator.slopedisplacement.utils.MusicUtils;
import com.example.administrator.slopedisplacement.utils.PointImgHelper;
import com.example.administrator.slopedisplacement.widget.CustomLoadMoreView;
import com.example.administrator.slopedisplacement.widget.pointImg.LineBean;
import com.example.administrator.slopedisplacement.widget.pointImg.PointBean;
import com.example.administrator.slopedisplacement.widget.pointImg.PointDataBean;
import com.example.administrator.slopedisplacement.widget.pointImg.PointFrameLayout;
import com.example.administrator.slopedisplacement.widget.popupwindow.BindViewHelper;
import com.example.administrator.slopedisplacement.widget.popupwindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlanLayoutOfPanoramaActivity extends BaseMvpActivity<PlanLayoutOfPanoramaPresenter> implements PlanLayoutOfPanoramaContact.View {
    @BindView(R.id.ivPlanLayoutOfPanormamBack)
    ImageView ivPlanLayoutOfPanormamBack;
    @BindView(R.id.btnAlarmInformation)
    TextView btnAlarmInformation;
    @BindView(R.id.pflPoint)
    PointFrameLayout mPointFrameLayout;

    private int mPageIndexImg = 0;//当前的页数(全景图列表)    ps：页数从0开始
    private int mPageSizeImg = 10;//每页的数量(全景图列表)
    private int mPageCountImg = 0;//数据的总页数(全景图列表)
    private int mPageIndexAlarm = 1;//当前的页数(告警列表)   ps：页数1开始
    private int mPageSizeAlarm = 10;//每页的数量(告警列表)
    private int mPageSizeNumAlarm = 0;//数据的总数(告警列表)

    ArrayList<PanoramaImgBean.ListBean> dataList = new ArrayList<PanoramaImgBean.ListBean>();
    ArrayList<SchemeAlarmListBean.ListBean> schemeAlarmList = new ArrayList<SchemeAlarmListBean.ListBean>();
    CommonPopupWindow mPopWindow;

    GetSchemeAlarmListAdapter getSchemeAlarmListAdapter;
    PanoramaAdapter panoramaAdapter;
    int SchemeAlarmListPosition;
    IVMS_8700_Bean ivms_8700_bean;
    String mSchemeID;
    private boolean isFromPush = false;//是否从推送跳转过来的
    private GetDatSchemeAreaListJson mArealListJson;//区域列表数据
    private GetDatSchemeFixedListJson mFixedListJson;//定点列表数据
    /**
     * 区域数据是否准备完成
     */
    private boolean mIsPrepareAreaData = false;
    /**
     * 定点数据是否准备完成
     */
    private boolean mIsPrepareFixedData = false;
    /**
     * 背景是否准备完成
     */
    private boolean mIsPrepareImg = false;
    private String mCamId;

    @Override
    protected PlanLayoutOfPanoramaPresenter loadPresenter() {
        return new PlanLayoutOfPanoramaPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plan_layout_of_panorama;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ivms_8700_bean = (IVMS_8700_Bean) getIntent().getSerializableExtra(JumpToUtils.KEY_IVMS_8700_BEAN);
        Log.e("ivms_8700_bean", ivms_8700_bean.getCamFlowState());
        mSchemeID = getIntent().getStringExtra(JumpToUtils.KEY_SCHEMEID);
        mCamId = getIntent().getStringExtra(JumpToUtils.KEY_CAMID);
        isFromPush = getIntent().getBooleanExtra(JumpToUtils.KEY_FROM_PUSH, false);
        mIsPrepareAreaData = false;
        mIsPrepareFixedData = false;
        mIsPrepareImg = false;
        mPresenter.getPanoramaImg(mCamId, mPageIndexImg + "", "10", UserInfoPref.getUserId());
        mPresenter.getDatSchemeAreaList(mSchemeID + "", UserInfoPref.getUserId());
        mPresenter.getDatSchemeFixedList(mSchemeID + "", UserInfoPref.getUserId());
    }

    /**
     * 显示告警列表
     */
    private void showPopupWindow() {
        mPageIndexAlarm = 1;//重置
        mPopWindow = new CommonPopupWindow.Builder(this) {
            @Override
            public void popBindView(BindViewHelper popupWindowBindView) {
                schemeAlarmList.clear();
                mPresenter.getSchemeAlarmList(mSchemeID + "", "", "", "", mPageIndexAlarm + "", "10", UserInfoPref.getUserId() + "");
                LinearLayout pop_LinearLayout = (LinearLayout) popupWindowBindView.getView(R.id.pop_LinearLayout);
                RecyclerView rvAlarmInformation = (RecyclerView) popupWindowBindView.getView(R.id.rvAlarmInformation);
                rvAlarmInformation.setLayoutManager(new LinearLayoutManager(PlanLayoutOfPanoramaActivity.this));
                for (SchemeAlarmListBean.ListBean list : schemeAlarmList) {
                    //3表示不显示   所以这边做移除
                    if (list.getStates() == 3) {
                        schemeAlarmList.remove(list);
                    }
                }
                getSchemeAlarmListAdapter = new GetSchemeAlarmListAdapter(R.layout.item_scheme_alarm_list, schemeAlarmList);
                rvAlarmInformation.setAdapter(getSchemeAlarmListAdapter);
                getSchemeAlarmListAdapter.setLoadMoreView(new CustomLoadMoreView());

                getSchemeAlarmListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        rvAlarmInformation.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mPageIndexAlarm * mPageSizeAlarm > mPageSizeNumAlarm) {
                                    showToast("已经是最后一页了");
                                    getSchemeAlarmListAdapter.loadMoreEnd();
                                } else {
                                    mPageIndexAlarm++;
                                    mPresenter.getSchemeAlarmList(mSchemeID + "", "", "", "", mPageIndexAlarm + "", "10", UserInfoPref.getUserId() + "");
                                }
                            }
                        }, 1000);
                    }
                }, rvAlarmInformation);
                getSchemeAlarmListAdapter.disableLoadMoreIfNotFullPage();
                getSchemeAlarmListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        SchemeAlarmListBean.ListBean schemeAlarmListBean = schemeAlarmList.get(position);

                        switch (view.getId()) {
                            case R.id.tvSchemeAlaramListMove1:
                                //MonitorID1 Aha1 Ava1 Shift1 Path1a nextPath1a oldPath1a
                                JumpToUtils.toAlarmChartViewActivity(getActivity(), schemeAlarmListBean.getMonitorID1(),
                                        schemeAlarmListBean.getAha1(), schemeAlarmListBean.getAva1(), schemeAlarmListBean.getShift1(),
                                        schemeAlarmListBean.getPath1a() + "", schemeAlarmListBean.getNextPath1a() + "", schemeAlarmListBean.getOldPath1a());
                                break;
                            case R.id.tvSchemeAlaramListMove2:
                                //MonitorID2 Aha2 Ava2 Shift2 Path2a nextPath2a oldPath2a
                                JumpToUtils.toAlarmChartViewActivity(getActivity(), schemeAlarmListBean.getMonitorID1(),
                                        schemeAlarmListBean.getAha1(), schemeAlarmListBean.getAva1(), schemeAlarmListBean.getShift1(),
                                        schemeAlarmListBean.getPath1a() + "", schemeAlarmListBean.getNextPath1a() + "", schemeAlarmListBean.getOldPath1a());
                                break;
                            case R.id.tvSchemeAlaramListMove3:
                                //MonitorID3 Aha3 Ava3 Shift3 Path3a nextPath3a oldPath3a
                                JumpToUtils.toAlarmChartViewActivity(getActivity(), schemeAlarmListBean.getMonitorID1(),
                                        schemeAlarmListBean.getAha1(), schemeAlarmListBean.getAva1(), schemeAlarmListBean.getShift1(),
                                        schemeAlarmListBean.getPath1a() + "", schemeAlarmListBean.getNextPath1a() + "", schemeAlarmListBean.getOldPath1a());
                                break;
                            case R.id.tvSchemeAlaramListMove4:
                                //MonitorID4 Aha4 Ava4 Shift4 Path4a nextPath4a oldPath4a
                                JumpToUtils.toAlarmChartViewActivity(getActivity(), schemeAlarmListBean.getMonitorID1(),
                                        schemeAlarmListBean.getAha1(), schemeAlarmListBean.getAva1(), schemeAlarmListBean.getShift1(),
                                        schemeAlarmListBean.getPath1a() + "", schemeAlarmListBean.getNextPath1a() + "", schemeAlarmListBean.getOldPath1a());
                                break;
                            case R.id.tvSchemeAlaramListMove5:
                                if (schemeAlarmList.get(position).getTypes() == 1) {
                                    showToast("此点为定点");
                                } else {
                                    JumpToUtils.toAlarmChartViewActivity(getActivity(), schemeAlarmListBean.getMonitorID1(),
                                            schemeAlarmListBean.getAha1(), schemeAlarmListBean.getAva1(), schemeAlarmListBean.getShift1(),
                                            schemeAlarmListBean.getPath1a() + "", schemeAlarmListBean.getNextPath1a() + "", schemeAlarmListBean.getOldPath1a());
                                    //MonitorID1 Aha1 Ava1 Shift1 Path1a nextPath1a oldPath1a
                                }
                                break;
                            case R.id.ivSchemeAlaramListNumConfirm:
                                //记录点击的position，
                                SchemeAlarmListPosition = position;
                                mPresenter.updateSchemeAlarmByAlarmID(schemeAlarmList.get(position).getAlarmID() + "", "2", UserInfoPref.getUserId() + "");
                                break;
                            default:
                                break;
                        }
                    }
                });
                pop_LinearLayout.setOnClickListener(v -> mPopWindow.dismiss());
            }
        }.setContentView(R.layout.view_popupwindow_listview)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(ViewGroup.LayoutParams.FILL_PARENT)
                .setOutsideTouchable(true)
                .setFocusable(true)
                .builder()
                .showAtLocation(R.layout.activity_plan_layout_of_panorama, Gravity.BOTTOM, 0, 0);
    }

    private void showPanoramaPopupWindow() {
        mPopWindow = new CommonPopupWindow.Builder(this) {
            @Override
            public void popBindView(BindViewHelper popupWindowBindView) {
                LinearLayout LLPopupPanorama = (LinearLayout) popupWindowBindView.getView(R.id.LLPopupPanorama);
                RecyclerView rvPanorama = (RecyclerView) popupWindowBindView.getView(R.id.rvPanorama);
                rvPanorama.setLayoutManager(new LinearLayoutManager(PlanLayoutOfPanoramaActivity.this));
                panoramaAdapter = new PanoramaAdapter(R.layout.item_panorama, dataList);
                rvPanorama.setAdapter(panoramaAdapter);
                panoramaAdapter.setLoadMoreView(new CustomLoadMoreView());
                panoramaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mPointFrameLayout.changeBg(dataList.get(position).getPuzzleImg());
                        mPopWindow.dismiss();
                    }
                });

                panoramaAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        rvPanorama.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPageIndexImg++;
                                if (mPageIndexImg >= mPageCountImg) {//页数从0开始
                                    showToast("已经是最后一页了");
                                    panoramaAdapter.loadMoreEnd();
                                } else {
                                    mPresenter.getPanoramaImg(mCamId, mPageIndexImg + "", "10", UserInfoPref.getUserId());
                                }
                            }
                        }, 1000);
                    }
                }, rvPanorama);
                LLPopupPanorama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopWindow.dismiss();
                    }
                });
                panoramaAdapter.disableLoadMoreIfNotFullPage();
            }
        }.setContentView(R.layout.view_popupwindow_panorama)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(ViewGroup.LayoutParams.FILL_PARENT)
                .setOutsideTouchable(true)
                .setFocusable(true)
                .builder()
                .showAtLocation(R.layout.activity_plan_layout_of_panorama, Gravity.RIGHT, 0, 0);
        mPageIndexImg = 0;
        dataList.clear();
        mPresenter.getPanoramaImg(mCamId, mPageIndexImg + "", "10", UserInfoPref.getUserId());
    }

    @Override
    public void onGetPanoramaImgSuccess(PanoramaImgBean panoramaImgBean) {
        if (panoramaImgBean.getList().size() == 0) {
            showToast("暂无数据！");
        } else {
            showToast("数据加载成功！");
        }
        dataList.addAll(panoramaImgBean.getList());
        mPageCountImg = (panoramaImgBean.getTotalCount() + mPageSizeImg - 1) / mPageSizeImg;
        if (panoramaAdapter != null) {
            panoramaAdapter.notifyDataSetChanged();
            if (panoramaAdapter.isLoading()) {
                panoramaAdapter.loadMoreComplete();
            }
        }
        if (!mIsPrepareImg) {//第一加载时，更新点、线、面数据
            mIsPrepareImg = true;
            loadPointImg();
        } else {//
            hideLoading();
        }

    }

    @Override
    public void onGetPanoramaImgFail(String msg) {
        showToast(msg);
        if (panoramaAdapter != null) {
            panoramaAdapter.notifyDataSetChanged();
            if (panoramaAdapter.isLoading()) {
                panoramaAdapter.loadMoreComplete();
            }
        }
        if (!mIsPrepareImg) {//第一加载时，更新点、线、面数据
            mIsPrepareImg = true;
            loadPointImg();
        } else {//
            hideLoading();
        }
    }

    @Override
    public void onGetSchemeAlarmListSuccess(SchemeAlarmListBean schemeAlarmListBean) {
        if (schemeAlarmListBean.getList().size() == 0) {
            showToast("暂无数据！");
        } else {
            showToast("数据加载成功！");
        }
        mPageSizeNumAlarm = schemeAlarmListBean.getTotalCount();
        schemeAlarmList.addAll(schemeAlarmListBean.getList());
        getSchemeAlarmListAdapter.notifyDataSetChanged();
        getSchemeAlarmListAdapter.loadMoreComplete();

    }

    @Override
    public void onGetSchemeAlarmListFail(String msg) {
        showToast(msg);
    }


    @Override
    public void onUpdateSchemeAlarmByAlarmIDSuccess(String msg) {
        //2代表已确认
        schemeAlarmList.get(SchemeAlarmListPosition).setStates(2);
        getSchemeAlarmListAdapter.notifyDataSetChanged();
        //刷新告警点
        mPresenter.getSchemeAlarm(mSchemeID + "", "2", UserInfoPref.getUserId());
    }

    @Override
    public void onUpdateSchemeAlarmByAlarmIDFail(String msg) {

    }


    @Override
    public void onGetDatSchemeAreaListSuccess(GetDatSchemeAreaListJson arealListJson) {
        mArealListJson = arealListJson;
        mIsPrepareAreaData = true;
        loadPointImg();
    }

    @Override
    public void onGetDatSchemeAreaListFail(String msg) {
        mArealListJson = null;
        mIsPrepareAreaData = true;
        loadPointImg();
    }

    @Override
    public void onGetDatSchemeFixedListSuccess(GetDatSchemeFixedListJson fixedListJson) {
        mFixedListJson = fixedListJson;
        mIsPrepareFixedData = true;
        loadPointImg();
    }

    @Override
    public void onGetDatSchemeFixedListFail(String msg) {
        mFixedListJson = null;
        mIsPrepareFixedData = true;
        loadPointImg();
    }

    private Disposable mDisposableMonitor;//轮循
    private Disposable mDisposableLoadPointMonitor;//轮循

    @Override
    public void getSchemeMonitorLogSuccess(GetSchemeMonitorLogJson getSchemeMonitorLogJson) {
        if (mDisposableLoadPointMonitor != null) {
            mDisposableLoadPointMonitor.dispose();
        }
        mDisposableLoadPointMonitor = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mPointFrameLayout != null && mPointFrameLayout.isPreparePoint()) {
                        mPointFrameLayout.stopAllPointAnimationMonitor();//停止所有巡航点动画
                        for (GetSchemeMonitorLogJson.ListBean json : getSchemeMonitorLogJson.getList()) {
                            mPointFrameLayout.startPointAnimation(json.getMonitorID(), PointFrameLayout.AnimationType.MONITOR);
                        }
                        mDisposableLoadPointMonitor.dispose();
                    }
                });
        startLoopMonitor(getSchemeMonitorLogJson.getSchemeMonitorAndAlarm(), getSchemeMonitorLogJson.getSchemeMonitorAndAlarm());
    }

    @Override
    public void getSchemeMonitorLogFail(String msg) {
        showToast(msg);
    }

    /**
     * 循环调用监测点接口
     *
     * @param initialDelay
     * @param period
     */
    private void startLoopMonitor(long initialDelay, long period) {
        if (mDisposableMonitor != null)
            mDisposableMonitor.dispose();
        mDisposableMonitor = Observable.interval(initialDelay, period, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mPresenter.getSchemeMonitorLog(mSchemeID + "", UserInfoPref.getUserId()));
    }

    Disposable mDisposableAlarm;
    Disposable mDisposableLoadPointAlarm;

    @Override
    public void onGetSchemeAlarmSuccess(GetSchemeAlarmJson getSchemeAlarmJsons) {
        if (mDisposableLoadPointAlarm != null) {
            mDisposableLoadPointAlarm.dispose();
        }
        if (getSchemeAlarmJsons.getList() != null && getSchemeAlarmJsons.getList().size() > 0) {
            MusicUtils.instance.playAlarm(getActivity());
        } else {
            MusicUtils.instance.destroy();
        }
        //加载告警点动画
        mDisposableLoadPointAlarm = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mPointFrameLayout != null && mPointFrameLayout.isPreparePoint()) {
                        //停止所有告警点动画
                        mPointFrameLayout.stopAllPointAnimationAlarm();
                        for (GetSchemeAlarmJson.ListBean json : getSchemeAlarmJsons.getList()) {
                            mPointFrameLayout.startPointAnimation(json.getMonitorID1(), PointFrameLayout.AnimationType.ALARM);
                            mPointFrameLayout.startPointAnimation(json.getMonitorID2(), PointFrameLayout.AnimationType.ALARM);
                            mPointFrameLayout.startPointAnimation(json.getMonitorID3(), PointFrameLayout.AnimationType.ALARM);
                            mPointFrameLayout.startPointAnimation(json.getMonitorID4(), PointFrameLayout.AnimationType.ALARM);
                        }
                        mDisposableLoadPointAlarm.dispose();
                    }
                });
        //循环刷新告警点数据
        startLoopAlarm(getSchemeAlarmJsons.getSchemeMonitorAndAlarm(), getSchemeAlarmJsons.getSchemeMonitorAndAlarm());
    }

    /**
     * 循环调用告警点接口
     *
     * @param initialDelay
     * @param period
     */
    private void startLoopAlarm(long initialDelay, long period) {
        if (mDisposableAlarm != null)
            mDisposableAlarm.dispose();
        mDisposableAlarm = Observable.interval(initialDelay, period, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mPresenter.getSchemeAlarm(mSchemeID + "", "2", UserInfoPref.getUserId()));
    }

    @Override
    public void onGetSchemeAlarmFail(String msg) {
        showToast(msg);
    }

    /**
     * 加载全景图和图上的点、线、面、文字
     */
    private synchronized void loadPointImg() {
        //区域列表、定点列表、获取全景图接口都调用完成后才执行加载点相关信息
        if (!mIsPrepareFixedData || !mIsPrepareAreaData || !mIsPrepareImg) {
            showLoading("加载数据中...");
            return;
        }
        ArrayList<PointBean> pointBeanList = new ArrayList<>();//点
        List<LineBean> lineBeanList = new ArrayList<>();//线
        List<LineBean> dottedLineList = new ArrayList<>();//虚线
        if (mArealListJson != null)
            PointImgHelper.areaPoint(mArealListJson.getList(), pointBeanList, lineBeanList, dottedLineList);
        if (mFixedListJson != null)
            PointImgHelper.fixedPoint(mFixedListJson.getList(), pointBeanList);

        PointDataBean imgPointBean = new PointDataBean();
        imgPointBean.setPointBeanList(pointBeanList);
        imgPointBean.setLineList(lineBeanList);
        imgPointBean.setDottedLineList(dottedLineList);
        mPointFrameLayout.setPointsInfo(imgPointBean);
        if (dataList != null && dataList.size() != 0)
            mPointFrameLayout.setBgImgUrl(dataList.get(0).getPuzzleImg());
        //全景图的加载完成后调用接口显示告警点和监测点
        mPresenter.getSchemeAlarm(mSchemeID + "", "2", UserInfoPref.getUserId());
        mPresenter.getSchemeMonitorLog(mSchemeID + "", UserInfoPref.getUserId());
        hideLoading();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }
        if (mDisposableMonitor != null) {
            mDisposableMonitor.dispose();
        }
        if (mDisposableLoadPointMonitor != null) {
            mDisposableLoadPointMonitor.dispose();
        }
        if (mDisposableAlarm != null) {
            mDisposableAlarm.dispose();
        }
        if (mDisposableLoadPointAlarm != null) {
            mDisposableLoadPointAlarm.dispose();
        }
        MusicUtils.instance.destroy();
    }

    @OnClick({R.id.btnAlarmInformation, R.id.btnPanorama, R.id.btnPanoramaDataReport, R.id.btnVideo, R.id.ivPlanLayoutOfPanormamBack})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlarmInformation://告警列表
                showPopupWindow();
                break;
            case R.id.btnPanorama://全景图列表
                showPanoramaPopupWindow();
                break;
            case R.id.btnVideo:
                if (ivms_8700_bean.getCamFlowState().equals("15")) {
                    String type = ivms_8700_bean.getmType();
                    //2,5,8为互信、3中星微2.1、7中星微3.3、15海康8700
                    if (type.equals("2") || type.equals("5") || type.equals("8") ) {
                        JumpToUtils.toHuXinVideoActivity(getActivity(), ivms_8700_bean);
                    } else if (type.equals("15")) {//海康8700
                        JumpToUtils.toHKVideoActivity(getActivity(), ivms_8700_bean);
                    } else {
                        JumpToUtils.toRtspVideoAc(getActivity(), ivms_8700_bean.getmRtsp());

                    }
                } else {
                    showToast("此视频维护或不在线");
                }
                break;
            case R.id.btnPanoramaDataReport://跳转到查看数据页面
                JumpToUtils.toDataReportActivity(getActivity(), mArealListJson, mFixedListJson, mSchemeID + "");
                break;
            case R.id.ivPlanLayoutOfPanormamBack:
                finish();
                break;
        }
    }
}
