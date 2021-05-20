package com.oury.tuto.cookingstore.data;

import android.media.MediaRouter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "cooking_table")
public class Cooking {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "protocol")
    private List<String> protocol;

    @ColumnInfo(name = "ingredient")
    private List<CookingIngredient> ingredient;

    @ColumnInfo(name = "type")
    private CookingType type;

    @ColumnInfo(name = "tags")
    private List<CookingTag> tags;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "image")
    private CookingImage image;

    @ColumnInfo(name = "time")
    private CookingDuration time;

    public Cooking() {
        protocol = new ArrayList<>();
        ingredient = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProtocol() {
        return protocol;
    }
    public void setProtocol(List<String> protocol) {
        this.protocol.clear();
        this.protocol.addAll(protocol);
    }
    public void addProtocol(String protocol) {
        this.protocol.add(protocol);
    }
    public void removeProtocol(int pos) {
        this.protocol.remove(pos);
    }
    public void editProtocol(int pos, String txt) {
        this.protocol.set(pos, txt);
    }

    public List<CookingIngredient> getIngredient() {
        return ingredient;
    }
    public void setIngredient(List<CookingIngredient> ingredient) {
        this.ingredient.clear();
        this.ingredient.addAll(ingredient);
    }
    public void addIngredient(CookingIngredient ingredient) {
        this.ingredient.add(ingredient);
    }
    public void removeIngredient(int pos) {
        this.ingredient.remove(pos);
    }

    public CookingType getType() {
        return type;
    }
    public void setType(CookingType type) {
        this.type = type;
    }

    public List<CookingTag> getTags() {
        return tags;
    }
    public void setTags(List<CookingTag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public CookingImage getImage() {
        return image;
    }
    public void setImage(CookingImage image) {
        this.image = image;
    }

    public CookingDuration getTime() {
        return time;
    }
    public void setTime(CookingDuration time) {
        this.time = time;
    }

    private static String firstCharToUpper(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String formatName(String name) {
        return firstCharToUpper(name);
    }

    public static String formatText(String text) {
        return firstCharToUpper(text);
    }
}
