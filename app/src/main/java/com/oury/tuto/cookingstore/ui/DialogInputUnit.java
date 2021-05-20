package com.oury.tuto.cookingstore.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oury.tuto.cookingstore.data.CookingUnit;

public class DialogInputUnit extends DialogFragment {

    DialogListener listener;

    public CookingUnit unit;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selectionnez l'unitée :")
                .setSingleChoiceItems(typeToString(), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unit = CookingUnit.values()[which];
                    }
                })
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        listener.onDialogPositiveClick(DialogInputUnit.this.getTag());
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
        String[] output = new String[CookingUnit.values().length];
        for(int i = 0; i < CookingUnit.values().length; i++) {
            output[i] = CookingUnit.values()[i].text;
        }
        return output;
    }
}
