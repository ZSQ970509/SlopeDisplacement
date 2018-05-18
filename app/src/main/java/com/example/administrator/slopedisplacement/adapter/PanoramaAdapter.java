package com.example.administrator.slopedisplacement.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.activity.PlanLayoutOfPanoramaActivity;
import com.example.administrator.slopedisplacement.bean.DriverBean;
import com.example.administrator.slopedisplacement.bean.PanoramaImgBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/18.
 */

public class PanoramaAdapter extends BaseItemDraggableAdapter<PanoramaImgBean.ListBean, BaseViewHolder> {
    public PanoramaAdapter(int layoutResId, ArrayList<PanoramaImgBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PanoramaImgBean.ListBean item) {
        helper.setText(R.id.tvPanoramaDate, item.getEndTime() + "");
        Glide.with(mContext).load(item.getPuzzleImg())//拿到头像本地存放路径
                .error(R.mipmap.ic_launcher)//失败默认
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存变换的资源
                .skipMemoryCache(false)// 设置是否跳过内存缓存
                .into((ImageView) helper.getView(R.id.ivPanorama));

    }
}
