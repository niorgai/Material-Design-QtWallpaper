package us.wili.qtwallpaper.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * BaseFragment
 * Created by qiu on 1/17/16.
 */
public abstract class BaseFragment extends Fragment {

    private Handler handler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();

        view.post(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onDelayLoad();
                    }
                });
            }
        });
    }

    protected void onDelayLoad() {

    }
}
