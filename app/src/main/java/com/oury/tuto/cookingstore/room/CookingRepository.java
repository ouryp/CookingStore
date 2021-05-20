package com.oury.tuto.cookingstore.room;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingTag;

import java.util.ArrayList;
import java.util.List;

public class CookingRepository {

    private CookingDataBase db;
    private CookingDao cookingDao;
    private CookingRoomEvent event;

    public CookingRepository(Application application, CookingRoomEvent eventCallback) {
        db = CookingDataBase.getDatabase(application);
        cookingDao = db.cookingDao();
        event = eventCallback;
    }

    public LiveData<Integer> getRowNbr() {
        return cookingDao.getRowNbr();
    }

    public LiveData<List<Cooking>> getAll() {
        return cookingDao.getAll();
    }

    public LiveData<List<Cooking>> getAllById(List<Integer> ids) {
        return cookingDao.getAllById(ids);
    }

    public LiveData<List<Integer>> search(String name, String type, List<CookingTag> cookingTags) {
        MutableLiveData<List<Integer>> listMutableLiveData = new MutableLiveData<>();
        CookingDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String query = "SELECT * FROM cooking_table WHERE name LIKE? OR ingredient LIKE?"; // AND type LIKE?
                List<String> args = new ArrayList<>();
                args.add("%" + name + "%");
                args.add("%" + name + "%");
//                args.add("%" + type + "%");
//                for(CookingTag ct : cookingTags) {
//                    query = query.concat(" AND tags LIKE?");
//                    args.add( "%" + ct.text + "%");
//                }
                Cursor c = db.query(query, args.toArray());

                List<Integer> result = new ArrayList<>();
                if(c.moveToFirst()) {
                    do {
                        result.add(c.getInt(0));
                    } while (c.moveToNext());
                }

                listMutableLiveData.postValue(result);
            }
        });
        return listMutableLiveData;
    }

    public LiveData<Cooking> get(int id) {
        return cookingDao.get(id);
    }

    public LiveData<Cooking> get(String name) {
        return cookingDao.get(name);
    }

    public void insert(Cooking cooking) {
        CookingDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                event.onInsertCallback(cookingDao.insert(cooking));
            }
        });
    }

    public void delete(Cooking cooking) {
        CookingDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cookingDao.delete(cooking);
            }
        });
    }

    public void update(Cooking cooking) {
        CookingDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cookingDao.update(cooking);
                event.onUpdateCallback();
            }
        });
    }
}
