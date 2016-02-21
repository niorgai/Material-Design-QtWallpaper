package us.wili.qtwallpaper.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.Map;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.connect.BroadcastValue;
import us.wili.qtwallpaper.model.WallpaperItem;

/**
 * 微信工具类
 * Created by qiu on 2/2/16.
 */
public class WxUtils {

    public static final String APP_ID = "wxf9add774442056f5";
    public static final String APP_SECRET = "278664f453ed3a673324c568313f8a57";

    public static void loginIn(final Activity activity) {
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(activity);
        final Intent completeIntent = new Intent();
        completeIntent.setAction(BroadcastValue.LOGIN_COMPLETE);
        if (activity == null || activity.isFinishing()) {
            manager.sendBroadcast(completeIntent);
            return;
        }
        final UMShareAPI shareAPI = UMShareAPI.get(activity);
        if (!shareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            ToastUtil.getInstance().showToast(R.string.wx_not_install);
            manager.sendBroadcast(completeIntent);
            return;
        }
        shareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //openid, access_token, expires_in
                AVUser.AVThirdPartyUserAuth userAuth = new AVUser.AVThirdPartyUserAuth(map.get("access_token"),
                        map.get("expires_in"), AVUser.AVThirdPartyUserAuth.SNS_TENCENT_WEIXIN, map.get("openid"));
                AVUser.loginWithAuthData(userAuth, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            shareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                                @Override
                                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> infoMap) {
                                    AVUser user = AVUser.getCurrentUser();
                                    if (user != null) {
                                        if (!TextUtils.isEmpty(infoMap.get("nickname"))) {
                                            user.put("nickname", infoMap.get("nickname"));
                                        }
                                        String sex = infoMap.get("sex");
                                        if (!TextUtils.isEmpty(sex)) {
                                            //在不为null的情况下,0为微信返回的男性
                                            user.put("sex", sex.equals("0") ? 1 : 2);
                                        } else {
                                            user.put("sex", "0");
                                        }
                                        if (!TextUtils.isEmpty(infoMap.get("headimgurl"))) {
                                            user.put("avatorUrl", infoMap.get("headimgurl"));
                                        }
                                        if (user.get("favourites") == null) {
                                            user.put("favourites", new ArrayList<>());
                                        }
                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                manager.sendBroadcast(completeIntent);
                                                if (e == null) {
                                                    ToastUtil.getInstance().showToast(R.string.wx_login_success);
                                                    //发送广播
                                                    Intent intent = new Intent();
                                                    intent.setAction(BroadcastValue.LOGIN);
                                                    manager.sendBroadcast(intent);
                                                } else {
                                                    ToastUtil.getInstance().showToast(R.string.wx_login_fail);
                                                }
                                            }
                                        });
                                    } else {
                                        manager.sendBroadcast(completeIntent);
                                        ToastUtil.getInstance().showToast(R.string.wx_login_fail);
                                    }
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                                    ToastUtil.getInstance().showToast(R.string.wx_login_fail);
                                    manager.sendBroadcast(completeIntent);
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media, int i) {
                                    manager.sendBroadcast(completeIntent);
                                }
                            });
                        } else {
                            ToastUtil.getInstance().showToast(R.string.wx_login_fail);
                            manager.sendBroadcast(completeIntent);
                        }
                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtil.getInstance().showToast(R.string.wx_login_fail);
                manager.sendBroadcast(completeIntent);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                manager.sendBroadcast(completeIntent);
            }
        });
    }

    //退出登录
    public static void logOut(Activity activity) {
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        shareAPI.deleteOauth(activity, SHARE_MEDIA.WEIXIN, null);
        AVUser.logOut();
        ToastUtil.getInstance().showToast(R.string.wx_log_out_success);
        //发送广播
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(activity);
        Intent intent = new Intent();
        intent.setAction(BroadcastValue.LOGOUT);
        manager.sendBroadcast(intent);
    }

    //分享给微信好友
    public static void shareToWxSession(Activity activity, WallpaperItem item) {
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(new UMImage(activity, item.imageUrl))
                //TODO::为什么不设置text不能分享
                .withText("1")
                .share();
    }

    //分享给微信朋友圈
    public static void shareToWxMoment(Activity activity, WallpaperItem item) {
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(new UMImage(activity, item.imageUrl))
                        //TODO::为什么不设置text不能分享
                .withTitle(activity.getString(R.string.app_name))
                .withText("1")
                .share();
    }

}
