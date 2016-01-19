package us.wili.qtwallpaper.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import us.wili.qtwallpaper.R;


/**
 * 状态栏兼容工具类
 * Created by qiu on 9/28/15.
 */
public class StatusBarCompat {

    private static final int INVALID_INDEX = -1;

    public static final int COLOR_DEFAULT = Color.parseColor("#18B1B1");

    public static void compat(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_INDEX) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | localLayoutParams.flags);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            int color = COLOR_DEFAULT;
            if (statusColor != INVALID_INDEX) {
                color = statusColor;
            }
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            //不要重复添加view
            View statusBarView = contentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
            if (activity.findViewById(R.id.root_layout) != null) {
                activity.findViewById(R.id.root_layout).setFitsSystemWindows(true);
            }

        }
    }

//    //设置ActionBar Menu的颜色,解决红米的兼容问题
//    public static void setActionBarText(final Activity activity) {
//        try {
//            final LayoutInflater inflater = activity.getLayoutInflater();
//            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//            field.setAccessible(true);
//            field.setBoolean(inflater, false);
//            LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
//                @Override
//                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                    if (name.equalsIgnoreCase("android.support.v7.view.menu.IconMenuItemView")
//                            || name.equalsIgnoreCase("android.support.v7.view.menu.ActionMenuItemView")) {
//                        final View view;
//                        try {
//                            view = inflater.createView(name, null, attrs);
//                            if (view instanceof TextView)
//                                ((TextView) view).setTextColor(activity.getResources().getColor(R.color.dark_pink));
//                            return view;
//                        } catch (ClassNotFoundException e) {
////                            e.printStackTrace();
//                        } catch (InflateException ex) {
////                            ex.printStackTrace();
//                        }
//                    }
//                    return null;
//                }
//            });
//        } catch (Exception e) {
//
//        }
//
//    }


    public static void compat(Activity activity) {
        compat(activity, INVALID_INDEX);
    }

    //获取状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }
}
