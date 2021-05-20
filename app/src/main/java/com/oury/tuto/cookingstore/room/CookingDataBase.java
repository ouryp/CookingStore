package com.oury.tuto.cookingstore.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.oury.tuto.cookingstore.data.Cooking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Cooking.class}, version = 1, exportSchema = false)
@TypeConverters({CookingConverter.class})
public abstract class CookingDataBase extends RoomDatabase {

    public abstract CookingDao cookingDao();

    private static volatile CookingDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CookingDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CookingDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CookingDataBase.class, "cooking_table").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
        }
    };

}
