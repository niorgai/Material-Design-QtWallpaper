package us.wili.qtwallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import us.wili.qtwallpaper.R;

/**
 * Loading Dialog
 * Created by qiu on 2/3/16.
 */
public class LoadingDialog extends Dialog {

    private ImageView mImageView;

    private RotateAnimation rotateAnimation;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialog);
        setContentView(R.layout.dialog_loading);
        mImageView = (ImageView) findViewById(R.id.loading_img);

        rotateAnimation = new RotateAnimation(0, 1080, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);
    }

    @Override
    public void show() {
        mImageView.startAnimation(rotateAnimation);
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        rotateAnimation.cancel();
    }
}
