package com.oury.tuto.cookingstore.Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingDuration;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.room.CookingRepository;
import com.oury.tuto.cookingstore.room.CookingRoomEvent;
import com.oury.tuto.cookingstore.ui.IngredientListAdapter;
import com.oury.tuto.cookingstore.ui.ListViewEvent;
import com.oury.tuto.cookingstore.ui.ProtocolListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.Duration;

public class DisplayCooking extends AppCompatActivity {

    public static final String EXTRA_UID_VALUE = "com.oury.tuto.cookingstore.Activity.DisplayCooking.EXTRA_UID_VALUE";

    private CookingRepository cookingRepository;
    private ImageView image;
    private TextView name;
    private TextView type;
    private TextView tags;
    private TextView time;
    private TextView comment;
    private RecyclerView ingredients;
    private IngredientListAdapter ingredientListAdapter;
    private RecyclerView protocol;
    private ProtocolListAdapter protocolListAdapter;
    private NestedScrollView mainScrollView;
    private LinearLayout rvLinearLayout;
    private LinearLayout llIngredientsTimer;
    private ObjectAnimator animation;
    private TextView timerText;
    private Button button_timer1;
    private Button button_timer10;
    private Button button_timer60;
    private Button button_timerReset;
    private Button button_timerStart;
    private FloatingActionButton editButton;

    private Duration timerDuration = Duration.ZERO;

    private CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {

        }

        @Override
        public void onUpdateCallback() {

        }
    };

    private ListViewEvent ingredientListEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {

        }

        @Override
        public void onListViewLongClickCallback(View v) {

        }
    };

    private ListViewEvent protocolListEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {

        }

        @Override
        public void onListViewLongClickCallback(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_cooking_2);

        cookingRepository = new CookingRepository(getApplication(), cookingRoomEvent);

        mainScrollView = findViewById(R.id.displayScrollView);

        image = findViewById(R.id.cooking_image);
        name = findViewById(R.id.cooking_name);
        type = findViewById(R.id.cooking_type);
        tags = findViewById(R.id.cooking_tags);
        time = findViewById(R.id.cooking_time);
        comment = findViewById(R.id.cooking_comment);

        ingredients = findViewById(R.id.cooking_ingredients);
        ingredients.setLayoutManager(new LinearLayoutManager(this));
        ingredientListAdapter = new IngredientListAdapter(ingredientListEvent, IngredientListAdapter.PROPRIETY_BOX_SIZE_LOW);
        ingredients.setAdapter(ingredientListAdapter);

        protocol = findViewById(R.id.cooking_protocol);
        protocol.setLayoutManager(new LinearLayoutManager(this));
        protocolListAdapter = new ProtocolListAdapter(protocolListEvent);
        protocol.setAdapter(protocolListAdapter);
        protocol.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvLinearLayout = findViewById(R.id.rvLinearLayout);
        llIngredientsTimer = findViewById(R.id.cooking_llIngredientsTimer);
        timerText = findViewById(R.id.cooking_timer);
        button_timer1 = findViewById(R.id.button_timer_1);
        button_timer10 = findViewById(R.id.button_timer_10);
        button_timer60 = findViewById(R.id.button_timer_60);
        button_timerReset = findViewById(R.id.button_timer_reset);
        button_timerStart = findViewById(R.id.button_timer_start);

        editButton = findViewById(R.id.displayCooking_fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid = DisplayCooking.this.getIntent().getIntExtra(EXTRA_UID_VALUE, -1);
                if(uid >= 0) {
                    Intent intent = new Intent(DisplayCooking.this, EditActivity.class);
                    intent.putExtra(EditActivity.EXTRA_UID_VALUE, uid);
                    startActivity(intent);
                }
            }
        });

        mainScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] loc = new int[2];
                rvLinearLayout.getLocationOnScreen(loc);
                float posY = loc[1] - 24 * getResources().getDisplayMetrics().density; // add the the android status bar height
                if(posY < 0) {
                    if(animation != null) {
                        if(!animation.isRunning()) {
                            animation.setFloatValues((-1)*posY);
                            animation.start();
                        }
                    } else {
                        animation = ObjectAnimator.ofFloat(llIngredientsTimer, "y", (-1)*posY);
                        animation.setDuration(0);
                        animation.start();
                    }
                }
            }
        });

        button_timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementTimer(1);
            }
        });
        button_timer10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementTimer(10);
            }
        });
        button_timer60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementTimer(60);
            }
        });
        button_timerReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        button_timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long value = getTimerMinutes();
                if(value > 0) {
                    startTimer(value*60);
                }
                resetTimer();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int uid = getIntent().getIntExtra(EXTRA_UID_VALUE, -1);
        if(uid < 0) {
            finish();
        } else {
            cookingRepository.get(uid).observe(this, new Observer<Cooking>() {
                @Override
                public void onChanged(Cooking cooking) {
                    try {
                        image.setImageBitmap(cooking.getImage().createThumbnail(new Size(image.getMaxWidth(), image.getMaxHeight())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    name.setText(cooking.getName());
                    type.setText(cooking.getType().text);
                    tags.setText(CookingTag.printTags(cooking.getTags()));
                    time.setText(cooking.getTime().print());
                    comment.setText(cooking.getComment());
                    ingredientListAdapter.submitList(cooking.getIngredient());
                    protocolListAdapter.submitList(cooking.getProtocol());
                }
            });
        }
    }

    public void startTimer(long seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, name.getText().toString() + " timer")
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Le timer est démarré !", Toast.LENGTH_SHORT).show();
        }
    }

    private void incrementTimer(int incr) {
        timerDuration = timerDuration.plusMinutes(incr);
        timerText.setText(CookingDuration.inHoursMinutes(timerDuration));
    }

    private long getTimerMinutes() {
        return timerDuration.toMinutes();
    }

    private void resetTimer() {
        timerDuration = Duration.ZERO;
        timerText.setText("");
    }
}