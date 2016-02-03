package us.wili.qtwallpaper.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.HotAdapter;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 热门
 * Created by qiu on 1/5/16.
 */
public class HotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    //热门
    private HotAdapter mHotAdapter;

    private WeakHandler mRefreshHandler;
    private static final int REFRESH_COMPLETE = 1;

    private List<CategoryItem> mBanner;
    private ArrayList<WallpaperItem> mItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.changeRefreshLayoutColor(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHotAdapter = new HotAdapter(getContext());
        mRecyclerView.setAdapter(mHotAdapter);

        mRefreshHandler = new WeakHandler(this);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<CategoryItem> bannerQuery = AVQuery.getQuery(CategoryItem.class);
                bannerQuery.whereEqualTo(CategoryItem.IS_HOT, true);
                try {
                    mBanner = bannerQuery.find();
                } catch (AVException e) {
                    mRefreshHandler.sendEmptyMessage(REFRESH_COMPLETE);
                    return;
                }
                AVQuery<WallpaperItem> gridQuery = AVQuery.getQuery(WallpaperItem.class);
                gridQuery.orderByDescending(WallpaperItem.DOWNLOADS);
                gridQuery.limit(36);
                try {
                    mItemList = (ArrayList<WallpaperItem>) gridQuery.find();
                } catch (AVException e) {
                    mRefreshHandler.sendEmptyMessage(REFRESH_COMPLETE);
                }
                mRefreshHandler.sendEmptyMessage(REFRESH_COMPLETE);
            }
        }).start();

    }

    static class WeakHandler extends Handler {
        private final WeakReference<HotFragment> fragmentWeakReference;

        public WeakHandler(HotFragment fragment) {
            this.fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HotFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                fragment.mHotAdapter.setBanners(fragment.mBanner);
                fragment.mHotAdapter.setWallPaper(fragment.mItemList);
                fragment.mRefreshLayout.setRefreshing(false);
            }
        }
    }
}
