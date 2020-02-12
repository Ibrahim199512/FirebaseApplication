package test.firebase.application.Modules;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Category {
    private String categoryId;
    private String categoryTitle;
    private String categoryImageUrl;

    public Category() {
    }

    public Category(String categoryId, String categoryTitle, String categoryImageUrl) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryId", categoryId);
        result.put("categoryTitle", categoryTitle);
        result.put("categoryImageUrl", categoryImageUrl);
        return result;
    }

}