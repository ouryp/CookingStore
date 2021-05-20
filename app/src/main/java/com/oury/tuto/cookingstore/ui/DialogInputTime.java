package com.oury.tuto.cookingstore.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.CookingDuration;

import java.sql.Time;
import java.time.Duration;

public class DialogInputTime extends DialogFragment {

    public CookingDuration cookingDuration;

    DialogListener listener;

    private Duration protocolDuration;
    private Duration cookDuration;
    private TimePicker.OnTimeChangedListener protocolTimeListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            protocolDuration = Duration.ofMinutes(minute + hourOfDay*60);
        }
    };
    private TimePicker.OnTimeChangedListener cookingTimeListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            cookDuration = Duration.ofMinutes(minute + hourOfDay*60);
        }
    };

    public DialogInputTime(CookingDuration cookingDuration) {
        this.cookingDuration = cookingDuration;
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
        builder.setMessage("Selectionnez les temps de la recette : ")
                .setView(inflater.inflate(R.layout.cooking_time, null))
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cookingDuration = new CookingDuration(protocolDuration, cookDuration);
                        dismiss();
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(DialogInputTime.this.getTag());
                    }
                })
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePicker timePicker_protocol = getDialog().findViewById(R.id.timePicker_protocol);
        timePicker_protocol.setOnTimeChangedListener(protocolTimeListener);
        timePicker_protocol.setIs24HourView(true);
        timePicker_protocol.setMinute((int)cookingDuration.getProtocolMinutes());
        timePicker_protocol.setHour((int) cookingDuration.getProtocolHours());

        TimePicker timePicker_cooking = getDialog().findViewById(R.id.timePicker_cooking);
        timePicker_cooking.setOnTimeChangedListener(cookingTimeListener);
        timePicker_cooking.setIs24HourView(true);
        timePicker_cooking.setHour((int) cookingDuration.getCookingHours());
        timePicker_cooking.setMinute((int) cookingDuration.getCookingMinutes());
    }
}
