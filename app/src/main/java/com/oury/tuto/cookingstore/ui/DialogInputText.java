package com.oury.tuto.cookingstore.ui;
import com.oury.tuto.cookingstore.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogInputText extends DialogFragment {

    DialogListener listener;

    public String text;

    private final String message;
    private final String positiveText;
    private final String negativeText;

    public DialogInputText(String message, @NonNull String positiveText, @NonNull String negativeText, String text) {
        this.message = message;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.text = text;
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
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setMessage(message)
                .setView(inflater.inflate(R.layout.dialog_text, null))
                // Add action buttons
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = getDialog().findViewById(R.id.dialog_text);
                        text = editText.getText().toString();
                        dismiss();
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(DialogInputText.this.getTag());
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = getDialog().findViewById(R.id.dialog_text);
                        text = editText.getText().toString();
                        dismiss();
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(DialogInputText.this.getTag());
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
        EditText editText = getDialog().findViewById(R.id.dialog_text);
        editText.setText(text);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
    }
}
