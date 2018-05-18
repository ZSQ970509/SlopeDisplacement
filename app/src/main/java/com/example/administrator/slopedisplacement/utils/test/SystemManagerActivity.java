package com.example.administrator.slopedisplacement.utils.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class SystemManagerActivity {
    public static final ArrayList<PackageAndActivity> xiaomi_Battery = new ArrayList<PackageAndActivity>() {{
        add(new PackageAndActivity("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
    }};
    public static final ArrayList<PackageAndActivity> xiaomi_AutoStart = new ArrayList<PackageAndActivity>() {{
        add(new PackageAndActivity("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        add(new PackageAndActivity("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
    }};

    public static final ArrayList<PackageAndActivity> Letv_Battery = new ArrayList<PackageAndActivity>() {{
        add(new PackageAndActivity("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
    }};
    public static final ArrayList<PackageAndActivity> Letv_AutoStart = new ArrayList<PackageAndActivity>() {{
        add(new PackageAndActivity("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        add(new PackageAndActivity("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
    }};

}
