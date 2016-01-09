package us.wili.qtwallpaper.global;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.facebook.drawee.backends.pipeline.Fresco;

import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.AVUtils;

/**
 * Base Application
 * Created by qiu on 12/30/15.
 */
public class QTApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        AVObject.registerSubclass(CategoryItem.class);
        AVObject.registerSubclass(WallpaperItem.class);
        AVOSCloud.initialize(this, AVUtils.APP_ID, AVUtils.APP_KEY);
    }
}
