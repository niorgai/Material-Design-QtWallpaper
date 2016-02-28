package us.wili.qtwallpaper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.GridAdapter;
import us.wili.qtwallpaper.connect.AVCallback;
import us.wili.qtwallpaper.connect.BroadcastValue;
import us.wili.qtwallpaper.model.User;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.ToastUtil;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 我的收藏
 * Created by qiu on 2/21/16.
 */
public class MyFavouritesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CHANGE_DATA = "change_data";

    private SwipeRefreshLayout mRefreshLayout;

    private GridAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.activity_category_detail);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        UIUtils.changeRefreshLayoutColor(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new GridAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(R.string.my_star);

        //当收藏的壁纸发生变化时,通知Adapter
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final ArrayList<WallpaperItem> wallpaperIds = intent.getParcelableArrayListExtra(CHANGE_DATA);
                if (wallpaperIds != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<WallpaperItem> items = mAdapter.getItemList();
                            items.removeAll(wallpaperIds);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }, new IntentFilter(BroadcastValue.FAVOURITE_CHANGE));
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        AVUser user = AVUser.getCurrentUser();
        if (user.get(User.FAVORITES) == null) {
            mRefreshLayout.setRefreshing(false);
            ToastUtil.getInstance().showToast(R.string.no_favourites);
            return;
        }
        JSONArray array = (JSONArray) user.get(User.FAVORITES);
        if (array.isEmpty()) {
            mRefreshLayout.setRefreshing(false);
            ToastUtil.getInstance().showToast(R.string.no_favourites);
            return;
        }
        AVQuery<WallpaperItem> query = AVQuery.getQuery(WallpaperItem.class);
        query.whereContainedIn(WallpaperItem.OBJECT_ID, array);
        query.findInBackground(new AVCallback<WallpaperItem>() {

            @Override
            protected void onSuccess(List<WallpaperItem> list) {
                super.onSuccess(list);
                ArrayList<WallpaperItem> items = new ArrayList<>();
                items.addAll(list);
                mAdapter.update(items);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(AVException e) {
                super.onFailure(e);
                ToastUtil.getInstance().showToast(R.string.no_favourites);
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                mRefreshLayout.setRefreshing(false);
            }
        });

    }
}
