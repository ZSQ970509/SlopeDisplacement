package com.example.administrator.slopedisplacement.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.base.BaseActivity;
import com.example.administrator.slopedisplacement.db.UserInfoPref;
import com.example.administrator.slopedisplacement.utils.FormatUtils;
import com.example.administrator.slopedisplacement.utils.L;
import com.example.administrator.slopedisplacement.utils.MusicUtils;
import com.example.administrator.slopedisplacement.utils.TimePickerUtils;
import com.example.administrator.slopedisplacement.widget.CommonDialog;
import com.example.administrator.slopedisplacement.widget.popupwindow.BindViewHelper;
import com.example.administrator.slopedisplacement.widget.popupwindow.CommonPopupWindow;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试页面
 */

public class TestActivity extends BaseActivity {
    @BindView(R.id.line_chart_test)
    LineChart mChart;
    @BindView(R.id.tv_text)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
    }


    CommonPopupWindow mPopupWindow;

    /**
     * 显示popupWindow
     */
    private void showPop() {
        mPopupWindow = new CommonPopupWindow.Builder(this) {
            @Override
            public void popBindView(BindViewHelper popupWindowBindView) {
                popupWindowBindView.setText(R.id.tvSchemeAlaramListNum, "改变");
            }
        }.setContentView(R.layout.view_popupwindow_listview)
                .builder()
                .showAtLocation(R.layout.activity_test, Gravity.CENTER, 0, 0);
    }

    int num = 0;
    Disposable disposable;

    @OnClick({R.id.btn_test1, R.id.btn_test2})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                Toast.makeText(getActivity(), "点击了1", Toast.LENGTH_SHORT).show();
                if (disposable != null)
                    disposable.dispose();
                MusicUtils.instance.playAlarm(getActivity());
//                new CommonDialog(getActivity())
//                        .setMsg("对话框")
//                        .setRightClick(v -> L.e("点击右侧按"))
//                        .show();
                break;
            case R.id.btn_test2:
                Toast.makeText(getActivity(), "点击了2", Toast.LENGTH_SHORT).show();
                MusicUtils.instance.destroy();
//                disposable = Observable.interval(5, 3, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(aLong -> Log.e("触发了", "" + num++));
//                startActivity(new Intent(getActivity(), DataReportActivity.class));

//                showPop();
//                showPickerView();
                break;
        }
    }
}
