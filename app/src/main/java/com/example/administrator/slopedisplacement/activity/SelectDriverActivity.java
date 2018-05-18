package com.example.administrator.slopedisplacement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.adapter.SelectDriverAdapter;
import com.example.administrator.slopedisplacement.adapter.SelectProjectAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpActivity;
import com.example.administrator.slopedisplacement.bean.DriverBean;
import com.example.administrator.slopedisplacement.bean.IVMS_8700_Bean;
import com.example.administrator.slopedisplacement.bean.LoginBean;
import com.example.administrator.slopedisplacement.bean.ProjectBean;
import com.example.administrator.slopedisplacement.bean.SchemeBean;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.SelectDriverContact;
import com.example.administrator.slopedisplacement.mvp.presenter.SelectDriverPresenter;
import com.example.administrator.slopedisplacement.utils.JumpToUtils;
import com.example.administrator.slopedisplacement.utils.XmlUtils;
import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import butterknife.BindView;

public class SelectDriverActivity extends BaseMvpActivity<SelectDriverPresenter> implements SelectDriverContact.View {
    Intent intent;
    SelectDriverAdapter selectDriverAdapter;
    ArrayList<DriverBean.ListBean> dataList = new ArrayList<DriverBean.ListBean>();
    @BindView(R.id.rvDriver)
    RecyclerView rvDriver;
    DriverBean.ListBean driverBean;

    @Override
    protected SelectDriverPresenter loadPresenter() {
        return new SelectDriverPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_driver;
    }

    @Override
    protected void initView() {
        setToolBar("设备列表");
        rvDriver.setLayoutManager(new LinearLayoutManager(this));
        selectDriverAdapter = new SelectDriverAdapter(R.layout.item_select_drivier, dataList);
        rvDriver.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvDriver.setAdapter(selectDriverAdapter);
        selectDriverAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(MainActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                driverBean = dataList.get(position);
                mPresenter.getSchemeList(dataList.get(position).getCamId() + "", UserInfoPref.getUserId());
                //TODO 测试数据暂时写死
//                mPresenter.getSchemeList("1014462",UserInfoPref.getUserId());
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        intent = getIntent();
        mPresenter.getVideoCamList(((ProjectBean.ListBean) intent.getSerializableExtra("Project")).getProjID() + "", UserInfoPref.getUserId());
    }

    @Override
    public void onGetVideoCamListSuccess(DriverBean driverBean) {
        if (driverBean.getList().size() == 0) {
            showToast("暂无数据！");
        } else {
            showToast("数据加载成功！");
        }
        dataList.addAll(driverBean.getList());
        selectDriverAdapter.notifyDataSetChanged();
    }


    @Override
    public void onGetVideoCamListFail(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetSchemeListSuccess(SchemeBean schemeBean) {
        try {
            SchemeBean.ListBean listBean = null;
            IVMS_8700_Bean ivms_8700_bean = XmlUtils.parseXMLWithPull(driverBean.getCam_Config());
            ivms_8700_bean.setCam_Dx_Puid(driverBean.getCam_DX_PUID());
            ivms_8700_bean.setCamFlowState(driverBean.getCamFlowState() + "");

            for (SchemeBean.ListBean list : schemeBean.getList()) {
                if (list.getStates() == 1) {
                    listBean = list;
                }
            }

            if (listBean != null) {
                //全景图页面
//            intent.setClass(SelectDriverActivity.this,PlanLayoutOfPanoramaActivity.class);
//            intent.putExtra("SchemeBean",listBean);
//            intent.putExtra("DriverBean",driverBean);
//            startActivity(intent);
                Log.e("ivms_8700_bean", ivms_8700_bean.getCamFlowState());
                JumpToUtils.toPlanLayoutOfPanoramaActivity(getActivity(), driverBean.getCamId() + "", listBean.getSchemeID() + "", ivms_8700_bean);
                //TODO 测试数据暂时写死
//            JumpToUtils.toPlanLayoutOfPanoramaActivity(getActivity(),"1014462",listBean.getSchemeID()+"",ivms_8700_bean);
            } else {
//            intent.setClass(SelectDriverActivity.this,SelectSchemeActivity.class);
//            intent.putExtra("DriverBean",driverBean);
//            intent.putExtra("SchemeList",schemeBean);
//            startActivity(intent);
                JumpToUtils.toSelectSchemeActivity(getActivity(), driverBean.getCamId() + "", schemeBean, ivms_8700_bean);
                //TODO 测试数据暂时写死
//            JumpToUtils.toSelectSchemeActivity(getActivity(),"1014462",schemeBean,ivms_8700_bean);
            }
           /* if(ivms_8700_bean.getCamFlowState().equals("15")) {
                //2,5,8为互信、3中星微2.1、7中星微3.3、15海康8700
                if (ivms_8700_bean.getmType().equals("2") || ivms_8700_bean.getmType().equals("5") || ivms_8700_bean.getmType().equals("8") || ivms_8700_bean.getmType().equals("3") || ivms_8700_bean.getmType().equals("7")) {
                    JumpToUtils.toHuXinVideoActivity(getActivity(),ivms_8700_bean);
                } else {
                    showToast("此为海康平台：");
                }
            }else{
                showToast("此视频维护或不在线");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetSchemeListFail(String msg) {
        showToast(msg);
    }
}
