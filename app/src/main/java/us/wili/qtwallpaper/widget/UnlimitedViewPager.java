package us.wili.qtwallpaper.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import us.wili.qtwallpaper.adapter.UnlimitedBannerAdapter;
import us.wili.qtwallpaper.model.ViewPagerModel;

/**
 * 数据大于1时
 * 无限滚动+自动轮播的ViewPager
 * Created by qiu on 1/10/16.
 */
public class UnlimitedViewPager extends ViewPager {
    private static final int MESSAGE_SCROLL = 100;

    private static final int SCROLL_TIME = 3000;

    private UnlimitedBannerAdapter mAdapter;

    private List<ViewPagerModel> data;

    private boolean isScrolling = false;

    private WeakHandler mScrollHandler = new WeakHandler(this);

    private int dataSize = 0;

    public int getCurrentPos() {
        return data == null ? getCurrentItem() : getCurrentItem() % dataSize;
    }

    public UnlimitedViewPager(Context context) {
        this(context, null);
    }

    public UnlimitedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setViewPagerScrollTime();
    }

    /**
     * 设置数据,当数据大于1开启自动轮播
     * 数据小于3时需要添加到3个,否则滑动效果不好
     */
    public void setAdapterData(List<ViewPagerModel> views, int dataSize) {
        stopAutoScroll();
        this.data = views;
        this.dataSize = dataSize;
        setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % dataSize));
        setOffscreenPageLimit(1);
        if (mAdapter == null) {
            mAdapter = new UnlimitedBannerAdapter(getContext(), data);
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (data.size() > 1) {
            startAutoScroll();
        }
    }

    /**
     * 开始自动轮播
     */
    private void startAutoScroll() {
        if (data != null && data.size() > 1 && !isScrolling) {
            isScrolling = true;
            mScrollHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL, SCROLL_TIME);
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoScroll() {
        if (data != null && data.size() > 1) {
            isScrolling = false;
            mScrollHandler.removeMessages(MESSAGE_SCROLL);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //防止内存泄露
        mScrollHandler.removeMessages(MESSAGE_SCROLL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //防止手指与Handler冲突
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                break;
            case MotionEvent.ACTION_UP:
                startAutoScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 改变ViewPager的滑动时间
     */
    public void setViewPagerScrollTime() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            MyScroller scroller = new MyScroller(getContext());
            mScroller.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        //默认1秒
        private int mDuration = 1000;

        public MyScroller(Context context) {
            this(context, null);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        //不可见时暂停Handler
        if (visibility == VISIBLE) {
            startAutoScroll();
        } else {
            stopAutoScroll();
        }
    }

    //自动滚动的Handler
    private static class WeakHandler extends Handler {
        private WeakReference<UnlimitedViewPager> mViewPagers;

        public WeakHandler(UnlimitedViewPager pager) {
            mViewPagers = new WeakReference<>(pager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UnlimitedViewPager viewPager = mViewPagers.get();
            if (viewPager != null) {
                int pos = viewPager.getCurrentItem();
                viewPager.setCurrentItem(pos + 1, true);
                sendEmptyMessageDelayed(MESSAGE_SCROLL, SCROLL_TIME);
            }
        }
    }

}
