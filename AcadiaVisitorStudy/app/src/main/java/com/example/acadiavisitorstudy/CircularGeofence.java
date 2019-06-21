package com.example.acadiavisitorstudy;

import android.location.Location;

public class CircularGeofence implements IGeofence{

    private double latitudeCenter; // Latitude of the center of the circle
    private double longitudeCenter; // Longitude of the center of the circle
    private double radius; // Radius of geofence (in meters)

    // Constructor
    public CircularGeofence(double latitudeCenter, double longitudeCenter, float radius) {
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.radius = radius;
    }

    public CircularGeofence(GPSPoint point, float radius) {
        this.latitudeCenter = point.getLatitude();
        this.longitudeCenter = point.getLongitude();
        this.radius = radius;
    }


    /***
     * Determine if a given location l is within a geofence.
     * @param l
     * @return
     */
    @Override
    public boolean withinGeofence(Location l) {
        // Create a new Location object
        Location dest = new Location("null");
        dest.setLatitude(this.latitudeCenter);
        dest.setLongitude(this.longitudeCenter);

        float distance  = l.distanceTo(dest);

        return distance <= radius;
    }

}
