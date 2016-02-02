package us.wili.qtwallpaper.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import us.wili.qtwallpaper.R;

/**
 * 关于界面
 * Created by qiu on 2/2/16.
 */
public class About extends BaseActivity {

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.activity_about);
        setTitle(R.string.about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.protocol) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("http://7xnzcf.com1.z0.glb.clouddn.com/sunnywallpaper_privacy.html"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
