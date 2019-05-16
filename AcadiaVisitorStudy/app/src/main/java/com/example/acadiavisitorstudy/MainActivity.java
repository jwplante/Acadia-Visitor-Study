package com.example.acadiavisitorstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button trackingButton;
    static private boolean ifNotTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackingButton = (Button) findViewById(R.id.tracking_button);
        ifNotTracking = true;
    }

    /***
     * Method to start collecting data
     * @param view
     */
    public void startCollection(View view) {
        if (ifNotTracking)
        {
            // Notify the user that the tracking has started
            Toast.makeText(this, R.string.on_track_start, Toast.LENGTH_SHORT).show();

            // UI Changes
            trackingButton.setText(R.string.tracking_button_text_stop);
            trackingButton.setBackgroundResource(R.color.stop_t);

            // Insert helper code to start tracking

            ifNotTracking = false;

        } else {
            // Notify the user that we are not tracking anymore (we promise)
            Toast.makeText(this, R.string.on_track_stop, Toast.LENGTH_SHORT).show();

            // UT Changes
            trackingButton.setText(R.string.tracking_button_text_start);
            trackingButton.setBackgroundResource(R.color.start_t);

            // Insert helper code

            ifNotTracking = true;
        }
    }

    /***
     * Launches the SurveyActivity activity
     * @param view
     */
    public void launchSurvey(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }
}
