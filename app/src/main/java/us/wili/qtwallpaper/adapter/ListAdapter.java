package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import us.wili.qtwallpaper.R;
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

    public ListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        int height = (MobileConfig.screenWidth / 640) * 380;
        options = new ResizeOptions(MobileConfig.screenWidth, height);
    }

    public void update(List<CategoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
        CategoryItem item = items.get(position);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.coverUrl))
                .setResizeOptions(options).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request)
                .setOldController(holder.mSimpleDraweeView.getController()).build();
        holder.mSimpleDraweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView;
        }
    }
}
