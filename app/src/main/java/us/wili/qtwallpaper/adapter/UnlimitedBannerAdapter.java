package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

import us.wili.qtwallpaper.activity.CategoryDetailActivity;
import us.wili.qtwallpaper.model.ViewPagerModel;

/**
 * 无限滚动的PagerAdapter
 * Created by qiu on 1/10/16.
 */
public class UnlimitedBannerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private List<ViewPagerModel> data;

    private SimpleDraweeView[] mViews;
    private ResizeOptions bannerResizeOption;

    public UnlimitedBannerAdapter(Context context) {
        mContext = context;
        mViews = new SimpleDraweeView[3];
        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = new SimpleDraweeView(context);
            mViews[i].setHierarchy(GenericDraweeHierarchyBuilder.newInstance(context.getResources()).setFadeDuration(0).build());
            mViews[i].setOnClickListener(this);
        }
        data = new ArrayList<>();
    }

    public void setData(List<ViewPagerModel> data) {
        this.data = data;
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void setBannerSize(int width, int height) {
        bannerResizeOption = new ResizeOptions(width, height);
    }

    @Override
    public int getCount() {
        return data.size() > 1 ? Integer.MAX_VALUE : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews[position % 3];
        if (data.size() > 1) {
            if (container.equals(view.getParent())) {
                container.removeView(view);
            }
        }
        container.addView(view);
        SimpleDraweeView draweeViw = (SimpleDraweeView) view;
        ViewPagerModel model = data.get(position % data.size());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(model.imageUrl))
                .setResizeOptions(bannerResizeOption)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeViw.getController())
                .build();
        draweeViw.setController(controller);
        draweeViw.setTag(model.objectId);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            Intent intent = CategoryDetailActivity.getIntent(mContext, (String)tag);
            mContext.startActivity(intent);
        }
    }
}
