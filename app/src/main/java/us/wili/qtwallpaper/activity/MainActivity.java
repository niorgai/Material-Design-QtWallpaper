package us.wili.qtwallpaper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
import us.wili.qtwallpaper.model.User;
import us.wili.qtwallpaper.utils.ToastUtil;
import us.wili.qtwallpaper.utils.WxUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PAGE_HOT = 0;
    public static final int PAGE_CATEGORY = 1;

    private SparseArray<Fragment> mFragments;
    private FragmentManager mFragmentManager;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private SimpleDraweeView mAvatar;
    private TextView mUserName;

    private int mContainer;
    private int currentTab = -1;

    private LoadingDialog mLoadingDialog;

    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化屏幕宽度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        MobileConfig.screenWidth = metrics.widthPixels;
        MobileConfig.screenHeight = metrics.heightPixels;

        initViews(savedInstanceState);

        initData();
    }

    private void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        if (header != null) {
            mAvatar = (SimpleDraweeView) header.findViewById(R.id.user_avatar);
            mUserName = (TextView) header.findViewById(R.id.user_name);
        }
        mContainer = R.id.container;

        mFragmentManager = getSupportFragmentManager();

        mFragments = new SparseArray<>();
        int saveTab = PAGE_HOT; //选中tab
        if (savedInstanceState == null) {
            mFragments.put(PAGE_HOT, new HotFragment());
            mFragments.put(PAGE_CATEGORY, new CategoryFragment());
        } else {
            saveTab = savedInstanceState.getInt("current", PAGE_HOT);
            //从保存的状态中恢复,避免重复创建
            if (mFragmentManager.getFragment(savedInstanceState, "HOT") != null) {
                mFragments.put(PAGE_HOT, mFragmentManager.getFragment(savedInstanceState, "HOT"));
            } else {
                mFragments.put(PAGE_HOT, new HotFragment());
            }
            if (mFragmentManager.getFragment(savedInstanceState, "CATEGORY") != null) {
                mFragments.put(PAGE_CATEGORY, mFragmentManager.getFragment(savedInstanceState, "CATEGORY"));
            } else {
                mFragments.put(PAGE_CATEGORY, new CategoryFragment());
            }
        }
        showFragmentOnCreate(saveTab);

        mLoadingDialog = new LoadingDialog(this);
    }

    private void initData() {
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
        manager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        }, new IntentFilter(BroadcastValue.LOGIN_COMPLETE));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    /**
     * onCreate时根据上次保存的tab来显示Fragment
     */
    private void showFragmentOnCreate(int page) {
        if (page == PAGE_HOT) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.hide(mFragments.get(PAGE_CATEGORY));
            //选中hot
            if (!mFragments.get(PAGE_HOT).isAdded()) {
                ft.add(mContainer, mFragments.get(PAGE_HOT));
            }
            ft.commit();
            mToolbar.setTitle(R.string.hot);
            currentTab = PAGE_HOT;
        } else {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.hide(mFragments.get(PAGE_HOT));
            //选中category
            if (!mFragments.get(PAGE_CATEGORY).isAdded()) {
                ft.add(mContainer, mFragments.get(PAGE_CATEGORY));
            }
            ft.commit();
            mToolbar.setTitle(R.string.category);
            currentTab = PAGE_CATEGORY;
        }
    }

    private void logInSuccess() {
        if (mNavigationView.getMenu() != null) {
            mNavigationView.getMenu().clear();
        }
        mNavigationView.inflateMenu(R.menu.navi_menu_logout);
        AVUser user = AVUser.getCurrentUser();
        if (user.get(User.AVATOR_URL) != null && user.get(User.AVATOR_URL) instanceof String) {
            mAvatar.setImageURI(Uri.parse((String) user.get(User.AVATOR_URL)));
        }
        if (user.get(User.NICK_NAME) != null && user.get(User.NICK_NAME) instanceof CharSequence) {
            mUserName.setText((CharSequence) user.get(User.NICK_NAME));
        }
    }

    private void logOutSuccess() {
        if (mNavigationView.getMenu() != null) {
            mNavigationView.getMenu().clear();
        }
        mNavigationView.inflateMenu(R.menu.navi_menu_login);
        mAvatar.setImageURI(Uri.EMPTY);
        mUserName.setText("");
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
        switch (currentTab) {
            case PAGE_HOT:
                mToolbar.setTitle(R.string.hot);
                break;
            case PAGE_CATEGORY:
                mToolbar.setTitle(R.string.category);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
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
            case R.id.my_star:
                if (AVUser.getCurrentUser() == null) {
                    WxUtils.loginIn(this);
                    return true;
                }
                startActivity(new Intent(this, MyFavouritesActivity.class));
                return true;
            case R.id.clear_cache:
                Fresco.getImagePipeline().clearCaches();
                ToastUtil.getInstance().showToast(R.string.clear_cache_success);
                return true;
            case R.id.send_feed_back:
                startActivity(new Intent(this, FeedBackActivity.class));
                return true;
            case R.id.support:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PackageManager manager = getPackageManager();
                if (intent.resolveActivity(manager) != null) {
                    startActivity(intent);
                } else {
                    ToastUtil.getInstance().showToast(R.string.no_store);
                }
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存Fragment
        Fragment hot = mFragments.get(PAGE_HOT);
        if (hot != null && hot.isAdded()) {
            mFragmentManager.putFragment(outState, "HOT", hot);
        }
        Fragment category = mFragments.get(PAGE_CATEGORY);
        if (category != null && category.isAdded()) {
            mFragmentManager.putFragment(outState, "CATEGORY", category);
        }
        outState.putInt("current", currentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            ToastUtil.getInstance().showToast(R.string.press_to_exit);
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

}
