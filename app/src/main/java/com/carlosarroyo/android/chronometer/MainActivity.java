package com.carlosarroyo.android.chronometer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String SECONDS_TAG = "Seconds";
    private static final String MILLISECONDS_TAG = "Milliseconds";
    private static final String MILLISECONDS_COUNTER_TAG = "MillisecondsCounter";
    private static final String IS_CHRONO_RUNNING_TAG = "IsChronoRunning";

    private boolean isChronoRunning;
    private int seconds = 0;
    private int milliseconds = 0;
    private int millisecondsCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRunningChrono();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SECONDS_TAG, seconds);
        outState.putInt(MILLISECONDS_TAG, milliseconds);
        outState.putInt(MILLISECONDS_COUNTER_TAG, millisecondsCounter);
        outState.putBoolean(IS_CHRONO_RUNNING_TAG, isChronoRunning);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        seconds = savedInstanceState.getInt(SECONDS_TAG);
        milliseconds = savedInstanceState.getInt(MILLISECONDS_TAG);
        millisecondsCounter = savedInstanceState.getInt(MILLISECONDS_COUNTER_TAG);
        isChronoRunning = savedInstanceState.getBoolean(IS_CHRONO_RUNNING_TAG);
    }

    private AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.7F);

    public void onClickStart(View view) {
        isChronoRunning = true;
        view.startAnimation(buttonClickAnimation);
    }

    public void onClickPause(View view) {
        isChronoRunning = false;
        view.startAnimation(buttonClickAnimation);
    }

    public void onClickReset(View view) {
        isChronoRunning = false;
        seconds = 0;
        milliseconds = 0;
        millisecondsCounter = 1;
        view.startAnimation(buttonClickAnimation);
    }

    private void startRunningChrono() {
        final TextView timeEdittext = findViewById(R.id.txt_seconds);
        final TextView millisEdittext = findViewById(R.id.txt_millis);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hoursInt = seconds / 3600;
                int minutesInt = (seconds % 3600) / 60;
                int secondsInt = seconds % 60;

                String hoursString = hoursInt > 0 ? String.format("%d:", hoursInt) : "";
                String minutesString = minutesInt > 0 ? String.format("%02d:", minutesInt) : "";
                String secondsString = String.format("%02d", secondsInt);
                int millisTime = milliseconds / 10;

                String fullTimeString = hoursString + minutesString + secondsString;
                String millisString = String.format("%02d", millisTime);

                timeEdittext.setText(fullTimeString);
                millisEdittext.setText(millisString);

                if (isChronoRunning) {
                    milliseconds = milliseconds + 10;

                    if (millisecondsCounter < 100) {
                        millisecondsCounter++;

                    } else if (millisecondsCounter == 100) {
                        MainActivity.this.seconds++;
                        millisecondsCounter = 1;
                        milliseconds = 0;
                    }
                }

                handler.postDelayed(this, 10);
            }
        });
    }
}
