package com.oury.tuto.cookingstore.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oury.tuto.cookingstore.data.CookingTag;

import java.util.ArrayList;
import java.util.List;

public class DialogInputTag extends DialogFragment {

    DialogListener listener;

    public List<CookingTag> cookingTags;
    private final boolean[] checkedItems = new boolean[CookingTag.values().length];

    public DialogInputTag(List<CookingTag> cookingTags) {
        this.cookingTags = cookingTags;
        for(int i = 0; i < CookingTag.values().length; i++) {
            checkedItems[i] = cookingTags.contains(CookingTag.values()[i]);
        }
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
        builder.setTitle("Selectionnez les tags de la recette")
                .setMultiChoiceItems(tagsToString(), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        CookingTag tag = CookingTag.values()[which];
                        if(isChecked) {
                            if(!cookingTags.contains(tag)) cookingTags.add(tag);
                        } else {
                            cookingTags.remove(tag);
                        }
                    }
                })
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(DialogInputTag.this.getTag());
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
    }

    private String[] tagsToString() {
        String[] output = new String[CookingTag.values().length];
        for(int i = 0; i < CookingTag.values().length; i++) {
            output[i] = CookingTag.values()[i].text;
        }
        return output;
    }
}
