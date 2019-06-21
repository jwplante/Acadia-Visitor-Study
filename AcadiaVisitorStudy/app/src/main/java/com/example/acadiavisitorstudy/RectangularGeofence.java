package com.example.acadiavisitorstudy;

import android.location.Location;

public class RectangularGeofence implements IGeofence {

    private double topLeftLatitude; // Latitude of the first GPS point
    private double topLeftLongitude; // Longitude of the second GPS point
    private double bottomRightLatitude; // Latitude of the second GPS point
    private double bottomRightLongitude; // Longitude of the second GPS pointk

    // Constructor
    public RectangularGeofence(GPSPoint topLeftPoint, GPSPoint bottomRightPoint) {
       this.topLeftLatitude = topLeftPoint.getLatitude();
       this.topLeftLongitude = topLeftPoint.getLongitude();
       this.bottomRightLatitude = bottomRightPoint.getLatitude();
       this.bottomRightLongitude = bottomRightPoint.getLongitude();
    }

    @Override
    public boolean withinGeofence(Location l) {

        // Get the point's latitude and longitude
        double compareLatitude = l.getLatitude();
        double compareLongitude = l.getLongitude();

        return ((compareLatitude <= this.topLeftLatitude) && (compareLatitude >= this.bottomRightLatitude)
                && (compareLongitude <= this.bottomRightLongitude) && (compareLongitude >= this.topLeftLongitude));
    }

}
