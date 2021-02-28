package com.example.Team8;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText editTextFromDate = (EditText) findViewById(R.id.mDatePickerFrom);
        EditText editTextToDate = (EditText) findViewById(R.id.mDatePickerTo);

        DatePickerFragment fromDate = new DatePickerFragment(editTextFromDate, this);
        DatePickerFragment toDate = new DatePickerFragment(editTextToDate, this);
    }

    public static class DatePickerFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        private final EditText editText;
        private final Calendar cal;
        private final Context ctx;

        public DatePickerFragment(EditText editText, Context ctx) {
            this.editText = editText;
            this.editText.setOnClickListener(this);
            this.cal = Calendar.getInstance();
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            new DatePickerDialog(this.ctx, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String format = "dd-MMM-yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
            editText.getText().clear();
            editText.setText(dateFormat.format(cal.getTime()));
        }
    }
}