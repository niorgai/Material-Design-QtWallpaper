package us.wili.qtwallpaper.connect;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * AVOS callback
 * Created by qiu on 1/9/16.
 */
public class AVCallback<T extends AVObject> extends FindCallback<T> {

    @Override
    public void done(List<T> list, AVException e) {
        if (e == null) {
            onSuccess(list);
        } else {
            onFailure(e);
        }
        onFinish();
    }

    /**
     * success callback
     */
    protected void onSuccess(List<T> list) {

    }

    /**
     * failure callback
     */
    protected void onFailure(AVException e) {

    }

    /**
     * final callback
     */
    protected void onFinish() {

    }
}
