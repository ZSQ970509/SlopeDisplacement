package com.example.administrator.slopedisplacement.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.adapter.SelectProjectAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpActivity;
import com.example.administrator.slopedisplacement.bean.ProjectBean;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.SelectProjectContact;
import com.example.administrator.slopedisplacement.mvp.presenter.SelectProjectPresenter;
import com.example.administrator.slopedisplacement.widget.CustomLoadMoreView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectProjectActivity extends BaseMvpActivity<SelectProjectPresenter> implements SelectProjectContact.View {
    @BindView(R.id.etProjectName)
    EditText etProjectName;
    @BindView(R.id.ivProjectSearch)
    ImageView ivProjectSearch;
    @BindView(R.id.rvProject)
    RecyclerView rvProject;
    int pageIndex = 0;
    int sumPage;
    SelectProjectAdapter selectProjectAdapter;
    ArrayList<ProjectBean.ListBean> dataList = new ArrayList<ProjectBean.ListBean>();
    Intent intent;

    @Override
    protected SelectProjectPresenter loadPresenter() {
        return new SelectProjectPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selec_project;
    }

    @Override
    protected void initView() {
        setToolBar("项目选择");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProject.setLayoutManager(linearLayoutManager);
        selectProjectAdapter = new SelectProjectAdapter(R.layout.item_select_project, dataList);
        rvProject.setAdapter(selectProjectAdapter);
        rvProject.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        selectProjectAdapter.setLoadMoreView(new CustomLoadMoreView());
        selectProjectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvProject.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pageIndex > sumPage) {
                            showToast("已经是最后一页了");
                            selectProjectAdapter.loadMoreEnd();
                        } else {
                            mPresenter.getVideoMonitorList(etProjectName.getText().toString(), pageIndex + "", "10", UserInfoPref.getUserId());
                        }

                    }

                }, 1000);
            }
        }, rvProject);
        selectProjectAdapter.disableLoadMoreIfNotFullPage();
        selectProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(MainActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                try {
                    intent = getIntent();
                    intent.setClass(SelectProjectActivity.this, SelectDriverActivity.class);
                    intent.putExtra("Project", dataList.get(position));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @OnClick({R.id.ivProjectSearch})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivProjectSearch:
                dataList.clear();
                selectProjectAdapter.notifyDataSetChanged();
                mPresenter.getVideoMonitorList(etProjectName.getText().toString(), pageIndex + "", "10", UserInfoPref.getUserId());
                break;
        }
    }

    @Override
    public void onGetVideoMonitorListSuccess(ProjectBean projectBean) {
        if (projectBean.getList().size() == 0) {
            showToast("暂无数据！");
        } else {
            showToast("数据加载成功！");
        }
        dataList.addAll(projectBean.getList());
        selectProjectAdapter.notifyDataSetChanged();
        sumPage = (projectBean.getTotalCount() + 10 - 1) / 10;
        if (pageIndex <= sumPage) {
            pageIndex++;
        }
        selectProjectAdapter.loadMoreComplete();

    }

    @Override
    public void onGetVideoMonitorListFail(String msg) {
        showToast(msg);
    }
}
