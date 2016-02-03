package us.wili.qtwallpaper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Wallpaper in AVOS
 *
 * objectId | String | 主键
 * imageUrl | String | 壁纸 url
 * name | String | 壁纸名字
 * categoryId | Pointer | 分类指针
 * order | Int | 顺序
 * downloads | Int | 下载次数，计数器，默认为 0
 * Created by qiu on 1/9/16.
 */
@AVClassName("Wallpaper")
public class WallpaperItem extends AVObject {

    public static final String DOWNLOADS = "downloads";
    public static final String CATEGORY_ID = "categoryId";

    public static final Parcelable.Creator<WallpaperItem> CREATOR = new Parcelable.ClassLoaderCreator<WallpaperItem>() {

        @Override
        public WallpaperItem createFromParcel(Parcel source, ClassLoader loader) {
            return new WallpaperItem(source);
        }

        public WallpaperItem createFromParcel(Parcel in) {
            return new WallpaperItem(in);
        }

        @Override
        public WallpaperItem[] newArray(int size) {
            return new WallpaperItem[size];
        }
    };

    public WallpaperItem() {

    }

    public WallpaperItem(Parcel in){
        super(in);
        imageUrl = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);
        out.writeString(imageUrl);
        out.writeString(name);
    }

    public String imageUrl;

    public String name;

    public CategoryItem categoryId;

    public int order;

    public int downloads;
}
