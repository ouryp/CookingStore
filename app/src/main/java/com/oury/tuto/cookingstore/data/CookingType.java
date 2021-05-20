package com.oury.tuto.cookingstore.data;

public enum CookingType {

    ALL(""),
    APERO("Apero"),
    ENTREE("Entree"),
    PLAT("Plat"),
    DESSERT("Dessert");

    public final String text;

    CookingType(String text) {
        this.text = text;
    }
}
