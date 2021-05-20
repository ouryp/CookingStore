package com.oury.tuto.cookingstore.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookingIngredient {

    private String ingredient;
    private int quantity;
    private CookingUnit unit;
    private static final String DELIMITER = ",";

    public CookingIngredient(String ingredient, int quantity, CookingUnit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public CookingIngredient(String room) {
        List<String> list = Arrays.asList(room.split(DELIMITER));
        ingredient = list.get(0);
        quantity = Integer.parseInt(list.get(1));
        unit = CookingUnit.valueOf(list.get(2));
    }

    public String toRoom() {
        List<String> list = new ArrayList<>();
        list.add(ingredient);
        list.add(String.valueOf(quantity));
        list.add(unit.name());
        return String.join(DELIMITER, list);
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity + " " + unit.text;
    }
}
