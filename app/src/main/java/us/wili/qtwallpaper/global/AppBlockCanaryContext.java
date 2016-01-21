package us.wili.qtwallpaper.global;

import com.github.moduth.blockcanary.BlockCanaryContext;

import java.io.File;

import us.wili.qtwallpaper.BuildConfig;

/**
 * AppBlockCanary的Context
 * Created by qiu on 1/21/16.
 */
public class AppBlockCanaryContext extends BlockCanaryContext {

    @Override
    public String getQualifier() {
        return null;
    }

    @Override
    public String getUid() {
        return null;
    }

    @Override
    public String getNetworkType() {
        return null;
    }

    @Override
    public int getConfigDuration() {
        return 0;
    }

    @Override
    public int getConfigBlockThreshold() {
        return 500;
    }

    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getLogPath() {
        return "/blockcanary/performance";
    }

    @Override
    public boolean zipLogFile(File[] files, File file) {
        return false;
    }

    @Override
    public void uploadLogFile(File file) {

    }
}
