package us.wili.qtwallpaper.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.avos.avoscloud.AVUser;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.GridAdapter;
import us.wili.qtwallpaper.model.User;
import us.wili.qtwallpaper.utils.ToastUtil;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 我的收藏
 * Created by qiu on 2/21/16.
 */
public class MyFavouritesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;

    private GridAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.activity_category_detail);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        UIUtils.changeRefreshLayoutColor(mRefreshLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GridAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mRefreshLayout.setRefreshing(true);
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
        //TODO::查询收藏
    }
}
