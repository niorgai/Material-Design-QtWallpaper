package us.wili.qtwallpaper.global;

import android.app.Application;
import android.graphics.drawable.ColorDrawable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import java.util.Random;

import us.wili.qtwallpaper.R;

/**
 * Base Application
 * Created by qiu on 12/30/15.
 */
public class QTApplication extends Application {

    private GenericDraweeHierarchyBuilder builder;

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }

    public GenericDraweeHierarchy getRandomBuilder() {
        if (builder == null) {
            builder = GenericDraweeHierarchyBuilder.newInstance(getResources());
        }
        int randomColor = new Random(System.currentTimeMillis()).nextInt(8);
        switch (randomColor) {
            case 0:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder1)));
                break;
            case 1:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder2)));
                break;
            case 2:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder3)));
                break;
            case 3:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder4)));
                break;
            case 4:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder5)));
                break;
            case 5:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder6)));
                break;
            case 6:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder7)));
                break;
            default:
                builder.setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.holder8)));
                break;
        }
        return builder.build();
    }
}
