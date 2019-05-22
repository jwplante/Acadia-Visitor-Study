package com.example.acadiavisitorstudy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    static private Button trackingButton;
    static private boolean ifNotTracking;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackingButton = (Button) findViewById(R.id.tracking_button);
        ifNotTracking = true;

        // Create location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /***
     * Method to start collecting data
     * @param view
     */
    public void startCollection(View view) {
        if (ifNotTracking)
        {
            // Check location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Need to request permissions.
                requestLocationPermission();

            } else {
                // Notify the user that the tracking has started
                Toast.makeText(this, R.string.on_track_start, Toast.LENGTH_SHORT).show();

                // UI Changes
                trackingButton.setText(R.string.tracking_button_text_stop);
                trackingButton.setBackgroundResource(R.color.stop_t);

                // Insert helper code to start tracking
                Log.d(TAG, "startCollection: This line is executed.");
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // If valid, display location object as string in Log
                                    String locAsString = LocationHelper.locToString(location);
                                    Toast.makeText(MainActivity.this, locAsString, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onSuccess: Current Location " + locAsString);
                                }
                            }
                        });

                ifNotTracking = false;
            }



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

    /**
     * Method to get user permissions about location. Displays a message box if permission is
     * initially denied.
     */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Permission is needed to use the GPS of your phone.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        } else {
           ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    /***
     * Required method to handle the resulting permissions.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
