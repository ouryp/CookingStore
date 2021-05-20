package com.oury.tuto.cookingstore.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingImage;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.data.CookingType;
import com.oury.tuto.cookingstore.room.CookingRepository;
import com.oury.tuto.cookingstore.room.CookingRoomEvent;
import com.oury.tuto.cookingstore.ui.DialogInputIngredient;
import com.oury.tuto.cookingstore.ui.DialogInputTag;
import com.oury.tuto.cookingstore.ui.DialogInputText;
import com.oury.tuto.cookingstore.ui.DialogInputTime;
import com.oury.tuto.cookingstore.ui.DialogInputType;
import com.oury.tuto.cookingstore.ui.DialogListener;
import com.oury.tuto.cookingstore.ui.IngredientListAdapter;
import com.oury.tuto.cookingstore.ui.ListViewEvent;
import com.oury.tuto.cookingstore.ui.ProtocolListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditActivity extends AppCompatActivity implements DialogListener {

    public static final String EXTRA_UID_VALUE = "com.oury.tuto.cookingstore.Activity.EditActivity.EXTRA_UID_VALUE";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String DIALOG_TAG_INGREDIENTS = "DIALOG_TAG_INGREDIENTS";
    private static final String DIALOG_TAG_TYPE = "DIALOG_TAG_TYPE";
    private static final String DIALOG_TAG_TAGS = "DIALOG_TAG_TAGS";
    private static final String DIALOG_TAG_TIME = "DIALOG_TAG_TIME";
    private static final String DIALOG_TAG_INGREDIENT = "DIALOG_TAG_INGREDIENT";
    private static final String DIALOG_TAG_INGREDIENT_DELETE = "DIALOG_TAG_INGREDIENT_DELETE";
    private static final String DIALOG_TAG_TEXT = "DIALOG_TAG_TEXT";
    private static final String DIALOG_TAG_TEXT_PROTOCOL = "DIALOG_TAG_TEXT_PROTOCOL";
    private static final String DIALOG_TAG_TEXT_PROTOCOL_ADD = "DIALOG_TAG_TEXT_PROTOCOL_ADD";
    private static final String DIALOG_TAG_TEXT_PROTOCOL_DELETE = "DIALOG_TAG_TEXT_PROTOCOL_DELETE";
    private static final String DIALOG_ARG_LIST_POS = "DIALOG_ARG_LIST_POS";
    private Cooking cooking;
    private ImageView imageView;
    private TextInputEditText name;
    private Button buttonType;
    private TextView textType;
    private Button buttonTags;
    private TextView textTags;
    private Button buttonTime;
    private TextView textTime;
    private TextView textComment;
    private RecyclerView rvIngredients;
    private Button buttonIngredientAdd;
    private RecyclerView rvProtocol;
    private Button buttonProtocolAdd;
    private IngredientListAdapter ingredientListAdapter;
    private ProtocolListAdapter protocolListAdapter;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton buttonSave;

    private CookingRepository cookingRepository;

    DialogInputType dialogInputType;
    DialogInputTag dialogInputTag;
    DialogInputText dialogInputText;
    DialogInputTime dialogInputTime;
    DialogInputIngredient dialogInputIngredient;

    private CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {

        }

        @Override
        public void onUpdateCallback() {
            EditActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(EditActivity.this, HomeActivity.class));
                }
            });
        }
    };

    private ListViewEvent ingredientListEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {

        }

        @Override
        public void onListViewLongClickCallback(View v) {
            int pos = rvIngredients.getChildAdapterPosition(v);
            dialogInputText = new DialogInputText(" Supprimer cet ingrédient ?", "Ok", "Annulé", ingredientListAdapter.getCurrentList().get(pos).getIngredient());
            Bundle bundle = new Bundle();
            bundle.putInt(DIALOG_ARG_LIST_POS, pos);
            dialogInputText.setArguments(bundle);
            dialogInputText.show(getSupportFragmentManager(), DIALOG_TAG_INGREDIENT_DELETE);
        }
    };

    private ListViewEvent protocolListEvent = new ListViewEvent() {
        @Override
        public void onListViewClickCallback(View v) {
            int pos = rvProtocol.getChildAdapterPosition(v);
            String txt = protocolListAdapter.getCurrentList().get(pos);
            dialogInputText = new DialogInputText("", "Ok", "Annulé", txt);
            Bundle bundle = new Bundle();
            bundle.putInt(DIALOG_ARG_LIST_POS, pos);
            dialogInputText.setArguments(bundle);
            dialogInputText.show(getSupportFragmentManager(), DIALOG_TAG_TEXT_PROTOCOL);
        }

        @Override
        public void onListViewLongClickCallback(View v) {
            int pos = rvProtocol.getChildAdapterPosition(v);
            String txt = protocolListAdapter.getCurrentList().get(pos);
            dialogInputText = new DialogInputText(" Supprimer cette ligne ?", "Ok", "Annulé", txt);
            Bundle bundle = new Bundle();
            bundle.putInt(DIALOG_ARG_LIST_POS, pos);
            dialogInputText.setArguments(bundle);
            dialogInputText.show(getSupportFragmentManager(), DIALOG_TAG_TEXT_PROTOCOL_DELETE);
        }
    };

    private View.OnClickListener imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showCaptureImage();
        }
    };
    private View.OnClickListener buttonTypeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputType = new DialogInputType(cooking.getType());
            dialogInputType.show(getSupportFragmentManager(), DIALOG_TAG_TYPE);
        }
    };
    private View.OnClickListener buttonTagsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputTag = new DialogInputTag(cooking.getTags());
            dialogInputTag.show(getSupportFragmentManager(), DIALOG_TAG_TAGS);
        }
    };
    private View.OnClickListener buttonTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputTime = new DialogInputTime(cooking.getTime());
            dialogInputTime.show(getSupportFragmentManager(), DIALOG_TAG_TIME);
        }
    };
    private View.OnClickListener commentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputText = new DialogInputText("", "OK", "Annulé", cooking.getComment());
            dialogInputText.show(getSupportFragmentManager(), DIALOG_TAG_TEXT);
        }
    };
    private View.OnClickListener buttonIngredientAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputIngredient = new DialogInputIngredient();
            dialogInputIngredient.show(getSupportFragmentManager(), DIALOG_TAG_INGREDIENT);
        }
    };
    private View.OnClickListener buttonProtocolAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogInputText = new DialogInputText("Ajouter une étape :", "OK", "Annulé", "");
            dialogInputText.show(getSupportFragmentManager(), DIALOG_TAG_TEXT_PROTOCOL_ADD);
        }
    };
    private View.OnClickListener buttonSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cookingRepository.update(cooking);
        }
    };
    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            cooking.setName(s.toString());
        }
    };

    private ItemTouchHelper.SimpleCallback simpleCallbackTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            protocolListAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_cooking);

        imageView = findViewById(R.id.edit_cooking_image);
        imageView.setOnClickListener(imageOnClickListener);
        name = findViewById(R.id.edit_cooking_name);
        name.addTextChangedListener(nameTextWatcher);
        buttonType = findViewById(R.id.edit_cooking_type_button);
        buttonType.setOnClickListener(buttonTypeOnClickListener);
        textType = findViewById(R.id.edit_cooking_type_text);
        buttonTags = findViewById(R.id.edit_cooking_tags_button);
        buttonTags.setOnClickListener(buttonTagsOnClickListener);
        textTags = findViewById(R.id.edit_cooking_tags_text);
        buttonTime = findViewById(R.id.edit_cooking_time_button);
        buttonTime.setOnClickListener(buttonTimeOnClickListener);
        textTime = findViewById(R.id.edit_cooking_time_text);
        textComment = findViewById(R.id.edit_cooking_comment_text);
        textComment.setOnClickListener(commentOnClickListener);
        rvIngredients = findViewById(R.id.edit_cooking_rv_ingredients);
        buttonIngredientAdd = findViewById(R.id.edit_cooking_ingredient_add);
        rvProtocol = findViewById(R.id.edit_cooking_rv_protocol);
        buttonProtocolAdd = findViewById(R.id.edit_cooking_protocol_add);
        buttonSave = findViewById(R.id.edit_cooking_fab);

        ingredientListAdapter = new IngredientListAdapter(ingredientListEvent, IngredientListAdapter.PROPRIETY_BOX_SIZE_HIGH);
        protocolListAdapter = new ProtocolListAdapter(protocolListEvent);
        protocolListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                ((ProtocolListAdapter.ViewHolder)rvProtocol.findViewHolderForLayoutPosition(fromPosition)).update(toPosition);
                ((ProtocolListAdapter.ViewHolder)rvProtocol.findViewHolderForLayoutPosition(toPosition)).update(fromPosition);
            }
        });

        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(ingredientListAdapter);
        buttonIngredientAdd.setOnClickListener(buttonIngredientAddClickListener);

        rvProtocol.setLayoutManager(new LinearLayoutManager(this));
        rvProtocol.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvProtocol.setAdapter(protocolListAdapter);
        buttonProtocolAdd.setOnClickListener(buttonProtocolAddClickListener);

        itemTouchHelper = new ItemTouchHelper(simpleCallbackTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvProtocol);

        buttonSave.setOnClickListener(buttonSaveClickListener);

        cookingRepository = new CookingRepository(getApplication(), cookingRoomEvent);
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
                    EditActivity.this.cooking = cooking;
                    try {
                        imageView.setImageBitmap(cooking.getImage().createThumbnail(new Size(imageView.getMaxWidth(), imageView.getMaxHeight())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    name.setText(cooking.getName());
                    textType.setText(cooking.getType().text);
                    textTags.setText(CookingTag.printTags(cooking.getTags()));
                    textTime.setText(cooking.getTime().print());
                    textComment.setText(cooking.getComment());
                    ingredientListAdapter.submitList(cooking.getIngredient());
                    protocolListAdapter.submitList(cooking.getProtocol());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK) {
                try {
                    imageView.setImageBitmap(cooking.getImage().createThumbnail(new Size(imageView.getWidth(), imageView.getHeight())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "photo error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(String tag) {
        switch (tag) {
            case DIALOG_TAG_TYPE :
                cooking.setType(dialogInputType.cookingType);
                textType.setText(cooking.getType().text);
                break;
            case DIALOG_TAG_TAGS :
                cooking.setTags(dialogInputTag.cookingTags);
                textTags.setText(CookingTag.printTags(cooking.getTags()));
                break;
            case DIALOG_TAG_TIME :
                cooking.setTime(dialogInputTime.cookingDuration);
                textTime.setText(cooking.getTime().print());
                break;
            case DIALOG_TAG_TEXT :
                cooking.setComment(dialogInputText.text);
                textComment.setText(cooking.getComment());
                break;
            case DIALOG_TAG_TEXT_PROTOCOL :
                String text = dialogInputText.text;
                int pos = dialogInputText.getArguments().getInt(DIALOG_ARG_LIST_POS);
                cooking.editProtocol(pos, text);
                protocolListAdapter.submitList(cooking.getProtocol());
                protocolListAdapter.notifyItemChanged(pos);
                break;
            case DIALOG_TAG_TEXT_PROTOCOL_DELETE :
                cooking.removeProtocol(dialogInputText.getArguments().getInt(DIALOG_ARG_LIST_POS));
                protocolListAdapter.submitList(cooking.getProtocol());
                protocolListAdapter.notifyDataSetChanged();
                break;
            case DIALOG_TAG_TEXT_PROTOCOL_ADD :
                cooking.addProtocol(dialogInputText.text);
                protocolListAdapter.submitList(cooking.getProtocol());
                protocolListAdapter.notifyDataSetChanged();
                break;
            case DialogInputIngredient.DIALOG_UNIT_TAG :
                dialogInputIngredient.readUnit();
                break;
            case DIALOG_TAG_INGREDIENT :
                cooking.addIngredient(dialogInputIngredient.ingredient);
                ingredientListAdapter.submitList(cooking.getIngredient());
                ingredientListAdapter.notifyDataSetChanged();
                break;
            case DIALOG_TAG_INGREDIENT_DELETE :
                cooking.removeIngredient(dialogInputText.getArguments().getInt(DIALOG_ARG_LIST_POS));
                ingredientListAdapter.submitList(cooking.getIngredient());
                ingredientListAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(String tag) {

    }

    private void showCaptureImage() {
        new AlertDialog.Builder(EditActivity.this)
                .setTitle("Photo")
                .setMessage("Voulez vous ajouter une photo maintenant ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri uri = FileProvider.getUriForFile(EditActivity.this,"com.oury.tuto.cookingstore.Activity", cooking.getImage().getFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }
}
