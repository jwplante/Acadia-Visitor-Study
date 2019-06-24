package com.example.acadiavisitorstudy;

import android.location.Location;

public interface IGeofence {
   /*
   * Common interface for different types of Geofences
    */

   /***
    * Returns true when the location l is within a geofence.
    * @param l
    * @return
    */
   boolean withinGeofence(Location l);
}
