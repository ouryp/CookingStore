package com.oury.tuto.cookingstore.ui;

import com.oury.tuto.cookingstore.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oury.tuto.cookingstore.data.CookingIngredient;
import com.oury.tuto.cookingstore.data.CookingUnit;

public class DialogInputIngredient extends DialogFragment{

    DialogListener listener;

    public  static final String DIALOG_UNIT_TAG = "com.oury.tuto.cookingstore.ui.DialogInputIngredient.DIALOG_UNIT_TAG";
    public CookingIngredient ingredient;

    private DialogInputUnit dialogInputUnit;
    private CookingUnit unit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_ingredient, null))
                // Add action buttons
                .setPositiveButton("Suivant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        readIngredient();
                        dismiss();
                        listener.onDialogPositiveClick(DialogInputIngredient.this.getTag());
                    }
                })
                .setNegativeButton("Terminé", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        readIngredient();
                        dismiss();
                        listener.onDialogNegativeClick(DialogInputIngredient.this.getTag());
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
        EditText editText = getDialog().findViewById(R.id.dialog_ingredient_name);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);

        Button buttonUnit = getDialog().findViewById(R.id.dialog_ingredient_unity);
        buttonUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInputUnit = new DialogInputUnit();
                dialogInputUnit.show(getChildFragmentManager(), DIALOG_UNIT_TAG);
            }
        });
    }

    public void readIngredient() {
        EditText editTextIngredient = getDialog().findViewById(R.id.dialog_ingredient_name);
        EditText editTextQuantity = getDialog().findViewById(R.id.dialog_ingredient_quantity);

        if(unit != null && !editTextIngredient.getText().toString().isEmpty() && !editTextQuantity.getText().toString().isEmpty()) {
            ingredient = new CookingIngredient(editTextIngredient.getText().toString(), Integer.parseInt(editTextQuantity.getText().toString()), unit);
        }
        else {
            ingredient = null;
        }
    }

    public void readUnit() {
        if(dialogInputUnit.unit != null) {
            unit = dialogInputUnit.unit;
            Button buttonUnit = getDialog().findViewById(R.id.dialog_ingredient_unity);
            buttonUnit.setText(unit.text);
        }
    }
}
