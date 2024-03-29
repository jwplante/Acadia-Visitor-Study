package com.example.acadiavisitorstudy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;

/*
 * NOTICE: Some functions from Google's Location Samples have been reproduced here
 * in order to get the LocationUpdatesService to function, so the following attribution
 * is included below to comply with Section 4 of the Apache 2.0 license.
 */

/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Modified by James Plante and Joseph Hogan, 2019
 * to include additional functionality.
 * Any modifications are also licensed under the Apache v2.0 License.
 */

public class MainActivity extends AppCompatActivity {

    // Middle button
    static private Button trackingButton;

    // Used to check button state (should be ported to a preference
    static private boolean ifNotTracking;

    private FusedLocationProviderClient fusedLocationClient; // Fused Location Client

    // Class tag
    private static final String TAG = "MainActivity";

    // Permission code number (doesn't matter what it is)
    private static final int LOCATION_PERMISSION_CODE = 0;

    // ID for the notification channel
    private static final String CHANNEL_ID = "AcadiaVisitorStudy";

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: This method is called");
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(MainActivity.this, LocationHelper.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myReceiver = new MyReceiver();

        LocationHelper.requestingLocationUpdates(this);

        setContentView(R.layout.activity_main);
        trackingButton = (Button) findViewById(R.id.tracking_button);

        // If there is no user ID, we want to create it

        // If the application is not launched for the first time
        // reset the buttons.
        ifNotTracking = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this);


        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
        SharedPreferences settings = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        ifNotTracking = settings.getBoolean("ifNotTracking", true);
        changeButtonState(!ifNotTracking);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        SharedPreferences s = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        s.edit().putBoolean("ifNotTracking", ifNotTracking).apply();
        super.onStop();
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

                changeButtonState(ifNotTracking);
                // Start up the location service
                mService.requestLocationUpdates();

                ifNotTracking = false;
            }

        } else {
            // Stop the foreground service
            mService.removeLocationUpdates();

            changeButtonState(ifNotTracking);

            // Notify the user that we are not tracking anymore (we promise)
            Toast.makeText(this, R.string.on_track_stop, Toast.LENGTH_SHORT).show();

            ifNotTracking = true;
        }
    }

    /***
     * Changes the state of the tracking button depending on when it is being tracked.
     * @param ifNotTracking
     */
    private void changeButtonState(boolean ifNotTracking) {
        if (ifNotTracking) {
            // Set to red and stop
            trackingButton.setText(R.string.tracking_button_text_stop);
            trackingButton.setBackgroundResource(R.color.stop_t);
        } else {
            // Set to green and start
            trackingButton.setText(R.string.tracking_button_text_start);
            trackingButton.setBackgroundResource(R.color.start_t);
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
    /***
     * Launches the LicenseActivity activity
     * @param view
     */
    public void viewLicense(View view){
        Intent intent = new Intent(this, LicensingActivity.class);
        startActivity(intent);
    }

    /***
     * Launches the InstructionActivity activity
     * @param view
     */
    public void viewInstructions(View view){
        Intent intent = new Intent(this, InstructionActivity.class);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ifNotTracking", ifNotTracking);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean currentState = savedInstanceState.getBoolean("ifNotTracking");

        SharedPreferences settings = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        ifNotTracking = settings.getBoolean("ifNotTracking", currentState);

        if (!ifNotTracking) {
            // Set to red and stop
            trackingButton.setText(R.string.tracking_button_text_stop);
            trackingButton.setBackgroundResource(R.color.stop_t);
        } else {
            trackingButton.setText(R.string.tracking_button_text_start);
            trackingButton.setBackgroundResource(R.color.start_t);
        }

    }

}
