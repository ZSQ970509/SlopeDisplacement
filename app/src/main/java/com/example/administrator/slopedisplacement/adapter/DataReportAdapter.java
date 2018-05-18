package com.example.administrator.slopedisplacement.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeAreaListJson;
import com.example.administrator.slopedisplacement.bean.json.GetDatSchemeFixedListJson;
import com.example.administrator.slopedisplacement.fragment.CruiseCurveAreaMapFragment;
import com.example.administrator.slopedisplacement.fragment.CruiseDataFragment;
import com.example.administrator.slopedisplacement.fragment.FixedFlowDataFragment;
import com.example.administrator.slopedisplacement.fragment.FixedPointCurveAreaMapFragment;

/**
 *
 */

public class DataReportAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = new String[]{
            "巡航流水数据",
            "定点流水数据",
            "巡航曲线区域图",
            "定点曲线区域图"
    };
    private Fragment[] fragments;
    private GetDatSchemeAreaListJson mArealListJson;//区域列表数据
    private GetDatSchemeFixedListJson mFixedListJson;//定点列表数据
    private String mSchemeID = "";

    public DataReportAdapter(FragmentManager fm, GetDatSchemeAreaListJson arealListJson, GetDatSchemeFixedListJson fixedListJson, String schemeID) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        this.mArealListJson = arealListJson;
        this.mFixedListJson = fixedListJson;
        this.mSchemeID = schemeID;
    }


    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = CruiseDataFragment.newInstance(mArealListJson, mSchemeID);
                    break;
                case 1:
                    fragments[position] = FixedFlowDataFragment.newInstance(mFixedListJson, mSchemeID);
                    break;
                case 2:
                    fragments[position] = CruiseCurveAreaMapFragment.newInstance(mArealListJson, mSchemeID);
                    break;
                case 3:
                    fragments[position] = FixedPointCurveAreaMapFragment.newInstance(mFixedListJson, mSchemeID);
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }


    @Override
    public int getCount() {
        return TITLES.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
