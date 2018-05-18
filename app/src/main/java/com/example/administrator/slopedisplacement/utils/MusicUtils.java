package com.example.administrator.slopedisplacement.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.administrator.slopedisplacement.R;

/**
 *
 */

public enum MusicUtils {
    instance;
    private MediaPlayer mMediaPlayer;

    public void playAlarm(Context context) {
        try {
            if (null == mMediaPlayer) {
                mMediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mMediaPlayer.reset();
                        return false;
                    }
                });
//                mMediaPlayer.prepareAsync();
                mMediaPlayer.setLooping(true);
            }
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭音乐播放
     */
    public void destroy() {
        if (null != mMediaPlayer) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
