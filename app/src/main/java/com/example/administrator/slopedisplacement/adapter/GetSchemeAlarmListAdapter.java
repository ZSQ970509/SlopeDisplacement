package com.example.administrator.slopedisplacement.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.bean.DriverBean;
import com.example.administrator.slopedisplacement.bean.SchemeAlarmListBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/18.
 */

public class GetSchemeAlarmListAdapter extends BaseItemDraggableAdapter<SchemeAlarmListBean.ListBean, BaseViewHolder> {
    public GetSchemeAlarmListAdapter(int layoutResId, ArrayList<SchemeAlarmListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SchemeAlarmListBean.ListBean item) {
        DecimalFormat df = new DecimalFormat("######0");
        helper.setText(R.id.tvSchemeAlaramListNum, item.getRowNumber() + "")
                .setText(R.id.tvSchemeAlaramListTime, item.getCreateTime() + "")
                .setText(R.id.tvSchemeAlaramListTarget, item.getAreaNmae())
                .setText(R.id.tvSchemeAlaramListMessage, item.getSendNun());
        if (item.getTypes() == 0) {
            helper.getView(R.id.tvSchemeAlaramListMove1).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvSchemeAlaramListMove2).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvSchemeAlaramListMove3).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvSchemeAlaramListMove4).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvSchemeAlaramListMove5).setVisibility(View.GONE);
//            helper.setText(R.id.tvSchemeAlaramListMove1, df.format(item.getShift1() * 1000) + "(" + item.getMonitorID1() + ")");
//            helper.setText(R.id.tvSchemeAlaramListMove2, df.format(item.getShift2() * 1000) + "(" + item.getMonitorID2() + ")");
//            helper.setText(R.id.tvSchemeAlaramListMove3, df.format(item.getShift3() * 1000) + "(" + item.getMonitorID3() + ")");
//            helper.setText(R.id.tvSchemeAlaramListMove4, df.format(item.getShift4() * 1000) + "(" + item.getMonitorID4() + ")");
            helper.setText(R.id.tvSchemeAlaramListMove1, df.format(item.getShift1() * 1000));
            helper.setText(R.id.tvSchemeAlaramListMove2, df.format(item.getShift2() * 1000));
            helper.setText(R.id.tvSchemeAlaramListMove3, df.format(item.getShift3() * 1000));
            helper.setText(R.id.tvSchemeAlaramListMove4, df.format(item.getShift4() * 1000));
            helper.addOnClickListener(R.id.tvSchemeAlaramListMove1);
            helper.addOnClickListener(R.id.tvSchemeAlaramListMove2);
            helper.addOnClickListener(R.id.tvSchemeAlaramListMove3);
            helper.addOnClickListener(R.id.tvSchemeAlaramListMove4);
        } else {
            helper.setText(R.id.tvSchemeAlaramListMove5, df.format(item.getShift1() * 1000) + "(" + item.getMonitorID1() + ")")
                    .addOnClickListener(R.id.tvSchemeAlaramListMove5);
        }
        if (item.getStates() == 0 || item.getStates() == 1) {
            helper.getView(R.id.ivSchemeAlaramListNumConfirm).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvSchemeAlaramListNumConfirm).setVisibility(View.GONE);
            helper.addOnClickListener(R.id.ivSchemeAlaramListNumConfirm);
        } else {
            helper.getView(R.id.tvSchemeAlaramListNumConfirm).setVisibility(View.VISIBLE);
            helper.getView(R.id.ivSchemeAlaramListNumConfirm).setVisibility(View.GONE);
            helper.setText(R.id.tvSchemeAlaramListNumConfirm, "已确认");
        }
    }
}
