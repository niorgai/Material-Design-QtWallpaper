package us.wili.qtwallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import us.wili.qtwallpaper.R;

/**
 * 操作按钮
 * Created by qiu on 2/7/16.
 */
public class ActiveImageView extends ImageView {

    private static final int[] STATE_ACTIVE = {R.attr.state_active};

    private static final int[] FIRST_SIDE = {R.attr.is_first_side};

    private boolean isActive = false;

    private boolean isFirstSide = true;

    public ActiveImageView(Context context) {
        this(context, null);
    }

    public ActiveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActiveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace);
        if (isActive && isFirstSide) {
            drawableState = super.onCreateDrawableState(extraSpace + 2);
            mergeDrawableStates(drawableState, STATE_ACTIVE);
            mergeDrawableStates(drawableState, FIRST_SIDE);
        } else {
            if (isFirstSide) {
                drawableState = super.onCreateDrawableState(extraSpace + 1);
                mergeDrawableStates(drawableState, FIRST_SIDE);
            }
            if (isActive) {
                drawableState = super.onCreateDrawableState(extraSpace + 1);
                mergeDrawableStates(drawableState, STATE_ACTIVE);
            }
        }
        return drawableState;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        if (this.isActive != isActive) {
            this.isActive = isActive;
            refreshDrawableState();
        }
    }

    public void toggleActive() {
        isActive = !isActive;
        refreshDrawableState();
    }

    public boolean isFirstSide() {
        return isFirstSide;
    }

    public void setIsFirstSide(boolean isFirstSide) {
        if (this.isFirstSide != isFirstSide) {
            this.isFirstSide = isFirstSide;
            refreshDrawableState();
        }
    }

    public void toggleFirstSide() {
        isFirstSide = !isFirstSide;
        refreshDrawableState();
    }
}
