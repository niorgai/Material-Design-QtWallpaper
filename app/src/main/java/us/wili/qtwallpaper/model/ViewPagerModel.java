package us.wili.qtwallpaper.model;

/**
 * Use for ViewPager
 * Created by qiu on 1/11/16.
 */
public class ViewPagerModel {

    public ViewPagerModel(String url, String id) {
        this.imageUrl = url;
        this.objectId = id;
    }

    public String imageUrl;

    public String objectId; //use for click
}
