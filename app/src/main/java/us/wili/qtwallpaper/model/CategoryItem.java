package us.wili.qtwallpaper.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Category model
 * objectId | String | 主键
 * name | String | 分类名字
 * coverUrl | String | 封面 url
 * order | Int | 顺序
 * isHot | Bool | 是否热门
 * Created by qiu on 1/9/16.
 */
@AVClassName("Category")
public class CategoryItem extends AVObject {

    public static final String IS_HOT = "isHot";

    public static final Creator CREATOR = AVObjectCreator.instance;

    public CategoryItem() {

    }

    public String name;

    public String coverUrl;

    public int order;

    public boolean isHot;
}
