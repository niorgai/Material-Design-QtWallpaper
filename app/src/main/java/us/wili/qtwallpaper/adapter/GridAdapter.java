package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.utils.PictureUtils;

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

    public GridAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;

        public GridViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            mSimpleDraweeView.setAspectRatio(0.56f);
            //set random loading color
            DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(mSimpleDraweeView.getController()).build();
            GenericDraweeHierarchy draweeHierarchy = mSimpleDraweeView.getHierarchy();
            if (draweeHierarchy == null) {
                draweeHierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources()).setFadeDuration(400).build();
            }
            controller.setHierarchy(draweeHierarchy);
            mSimpleDraweeView.setController(controller);
        }
    }
}
