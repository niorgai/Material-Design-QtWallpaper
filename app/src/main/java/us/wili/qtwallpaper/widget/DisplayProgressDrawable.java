package us.wili.qtwallpaper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.facebook.drawee.drawable.ProgressBarDrawable;

import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 图片下载进度条
 * Created by qiu on 1/23/16.
 */
public class DisplayProgressDrawable extends ProgressBarDrawable {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLevel = 0;
    private int maxLevel = 10000;

    private final int backgroundColor = Color.parseColor("#FFFFFF");
    private final int progressColor = Color.parseColor("#4BE4E4");

    private int height;

    public DisplayProgressDrawable(Context context) {
        height = UIUtils.dip2px(context, 2f);
    }

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (getHideWhenZero() && mLevel == 0) {
            return;
        }
        drawBar(canvas, maxLevel, backgroundColor);
        drawBar(canvas, mLevel, getColor());
    }

    private void drawBar(Canvas canvas, int level, int color) {
        int length = (MobileConfig.screenWidth) * level / maxLevel;
        mPaint.setColor(color);
        canvas.drawRect(0, 0, length, height, mPaint);
    }

}
