package us.wili.qtwallpaper.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewStub;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.utils.StatusBarCompat;

/**
 * BaseActivity
 * Created by qiu on 12/29/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Handler delayLoadHandler;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.setContentView(R.layout.activity_base);
        StatusBarCompat.compat(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("");
        }

        initViews();

        initData();

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
    }

    protected void initViews() {

    }

    protected void initData() {

    }

    protected void onDelayLoad() {

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewStub stub = (ViewStub) findViewById(R.id.stub);
        stub.setLayoutResource(layoutResID);
        stub.inflate();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mActionBar != null) {
            mActionBar.setTitle(titleId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
