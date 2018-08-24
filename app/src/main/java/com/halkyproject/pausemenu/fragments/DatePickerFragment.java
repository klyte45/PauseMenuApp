package com.halkyproject.pausemenu.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import com.halkyproject.pausemenu.interfaces.DateTimeSetCallback;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar currentValue = Calendar.getInstance();
    private DateTimeSetCallback callback;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = currentValue.get(Calendar.YEAR);
        int month = currentValue.get(Calendar.MONTH);
        int day = currentValue.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (callback != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            callback.onSetDateTime(calendar);
        }
    }

    public Calendar getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Calendar currentValue) {
        this.currentValue = currentValue;
    }

    public void setCallback(DateTimeSetCallback callback) {
        this.callback = callback;
    }
}
