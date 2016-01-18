package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.PictureUtils;
import us.wili.qtwallpaper.utils.UIUtils;

/**
 * Common Grid Adapter
 * item's mask is 280 * 500 in 2 cols.
 * Created by qiu on 1/3/16.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private LayoutInflater mInflater;
    private Context mContext;
    private ResizeOptions mGridResizeOption;

    private List<WallpaperItem> itemList;

    public GridAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        int width = (MobileConfig.screenWidth - 3 * UIUtils.dip2px(mContext, 10)) / 2;
        int height = (int) (width * 0.56f);
        mGridResizeOption = new ResizeOptions(width, height);
    }

    public void update(List<WallpaperItem> items) {
        itemList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_LEFT : TYPE_RIGHT;
    }

    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LEFT) {
            return new GridViewHolder(mInflater.inflate(R.layout.item_grid_left, parent, false));
        } else {
            return new GridViewHolder(mInflater.inflate(R.layout.item_grid_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(itemList.get(position).imageUrl))
                .setResizeOptions(mGridResizeOption).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request)
                .setOldController(holder.mSimpleDraweeView.getController()).build();
        holder.mSimpleDraweeView.setController(controller);

    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;

        public GridViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            mSimpleDraweeView.setAspectRatio(0.56f);
            //set random loading color
            GenericDraweeHierarchy draweeHierarchy = mSimpleDraweeView.getHierarchy();
            if (draweeHierarchy == null) {
                draweeHierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources()).build();
            }
            draweeHierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        }
    }
}
