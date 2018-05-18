package com.example.administrator.slopedisplacement.activity;


import android.content.res.Configuration;

import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.base.BaseActivity;
import com.example.administrator.slopedisplacement.bean.IVMS_8700_Bean;
import com.example.administrator.slopedisplacement.utils.HuXinUtil;
import com.example.administrator.slopedisplacement.utils.JumpToUtils;
import com.ffcs.surfingscene.function.SurfingScenePlayer;
import com.ffcs.surfingscene.function.onPlayListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */

public class RtspVideoActivity extends BaseActivity {

    @BindView(R.id.GLsurface_view)
    public GLSurfaceView glv;
    @BindView(R.id.layout_pross)
    public View layoutPross;
    @BindView(R.id.txt_pross)
    public TextView prossTV;
    public SurfingScenePlayer splay;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hu_xin_video;
    }

    protected void initView() {
        String rtsp = getIntent().getStringExtra(JumpToUtils.KEY_VIDEO_RTSP);
        splay = new SurfingScenePlayer(this);
        glv = (GLSurfaceView) this.findViewById(R.id.GLsurface_view);
        if (TextUtils.isEmpty(rtsp) || !rtsp.contains("rtsp")) {
            showToast("该视频无法播放");
        } else {
            HuXinUtil.playRtsp(splay, glv, rtsp, playListener);
        }
    }

    onPlayListener playListener = new onPlayListener() {
        @Override
        public void setOnPlaysuccess() {
            prossTV.setText("视频缓冲进度：100%");
            layoutPross.setVisibility(View.GONE);
        }

        @Override
        public void onPlayFail(int arg0, final String error) {
            layoutPross.setVisibility(View.GONE);
            showToast("播放失败：" + error);
        }

        @Override
        public void onBufferProssgress(int bufferValue) {

            if (bufferValue >= 99) {
                prossTV.setText("视频缓冲进度：100%");
                layoutPross.setVisibility(View.GONE);
            } else {
                prossTV.setText("视频缓冲进度：" + bufferValue + "%");
            }
        }
    };

    @Override
    public void finish() {
//        RTSPClientStop();//停止播放视频
        System.gc();
        splay.playEnd();
        super.finish();
    }

    @OnClick({R.id.ivHuXinBack})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivHuXinBack:
                finish();
                break;
        }
    }
}

