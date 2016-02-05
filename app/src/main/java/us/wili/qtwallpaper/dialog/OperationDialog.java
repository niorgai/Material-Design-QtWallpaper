package us.wili.qtwallpaper.dialog;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import us.wili.qtwallpaper.R;

/**
 * 图片详情,操作的dialog
 * Created by qiu on 2/4/16.
 */
public class OperationDialog extends PopupWindow {

    private Context mContext;
    private View mParent;

    private ArrayList<ImageView> imageViews = new ArrayList<>();

    private boolean isAnimation = false;

    private final int ANIMATION_DURATION = 1000;
    private final int ANIMATION_DELAY = 100;

    public OperationDialog(Context context, View parent) {
        this.mContext = context;
        this.mParent = parent;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_operation, null);
        setContentView(view);
        imageViews.add((ImageView) view.findViewById(R.id.img1));
        imageViews.add((ImageView) view.findViewById(R.id.img2));
        imageViews.add((ImageView) view.findViewById(R.id.img3));
        imageViews.add((ImageView) view.findViewById(R.id.img4));

        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        getContentView().setFocusableInTouchMode(true);
        getContentView().setFocusable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    public boolean isAnimation() {
        return isAnimation;
    }

    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setTranslationY(600);
            if (i == 0) {
                imageViews.get(i).animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimation = true;
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
                        isAnimation = true;
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

    private TimeInterpolator exitInterpolator = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            //x: 0 ~ .333333333, y: 1 ~ 0.8
            //x: 0.333333333 ~ 0.666666667, y: 0.8 ~ 1.5
            //x: 0.666666667 ~ 1, y: 0.8 ~ 1
            if (input < 0.333333333) {
                return input * 4.500000004f;
            } else if (0.333333333 <= input && input < 0.666666667) {
                return ((input * (-2.099999996f)) + 2.199999998f);
            } else {
                return (input * 0.6f) + 0.4f;
            }
//            return 0;
        }
    };
}
