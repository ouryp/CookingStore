package com.oury.tuto.cookingstore.room;

import androidx.room.TypeConverter;

import com.oury.tuto.cookingstore.data.CookingImage;
import com.oury.tuto.cookingstore.data.CookingIngredient;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.data.CookingDuration;
import com.oury.tuto.cookingstore.data.CookingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookingConverter {

    private static final String DELIMITER = "\n";


    /* COOKING OBJECTS -> ROOM */

    @TypeConverter
    public static String toRoom(List<String> list) {
        return String.join(DELIMITER, list);
    }

    @TypeConverter
    public static String ingredientToRoom(List<CookingIngredient> list) {
        ArrayList<String> listIngredientString = new ArrayList<>();
        for(CookingIngredient ci : list) {
            listIngredientString.add(ci.toRoom());
        }
        return toRoom(listIngredientString);
    }

    @TypeConverter
    public static String toRoom(CookingType type) {
        return type.name();
    }

    @TypeConverter
    public static String tagToRoom(List<CookingTag> tags) {
        ArrayList<String> listTagsString = new ArrayList<>();
        for(CookingTag ct : tags) {
            listTagsString.add(ct.name());
        }
        return toRoom(listTagsString);
    }

    @TypeConverter
    public static String toRoom(CookingImage image) {
        return image.toRoom();
    }

    @TypeConverter
    public static String toRoom(CookingDuration time) {
        return toRoom(time.toRoom());
    }


    /* ROOM -> COOKING OBJECTS */

    @TypeConverter
    public static List<String> toListString(String strRoom) {
        return Arrays.asList(strRoom.split(DELIMITER));
    }

    @TypeConverter
    public static List<CookingIngredient> toListIngredient(String strRoom) {
        List<String> list = toListString(strRoom);
        List<CookingIngredient> ingredientList = new ArrayList<>();
        for(String s : list) {
            ingredientList.add(new CookingIngredient(s));
        }
        return ingredientList;
    }

    @TypeConverter
    public static CookingType toType(String strRoom) {
        return CookingType.valueOf(strRoom);
    }

    @TypeConverter
    public static List<CookingTag> toTag(String strRoom) {
        List<String> list = toListString(strRoom);
        List<CookingTag> cookingTags = new ArrayList<>();
        for(String s : list) {
            cookingTags.add(CookingTag.valueOf(s));
        }
        return cookingTags;
    }

    @TypeConverter
    public static CookingImage toImage(String strRoom) {
        return CookingImage.load(strRoom);
    }

    @TypeConverter
    public static CookingDuration toTime(String strRoom) {
        List<String> list = toListString(strRoom);
        return new CookingDuration(list);
    }
}
