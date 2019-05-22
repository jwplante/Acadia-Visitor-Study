package com.example.acadiavisitorstudy;

import android.app.Application;
import android.location.Location;

public class LocationHelper extends Application {

    // Blank constructor
    LocationHelper(){}


    /***
     * Converts a Location object into a displayable string
     * @param l (Location) location data
     */
   public static String locToString(Location l){
       double latitude = l.getLatitude();
       double longitude = l.getLongitude();

       return "(" + latitude + ", " + longitude + ")";
   }
}
