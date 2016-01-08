package us.wili.qtwallpaper.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.utils.PictureUtils;

/**
 * ListAdapter item is 640 * 380
 * Created by qiu on 1/8/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    public ListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(new ColorDrawable(PictureUtils.getRandomColor(mContext)));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView;
            mSimpleDraweeView.setAspectRatio(1.684210526f);
            //set random loading color
            GenericDraweeHierarchy draweeHierarchy = mSimpleDraweeView.getHierarchy();
            if (draweeHierarchy == null) {
                draweeHierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources()).build();
            }
            draweeHierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        }
    }
}
