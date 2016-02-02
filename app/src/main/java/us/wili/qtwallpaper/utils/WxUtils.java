package us.wili.qtwallpaper.utils;

import android.app.Activity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import us.wili.qtwallpaper.R;

/**
 * 微信工具类
 * Created by qiu on 2/2/16.
 */
public class WxUtils {

    public static final String APP_ID = "wxf9add774442056f5";
    public static final String APP_SECRET = "278664f453ed3a673324c568313f8a57";

    public static void loginIn(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        if (!shareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            ToastUtil.getInstance().showToast(R.string.wx_not_install);
            return;
        }
        shareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                ToastUtil.getInstance().showToast(R.string.wx_login_success);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtil.getInstance().showToast(R.string.wx_login_fail);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

}
