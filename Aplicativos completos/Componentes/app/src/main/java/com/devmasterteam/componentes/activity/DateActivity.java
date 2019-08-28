package com.devmasterteam.componentes.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devmasterteam.componentes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        this.mViewHolder.mButtonDate = this.findViewById(R.id.button_date);
        this.mViewHolder.mButtonGetTime = this.findViewById(R.id.button_get_time);
        this.mViewHolder.mButtonSetTime = this.findViewById(R.id.button_set_time);
        this.mViewHolder.mTimePicker = this.findViewById(R.id.time_picker);

        // Eventos
        this.setListeners();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_date) {
            this.showDatePickerDialog();
        } else if (id == R.id.button_get_time) {

            String hour;
            String minute;
            String time;

            if (Build.VERSION.SDK_INT >= 23) {
                hour = String.valueOf(this.mViewHolder.mTimePicker.getHour());
                minute = String.valueOf(this.mViewHolder.mTimePicker.getMinute());
                time = hour + ":" + minute;
            } else {
                hour = String.valueOf(this.mViewHolder.mTimePicker.getCurrentHour());
                minute = String.valueOf(this.mViewHolder.mTimePicker.getCurrentMinute());
                time = hour + ":" + minute;
            }

            Toast.makeText(this, time, Toast.LENGTH_LONG).show();

        } else if (id == R.id.button_set_time) {

            if (Build.VERSION.SDK_INT >= 23) {
                this.mViewHolder.mTimePicker.setHour(20);
                this.mViewHolder.mTimePicker.setMinute(15);
            } else {
                this.mViewHolder.mTimePicker.setCurrentHour(20);
                this.mViewHolder.mTimePicker.setCurrentMinute(15);
            }

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        String date = SIMPLE_DATE_FORMAT.format(calendar.getTime());
        this.mViewHolder.mButtonDate.setText(date);
    }

    /**
     * Mostra seleção de data
     */
    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    /**
     * Atribui eventos aos elementos
     */
    private void setListeners() {
        this.mViewHolder.mButtonDate.setOnClickListener(this);
        this.mViewHolder.mButtonGetTime.setOnClickListener(this);
        this.mViewHolder.mButtonSetTime.setOnClickListener(this);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        Button mButtonDate;
        Button mButtonGetTime;
        Button mButtonSetTime;
        TimePicker mTimePicker;
    }
}
