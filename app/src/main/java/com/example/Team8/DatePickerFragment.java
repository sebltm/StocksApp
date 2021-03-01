package com.example.Team8;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//TODO refactor this into its own file
public class DatePickerFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String format = "dd MMM yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    private final EditText editText;
    private final Context ctx;
    private Calendar cal;

    public DatePickerFragment(EditText editText, Context ctx) {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.cal = Calendar.getInstance();
        this.ctx = ctx;

        editText.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public void onClick(View v) {
        new DatePickerDialog(this.ctx, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        cal.set(year, month, dayOfMonth);

        if (cal.getTime().compareTo(new Date(System.currentTimeMillis())) > 0) {
            Toast.makeText(ctx, R.string.date_future, Toast.LENGTH_SHORT).show();
            Date currDate = new Date(System.currentTimeMillis());
            cal.setTime(currDate);
            editText.setText(dateFormat.format(currDate));
        } else {
            editText.setText(dateFormat.format(cal.getTime()));
        }

    }

    public void clear() {
        this.cal = Calendar.getInstance();
        editText.setText(dateFormat.format(cal.getTime()));
    }

    public void setDayEqual(DatePickerFragment otherDate) {
        cal.setTime(otherDate.getCal().getTime());
        editText.setText(dateFormat.format(cal.getTime()));
    }

    public Calendar getCal() {
        return cal;
    }
}