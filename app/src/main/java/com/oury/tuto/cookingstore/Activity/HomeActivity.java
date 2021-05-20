package com.oury.tuto.cookingstore.Activity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.room.CookingRepository;
import com.oury.tuto.cookingstore.room.CookingRoomEvent;
import com.oury.tuto.cookingstore.ui.CookingListAdapter;
import com.oury.tuto.cookingstore.ui.ListViewEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private CookingListAdapter cookingListAdapter;
    private RecyclerView recyclerView;
    private CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {

        }

        @Override
        public void onUpdateCallback() {

        }
    };
    private ListViewEvent listViewEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {
            int pos = recyclerView.getChildAdapterPosition(v);
            int uid = cookingListAdapter.getCurrentList().get(pos).getUid();
            Intent intent = new Intent(HomeActivity.this, DisplayCooking.class);
            intent.putExtra(DisplayCooking.EXTRA_UID_VALUE, uid);
            startActivity(intent);
        }

        @Override
        public void onListViewLongClickCallback(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.home_recyclerView);
        cookingListAdapter = new CookingListAdapter(new CookingListAdapter.listDiff(), listViewEvent);
        recyclerView.setAdapter(cookingListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FloatingActionButton floatingActionButtonNew = findViewById(R.id.home_fabNew);
        floatingActionButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewCooking.class));
            }
        });

        FloatingActionButton floatingActionButtonSearch = findViewById(R.id.home_fabSearch);
        floatingActionButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        CookingRepository cookingRepository = new CookingRepository(getApplication(), cookingRoomEvent);
        cookingRepository.getAll().observe(this, new Observer<List<Cooking>>() {
            @Override
            public void onChanged(List<Cooking> cookings) {
                cookingListAdapter.submitList(cookings);
            }
        });
    }
}