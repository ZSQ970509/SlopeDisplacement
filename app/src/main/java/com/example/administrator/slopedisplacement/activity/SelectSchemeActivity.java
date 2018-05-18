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
import com.example.administrator.slopedisplacement.adapter.SelectSchemeAdapter;
import com.example.administrator.slopedisplacement.base.BaseMvpActivity;
import com.example.administrator.slopedisplacement.bean.DriverBean;
import com.example.administrator.slopedisplacement.bean.IVMS_8700_Bean;
import com.example.administrator.slopedisplacement.bean.SchemeBean;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.mvp.contact.SelectDriverContact;
import com.example.administrator.slopedisplacement.mvp.contact.SelectSchemeContact;
import com.example.administrator.slopedisplacement.mvp.presenter.SelectDriverPresenter;
import com.example.administrator.slopedisplacement.mvp.presenter.SelectSchemePresenter;
import com.example.administrator.slopedisplacement.utils.JumpToUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

import static com.example.administrator.slopedisplacement.utils.JumpToUtils.KEY_CAMID;

public class SelectSchemeActivity extends BaseMvpActivity<SelectSchemePresenter> implements SelectSchemeContact.View {
    Intent intent;
    SchemeBean schemeBean;
    SelectSchemeAdapter selectSchemeAdapter;
    @BindView(R.id.rvScheme)
    RecyclerView rvScheme;

    @Override
    protected SelectSchemePresenter loadPresenter() {
        return new SelectSchemePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_scheme;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setToolBar("方案选择");
        intent = getIntent();
        IVMS_8700_Bean ivms_8700_bean = (IVMS_8700_Bean) getIntent().getSerializableExtra(JumpToUtils.KEY_IVMS_8700_BEAN);
        Log.e("ivms_8700_bean", ivms_8700_bean.getCamFlowState());
        schemeBean = (SchemeBean) intent.getSerializableExtra(JumpToUtils.KEY_SCHEMEBEAN);
        String camId = intent.getStringExtra(JumpToUtils.KEY_CAMID);
        rvScheme.setLayoutManager(new LinearLayoutManager(this));
        selectSchemeAdapter = new SelectSchemeAdapter(R.layout.item_select_scheme, schemeBean.getList());
        Log.e("schemeBean.getList()", schemeBean.getList().size() + "");
        rvScheme.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvScheme.setAdapter(selectSchemeAdapter);
        selectSchemeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(MainActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
//                intent.setClass(SelectSchemeActivity.this,PlanLayoutOfPanoramaActivity.class);
//                intent.putExtra("SchemeList",schemeBean.getList().get(position));
//                startActivity(intent);
                JumpToUtils.toPlanLayoutOfPanoramaActivity(getActivity(), camId, schemeBean.getList().get(position).getSchemeID() + "", ivms_8700_bean);
                //JumpToUtils.toPlanLayoutOfPanoramaActivityFromPush(getActivity(),camId,schemeBean.getList().get(position).getSchemeID()+"");
            }
        });

    }

    @Override
    protected void initView() {

    }

}
