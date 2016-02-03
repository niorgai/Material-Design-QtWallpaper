package us.wili.qtwallpaper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.connect.BroadcastValue;
import us.wili.qtwallpaper.dialog.LoadingDialog;
import us.wili.qtwallpaper.fragment.CategoryFragment;
import us.wili.qtwallpaper.fragment.HotFragment;
import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.utils.ToastUtil;
import us.wili.qtwallpaper.utils.WxUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PAGE_HOT = 0;
    public static final int PAGE_CATEGORY = 1;

    private SparseArray<Fragment> mFragments;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SimpleDraweeView mAvatar;
    private TextView mUserName;

    private int mContainer;
    private int currentTab = -1;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化屏幕宽度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        MobileConfig.screenWidth = metrics.widthPixels;
        MobileConfig.screenHeight = metrics.heightPixels;

        initViews();

        initData();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.hot);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        if (header != null) {
            mAvatar = (SimpleDraweeView) header.findViewById(R.id.user_avatar);
            mUserName = (TextView) header.findViewById(R.id.user_name);
        }
        mContainer = R.id.container;

        mFragments = new SparseArray<>();
        mFragments.put(PAGE_HOT, new HotFragment());
        mFragments.put(PAGE_CATEGORY, new CategoryFragment());

        mLoadingDialog = new LoadingDialog(this);
    }

    private void initData() {
        //选中hot
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(mContainer, mFragments.get(PAGE_HOT));
        ft.commit();
        currentTab = PAGE_HOT;

        AVUser user = AVUser.getCurrentUser();
        if (user == null) {
            logOutSuccess();
        } else {
            logInSuccess();
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                logInSuccess();
            }
        }, new IntentFilter(BroadcastValue.LOGIN));
        manager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                logOutSuccess();
            }
        }, new IntentFilter(BroadcastValue.LOGOUT));
    }

    private void logInSuccess() {
        if (mNavigationView.getMenu() != null) {
            mNavigationView.getMenu().clear();
        }
        mNavigationView.inflateMenu(R.menu.navi_menu_logout);
        AVUser user = AVUser.getCurrentUser();
        if (user.get("avatorUrl") != null && user.get("avatorUrl") instanceof String) {
            mAvatar.setImageURI(Uri.parse((String) user.get("avatorUrl")));
        }
        if (user.get("nickname") != null && user.get("nickname") instanceof CharSequence) {
            mUserName.setText((CharSequence) user.get("nickname"));
        }
    }

    private void logOutSuccess() {
        if (mNavigationView.getMenu() != null) {
            mNavigationView.getMenu().clear();
        }
        mNavigationView.inflateMenu(R.menu.nav_menu_login);
        mAvatar.setImageURI(Uri.EMPTY);
        mUserName.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 改变选项卡
     * @param tab PAGE_HOT / PAGE_CATEGORY
     */
    private void changeTab(int tab) {
        if (currentTab == tab) {
            return;
        }
        Fragment nextFragment = mFragments.get(tab);
        if (nextFragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!nextFragment.isAdded()) {
            ft.add(mContainer, nextFragment);
        }
        ft.hide(mFragments.get(currentTab));
        ft.show(nextFragment);
        if (!isFinishing()) {
            ft.commitAllowingStateLoss();
        }
        currentTab = tab;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.group_tab) {
            item.setChecked(true);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.hot:
                changeTab(PAGE_HOT);
                return true;
            case R.id.category:
                changeTab(PAGE_CATEGORY);
                return true;
            case R.id.clear_cache:
                Fresco.getImagePipeline().clearCaches();
                ToastUtil.getInstance().showToast(R.string.clear_cache_success);
                return true;
            case R.id.send_feed_back:
                startActivity(new Intent(this, FeedBackActivity.class));
                return true;
            //TODO::
            case R.id.support:
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.log_in:
                mLoadingDialog.show();
                WxUtils.loginIn(this);
                return true;
            case R.id.log_out:
                WxUtils.logOut(this);
                return true;
            default:
                break;
        }
        return true;
    }
}
