package com.oury.tuto.cookingstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.room.CookingRepository;
import com.oury.tuto.cookingstore.room.CookingRoomEvent;

public class MainActivity extends AppCompatActivity {

    private CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {

        }

        @Override
        public void onUpdateCallback() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        CookingRepository repository = new CookingRepository(getApplication(), cookingRoomEvent);
        repository.getRowNbr().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Intent intent;
                if(integer > 0) {
                    intent = new Intent(MainActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, NewCooking.class);
                }
                startActivity(intent);
            }
        });
        super.onResume();
    }
}