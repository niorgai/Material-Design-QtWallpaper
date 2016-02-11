package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
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
import us.wili.qtwallpaper.widget.DisplayProgressDrawable;
import us.wili.qtwallpaper.widget.PictureOperationView;

/**
 * DisplayActivity的Adapter
 * Created by qiu on 1/21/16.
 */
public class DisplayPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private ArrayList<WallpaperItem> mItems;

    private SimpleDraweeView[] mViews;

    private ResizeOptions bannerResizeOption;

    private PictureOperationView mOperationView;

    public DisplayPagerAdapter(Context context, ArrayList<WallpaperItem> items) {
        mItems = items;
        mViews = new SimpleDraweeView[3];
        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = new SimpleDraweeView(context);
            mViews[i].setHierarchy(GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFadeDuration(0)
                    .setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(context)))
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setProgressBarImage(new DisplayProgressDrawable(context))
                    .build());
            mViews[i].setOnClickListener(this);
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
        SimpleDraweeView draweeView = (SimpleDraweeView) view;
        WallpaperItem model = mItems.get(position);
        ImageRequest fullRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(model.imageUrl))
                .setResizeOptions(bannerResizeOption)
                .build();
        ImageRequest lowRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(model.imageUrl + PictureUtils.COMPRESS_20))
                .setResizeOptions(bannerResizeOption)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(lowRequest)
                .setImageRequest(fullRequest)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
        draweeView.setTag(model);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public void onClick(View v) {
        if (mOperationView.isShowing()) {
            mOperationView.dismiss();
        } else {
            if (v.getTag() != null && v.getTag() instanceof WallpaperItem) {
                WallpaperItem item = (WallpaperItem) v.getTag();
                if (mOperationView.getWallpaperItem() == null || !mOperationView.getWallpaperItem().imageUrl.equals(item.imageUrl)) {
                    mOperationView.setWallpaperItem(item);
                    mOperationView.show();
                }
            }
        }
    }

    public void setOperationView(PictureOperationView mOperationView) {
        this.mOperationView = mOperationView;
    }
}
