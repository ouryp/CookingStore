package com.oury.tuto.cookingstore.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CookingTag {

    UNKNOW(""),
    ETE("ETE"),
    AUTOMNE("AUTOMNE"),
    HIVER("HIVER"),
    PRINTEMPS("PRINTEMPS"),
    HEALTHY("HEALTHY"),
    CHAUD("CHAUD"),
    FROID("FROID"),
    VIANDE("VIANDE"),
    POISSON("POISSON"),
    FRUIT_DE_MER("FRUIT DE MER"),
    ASIATIQUE("ASIATIQUE");

    public final String text;

    CookingTag(String text) {
        this.text = text;
    }

    public static String printTags(List<CookingTag> cookingTags) {
        List<String> stringList = new ArrayList<>();
        for(CookingTag ct : cookingTags) {
            stringList.add(ct.text);
        }
        return String.join(", ", stringList);
    }

    public static List<CookingTag> splitString(String tags) {
        List<String> split = Arrays.asList(tags.split(", "));
        List<CookingTag> result = new ArrayList<>();
        for(String s : split) {
            result.add(CookingTag.valueOf(s));
        }
        return result;
    }
}
