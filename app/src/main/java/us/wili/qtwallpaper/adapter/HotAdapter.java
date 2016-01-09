package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.PictureUtils;

/**
 * Common Grid Adapter
 * item's mask is 280 * 500 in 2 cols.
 * Created by qiu on 1/3/16.
 */
public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_BANNER = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;

    private LayoutInflater mInflater;
    private Context mContext;

    private List<CategoryItem> mBanners;
    private List<WallpaperItem> mWallPaper;

    public HotAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setBanners(List<CategoryItem> mBanners) {
        if (mBanners == null || mBanners.isEmpty()) {
            this.mBanners = mBanners;
            if (isHaveBannerItem()) {
                notifyItemRemoved(0);
            }
        } else {
            this.mBanners = mBanners;
            if (!isHaveBannerItem()) {
                notifyItemInserted(0);
            } else {
                notifyItemChanged(0);
            }
        }
    }

    /**
     * 是否存在第一位的banner
     * @return true为存在
     */
    private boolean isHaveBannerItem() {
        return !(mBanners == null || mBanners.isEmpty());
    }

    public void setWallPaper(List<WallpaperItem> mWallPaper) {
        this.mWallPaper = mWallPaper;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager != null && manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            final GridLayoutManager.SpanSizeLookup originLookup = ((GridLayoutManager) manager).getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0 && isHaveBannerItem()) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return originLookup.getSpanSize(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHaveBannerItem()) {
            if (position == 0) {
                return TYPE_BANNER;
            } else {
                return position % 2 == 1 ? TYPE_LEFT : TYPE_RIGHT;
            }
        }
        return position % 2 == 0 ? TYPE_LEFT : TYPE_RIGHT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new BannerViewHolder(mInflater.inflate(R.layout.view_hot_banner, parent, false));
            case TYPE_LEFT:
                return new GridViewHolder(mInflater.inflate(R.layout.item_grid_left, parent, false));
            default:
                return new GridViewHolder(mInflater.inflate(R.layout.item_grid_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {

        } else if (holder instanceof GridViewHolder) {
            GridViewHolder viewHolder = (GridViewHolder) holder;
            viewHolder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
            int offset = isHaveBannerItem() ? 1 : 0;
            WallpaperItem item = mWallPaper.get(position - offset);
            viewHolder.mSimpleDraweeView.setImageURI(Uri.parse(item.imageUrl));
        }
    }

    @Override
    public int getItemCount() {
        int offset = isHaveBannerItem() ? 1 : 0;
        int itemSize = (mWallPaper == null || mWallPaper.isEmpty()) ? 0 : mWallPaper.size();
        return offset + itemSize;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(View itemView) {
            super(itemView);
        }
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
