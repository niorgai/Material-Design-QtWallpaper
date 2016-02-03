package us.wili.qtwallpaper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.activity.WallPaperDisplayActivity;
import us.wili.qtwallpaper.global.MobileConfig;
import us.wili.qtwallpaper.model.CategoryItem;
import us.wili.qtwallpaper.model.ViewPagerModel;
import us.wili.qtwallpaper.model.WallpaperItem;
import us.wili.qtwallpaper.utils.PictureUtils;
import us.wili.qtwallpaper.utils.UIUtils;
import us.wili.qtwallpaper.widget.UnlimitedViewPager;

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
    private ArrayList<WallpaperItem> mWallPaper;

    private ResizeOptions mGridResizeOption;

    //for animation
    private int lastAnimatedPosition = -1;
    private boolean isFirstPageLoadFinish = false;

    public HotAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        int width = (MobileConfig.screenWidth - 3 * UIUtils.dip2px(mContext, 10)) / 2;
        int height = (int) (width * 0.56f);
        mGridResizeOption = new ResizeOptions(width, height);
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

    public void setWallPaper(ArrayList<WallpaperItem> mWallPaper) {
        this.mWallPaper = mWallPaper;
        lastAnimatedPosition = -1;
        isFirstPageLoadFinish = false;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager != null && manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            final GridLayoutManager.SpanSizeLookup originLookup = gridLayoutManager.getSpanSizeLookup();
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
        runEnterAnimation(holder.itemView, position);
        if (holder instanceof BannerViewHolder) {
            bindBannerViewHolder((BannerViewHolder) holder);
        } else if (holder instanceof GridViewHolder) {
            GridViewHolder viewHolder = (GridViewHolder) holder;
            viewHolder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
            int offset = isHaveBannerItem() ? 1 : 0;
            WallpaperItem item = mWallPaper.get(position - offset);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.imageUrl + PictureUtils.COMPRESS_20))
                    .setResizeOptions(mGridResizeOption).build();
            DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request)
                    .setOldController(viewHolder.mSimpleDraweeView.getController()).build();
            viewHolder.mSimpleDraweeView.setController(controller);
        }
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
        int offset = isHaveBannerItem() ? 1 : 0;
        int itemSize = (mWallPaper == null || mWallPaper.isEmpty()) ? 0 : mWallPaper.size();
        return offset + itemSize;
    }

    private void bindBannerViewHolder(BannerViewHolder holder) {
        holder.mViewPager.stopAutoScroll();
        holder.mDotsLayout.removeAllViewsInLayout();
        holder.mDots = new ImageView[mBanners.size()];
        holder.preIndex = 0;

        List<ViewPagerModel> models = new ArrayList<>();
        for (int i = 0; i < mBanners.size(); i++) {
            CategoryItem item = mBanners.get(i);
            models.add(new ViewPagerModel(item.coverUrl, item.name, item.getObjectId()));

            if (mBanners.size() > 1) {
                ImageView imageView = new ImageView(mContext);
                if (i == 0) {
                    imageView.setImageResource(R.drawable.dot_focused_grey);
                } else {
                    imageView.setImageResource(R.drawable.dot_normal_white);
                }
                imageView.setLayoutParams(holder.mDotLayoutParams);
                holder.mDots[i] = imageView;
                holder.mDotsLayout.addView(imageView);
            }
        }
        holder.mAdapter.setData(models);
        holder.mViewPager.setAdapterDataSize(models.size());
        int mid = UnlimitedBannerAdapter.MAX_COUNT / 2 - ((UnlimitedBannerAdapter.MAX_COUNT / 2) % models.size());
        holder.mViewPager.setCurrentItem(mid, false);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener {
        UnlimitedViewPager mViewPager;
        UnlimitedBannerAdapter mAdapter;
        LinearLayout mDotsLayout;
        ImageView[] mDots;
        LinearLayout.LayoutParams mDotLayoutParams;
        int preIndex = 0;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mViewPager = (UnlimitedViewPager) itemView.findViewById(R.id.view_pager);
            mDotsLayout = (LinearLayout) itemView.findViewById(R.id.dots_layout);

            mViewPager.addOnPageChangeListener(this);
            int params = UIUtils.dip2px(mContext, 6);
            mDotLayoutParams = new LinearLayout.LayoutParams(params,params);
            mDotLayoutParams.setMargins(params / 2, 0, params / 2, 0);
            mAdapter = new UnlimitedBannerAdapter(mContext);
            mViewPager.setAdapter(mAdapter);

            int height = (int) ((MobileConfig.screenWidth / 640f) * 380f);
            mViewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            mAdapter.setBannerSize(MobileConfig.screenWidth, height);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mDots[preIndex].setImageResource(R.drawable.dot_normal_white);
            mDots[mViewPager.getCurrentPos()].setImageResource(R.drawable.dot_focused_grey);
            preIndex = mViewPager.getCurrentPos();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mSimpleDraweeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                int pos = isHaveBannerItem() ? getAdapterPosition() - 1 : getAdapterPosition();
                Intent intent = WallPaperDisplayActivity.getIntent(mContext, mWallPaper, pos);
                mContext.startActivity(intent);
            }
        }
    }
}
