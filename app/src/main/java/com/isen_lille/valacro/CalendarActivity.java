package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité affichant un simple calendrier
 *
 */
public class CalendarActivity extends AppCompatActivity {

    /**
     * TAG activité calendrier
     */
    private static final String TAG = "CalendarActivity";

    /**
     * Vue calendrier
     */
    public CalendarView mCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
    }
}