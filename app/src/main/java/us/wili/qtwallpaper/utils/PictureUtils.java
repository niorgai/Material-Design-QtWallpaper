package us.wili.qtwallpaper.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.ByteArrayOutputStream;
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

    //设置壁纸
    public static void setWallpaper(final Context context, final Uri source) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImagePipeline pipeline = Fresco.getImagePipeline();
                final BaseBitmapDataSubscriber subscriber = new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        setWallPaperWithBitmap(context, bitmap);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        ToastUtil.getInstance().showToast(R.string.set_wallpaper_fail);
                    }
                };
                pipeline.fetchDecodedImage(ImageRequestBuilder.newBuilderWithSource(source).build(), context)
                        .subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
            }
        }).start();
    }

    private static final String CATEGORY_TYPE = "QTWallpaper";

    //保存Bitmap
    private static Uri getBitmapUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, CATEGORY_TYPE, null);
        return Uri.parse(path);
    }

    private static void setWallPaperWithBitmap(Context context, Bitmap bitmap) {
        WallpaperManager manager = WallpaperManager.getInstance(context);
        manager.suggestDesiredDimensions(MobileConfig.screenWidth, MobileConfig.screenHeight);
        Uri bitmapUri = getBitmapUri(context, bitmap);
        if (bitmapUri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                context.startActivity(manager.getCropAndSetWallpaperIntent(bitmapUri));
            } else {
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(bitmapUri, "image/*");
                intent.putExtra("mimeType", "image/*");
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.set_as)));
            }
        } else {
            try {
                manager.setBitmap(bitmap);
                ToastUtil.getInstance().showToast(R.string.set_wallpaper_success);
            } catch (IOException e) {
                ToastUtil.getInstance().showToast(R.string.set_wallpaper_fail);
            }
        }
    }
}
