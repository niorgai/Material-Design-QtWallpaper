package us.wili.qtwallpaper.widget;

import android.Manifest;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.PictureUtils;
import us.wili.qtwallpaper.utils.ToastUtil;
import us.wili.qtwallpaper.utils.WxUtils;

/**
 * 操作图片
 * Created by qiu on 2/6/16.
 */
public class PictureOperationView extends LinearLayout implements View.OnClickListener {

    private final int CODE_REQUEST_WRITE_STORAGE = 8;

    private final int ANIMATION_DURATION = 1000;
    private final int ANIMATION_DELAY = 100;

    private ArrayList<ActiveImageView> imageViews;

    private boolean isInAnimation = false;
    private boolean isShowing = false;

    private WallpaperItem mWallpaperItem;

    public PictureOperationView(Context context) {
        this(context, null);
    }

    public PictureOperationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureOperationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(GONE);
        inflate(getContext(), R.layout.view_picture_operation, this);
        setGravity(Gravity.BOTTOM);
        setOrientation(HORIZONTAL);
        imageViews = new ArrayList<>();
        imageViews.add((ActiveImageView) findViewById(R.id.save));
        imageViews.add((ActiveImageView) findViewById(R.id.favourite));
        imageViews.add((ActiveImageView) findViewById(R.id.session));
        imageViews.add((ActiveImageView) findViewById(R.id.moment));
        for (View view : imageViews) {
            view.setOnClickListener(this);
        }
    }

    private TimeInterpolator enterInterpolator = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            //x: 0 - 0.675, y: 0 - 1.35; y = 2x;
            //x: 0.675 - 0.925, y: 1.35 - 0.85; y = -2x + 2.7
            //x: 0.925 - 1, y: 0.85 - 1; y = 2x - 1
            if (input < 0.8125) {
                return input * 1.6f;
            } else if (0.675 <= input && input < 0.925) {
                return ((input * (-2f)) + 2.7f);
            } else {
                return (input * 2f) - 1f;
            }
        }
    };

    public void show() {
        if (isShowing || isInAnimation) {
            return;
        }
        setVisibility(VISIBLE);
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setTranslationY(600);
            if (i == 0) {
                imageViews.get(i).animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isInAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).translationY(0).setDuration(ANIMATION_DURATION).setInterpolator(enterInterpolator).start();
            } else if (i == 3) {
                imageViews.get(i).animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isInAnimation = false;
                        isShowing = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setStartDelay(i * ANIMATION_DELAY).translationY(0).setDuration(ANIMATION_DURATION).setInterpolator(enterInterpolator).start();
            } else {
                imageViews.get(i).animate().setStartDelay(i * ANIMATION_DELAY).translationY(0).setDuration(ANIMATION_DURATION).setInterpolator(enterInterpolator).start();
            }
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void dismiss() {
        setVisibility(GONE);
        isShowing = false;
    }

    public void setWallpaperItem(WallpaperItem mWallpaperItem) {
        this.mWallpaperItem = mWallpaperItem;
    }

    public WallpaperItem getWallpaperItem() {
        return mWallpaperItem;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                setWallPaper();
                break;
            case R.id.favourite:
                toggleFavourite();
                break;
            case R.id.session:
                WxUtils.shareToWxSession();
                break;
            case R.id.moment:
                WxUtils.shareToWxMoment();
                break;
        }
    }

    //设置壁纸,需要判断是否有读取内存卡的权限
    private void setWallPaper() {
        //首先检查是否拥有权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            final Activity activity = (Activity) getContext();
            //接着判断是否需要给予获取权限的提示
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(this, getContext().getString(R.string.set_wallpaper_need_permission), Snackbar.LENGTH_LONG).setAction(getContext().getString(R.string.click_to_grant_permission), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_WRITE_STORAGE);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_WRITE_STORAGE);
            }
        } else {
            PictureUtils.setWallpaper(getContext(), Uri.parse(mWallpaperItem.imageUrl));
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限获取成功
                PictureUtils.setWallpaper(getContext(), Uri.parse(mWallpaperItem.imageUrl));
            } else {
                ToastUtil.getInstance().showToast(R.string.set_wallpaper_grant_permission_fail);
            }
        }
    }

    //设置/取消收藏
    private void toggleFavourite() {

    }
}
