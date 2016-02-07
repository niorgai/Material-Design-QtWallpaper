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

    private static final int[] STATE_ACTIVE = {R.attr.stateActive};

    private boolean isActive = false;

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
        if (isActive) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_ACTIVE);
            return drawableState;
        }
        return super.onCreateDrawableState(extraSpace);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
