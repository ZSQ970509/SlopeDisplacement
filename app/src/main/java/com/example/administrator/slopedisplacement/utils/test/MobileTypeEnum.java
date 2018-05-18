package com.example.administrator.slopedisplacement.utils.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机类型
 */

public enum MobileTypeEnum {
    XIAOMI(SystemManagerActivity.xiaomi_Battery, SystemManagerActivity.xiaomi_AutoStart),
    LETV(SystemManagerActivity.Letv_Battery, SystemManagerActivity.Letv_AutoStart);

    private ArrayList<PackageAndActivity> mBattery;
    private ArrayList<PackageAndActivity> mAutoStarty;

    MobileTypeEnum(ArrayList<PackageAndActivity> battery, ArrayList<PackageAndActivity> autoStarty) {
        mBattery = battery;
        mAutoStarty = autoStarty;
    }

    public ArrayList<PackageAndActivity> getmBattery() {
        return mBattery;
    }

    public ArrayList<PackageAndActivity> getmAutoStarty() {
        return mAutoStarty;
    }
}
