package com.example.administrator.slopedisplacement.widget.pointImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.slopedisplacement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 点图
 */

public class PointFrameLayout extends FrameLayout {
    private Context mContext;
    /**
     * 背景图
     */
    private ImageView mIvBg;
    /**
     * 水印层
     */
    private ImageView mIvTran;
    private FrameLayout mFrameLayout;
    /**
     * 点、实线、虚线相关数据
     */
    private PointDataBean imgPointBean;
    private int mPointSize;//点的大小
    /**
     * 所有点的ImageView
     */
    private HashMap<String, ImageView> mPointImageViews = new HashMap<>();
    private List<String> mAnimationMonitorIdAlarm = new ArrayList<>();
    private List<String> mAnimationMonitorIdMonitor = new ArrayList<>();
    private boolean mIsPreparePoint = false;//点是否加载完成

    public PointFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public PointFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.initView();
    }

    private void initView() {
        mPointSize = dip2px(15);
        View imgPointLayout = inflate(mContext, R.layout.widget_point_img, this);
        mIvBg = (ImageView) imgPointLayout.findViewById(R.id.IvWidgetPointAndImg);
        mIvTran = (ImageView) imgPointLayout.findViewById(R.id.IvWidgetTran);
        mFrameLayout = (FrameLayout) imgPointLayout.findViewById(R.id.FlWidgetPointAndImg);

    }

    /**
     * 设置数据
     *
     * @param imgPointBean
     */
    public void setPointsInfo(PointDataBean imgPointBean) {
        this.imgPointBean = imgPointBean;
    }

    /**
     * 设置背景图
     *
     * @param bgImgUrl 图片网络地址
     */
    public void setBgImgUrl(String bgImgUrl) {
        Glide.with(mContext)
                .load(bgImgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        //获取图片原始宽高
                        int originalWidth = resource.getWidth();//原始图的宽度
                        int originalHeight = resource.getHeight();//原始图的高度

                        //获取图片显示（即拉伸后的图）后的宽高
                        double currentWidth;//拉伸后图的宽度
                        double currentHeight;//拉伸后图的高度
                        if (originalWidth / originalHeight - mFrameLayout.getWidth() / mFrameLayout.getHeight() > 0) {//图片的宽大于容器的宽
                            double tmpHeight = mFrameLayout.getWidth() * originalHeight / originalWidth;
                            if (tmpHeight > mFrameLayout.getHeight()) {
                                currentWidth = mFrameLayout.getWidth() * mFrameLayout.getHeight() / tmpHeight;
                                currentHeight = mFrameLayout.getHeight();
                            } else {
                                currentWidth = mFrameLayout.getWidth();
                                currentHeight = tmpHeight;
                            }
                        } else {
                            double tmpWidth = mFrameLayout.getHeight() * originalWidth / originalHeight;
                            if (tmpWidth > mFrameLayout.getWidth()) {
                                currentWidth = mFrameLayout.getWidth();
                                currentHeight = mFrameLayout.getHeight() * mFrameLayout.getWidth() / tmpWidth;
                            } else {
                                currentWidth = tmpWidth;
                                currentHeight = mFrameLayout.getHeight();
                            }
                        }
                        refreshData(currentWidth, currentHeight);
                        addPoint();

                        //创建一个透明层，用于描绘线和文字
                        Bitmap bm = Bitmap.createBitmap((int) currentWidth, (int) currentHeight, Bitmap.Config.ARGB_8888);
                        Bitmap bitmap = createWaterMaskBitmap(bm);
                        mIvTran.setImageBitmap(bitmap);

                        //压缩图片
                        Matrix matrix = new Matrix();
                        matrix.setScale((float) (currentWidth / resource.getWidth()), (float) (currentHeight / resource.getHeight()));
                        resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
                        mIvBg.setImageBitmap(resource);
                    }
                });
    }

    /**
     * 改变背景图
     *
     * @param bgImgUrl 图片网络地址
     */
    public void changeBg(String bgImgUrl) {
        Glide.with(mContext)
                .load(bgImgUrl)
                .asBitmap()
                .into(mIvBg);
    }

    /**
     * 根据图片的原始高和宽、拉伸后的高、宽更新imgPointBean里点的数据
     *
     * @param currentWidth  拉伸后图的宽度
     * @param currentHeight 拉伸后图的高度
     */
    private void refreshData(double currentWidth, double currentHeight) {
        mIsPreparePoint = false;
        List<PointBean> pointLists = imgPointBean.getPointBeanList();
        for (PointBean pointBean : pointLists) {
            //初始化
            // 点在拉伸后的图里的位置
            pointBean.setShowMarginTop((int) (currentHeight * pointBean.getYScale() / 100));
            pointBean.setShowMarginLeft((int) (currentWidth * pointBean.getXScale() / 100));
//            pointBean.setShowMarginTop(getMarginLine(mFrameLayout.getHeight(), currentHeight, mPointSize, pointBean.getYScale()));
//            pointBean.setShowMarginLeft(getMarginLine(mFrameLayout.getHeight(), currentHeight, mPointSize, pointBean.getXScale()));
            // 点在屏幕里实际的位置
            pointBean.setMarginLeft(getMargin(mFrameLayout.getWidth(), currentWidth, mPointSize, pointBean.getXScale()));
            pointBean.setMarginTop(getMargin(mFrameLayout.getHeight(), currentHeight, mPointSize, pointBean.getYScale()));
        }
    }

    /**
     * 添加点
     */
    private void addPoint() {
        mFrameLayout.removeAllViews();
        List<PointBean> pointLists = imgPointBean.getPointBeanList();
        ImageView imageView;
        for (PointBean pointBean : pointLists) {
            //添加点
            imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.point_defult);
            imageView.setTag(pointBean.getPointIndex());
            LayoutParams layoutParams = new LayoutParams(mPointSize, mPointSize);
            layoutParams.leftMargin = pointBean.getMarginLeft();
            layoutParams.topMargin = pointBean.getMarginTop();
            imageView.setLayoutParams(layoutParams);
            mFrameLayout.addView(imageView);
            mPointImageViews.put(pointBean.getmMonitorID(), imageView);
        }
        mIsPreparePoint = true;
    }

    public enum AnimationType {
        ALARM, MONITOR
    }

    /**
     * 启动点的动画(闪烁)
     *
     * @param monitorId
     */
    public void startPointAnimation(String monitorId, AnimationType animationType) {
        ImageView imageView = mPointImageViews.get(monitorId);
        if (imageView != null) {
            if (animationType == AnimationType.ALARM) {
                imageView.setImageResource(R.drawable.anim_point_alarm);
                ((AnimationDrawable) imageView.getDrawable()).start();
                mAnimationMonitorIdAlarm.add(monitorId);
            } else {
                imageView.setImageResource(R.drawable.anim_point_monitor);
                ((AnimationDrawable) imageView.getDrawable()).start();
                mAnimationMonitorIdMonitor.add(monitorId);
            }
        }
    }

    /**
     * 停止点的动画(闪烁)
     *
     * @param monitorId
     */
    public void stopPointAnimation(String monitorId) {
        ImageView imageView = mPointImageViews.get(monitorId);
        if (imageView != null && imageView.getDrawable() != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) imageView.getDrawable()).stop();
            }
            imageView.setImageResource(R.mipmap.point_defult);
        }
    }

    /**
     * 停止所有告警点的动画(闪烁)
     */
    public void stopAllPointAnimationAlarm() {
        if (mAnimationMonitorIdAlarm == null || mAnimationMonitorIdAlarm.size() == 0) {
            return;
        }
        for (String monitorId : mAnimationMonitorIdAlarm) {
            stopPointAnimation(monitorId);
        }
    }

    /**
     * 停止所有巡航点的动画(闪烁)
     */
    public void stopAllPointAnimationMonitor() {
        if (mAnimationMonitorIdMonitor == null || mAnimationMonitorIdMonitor.size() == 0) {
            return;
        }
        for (String monitorId : mAnimationMonitorIdMonitor) {
            stopPointAnimation(monitorId);
        }
    }

    /**
     * 停止所有点的动画(闪烁)
     */
    public void stopAllPointAnimation() {
        Iterator iter = mPointImageViews.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            ImageView imageView = (ImageView) entry.getValue();
            if (imageView != null && imageView.getDrawable() instanceof AnimationDrawable)
                ((AnimationDrawable) imageView.getDrawable()).stop();
        }
    }

    /**
     * 添加水印(文字和线)
     *
     * @param bitmap 图
     * @return
     */
    private Bitmap createWaterMaskBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);  //在画布 0，0坐标上开始绘制
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        Paint mPaint1 = new Paint();
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setColor(Color.parseColor("#ccffff00"));
        mPaint1.setTextSize(21f);
        mPaint.setColor(Color.parseColor("#ccff0000"));
        mPaint.setTextSize(21f);
        //添加文字
        for (PointBean pointBean : imgPointBean.getPointBeanList()) {
            if (TextUtils.isEmpty(pointBean.getPointName()))
                continue;
            int margin = pointBean.getPointName().length();
            canvas.drawText(pointBean.getPointName(), pointBean.getShowMarginLeft() - margin * 10, pointBean.getShowMarginTop() - 20, mPaint1); //在画布上绘制水印图片

            canvas.drawText(pointBean.getPointName(), pointBean.getShowMarginLeft() - margin * 10, pointBean.getShowMarginTop() - 20, mPaint); //在画布上绘制水印图片
        }
        //添加实线
        mPaint.setStrokeWidth(3);
        for (LineBean lineBean : imgPointBean.getLineList()) {
            PointBean startPoint = imgPointBean.getPointBeanList().get(lineBean.getStartIndex());
            PointBean endPoint = imgPointBean.getPointBeanList().get(lineBean.getEndIndex());
            canvas.drawLine(startPoint.getShowMarginLeft(), startPoint.getShowMarginTop()
                    , endPoint.getShowMarginLeft(), endPoint.getShowMarginTop(), mPaint);
        }
        for (LineBean lineBean : imgPointBean.getDottedLineList()) {
            PointBean startPoint = imgPointBean.getPointBeanList().get(lineBean.getStartIndex());
            PointBean endPoint = imgPointBean.getPointBeanList().get(lineBean.getEndIndex());
            DashPathEffect pathEffect = new DashPathEffect(new float[]{4, 3}, 1);
            Paint paint = new Paint();
            paint.reset();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.parseColor("#FF4081"));
            paint.setAntiAlias(true);
            paint.setPathEffect(pathEffect);
            Path path = new Path();
            path.moveTo(startPoint.getShowMarginLeft(), startPoint.getShowMarginTop());
            path.lineTo(endPoint.getShowMarginLeft(), endPoint.getShowMarginTop());
            canvas.drawPath(path, paint);
        }


        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();// 存储
        return bitmap;
    }

    public boolean isPreparePoint() {
        return mIsPreparePoint;
    }

    /**
     * 获取在容器里实际的位置
     *
     * @param layout        容器的高/宽
     * @param img           显示图的高/宽
     * @param pointViewSize 点的大小
     * @param scale         比例
     * @return
     */
    private int getMargin(int layout, double img, int pointViewSize, double scale) {
        return (int) ((layout - img) / 2.0 - pointViewSize / 2.0 + img * scale / 100);
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
