package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.PictureUtils;

/**
 * DisplayActivity的Adapter
 * Created by qiu on 1/21/16.
 */
public class DisplayPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<WallpaperItem> mItems;

    private SimpleDraweeView[] mViews;

    private ResizeOptions bannerResizeOption;
    private ImageRequest fullRequest;
    private ImageRequest lowRequest;

    public DisplayPagerAdapter(Context context, ArrayList<WallpaperItem> items) {
        mContext = context;
        mItems = items;
        mViews = new SimpleDraweeView[3];
        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = new SimpleDraweeView(context);
            mViews[i].setHierarchy(GenericDraweeHierarchyBuilder.newInstance(context.getResources()).setFadeDuration(0).build());
        }
        bannerResizeOption = new ResizeOptions(MobileConfig.screenWidth, MobileConfig.screenHeight);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews[position % 3];
        if (container.equals(view.getParent())) {
            container.removeView(view);
        }
        container.addView(view);
        SimpleDraweeView draweeViw = (SimpleDraweeView) view;
        WallpaperItem model = mItems.get(position);
        fullRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(model.imageUrl))
                .setResizeOptions(bannerResizeOption)
                .build();
        lowRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(model.imageUrl + PictureUtils.COMPRESS_20))
                .setResizeOptions(bannerResizeOption)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(lowRequest)
                .setImageRequest(fullRequest)
                .setOldController(draweeViw.getController())
                .build();
        draweeViw.setController(controller);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
