package com.example.acadiavisitorstudy;

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

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocationHelper extends Application {

    // Blank constructor
    LocationHelper(){}

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    static final float NUMBER_OF_METERS_IN_MILE = 1609.34f;

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    /***
     * Checks if a Location is within an array of Geofences. Returns true if yes, false if no.
     * @param location
     * @param geofences
     * @return
     */
    static boolean ifWithinGeofences(Location location, ArrayList<IGeofence> geofences) {
        boolean withinGeofence = false;

        /*
         * Check all of the geofences
         */
        for (IGeofence geofence : geofences) {
            if (geofence.withinGeofence(location)){
                withinGeofence = true;
            }
        }

        return withinGeofence;
    }

}
