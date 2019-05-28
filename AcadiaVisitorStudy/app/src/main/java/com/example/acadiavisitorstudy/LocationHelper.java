package com.example.acadiavisitorstudy;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
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
     * Determines if a Location object is within a specific radius of a given
     * area.
     * @param latCenter - latitude of the center of geofence
     * @param longCenter - longitude of the center of geofence
     * @param radius - in miles
     * @return
     */
    static boolean withinGeofence(Location l, float latCenter, float longCenter, float radius) {
        // Create a new Location object
        Location dest = new Location("null");
        dest.setLatitude(latCenter);
        dest.setLongitude(longCenter);

       float distance  = metersToMiles(l.distanceTo(dest));

       return distance <= radius;
    }

    /***
     * Simple method to convert meters to miles.
     * @param distance
     * @return
     */
    static float metersToMiles(float distance) {
       return (distance / NUMBER_OF_METERS_IN_MILE);
    }
}
