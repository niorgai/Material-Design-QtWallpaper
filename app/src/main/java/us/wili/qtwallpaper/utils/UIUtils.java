package us.wili.qtwallpaper.utils;

import android.content.Context;

/**
 * UI
 * Created by qiu on 1/10/16.
 */
public class UIUtils {

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
