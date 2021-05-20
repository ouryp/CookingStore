package com.oury.tuto.cookingstore.data;

public enum CookingUnit {

    None(" "),
    L("L"),
    cL("cL"),
    mL("mL"),
    Kg("Kg"),
    g("g"),
    mg("mg"),
    cs("Cuillère à soupe"),
    cc("Cuillère à café"),
    pc("Pincée");

    public String text;

    CookingUnit(String text) {
        this.text = text;
    }
}
