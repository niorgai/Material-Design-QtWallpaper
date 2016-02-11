package us.wili.qtwallpaper.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.util.Random;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.global.MobileConfig;

/**
 * use for picture load
 * Created by qiu on 1/3/16.
 */
public class PictureUtils {

    public static final String COMPRESS_20 = "?imageMogr2/thumbnail/!20p";

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

    public static void setWallpaper(final Context context, final Uri source) {
        ImagePipeline pipeline = Fresco.getImagePipeline();
        BaseBitmapDataSubscriber subscriber = new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                WallpaperManager manager = WallpaperManager.getInstance(context);
                manager.suggestDesiredDimensions(MobileConfig.screenWidth, MobileConfig.screenHeight);
                try {
                    manager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                ToastUtil.getInstance().showToast("fail");
            }
        };
        pipeline.fetchDecodedImage(ImageRequestBuilder.newBuilderWithSource(source)
                .setResizeOptions(new ResizeOptions(MobileConfig.screenWidth, MobileConfig.screenHeight))
                .build(), context).subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
    }
}
