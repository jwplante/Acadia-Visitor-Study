package com.example.acadiavisitorstudy;

import android.location.Location;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class GeofenceTest {

    Location testPt1 = new Location("null");
    CircularGeofence geofence1 = new CircularGeofence(40.0f, 60.0f, 1.0f);

    public GeofenceTest(){
        testPt1.setLatitude(0.00);
        testPt1.setLongitude(0.00);
    }

    @Test
    public void outsideLocation() {
        assertFalse(geofence1.withinGeofence(testPt1));
    }

}
