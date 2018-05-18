package com.example.administrator.slopedisplacement.activity;


import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.slopedisplacement.R;
import com.ffcs.surfingscene.entity.GlobalEyeEntity;
import com.ffcs.surfingscene.function.SurfingScenePlayer;
import com.ffcs.surfingscene.function.onPlayListener;
import com.kj.guradc.VideoActivity;

import butterknife.OnClick;

/**
 *
 */

public class RtspNewVideoActivity extends VideoActivity {
    private boolean videoLoadComplete = false;
    public SurfingScenePlayer splay;
    private TextView prossTV;
    private TextView resultTV;
    private SurfaceView mSurfaceView;
    private GlobalEyeEntity mCurEntity = null;
    @Override
    protected void initComponents() {
//        up_btn = (Button) this.findViewById(R.id.up_btn);
//        down_btn = (Button) this.findViewById(R.id.down_btn);
        prossTV = (TextView) findViewById(R.id.prossTV);
        resultTV = (TextView) findViewById(R.id.resultTV);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view2);
        mSurfaceView.setVisibility(View.VISIBLE);
//        setPlayListener(new OnFinishListener());
//        getProgressValue();
    }
    class OnFinishListener implements onPlayListener {

        @Override
        public void onPlayFail(int state, String msg) {
            videoLoadComplete = false;
            prossTV.setVisibility(View.GONE);
            resultTV.setText("播放失败," + "state:" + state + ",msg:" + msg);
            Log.i("PuIdPlayerActivity", "播放失败," + "state:" + state + ",msg:" + msg);
        }

        @Override
        public void setOnPlaysuccess() {
            videoLoadComplete = true;
            prossTV.setVisibility(View.GONE);
            resultTV.setText("播放成功");
            Log.i("PuIdPlayerActivity", "播放成功");
        }

        @Override
        public void onBufferProssgress(int bufferValue) {
            prossTV.setVisibility(View.VISIBLE);
            prossTV.setText("缓冲进度：" + bufferValue + "%");
            Log.i("PuIdPlayerActivity", "缓冲进度：" + bufferValue + "%");
        }
    }
    @Override
    protected void initData() {
        String rtsp = "rtsp://admin:hckj1234@10.1.4.194:554/h264/ch1/main/av_stream";
        mplayer(rtsp, 1, 0);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_new_hu_xin_video;
    }

    @Override
    public int setSurViewId() {
        return R.id.surface_view2;
    }

    @Override
    protected void onDestroy() {
        RTSPClientStop();// 停止播放视频
        System.gc();
        super.onDestroy();
    }

//    @OnClick({R.id.ivHuXinBack})
//    void OnClick(View view) {
//        switch (view.getId()) {
//            case R.id.ivHuXinBack:
//                finish();
//                break;
//        }
//    }
}

