package us.wili.qtwallpaper.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Scroller;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.utils.StatusBarCompat;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 下拉返回Layout
 * Created by qiu on 1/23/16.
 */
public class SlideFinishLayout extends FrameLayout {
    //parent
    private View mParentView;

    //边界距离
    private int mEdgeSlop;

    //最小滑动距离
    private int mTouchSlop;

    //状态栏高度
    private int mStatusBarHeight;

    //导航栏高度
    private int mNavBarHeight;

    //初始坐标
    private int mInitY;

    private int mInitX;

    //用于计算x的偏移值
    private int mTempY;

    private Scroller mScroller;

    private boolean isFinish = false;

    private boolean isSliding = false;

    private int mViewHeight;

    //背景色
    private ColorDrawable backDrawable;

    private Window window;

    private onSlideFinishListener finishListener;

    public SlideFinishLayout(Context context) {
        this(context, null);
    }

    public SlideFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mEdgeSlop = ViewConfiguration.get(context).getScaledEdgeSlop();

        mStatusBarHeight = StatusBarCompat.getStatusBarHeight(context);

        mNavBarHeight = UIUtils.getNavigationBarHeight(context);

        mScroller = new Scroller(context);

        if (context instanceof Activity) {
            window = ((Activity) context).getWindow();
            //初始化时以40%的黑色透明作为Activity背景色
            backDrawable = new ColorDrawable(getResources().getColor(R.color.commonBlack));
            backDrawable.setAlpha((int) (255 * 0.4));
            window.setBackgroundDrawable(backDrawable);
        }
    }

    public void setOnFinishListener(onSlideFinishListener finishListener) {
        this.finishListener = finishListener;
    }

    public interface onSlideFinishListener {
        void onSlideFinish();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mParentView = (View) getParent();
            mViewHeight = getHeight() + mNavBarHeight;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitX = (int) event.getRawX();
                mInitY = mTempY = (int) event.getRawY();
                //如果是从状态栏开始,则不拦截
                if (mInitY < mStatusBarHeight) {
                    return false;
                }
                //如果从边缘滑动,那么拦截这个Touch事件
                if (mInitY <= mEdgeSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getY();
                final float deltaX = Math.abs((int) event.getRawX() - mInitX);
                final float deltaY = Math.abs((int) event.getRawY() - mInitY);
                //先避免点击事件的拦截
                if (deltaX < mTouchSlop && deltaY < mTouchSlop) {
                    return false;
                }
                //向下滑动
                if (!isSliding && moveY > mInitY) {
                    if (Math.abs((int)event.getRawY() - mInitY) > mTouchSlop) {
                        //当Y方向的位移远大于X方向的位移时,才判定为关闭
                        if (deltaX * 0.7 < deltaY) {
                            isSliding = true;
                            return true;
                        }
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSliding || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                if (moveY > mInitY && isSliding) {
                    mParentView.scrollBy(0, mTempY - moveY);
                    //计算百分比设置40% - 100%的透明
                    if (window != null) {
                        int pre = (int) (((float)moveY / mViewHeight) * 153 + 102f);
                        backDrawable.setAlpha(255 - pre);
                        window.setBackgroundDrawable(backDrawable);
                    }
                } else if (moveY < mInitY) {
                    //解决连续滑动Activity无法闭合的问题
                    mParentView.scrollTo(0, 0);
                }
                mTempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                if (mParentView.getScrollY() <= -mViewHeight / 2) {
                    isFinish = true;
                    scrollToBottom();
                } else {
                    isFinish = false;
                    scrollToOrigin();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            if (mScroller.isFinished() && isFinish && finishListener != null) {
                finishListener.onSlideFinish();
            }
        }
    }

    //滑动至原点
    private void scrollToOrigin() {
        final int delta = mParentView.getScrollY();
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta, Math.abs(delta));
        postInvalidate();
    }

    //滑动到底部
    private void scrollToBottom() {
        backDrawable.setAlpha(0);
        window.setBackgroundDrawable(backDrawable);
        final int delta = mViewHeight + mParentView.getScrollY() - mStatusBarHeight - mNavBarHeight;
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta + 1, Math.abs(delta));
        postInvalidate();
    }
}
