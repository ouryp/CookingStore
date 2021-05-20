package com.oury.tuto.cookingstore.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingType;

import java.util.ArrayList;
import java.util.List;

public class DialogInputType extends DialogFragment {

    DialogListener listener;

    public CookingType cookingType;

    public DialogInputType(CookingType cookingType) {
        this.cookingType = cookingType;
    }

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
        builder.setTitle("Selectionnez le type de la recette")
                .setSingleChoiceItems(typeToString(), cookingType.ordinal(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cookingType = CookingType.values()[which];
                    }
                })
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(DialogInputType.this.getTag());
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
    }

    private String[] typeToString() {
        String[] output = new String[CookingType.values().length];
        for(int i = 0; i < CookingType.values().length; i++) {
            output[i] = CookingType.values()[i].text;
        }
        return output;
    }
}
