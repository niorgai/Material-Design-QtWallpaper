package us.wili.qtwallpaper.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * BaseActivity
 * Created by qiu on 12/29/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Handler delayLoadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        delayLoadHandler = new Handler();

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                delayLoadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onDelayLoad();
                    }
                });
            }
        });

        initViews();

        initData();
    }

    protected void initViews() {

    }

    protected void initData() {

    }

    protected void onDelayLoad() {

    }
}
