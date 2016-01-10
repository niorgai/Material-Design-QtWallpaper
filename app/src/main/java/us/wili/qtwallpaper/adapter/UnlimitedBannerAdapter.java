package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import us.wili.qtwallpaper.model.ViewPagerModel;

/**
 * 无限滚动的PagerAdapter
 * Created by qiu on 1/10/16.
 */
public class UnlimitedBannerAdapter extends PagerAdapter implements View.OnClickListener {

    private List<ViewPagerModel> data;

    private SimpleDraweeView[] mViews;

    public UnlimitedBannerAdapter(Context context, List<ViewPagerModel> data) {
        this.data = data;
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        mViews = new SimpleDraweeView[3];
        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = new SimpleDraweeView(context);
            mViews[i].setOnClickListener(this);
        }
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
        draweeViw.setImageURI(Uri.parse(model.imageUrl));
        draweeViw.setTag(model.objectId);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            Log.d("tag: " , v.getTag().toString());
            for (ViewPagerModel model : data) {
                Log.d("model: ", model.objectId);
            }
        }
    }
}
