package us.wili.qtwallpaper.model;

/**
 * Use for ViewPager
 * Created by qiu on 1/11/16.
 */
public class ViewPagerModel {

    public ViewPagerModel(String url, String name, String id) {
        this.imageUrl = url;
        this.name = name;
        this.objectId = id;
    }

    public String imageUrl;

    public String name;

    public String objectId;
}
