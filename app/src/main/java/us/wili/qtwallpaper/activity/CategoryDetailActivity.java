package us.wili.qtwallpaper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;

import java.util.ArrayList;
import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.GridAdapter;
import us.wili.qtwallpaper.connect.AVCallback;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 分类详情
 * Created by qiu on 1/17/16.
 */
public class CategoryDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final static String CATEGORY_ID = "category_id";
    private final static String CATEGORY_NAME = "category_name";

    public static Intent getIntent(Context context, String categoryName, String categoryId) {
        Intent intent = new Intent(context, CategoryDetailActivity.class);
        intent.putExtra(CATEGORY_NAME, categoryName);
        intent.putExtra(CATEGORY_ID, categoryId);
        return intent;
    }

    private SwipeRefreshLayout mRefreshLayout;

    private GridAdapter mAdapter;

    private String categoryId;

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.activity_category_detail);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        UIUtils.changeRefreshLayoutColor(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new GridAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void initData() {
        super.initData();
        Intent comingIntent = getIntent();
        setTitle(comingIntent.getStringExtra(CATEGORY_NAME));
        categoryId = comingIntent.getStringExtra(CATEGORY_ID);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        try {
            CategoryItem item = CategoryItem.createWithoutData(CategoryItem.class, categoryId);
            AVQuery<WallpaperItem> bannerQuery = AVRelation.reverseQuery(WallpaperItem.class, WallpaperItem.CATEGORY_ID, item);
            bannerQuery.orderByDescending(WallpaperItem.DOWNLOADS);
            bannerQuery.findInBackground(new AVCallback<WallpaperItem>() {
                @Override
                protected void onSuccess(List<WallpaperItem> list) {
                    super.onSuccess(list);
                    ArrayList<WallpaperItem> items = new ArrayList<>();
                    items.addAll(list);
                    mAdapter.update(items);
                }

                @Override
                protected void onFinish() {
                    super.onFinish();
                    mRefreshLayout.setRefreshing(false);
                }
            });
        } catch (AVException e) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
