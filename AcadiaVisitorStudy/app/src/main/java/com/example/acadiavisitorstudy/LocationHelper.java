package com.example.acadiavisitorstudy;

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
         * Check all of the geofences in a loca
         */
        for (IGeofence geofence : geofences) {
            if (geofence.withinGeofence(location)){
                withinGeofence = true;
            }
        }

        return withinGeofence;
    }

}
