package com.oury.tuto.cookingstore.Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingDuration;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class NewCooking extends AppCompatActivity implements DialogListener {

    public static final String EXTRA_STRING_NAME = "com.oury.tuto.cookingstore.Activity.NewCooking.EXTRA_STRING_NAME";

    private static final String DIALOG_INPUT_TYPE_TAG = "DIALOG_INPUT_TYPE_TAG";
    private static final String DIALOG_INPUT_TAG_TAG = "DIALOG_INPUT_TAG_TAG";
    private static final String DIALOG_INPUT_INGREDIENT_TAG = "DIALOG_INPUT_INGREDIENT_TAG";
    private static final String DIALOG_INPUT_TEXT_PROTOCOL_TAG = "DIALOG_INPUT_TEXT_PROTOCOL_TAG";
    private static final String DIALOG_INPUT_TEXT_NAME_TAG = "DIALOG_INPUT_TEXT_NAME_TAG";
    private static final String DIALOG_INPUT_TEXT_COMMENT_TAG = "DIALOG_INPUT_TEXT_COMMENT_TAG";
    private static final String DIALOG_INPUT_TEXT_TIME_TAG = "DIALOG_INPUT_TEXT_TIME_TAG";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Cooking cooking;

    private ImageView image;
    private TextInputEditText txtName;
    private TextView txtType;
    private TextView txtTag;
    private TextView txtTime;
    private TextView txtComment;
    private IngredientListAdapter ingredientListAdapter;
    private ProtocolListAdapter protocolListAdapter;
    private FloatingActionButton floatingActionButton;

    View.OnClickListener floatingActionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CookingRepository cookingRepository = new CookingRepository(getApplication(), cookingRoomEvent);
            cookingRepository.insert(cooking);
        }
    };

    CookingRoomEvent cookingRoomEvent = new CookingRoomEvent() {
        @Override
        public void onInsertCallback(long id) {
            NewCooking.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(id < 0) {
                        Toast.makeText(getApplicationContext(), "Insert ERROR, line : " + id, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Insert successful at line : " + id, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewCooking.this, HomeActivity.class));
                    }
                }
            });
        }

        @Override
        public void onUpdateCallback() {

        }
    };

    DialogInputType dialogInputType;
    DialogInputTag dialogInputTag;
    DialogInputIngredient dialogInputIngredient;
    DialogInputText dialogInputText;
    DialogInputTime dialogInputTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_cooking_2);

        image = findViewById(R.id.cooking_image);
        txtName = findViewById(R.id.cooking_name);
        txtType = findViewById(R.id.cooking_type);
        txtTag = findViewById(R.id.cooking_tags);
        txtTime = findViewById(R.id.cooking_time);
        txtComment = findViewById(R.id.cooking_comment);
        RecyclerView rvIngredients = findViewById(R.id.cooking_ingredients);
        RecyclerView rvProtocol = findViewById(R.id.cooking_protocol);
        floatingActionButton = findViewById(R.id.displayCooking_fab);
        floatingActionButton.setOnClickListener(floatingActionButtonOnClickListener);

        ingredientListAdapter = new IngredientListAdapter(new ListViewEvent() {
            @Override
            public void onListViewClickCallback(View v) {

            }

            @Override
            public void onListViewLongClickCallback(View v) {

            }
        }, IngredientListAdapter.PROPRIETY_BOX_SIZE_LOW);

        protocolListAdapter = new ProtocolListAdapter(new ListViewEvent() {
            @Override
            public void onListViewClickCallback(View v) {

            }

            @Override
            public void onListViewLongClickCallback(View v) {

            }
        });

        rvIngredients.setAdapter(ingredientListAdapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));

        rvProtocol.setAdapter(protocolListAdapter);
        rvProtocol.setLayoutManager(new LinearLayoutManager(this));
        rvProtocol.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        dialogInputText = new DialogInputText("Entrée le nom de la recette : ", "Ok", "Annuler", "");
        dialogInputText.show(getSupportFragmentManager(), DIALOG_INPUT_TEXT_NAME_TAG);

        cooking = new Cooking();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDialogPositiveClick(String tag) {
        switch(tag) {
            case DIALOG_INPUT_TEXT_NAME_TAG :
                cooking.setName(Cooking.formatName(dialogInputText.text));
                txtName.setText(cooking.getName());
                dialogInputType = new DialogInputType(CookingType.ALL);
                dialogInputType.show(getSupportFragmentManager(), DIALOG_INPUT_TYPE_TAG);
                break;
            case DIALOG_INPUT_TYPE_TAG :
                cooking.setType(dialogInputType.cookingType);
                txtType.setText(cooking.getType().text);
                dialogInputTag = new DialogInputTag(new ArrayList<>());
                dialogInputTag.show(getSupportFragmentManager(), DIALOG_INPUT_TAG_TAG);
                break;
            case DIALOG_INPUT_TAG_TAG :
                cooking.setTags(dialogInputTag.cookingTags);
                txtTag.setText(CookingTag.printTags(cooking.getTags()));
                dialogInputTime = new DialogInputTime(new CookingDuration(Duration.ZERO, Duration.ZERO));
                dialogInputTime.show(getSupportFragmentManager(), DIALOG_INPUT_TEXT_TIME_TAG);
                break;
            case DIALOG_INPUT_TEXT_TIME_TAG :
                cooking.setTime(dialogInputTime.cookingDuration);
                txtTime.setText(cooking.getTime().print());
                showCaptureImage();
                break;
            case DialogInputIngredient.DIALOG_UNIT_TAG :
                dialogInputIngredient.readUnit();
                break;
            case DIALOG_INPUT_INGREDIENT_TAG :
                if(dialogInputIngredient.ingredient != null) {
                    cooking.addIngredient(dialogInputIngredient.ingredient);
                    ingredientListAdapter.submitList(cooking.getIngredient());
                }
                dialogInputIngredient = new DialogInputIngredient();
                dialogInputIngredient.show(getSupportFragmentManager(), DIALOG_INPUT_INGREDIENT_TAG);
                break;
            case DIALOG_INPUT_TEXT_PROTOCOL_TAG:
                if(dialogInputText.text != null) {
                    cooking.addProtocol(Cooking.formatText(dialogInputText.text));
                    protocolListAdapter.submitList(cooking.getProtocol());
                }
                dialogInputText = new DialogInputText("Entrée une étape ...", "Suivant ...", "Terminé", "");
                dialogInputText.show(getSupportFragmentManager(), DIALOG_INPUT_TEXT_PROTOCOL_TAG);
            case DIALOG_INPUT_TEXT_COMMENT_TAG :
                cooking.setComment(Cooking.formatText(dialogInputText.text));
                txtComment.setText(cooking.getComment());
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(String tag) {
        if(tag.equals(DIALOG_INPUT_INGREDIENT_TAG)) {
            if(dialogInputIngredient.ingredient != null) {
                cooking.addIngredient(dialogInputIngredient.ingredient);
                ingredientListAdapter.submitList(cooking.getIngredient());
            }
            dialogInputText = new DialogInputText("Entrée une étape ...", "Suivant ...", "Terminé", "");
            dialogInputText.show(getSupportFragmentManager(), DIALOG_INPUT_TEXT_PROTOCOL_TAG);
        } else if(tag.equals(DIALOG_INPUT_TEXT_PROTOCOL_TAG)) {
            if(dialogInputText.text != null) {
                cooking.addProtocol(dialogInputText.text);
                protocolListAdapter.submitList(cooking.getProtocol());
            }
            dialogInputText = new DialogInputText("Entrée un commentaire", "OK", "", "");
            dialogInputText.show(getSupportFragmentManager(), DIALOG_INPUT_TEXT_COMMENT_TAG);
        } else if(tag.equals(DIALOG_INPUT_TEXT_NAME_TAG)){
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK) {
                try {
                    image.setImageBitmap(cooking.getImage().createThumbnail(new Size(image.getWidth(), image.getHeight())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "photo error", Toast.LENGTH_SHORT).show();
                cooking.getImage().delete();
            }
            dialogInputIngredient = new DialogInputIngredient();
            dialogInputIngredient.show(getSupportFragmentManager(), DIALOG_INPUT_INGREDIENT_TAG);
        }
    }

    private void showCaptureImage() {
        new AlertDialog.Builder(NewCooking.this)
                .setTitle("Photo")
                .setMessage("Voulez vous ajouter une photo maintenant ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cooking.setImage(CookingImage.create(getFilesDir(), cooking.getName()));
                        Uri uri = FileProvider.getUriForFile(NewCooking.this,"com.oury.tuto.cookingstore.Activity", cooking.getImage().getFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogInputIngredient = new DialogInputIngredient();
                        dialogInputIngredient.show(getSupportFragmentManager(), DIALOG_INPUT_INGREDIENT_TAG);
                    }
                })
                .setCancelable(false)
                .show();
    }
}