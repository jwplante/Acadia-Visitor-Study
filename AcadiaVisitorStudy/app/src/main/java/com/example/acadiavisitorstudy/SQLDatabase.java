package com.example.acadiavisitorstudy;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

public class SQLDatabase implements ILocationProcessor{

    SQLDatabase(){}

    private static final String TAG = "SQLDatabase";

    /***
     * Uploads all of the cached data points to a SQL Server.
     * @return
     */
    public boolean processDataPoints(ArrayList<Location> locations){
       // Currently a stub right now to test the logic.
       for (Location l : locations) {
           Log.d(TAG, "Location point: " + LocationHelper.getLocationText(l) + "has been uploaded to the server.");
       }
       return true;
    }
}
