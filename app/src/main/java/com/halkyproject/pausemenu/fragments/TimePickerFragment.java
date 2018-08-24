package com.halkyproject.pausemenu.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import com.halkyproject.pausemenu.interfaces.DateTimeSetCallback;

import java.util.Calendar;
import java.util.Objects;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Calendar currentValue = Calendar.getInstance();
    private DateTimeSetCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(Objects.requireNonNull(getActivity()), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the date chosen by the user
        if (callback != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
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
