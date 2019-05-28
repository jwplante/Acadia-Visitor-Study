package com.example.acadiavisitorstudy;

import android.location.Location;

import java.util.ArrayList;

/*
 * This provides the interface for how the locations will be processed externally.
 * regardless of how it is modified in the future.
 */
public interface ILocationProcessor {

    /***
     * Should return true if the GPS points have been processed.
     * False if not.
     * @return
     */
    boolean processDataPoints(ArrayList<Location> locations);

}
