package com.example.acadiavisitorstudy;

public class GPSPoint {

    /***
     * Custom class to operate with the geofence classes
     */
    private double latitude;
    private double longitude;

    // Constructor
    public GPSPoint(double latitude, double longitude) {
       this.latitude = latitude;
       this.longitude = longitude;
    }

    // Getters
    /***
     * Gets the latitude value of the GPS point
     * @return (float) - latitude value
     */
    public double getLatitude() {
        return this.latitude;
    }

    /***
     * Gets the longitude value of the GPS point
     * @return (float) - longitude value
     */
    public double getLongitude() {
        return this.longitude;
    }
}
