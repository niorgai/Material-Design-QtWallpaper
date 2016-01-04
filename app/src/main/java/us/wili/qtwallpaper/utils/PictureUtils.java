package us.wili.qtwallpaper.utils;

import android.content.Context;

import java.util.Random;

import us.wili.qtwallpaper.R;

/**
 * use for picture load
 * Created by qiu on 1/3/16.
 */
public class PictureUtils {

    public static int getRandomColor(Context context) {
        int randomColor = new Random().nextInt() % 8;
        switch (randomColor) {
            case 0:
                return context.getResources().getColor(R.color.holder1);
            case 1:
                return context.getResources().getColor(R.color.holder2);
            case 2:
                return context.getResources().getColor(R.color.holder3);
            case 3:
                return context.getResources().getColor(R.color.holder4);
            case 4:
                return context.getResources().getColor(R.color.holder5);
            case 5:
                return context.getResources().getColor(R.color.holder6);
            case 6:
                return context.getResources().getColor(R.color.holder7);
            default:
                return context.getResources().getColor(R.color.holder8);

        }
    }
}
