package us.wili.qtwallpaper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.adapter.GridAdapter;

/**
 * 热门
 * Created by qiu on 1/5/16.
 */
public class HotFragment extends Fragment {

    private RecyclerView mRecylerView;

    //热门
    private GridAdapter mHotAdapter;
    private GridLayoutManager mHotManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecylerView = (RecyclerView) view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHotManager = new GridLayoutManager(getContext(), 2);
        mHotAdapter = new GridAdapter(getContext());
        mRecylerView.setAdapter(mHotAdapter);
        mRecylerView.setLayoutManager(mHotManager);
    }
}
