package us.wili.qtwallpaper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVQuery;

import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.ListAdapter;
import us.wili.qtwallpaper.connect.AVCallback;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * 分类Fragment 77693
 * Created by qiu on 1/8/16.
 */
public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private ListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
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

        mAdapter = new ListAdapter(getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 100;
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        AVQuery<CategoryItem> bannerQuery = AVQuery.getQuery(CategoryItem.class);
        bannerQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bannerQuery.orderByDescending(CategoryItem.ORDER);
        bannerQuery.findInBackground(new AVCallback<CategoryItem>() {
            @Override
            protected void onSuccess(List<CategoryItem> list) {
                super.onSuccess(list);
                mAdapter.update(list);
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
