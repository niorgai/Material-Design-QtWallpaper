package us.wili.qtwallpaper.widget;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.model.WallpaperItem;

/**
 * 操作图片
 * Created by qiu on 2/6/16.
 */
public class PictureOperationView extends LinearLayout {

    private final int ANIMATION_DURATION = 1000;
    private final int ANIMATION_DELAY = 100;

    private ArrayList<ImageView> imageViews;

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
        imageViews.add((ImageView) findViewById(R.id.img1));
        imageViews.add((ImageView) findViewById(R.id.img2));
        imageViews.add((ImageView) findViewById(R.id.img3));
        imageViews.add((ImageView) findViewById(R.id.img4));
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
                        isShowing = true;
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
}
