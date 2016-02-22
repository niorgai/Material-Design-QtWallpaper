package us.wili.qtwallpaper.widget;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.connect.BroadcastValue;
import us.wili.qtwallpaper.model.User;
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

    private final int ANIMATION_DURATION = 600;
    private final int ANIMATION_DELAY = 50;
    private final int Y_SHOW = 400;
    private final int Y_HIDE = 0;

    private BaseInterpolator mShowInterpolator;
    private BaseInterpolator mDismissInterpolator;

    private ArrayList<ActiveImageView> imageViews;

    private boolean isInAnimation = false;
    private boolean isShowing = false;

    private WallpaperItem mWallpaperItem;

    private AVUser mCurrentUser;

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

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastValue.LOGIN);
        filter.addAction(BroadcastValue.LOGOUT);
        manager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkFavourites();
            }
        }, filter);

        mShowInterpolator = new OvershootInterpolator(1.5f);
        mDismissInterpolator = new DecelerateInterpolator(2f);
    }

    public void show() {
        if (isShowing || isInAnimation) {
            return;
        }
        setVisibility(VISIBLE);
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setTranslationY(Y_SHOW);
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
                }).translationY(Y_HIDE).setDuration(ANIMATION_DURATION).setInterpolator(mShowInterpolator).start();
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
                }).setStartDelay(i * ANIMATION_DELAY).translationY(Y_HIDE).setDuration(ANIMATION_DURATION).setInterpolator(mShowInterpolator).start();
            } else {
                imageViews.get(i).animate().setStartDelay(i * ANIMATION_DELAY).translationY(Y_HIDE).setDuration(ANIMATION_DURATION).setInterpolator(mShowInterpolator).start();
            }
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void dismiss() {
        if (!isShowing || isInAnimation) {
            return;
        }
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setTranslationY(Y_HIDE);
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
                }).translationY(Y_SHOW).setDuration(ANIMATION_DURATION).setInterpolator(mDismissInterpolator).start();
            } else if (i == 3) {
                imageViews.get(i).animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isInAnimation = false;
                        isShowing = false;
                        setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setStartDelay(i * ANIMATION_DELAY).translationY(Y_SHOW).setDuration(ANIMATION_DURATION).setInterpolator(mDismissInterpolator).start();
            } else {
                imageViews.get(i).animate().setStartDelay(i * ANIMATION_DELAY).translationY(Y_SHOW).setDuration(ANIMATION_DURATION).setInterpolator(mDismissInterpolator).start();
            }
        }
    }

    public void setWallpaperItem(WallpaperItem mWallpaperItem) {
        this.mWallpaperItem = mWallpaperItem;
        checkFavourites();
    }

    //检查当前照片是否被添加到收藏
    private void checkFavourites() {
        mCurrentUser = AVUser.getCurrentUser();
        if (mCurrentUser != null) {
            JSONArray array = (JSONArray) mCurrentUser.get(User.FAVORITES);
            if (array == null) {
                array = new JSONArray();
            }
            if (array.contains(mWallpaperItem.getObjectId())) {
                imageViews.get(1).setIsActive(true);
            } else {
                imageViews.get(1).setIsActive(false);
            }
        }
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
                WxUtils.shareToWxSession((Activity) getContext(), mWallpaperItem);
                break;
            case R.id.moment:
                WxUtils.shareToWxMoment((Activity) getContext(), mWallpaperItem);
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
        if (mCurrentUser == null) {
            WxUtils.loginIn((Activity) getContext());
            return;
        }
        JSONArray array = (JSONArray) mCurrentUser.get(User.FAVORITES);
        if (array == null) {
            array = new JSONArray();
        }
        if (array.contains(mWallpaperItem.getObjectId())) {
            array.remove(mWallpaperItem.getObjectId());
        } else {
            array.add(mWallpaperItem.getObjectId());
        }
        mCurrentUser.put(User.FAVORITES, array);
        imageViews.get(1).setClickable(false);
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    imageViews.get(1).toggleActive();
                    imageViews.get(1).setClickable(true);
                } else {
                    ToastUtil.getInstance().showToast(R.string.operate_fail);
                }
            }
        });
    }
}
