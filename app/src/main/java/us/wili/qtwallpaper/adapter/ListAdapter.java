package us.wili.qtwallpaper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.activity.CategoryDetailActivity;
import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.utils.PictureUtils;

/**
 * ListAdapter item is 640 * 380
 * Created by qiu on 1/8/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ResizeOptions options;

    private List<CategoryItem> items;

    //for animation
    private int lastAnimatedPosition = -1;
    private boolean isFirstPageLoadFinish = false;

    public ListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        int height = (MobileConfig.screenWidth / 640) * 380;
        options = new ResizeOptions(MobileConfig.screenWidth, height);
    }

    public void update(List<CategoryItem> items) {
        this.items = items;
        lastAnimatedPosition = -1;
        isFirstPageLoadFinish = false;
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
        CategoryItem item = items.get(position);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.coverUrl))
                .setResizeOptions(options).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request)
                .setOldController(holder.mSimpleDraweeView.getController()).build();
        holder.mSimpleDraweeView.setController(controller);
    }

    private void runEnterAnimation(View view, int position) {
        if (isFirstPageLoadFinish) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(600);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(20 * (position))
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isFirstPageLoadFinish = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView mSimpleDraweeView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView;
            mSimpleDraweeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) {
                return;
            }
            CategoryItem item = items.get(pos);
            Intent intent = CategoryDetailActivity.getIntent(mContext, item.name, item.getObjectId());
            mContext.startActivity(intent);
        }
    }
}
