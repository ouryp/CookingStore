package com.oury.tuto.cookingstore.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.data.CookingType;
import com.oury.tuto.cookingstore.room.CookingRepository;
import com.oury.tuto.cookingstore.room.CookingRoomEvent;
import com.oury.tuto.cookingstore.ui.CookingListAdapter;
import com.oury.tuto.cookingstore.ui.DialogInputTag;
import com.oury.tuto.cookingstore.ui.DialogInputType;
import com.oury.tuto.cookingstore.ui.DialogListener;
import com.oury.tuto.cookingstore.ui.ListViewEvent;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements DialogListener {

    private final static String DIALOG_TAG_TYPE = "DIALOG_TAG_TYPE";
    private final static String DIALOG_TAG_TAGS = "DIALOG_TAG_TAGS";
    private TextInputLayout textName;
    private Button buttonType;
    private TextView textType;
    private Button buttonTags;
    private TextView textTags;
    private RecyclerView rvCooking;
    private CookingListAdapter cookingListAdapter;

    private CookingRepository repository;

    private DialogInputType dialogInputType = new DialogInputType(CookingType.ALL);
    private DialogInputTag dialogInputTag = new DialogInputTag(new ArrayList<>());

    private ListViewEvent cookingListViewEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {
            int pos = rvCooking.getChildAdapterPosition(v);
            int uid = cookingListAdapter.getCurrentList().get(pos).getUid();
            Intent intent = new Intent(SearchActivity.this, DisplayCooking.class);
            intent.putExtra(DisplayCooking.EXTRA_UID_VALUE, uid);
            startActivity(intent);
        }

        @Override
        public void onListViewLongClickCallback(View v) {

        }
    };

    private CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {

        }

        @Override
        public void onUpdateCallback() {

        }
    };

    private Observer<List<Integer>> integerObserver = new Observer<List<Integer>>() {
        @Override
        public void onChanged(List<Integer> integers) {
            repository.getAllById(integers).observe(SearchActivity.this, cookingObserver);
        }
    };

    private Observer<List<Cooking>> cookingObserver = new Observer<List<Cooking>>() {
        @Override
        public void onChanged(List<Cooking> cookings) {
            cookingListAdapter.submitList(cookings);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textName = findViewById(R.id.activity_search_textName);
        buttonTags = findViewById(R.id.activity_search_buttonTags);
        textTags = findViewById(R.id.activity_search_textTags);
        buttonType = findViewById(R.id.activity_search_buttonType);
        textType = findViewById(R.id.activity_search_textType);
        rvCooking = findViewById(R.id.activity_search_rvCooking);

        cookingListAdapter = new CookingListAdapter(new CookingListAdapter.listDiff(), cookingListViewEvent);

        rvCooking.setLayoutManager(new LinearLayoutManager(this));
        rvCooking.setAdapter(cookingListAdapter);
        rvCooking.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        textName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repository.search(s.toString(), dialogInputType.cookingType.text, dialogInputTag.cookingTags).observe(SearchActivity.this, integerObserver);
            }
        });

        buttonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInputType.show(getSupportFragmentManager(), DIALOG_TAG_TYPE);
            }
        });

        buttonTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInputTag.show(getSupportFragmentManager(), DIALOG_TAG_TAGS);
            }
        });

        repository = new CookingRepository(getApplication(), cookingRoomEvent);
    }

    @Override
    public void onDialogPositiveClick(String tag) {
        switch(tag) {
            case DIALOG_TAG_TYPE :
                textType.setText(dialogInputType.cookingType.text);
                repository.search(textName.getEditText().getText().toString(), dialogInputType.cookingType.text, dialogInputTag.cookingTags).observe(SearchActivity.this, integerObserver);
                break;
            case DIALOG_TAG_TAGS :
                textTags.setText(CookingTag.printTags(dialogInputTag.cookingTags));
                repository.search(textName.getEditText().getText().toString(), dialogInputType.cookingType.text, dialogInputTag.cookingTags).observe(SearchActivity.this, integerObserver);
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(String tag) {

    }
}
