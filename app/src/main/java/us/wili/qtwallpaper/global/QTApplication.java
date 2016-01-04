package us.wili.qtwallpaper.global;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Base Application
 * Created by qiu on 12/30/15.
 */
public class QTApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
