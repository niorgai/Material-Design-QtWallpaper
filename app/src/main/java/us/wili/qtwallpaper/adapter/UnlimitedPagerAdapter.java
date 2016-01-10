package us.wili.qtwallpaper.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限滚动的PagerAdapter
 * Created by qiu on 1/10/16.
 */
public class UnlimitedPagerAdapter extends PagerAdapter {

    private List<View> data;

    public UnlimitedPagerAdapter(List<View> data) {
        this.data = data;
        if (this.data == null) {
            this.data = new ArrayList<>();
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
        View view;
        if (data.size() > 1) {
            view = data.get(position % data.size());
            if (container.equals(view.getParent())) {
                container.removeView(view);
            }
        } else {
            view = data.get(position);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
