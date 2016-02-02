package us.wili.qtwallpaper.global;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.umeng.socialize.PlatformConfig;

import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.AVUtils;
import us.wili.qtwallpaper.utils.WxUtils;

/**
 * Base Application
 * Created by qiu on 12/30/15.
 */
public class QTApplication extends Application {

    private static Context mContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this, config);

//        Lean cloud
        AVObject.registerSubclass(CategoryItem.class);
        AVObject.registerSubclass(WallpaperItem.class);
        AVOSCloud.initialize(this, AVUtils.APP_ID, AVUtils.APP_KEY);

//        Umeng
        PlatformConfig.setWeixin(WxUtils.APP_ID, WxUtils.APP_SECRET);
    }

    public static Context getMyApplicationContext() {
        return mContext;
    }
}
