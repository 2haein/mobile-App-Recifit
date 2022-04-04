package com.recifit.application.ui.recipe;


import android.graphics.drawable.Drawable;
import android.net.Uri;

public class RecipeListView {
    private Drawable iconDrawable;
    private Drawable recipeDrawable;
    private String titleStr;
    private String contentStr;
    private String likeStr;
    private String dateStr;
    private String viewStr;
    private String idStr;
    private Uri imgUrl;

    public void setIcon(Drawable icon) {iconDrawable = icon;}
    public void setRecipe(Drawable recipe) {recipeDrawable = recipe;}
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setContent(String content) {
        contentStr = content;
    }
    public void setLike(String like) { likeStr = like; }
    public void setView(String view) {viewStr = view; }
    public void setDate(String date) {
        dateStr = date;
    }
    public void setId(String id) {idStr = id;}
    public void setImgUrl(String url) {imgUrl = Uri.parse(url);}



    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public Drawable getRecipe() {
        return this.recipeDrawable ;
    }
    public String getTitle() {
        return this.titleStr;
    }
    public String getContent() {
        return this.contentStr;
    }
    public String getLike() {
        return this.likeStr;
    }
    public String getDate() {
        return this.dateStr;
    }
    public String getId() {
        return this.idStr;
    }
    public String getView() {
        return viewStr;
    }
    public Uri getImgUrl() {return Uri.parse(String.valueOf(imgUrl));}
}