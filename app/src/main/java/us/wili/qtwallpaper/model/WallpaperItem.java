package us.wili.qtwallpaper.model;

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
    public static final Parcelable.Creator CREATOR = AVObject.AVObjectCreator.instance;

    public WallpaperItem() {

    }

    public String imageUrl;

    public String name;

    public CategoryItem categoryId;

    public int order;

    public int downloads;
}
