package com.oury.tuto.cookingstore.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.data.CookingType;

import java.util.List;

@Dao
public interface CookingDao {

    @Query("SELECT COUNT(name) FROM cooking_table")
    LiveData<Integer> getRowNbr();

    @Query("SELECT * FROM cooking_table WHERE uid IN (:ids)")
    LiveData<List<Cooking>> getAllById(List<Integer> ids);

    @Query("SELECT * FROM cooking_table")
    LiveData<List<Cooking>> getAll();

    @Query("SELECT * FROM cooking_table WHERE uid LIKE:id")
    LiveData<Cooking> get(int id);

    @Query("SELECT * FROM cooking_table WHERE name LIKE:name")
    LiveData<Cooking> get(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Cooking cooking);

    @Delete
    void delete(Cooking cooking);

    @Update
    void update(Cooking cooking);
}
